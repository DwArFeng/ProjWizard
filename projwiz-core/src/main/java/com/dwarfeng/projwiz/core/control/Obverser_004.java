package com.dwarfeng.projwiz.core.control;

import com.dwarfeng.dutil.develop.backgr.Task;
import com.dwarfeng.dutil.develop.backgr.obv.BackgroundObverser;
import com.dwarfeng.projwiz.core.model.struct.Toolkit.BackgroundType;

final class BackgroundObverserImpl extends ProjWizObverser implements BackgroundObverser {

	public BackgroundObverserImpl(ProjWizard projWizard) {
		super(projWizard);
	}

	@Override
	public void fireTaskSubmitted(Task task) {
	}

	@Override
	public void fireTaskStarted(Task task) {
	}

	@Override
	public void fireTaskFinished(Task task) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fireTaskRemoved(Task task) {
		if (task instanceof ThrowableReportTask)
			return;
		if (task.getThrowable() == null)
			return;

		projWizard.getToolkit().submitTask(new ThrowableReportTask(projWizard, task), BackgroundType.CONCURRENT);
	}

	@Override
	public void fireShutDown() {
	}

	@Override
	public void fireTerminated() {
	}

}