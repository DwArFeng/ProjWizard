package com.dwarfeng.projwiz.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.dwarfeng.dutil.basic.cna.CollectionUtil;
import com.dwarfeng.dutil.basic.cna.model.obv.MapObverser;
import com.dwarfeng.dutil.basic.cna.model.obv.SetObverser;
import com.dwarfeng.projwiz.core.model.cm.ComponentModel;
import com.dwarfeng.projwiz.core.model.cm.SyncComponentModel;
import com.dwarfeng.projwiz.core.model.cm.SyncToolkitPermModel;
import com.dwarfeng.projwiz.core.model.cm.ToolkitPermModel;
import com.dwarfeng.projwiz.core.model.cm.Tree;
import com.dwarfeng.projwiz.core.model.io.PluginClassLoader;
import com.dwarfeng.projwiz.core.model.struct.Component;
import com.dwarfeng.projwiz.core.model.struct.Toolkit.Method;

/**
 * 模型工具。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public final class ModelUtil {

	private static final class UnmodifiableComponentModel implements ComponentModel {

		private final ComponentModel delegate;

		public UnmodifiableComponentModel(ComponentModel delegate) {
			this.delegate = delegate;
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
			return CollectionUtil.unmodifiableIterator(delegate.iterator());
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
			throw new UnsupportedOperationException("add");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean remove(Object o) {
			throw new UnsupportedOperationException("remove");
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
			throw new UnsupportedOperationException("addAll");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean retainAll(Collection<?> c) {
			throw new UnsupportedOperationException("retainAll");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean removeAll(Collection<?> c) {
			throw new UnsupportedOperationException("removeAll");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void clear() {
			throw new UnsupportedOperationException("clear");
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
			throw new UnsupportedOperationException("addObverser");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean removeObverser(SetObverser<Component> obverser) {
			throw new UnsupportedOperationException("removeObverser");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void clearObverser() {
			throw new UnsupportedOperationException("clearObverser");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public <T extends Component> T get(Class<T> clazz) {
			return delegate.get(clazz);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public <T extends Component> Collection<T> getSubs(Class<T> clazz) {
			return delegate.getSubs(clazz);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean containsClass(Class<?> clazz) {
			return delegate.contains(clazz);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean containsAllClass(Collection<Class<?>> c) {
			return delegate.containsAllClass(c);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean removeClass(Class<?> clazz) {
			throw new UnsupportedOperationException("removeClass");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean removeAllClass(Collection<Class<?>> c) {
			throw new UnsupportedOperationException("removeAllClass");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean retainAllClass(Collection<Class<?>> c) {
			throw new UnsupportedOperationException("retainAllClass");
		}

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
		public int hashCode() {
			return delegate.hashCode();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return delegate.toString();
		}

	}

	private static final class SyncComponentModelImpl implements SyncComponentModel {

		private final ReadWriteLock lock = new ReentrantReadWriteLock();

		private final ComponentModel delegate;

		private SyncComponentModelImpl(ComponentModel delegate) {
			this.delegate = delegate;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean add(Component e) {
			lock.writeLock().lock();
			try {
				return delegate.add(e);
			} finally {
				lock.writeLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean addAll(Collection<? extends Component> c) {
			lock.writeLock().lock();
			try {
				return delegate.addAll(c);
			} finally {
				lock.writeLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean addObverser(SetObverser<Component> obverser) {
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
		public boolean contains(Object o) {
			lock.readLock().lock();
			try {
				return delegate.contains(o);
			} finally {
				lock.readLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean containsAll(Collection<?> c) {
			lock.readLock().lock();
			try {
				return delegate.containsAll(c);
			} finally {
				lock.readLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean containsAllClass(Collection<Class<?>> c) {
			lock.readLock().lock();
			try {
				return delegate.containsAllClass(c);
			} finally {
				lock.readLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean containsClass(Class<?> clazz) {
			lock.readLock().lock();
			try {
				return delegate.containsClass(clazz);
			} finally {
				lock.readLock().unlock();
			}
		}

		@Override
		public boolean equals(Object obj) {
			lock.readLock().lock();
			try {
				if (obj == this) {
					return true;
				}

				if (obj == delegate) {
					return true;
				}

				return delegate.equals(obj);
			} finally {
				lock.readLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public <T extends Component> T get(Class<T> clazz) {
			lock.readLock().lock();
			try {
				return delegate.get(clazz);
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
		public Set<SetObverser<Component>> getObversers() {
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
		public <T extends Component> Collection<T> getSubs(Class<T> clazz) {
			lock.readLock().lock();
			try {
				return delegate.getSubs(clazz);
			} finally {
				lock.readLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode() {
			lock.readLock().lock();
			try {
				return delegate.hashCode();

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
		public Iterator<Component> iterator() {
			lock.readLock().lock();
			try {
				return delegate.iterator();
			} finally {
				lock.readLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean remove(Object o) {
			lock.writeLock().lock();
			try {
				return delegate.remove(o);
			} finally {
				lock.writeLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean removeAll(Collection<?> c) {
			lock.writeLock().lock();
			try {
				return delegate.removeAll(c);
			} finally {
				lock.writeLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean removeAllClass(Collection<Class<?>> c) {
			lock.writeLock().lock();
			try {
				return delegate.removeAllClass(c);
			} finally {
				lock.writeLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean removeClass(Class<?> clazz) {
			lock.writeLock().lock();
			try {
				return delegate.removeClass(clazz);
			} finally {
				lock.writeLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean removeObverser(SetObverser<Component> obverser) {
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
		public boolean retainAll(Collection<?> c) {
			lock.writeLock().lock();
			try {
				return delegate.retainAll(c);
			} finally {
				lock.writeLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean retainAllClass(Collection<Class<?>> c) {
			lock.writeLock().lock();
			try {
				return delegate.retainAllClass(c);
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
		public Object[] toArray() {
			lock.readLock().lock();
			try {
				return delegate.toArray();
			} finally {
				lock.readLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public <T> T[] toArray(T[] a) {
			lock.readLock().lock();
			try {
				return delegate.toArray(a);
			} finally {
				lock.readLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			lock.readLock().lock();
			try {
				return delegate.toString();
			} finally {
				lock.readLock().unlock();
			}
		}

	}

	private static final class SyncToolkitPermModelImpl implements SyncToolkitPermModel {

		private final ReadWriteLock lock = new ReentrantReadWriteLock();

		private final ToolkitPermModel delegate;

		private SyncToolkitPermModelImpl(ToolkitPermModel delegate) {
			this.delegate = delegate;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean addObverser(MapObverser<Method, Integer> obverser) {
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
		public Set<Entry<Method, Integer>> entrySet() {
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
		public boolean equals(Object obj) {
			lock.readLock().lock();
			try {
				if (obj == this)
					return true;
				if (obj == delegate)
					return true;
				return delegate.equals(obj);
			} finally {
				lock.readLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Integer get(Object key) {
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
		public int getDpl() {
			lock.readLock().lock();
			try {
				return delegate.getDpl();
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
		public Set<MapObverser<Method, Integer>> getObversers() {
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
		public int getPermLevel(Method method) {
			lock.readLock().lock();
			try {
				return delegate.getPermLevel(method);
			} finally {
				lock.readLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode() {
			lock.readLock().lock();
			try {
				return delegate.hashCode();

			} finally {
				lock.readLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasPerm(Method method, int permLevel) {
			lock.readLock().lock();
			try {
				return delegate.hasPerm(method, permLevel);
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
		public Set<Method> keySet() {
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
		public Integer put(Method key, Integer value) {
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
		public void putAll(Map<? extends Method, ? extends Integer> m) {
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
		public Integer remove(Object key) {
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
		public boolean removeObverser(MapObverser<Method, Integer> obverser) {
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
		public boolean setDpl(int value) {
			lock.writeLock().lock();
			try {
				return delegate.setDpl(value);
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
		public Collection<Integer> values() {
			lock.readLock().lock();
			try {
				return delegate.values();
			} finally {
				lock.readLock().unlock();
			}
		}

	}

	private static final class UnmodifiablePluginClassLoader implements PluginClassLoader {

		private final PluginClassLoader delegate;

		public UnmodifiablePluginClassLoader(PluginClassLoader delegate) {
			this.delegate = delegate;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void addURL(URL url) {
			throw new UnsupportedOperationException("addURL");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void clearAssertionStatus() {
			throw new UnsupportedOperationException("clearAssertionStatus");
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.io.Closeable#close()
		 */
		@Override
		public void close() throws IOException {
			throw new UnsupportedOperationException("close");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj == this)
				return true;
			if (obj == delegate)
				return true;
			return delegate.equals(obj);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public URL findResource(String name) {
			return delegate.findResource(name);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Enumeration<URL> findResources(String name) throws IOException {
			return delegate.findResources(name);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public URL getResource(String name) {
			return delegate.getResource(name);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see com.dwarfeng.projwiz.core.model.io.PluginClassLoader#getResourceAsStream(java.lang.String)
		 */
		@Override
		public InputStream getResourceAsStream(String name) {
			return delegate.getResourceAsStream(name);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Enumeration<URL> getResources(String name) throws IOException {
			return delegate.getResources(name);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see com.dwarfeng.projwiz.core.model.io.PluginClassLoader#getURLs()
		 */
		@Override
		public URL[] getURLs() {
			return delegate.getURLs();
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
		public Class<?> loadClass(String name) throws ClassNotFoundException {
			throw new UnsupportedOperationException();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setClassAssertionStatus(String className, boolean enabled) {
			throw new UnsupportedOperationException("setClassAssertionStatus");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setDefaultAssertionStatus(boolean enabled) {
			throw new UnsupportedOperationException("setDefaultAssertionStatus");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setPackageAssertionStatus(String packageName, boolean enabled) {
			throw new UnsupportedOperationException("setPackageAssertionStatus");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return delegate.toString();
		}

	}

	private static final class UnmodifiableToolkitPermModel implements ToolkitPermModel {

		private final ToolkitPermModel delegate;

		private UnmodifiableToolkitPermModel(ToolkitPermModel delegate) {
			this.delegate = delegate;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean addObverser(MapObverser<Method, Integer> obverser) {
			throw new UnsupportedOperationException("addObverser");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void clear() {
			throw new UnsupportedOperationException("clear");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void clearObverser() {
			throw new UnsupportedOperationException("clearObverser");
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
		public Set<Entry<Method, Integer>> entrySet() {
			return Collections.unmodifiableSet(delegate.entrySet());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj == this)
				return true;
			if (obj == delegate)
				return true;
			return delegate.equals(obj);
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
		public int getDpl() {
			return delegate.getDpl();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<MapObverser<Method, Integer>> getObversers() {
			return delegate.getObversers();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getPermLevel(Method method) {
			return delegate.getPermLevel(method);
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
		public boolean hasPerm(Method method, int permLevel) {
			return delegate.hasPerm(method, permLevel);
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
		public Set<Method> keySet() {
			return Collections.unmodifiableSet(delegate.keySet());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Integer put(Method key, Integer value) {
			throw new UnsupportedOperationException("put");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void putAll(Map<? extends Method, ? extends Integer> m) {
			throw new UnsupportedOperationException("putAll");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Integer remove(Object key) {
			throw new UnsupportedOperationException("remove");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean removeObverser(MapObverser<Method, Integer> obverser) {
			throw new UnsupportedOperationException("removeObverser");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean setDpl(int value) {
			throw new UnsupportedOperationException("setDpl");
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
		public Collection<Integer> values() {
			return Collections.unmodifiableCollection(delegate.values());
		}

	}

	private static class UnmodifiableTree<E> implements Tree<E> {

		private final Tree<E> delegate;

		public UnmodifiableTree(Tree<E> delegate) {
			super();
			this.delegate = delegate;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean add(com.dwarfeng.projwiz.core.model.cm.Tree.ParentChildPair<E> pair) {
			throw new UnsupportedOperationException("add");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean add(E parent, E element) {
			throw new UnsupportedOperationException("add");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean addAll(Collection<? extends com.dwarfeng.projwiz.core.model.cm.Tree.ParentChildPair<E>> c) {
			throw new UnsupportedOperationException("addAll");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean addAll(E parent, Collection<? extends E> c) {
			throw new UnsupportedOperationException("addAll");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean addRoot(E root) {
			throw new UnsupportedOperationException("addRoot");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void clear() {
			throw new UnsupportedOperationException("clear");
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
		public boolean containsAll(Collection<?> c) {
			return delegate.containsAll(c);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Collection<E> getChilds(Object o) {
			return delegate.getChilds(o);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getDepth(Object o) {
			return delegate.getDepth(o);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public E getParent(Object o) {
			return delegate.getParent(o);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public com.dwarfeng.projwiz.core.model.cm.Tree.Path<E> getPath(Object o) {
			return delegate.getPath(o);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public E getRoot() {
			return delegate.getRoot();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isAncestor(Object anscetor, Object o) {
			return delegate.isAncestor(anscetor, o);
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
		public boolean isLeaf(Object o) {
			return delegate.isLeaf(o);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isRoot(Object o) {
			return delegate.isRoot(o);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Iterator<E> iterator() {
			return CollectionUtil.unmodifiableIterator(delegate.iterator());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean remove(Object o) {
			throw new UnsupportedOperationException("remove");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean removeAll(Collection<?> c) {
			throw new UnsupportedOperationException("removeAll");
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
		public Tree<E> subTree(Object root) {
			return new UnmodifiableTree<>(delegate.subTree(root));
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

	}

	/**
	 * 根据指定的组件模型生成一个线程安全的组件模型。
	 * 
	 * @param componentModel
	 *            指定的组件模型。
	 * @return 由指定的组件模型生成的线程安全的组件模型。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public static SyncComponentModel syncComponentModel(ComponentModel componentModel) {
		Objects.requireNonNull(componentModel, "入口参数 componentModel 不能为 null。");
		return new SyncComponentModelImpl(componentModel);
	}

	/**
	 * 根据指定的组件模型生成一个不可编辑的组件模型。
	 * 
	 * @param componentModel
	 *            指定的组件模型。
	 * @return 由指定的组件模型生成的不可编辑的组件模型。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public static ComponentModel unmodifiableComponentModel(ComponentModel componentModel) {
		Objects.requireNonNull(componentModel, "入口参数 componentModel 不能为 null。");
		return new UnmodifiableComponentModel(componentModel);
	}

	/**
	 * 根据指定的工具包权限模型生成一个线程安全的工具包权限模型。
	 * 
	 * @param toolkitPermModel
	 *            指定的工具包权限模型。
	 * @return 根据指定的工具包权限模型生成的线程安全的工具包权限模型。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public static SyncToolkitPermModel syncToolkitPermModel(ToolkitPermModel toolkitPermModel) {
		Objects.requireNonNull(toolkitPermModel, "入口参数 toolkitPermModel 不能为 null。");
		return new SyncToolkitPermModelImpl(toolkitPermModel);
	}

	/**
	 * 由指定的插件读取器生成一个不可编辑的插件读取器。
	 * 
	 * @param pluginClassLoader
	 *            指定的插件读取器。
	 * @return 由指定的插件读取器生成的不可编辑的插件读取器。
	 */
	public static PluginClassLoader unmodifaiblePluginClassLoader(PluginClassLoader pluginClassLoader) {
		Objects.requireNonNull(pluginClassLoader, "入口参数 pluginClassLoader 不能为 null。");
		return new UnmodifiablePluginClassLoader(pluginClassLoader);
	}

	/**
	 * 根据指定的工具包权限模型生成的不可编辑的工具包权限模型。
	 * 
	 * @param toolkitPermModel
	 *            指定的工具包权限模型。
	 * @return 根据指定的工具包权限模型生成的不可编辑的工具包权限模型。
	 */
	public static ToolkitPermModel unmodifiableToolkitPermModel(ToolkitPermModel toolkitPermModel) {
		Objects.requireNonNull(toolkitPermModel, "入口参数 toolkitPermModel 不能为 null。");
		return new UnmodifiableToolkitPermModel(toolkitPermModel);
	}

	/**
	 * 生成一个不可编辑的树。
	 * 
	 * @param tree
	 *            指定的树。
	 * @return 根据指定的树生成的不可编辑的树。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public static <E> Tree<E> unmodifiableTree(Tree<E> tree) {
		Objects.requireNonNull(tree, "入口参数 tree 不能为 null。");
		return new UnmodifiableTree<>(tree);
	}

	// 禁止外部实例化
	private ModelUtil() {
	}

}
