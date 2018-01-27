package com.dwarfeng.projwiz.core.model.cm;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import com.dwarfeng.dutil.basic.cna.model.DelegateKeySetModel;
import com.dwarfeng.dutil.basic.cna.model.KeySetModel;
import com.dwarfeng.dutil.basic.cna.model.MapKeySetModel;
import com.dwarfeng.dutil.basic.cna.model.ModelUtil;
import com.dwarfeng.dutil.basic.cna.model.obv.SetObverser;
import com.dwarfeng.projwiz.core.model.struct.Component;

/**
 * 代理组件模型。
 * 
 * <p>
 * 通过代理一个键值集合模型来实现的代理组件模型。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public final class DelegateComponentModel implements ComponentModel {

	/** 被代理的键值集合模型。 */
	protected final KeySetModel<String, Component> delegate;

	public DelegateComponentModel() {
		this(new MapKeySetModel<>());
	}

	public DelegateComponentModel(KeySetModel<String, Component> delegate) {
		Objects.requireNonNull(delegate, "入口参数 delegate 不能为 null。");

		this.delegate = delegate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<SetObverser<Component>> getObversers() {
		return delegate.getObversers();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addObverser(SetObverser<Component> obverser) {
		return delegate.addObverser(obverser);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component get(String key) {
		return delegate.get(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeObverser(SetObverser<Component> obverser) {
		return delegate.removeObverser(obverser);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsKey(Object key) {
		return delegate.containsKey(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clearObverser() {
		delegate.clearObverser();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsAllKey(Collection<?> c) {
		return delegate.containsAllKey(c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeKey(Object key) {
		return delegate.removeKey(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAllKey(Collection<?> c) {
		return delegate.removeAllKey(c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean retainAllKey(Collection<?> c) {
		return delegate.retainAllKey(c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return delegate.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(Object o) {
		return delegate.contains(o);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Component> iterator() {
		return delegate.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return delegate.toArray();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T[] toArray(T[] a) {
		return delegate.toArray(a);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean add(Component e) {
		return delegate.add(e);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(Object o) {
		return delegate.remove(o);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		return delegate.containsAll(c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAll(Collection<? extends Component> c) {
		return delegate.addAll(c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		return delegate.retainAll(c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		return delegate.removeAll(c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		delegate.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		return delegate.equals(o);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return delegate.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Component> T get(String key, Class<T> clas) throws ClassCastException, NullPointerException {
		Objects.requireNonNull(key, "入口参数 key 不能为 null。");
		Objects.requireNonNull(clas, "入口参数 clas 不能为 null。");

		return clas.cast(get(key));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Component> KeySetModel<String, T> getAll(Class<T> clas) throws NullPointerException {
		Objects.requireNonNull(clas, "入口参数 clas 不能为 null。");

		KeySetModel<String, T> model = new DelegateKeySetModel<>();
		for (Iterator<Component> i = iterator(); i.hasNext();) {
			Component component = i.next();
			if (clas.isInstance(component)) {
				model.add(clas.cast(component));
			}
		}

		return ModelUtil.unmodifiableKeySetModel(model);
	}

}
