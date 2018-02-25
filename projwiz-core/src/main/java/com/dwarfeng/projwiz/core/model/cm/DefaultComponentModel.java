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
import com.dwarfeng.projwiz.core.model.struct.Component;

/**
 * 默认组件模型。
 * 
 * <p>
 * 组件模型的默认实现。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public final class DefaultComponentModel extends AbstractSetModel<Component> implements ComponentModel {

	/** 组件映射。 */
	private final Map<Class<? extends Component>, Component> componentMap;

	/**
	 * 新实例。
	 */
	public DefaultComponentModel() {
		this(new HashMap<>(), Collections.newSetFromMap(new WeakHashMap<>()));
	}

	/**
	 * 新实例。
	 * 
	 * @param componentMap
	 *            指定的组件映射。
	 * @param obversers
	 *            指定的侦听器集合。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public DefaultComponentModel(Map<Class<? extends Component>, Component> componentMap,
			Set<SetObverser<Component>> obversers) {
		super(obversers);

		Objects.requireNonNull(componentMap, "入口参数 componentMap 不能为 null。");
		this.componentMap = componentMap;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return componentMap.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return componentMap.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(Object o) {
		return componentMap.containsValue(o);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Component> iterator() {
		return new InnerIterator(componentMap.values().iterator());
	}

	private class InnerIterator implements Iterator<Component> {

		private final Iterator<Component> itr;
		private Component cursor = null;

		public InnerIterator(Iterator<Component> itr) {
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
		public Component next() {
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
		return componentMap.values().toArray();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T[] toArray(T[] a) {
		return componentMap.values().toArray(a);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean add(Component e) {
		Objects.requireNonNull(e, "入口参数 e 不能为 null。");

		Class<? extends Component> clazz = e.getClass();

		if (componentMap.containsKey(clazz)) {
			return false;
		}

		componentMap.put(clazz, e);
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

		if (!(o instanceof Component)) {
			return false;
		}

		/*
		 * 由于之前已经判定该对象属于组件，因此该转换类型安全。
		 */
		@SuppressWarnings("unchecked")
		Class<? extends Component> clazz = (Class<? extends Component>) o.getClass();

		if (Objects.isNull(componentMap.remove(clazz))) {
			return false;
		}

		fireRemoved((Component) o);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		Objects.requireNonNull(c, "入口参数 c 不能为 null。");
		return componentMap.values().containsAll(c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAll(Collection<? extends Component> c) {
		Objects.requireNonNull(c, "入口参数 c 不能为 null。");

		boolean result = false;
		for (Component component : c) {
			if (add(component)) {
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

		for (Iterator<Component> i = iterator(); i.hasNext();) {
			Component component = i.next();

			if (c.contains(component) == aFlag) {
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
		componentMap.clear();
		fireCleared();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Component> T get(Class<T> clazz) {
		if (Objects.isNull(clazz)) {
			return null;
		}

		for (Iterator<Component> i = iterator(); i.hasNext();) {
			Component component = i.next();
			if (component.getClass().equals(clazz)) {
				return clazz.cast(component);
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Component> Collection<T> getSubs(Class<T> clazz) {
		Collection<T> components = new HashSet<>();

		if (Objects.isNull(clazz)) {
			return components;
		}

		for (Iterator<Component> i = iterator(); i.hasNext();) {
			Component component = i.next();
			if (clazz.isInstance(component)) {
				components.add(clazz.cast(component));
			}
		}
		return components;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsClass(Class<?> clazz) {
		return componentMap.containsKey(clazz);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsAllClass(Collection<Class<?>> c) {
		Objects.requireNonNull(c, "入口参数 c 不能为 null。");

		for (Class<?> clazz : c) {
			if (!componentMap.containsKey(clazz)) {
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

		if (!componentMap.containsKey(clazz)) {
			return false;
		}

		Component component = componentMap.get(clazz);
		componentMap.remove(clazz);

		fireRemoved(component);
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

		for (Iterator<Component> i = iterator(); i.hasNext();) {
			Class<? extends Component> clazz = i.next().getClass();

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
		Iterator<Component> i = iterator();
		while (i.hasNext()) {
			Component component = i.next();
			if (component != null)
				h += component.hashCode();
		}
		return h;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		Iterator<Component> it = iterator();
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
