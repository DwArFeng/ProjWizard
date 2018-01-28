package com.dwarfeng.projwiz.core.model.cm;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.WeakHashMap;

import com.dwarfeng.dutil.basic.cna.model.AbstractMapModel;
import com.dwarfeng.dutil.basic.cna.model.DelegateMapModel;
import com.dwarfeng.dutil.basic.cna.model.MapModel;
import com.dwarfeng.dutil.basic.cna.model.obv.MapAdapter;
import com.dwarfeng.dutil.basic.cna.model.obv.MapObverser;
import com.dwarfeng.projwiz.core.model.obv.ToolkitPermObverser;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;
import com.dwarfeng.projwiz.core.model.struct.Toolkit.Method;

/**
 * 默认工具包权限模型。
 * 
 * <p>
 * 工具包权限模型的默认实现。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public final class DefaultToolkitPermModel extends AbstractMapModel<Toolkit.Method, Integer>
		implements ToolkitPermModel {

	private final class TransformedObverser extends MapAdapter<Method, Integer> {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void firePut(Method key, Integer value) {
			DefaultToolkitPermModel.this.firePut(key, value);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireChanged(Method key, Integer oldValue, Integer newValue) {
			DefaultToolkitPermModel.this.fireChanged(key, oldValue, newValue);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireRemoved(Method key, Integer value) {
			DefaultToolkitPermModel.this.fireRemoved(key, value);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireCleared() {
			DefaultToolkitPermModel.this.fireCleared();
		}

	}

	/** 映射模型代理。 */
	protected final MapModel<Toolkit.Method, Integer> delegate;

	/** 转运观察器。 */
	private final MapObverser<Toolkit.Method, Integer> transObv = new TransformedObverser();

	/** 默认权限等级。 */
	private int dpl = 0;

	/**
	 * 生成一个默认的工具包权限模型。
	 */
	public DefaultToolkitPermModel() {
		this(new DelegateMapModel<>(new EnumMap<>(Toolkit.Method.class),
				Collections.newSetFromMap(new WeakHashMap<>())));
	}

	/**
	 * 生成一个具有指定代理的工具包权限模型。
	 * 
	 * @param delegate
	 *            指定的代理。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public DefaultToolkitPermModel(MapModel<Method, Integer> delegate) {
		Objects.requireNonNull(delegate, "入口参数 delegate 不能为 null。");

		this.delegate = delegate;
		delegate.addObverser(transObv);
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
	public Integer get(Object key) {
		return delegate.get(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer put(Method key, Integer value) {
		return delegate.put(key, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer remove(Object key) {
		return delegate.remove(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void putAll(Map<? extends Method, ? extends Integer> m) {
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
	public Set<Method> keySet() {
		return delegate.keySet();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Integer> values() {
		return delegate.values();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<java.util.Map.Entry<Method, Integer>> entrySet() {
		return delegate.entrySet();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getDpl() {
		return dpl;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean setDpl(int value) {
		if (Objects.equals(dpl, value)) {
			return false;
		}

		int oldValue = this.dpl;
		this.dpl = value;
		fireDplChanged(oldValue, value);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasPerm(Method method, int permLevel) {
		Objects.requireNonNull(method, "入口参数 method 不能为 null。");
		Objects.requireNonNull(permLevel, "入口参数 permLevel 不能为 null。");

		int mpl = delegate.getOrDefault(method, dpl);
		return permLevel >= mpl;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((delegate == null) ? 0 : delegate.hashCode());
		result = prime * result + dpl;
		result = prime * result + ((transObv == null) ? 0 : transObv.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getPermLevel(Method method) {
		Objects.requireNonNull(method, "入口参数 method 不能为 null。");
		return delegate.getOrDefault(method, dpl);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof DefaultToolkitPermModel))
			return false;
		DefaultToolkitPermModel other = (DefaultToolkitPermModel) obj;
		if (delegate == null) {
			if (other.delegate != null)
				return false;
		} else if (!delegate.equals(other.delegate))
			return false;
		if (dpl != other.dpl)
			return false;
		if (transObv == null) {
			if (other.transObv != null)
				return false;
		} else if (!transObv.equals(other.transObv))
			return false;
		return true;
	}

	/**
	 * 通知模型中的默认权限等级被改变。
	 * 
	 * @param oldValue
	 *            旧的默认权限等级。
	 * @param newValue
	 *            新的默认权限等级。
	 */
	protected void fireDplChanged(Integer oldValue, Integer newValue) {
		for (MapObverser<Toolkit.Method, Integer> obverser : obversers) {
			if (Objects.nonNull(obverser))
				try {
					if (obverser instanceof ToolkitPermObverser) {
						((ToolkitPermObverser) obverser).fireDplChanged(oldValue, newValue);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}

}
