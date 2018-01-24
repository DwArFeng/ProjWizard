package com.dwarfeng.projwiz.core.control;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import com.dwarfeng.dutil.basic.cna.model.SyncListModel;
import com.dwarfeng.dutil.basic.cna.model.SyncMapModel;
import com.dwarfeng.dutil.basic.cna.model.SyncReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.SyncSetModel;
import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.projwiz.core.model.eum.DialogMessage;
import com.dwarfeng.projwiz.core.model.eum.DialogOption;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.model.eum.LoggerStringKey;
import com.dwarfeng.projwiz.core.model.struct.Editor;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.ProjectProcessor;
import com.dwarfeng.projwiz.core.view.gui.ComponentSelectDialog;

final class SaveAsFocusProjectTask extends ProjWizTask {

	public SaveAsFocusProjectTask(ProjWizard projWizard) {
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

		// 选择指定的工程处理器。
		boolean emptyFlag = true;

		for (ProjectProcessor processor : projWizard.getToolkit().getComponentModel().getAll(ProjectProcessor.class)) {
			if (processor.isSaveProjectSupported()) {
				emptyFlag = false;
				break;
			}
		}

		if (emptyFlag) {
			projWizard.getToolkit().showMessageDialog(label(LabelStringKey.MSGDIA_26), label(LabelStringKey.MSGDIA_27),
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
						return ((ProjectProcessor) component).isSaveProjectSupported();
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

		final Project focusProject;

		focusProject = focusProjectModel.get();

		if (Objects.isNull(focusProject))
			return;

		Project savedAsProject = null;
		try {
			savedAsProject = processor.saveProject(focusProject);
		} catch (ProcessException e) {
			projWizard.getToolkit().showMessageDialog(label(LabelStringKey.MSGDIA_28), label(LabelStringKey.MSGDIA_29),
					DialogMessage.WARNING_MESSAGE);
			warn(LoggerStringKey.TASK_SAVEASPROJECT_0);
			formatWarn(LoggerStringKey.TASK_SAVEASPROJECT_1, focusProject.getRegisterKey(), focusProject.getName(),
					focusProject.getClass().toString());
			warn(LoggerStringKey.TASK_SAVEASPROJECT_2, e);
			return;
		}

		if (Objects.isNull(savedAsProject)) {
			return;
		}

		// 判断savedAsProject是否与已经打开的工程重名。
		String savedAsProjectName = savedAsProject.getName();
		if (isNameAlreadyExists(savedAsProjectName)) {
			projWizard.getToolkit().showMessageDialog(
					formatLabel(LabelStringKey.MSGDIA_34, savedAsProjectName, savedAsProjectName),
					label(LabelStringKey.MSGDIA_35), DialogMessage.WARNING_MESSAGE);
			return;
		}

		SyncMapModel<String, Project> projectIndicateModel = projWizard.getToolkit().getProjectIndicateModel();
		SyncMapModel<String, File> fileIndicateModel = projWizard.getToolkit().getFileIndicateModel();

		savedAsProject.getLock().readLock().lock();
		try {
			projectIndicateModel.put(savedAsProject.getUniqueLabel(), savedAsProject);
			savedAsProject.getFileTree().forEach(file -> {
				fileIndicateModel.put(file.getUniqueLabel(), file);
			});
		} finally {
			savedAsProject.getLock().readLock().unlock();
		}

		// 将新的工程置为焦点工程。
		anchorFileModel.getLock().writeLock().lock();
		focusEditorModel.getLock().writeLock().lock();
		focusFileModel.getLock().writeLock().lock();
		focusProjectModel.getLock().writeLock().lock();
		holdProjectModel.getLock().writeLock().lock();
		try {
			anchorFileModel.clear();
			focusFileModel.clear();
			focusEditorModel.put(savedAsProject, null);
			holdProjectModel.add(savedAsProject);
			focusProjectModel.set(savedAsProject);
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
}
