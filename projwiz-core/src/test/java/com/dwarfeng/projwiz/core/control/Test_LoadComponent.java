package com.dwarfeng.projwiz.core.control;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.SyncMapModel;
import com.dwarfeng.dutil.basic.io.CT;
import com.dwarfeng.projwiz.core.model.cm.SyncComponentModel;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;
import com.dwarfeng.projwiz.core.model.struct.Toolkit.Method;

public class Test_LoadComponent {

	private static ProjWizard projWizard;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		projWizard = new ProjWizard("test_case=true", "cfg_force_reset=true",
				"cfg_list_path=/com/dwarfeng/projwiz/resources/core/cfg-list-test0.xml", "cfg_list_path_type=INJAR");
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
		SyncComponentModel componentModel = projWizard.getToolkit().getComponentModel();
		assertEquals(2, componentModel.size());
		assertFalse(componentModel.containsKey("com.dwarfeng.projwiz.test.Test0"));
		assertFalse(componentModel.containsKey(null));
		assertTrue(componentModel.containsKey("com.dwarfeng.projwiz.test.Test1"));
		assertTrue(componentModel.containsKey("com.dwarfeng.projwiz.test.Test2"));

		SyncMapModel<String, ReferenceModel<Toolkit>> cmpoentToolkitModel = projWizard.getToolkit()
				.getCmpoentToolkitModel();
		CT.trace(Arrays.toString(cmpoentToolkitModel.keySet().toArray()));
		assertEquals(2, cmpoentToolkitModel.size());
		assertFalse(cmpoentToolkitModel.containsKey("com.dwarfeng.projwiz.test.Test0"));
		assertTrue(cmpoentToolkitModel.containsKey("com.dwarfeng.projwiz.test.Test1"));
		assertTrue(cmpoentToolkitModel.containsKey("com.dwarfeng.projwiz.test.Test2"));
		assertNotNull(cmpoentToolkitModel.get("com.dwarfeng.projwiz.test.Test1").get());
		assertNotNull(cmpoentToolkitModel.get("com.dwarfeng.projwiz.test.Test2").get());

		Toolkit toolkit1 = cmpoentToolkitModel.get("com.dwarfeng.projwiz.test.Test1").get();
		assertTrue(toolkit1.hasPermission(Method.INFO));
		assertTrue(toolkit1.hasPermission(Method.WARN));
		assertFalse(toolkit1.hasPermission(Method.ERROR));
		assertFalse(toolkit1.hasPermission(Method.FATAL));
		
		Toolkit toolkit2 = cmpoentToolkitModel.get("com.dwarfeng.projwiz.test.Test2").get();
		assertTrue(toolkit2.hasPermission(Method.INFO));
		assertTrue(toolkit2.hasPermission(Method.WARN));
		assertTrue(toolkit2.hasPermission(Method.ERROR));
		assertFalse(toolkit2.hasPermission(Method.FATAL));
	}

}
