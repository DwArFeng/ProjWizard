package com.dwarfeng.projwiz.basic4;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import com.dwarfeng.projwiz.core.control.ProjWizard;

/**
 * Basic4组件包的手动测试类。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public class Test_Basic4 {

	private static ProjWizard projWizard;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		projWizard = new ProjWizard("test_case=false", "cfg_force_reset=true",
				"cfg_list_path=/com/dwarfeng/progwiz/resources/basic4/cfg-list-test0.xml", "cfg_list_path_type=INJAR");
		projWizard.getToolkit().start();
	}

}
