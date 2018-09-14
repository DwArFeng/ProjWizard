package com.dwarfeng.projwiz.launch;

import java.util.Objects;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.dwarfeng.dutil.basic.prog.ProgramObverser;
import com.dwarfeng.dutil.basic.prog.RuntimeState;
import com.dwarfeng.dutil.develop.backgr.AbstractTask;
import com.dwarfeng.dutil.develop.backgr.Task;
import com.dwarfeng.projwiz.core.control.ProjWizard;
import com.dwarfeng.projwiz.core.model.struct.Toolkit.BackgroundType;

public class BasicLauncher {

	private final ProjWizard projWizard;

	private final Lock lock = new ReentrantLock();
	private final Condition condition = lock.newCondition();
	private final ProgramObverser programObverser = new ProgramObverser() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireRuntimeStateChanged(RuntimeState oldValue, RuntimeState newValue) {
			if (newValue.equals(RuntimeState.ENDED)) {
				exitCode = projWizard.getToolkit().getExitCode();
				exitFlag = true;
				lock.lock();
				try {
					condition.signalAll();
				} finally {
					lock.unlock();
				}
			}
		}
	};

	private boolean exitFlag = false;

	private int exitCode = 0;

	public BasicLauncher() {
		projWizard = new ProjWizard();
		projWizard.getToolkit().addProgramObverser(programObverser);
	}

	public BasicLauncher(String... args) throws NullPointerException {
		Objects.requireNonNull(args, "入口参数 args 不能为 null。");
		projWizard = new ProjWizard(args);
		projWizard.getToolkit().addProgramObverser(programObverser);
	}

	/**
	 * 启动程序。
	 * <p>
	 * 该方法以异步的方式启动程序。
	 */
	public void launch() {
		Task task = new AbstractTask() {

			@Override
			protected void todo() throws Exception {
				projWizard.getToolkit().start();
			}
		};

		projWizard.getToolkit().submitTask(task, BackgroundType.CONCURRENT);
	}

	/**
	 * 等待程序运行结束。
	 * 
	 * @throws InterruptedException
	 *             等待过程中线程被外部中断。
	 */
	public void awaitFinish() throws InterruptedException {
		lock.lock();
		try {
			while (!exitFlag) {
				condition.await();
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 获取退出代码。
	 * 
	 * @return 退出代码。
	 */
	public int getExitCode() {
		return exitCode;
	}

}
