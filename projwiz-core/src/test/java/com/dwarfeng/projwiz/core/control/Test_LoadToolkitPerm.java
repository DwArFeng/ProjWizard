package com.dwarfeng.projwiz.core.control;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dwarfeng.projwiz.core.control.ProjWizard;
import com.dwarfeng.projwiz.core.model.cm.ToolkitPermModel;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;

public class Test_LoadToolkitPerm {

	private static ProjWizard projWizard;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		projWizard = new ProjWizard("test_case=true");
		projWizard.getToolkit().start();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		projWizard.getToolkit().exit();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void test() {
		ToolkitPermModel model = projWizard.getToolkit().getToolkitPermModel();
		assertEquals(0, model.getDpl());
		assertEquals(1000, model.getPermLevel(Toolkit.Method.START));
	}

}
