package com.dwarfeng.projwiz.basic4;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import com.dwarfeng.projwiz.launch.BasicLauncher;

/**
 * Basic4组件包的手动测试类。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public class Test_Basic4 {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		try {
			BasicLauncher launcher = new BasicLauncher("test_case=false", "cfg_force_reset=true",
					"cfg_list_path=/com/dwarfeng/progwiz/resources/basic4/cfg-list-test0.xml",
					"cfg_list_path_type=INJAR");
			launcher.launch();
			launcher.awaitFinish();
			System.exit(launcher.getExitCode());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
