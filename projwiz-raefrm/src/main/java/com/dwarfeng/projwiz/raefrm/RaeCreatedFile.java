package com.dwarfeng.projwiz.raefrm;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.dwarfeng.dutil.basic.io.ByteBufferInputStream;
import com.dwarfeng.dutil.basic.str.Name;
import com.dwarfeng.projwiz.core.model.obv.FileObverser;
import com.dwarfeng.projwiz.core.model.struct.FileProcessor;
import com.dwarfeng.projwiz.raefrm.model.struct.ProjProcToolkit;

/**
 * 被创造的文件。
 * <p>
 * 使用该类以快速的配置 {@link FileProcessor#newFile()}。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class RaeCreatedFile extends RaeFile {

	/**
	 * 被创造文件的构造器。
	 * 
	 * @author DwArFeng
	 * @since 0.0.3-alpha
	 */
	public static class Builder extends RaeFileBuilder {

		/** 标签-数据映射。 */
		protected Map<String, ByteBuffer> buffers = new HashMap<>();

		public Builder(boolean isFolder, ProjProcToolkit projProcToolkit, Name fileType) {
			super(isFolder, projProcToolkit, fileType);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public RaeCreatedFile build() {
			return new RaeCreatedFile(buffers, isFolder, projProcToolkit, fileType, processorClass, accessTime,
					createTime, modifyTime, obversers);
		}

		/**
		 * 获取文件的标签-数据映射。
		 * 
		 * @return 标签-数据映射。
		 */
		public Map<String, ByteBuffer> getBuffers() {
			return buffers;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Builder setAccessTime(long accessTime) {
			super.setAccessTime(accessTime);
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
		public Builder setCreateTime(long createTime) {
			super.setCreateTime(createTime);
			return this;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Builder setModifyTime(long modifyTime) {
			super.setModifyTime(modifyTime);
			return this;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Builder setObversers(Set<FileObverser> obversers) {
			super.setObversers(obversers);
			return this;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Builder setProcessorClass(Class<? extends FileProcessor> processorClass) {
			super.setProcessorClass(processorClass);
			return this;
		}

	}

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
	protected RaeCreatedFile(Map<String, ByteBuffer> buffers, boolean isFolder, ProjProcToolkit projProcToolkit,
			Name fileType, Class<? extends FileProcessor> processorClass, long accessTime, long createTime,
			long modifyTime, Set<FileObverser> obversers) {
		super(isFolder, projProcToolkit, fileType, processorClass, accessTime, createTime, modifyTime, obversers);

		Objects.requireNonNull(buffers, "入口参数 buffers 不能为 null。");
		this.buffers = buffers;
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
	public boolean isReadSupported() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream openInputStream(String label) throws IOException {
		return new ObversableInputStream(label, new ByteBufferInputStream(buffers.get(label)));
	}

}