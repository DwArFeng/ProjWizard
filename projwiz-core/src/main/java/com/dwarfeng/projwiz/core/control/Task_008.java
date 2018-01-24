package com.dwarfeng.projwiz.core.control;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import com.dwarfeng.dutil.basic.cna.model.SyncListModel;
import com.dwarfeng.dutil.basic.cna.model.SyncMapModel;
import com.dwarfeng.dutil.basic.cna.model.SyncReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.SyncSetModel;
import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.projwiz.core.model.cm.Tree;
import com.dwarfeng.projwiz.core.model.eum.DialogMessage;
import com.dwarfeng.projwiz.core.model.eum.DialogOption;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.model.eum.LoggerStringKey;
import com.dwarfeng.projwiz.core.model.struct.Editor;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.ProjectProcessor;
import com.dwarfeng.projwiz.core.util.FileUtil;
import com.dwarfeng.projwiz.core.view.gui.ComponentSelectDialog;

final class OpenProjectTask extends ProjWizTask {

	public OpenProjectTask(ProjWizard projWizard) {
		super(projWizard);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		SyncReferenceModel<File> anchorFileModel = projWizard.getToolkit().getAnchorFileModel();
		SyncMapModel<Project, Editor> focusEditorModel = projWizard.getToolkit().getFocusEditorModel();
		SyncSetModel<File> focusFileModel = projWizard.getToolkit().getFocusFileModel();
		SyncReferenceModel<Project> focusProjectModel = projWizard.getToolkit().getFocusProjectModel();
		SyncListModel<Project> holdProjectModel = projWizard.getToolkit().getHoldProjectModel();

		boolean emptyFlag = true;

		for (ProjectProcessor processor : projWizard.getToolkit().getComponentModel().getAll(ProjectProcessor.class)) {
			if (processor.isOpenProjectSupported()) {
				emptyFlag = false;
				break;
			}
		}

		if (emptyFlag) {
			projWizard.getToolkit().showMessageDialog(label(LabelStringKey.MSGDIA_8), label(LabelStringKey.MSGDIA_9),
					DialogMessage.INFORMATION_MESSAGE, null);
		}

		AtomicReference<ComponentSelectDialog> dialogRef = new AtomicReference<>();

		SwingUtil.invokeAndWaitInEventQueue(() -> {
			dialogRef.set(new ComponentSelectDialog(projWizard.getToolkit().getGuiManager(),
					projWizard.getToolkit().getLabelI18nHandler(), projWizard.getToolkit().getMainFrame(),
					projWizard.getToolkit().getComponentModel(), component -> {
						if (!(component instanceof ProjectProcessor)) {
							return false;
						}
						return ((ProjectProcessor) component).isOpenProjectSupported();
					}));
		});

		projWizard.getToolkit().showExternalWindow(dialogRef.get());
		dialogRef.get().waitDispose();

		if (dialogRef.get().getOption() != DialogOption.OK_YES) {
			return;
		}

		ProjectProcessor processor = dialogRef.get().getCurrentComponent(ProjectProcessor.class);
		if (Objects.isNull(processor)) {
			return;
		}

		Project openedProject = processor.openProject();
		if (Objects.isNull(openedProject)) {
			return;
		}

		// 判断openedProject是否与已经打开的工程重名。
		openedProject.getLock().readLock().lock();
		try {
			String openedProjectName = openedProject.getName();
			if (isNameAlreadyExists(openedProjectName)) {
				projWizard.getToolkit().showMessageDialog(
						formatLabel(LabelStringKey.MSGDIA_30, openedProjectName, openedProjectName),
						label(LabelStringKey.MSGDIA_31), DialogMessage.WARNING_MESSAGE);
				return;
			}
		} finally {
			openedProject.getLock().readLock().unlock();
		}

		// 判断openedProject中是否有重名文件。
		File repeditionFile = checkNameRepetition(openedProject.getFileTree());
		if (Objects.nonNull(repeditionFile)) {
			projWizard.getToolkit().showMessageDialog(
					formatLabel(LabelStringKey.MSGDIA_36, FileUtil.getStdPath(openedProject, repeditionFile)),
					label(LabelStringKey.MSGDIA_31), DialogMessage.WARNING_MESSAGE);
			return;
		}

		SyncMapModel<String, Project> projectIndicateModel = projWizard.getToolkit().getProjectIndicateModel();
		SyncMapModel<String, File> fileIndicateModel = projWizard.getToolkit().getFileIndicateModel();

		openedProject.getLock().readLock().lock();
		try {
			projectIndicateModel.put(openedProject.getUniqueLabel(), openedProject);
			openedProject.getFileTree().forEach(file -> {
				fileIndicateModel.put(file.getUniqueLabel(), file);
			});
		} finally {
			openedProject.getLock().readLock().unlock();
		}

		info(LoggerStringKey.TASK_OPENPROJECT_0);
		formatInfo(LoggerStringKey.TASK_OPENPROJECT_1, openedProject.getRegisterKey(), openedProject.getName(),
				openedProject.getClass().toString());

		anchorFileModel.getLock().writeLock().lock();
		focusEditorModel.getLock().writeLock().lock();
		focusFileModel.getLock().writeLock().lock();
		focusProjectModel.getLock().writeLock().lock();
		holdProjectModel.getLock().writeLock().lock();
		try {
			anchorFileModel.clear();
			focusFileModel.clear();
			focusEditorModel.put(openedProject, null);
			holdProjectModel.add(openedProject);
			focusProjectModel.set(openedProject);
		} finally {
			holdProjectModel.getLock().writeLock().unlock();
			focusProjectModel.getLock().writeLock().unlock();
			focusFileModel.getLock().writeLock().unlock();
			focusEditorModel.getLock().writeLock().unlock();
			anchorFileModel.getLock().writeLock().unlock();
		}

	}

	private boolean isNameAlreadyExists(String name) {
		SyncListModel<Project> holdProjectModel = projWizard.getToolkit().getHoldProjectModel();

		holdProjectModel.getLock().readLock().lock();
		try {
			for (Project project : holdProjectModel) {
				String nameAlreadyExists = project.getName();
				if (Objects.equals(name, nameAlreadyExists)) {
					return true;
				}
			}
		} finally {
			holdProjectModel.getLock().readLock().unlock();
		}
		return false;
	}

	private File checkNameRepetition(Tree<? extends File> fileTree) {
		// 如果 fileTree 是 null 的话，直接返回 null。
		if (Objects.isNull(fileTree)) {
			return null;
		}

		// 如果 fileTree 的根节点是叶节点的话，直接返回 null。
		if (fileTree.isLeaf(fileTree.getRoot())) {
			return null;
		}

		// 如果fileTree的根元素中含有子元素，判断子元素是否有重名，有的话，返回 fileTree 的根元素。
		Set<String> nameSet = new HashSet<>();
		for (File file : fileTree.getChilds(fileTree.getRoot())) {
			if (nameSet.contains(file.getName())) {
				return fileTree.getRoot();
			} else {
				nameSet.add(file.getName());
			}
		}

		// 如果 fileTree 的根元素中的子元素不重名，则判断子元素的子树是否有重名，返回重名的那个根节点。
		for (File file : fileTree.getChilds(fileTree.getRoot())) {
			Tree<? extends File> subTree = fileTree.subTree(file);
			File repetition = null;
			if (Objects.nonNull(repetition = checkNameRepetition(subTree))) {
				return repetition;
			}
		}

		// 如果子树中也没有重名元素，则返回 null。
		return null;
	}

}
