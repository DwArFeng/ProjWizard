package com.dwarfeng.projwiz.raefrm.model.cm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dwarfeng.projwiz.core.model.struct.Toolkit;
import com.dwarfeng.projwiz.core.model.struct.Toolkit.Method;
import com.dwarfeng.projwiz.raefrm.model.obv.PermDemandAdapter;

public class Test_DelegatePermDemandModel {

	private static final class TestPermDemandObverser extends PermDemandAdapter {

		final List<String> putList = new ArrayList<>();
		final List<String> changeList = new ArrayList<>();
		final List<String> removeList = new ArrayList<>();
		final AtomicBoolean clearFlagRef = new AtomicBoolean(false);

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void firePut(String key, Collection<Method> value) {
			putList.add(key);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireChanged(String key, Collection<Method> oldValue, Collection<Method> newValue) {
			changeList.add(key);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireRemoved(String key, Collection<Method> value) {
			removeList.add(key);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireCleared() {
			clearFlagRef.set(true);
		}

	}

	private static DelegatePermDemandModel model;
	private static TestPermDemandObverser obv;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		model = new DelegatePermDemandModel();
		obv = new TestPermDemandObverser();
		model.put("logger",
				Arrays.asList(new Toolkit.Method[] { Toolkit.Method.INFO, Toolkit.Method.WARN, Toolkit.Method.ERROR }));
		model.addObverser(obv);
	}

	@After
	public void tearDown() throws Exception {
		model = null;
		obv = null;
	}

	@Test
	public void testGetObversers() {
		assertTrue(model.getObversers().contains(obv));
	}

	@Test
	public void testAddObverser() {
		assertFalse(model.addObverser(obv));
	}

	@Test
	public void testRemoveObverser() {
		assertTrue(model.removeObverser(obv));
	}

	@Test
	public void testClearObverser() {
		model.clearObverser();
		assertTrue(model.getObversers().isEmpty());
	}

	@Test
	public void testSize() {
		assertEquals(1, model.size());
		model.clear();
		assertEquals(0, model.size());
	}

	@Test
	public void testIsEmpty() {
		assertFalse(model.isEmpty());
		model.clear();
		assertTrue(model.isEmpty());
	}

	@Test
	public void testContainsKey() {
		assertTrue(model.containsKey("logger"));
		assertFalse(model.containsKey("logger0"));
	}

	@Test
	public void testContainsValue() {
		assertTrue(model.containsValue(Arrays
				.asList(new Toolkit.Method[] { Toolkit.Method.INFO, Toolkit.Method.WARN, Toolkit.Method.ERROR })));
	}

	@Test
	public void testGet() {
		assertEquals(
				Arrays.asList(new Toolkit.Method[] { Toolkit.Method.INFO, Toolkit.Method.WARN, Toolkit.Method.ERROR }),
				model.get("logger"));
	}

	@Test
	public void testPut() {
		model.put("logger0",
				Arrays.asList(new Toolkit.Method[] { Toolkit.Method.INFO, Toolkit.Method.WARN, Toolkit.Method.ERROR }));
		assertEquals(2, model.size());
		assertEquals("logger0", obv.putList.get(0));
		model.put("logger", Arrays.asList(new Toolkit.Method[] { Toolkit.Method.INFO, Toolkit.Method.WARN }));
		assertEquals("logger", obv.changeList.get(0));
	}

	@Test
	public void testRemove() {
		model.remove("logger");
		assertEquals("logger", obv.removeList.get(0));
		assertTrue(model.isEmpty());
	}

	@Test
	public void testPutAll() {
		Map<String, Collection<Toolkit.Method>> map = new HashMap<>();
		map.put("logger0",
				Arrays.asList(new Toolkit.Method[] { Toolkit.Method.INFO, Toolkit.Method.WARN, Toolkit.Method.ERROR }));
		map.put("logger1",
				Arrays.asList(new Toolkit.Method[] { Toolkit.Method.INFO, Toolkit.Method.WARN, Toolkit.Method.ERROR }));
		map.put("logger",
				Arrays.asList(new Toolkit.Method[] { Toolkit.Method.INFO, Toolkit.Method.WARN, Toolkit.Method.ERROR }));

		model.putAll(map);
		assertEquals(3, model.size());
		assertArrayEquals(new Object[] { "logger0", "logger1" }, obv.putList.toArray(new Object[0]));
		assertEquals("logger", obv.changeList.get(0));
	}

	@Test
	public void testClear() {
		model.clear();
		assertTrue(model.isEmpty());
	}

	@Test
	public void testKeySet() {
		assertTrue(model.keySet().contains("logger"));
	}

	@Test
	public void testValues() {
		assertTrue(model.values().contains(Arrays
				.asList(new Toolkit.Method[] { Toolkit.Method.INFO, Toolkit.Method.WARN, Toolkit.Method.ERROR })));
	}

	@Test
	public void testEntrySet() {
		Set<Map.Entry<String, Collection<Toolkit.Method>>> entrySet = model.entrySet();
		assertEquals(1, entrySet.size());
	}

	@Test
	public void testIsPermDemand() {
		assertTrue(model.isPermDemand("logger", Toolkit.Method.INFO));
		assertFalse(model.isPermDemand("logger", Toolkit.Method.FATAL));
	}

}
