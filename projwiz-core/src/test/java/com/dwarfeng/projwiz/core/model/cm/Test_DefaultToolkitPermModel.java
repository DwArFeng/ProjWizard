package com.dwarfeng.projwiz.core.model.cm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dwarfeng.projwiz.core.model.obv.ToolkitPermAdapter;
import com.dwarfeng.projwiz.core.model.struct.Toolkit.Method;

public class Test_DefaultToolkitPermModel {

	private static final class TestToolkitPermObverser extends ToolkitPermAdapter {

		final List<Method> putList = new ArrayList<>();
		final List<Method> changeList = new ArrayList<>();
		final List<Method> removeList = new ArrayList<>();
		final AtomicBoolean clearFlagRef = new AtomicBoolean(false);
		final List<Integer> dplChangeList = new ArrayList<>();

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void firePut(Method key, Integer value) {
			putList.add(key);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireChanged(Method key, Integer oldValue, Integer newValue) {
			changeList.add(key);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireRemoved(Method key, Integer value) {
			removeList.add(key);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireCleared() {
			clearFlagRef.set(true);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireDplChanged(Integer oldValue, Integer newValue) {
			dplChangeList.add(newValue);
		}

	}

	private static DefaultToolkitPermModel model;
	private static TestToolkitPermObverser obv;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		model = new DefaultToolkitPermModel();
		obv = new TestToolkitPermObverser();
		model.put(Method.TRACE, 15);
		model.put(Method.INFO, 20);
		model.put(Method.WARN, 25);
		model.addObverser(obv);
	}

	@After
	public void tearDown() throws Exception {
		model = null;
		obv = null;
	}

	@Test
	public final void testSize() {
		assertEquals(3, model.size());
	}

	@Test
	public final void testIsEmpty() {
		assertFalse(model.isEmpty());
		model.clear();
		assertTrue(model.isEmpty());
	}

	@Test
	public final void testContainsKey() {
		assertTrue(model.containsKey(Method.TRACE));
		assertFalse(model.containsKey(Method.ERROR));
	}

	@Test
	public final void testContainsValue() {
		assertTrue(model.containsValue(15));
		assertFalse(model.containsValue(35));
	}

	@Test
	public final void testGet() {
		assertEquals(new Integer(15), model.get(Method.TRACE));
	}

	@Test
	public final void testPut() {
		assertEquals(new Integer(15), model.put(Method.TRACE, 35));
		assertEquals(Method.TRACE, obv.changeList.get(0));
		assertEquals(null, model.put(Method.ERROR, 35));
		assertEquals(Method.ERROR, obv.putList.get(0));
	}

	@Test
	public final void testRemove() {
		assertEquals(new Integer(15), model.remove(Method.TRACE));
		assertEquals(null, model.remove(Method.ERROR));
		assertEquals(new Integer(20), model.remove(Method.INFO));
		assertEquals(Method.TRACE, obv.removeList.get(0));
		assertEquals(Method.INFO, obv.removeList.get(1));
	}

	@Test
	public final void testClear() {
		model.clear();
		assertTrue(model.isEmpty());
		assertEquals(0, model.size());
		assertTrue(obv.clearFlagRef.get());
	}

	@Test
	public final void testGetDpl() {
		assertEquals(0, model.getDpl());
	}

	@Test
	public final void testSetDpl() {
		assertFalse(model.setDpl(0));
		assertTrue(model.setDpl(45));
		assertEquals(new Integer(45), obv.dplChangeList.get(0));
	}

	@Test
	public final void testHasPerm() {
		assertTrue(model.hasPerm(Method.TRACE, 20));
		assertFalse(model.hasPerm(Method.WARN, 20));
		model.setDpl(45);
		assertTrue(model.hasPerm(Method.ERROR, 45));
		assertFalse(model.hasPerm(Method.ERROR, 44));
	}

}
