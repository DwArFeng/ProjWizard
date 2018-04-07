package com.dwarfeng.projwiz.raefrm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.dwarfeng.dutil.basic.io.ByteBufferInputStream;
import com.dwarfeng.dutil.basic.prog.Buildable;
import com.dwarfeng.dutil.basic.str.Name;
import com.dwarfeng.projwiz.core.model.obv.FileObverser;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.FileProcessor;

/**
 * 被创造的文件。
 * <p>
 * 使用该类以快速的配置 {@link FileProcessor#newFile()}。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class RaeCreatedFile implements File {

	/**
	 * 被创造文件的构造器。
	 * 
	 * @author DwArFeng
	 * @since 0.0.3-alpha
	 */
	public static class Builder implements Buildable<RaeCreatedFile> {

		/** 文件是否为文件夹。 */
		protected final boolean isFolder;
		/** 文件的类型。 */
		protected final Name fileType;
		/** 文件的处理器类。 */
		protected Class<? extends FileProcessor> processorClass = null;
		/** 标签-数据映射。 */
		protected Map<String, ByteBuffer> buffers = new HashMap<>();

		/**
		 * 新实例。
		 * 
		 * @param isFolder
		 *            是否是文件夹。
		 * @param fileType
		 *            文件的类型。
		 */
		public Builder(boolean isFolder, Name fileType) {
			Objects.requireNonNull(fileType, "入口参数 fileType 不能为 null。");

			this.isFolder = isFolder;
			this.fileType = fileType;
		}

		/**
		 * 设置文件的处理器类。
		 * 
		 * @param processorClass
		 *            指定的处理器类。
		 * @return 构造器自身。
		 */
		public Builder setProcessorClass(Class<? extends FileProcessor> processorClass) {
			this.processorClass = processorClass;
			return this;
		}

		/**
		 * 设置文件的标签-数据映射。
		 * 
		 * @param buffers
		 *            指定的标签-数据映射。
		 * @return 构造器自身。
		 */
		public Builder setBuffers(Map<String, ByteBuffer> buffers) {
			this.buffers = Objects.isNull(buffers) ? new HashMap<>() : buffers;
			return this;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public RaeCreatedFile build() {
			return new RaeCreatedFile(isFolder, fileType, processorClass, buffers);
		}

	}

	/** 同步读写锁。 */
	protected final ReadWriteLock lock = new ReentrantReadWriteLock();

	/** 文件是否为文件夹。 */
	protected final boolean isFolder;
	/** 文件的类型。 */
	protected final Name fileType;
	/** 文件的处理器类。 */
	protected final Class<? extends FileProcessor> processorClass;
	/** 标签-数据映射。 */
	protected final Map<String, ByteBuffer> buffers;

	private long length = -1;

	/**
	 * 新实例。
	 * 
	 * @param buffers
	 *            指定的缓冲映射。
	 * @param isFolder
	 *            是否为文件夹。
	 * @param projProcToolkit
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
	protected RaeCreatedFile(boolean isFolder, Name fileType, Class<? extends FileProcessor> processorClass,
			Map<String, ByteBuffer> buffers) {
		Objects.requireNonNull(fileType, "入口参数 fileType 不能为 null。");
		Objects.requireNonNull(buffers, "入口参数 buffers 不能为 null。");

		this.isFolder = isFolder;
		this.fileType = fileType;
		this.buffers = buffers;
		this.processorClass = processorClass;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<FileObverser> getObversers() {
		return Collections.emptySet();
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
	public boolean removeObverser(FileObverser obverser) {
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
	public ReadWriteLock getLock() {
		return lock;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getAccessTime() {
		return -1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getModifyTime() {
		return -1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getCreateTime() {
		return -1;
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
		lock.readLock().lock();
		try {
			return Collections.unmodifiableSet(buffers.keySet());
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getLength() {
		lock.readLock().lock();
		try {
			if (length >= 0) {
				return length;
			}

			long totle = 0;
			for (ByteBuffer buffer : buffers.values()) {
				totle += buffer.limit();
			}
			length = totle;

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
		lock.readLock().lock();
		try {
			return buffers.get(label).limit();
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getOccupiedSize() {
		lock.readLock().lock();
		try {
			if (length >= 0) {
				return length;
			}

			long totle = 0;
			for (ByteBuffer buffer : buffers.values()) {
				totle += buffer.capacity();
			}
			length = totle;

			return length;
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<? extends FileProcessor> getProcessorClass() {
		lock.readLock().lock();
		try {
			return processorClass;
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAcceptSubFile(File file) {
		return true;
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
		return true;
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
	public boolean newLabel(String label) throws IOException {
		throw new UnsupportedOperationException("newLabel");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean discardLabel(String label) throws IOException {
		throw new UnsupportedOperationException("discardLabel");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream openInputStream(String label) throws IOException {
		return new ByteBufferInputStream(buffers.get(label));
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
	public void setAccessTime(long time) {
		throw new UnsupportedOperationException("setAccessTime");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setModifyTime(long time) {
		throw new UnsupportedOperationException("setModifyTime");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean setProcessorClass(Class<? extends FileProcessor> clazz) {
		throw new UnsupportedOperationException("setProcessorClass");
	}

}