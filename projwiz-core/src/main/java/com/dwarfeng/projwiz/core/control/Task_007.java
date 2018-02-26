package com.dwarfeng.projwiz.core.control;

import com.dwarfeng.dutil.basic.cna.model.SyncMapModel;
import com.dwarfeng.dutil.basic.cna.model.SyncReferenceModel;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.projwiz.core.model.eum.DialogMessage;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.model.eum.LoggerStringKey;
import com.dwarfeng.projwiz.core.model.struct.Editor;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.view.struct.MessageDialogSetting;

final class SaveFocusEditorTask extends ProjWizTask {

	public SaveFocusEditorTask(ProjWizard projWizard) {
		super(projWizard);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		SyncMapModel<Project, Editor> focusEditorModel = projWizard.getToolkit().getFocusEditorModel();
		SyncReferenceModel<Project> focusProjectModel = projWizard.getToolkit().getFocusProjectModel();

		final Project focusProject;
		final Editor focusEditor;

		focusEditorModel.getLock().writeLock().lock();
		focusProjectModel.getLock().writeLock().lock();
		try {
			focusProject = focusProjectModel.get();
			focusEditor = focusEditorModel.get(focusProject);
		} finally {
			focusProjectModel.getLock().writeLock().unlock();
			focusEditorModel.getLock().writeLock().unlock();
		}

		if (!focusEditor.isSaveSupported()) {
			projWizard.getToolkit()
					.showMessageDialog(new MessageDialogSetting.Builder().setMessage(label(LabelStringKey.MSGDIA_17))
							.setTitle(label(LabelStringKey.MSGDIA_19))
							.setDialogMessage(DialogMessage.INFORMATION_MESSAGE).build());
			return;
		}

		try {
			focusEditor.save();
			info(LoggerStringKey.TASK_SAVEEDITOR_1);
			info(LoggerStringKey.TASK_SAVEEDITOR_2);
			formatInfo(LoggerStringKey.TASK_SAVEEDITOR_3, focusEditor.getEditProject().getName(),
					focusEditor.getEditProject().getFileName(focusEditor.getEditFile()));
		} catch (ProcessException e) {
			projWizard.getToolkit()
					.showMessageDialog(new MessageDialogSetting.Builder().setMessage(label(LabelStringKey.MSGDIA_20))
							.setTitle(label(LabelStringKey.MSGDIA_21)).setDialogMessage(DialogMessage.WARNING_MESSAGE)
							.build());
			warn(LoggerStringKey.TASK_SAVEEDITOR_0, e);
		}

	}
}

final class SaveAllEditorTask extends ProjWizTask {

	public SaveAllEditorTask(ProjWizard projWizard) {
		super(projWizard);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		// TODO 保存全部编辑的动作——待完成。
	}
}