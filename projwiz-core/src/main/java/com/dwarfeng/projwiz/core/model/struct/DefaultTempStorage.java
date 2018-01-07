package com.dwarfeng.projwiz.core.model.struct;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.dwarfeng.dutil.basic.cna.model.KeySetModel;
import com.dwarfeng.dutil.basic.cna.model.MapKeySetModel;
import com.dwarfeng.dutil.basic.io.FileUtil;
import com.dwarfeng.dutil.basic.prog.WithKey;

/**
 * 默认临时仓库。
 * <p>
 * 临时仓库的默认实现。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public final class DefaultTempStorage implements TempStorage {

	private final Map<String, InputStream> openedInputStream = Collections.synchronizedMap(new HashMap<>());
	private final Map<String, OutputStream> openedOutputStream = Collections.synchronizedMap(new HashMap<>());

	private final String filePath;
	private final KeySetModel<String, TempFile> tempFileModel = new MapKeySetModel<>();

	/**
	 * 新实例
	 * 
	 * @param filePath
	 *            指定的文件路径。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	public DefaultTempStorage(String filePath) {
		Objects.requireNonNull(filePath, "入口参数 filePath 不能为 null。");
		this.filePath = filePath;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream openInputStream(String key) throws IOException {
		if (!tempFileModel.containsKey(key))
			return null;
		TempFile tempFile = tempFileModel.get(key);

		if (tempFile.isDispose())
			throw new IOException(String.format("临时文件 %s 已经被释放", key));
		if (openedInputStream.containsKey(key) || openedOutputStream.containsKey(key))
			throw new IOException(String.format("临时文件 %s 正在被使用", key));

		java.io.File file = new java.io.File(filePath, tempFile.getFileName());

		FileInputStream fin = null;
		fin = new FileInputStream(file);
		TempStorageInputStream in = new TempStorageInputStream(fin, tempFile);
		openedInputStream.put(key, in);
		return in;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OutputStream openOutputStream(String key) throws IOException {

		if (!tempFileModel.containsKey(key))
			return null;
		TempFile tempFile = tempFileModel.get(key);

		if (tempFile.isDispose())
			throw new IOException(String.format("临时文件 %s 已经被释放", key));
		if (openedInputStream.containsKey(key) || openedOutputStream.containsKey(key))
			throw new IOException(String.format("临时文件 %s 正在被使用", key));

		java.io.File file = new java.io.File(filePath, tempFile.getFileName());

		FileOutputStream fout = null;
		fout = new FileOutputStream(file);
		TempStorageOutputStream out = new TempStorageOutputStream(fout, tempFile);
		openedOutputStream.put(key, out);
		return out;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void disposeLabel(String key) throws IOException {
		if (!tempFileModel.containsKey(key))
			return;

		TempFile tempFile = tempFileModel.get(key);
		try {
			if (openedInputStream.containsKey(key)) {
				InputStream in = openedInputStream.get(key);
				in.close();
			}
			if (openedOutputStream.containsKey(key)) {
				OutputStream out = openedOutputStream.get(key);
				out.close();
			}
			tempFile.dispose();
			tempFileModel.remove(tempFile);
		} finally {
			openedInputStream.remove(key);
			openedOutputStream.remove(key);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String requireNewLabel() throws IOException {
		String testKey = UUID.randomUUID().toString();
		while (tempFileModel.containsKey(testKey)) {
			testKey = UUID.randomUUID().toString();
		}

		TempFile tempFile = new TempFile(testKey);
		tempFileModel.add(tempFile);

		java.io.File file = new java.io.File(filePath, tempFile.getFileName());

		try {
			FileUtil.createFileIfNotExists(file);
		} catch (IOException e) {
			tempFileModel.remove(tempFile);
			throw e;
		}

		return testKey;
	}

	private final class TempStorageInputStream extends InputStream {

		private final InputStream delegate;
		private final TempFile tempFile;

		public TempStorageInputStream(InputStream delegate, TempFile tempFile) {
			this.delegate = delegate;
			this.tempFile = tempFile;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int read() throws IOException {
			if (tempFile.isDispose())
				throw new IOException(String.format("临时文件 %s 已经被释放", tempFile.getKey()));
			return delegate.read();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int read(byte[] b) throws IOException {
			if (tempFile.isDispose())
				throw new IOException(String.format("临时文件 %s 已经被释放", tempFile.getKey()));
			return delegate.read(b);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int read(byte[] b, int off, int len) throws IOException {
			if (tempFile.isDispose())
				throw new IOException(String.format("临时文件 %s 已经被释放", tempFile.getKey()));
			return delegate.read(b, off, len);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public long skip(long n) throws IOException {
			if (tempFile.isDispose())
				throw new IOException(String.format("临时文件 %s 已经被释放", tempFile.getKey()));
			return delegate.skip(n);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int available() throws IOException {
			if (tempFile.isDispose())
				throw new IOException(String.format("临时文件 %s 已经被释放", tempFile.getKey()));
			return delegate.available();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void close() throws IOException {
			if (tempFile.isDispose())
				throw new IOException(String.format("临时文件 %s 已经被释放", tempFile.getKey()));
			delegate.close();
			openedInputStream.remove(tempFile.getKey());
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
		public void reset() throws IOException {
			if (tempFile.isDispose())
				throw new IOException(String.format("临时文件 %s 已经被释放", tempFile.getKey()));
			delegate.reset();
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
		public String toString() {
			return delegate.toString();
		}

	}

	private final class TempStorageOutputStream extends OutputStream {

		private final OutputStream delegate;
		private final TempFile tempFile;

		public TempStorageOutputStream(OutputStream delegate, TempFile tempFile) {
			this.delegate = delegate;
			this.tempFile = tempFile;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void write(int b) throws IOException {
			if (tempFile.isDispose())
				throw new IOException(String.format("临时文件 %s 已经被释放", tempFile.getKey()));
			delegate.write(b);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void write(byte[] b) throws IOException {
			if (tempFile.isDispose())
				throw new IOException(String.format("临时文件 %s 已经被释放", tempFile.getKey()));
			delegate.write(b);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			if (tempFile.isDispose())
				throw new IOException(String.format("临时文件 %s 已经被释放", tempFile.getKey()));
			delegate.write(b, off, len);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void flush() throws IOException {
			if (tempFile.isDispose())
				throw new IOException(String.format("临时文件 %s 已经被释放", tempFile.getKey()));
			delegate.flush();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void close() throws IOException {
			if (tempFile.isDispose())
				throw new IOException(String.format("临时文件 %s 已经被释放", tempFile.getKey()));
			delegate.close();
			openedOutputStream.remove(tempFile.getKey());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return delegate.toString();
		}

	}

	private static final class TempFile implements WithKey<String> {

		private final String key;
		private boolean disposeFlag = false;
		private final ReadWriteLock lock = new ReentrantReadWriteLock();

		public TempFile(String key) {
			this.key = key;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getKey() {
			lock.readLock().lock();
			try {
				return key;

			} finally {
				lock.readLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public String getFileName() {
			lock.readLock().lock();
			try {
				return key + ".tmp";

			} finally {
				lock.readLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean isDispose() {
			lock.readLock().lock();
			try {
				return disposeFlag;

			} finally {
				lock.readLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void dispose() throws IOException {
			lock.writeLock().lock();
			try {
				disposeFlag = true;
			} finally {
				lock.writeLock().unlock();
			}
		}

	}

}
