package com.dwarfeng.projwiz.core.control;

import java.util.Objects;

import com.dwarfeng.dutil.basic.cna.model.SyncListModel;
import com.dwarfeng.dutil.basic.cna.model.SyncMapModel;
import com.dwarfeng.dutil.basic.cna.model.SyncReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.SyncSetModel;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.model.eum.LoggerStringKey;
import com.dwarfeng.projwiz.core.model.struct.Component;
import com.dwarfeng.projwiz.core.model.struct.Editor;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.ProjectProcessor;
import com.dwarfeng.projwiz.core.view.eum.DialogMessage;
import com.dwarfeng.projwiz.core.view.struct.ComponentChooserSetting;
import com.dwarfeng.projwiz.core.view.struct.MessageDialogSetting;

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

		for (ProjectProcessor processor : projWizard.getToolkit().getComponentModel().getSubs(ProjectProcessor.class)) {
			if (processor.isSaveProjectSupported()) {
				emptyFlag = false;
				break;
			}
		}

		if (emptyFlag) {
			projWizard.getToolkit()
					.showMessageDialog(new MessageDialogSetting.Builder().setMessage(label(LabelStringKey.MSGDIA_26))
							.setTitle(label(LabelStringKey.MSGDIA_27))
							.setDialogMessage(DialogMessage.INFORMATION_MESSAGE).build());
		}

		Component[] components = projWizard.getToolkit().chooseComponent(
				new ComponentChooserSetting.Builder().setMultiSelectionEnabled(false).setComponentFilter(component -> {
					if (!(component instanceof ProjectProcessor)) {
						return false;
					}
					return ((ProjectProcessor) component).isSaveProjectSupported();
				}).build());
		if (components.length == 0) {
			return;
		}
		ProjectProcessor processor = (ProjectProcessor) components[0];

		final Project focusProject;

		focusProject = focusProjectModel.get();

		if (Objects.isNull(focusProject))
			return;

		Project savedAsProject = null;
		try {
			savedAsProject = processor.saveProject(focusProject);
		} catch (ProcessException e) {
			projWizard.getToolkit()
					.showMessageDialog(new MessageDialogSetting.Builder().setMessage(label(LabelStringKey.MSGDIA_28))
							.setTitle(label(LabelStringKey.MSGDIA_29)).setDialogMessage(DialogMessage.WARNING_MESSAGE)
							.build());
			warn(LoggerStringKey.TASK_SAVEASPROJECT_0);
			formatWarn(LoggerStringKey.TASK_SAVEASPROJECT_1, focusProject.getProcessorClass().getName(),
					focusProject.getName(), focusProject.getClass().toString());
			warn(LoggerStringKey.TASK_SAVEASPROJECT_2, e);
			return;
		}

		if (Objects.isNull(savedAsProject)) {
			return;
		}

		// 判断savedAsProject是否与已经打开的工程重名。
		String savedAsProjectName = savedAsProject.getName();
		if (isNameAlreadyExists(savedAsProjectName)) {
			projWizard.getToolkit().showMessageDialog(new MessageDialogSetting.Builder()
					.setMessage(formatLabel(LabelStringKey.MSGDIA_34, savedAsProjectName, savedAsProjectName))
					.setTitle(label(LabelStringKey.MSGDIA_35)).setDialogMessage(DialogMessage.WARNING_MESSAGE).build());
			return;
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
