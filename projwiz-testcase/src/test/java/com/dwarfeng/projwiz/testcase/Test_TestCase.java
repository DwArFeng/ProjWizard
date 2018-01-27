package com.dwarfeng.projwiz.testcase;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dwarfeng.projwiz.core.control.ProjWizard;

public class Test_TestCase {

	private static ProjWizard projWizard;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void test() {
		projWizard = new ProjWizard("test_case=true");
		projWizard.getToolkit().start();
		projWizard.getToolkit().exit();
		assertEquals(0, projWizard.getToolkit().getExitCode());
	}

}
