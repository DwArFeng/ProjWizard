package com.dwarfeng.projwiz.api;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.dwarfeng.dutil.basic.io.ByteBufferInputStream;
import com.dwarfeng.projwiz.core.model.struct.FileProcessor;

/**
 * 被创造的文件。
 * <p>
 * 使用该类以快速的配置 {@link FileProcessor#newFile()}。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class CreatedFile extends AbstractFile {

	private final Map<String, ByteBuffer> buffers;

	private long length = -1;

	/**
	 * 新实例。
	 * 
	 * @param registerKey
	 *            指定的注册键。
	 * @param isFolder
	 *            是否是文件夹。
	 * @param name
	 *            文件的名称
	 * @param buffers
	 *            指定的缓冲映射。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	public CreatedFile(String registerKey, boolean isFolder, String name, Map<String, ByteBuffer> buffers) {
		this(registerKey, isFolder, name, buffers, -1, System.currentTimeMillis(), -1);
	}

	/**
	 * 新实例。
	 * 
	 * @param registerKey
	 *            指定的注册键。
	 * @param isFolder
	 *            是否是文件夹。
	 * @param name
	 *            文件的名称。
	 * @param buffers
	 *            指定的缓冲映射。
	 * @param accessTime
	 *            文件的访问时间。
	 * @param createTime
	 *            文件的创建时间。
	 * @param modifyTime
	 *            文件的编辑时间。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public CreatedFile(String registerKey, boolean isFolder, String name, Map<String, ByteBuffer> buffers,
			long accessTime, long createTime, long modifyTime) {
		super(registerKey, isFolder, name, accessTime, createTime, modifyTime);

		Objects.requireNonNull(buffers, "入口参数 buffers 不能为 null。");
		this.buffers = buffers;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean acceptIn(String key) {
		return false;
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

}