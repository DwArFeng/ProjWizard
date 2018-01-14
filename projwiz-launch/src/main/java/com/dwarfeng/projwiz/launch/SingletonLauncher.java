package com.dwarfeng.projwiz.launch;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.dutil.basic.prog.ProgramObverser;
import com.dwarfeng.dutil.basic.prog.RuntimeState;
import com.dwarfeng.dutil.develop.backgr.AbstractTask;
import com.dwarfeng.dutil.develop.backgr.Task;
import com.dwarfeng.projwiz.core.control.ProjWizard;
import com.dwarfeng.projwiz.core.model.struct.Toolkit.BackgroundType;

/**
 * 单例启动器。
 * <p>
 * 该启动器创建一个 {@link Mh4w} 实例，并启动； 当实例运行结束时，虚拟机随即退出。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class SingletonLauncher {

	public static final SingletonLauncher INSTANCE = new SingletonLauncher();

	/**
	 * 新实例。
	 */
	private SingletonLauncher() {
		projWizard = new ProjWizard();
		projWizard.getToolkit().addProgramObverser(programObverser);
	}

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

	/**
	 * 启动程序。
	 * <p>
	 * 该方法以异步的方式启动程序。
	 */
	public void launch() {
		Task task = new AbstractTask() {

			@Override
			protected void todo() throws Exception {
				try {
					projWizard.getToolkit().start();
				} catch (ProcessException e) {
					e.printStackTrace();
					exitFlag = true;
					exitCode = 12450;
				}
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
	public void waitFinish() throws InterruptedException {
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