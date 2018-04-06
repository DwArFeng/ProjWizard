package com.dwarfeng.projwiz.core.model.cm;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dwarfeng.dutil.basic.cna.model.obv.SetObverser;
import com.dwarfeng.projwiz.core.model.struct.Module;
import com.dwarfeng.projwiz.core.model.struct.TestSimpleModule;

public class Test_DefaultModuleModel {

	private static final class TestSetObverser implements SetObverser<Module> {

		public List<Module> addedList = new ArrayList<>();
		public List<Module> removedList = new ArrayList<>();
		public int cleared = 0;

		@Override
		public void fireAdded(Module modlue) {
			addedList.add(modlue);
		}

		@Override
		public void fireCleared() {
			cleared++;
		}

		@Override
		public void fireRemoved(Module modlue) {
			removedList.add(modlue);
		}

	}

	private static final Module MODULE_1 = new TestSimpleModule_Sub1();
	private static final Module MODULE_2 = new TestSimpleModule_Sub2();
	private static final Module MODULE_3 = new TestSimpleModule_Sub3();
	private static final Module MODULE_4 = new TestSimpleModule_Sub4();
	private static final Module MODULE_5 = new TestSimpleModule_Sub5();
	private static final Module MODULE_6 = new TestSimpleModule_Sub6();

	private static DefaultModuleModel model;
	private static TestSetObverser obv;

	@Before
	public void setUp() throws Exception {
		model = new DefaultModuleModel(new LinkedHashMap<>(), Collections.newSetFromMap(new WeakHashMap<>()));
		obv = new TestSetObverser();

		model.add(MODULE_1);
		model.add(MODULE_2);
		model.add(MODULE_3);
		model.add(MODULE_4);
		model.add(MODULE_5);
		model.addObverser(obv);
	}

	@After
	public void tearDown() {
		model = null;
		obv = null;
	}

	@Test
	public final void testHashCode() {
		Set<Module> set = new HashSet<>(
				Arrays.asList(MODULE_1, MODULE_2, MODULE_3, MODULE_4, MODULE_5));
		assertEquals(true, set.hashCode() == model.hashCode());
	}

	@Test
	public final void testSize() {
		assertEquals(5, model.size());
	}

	@Test
	public final void testIsEmpty() {
		assertEquals(false, model.isEmpty());
		model.clear();
		assertEquals(true, model.isEmpty());
	}

	@Test
	public final void testContains() {
		assertEquals(true, model.contains(MODULE_1));
		assertEquals(true, model.contains(MODULE_2));
		assertEquals(true, model.contains(MODULE_3));
		assertEquals(true, model.contains(MODULE_4));
		assertEquals(true, model.contains(MODULE_5));
		assertEquals(false, model.contains(MODULE_6));
	}

	@Test
	public final void testIterator() {
		Iterator<Module> i = model.iterator();
		assertEquals(MODULE_1, i.next());
		i.remove();
		assertEquals(MODULE_1, obv.removedList.get(0));
		i.next();
		i.next();
		i.next();
		i.next();
		assertEquals(false, i.hasNext());
	}

	@Test
	public final void testToArray() {
		assertArrayEquals(new Object[] { MODULE_1, MODULE_2, MODULE_3, MODULE_4, MODULE_5 },
				model.toArray());
	}

	@Test
	public final void testToArrayTArray() {
		assertArrayEquals(new Object[] { MODULE_1, MODULE_2, MODULE_3, MODULE_4, MODULE_5 },
				model.toArray(new Module[0]));
	}

	@Test
	public final void testAdd() {
		assertEquals(false, model.add(MODULE_1));
		assertEquals(true, model.add(MODULE_6));
		assertEquals(MODULE_6, obv.addedList.get(0));
	}

	@Test
	public final void testRemove() {
		assertEquals(false, model.remove(null));
		assertEquals(false, model.remove(MODULE_6));
		assertEquals(true, model.remove(MODULE_1));
		assertEquals(MODULE_1, obv.removedList.get(0));
	}

	@Test
	public final void testContainsAll() {
		assertEquals(true,
				model.containsAll(Arrays.asList(MODULE_1, MODULE_2, MODULE_3, MODULE_4, MODULE_5)));
		assertEquals(false, model.containsAll(
				Arrays.asList(MODULE_1, MODULE_2, MODULE_3, MODULE_4, MODULE_5, MODULE_6)));
	}

	@Test
	public final void testAddAll() {
		assertEquals(true, model.addAll(Arrays.asList(MODULE_1, MODULE_5, MODULE_6)));
		assertEquals(MODULE_6, obv.addedList.get(0));
		assertEquals(6, model.size());
	}

	@Test
	public final void testRetainAll() {
		assertEquals(true, model.retainAll(Arrays.asList(MODULE_2, MODULE_3, MODULE_4)));
		assertEquals(3, model.size());
		assertEquals(MODULE_1, obv.removedList.get(0));
		assertEquals(MODULE_5, obv.removedList.get(1));
	}

	@Test
	public final void testRemoveAll() {
		assertEquals(true, model.removeAll(Arrays.asList(MODULE_2, MODULE_3, MODULE_4)));
		assertEquals(2, model.size());
		assertEquals(MODULE_2, obv.removedList.get(0));
		assertEquals(MODULE_3, obv.removedList.get(1));
		assertEquals(MODULE_4, obv.removedList.get(2));
	}

	@Test
	public final void testClear() {
		model.clear();
		assertEquals(true, model.isEmpty());
		assertEquals(0, model.size());
		assertEquals(1, obv.cleared);
	}

	@Test
	public final void testGet() {
		assertEquals(MODULE_1, model.get(TestSimpleModule_Sub1.class));
		assertEquals(MODULE_2, model.get(TestSimpleModule_Sub2.class));
		assertEquals(MODULE_3, model.get(TestSimpleModule_Sub3.class));
		assertEquals(MODULE_4, model.get(TestSimpleModule_Sub4.class));
		assertEquals(MODULE_5, model.get(TestSimpleModule_Sub5.class));
		assertNull(model.get(TestSimpleModule_Sub6.class));
		assertNull(model.get(TestSimpleModule.class));
	}

	@Test
	public final void testGetSubs() {
		Set<Module> set1 = new HashSet<>(Arrays.asList(MODULE_1));
		assertEquals(set1, model.getSubs(TestSimpleModule_Sub1.class));
		Set<Module> set2 = new HashSet<>(
				Arrays.asList(MODULE_1, MODULE_2, MODULE_3, MODULE_4, MODULE_5));
		assertEquals(set2, model.getSubs(TestSimpleModule.class));
	}

	@Test
	public final void testContainsClass() {
		assertTrue(model.containsClass(TestSimpleModule_Sub1.class));
		assertTrue(model.containsClass(TestSimpleModule_Sub2.class));
		assertTrue(model.containsClass(TestSimpleModule_Sub3.class));
		assertTrue(model.containsClass(TestSimpleModule_Sub4.class));
		assertTrue(model.containsClass(TestSimpleModule_Sub5.class));
		assertFalse(model.containsClass(TestSimpleModule_Sub6.class));
	}

	@Test
	public final void testContainsAllClass() {
		assertTrue(model.containsAllClass(Arrays.asList(TestSimpleModule_Sub1.class, TestSimpleModule_Sub2.class,
				TestSimpleModule_Sub3.class, TestSimpleModule_Sub4.class, TestSimpleModule_Sub5.class)));
		assertFalse(model.containsAllClass(Arrays.asList(TestSimpleModule_Sub1.class, TestSimpleModule_Sub2.class,
				TestSimpleModule_Sub3.class, TestSimpleModule_Sub4.class, TestSimpleModule_Sub5.class,
				TestSimpleModule_Sub6.class)));
	}

	@Test
	public final void testRemoveClass() {
		assertFalse(model.removeClass(null));
		assertTrue(model.removeClass(TestSimpleModule_Sub1.class));
		assertTrue(model.removeClass(TestSimpleModule_Sub2.class));
		assertTrue(model.removeClass(TestSimpleModule_Sub3.class));
		assertTrue(model.removeClass(TestSimpleModule_Sub4.class));
		assertTrue(model.removeClass(TestSimpleModule_Sub5.class));
		assertFalse(model.removeClass(TestSimpleModule_Sub6.class));
		assertEquals(MODULE_1, obv.removedList.get(0));
		assertEquals(MODULE_2, obv.removedList.get(1));
		assertEquals(MODULE_3, obv.removedList.get(2));
		assertEquals(MODULE_4, obv.removedList.get(3));
		assertEquals(MODULE_5, obv.removedList.get(4));
	}

	@Test
	public final void testRemoveAllClass() {
		assertTrue(model.removeAllClass(Arrays.asList(TestSimpleModule_Sub2.class, TestSimpleModule_Sub3.class,
				TestSimpleModule_Sub4.class)));
		assertEquals(2, model.size());
		assertEquals(MODULE_2, obv.removedList.get(0));
		assertEquals(MODULE_3, obv.removedList.get(1));
		assertEquals(MODULE_4, obv.removedList.get(2));
	}

	@Test
	public final void testRetainAllKey() {
		assertTrue(model.retainAllClass(Arrays.asList(TestSimpleModule_Sub2.class, TestSimpleModule_Sub3.class,
				TestSimpleModule_Sub4.class)));
		assertEquals(3, model.size());
		assertEquals(MODULE_1, obv.removedList.get(0));
		assertEquals(MODULE_5, obv.removedList.get(1));
	}

	@Test
	public final void testEqualsObject() {
		Set<Module> set = new HashSet<>(
				Arrays.asList(MODULE_1, MODULE_2, MODULE_3, MODULE_4, MODULE_5));
		assertEquals(true, set.equals(model));
	}

	@Test
	public final void testGetObversers() {
		assertEquals(true, model.getObversers().contains(obv));
		assertEquals(1, model.getObversers().size());
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void testGetObversersException() {
		Set<SetObverser<Module>> obversers = model.getObversers();
		obversers.clear();
		fail("应该抛出异常，但是没有。");
	}

	@Test
	public final void testRemoveObverser() {
		model.removeObverser(obv);
		assertEquals(0, model.getObversers().size());
		assertEquals(false, model.getObversers().contains(obv));
	}

	@Test
	public final void testClearObverser() {
		model.clearObverser();
		assertTrue(model.getObversers().isEmpty());
	}

}
