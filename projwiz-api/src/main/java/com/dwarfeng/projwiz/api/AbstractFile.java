package com.dwarfeng.projwiz.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.dwarfeng.projwiz.core.model.obv.FileObverser;
import com.dwarfeng.projwiz.core.model.struct.File;

/**
 * 抽象文件。
 * 
 * <p>
 * 文件的抽象实现。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public abstract class AbstractFile implements File {

	/**
	 * 可提供观察的输入流。
	 * 
	 * <p>
	 * 该输入流在打开和关闭的时候会触发侦听。
	 * 
	 * @author DwArFeng
	 * @since 0.0.1-alpha
	 */
	protected class ObversableInputStream extends InputStream {

		private final InputStream delegate;
		private final String label;

		/**
		 * 新实例。
		 * 
		 * @param delegate
		 *            代理输入流。
		 * @param label
		 *            该输入流对应的标签。
		 * @throws NullPointerException
		 *             入口参数为 <code>null</code>。
		 */
		public ObversableInputStream(String label, InputStream delegate) {
			Objects.requireNonNull(label, "入口参数 label 不能为 null。");
			Objects.requireNonNull(delegate, "入口参数 delegate 不能为 null。");

			this.delegate = delegate;
			this.label = label;

			fireInputOpened(label);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int available() throws IOException {
			return delegate.available();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void close() throws IOException {
			try {
				delegate.close();
			} finally {
				fireInputClosed(label);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object obj) {
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
		public void mark(int readlimit) {
			delegate.mark(readlimit);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean markSupported() {
			return delegate.markSupported();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int read() throws IOException {
			return delegate.read();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int read(byte[] b) throws IOException {
			return delegate.read(b);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int read(byte[] b, int off, int len) throws IOException {
			return delegate.read(b, off, len);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void reset() throws IOException {
			delegate.reset();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public long skip(long n) throws IOException {
			return delegate.skip(n);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return delegate.toString();
		}

	}

	/**
	 * 可提供观察的输出流。
	 * 
	 * <p>
	 * 该输出流在打开和关闭的时候会触发侦听。
	 * 
	 * @author DwArFeng
	 * @since 0.0.1-alpha
	 */
	protected class ObversableOutputStream extends OutputStream {

		private final OutputStream delegate;
		private final String label;

		/**
		 * 新实例。
		 * 
		 * @param delegate
		 *            代理输出流。
		 * @param label
		 *            该输出流对应的标签。
		 * @throws NullPointerException
		 *             入口参数为 <code>null</code>。
		 */
		public ObversableOutputStream(String label, OutputStream delegate) {
			Objects.requireNonNull(label, "入口参数 label 不能为 null。");
			Objects.requireNonNull(delegate, "入口参数 delegate 不能为 null。");

			this.delegate = delegate;
			this.label = label;

			fireOutputOpened(label);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void close() throws IOException {
			try {
				delegate.close();
			} finally {
				fireOutputClosed(label);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object obj) {
			return delegate.equals(obj);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void flush() throws IOException {
			delegate.flush();
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

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void write(byte[] b) throws IOException {
			delegate.write(b);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			delegate.write(b, off, len);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void write(int b) throws IOException {
			delegate.write(b);
		}

	}

	/** 观察器集合。 */
	protected final Set<FileObverser> obversers = Collections.newSetFromMap(new WeakHashMap<>());;

	/** 同步读写锁。 */
	protected final ReadWriteLock lock = new ReentrantReadWriteLock();

	/** 文件的独立标签 */
	protected final String uniqueLabel;
	/** 文件的注册键。 */
	protected final String registerKey;
	/** 文件是否为文件夹。 */
	protected final boolean isFolder;

	/** 文件的名称。 */
	protected String name;
	/** 文件的访问时间。 */
	protected long accessTime;
	/** 文件的创建时间。 */
	protected long createTime;
	/** 文件的编辑时间。 */
	protected long modifyTime;

	/**
	 * 新实例。
	 * 
	 * @param uniqueLabel
	 *            文件的独立标签。
	 * @param registerKey
	 *            指定的注册键。
	 * @param isFolder
	 *            是否是文件夹。
	 * @param name
	 *            文件的名称。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public AbstractFile(String uniqueLabel, String registerKey, boolean isFolder, String name) {
		this(uniqueLabel, registerKey, isFolder, name, -1, -1, -1);
	}

	/**
	 * 新实例。
	 * 
	 * @param uniqueLabel
	 *            文件的独立标签。
	 * @param registerKey
	 *            指定的注册键。
	 * @param isFolder
	 *            是否是文件夹。
	 * @param name
	 *            文件的名称。
	 * @param accessTime
	 *            文件的接触时间。
	 * @param createTime
	 *            文件的创建时间。
	 * @param modifyTime
	 *            文件的编辑时间。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public AbstractFile(String uniqueLabel, String registerKey, boolean isFolder, String name, long accessTime,
			long createTime, long modifyTime) {
		Objects.requireNonNull(uniqueLabel);
		Objects.requireNonNull(obversers, "入口参数 obversers 不能为 null。");
		Objects.requireNonNull(registerKey, "入口参数 registerKey 不能为 null。");
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");

		this.uniqueLabel = uniqueLabel;
		this.registerKey = registerKey;
		this.isFolder = isFolder;
		this.name = name;
		this.accessTime = accessTime;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean acceptIn(String key) {
		lock.readLock().lock();
		try {
			return isFolder;
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addObverser(FileObverser obverser) {
		lock.writeLock().lock();
		try {
			return obversers.add(obverser);
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
			obversers.clear();
		} finally {
			lock.writeLock().unlock();
		}
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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof File))
			return false;
		File other = (File) obj;
		if (uniqueLabel == null) {
			if (other.getUniqueLabel() != null)
				return false;
		} else if (!uniqueLabel.equals(other.getUniqueLabel()))
			return false;
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getAccessTime() {
		lock.readLock().lock();
		try {
			return accessTime;
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getCreateTime() {
		lock.readLock().lock();
		try {
			return createTime;
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<String> getLabels() {
		throw new UnsupportedOperationException("getLabels");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getLength() {
		lock.readLock().lock();
		try {
			long length = 0;
			for (String label : getLabels()) {
				length += getLength(label);
			}
			return length;
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getLength(String label) {
		return -1;
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
	public long getModifyTime() {
		lock.readLock().lock();
		try {
			return modifyTime;
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		lock.readLock().lock();
		try {
			return name;
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<FileObverser> getObversers() {
		lock.readLock().lock();
		try {
			return Collections.unmodifiableSet(obversers);
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getOccupiedSize() {
		return -1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getRegisterKey() {
		return registerKey;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUniqueLabel() {
		return uniqueLabel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uniqueLabel == null) ? 0 : uniqueLabel.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFolder() {
		return isFolder;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isReadSupported() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWriteSupported() {
		return false;
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
		lock.writeLock().lock();
		try {
			return obversers.remove(obverser);
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.getClass().toString() + " [getRegisterKey()=" + getRegisterKey() + ", getName()=" + getName()
				+ ", isFolder()=" + isFolder() + ", getAccessTime()=" + getAccessTime() + ", getCreateTime()="
				+ getCreateTime() + ", getModifyTime()=" + getModifyTime() + "]";
	}

	/**
	 * 通知访问时间发生改变。
	 * 
	 * @param oldValue
	 *            旧的访问时间。
	 * @param newValue
	 *            新的访问时间。
	 */
	protected void fireAccessTimeChanged(long oldValue, long newValue) {
		obversers.forEach(obverser -> {
			obverser.fireAccessTimeChanged(oldValue, newValue);
		});
	}

	/**
	 * 通知创建时间发生改变。
	 * 
	 * @param oldValue
	 *            旧的创建时间。
	 * @param newValue
	 *            新的创建时间。
	 */
	protected void fireCreateTimeChanged(long oldValue, long newValue) {
		obversers.forEach(obverser -> {
			obverser.fireCreateTimeChanged(oldValue, newValue);
		});
	}

	/**
	 * 通知指定的文件的指定标签所在的输入流被关闭。
	 * 
	 * @param label
	 *            指定的标签。
	 */
	protected void fireInputClosed(String label) {
		obversers.forEach(obverser -> {
			obverser.fireInputClosed(label);
		});
	}

	/**
	 * 通知指定的文件的指定标签所在的输入流被打开。
	 * 
	 * @param label
	 *            指定的标签。
	 */
	protected void fireInputOpened(String label) {
		obversers.forEach(obverser -> {
			obverser.fireInputOpened(label);
		});
	}

	/**
	 * 通知指定的标签被添加。
	 * 
	 * @param label
	 *            指定的标签。
	 */
	protected void fireLabelAdded(String label) {
		obversers.forEach(obverser -> {
			obverser.fireLabelAdded(label);
		});
	}

	/**
	 * 通知指定的标签被移除。
	 * 
	 * @param label
	 *            指定的标签。
	 */
	protected void fireLabelRemoved(String label) {
		obversers.forEach(obverser -> {
			obverser.fireLabelRemoved(label);
		});
	}

	/**
	 * 通知文件的长度发生改变。
	 * 
	 * @param oldValue
	 *            旧的长度。
	 * @param newValue
	 *            新的长度。
	 */
	protected void fireLengthChanged(long oldValue, long newValue) {
		obversers.forEach(obverser -> {
			obverser.fireLengthChanged(oldValue, newValue);
		});
	}

	/**
	 * 通知编辑时间发生改变。
	 * 
	 * @param oldValue
	 *            旧的编辑时间。
	 * @param newValue
	 *            新的编辑时间。
	 */
	protected void fireModifyTimeChanged(long oldValue, long newValue) {
		obversers.forEach(obverser -> {
			obverser.fireModifyTimeChanged(oldValue, newValue);
		});
	}

	/**
	 * 通知文件的占用大小发生了改变。
	 * 
	 * @param oldValue
	 *            旧的占用大小。
	 * @param newValue
	 *            新的占用大小。
	 */
	protected void fireOccupiedSizeChanged(long oldValue, long newValue) {
		obversers.forEach(obverser -> {
			obverser.fireOccupiedSizeChanged(oldValue, newValue);
		});
	}

	/**
	 * 通知是定的文件的指定标签所在的输出流被关闭。
	 * 
	 * @param label
	 *            指定的标签。
	 */
	protected void fireOutputClosed(String label) {
		obversers.forEach(obverser -> {
			obverser.fireOutputClosed(label);
		});
	}

	/**
	 * 通知指定的文件的指定标签所在的输出流被打开。
	 * 
	 * @param file
	 *            指定的文件。
	 * @param label
	 *            指定的标签。
	 */
	protected void fireOutputOpened(String label) {
		obversers.forEach(obverser -> {
			obverser.fireOutputOpened(label);
		});
	}

	/**
	 * 通知文件的读支持发生了改变。
	 * 
	 * @param newValue
	 *            新的值。
	 */
	protected void fireReadSupportedChanged(boolean newValue) {
		obversers.forEach(obverser -> {
			obverser.fireReadSupportedChanged(newValue);
		});
	}

	/**
	 * 通知文件的写支持发生了改变。
	 * 
	 * @param newValue
	 *            新的值。
	 */
	protected void fireWriteSupportedChanged(boolean newValue) {
		obversers.forEach(obverser -> {
			obverser.fireWriteSupportedChanged(newValue);
		});
	}

	/**
	 * 设置文件的访问时间。
	 * 
	 * @param accessTime
	 *            文件的访问时间。
	 */
	protected void setAccessTime(long accessTime) {
		this.accessTime = accessTime;
	}

	/**
	 * 设置文件的创建时间。
	 * 
	 * @param createTime
	 *            文件的创建时间。
	 */
	protected void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	/**
	 * 设置文件的编辑时间。
	 * 
	 * @param modifyTime
	 *            文件的编辑时间。
	 */
	protected void setModifyTime(long modifyTime) {
		this.modifyTime = modifyTime;
	}

}