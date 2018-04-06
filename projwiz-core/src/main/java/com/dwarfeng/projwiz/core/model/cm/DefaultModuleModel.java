package com.dwarfeng.projwiz.core.model.cm;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.WeakHashMap;

import com.dwarfeng.dutil.basic.cna.model.AbstractSetModel;
import com.dwarfeng.dutil.basic.cna.model.obv.SetObverser;
import com.dwarfeng.projwiz.core.model.struct.Module;

/**
 * 默认组件模型。
 * 
 * <p>
 * 组件模型的默认实现。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public final class DefaultModuleModel extends AbstractSetModel<Module> implements ModuleModel {

	/** 组件映射。 */
	private final Map<Class<? extends Module>, Module> moduleMap;

	/**
	 * 新实例。
	 */
	public DefaultModuleModel() {
		this(new HashMap<>(), Collections.newSetFromMap(new WeakHashMap<>()));
	}

	/**
	 * 新实例。
	 * 
	 * @param moduleMap
	 *            指定的组件映射。
	 * @param obversers
	 *            指定的侦听器集合。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public DefaultModuleModel(Map<Class<? extends Module>, Module> moduleMap, Set<SetObverser<Module>> obversers) {
		super(obversers);

		Objects.requireNonNull(moduleMap, "入口参数 moduleMap 不能为 null。");
		this.moduleMap = moduleMap;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return moduleMap.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return moduleMap.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(Object o) {
		return moduleMap.containsValue(o);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Module> iterator() {
		return new InnerIterator(moduleMap.values().iterator());
	}

	private class InnerIterator implements Iterator<Module> {

		private final Iterator<Module> itr;
		private Module cursor = null;

		public InnerIterator(Iterator<Module> itr) {
			this.itr = itr;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return itr.hasNext();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Module next() {
			cursor = itr.next();
			return cursor;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			itr.remove();
			fireRemoved(cursor);
			cursor = null;
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return moduleMap.values().toArray();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T[] toArray(T[] a) {
		return moduleMap.values().toArray(a);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean add(Module e) {
		Objects.requireNonNull(e, "入口参数 e 不能为 null。");

		Class<? extends Module> clazz = e.getClass();

		if (moduleMap.containsKey(clazz)) {
			return false;
		}

		moduleMap.put(clazz, e);
		fireAdded(e);

		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(Object o) {
		if (Objects.isNull(o)) {
			return false;
		}

		if (!(o instanceof Module)) {
			return false;
		}

		/*
		 * 由于之前已经判定该对象属于组件，因此该转换类型安全。
		 */
		@SuppressWarnings("unchecked")
		Class<? extends Module> clazz = (Class<? extends Module>) o.getClass();

		if (Objects.isNull(moduleMap.remove(clazz))) {
			return false;
		}

		fireRemoved((Module) o);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		Objects.requireNonNull(c, "入口参数 c 不能为 null。");
		return moduleMap.values().containsAll(c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAll(Collection<? extends Module> c) {
		Objects.requireNonNull(c, "入口参数 c 不能为 null。");

		boolean result = false;
		for (Module module : c) {
			if (add(module)) {
				result = true;
			}
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		Objects.requireNonNull(c, "入口参数 c 不能为 null。");
		return batchRemove(c, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		Objects.requireNonNull(c, "入口参数 c 不能为 null。");
		return batchRemove(c, true);
	}

	private boolean batchRemove(Collection<?> c, boolean aFlag) {
		boolean result = false;

		for (Iterator<Module> i = iterator(); i.hasNext();) {
			Module module = i.next();

			if (c.contains(module) == aFlag) {
				i.remove();
				result = true;
			}
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		moduleMap.clear();
		fireCleared();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Module> T get(Class<T> clazz) {
		if (Objects.isNull(clazz)) {
			return null;
		}

		for (Iterator<Module> i = iterator(); i.hasNext();) {
			Module module = i.next();
			if (module.getClass().equals(clazz)) {
				return clazz.cast(module);
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Module> Collection<T> getSubs(Class<T> clazz) {
		Collection<T> modules = new HashSet<>();

		if (Objects.isNull(clazz)) {
			return modules;
		}

		for (Iterator<Module> i = iterator(); i.hasNext();) {
			Module module = i.next();
			if (clazz.isInstance(module)) {
				modules.add(clazz.cast(module));
			}
		}
		return modules;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsClass(Class<?> clazz) {
		return moduleMap.containsKey(clazz);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsAllClass(Collection<Class<?>> c) {
		Objects.requireNonNull(c, "入口参数 c 不能为 null。");

		for (Class<?> clazz : c) {
			if (!moduleMap.containsKey(clazz)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeClass(Class<?> clazz) {
		if (Objects.isNull(clazz)) {
			return false;
		}

		if (!moduleMap.containsKey(clazz)) {
			return false;
		}

		Module module = moduleMap.get(clazz);
		moduleMap.remove(clazz);

		fireRemoved(module);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAllClass(Collection<Class<?>> c) {
		Objects.requireNonNull(c, "入口参数 c 不能为 null。");
		return batchRemoveClass(c, true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean retainAllClass(Collection<Class<?>> c) {
		Objects.requireNonNull(c, "入口参数 c 不能为 null。");
		return batchRemoveClass(c, false);
	}

	private boolean batchRemoveClass(Collection<?> c, boolean aFlag) {
		boolean result = false;

		for (Iterator<Module> i = iterator(); i.hasNext();) {
			Class<? extends Module> clazz = i.next().getClass();

			if (c.contains(clazz) == aFlag) {
				i.remove();
				result = true;
			}
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;

		if (!(o instanceof Set))
			return false;
		Collection<?> c = (Collection<?>) o;
		if (c.size() != size())
			return false;
		try {
			return containsAll(c);
		} catch (ClassCastException unused) {
			return false;
		} catch (NullPointerException unused) {
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		int h = 0;
		Iterator<Module> i = iterator();
		while (i.hasNext()) {
			Module module = i.next();
			if (module != null)
				h += module.hashCode();
		}
		return h;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		Iterator<Module> it = iterator();
		if (!it.hasNext())
			return "[]";

		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for (;;) {
			sb.append(it.next());
			if (!it.hasNext())
				return sb.append(']').toString();
			sb.append(',').append(' ');
		}

	}

}
