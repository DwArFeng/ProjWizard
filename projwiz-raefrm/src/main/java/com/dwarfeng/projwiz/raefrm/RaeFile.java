package com.dwarfeng.projwiz.raefrm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.dwarfeng.dutil.basic.str.Name;
import com.dwarfeng.dutil.develop.resource.Resource;
import com.dwarfeng.dutil.develop.resource.ResourceHandler;
import com.dwarfeng.projwiz.core.model.obv.FileObverser;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.FileProcessor;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;
import com.dwarfeng.projwiz.raefrm.model.struct.ProjProcToolkit;

/**
 * Rae框架文件。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public abstract class RaeFile implements File {

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
	protected final Set<FileObverser> obversers;

	/** 同步读写锁。 */
	protected final ReadWriteLock lock = new ReentrantReadWriteLock();
	/** 文件是否为文件夹。 */
	protected final boolean isFolder;
	/** 对应的工程处理器的工具包。 */
	protected final ProjProcToolkit projprocToolkit;
	/** 文件的类型。 */
	protected final Name fileType;

	/** 文件的处理器类。 */
	protected Class<? extends FileProcessor> processorClass;
	/** 文件的访问时间。 */
	protected long accessTime;
	/** 文件的创建时间。 */
	protected long createTime;
	/** 文件的编辑时间。 */
	protected long modifyTime;

	/**
	 * 新实例。
	 * 
	 * @param isFolder
	 *            是否为文件夹。
	 * @param projprocToolkit
	 *            对应的工程处理器的工具包。
	 * @param fileType
	 *            文件的类型。
	 * @param processorClass
	 *            文件的处理器类。
	 * @param name
	 *            文件的名称。
	 * @param accessTime
	 *            文件的接触时间。
	 * @param createTime
	 *            文件的创建时间。
	 * @param modifyTime
	 *            文件的编辑时间。
	 * @param obversers
	 *            文件的观察器集合。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected RaeFile(boolean isFolder, ProjProcToolkit projprocToolkit, Name fileType,
			Class<? extends FileProcessor> processorClass, long accessTime, long createTime, long modifyTime,
			Set<FileObverser> obversers) {
		Objects.requireNonNull(projprocToolkit, "入口参数 projprocToolkit 不能为 null。");
		Objects.requireNonNull(fileType, "入口参数 fileType 不能为 null。");
		Objects.requireNonNull(obversers, "入口参数 obversers 不能为 null。");

		this.isFolder = isFolder;
		this.projprocToolkit = projprocToolkit;
		this.fileType = fileType;
		this.processorClass = processorClass;
		this.accessTime = accessTime;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
		this.obversers = obversers;
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
	public Name getFileType() {
		return fileType;
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
			long totleLength = 0;
			for (String label : getLabels()) {
				long length = getLength(label);
				if (length < 0)
					return -1;
				totleLength += length;
			}
			return totleLength;
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
	public Class<? extends FileProcessor> getProcessorClass() {
		lock.readLock().lock();
		try {
			return getProcessorClass();
		} finally {
			lock.readLock().unlock();
		}
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
	public void setProcessorClass(Class<? extends FileProcessor> clazz) {
		lock.writeLock().lock();
		try {
			if (Objects.equals(processorClass, clazz)) {
				return;
			}

			Class<? extends FileProcessor> oldValue = this.processorClass;
			this.processorClass = clazz;

			fireProcessorClassChanged(oldValue, clazz);
		} finally {
			lock.writeLock().unlock();
		}
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
			try {
				obverser.fireAccessTimeChanged(oldValue, newValue);
			} catch (Exception e) {
				e.printStackTrace();
			}
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
			try {
				obverser.fireCreateTimeChanged(oldValue, newValue);
			} catch (Exception e) {
				e.printStackTrace();
			}
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
			try {
				obverser.fireInputClosed(label);
			} catch (Exception e) {
				e.printStackTrace();
			}
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
			try {
				obverser.fireInputOpened(label);
			} catch (Exception e) {
				e.printStackTrace();
			}
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
			try {
				obverser.fireLabelAdded(label);
			} catch (Exception e) {
				e.printStackTrace();
			}
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
			try {
				obverser.fireLabelRemoved(label);
			} catch (Exception e) {
				e.printStackTrace();
			}
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
			try {
				obverser.fireLengthChanged(oldValue, newValue);
			} catch (Exception e) {
				e.printStackTrace();
			}
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
			try {
				obverser.fireModifyTimeChanged(oldValue, newValue);
			} catch (Exception e) {
				e.printStackTrace();
			}
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
			try {
				obverser.fireOccupiedSizeChanged(oldValue, newValue);
			} catch (Exception e) {
				e.printStackTrace();
			}
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
			try {
				obverser.fireOutputClosed(label);
			} catch (Exception e) {
				e.printStackTrace();
			}
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
			try {
				obverser.fireOutputOpened(label);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * 通知文件的处理器类发生了改变。
	 * 
	 * @param oldValue
	 *            旧的处理器类。
	 * @param newValue
	 *            新的处理器类。
	 */
	protected void fireProcessorClassChanged(Class<? extends FileProcessor> oldValue,
			Class<? extends FileProcessor> newValue) {
		obversers.forEach(obverser -> {
			obverser.fireProcessorClassChanged(oldValue, newValue);
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
			try {
				obverser.fireReadSupportedChanged(newValue);
			} catch (Exception e) {
				e.printStackTrace();
			}
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
			try {
				obverser.fireWriteSupportedChanged(newValue);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * 向记录器中格式化输出一条信息。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @param args
	 *            参数。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void formatInfo(Name name, Object... args) {
		Objects.requireNonNull(name, "入口参数 loggerStringKey 不能为 null。");
		Objects.requireNonNull(args, "入口参数 args 不能为 null。");

		getToolkit().info(String.format(projprocToolkit.getLoggerI18nHandler().getStringOrDefault(name,
				com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL), args));
	}

	/**
	 * 向记录器中格式化输出一条警告。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @param args
	 *            参数。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void formatWarn(Name name, Object... args) {
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");
		Objects.requireNonNull(args, "入口参数 args 不能为 null。");

		getToolkit().warn(String.format(projprocToolkit.getLoggerI18nHandler().getStringOrDefault(name,
				com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL), args));
	}

	/**
	 * 向记录器中格式化输出一条警告。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @param e
	 *            指定的可抛出对象。
	 * @param args
	 *            参数。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void formatWarn(Name name, Throwable e, Object... args) {
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");
		Objects.requireNonNull(e, "入口参数 e 不能为 null。");
		Objects.requireNonNull(args, "入口参数 args 不能为 null。");

		getToolkit().warn(String.format(projprocToolkit.getLoggerI18nHandler().getStringOrDefault(name,
				com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL), args), e);
	}

	/**
	 * 获取组件的工具包。
	 * 
	 * @return 组件的工具包。
	 */
	protected final Toolkit getToolkit() {
		return projprocToolkit.getToolkit();
	}

	/**
	 * 向记录器中输出一条信息。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void info(Name name) {
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");

		getToolkit().info(projprocToolkit.getLoggerI18nHandler().getStringOrDefault(name,
				com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL));
	}

	/**
	 * 返回此组件的工具包是否不满足指定的权限键对应的所有需求。
	 * 
	 * @param permKey
	 *            指定的权限键。
	 * @return 此组件的工具包是否不满足指定的权限键对应的所有需求。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected boolean isNotPermKeyAvaliable(Name permKey) {
		Objects.requireNonNull(permKey, "入口参数 permKey 不能为 null。");
		return projprocToolkit.getPermDemandModel().isNotPermKeyAvaliable(permKey, getToolkit());
	}

	/**
	 * 返回此组件的工具包是否能满足指定的权限键对应的所有需求。
	 * 
	 * @param permKey
	 *            含有指定的权限键的名称对象。
	 * @return 此组件的工具包是否能满足指定的权限键对应的所有需求。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected boolean isPermKeyAvailable(Name permKey) {
		Objects.requireNonNull(permKey, "入口参数 permKey 不能为 null。");
		return projprocToolkit.getPermDemandModel().isPermKeyAvailable(permKey, getToolkit());
	}

	/**
	 * 打开指定资源键对应的资源。
	 * 
	 * <p>
	 * 该方法通过 Toolkit 获取只读配置处理器，随后获取指定资源键对应的资源，在资源自动重置之后，打开输入流。
	 * 
	 * @param resourceKey
	 *            指定的资源键对应的名称接口。
	 * @return 指定资源对应的资源键。
	 * @throws IOException
	 *             IO异常。
	 */
	protected InputStream openResource(Name resourceKey) throws IOException {
		return openResource(resourceKey, true);
	}

	/**
	 * 打开指定资源键对应的资源。
	 * 
	 * <p>
	 * 该方法通过 Toolkit 获取只读配置处理器，随后获取指定资源键对应的资源，打开输入流。
	 * 
	 * @param resourceKey
	 *            指定的资源键对应的名称接口。
	 * @param autoReset
	 *            是否自动重置。
	 * @return 指定资源对应的资源键。
	 * @throws IOException
	 *             IO异常。
	 */
	protected InputStream openResource(Name resourceKey, boolean autoReset) throws IOException {
		Objects.requireNonNull(resourceKey, "入口参数 resourceKey 不能为 null。");

		ResourceHandler cfgHandlerReadOnly = getToolkit().getCfgHandlerReadOnly();
		if (!cfgHandlerReadOnly.containsKey(resourceKey)) {
			throw new IOException(String.format("不存在指定的资源: %s", resourceKey.getName()));
		}

		Resource resource = cfgHandlerReadOnly.get(resourceKey.getName());
		if (autoReset) {
			resource.autoReset();
		}
		return resource.openInputStream();
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

	/**
	 * 向记录器中输出一条信息。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void warn(Name name) {
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");

		getToolkit().warn(projprocToolkit.getLoggerI18nHandler().getStringOrDefault(name,
				com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL));
	}

	/**
	 * 向记录器中输出一条警告。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @param e
	 *            指定的可抛出对象。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void warn(Name name, Throwable e) {
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");

		getToolkit().warn(projprocToolkit.getLoggerI18nHandler().getStringOrDefault(name,
				com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL), e);
	}

}
