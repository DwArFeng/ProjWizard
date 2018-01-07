package com.dwarfeng.projwiz.core.control;

import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.projwiz.core.model.eum.DialogMessage;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.model.eum.LoggerStringKey;
import com.dwarfeng.projwiz.core.model.struct.Project;

final class SaveFocusProjectTask extends ProjWizTask {

	public SaveFocusProjectTask(ProjWizard projWizard) {
		super(projWizard);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		final Project focusProject = projWizard.getToolkit().getFocusProjectModel().get();

		if (!focusProject.isSaveSupported()) {
			return;
		}

		try {
			focusProject.save();
		} catch (ProcessException e) {
			projWizard.getToolkit().showMessageDialog(label(LabelStringKey.MSGDIA_22), label(LabelStringKey.MSGDIA_23),
					DialogMessage.WARNING_MESSAGE);
			warn(LoggerStringKey.TASK_SAVEPROJECT_0);
			formatWarn(LoggerStringKey.TASK_SAVEPROJECT_1, focusProject.getRegisterKey(), focusProject.getName(),
					focusProject.getClass().toString());
			warn(LoggerStringKey.TASK_SAVEPROJECT_2, e);
			return;
		}

	}
}

final class SaveAllProjectTask extends ProjWizTask {

	public SaveAllProjectTask(ProjWizard projWizard) {
		super(projWizard);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		// TODO
	}
}
