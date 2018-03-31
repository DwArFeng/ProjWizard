package com.dwarfeng.projwiz.basic4.model.struct;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.dwarfeng.dutil.basic.io.ByteBufferInputStream;
import com.dwarfeng.dutil.basic.io.ByteBufferOutputStream;
import com.dwarfeng.dutil.basic.num.Interval;
import com.dwarfeng.dutil.basic.num.NumberUtil;
import com.dwarfeng.dutil.basic.str.Name;
import com.dwarfeng.projwiz.basic4.model.obv.MeppFileObverser;
import com.dwarfeng.projwiz.core.model.obv.FileObverser;
import com.dwarfeng.projwiz.core.model.struct.FileProcessor;
import com.dwarfeng.projwiz.raefrm.RaeFile;
import com.dwarfeng.projwiz.raefrm.model.struct.ProjProcToolkit;

/**
 * 内存文件。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public class MeppFile extends RaeFile {

	/**
	 * 内存文件构造器。
	 * 
	 * @author DwArFeng
	 * @since 0.0.3-alpha
	 */
	public static class Builder extends RaeFileBuilder {

		/** 标签-字节缓冲映射。 */
		protected final Map<String, ByteBuffer> buffers;

		/** 缓冲容量。 */
		protected int buffCapa = 1024;
		/** 是否可读。 */
		protected boolean readSupported = true;
		/** 是否可写。 */
		protected boolean writeSupported = true;

		/**
		 * 新实例。
		 * 
		 * @param isFolder
		 *            是否为文件夹。
		 * @param projProcToolkit
		 *            指定的工程文件处理器工具包。
		 * @param fileType
		 *            指定的文件名称。
		 * @param buffers
		 *            指定的标签-字节缓冲映射。
		 * @throws NullPointerException
		 *             入口参数为 <code>null</code>。
		 */
		public Builder(boolean isFolder, ProjProcToolkit projProcToolkit, Name fileType,
				Map<String, ByteBuffer> buffers) {
			super(isFolder, projProcToolkit, fileType);

			Objects.requireNonNull(buffers, "入口参数 buffers 不能为 null。");
			this.buffers = buffers;

		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public MeppFile build() {
			return new MeppFile(isFolder, projProcToolkit, fileType, processorClass, accessTime, modifyTime, obversers,
					buffers, buffCapa, readSupported, writeSupported);
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
		 * 设置文件的新标签分配大小。
		 * 
		 * @param buffCapa
		 *            指定的缓冲容量。
		 * @return 构造器自身。
		 */
		public Builder setBuffCapa(int buffCapa) {
			this.buffCapa = Math.max(0, buffCapa);
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

		/**
		 * 设置文件是否可读。
		 * 
		 * @param readSupported
		 *            文件是否可读。
		 * @return 构造器自身。
		 */
		public Builder setReadSupported(boolean readSupported) {
			this.readSupported = readSupported;
			return this;
		}

		/**
		 * 设置文件是否可写。
		 * 
		 * @param writeSupported
		 *            文件是否可写。
		 * @return 构造器自身。
		 */
		public Builder setWriteSupported(boolean writeSupported) {
			this.writeSupported = writeSupported;
			return this;
		}

	}

	/** 标签-字节缓冲映射。 */
	protected final Map<String, ByteBuffer> buffers;

	/** 缓冲容量。 */
	protected int buffCapa;
	/** 是否可读。 */
	protected boolean readSupported = true;
	/** 是否可写。 */
	protected boolean writeSupported = true;

	/**
	 * 新实例。
	 * 
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
	 * @param buffers
	 *            指定的标签-字节缓冲映射。
	 * @param buffCapa
	 *            字节缓冲大小。
	 * @param readSupported
	 *            是否可读。
	 * @param writeSupported
	 *            是否可写。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected MeppFile(boolean isFolder, ProjProcToolkit projProcToolkit, Name fileType,
			Class<? extends FileProcessor> processorClass, long accessTime, long modifyTime,
			Set<FileObverser> obversers, Map<String, ByteBuffer> buffers, int buffCapa, boolean readSupported,
			boolean writeSupported) {
		super(isFolder, projProcToolkit, fileType, processorClass, accessTime, modifyTime, obversers);

		Objects.requireNonNull(buffers, "入口参数 buffers 不能为 null。");
		NumberUtil.requireInInterval(buffCapa, Interval.INTERVAL_NOT_NEGATIVE, "参数 buffCapa 不能小于0。");

		this.buffers = buffers;
		this.buffCapa = buffCapa;
		this.readSupported = readSupported;
		this.writeSupported = writeSupported;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean discardLabel(String label) throws IOException {
		lock.writeLock().lock();
		try {
			if (!writeSupported) {
				throw new UnsupportedOperationException("discardLabel");
			}

			if (Objects.isNull(label)) {
				return false;
			}

			if (!buffers.containsKey(label)) {
				return false;
			}

			buffers.remove(label);
			fireLabelRemoved(label);
			return true;
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * 获取文件中的缓冲容量。
	 * 
	 * @return 文件的缓冲容量。
	 */
	public int getBuffCapa() {
		return buffCapa;
	}

	/**
	 * 获取文件中的标签-字节缓冲映射。
	 * 
	 * <p>
	 * 该映射是不可更改的。
	 * 
	 * @return 文件中的标签-字节缓冲映射。
	 */
	public Map<String, ByteBuffer> getBuffers() {
		lock.readLock().lock();
		try {
			return Collections.unmodifiableMap(buffers);
		} finally {
			lock.readLock().unlock();
		}
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
			long totle = 0;
			for (ByteBuffer buffer : buffers.values()) {
				totle += buffer.limit();
			}
			return totle;
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
			long totle = 0;
			for (ByteBuffer buffer : buffers.values()) {
				totle += buffer.capacity();
			}
			return totle;
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isReadSupported() {
		lock.readLock().lock();
		try {
			return readSupported;
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWriteSupported() {
		lock.readLock().lock();
		try {
			return writeSupported;
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean newLabel(String label) throws IOException {
		lock.writeLock().lock();
		try {
			if (!writeSupported) {
				throw new UnsupportedOperationException("newLabel");
			}

			if (Objects.isNull(label)) {
				return false;
			}

			if (buffers.containsKey(label)) {
				return false;
			}

			ByteBuffer buffer = ByteBuffer.allocate(buffCapa);
			buffers.put(label, buffer);

			fireLabelAdded(label);
			return true;
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream openInputStream(String label) throws IOException {
		lock.writeLock().lock();
		try {
			if (!readSupported) {
				throw new UnsupportedOperationException("openInputStream");
			}

			ByteBuffer buffer = buffers.get(label);
			if (Objects.isNull(buffer)) {
				throw new IllegalArgumentException("文件中不存在指定的标签: " + label);
			}
			setAccessTime(System.currentTimeMillis());
			return new ObversableInputStream(label, new ByteBufferInputStream(buffer));
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OutputStream openOutputStream(String label) throws IOException {
		lock.writeLock().lock();
		try {
			if (!writeSupported) {
				throw new UnsupportedOperationException("openOutputStream");
			}

			if (!getLabels().contains(label)) {
				throw new IOException("文件中不存在指定的标签: " + label);
			}

			ByteBuffer buffer = ByteBuffer.allocate(buffCapa);
			buffers.put(label, buffer);

			long currentTimeMillis = System.currentTimeMillis();
			setAccessTime(currentTimeMillis);
			setModifyTime(currentTimeMillis);
			return new ObversableOutputStream(label, new ByteBufferOutputStream(buffer));
		} finally {
			lock.writeLock().unlock();
		}

	}

	/**
	 * 设置文件的缓冲容量。
	 * 
	 * @param buffCapa
	 *            分配大小。
	 */
	public void setBuffCapa(int buffCapa) {
		this.buffCapa = buffCapa;
	}

	public void setReadSupported(boolean readSupported) {
		lock.writeLock().lock();
		try {
			this.readSupported = readSupported;
			fireReadSupportedChanged(readSupported);
		} finally {
			lock.writeLock().unlock();
		}
	}

	public void setWriteSupported(boolean writeSupported) {
		lock.writeLock().lock();
		try {
			this.writeSupported = writeSupported;
			fireWriteSupportedChanged(writeSupported);
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * 通知内存文件的可读性发生了改变。
	 * 
	 * @param newValue
	 *            新值。
	 */
	protected void fireReadSupportedChanged(boolean newValue) {
		obversers.forEach(obverser -> {
			try {
				if (obverser instanceof MeppFileObverser) {
					((MeppFileObverser) obverser).fireReadSupportedChanged(newValue);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * 通知内存文件的可写性发生了改变。
	 * 
	 * @param newValue
	 *            新值。
	 */
	protected void fireWriteSupportedChanged(boolean newValue) {
		obversers.forEach(obverser -> {
			try {
				if (obverser instanceof MeppFileObverser) {
					((MeppFileObverser) obverser).fireWriteSupportedChanged(newValue);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

}
