package com.dwarfeng.projwiz.core.control;

import com.dwarfeng.dutil.basic.cna.model.SyncMapModel;
import com.dwarfeng.dutil.basic.cna.model.SyncReferenceModel;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.projwiz.core.model.eum.DialogMessage;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.model.eum.LoggerStringKey;
import com.dwarfeng.projwiz.core.model.struct.Editor;
import com.dwarfeng.projwiz.core.model.struct.Project;

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
			projWizard.getToolkit().showMessageDialog(label(LabelStringKey.MSGDIA_17), label(LabelStringKey.MSGDIA_19),
					DialogMessage.INFORMATION_MESSAGE);
			return;
		}

		try {
			focusEditor.save();
			info(LoggerStringKey.TASK_SAVEEDITOR_1);
			info(LoggerStringKey.TASK_SAVEEDITOR_2);
			formatInfo(LoggerStringKey.TASK_SAVEEDITOR_3, focusEditor.getEditProject().getName(),
					focusEditor.getEditFile().getName());
		} catch (ProcessException e) {
			projWizard.getToolkit().showMessageDialog(label(LabelStringKey.MSGDIA_20), label(LabelStringKey.MSGDIA_21),
					DialogMessage.WARNING_MESSAGE);
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