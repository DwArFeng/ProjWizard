package com.dwarfeng.projwiz.core.util;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import com.dwarfeng.dutil.basic.cna.model.KeySetModel;
import com.dwarfeng.dutil.basic.cna.model.obv.MapObverser;
import com.dwarfeng.dutil.basic.cna.model.obv.SetObverser;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.dutil.basic.prog.ReadOnlyGenerator;
import com.dwarfeng.dutil.basic.threads.ThreadUtil;
import com.dwarfeng.projwiz.core.model.cm.ComponentModel;
import com.dwarfeng.projwiz.core.model.cm.SyncComponentModel;
import com.dwarfeng.projwiz.core.model.cm.SyncToolkitPermModel;
import com.dwarfeng.projwiz.core.model.cm.ToolkitPermModel;
import com.dwarfeng.projwiz.core.model.cm.Tree;
import com.dwarfeng.projwiz.core.model.eum.IconVariability;
import com.dwarfeng.projwiz.core.model.io.PluginClassLoader;
import com.dwarfeng.projwiz.core.model.obv.EditorObverser;
import com.dwarfeng.projwiz.core.model.obv.FileObverser;
import com.dwarfeng.projwiz.core.model.obv.ProjectObverser;
import com.dwarfeng.projwiz.core.model.struct.Component;
import com.dwarfeng.projwiz.core.model.struct.Editor;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.FileProcessor;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.ProjectProcessor;
import com.dwarfeng.projwiz.core.model.struct.PropSuppiler;
import com.dwarfeng.projwiz.core.model.struct.Toolkit.Method;

/**
 * 模型工具。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public final class ModelUtil {

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
		public boolean containsAllKey(Collection<?> c) {
			lock.readLock().lock();
			try {
				return delegate.containsAllKey(c);
			} finally {
				lock.readLock().unlock();
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
		public Component get(String key) {
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
		public <T extends Component> T get(String key, Class<T> clas) throws ClassCastException, NullPointerException {
			lock.readLock().lock();
			try {
				return delegate.get(key, clas);
			} finally {
				lock.readLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public <T extends Component> KeySetModel<String, T> getAll(Class<T> clas) throws NullPointerException {
			lock.readLock().lock();
			try {
				return delegate.getAll(clas);
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
		public int hashCode() {
			return delegate.hashCode();
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
		public boolean removeAllKey(Collection<?> c) {
			lock.writeLock().lock();
			try {
				return delegate.removeAllKey(c);
			} finally {
				lock.writeLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean removeKey(Object key) {
			lock.writeLock().lock();
			try {
				return delegate.removeKey(key);
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
		public boolean retainAllKey(Collection<?> c) {
			lock.writeLock().lock();
			try {
				return delegate.retainAllKey(c);
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

	private static final class UnmodifiableEditor implements Editor {

		private final Editor delegate;

		public UnmodifiableEditor(Editor delegate) {
			this.delegate = delegate;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean addObverser(EditorObverser obverser) {
			throw new UnsupportedOperationException("addObverser");
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
		public File getEditFile() {
			return unmodifiableFile(delegate.getEditFile());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public java.awt.Component getEditorView() {
			throw new UnsupportedOperationException("getEditorView");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Project getEditProject() {
			return unmodifiableProject(delegate.getEditProject());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ReadWriteLock getLock() {
			return ThreadUtil.unmodifiableReadWriteLock(delegate.getLock());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<EditorObverser> getObversers() {
			return delegate.getObversers();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getTitle() {
			return delegate.getTitle();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isSaveSupported() {
			return delegate.isSaveSupported();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isStopSuggest() {
			return delegate.isStopSuggest();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean removeObverser(EditorObverser obverser) {
			throw new UnsupportedOperationException("removeObverser");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void save() {
			throw new UnsupportedOperationException("save");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void stop() {
			throw new UnsupportedOperationException("stopEdit");
		}

	}

	private static final class UnmodifiableFile implements File {

		private final File delegate;

		public UnmodifiableFile(File delegate) {
			this.delegate = delegate;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean acceptIn(String key) {
			return delegate.acceptIn(key);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean addObverser(FileObverser obverser) {
			throw new UnsupportedOperationException("addObverser");
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
		public void discardLabel(String label) throws IOException {
			throw new UnsupportedOperationException("discardLabel");
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
		public long getAccessTime() {
			return delegate.getAccessTime();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public long getCreateTime() {
			return delegate.getCreateTime();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<String> getLabels() {
			return delegate.getLabels();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public long getLength() {
			return delegate.getLength();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public long getLength(String label) {
			return delegate.getLength(label);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ReadWriteLock getLock() {
			return delegate.getLock();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public long getModifyTime() {
			return delegate.getModifyTime();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getName() {
			return delegate.getName();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<FileObverser> getObversers() {
			return delegate.getObversers();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public long getOccupiedSize() {
			return delegate.getOccupiedSize();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getRegisterKey() {
			return delegate.getRegisterKey();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getUniqueLabel() {
			return delegate.getUniqueLabel();
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
		public boolean isFolder() {
			return delegate.isFolder();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isReadSupported() {
			return delegate.isReadSupported();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isWriteSupported() {
			return delegate.isWriteSupported();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void newLabel(String label) throws IOException {
			throw new UnsupportedOperationException("newLabel");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public InputStream openInputStream(String label) throws IOException {
			throw new UnsupportedOperationException("openInputStream");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public OutputStream openOutputStream(String label) throws IOException {
			throw new UnsupportedOperationException("openOutputStream");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean removeObverser(FileObverser obverser) {
			throw new UnsupportedOperationException("removeObverser");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return delegate.toString();
		}

	}

	private static final class UnmodifiableFileProcessor implements FileProcessor {

		private final FileProcessor delegate;

		public UnmodifiableFileProcessor(FileProcessor delegate) {
			this.delegate = delegate;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean canOpenFile(File file) {
			return delegate.canOpenFile(file);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void dispose() {
			throw new UnsupportedOperationException("dispose");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getDescription() {
			return delegate.getDescription();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Image getFileIcon(File file) {
			return delegate.getFileIcon(file);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public IconVariability getFileIconVariability(File file) {
			return delegate.getFileIconVariability(file);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public IconVariability getFileThumbVariability(File file) {
			return delegate.getFileThumbVariability(file);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Image getIcon() {
			return delegate.getIcon();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public IconVariability getIconVarialibity() {
			return delegate.getIconVarialibity();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getKey() {
			return delegate.getKey();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ReadWriteLock getLock() {
			return delegate.getLock();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getName() {
			return delegate.getName();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public PropSuppiler getPropSuppiler(File file) {
			return delegate.getPropSuppiler(file);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Image getThumb(File file) {
			return delegate.getThumb(file);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isNewEditorSupported() {
			return delegate.isNewEditorSupported();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isNewFileSupported() {
			return delegate.isNewFileSupported();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Editor newEditor(Project editProject, File editFile) throws ProcessException {
			throw new UnsupportedOperationException("newEditor");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public File newFile() throws ProcessException {
			throw new UnsupportedOperationException("newFile");
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

	private static final class UnmodifiableProject implements Project {

		private final Project delegate;

		public UnmodifiableProject(Project delegate) {
			this.delegate = delegate;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public File addFile(File parent, File file, AddingSituation situation) {
			throw new UnsupportedOperationException("addFile");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean addObverser(ProjectObverser obverser) {
			throw new UnsupportedOperationException("addObverser");
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
		public Tree<? extends File> getFileTree() {
			// TODO 构造只读树。
			return delegate.getFileTree();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ReadWriteLock getLock() {
			return delegate.getLock();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getName() {
			return delegate.getName();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProjectObverser> getObversers() {
			return delegate.getObversers();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getRegisterKey() {
			return delegate.getRegisterKey();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getUniqueLabel() {
			return delegate.getUniqueLabel();
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
		public boolean isAddFileByCopySupported() {
			return delegate.isAddFileByCopySupported();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isAddFileByMoveSupported() {
			return delegate.isAddFileByMoveSupported();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isAddFileByNewSupported() {
			return delegate.isAddFileByNewSupported();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isRemoveFileByDeleteSupported() {
			return delegate.isRemoveFileByDeleteSupported();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isRemoveFileByMoveSupported() {
			return delegate.isRemoveFileByMoveSupported();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isRenameFileSupported() {
			return delegate.isRenameFileSupported();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isSaveSupported() {
			return delegate.isSaveSupported();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isStopSuggest() {
			return delegate.isStopSuggest();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public File removeFile(File file, RemovingSituation situation) {
			throw new UnsupportedOperationException("removeFile");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean removeObverser(ProjectObverser obverser) {
			throw new UnsupportedOperationException("removeObverser");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public File renameFile(File file, String newName) {
			throw new UnsupportedOperationException("renameFile");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void save() throws ProcessException {
			throw new UnsupportedOperationException("save");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void stop() {
			throw new UnsupportedOperationException("stop");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return delegate.toString();
		}

	}

	private static final class UnmodifiableProjectProcessor implements ProjectProcessor {

		private final ProjectProcessor delegate;

		public UnmodifiableProjectProcessor(ProjectProcessor delegate) {
			this.delegate = delegate;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void dispose() {
			throw new UnsupportedOperationException("dispose");
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
		public String getDescription() {
			return delegate.getDescription();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public PropSuppiler getFilePropSuppiler(File file) {
			throw new UnsupportedOperationException("getFilePropSuppiler");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Image getIcon() {
			return delegate.getIcon();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public IconVariability getIconVarialibity() {
			return delegate.getIconVarialibity();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getKey() {
			return delegate.getKey();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ReadWriteLock getLock() {
			return ThreadUtil.unmodifiableReadWriteLock(delegate.getLock());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getName() {
			return delegate.getName();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Image getProjectIcon(Project project) {
			return delegate.getProjectIcon(project);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public IconVariability getProjectIconFixType(Project project) {
			return delegate.getProjectIconFixType(project);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public PropSuppiler getProjectPropSuppiler(Project project) {
			throw new UnsupportedOperationException("getProjectPropSuppiler");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Image getProjectThumb(Project project) {
			return delegate.getProjectThumb(project);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public IconVariability getProjectThumbFixType(Project project) {
			return delegate.getProjectThumbFixType(project);
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
		public boolean isNewProjectSupported() {
			return delegate.isNewProjectSupported();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isOpenProjectSupported() {
			return delegate.isOpenProjectSupported();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isSaveProjectSupported() {
			return delegate.isSaveProjectSupported();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Project newProject() throws ProcessException {
			throw new UnsupportedOperationException("newProject");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Project openProject() throws ProcessException {
			throw new UnsupportedOperationException("openProject");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Project saveProject(Project project) throws ProcessException {
			throw new UnsupportedOperationException("saveProject");
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
	 * 根据指定的工程处理器模型生成一个只读的工程处理器模型。
	 * 
	 * @param projectProcessorModel
	 *            指定的工程处理器模型。
	 * @return 根据指定的工程处理器模型生成一个只读的工程处理器模型。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	public static KeySetModel<String, ProjectProcessor> readOnlyProjectProcessorModel(
			KeySetModel<String, ProjectProcessor> projectProcessorModel) {
		Objects.requireNonNull(projectProcessorModel, "入口参数 projectProcessorModel 不能为 null。");

		return readOnlyProjectProcessorModel(projectProcessorModel, projectProcessor -> {
			return unmodifableProjectProcessor(projectProcessor);
		});
	}

	/**
	 * 根据指定的工程处理器和指定的只读生成器生成一个只读的工程处理器。
	 * 
	 * @param projectProcessorModel
	 *            指定的工程处理器。
	 * @param generator
	 *            指定的只读生成器。
	 * @return 根据指定的工程处理器和指定的只读生成器生成的只读工程处理器。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	public static KeySetModel<String, ProjectProcessor> readOnlyProjectProcessorModel(
			KeySetModel<String, ProjectProcessor> projectProcessorModel,
			ReadOnlyGenerator<ProjectProcessor> generator) {
		Objects.requireNonNull(projectProcessorModel, "入口参数 projectProcessorModel 不能为 null。");

		return com.dwarfeng.dutil.basic.cna.model.ModelUtil.readOnlyKeySetModel(projectProcessorModel, generator);
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
	 * 由指定的工程处理器和指定的只读资源生成器生成一个不可编辑的工程处理器。
	 * 
	 * @param projectProcessor
	 *            指定的工程处理器。
	 * @return 由指定的工程处理器生成的不可编辑的工程处理器。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	public static ProjectProcessor unmodifableProjectProcessor(ProjectProcessor projectProcessor) {
		Objects.requireNonNull(projectProcessor, "入口参数 projectProcessor 不能为 null。");
		return new UnmodifiableProjectProcessor(projectProcessor);
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
	 * 由指定的编辑器生成的不可编辑的编辑器。
	 * 
	 * @param editor
	 *            指定的编辑器。
	 * @return 由指定的编辑器生成的不可编辑的编辑器。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public static Editor unmodifiableEditor(Editor editor) {
		Objects.requireNonNull(editor, "入口参数 editor 不能为 null。");
		return new UnmodifiableEditor(editor);
	}

	/**
	 * 根据指定的文件生成一个不可编辑的文件。
	 * 
	 * @param file
	 *            指定的文件。
	 * @return 根据指定的文件生成的不可编辑的文件。
	 */
	public static File unmodifiableFile(File file) {
		Objects.requireNonNull(file, "入口参数 file 不能为 null。");
		return new UnmodifiableFile(file);
	}

	/**
	 * 由指定的工程文件生成的不可编辑的工程文件。
	 * 
	 * @param editor
	 *            指定的工程文件。
	 * @return 由指定的工程文件生成的不可编辑的工程文件。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public static FileProcessor unmodifiableFileProcessor(FileProcessor fileProcessor) {
		Objects.requireNonNull(fileProcessor, "入口参数 fileProcessor 不能为 null。");
		return new UnmodifiableFileProcessor(fileProcessor);
	}

	/**
	 * 根据指定的工程生成一个不可编辑的工程。
	 * 
	 * @param project
	 *            指定的工程。
	 * @return 根据指定的工程生成的不可编辑的工程。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	public static Project unmodifiableProject(Project project) {
		Objects.requireNonNull(project, "入口参数 project 不能为 null。");
		return new UnmodifiableProject(project);
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
