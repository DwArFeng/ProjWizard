package com.dwarfeng.projwiz.core.model.struct;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

import com.dwarfeng.dutil.basic.io.FileUtil;
import com.dwarfeng.dutil.basic.prog.Filter;

/**
 * 默认的元数据仓库。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public final class DefaultMetaDataStorage implements MetaDataStorage {

	/** 仓库的根目录 */
	protected final File rootDir;
	/** 仓库的类别。 */
	protected final String classify;

	/**
	 * 新实例。
	 * 
	 * @param rootDir
	 *            指定的根目录。
	 * @param classify
	 *            指定的类别。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	public DefaultMetaDataStorage(File rootDir, String classify) {
		Objects.requireNonNull(rootDir, "入口参数 rootDir 不能为 null。");
		Objects.requireNonNull(classify, "入口参数 classify 不能为 null。");

		this.rootDir = rootDir;
		this.classify = classify;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean create(String fileName) throws IOException {
		File file = new File(getStorageDir(), fileName);

		if (file.exists())
			return false;

		FileUtil.createFileIfNotExists(file);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean delete(String fileName) throws IOException {
		File file = new File(getStorageDir(), fileName);

		if (!file.exists())
			return false;

		return FileUtil.deleteFile(file);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream openInputStream(String fileName) throws IOException {
		File file = new File(getStorageDir(), fileName);

		if (!file.exists())
			throw new FileNotFoundException(String.format("无法在元数据仓库中找到文件: %s", fileName));

		return new FileInputStream(file);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OutputStream openOutputStream(String fileName) throws IOException {
		File file = new File(getStorageDir(), fileName);

		if (!file.exists())
			throw new FileNotFoundException(String.format("无法在元数据仓库中找到文件: %s", fileName));

		return new FileOutputStream(file);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] listFileName() {
		// File file = new File(getStorageDir(), fileName);
		// if (!file.exists())
		// return new String[0];
		//
		// return FileUtil.listAllFile(file)

		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] listFileName(Filter<String> filter) {
		// TODO Auto-generated method stub
		return null;
	}

	private File getStorageDir() {
		return new File(rootDir, classify);
	}

}
