package com.dwarfeng.projwiz.core.control;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.dwarfeng.dutil.basic.cna.model.SyncListModel;
import com.dwarfeng.dutil.basic.cna.model.SyncMapModel;
import com.dwarfeng.dutil.basic.cna.model.SyncReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.SyncSetModel;
import com.dwarfeng.projwiz.core.model.cm.Tree;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.model.eum.LoggerStringKey;
import com.dwarfeng.projwiz.core.model.struct.Component;
import com.dwarfeng.projwiz.core.model.struct.Editor;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.ProjectProcessor;
import com.dwarfeng.projwiz.core.util.ProjectFileUtil;
import com.dwarfeng.projwiz.core.view.eum.DialogMessage;
import com.dwarfeng.projwiz.core.view.struct.ComponentChooserSetting;
import com.dwarfeng.projwiz.core.view.struct.MessageDialogSetting;

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

		for (ProjectProcessor processor : projWizard.getToolkit().getComponentModel().getSubs(ProjectProcessor.class)) {
			if (processor.isOpenProjectSupported()) {
				emptyFlag = false;
				break;
			}
		}

		if (emptyFlag) {
			projWizard.getToolkit()
					.showMessageDialog(new MessageDialogSetting.Builder().setMessage(label(LabelStringKey.MSGDIA_8))
							.setTitle(label(LabelStringKey.MSGDIA_9))
							.setDialogMessage(DialogMessage.INFORMATION_MESSAGE).build());
		}

		Component[] components = projWizard.getToolkit().chooseComponent(
				new ComponentChooserSetting.Builder().setMultiSelectionEnabled(false).setComponentFilter(component -> {
					if (!(component instanceof ProjectProcessor)) {
						return false;
					}
					return ((ProjectProcessor) component).isOpenProjectSupported();
				}).build());
		if (components.length == 0) {
			return;
		}
		ProjectProcessor processor = (ProjectProcessor) components[0];

		Project openedProject = processor.openProject();
		if (Objects.isNull(openedProject)) {
			return;
		}

		// 判断openedProject是否与已经打开的工程重名。
		openedProject.getLock().readLock().lock();
		try {
			String openedProjectName = openedProject.getName();
			if (isNameAlreadyExists(openedProjectName)) {
				projWizard.getToolkit()
						.showMessageDialog(new MessageDialogSetting.Builder()
								.setMessage(formatLabel(LabelStringKey.MSGDIA_30, openedProjectName, openedProjectName))
								.setTitle(label(LabelStringKey.MSGDIA_31))
								.setDialogMessage(DialogMessage.WARNING_MESSAGE).build());
				return;
			}
		} finally {
			openedProject.getLock().readLock().unlock();
		}

		// 判断openedProject中是否有重名文件。
		File repeditionFile = checkNameRepetition(openedProject, openedProject.getFileTree());
		if (Objects.nonNull(repeditionFile)) {
			projWizard.getToolkit().showMessageDialog(new MessageDialogSetting.Builder()
					.setMessage(formatLabel(LabelStringKey.MSGDIA_36,
							ProjectFileUtil.getStdPath(openedProject, repeditionFile)))
					.setTitle(label(LabelStringKey.MSGDIA_31)).setDialogMessage(DialogMessage.WARNING_MESSAGE).build());
			return;
		}

		info(LoggerStringKey.TASK_OPENPROJECT_0);
		formatInfo(LoggerStringKey.TASK_OPENPROJECT_1, openedProject.getProcessorClass().getName(),
				openedProject.getName(), openedProject.getClass().toString());

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

	private File checkNameRepetition(Project project, Tree<? extends File> fileTree) {
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
			if (nameSet.contains(project.getFileName(file))) {
				return fileTree.getRoot();
			} else {
				nameSet.add(project.getFileName(file));
			}
		}

		// 如果 fileTree 的根元素中的子元素不重名，则判断子元素的子树是否有重名，返回重名的那个根节点。
		for (File file : fileTree.getChilds(fileTree.getRoot())) {
			Tree<? extends File> subTree = fileTree.subTree(file);
			File repetition = null;
			if (Objects.nonNull(repetition = checkNameRepetition(project, subTree))) {
				return repetition;
			}
		}

		// 如果子树中也没有重名元素，则返回 null。
		return null;
	}

}
