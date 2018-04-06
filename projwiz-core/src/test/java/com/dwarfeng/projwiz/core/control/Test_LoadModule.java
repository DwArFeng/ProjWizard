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
import com.dwarfeng.projwiz.core.model.cm.SyncModuleModel;
import com.dwarfeng.projwiz.core.model.struct.Module;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;
import com.dwarfeng.projwiz.core.model.struct.Toolkit.Method;

public class Test_LoadModule {

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
		SyncModuleModel moduleModel = projWizard.getToolkit().getModuleModel();
		assertEquals(2, moduleModel.size());
		assertFalse(moduleModel.containsClass(TestModule1.class));
		assertFalse(moduleModel.containsClass(null));
		assertTrue(moduleModel.containsClass(TestModule2.class));
		assertTrue(moduleModel.containsClass(TestModule3.class));

		SyncMapModel<Class<? extends Module>, ReferenceModel<Toolkit>> moduleToolkitModel = projWizard.getToolkit()
				.getModuleToolkitModel();
		CT.trace(Arrays.toString(moduleToolkitModel.keySet().toArray()));
		assertEquals(2, moduleToolkitModel.size());
		assertFalse(moduleToolkitModel.containsKey(TestModule1.class));
		assertTrue(moduleToolkitModel.containsKey(TestModule2.class));
		assertTrue(moduleToolkitModel.containsKey(TestModule3.class));
		assertNotNull(moduleToolkitModel.get(TestModule2.class).get());
		assertNotNull(moduleToolkitModel.get(TestModule3.class).get());

		Toolkit toolkit1 = moduleToolkitModel.get(TestModule2.class).get();
		assertTrue(toolkit1.hasPermission(Method.INFO));
		assertTrue(toolkit1.hasPermission(Method.WARN));
		assertFalse(toolkit1.hasPermission(Method.ERROR));
		assertFalse(toolkit1.hasPermission(Method.FATAL));

		Toolkit toolkit2 = moduleToolkitModel.get(TestModule3.class).get();
		assertTrue(toolkit2.hasPermission(Method.INFO));
		assertTrue(toolkit2.hasPermission(Method.WARN));
		assertTrue(toolkit2.hasPermission(Method.ERROR));
		assertFalse(toolkit2.hasPermission(Method.FATAL));
	}

}
