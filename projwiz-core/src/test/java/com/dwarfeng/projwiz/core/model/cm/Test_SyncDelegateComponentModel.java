package com.dwarfeng.projwiz.core.model.cm;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReadWriteLock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dwarfeng.dutil.basic.cna.model.MapKeySetModel;
import com.dwarfeng.dutil.basic.cna.model.obv.SetObverser;
import com.dwarfeng.projwiz.core.model.eum.IconVariability;
import com.dwarfeng.projwiz.core.model.struct.Component;
import com.dwarfeng.projwiz.core.util.ModelUtil;

public class Test_SyncDelegateComponentModel {

	private static final class TestComponent implements Component {

		private final String key;

		private TestComponent(String key) {
			this.key = key;
		}

		@Override
		public void dispose() {

		}

		@Override
		public String getDescription() {
			return null;
		}

		@Override
		public Image getIcon() {
			return null;
		}

		@Override
		public IconVariability getIconVarialibity() {
			return null;
		}

		@Override
		public String getKey() {
			return key;
		}

		@Override
		public ReadWriteLock getLock() {
			return null;
		}

		@Override
		public String getName() {
			return null;
		}

		public String testMethod() {
			return "1111";
		}

		@Override
		public String toString() {
			return new StringBuilder().append("COMPONENT_").append(key).toString();
		}

	}

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

	private static final Component COMPONENT_1 = new TestComponent("1");
	private static final Component COMPONENT_2 = new TestComponent("2");
	private static final Component COMPONENT_3 = new TestComponent("3");
	private static final Component COMPONENT_4 = new TestComponent("4");
	private static final Component COMPONENT_5 = new TestComponent("5");
	private static final Component COMPONENT_6 = new TestComponent("6");
	private static final Component COMPONENT_FAIL = new TestComponent("1");

	private static SyncComponentModel model;
	private static TestSetObverser obv;

	@Before
	public void setUp() throws Exception {
		model = ModelUtil.syncComponentModel(new DelegateComponentModel(
				new MapKeySetModel<>(new LinkedHashMap<>(), Collections.newSetFromMap(new WeakHashMap<>()))));
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
	public void testAdd() {
		assertEquals(false, model.add(COMPONENT_FAIL));
		assertEquals(true, model.add(COMPONENT_6));
		assertEquals(COMPONENT_6, obv.addedList.get(0));
	}

	@Test
	public void testAddAll() {
		assertEquals(true, model.addAll(Arrays.asList(COMPONENT_FAIL, COMPONENT_1, COMPONENT_5, COMPONENT_6)));
		assertEquals(COMPONENT_6, obv.addedList.get(0));
		assertEquals(6, model.size());
	}

	@Test
	public void testClear() {
		model.clear();
		assertEquals(true, model.isEmpty());
		assertEquals(0, model.size());
		assertEquals(1, obv.cleared);
	}

	@Test
	public void testContains() {
		assertEquals(true, model.contains(COMPONENT_1));
		assertEquals(true, model.contains(COMPONENT_2));
		assertEquals(true, model.contains(COMPONENT_2));
		assertEquals(true, model.contains(COMPONENT_2));
		assertEquals(true, model.contains(COMPONENT_2));
		assertEquals(false, model.contains(COMPONENT_FAIL));
	}

	@Test
	public void testContainsAll() {
		assertEquals(true,
				model.containsAll(Arrays.asList(COMPONENT_1, COMPONENT_2, COMPONENT_3, COMPONENT_4, COMPONENT_5)));
		assertEquals(false, model.containsAll(
				Arrays.asList(COMPONENT_1, COMPONENT_2, COMPONENT_3, COMPONENT_4, COMPONENT_5, COMPONENT_6)));
	}

	@Test
	public void testContainsAllKey() {
		assertEquals(true, model.containsAllKey(Arrays.asList("1", "2", "3", "4", "5")));
		assertEquals(false, model.containsAllKey(Arrays.asList("1", "2", "3", "4", "5", "6")));
	}

	@Test
	public void testContainsKey() {
		assertEquals(true, model.containsKey("1"));
		assertEquals(true, model.containsKey("2"));
		assertEquals(true, model.containsKey("3"));
		assertEquals(true, model.containsKey("4"));
		assertEquals(true, model.containsKey("5"));
		assertEquals(false, model.containsKey("6"));
	}

	@Test
	public void testEqualsObject() {
		Set<Component> set = new HashSet<>(
				Arrays.asList(COMPONENT_1, COMPONENT_2, COMPONENT_3, COMPONENT_4, COMPONENT_5));
		assertEquals(true, set.equals(model));
	}

	@Test
	public void testGetAllClass() {
		Collection<TestComponent> collection = model.getAll(TestComponent.class);
		assertEquals(5, collection.size());
		for (TestComponent component : collection) {
			assertEquals("1111", component.testMethod());
		}
	}

	@Test
	public void testGetObversers() {
		assertEquals(true, model.getObversers().contains(obv));
		assertEquals(1, model.getObversers().size());
	}

	@Test
	public void testGetStringClass0() {
		TestComponent component = model.get("1", TestComponent.class);
		assertEquals("1111", component.testMethod());
	}

	@Test
	public void testHashCode() {
		Set<Component> set = new HashSet<>(
				Arrays.asList(COMPONENT_1, COMPONENT_2, COMPONENT_3, COMPONENT_4, COMPONENT_5));
		assertEquals(true, set.hashCode() == model.hashCode());
	}

	@Test
	public void testIsEmpty() {
		assertEquals(false, model.isEmpty());
		model.clear();
		assertEquals(true, model.isEmpty());
	}

	@Test
	public void testIterator() {
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
	public void testRemove() {
		assertEquals(false, model.remove(null));
		assertEquals(false, model.remove(COMPONENT_FAIL));
		assertEquals(true, model.remove(COMPONENT_1));
		assertEquals(COMPONENT_1, obv.removedList.get(0));
	}

	@Test
	public void testRemoveAll() {
		assertEquals(true, model.removeAll(Arrays.asList(COMPONENT_2, COMPONENT_3, COMPONENT_4)));

		assertEquals(2, model.size());

		assertEquals(COMPONENT_2, obv.removedList.get(0));
		assertEquals(COMPONENT_3, obv.removedList.get(1));
		assertEquals(COMPONENT_4, obv.removedList.get(2));
	}

	@Test
	public void testRemoveAllKey() {
		assertEquals(true, model.removeAllKey(Arrays.asList("2", "3", "4")));
		assertEquals(2, model.size());

		assertEquals(COMPONENT_2, obv.removedList.get(0));
		assertEquals(COMPONENT_3, obv.removedList.get(1));
		assertEquals(COMPONENT_4, obv.removedList.get(2));
	}

	@Test
	public void testRemoveKey() {
		assertEquals(true, model.removeKey("2"));
		assertEquals(false, model.removeKey("6"));
		assertEquals(COMPONENT_2, obv.removedList.get(0));
	}

	@Test
	public void testRemoveObverser() {
		model.removeObverser(obv);
		assertEquals(0, model.getObversers().size());
		assertEquals(false, model.getObversers().contains(obv));
	}

	@Test
	public void testRetainAll() {
		assertEquals(true, model.retainAll(Arrays.asList(COMPONENT_2, COMPONENT_3, COMPONENT_4)));

		assertEquals(3, model.size());

		assertEquals(COMPONENT_1, obv.removedList.get(0));
		assertEquals(COMPONENT_5, obv.removedList.get(1));
	}

	@Test
	public void testRetainAllKey() {
		assertEquals(true, model.retainAllKey(Arrays.asList("2", "3", "4")));
		assertEquals(3, model.size());

		assertEquals(COMPONENT_1, obv.removedList.get(0));
		assertEquals(COMPONENT_5, obv.removedList.get(1));
	}

	@Test
	public void testSize() {
		assertEquals(5, model.size());
	}

	@Test
	public void testToArray() {
		assertArrayEquals(new Object[] { COMPONENT_1, COMPONENT_2, COMPONENT_3, COMPONENT_4, COMPONENT_5 },
				model.toArray());
	}

	@Test
	public void testToArrayTArray() {
		assertArrayEquals(new Object[] { COMPONENT_1, COMPONENT_2, COMPONENT_3, COMPONENT_4, COMPONENT_5 },
				model.toArray(new Component[0]));
	}

}
