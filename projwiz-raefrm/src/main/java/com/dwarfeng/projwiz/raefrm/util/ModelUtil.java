package com.dwarfeng.projwiz.raefrm.util;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.dwarfeng.dutil.basic.cna.model.obv.MapObverser;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;
import com.dwarfeng.projwiz.core.model.struct.Toolkit.Method;
import com.dwarfeng.projwiz.raefrm.model.cm.PermDemandModel;
import com.dwarfeng.projwiz.raefrm.model.cm.SyncPermDemandModel;

/**
 * Rae框架模型工具。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public final class ModelUtil {

	private static final class SyncPermDemandModelImpl implements SyncPermDemandModel {

		private final PermDemandModel delegate;
		private final ReadWriteLock lock = new ReentrantReadWriteLock();

		public SyncPermDemandModelImpl(PermDemandModel delegate) {
			this.delegate = delegate;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean addObverser(MapObverser<String, Collection<Method>> obverser) {
			lock.writeLock().lock();
			try {
				return delegate.addObverser(obverser);
			} finally {
				lock.writeLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void clear() {
			lock.writeLock().lock();
			try {
				delegate.clear();
			} finally {
				lock.writeLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void clearObverser() {
			lock.writeLock().lock();
			try {
				delegate.clearObverser();
			} finally {
				lock.writeLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean containsKey(Object key) {
			lock.readLock().lock();
			try {
				return delegate.containsKey(key);
			} finally {
				lock.readLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean containsValue(Object value) {
			lock.readLock().lock();
			try {
				return delegate.containsValue(value);
			} finally {
				lock.readLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<Entry<String, Collection<Method>>> entrySet() {
			lock.readLock().lock();
			try {
				return delegate.entrySet();
			} finally {
				lock.readLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Collection<Method> get(Object key) {
			lock.readLock().lock();
			try {
				return delegate.get(key);
			} finally {
				lock.readLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ReadWriteLock getLock() {
			return lock;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<MapObverser<String, Collection<Method>>> getObversers() {
			lock.readLock().lock();
			try {
				return delegate.getObversers();
			} finally {
				lock.readLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isEmpty() {
			lock.readLock().lock();
			try {
				return delegate.isEmpty();
			} finally {
				lock.readLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isPermDemand(String permKey, Method method) {
			lock.readLock().lock();
			try {
				return delegate.isPermDemand(permKey, method);
			} finally {
				lock.readLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isPermKeyAvailable(String permKey, Toolkit toolkit) {
			lock.readLock().lock();
			try {
				return delegate.isPermKeyAvailable(permKey, toolkit);
			} finally {
				lock.readLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<String> keySet() {
			lock.readLock().lock();
			try {
				return delegate.keySet();
			} finally {
				lock.readLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Collection<Method> put(String key, Collection<Method> value) {
			lock.writeLock().lock();
			try {
				return delegate.put(key, value);
			} finally {
				lock.writeLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void putAll(Map<? extends String, ? extends Collection<Method>> m) {
			lock.writeLock().lock();
			try {
				delegate.putAll(m);
			} finally {
				lock.writeLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Collection<Method> remove(Object key) {
			lock.writeLock().lock();
			try {
				return delegate.remove(key);
			} finally {
				lock.writeLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean removeObverser(MapObverser<String, Collection<Method>> obverser) {
			lock.writeLock().lock();
			try {
				return delegate.removeObverser(obverser);
			} finally {
				lock.writeLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int size() {
			lock.readLock().lock();
			try {
				return delegate.size();
			} finally {
				lock.readLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Collection<Collection<Method>> values() {
			lock.readLock().lock();
			try {
				return delegate.values();
			} finally {
				lock.readLock().unlock();
			}
		}

	}

	/**
	 * 根据指定的权限需求模型生成的线程安全的权限需求模型。
	 * 
	 * @param permDemandModel
	 *            指定的权限需求模型。
	 * @return 根据指定的权限需求模型生成的线程安全的权限需求模型。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	public static SyncPermDemandModel syncPermDemandModel(PermDemandModel permDemandModel) {
		Objects.requireNonNull(permDemandModel, "入口参数 permDemandModel 不能为 null。");
		return new SyncPermDemandModelImpl(permDemandModel);
	}

	// 禁止外部实例化。
	private ModelUtil() {
	}

}
