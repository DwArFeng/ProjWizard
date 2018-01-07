package com.dwarfeng.projwiz.core.control;

import java.util.Objects;

import com.dwarfeng.dutil.develop.backgr.Task;
import com.dwarfeng.projwiz.core.model.eum.LoggerStringKey;

final class ThrowableReportTask extends ProjWizTask {

	private final Task task;

	public ThrowableReportTask(ProjWizard projWizard, Task task) {
		super(projWizard);

		Objects.requireNonNull(task, "入口参数 task 不能为 null。");
		this.task = task;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		if (task.getThrowable() != null) {
			warn(LoggerStringKey.TASK_THROABLEREPORT_0);
			formatWarn(LoggerStringKey.TASK_THROABLEREPORT_1, task.toString(), task.getClass().toString());
			warn(LoggerStringKey.TASK_THROABLEREPORT_2, task.getThrowable());
		}
	}

}
