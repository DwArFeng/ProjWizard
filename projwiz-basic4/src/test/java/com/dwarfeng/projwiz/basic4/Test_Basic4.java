package com.dwarfeng.projwiz.basic4;

import com.dwarfeng.projwiz.core.control.ProjWizard;

public class Test_Basic4 {

	private static ProjWizard projWizard;

	public static void main(String[] args) {
		projWizard = new ProjWizard("test_case=false", "cfg_force_reset=true",
				"cfg_list_path=/com/dwarfeng/progwiz/resources/basic4/cfg-list-test0.xml", "cfg_list_path_type=INJAR");
		projWizard.getToolkit().start();
	}

}
