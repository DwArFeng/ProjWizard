package com.dwarfeng.projwiz.core.control;

import java.util.Objects;

import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.dutil.develop.backgr.Task;
import com.dwarfeng.projwiz.core.model.eum.LoggerStringKey;

final class StartTask extends ProjWizTask {

	public StartTask(ProjWizard projWizard) {
		super(projWizard);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		try {
			projWizard.getToolkit().start();
		} catch (ProcessException e) {
			warn(LoggerStringKey.TASK_START_0);
			warn(LoggerStringKey.TASK_START_1, e);
		}

	}
}

final class TryExitTask extends ProjWizTask {

	public TryExitTask(ProjWizard projWizard) {
		super(projWizard);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		try {
			Task task = new TryCloseAllProjectTask(projWizard);
			task.run();
			if (Objects.nonNull(task.getThrowable())) {
				throw (Exception) task.getThrowable();
			}

			if (!projWizard.getToolkit().getHoldProjectModel().isEmpty()) {
				return;
			}

			projWizard.getToolkit().exit();
		} catch (ProcessException e) {
			warn(LoggerStringKey.TASK_EXIT_0);
			warn(LoggerStringKey.TASK_EXIT_1, e);
		}

	}

}

final class ForceExitTask extends ProjWizTask {

	public ForceExitTask(ProjWizard projWizard) {
		super(projWizard);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		try {
			projWizard.getToolkit().exit();
		} catch (ProcessException e) {
			warn(LoggerStringKey.TASK_EXIT_0);
			warn(LoggerStringKey.TASK_EXIT_1, e);
		}

	}
}