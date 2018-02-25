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
import com.dwarfeng.projwiz.core.model.struct.Component;
import com.dwarfeng.projwiz.core.model.struct.TestSimpleComponent;

public class Test_DefaultComponentModel {

	private static final class TestSetObverser implements SetObverser<Component> {

		public List<Component> addedList = new ArrayList<>();
		public List<Component> removedList = new ArrayList<>();
		public int cleared = 0;

		@Override
		public void fireAdded(Component component) {
			addedList.add(component);
		}

		@Override
		public void fireCleared() {
			cleared++;
		}

		@Override
		public void fireRemoved(Component component) {
			removedList.add(component);
		}

	}

	private static final Component COMPONENT_1 = new TestSimpleComponent_Sub1();
	private static final Component COMPONENT_2 = new TestSimpleComponent_Sub2();
	private static final Component COMPONENT_3 = new TestSimpleComponent_Sub3();
	private static final Component COMPONENT_4 = new TestSimpleComponent_Sub4();
	private static final Component COMPONENT_5 = new TestSimpleComponent_Sub5();
	private static final Component COMPONENT_6 = new TestSimpleComponent_Sub6();

	private static DefaultComponentModel model;
	private static TestSetObverser obv;

	@Before
	public void setUp() throws Exception {
		model = new DefaultComponentModel(new LinkedHashMap<>(), Collections.newSetFromMap(new WeakHashMap<>()));
		obv = new TestSetObverser();

		model.add(COMPONENT_1);
		model.add(COMPONENT_2);
		model.add(COMPONENT_3);
		model.add(COMPONENT_4);
		model.add(COMPONENT_5);
		model.addObverser(obv);
	}

	@After
	public void tearDown() {
		model = null;
		obv = null;
	}

	@Test
	public final void testHashCode() {
		Set<Component> set = new HashSet<>(
				Arrays.asList(COMPONENT_1, COMPONENT_2, COMPONENT_3, COMPONENT_4, COMPONENT_5));
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
		assertEquals(true, model.contains(COMPONENT_1));
		assertEquals(true, model.contains(COMPONENT_2));
		assertEquals(true, model.contains(COMPONENT_3));
		assertEquals(true, model.contains(COMPONENT_4));
		assertEquals(true, model.contains(COMPONENT_5));
		assertEquals(false, model.contains(COMPONENT_6));
	}

	@Test
	public final void testIterator() {
		Iterator<Component> i = model.iterator();
		assertEquals(COMPONENT_1, i.next());
		i.remove();
		assertEquals(COMPONENT_1, obv.removedList.get(0));
		i.next();
		i.next();
		i.next();
		i.next();
		assertEquals(false, i.hasNext());
	}

	@Test
	public final void testToArray() {
		assertArrayEquals(new Object[] { COMPONENT_1, COMPONENT_2, COMPONENT_3, COMPONENT_4, COMPONENT_5 },
				model.toArray());
	}

	@Test
	public final void testToArrayTArray() {
		assertArrayEquals(new Object[] { COMPONENT_1, COMPONENT_2, COMPONENT_3, COMPONENT_4, COMPONENT_5 },
				model.toArray(new Component[0]));
	}

	@Test
	public final void testAdd() {
		assertEquals(false, model.add(COMPONENT_1));
		assertEquals(true, model.add(COMPONENT_6));
		assertEquals(COMPONENT_6, obv.addedList.get(0));
	}

	@Test
	public final void testRemove() {
		assertEquals(false, model.remove(null));
		assertEquals(false, model.remove(COMPONENT_6));
		assertEquals(true, model.remove(COMPONENT_1));
		assertEquals(COMPONENT_1, obv.removedList.get(0));
	}

	@Test
	public final void testContainsAll() {
		assertEquals(true,
				model.containsAll(Arrays.asList(COMPONENT_1, COMPONENT_2, COMPONENT_3, COMPONENT_4, COMPONENT_5)));
		assertEquals(false, model.containsAll(
				Arrays.asList(COMPONENT_1, COMPONENT_2, COMPONENT_3, COMPONENT_4, COMPONENT_5, COMPONENT_6)));
	}

	@Test
	public final void testAddAll() {
		assertEquals(true, model.addAll(Arrays.asList(COMPONENT_1, COMPONENT_5, COMPONENT_6)));
		assertEquals(COMPONENT_6, obv.addedList.get(0));
		assertEquals(6, model.size());
	}

	@Test
	public final void testRetainAll() {
		assertEquals(true, model.retainAll(Arrays.asList(COMPONENT_2, COMPONENT_3, COMPONENT_4)));
		assertEquals(3, model.size());
		assertEquals(COMPONENT_1, obv.removedList.get(0));
		assertEquals(COMPONENT_5, obv.removedList.get(1));
	}

	@Test
	public final void testRemoveAll() {
		assertEquals(true, model.removeAll(Arrays.asList(COMPONENT_2, COMPONENT_3, COMPONENT_4)));
		assertEquals(2, model.size());
		assertEquals(COMPONENT_2, obv.removedList.get(0));
		assertEquals(COMPONENT_3, obv.removedList.get(1));
		assertEquals(COMPONENT_4, obv.removedList.get(2));
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
		assertEquals(COMPONENT_1, model.get(TestSimpleComponent_Sub1.class));
		assertEquals(COMPONENT_2, model.get(TestSimpleComponent_Sub2.class));
		assertEquals(COMPONENT_3, model.get(TestSimpleComponent_Sub3.class));
		assertEquals(COMPONENT_4, model.get(TestSimpleComponent_Sub4.class));
		assertEquals(COMPONENT_5, model.get(TestSimpleComponent_Sub5.class));
		assertNull(model.get(TestSimpleComponent_Sub6.class));
		assertNull(model.get(TestSimpleComponent.class));
	}

	@Test
	public final void testGetSubs() {
		Set<Component> set1 = new HashSet<>(Arrays.asList(COMPONENT_1));
		assertEquals(set1, model.getSubs(TestSimpleComponent_Sub1.class));
		Set<Component> set2 = new HashSet<>(
				Arrays.asList(COMPONENT_1, COMPONENT_2, COMPONENT_3, COMPONENT_4, COMPONENT_5));
		assertEquals(set2, model.getSubs(TestSimpleComponent.class));
	}

	@Test
	public final void testContainsClass() {
		assertTrue(model.containsClass(TestSimpleComponent_Sub1.class));
		assertTrue(model.containsClass(TestSimpleComponent_Sub2.class));
		assertTrue(model.containsClass(TestSimpleComponent_Sub3.class));
		assertTrue(model.containsClass(TestSimpleComponent_Sub4.class));
		assertTrue(model.containsClass(TestSimpleComponent_Sub5.class));
		assertFalse(model.containsClass(TestSimpleComponent_Sub6.class));
	}

	@Test
	public final void testContainsAllClass() {
		assertTrue(model.containsAllClass(Arrays.asList(TestSimpleComponent_Sub1.class, TestSimpleComponent_Sub2.class,
				TestSimpleComponent_Sub3.class, TestSimpleComponent_Sub4.class, TestSimpleComponent_Sub5.class)));
		assertFalse(model.containsAllClass(Arrays.asList(TestSimpleComponent_Sub1.class, TestSimpleComponent_Sub2.class,
				TestSimpleComponent_Sub3.class, TestSimpleComponent_Sub4.class, TestSimpleComponent_Sub5.class,
				TestSimpleComponent_Sub6.class)));
	}

	@Test
	public final void testRemoveClass() {
		assertFalse(model.removeClass(null));
		assertTrue(model.removeClass(TestSimpleComponent_Sub1.class));
		assertTrue(model.removeClass(TestSimpleComponent_Sub2.class));
		assertTrue(model.removeClass(TestSimpleComponent_Sub3.class));
		assertTrue(model.removeClass(TestSimpleComponent_Sub4.class));
		assertTrue(model.removeClass(TestSimpleComponent_Sub5.class));
		assertFalse(model.removeClass(TestSimpleComponent_Sub6.class));
		assertEquals(COMPONENT_1, obv.removedList.get(0));
		assertEquals(COMPONENT_2, obv.removedList.get(1));
		assertEquals(COMPONENT_3, obv.removedList.get(2));
		assertEquals(COMPONENT_4, obv.removedList.get(3));
		assertEquals(COMPONENT_5, obv.removedList.get(4));
	}

	@Test
	public final void testRemoveAllClass() {
		assertTrue(model.removeAllClass(Arrays.asList(TestSimpleComponent_Sub2.class, TestSimpleComponent_Sub3.class,
				TestSimpleComponent_Sub4.class)));
		assertEquals(2, model.size());
		assertEquals(COMPONENT_2, obv.removedList.get(0));
		assertEquals(COMPONENT_3, obv.removedList.get(1));
		assertEquals(COMPONENT_4, obv.removedList.get(2));
	}

	@Test
	public final void testRetainAllKey() {
		assertTrue(model.retainAllClass(Arrays.asList(TestSimpleComponent_Sub2.class, TestSimpleComponent_Sub3.class,
				TestSimpleComponent_Sub4.class)));
		assertEquals(3, model.size());
		assertEquals(COMPONENT_1, obv.removedList.get(0));
		assertEquals(COMPONENT_5, obv.removedList.get(1));
	}

	@Test
	public final void testEqualsObject() {
		Set<Component> set = new HashSet<>(
				Arrays.asList(COMPONENT_1, COMPONENT_2, COMPONENT_3, COMPONENT_4, COMPONENT_5));
		assertEquals(true, set.equals(model));
	}

	@Test
	public final void testGetObversers() {
		assertEquals(true, model.getObversers().contains(obv));
		assertEquals(1, model.getObversers().size());
	}

	@Test(expected = UnsupportedOperationException.class)
	public final void testGetObversersException() {
		Set<SetObverser<Component>> obversers = model.getObversers();
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
