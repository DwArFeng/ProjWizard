package com.dwarfeng.projwiz.core.control;

import java.util.Objects;

import com.dwarfeng.dutil.basic.cna.model.SyncListModel;
import com.dwarfeng.dutil.basic.cna.model.SyncMapModel;
import com.dwarfeng.dutil.basic.cna.model.SyncReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.SyncSetModel;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.model.eum.LoggerStringKey;
import com.dwarfeng.projwiz.core.model.struct.Module;
import com.dwarfeng.projwiz.core.model.struct.Editor;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.ProjectProcessor;
import com.dwarfeng.projwiz.core.view.eum.DialogMessage;
import com.dwarfeng.projwiz.core.view.struct.ModuleChooserSetting;
import com.dwarfeng.projwiz.core.view.struct.MessageDialogSetting;

final class NewProjectTask extends ProjWizTask {

	public NewProjectTask(ProjWizard projWizard) {
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

		for (ProjectProcessor processor : projWizard.getToolkit().getModuleModel().getSubs(ProjectProcessor.class)) {
			if (processor.isNewProjectSupported()) {
				emptyFlag = false;
				break;
			}
		}

		if (emptyFlag) {
			projWizard.getToolkit()
					.showMessageDialog(new MessageDialogSetting.Builder().setMessage(label(LabelStringKey.MSGDIA_1))
							.setTitle(label(LabelStringKey.MSGDIA_2))
							.setDialogMessage(DialogMessage.INFORMATION_MESSAGE).build());
		}

		Module[] modules = projWizard.getToolkit().chooseModule(
				new ModuleChooserSetting.Builder().setMultiSelectionEnabled(false).setModuleFilter(module -> {
					if (!(module instanceof ProjectProcessor)) {
						return false;
					}
					return ((ProjectProcessor) module).isNewProjectSupported();
				}).build());
		if (modules.length == 0) {
			return;
		}
		ProjectProcessor processor = (ProjectProcessor) modules[0];

		Project project = null;

		try {
			project = processor.newProject();
		} catch (ProcessException e) {
			warn(LoggerStringKey.TASK_NEWPROJECT_0);
			formatWarn(LoggerStringKey.TASK_NEWPROJECT_1, processor.getClass().getName(),
					processor.getClass().toString());
			warn(LoggerStringKey.TASK_NEWPROJECT_2, e);
			return;
		}

		if (Objects.isNull(project)) {
			return;
		}

		// 判断project是否与已经打开的工程重名。TODO
		// 还要判断工程名称是否合法，而且在名称重复和不合法的时候要循环询问，直到合法或用户取消。
		String projectName = project.getName();
		if (isNameAlreadyExists(projectName)) {
			projWizard.getToolkit().showMessageDialog(new MessageDialogSetting.Builder()
					.setMessage(formatLabel(LabelStringKey.MSGDIA_32, projectName, projectName))
					.setTitle(label(LabelStringKey.MSGDIA_33)).setDialogMessage(DialogMessage.WARNING_MESSAGE).build());
			return;
		}

		info(LoggerStringKey.TASK_NEWPROJECT_3);
		formatInfo(LoggerStringKey.TASK_NEWPROJECT_1, processor.getClass().getName(), processor.getClass().toString());
		info(LoggerStringKey.TASK_NEWPROJECT_5);
		formatInfo(LoggerStringKey.TASK_NEWPROJECT_6, project.getProcessorClass().getName(), project.getName(),
				project.getClass().toString());

		anchorFileModel.getLock().writeLock().lock();
		focusEditorModel.getLock().writeLock().lock();
		focusFileModel.getLock().writeLock().lock();
		focusProjectModel.getLock().writeLock().lock();
		holdProjectModel.getLock().writeLock().lock();
		try {
			holdProjectModel.add(project);
			focusProjectModel.set(project);
			anchorFileModel.clear();
			focusFileModel.clear();
			focusEditorModel.put(project, null);
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
