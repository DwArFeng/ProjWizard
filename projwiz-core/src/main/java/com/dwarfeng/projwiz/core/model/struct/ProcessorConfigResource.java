package com.dwarfeng.projwiz.core.model.struct;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

import com.dwarfeng.dutil.basic.io.FileUtil;
import com.dwarfeng.dutil.develop.resource.AbstractResource;

/**
 * 处理器的配置资源。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public final class ProcessorConfigResource extends AbstractResource {

	private final File configFile;

	/**
	 * 新实例。
	 * 
	 * @param key
	 *            处理器的键。
	 * @param configFile
	 *            存储配置的文件。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public ProcessorConfigResource(String key, File configFile) {
		super(key);

		Objects.requireNonNull(configFile, "入口参数 configFile 不能为 null。");
		this.configFile = configFile;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream openInputStream() throws IOException {
		FileUtil.createFileIfNotExists(configFile);
		return new FileInputStream(configFile);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OutputStream openOutputStream() throws IOException {
		return new FileOutputStream(configFile);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reset() throws IOException {
		FileUtil.createFileIfNotExists(configFile);
		FileOutputStream reseter = null;
		try {
			reseter = new FileOutputStream(configFile);
			reseter.flush();
		} finally {
			if (Objects.nonNull(reseter)) {
				reseter.close();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((configFile == null) ? 0 : configFile.hashCode());
		return result;
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
		if (!(obj instanceof ProcessorConfigResource))
			return false;
		ProcessorConfigResource other = (ProcessorConfigResource) obj;
		if (configFile == null) {
			if (other.configFile != null)
				return false;
		} else if (!configFile.equals(other.configFile))
			return false;
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "ProcessorConfigResource [configFile=" + configFile + "]";
	}

}
