package com.dwarfeng.projwiz.launch;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 * 对程序的单例启动器进行最小化启动测试。
 * 
 * @author DwArFeng
 * @since 1.8
 */
public class Test_BasicLauncher {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
		} catch (UnsupportedLookAndFeelException ignore) {
			// 界面中的所有元素均支持这一外观，因此不可能出现异常。
		}

		try {
			BasicLauncher launcher = new BasicLauncher("test_case=false", "cfg_force_reset=true");
			launcher.launch();
			launcher.awaitFinish();
			System.exit(launcher.getExitCode());
		} catch (InterruptedException ignore) {
			// 中断也要按照基本法。
		}

	}

}
