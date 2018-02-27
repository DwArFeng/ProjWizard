package com.dwarfeng.projwiz.raefrm.model.cm;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.dwarfeng.dutil.basic.cna.model.DelegateMapModel;
import com.dwarfeng.dutil.basic.cna.model.MapModel;
import com.dwarfeng.dutil.basic.cna.model.obv.MapObverser;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;
import com.dwarfeng.projwiz.core.model.struct.Toolkit.Method;

/**
 * 通过代理指定映射模型实现的权限需求模型。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public final class DelegatePermDemandModel implements PermDemandModel {

	/** 该对象代理的映射模型。 */
	protected final MapModel<String, Collection<Method>> delegate;

	/**
	 * 生成一个默认的权限需求模型。
	 */
	public DelegatePermDemandModel() {
		this(new DelegateMapModel<>());
	}

	/**
	 * 生成一个通过代理指定的映射模型实现的权限需求模型。
	 * 
	 * @param delegate
	 *            代理的映射模型。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public DelegatePermDemandModel(MapModel<String, Collection<Method>> delegate) {
		Objects.requireNonNull(delegate, "入口参数 delegate 不能为 null。");
		this.delegate = delegate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<MapObverser<String, Collection<Method>>> getObversers() {
		return delegate.getObversers();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addObverser(MapObverser<String, Collection<Method>> obverser) {
		return delegate.addObverser(obverser);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeObverser(MapObverser<String, Collection<Method>> obverser) {
		return delegate.removeObverser(obverser);
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
	public boolean containsKey(Object key) {
		return delegate.containsKey(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsValue(Object value) {
		return delegate.containsValue(value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Method> get(Object key) {
		return delegate.get(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Method> put(String key, Collection<Method> value) {
		return delegate.put(key, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Method> remove(Object key) {
		return delegate.remove(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void putAll(Map<? extends String, ? extends Collection<Method>> m) {
		delegate.putAll(m);
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
	public Set<String> keySet() {
		return delegate.keySet();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Collection<Method>> values() {
		return delegate.values();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<java.util.Map.Entry<String, Collection<Method>>> entrySet() {
		return delegate.entrySet();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPermDemand(String permKey, Method method) {
		Objects.requireNonNull(method, "入口参数 method 不能为 null。");

		if (!delegate.containsKey(permKey)) {
			throw new IllegalArgumentException("权限需求模型中不包含指定的键" + permKey);
		}

		Collection<Method> methods = delegate.get(permKey);
		return methods.contains(method);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPermKeyAvailable(String permKey, Toolkit toolkit) {
		Objects.requireNonNull(toolkit, "入口参数 toolkit 不能为 null。");

		if (!delegate.containsKey(permKey)) {
			throw new IllegalArgumentException("权限需求模型中不包含指定的键" + permKey);
		}

		Collection<Toolkit.Method> demandMethods = delegate.get(permKey);

		for (Toolkit.Method demandMethod : demandMethods) {
			if (toolkit.notHasPermission(demandMethod))
				return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void requirePermKeyAvailable(String permKey, Toolkit toolkit)
			throws IllegalStateException, NullPointerException, IllegalArgumentException {
		Objects.requireNonNull(toolkit, "入口参数 toolkit 不能为 null。");

		if (!delegate.containsKey(permKey)) {
			throw new IllegalArgumentException("权限需求模型中不包含指定的键" + permKey);
		}

		Collection<Toolkit.Method> demandMethods = delegate.get(permKey);

		for (Toolkit.Method demandMethod : demandMethods) {
			if (toolkit.notHasPermission(demandMethod))
				throw new IllegalStateException("组件工具包没有权限执行指定方法: " + demandMethod);
		}
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
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (obj == delegate) {
			return true;
		}

		return delegate.equals(obj);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return delegate.toString();
	}

}
