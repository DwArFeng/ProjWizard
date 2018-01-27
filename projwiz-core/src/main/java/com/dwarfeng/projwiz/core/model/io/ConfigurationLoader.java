package com.dwarfeng.projwiz.core.model.io;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.dwarfeng.dutil.basic.DwarfUtil;
import com.dwarfeng.dutil.basic.io.LoadFailedException;
import com.dwarfeng.dutil.basic.io.StreamLoader;
import com.dwarfeng.dutil.develop.resource.ResourceHandler;
import com.dwarfeng.dutil.develop.resource.Url2RepoRresource;

/**
 * 配置文件读取器。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public final class ConfigurationLoader extends StreamLoader<ResourceHandler> {

	/** 仓库的根目录。 */
	protected final File repoDir;
	/** 忽略的键值集合。 */
	protected final Collection<String> ignoreCfgKeys;
	/** 是否自动复位。 */
	protected final boolean autoReset;

	private boolean readFlag = false;

	/**
	 * 新实例。
	 * 
	 * @param in
	 *            指定的输入流。
	 * @throws NullPointerException
	 *             入口参数 <code>in</code> 为 <code>null</code>。
	 */
	public ConfigurationLoader(InputStream in) {
		this(in, null, Collections.emptySet(), false);
	}

	/**
	 * 生成一个 XML jar包资源仓库读取器，由指定的资源仓库根目录替代 XML 文件中的根目录。
	 * 
	 * @param in
	 *            指定的输入流。
	 * @param repoDir
	 *            指定的资源仓库根目录，如果为 <code>null</code>，则使用 XML 中的根目录。
	 * @param autoReset
	 *            是否自动复位。
	 * @throws NullPointerException
	 *             入口参数 <code>in</code> 为 <code>null</code>。
	 */
	public ConfigurationLoader(InputStream in, File repoDir, Collection<String> ignoreCfgKeys, boolean autoReset) {
		super(in);
		Objects.requireNonNull(ignoreCfgKeys, "入口参数 keySet 不能为 null。");
		this.repoDir = repoDir;
		this.ignoreCfgKeys = ignoreCfgKeys;
		this.autoReset = autoReset;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void load(ResourceHandler resourceHandler) throws LoadFailedException, IllegalStateException {
		if (readFlag)
			throw new IllegalStateException("读取器已经使用过了。");

		Objects.requireNonNull(resourceHandler, "入口参数 resourceHandler 不能为 null。");

		try {
			readFlag = true;

			SAXReader reader = new SAXReader();
			Element root = reader.read(in).getRootElement();

			File repoDir0;

			if (Objects.nonNull(repoDir)) {
				repoDir0 = repoDir;
			} else {
				repoDir0 = new File(root.attributeValue("dir"));
			}

			/*
			 * 根据 dom4j 的相关说明，此处转换是安全的。
			 */
			@SuppressWarnings("unchecked")
			List<Element> infos = (List<Element>) root.elements("info");

			for (Element info : infos) {
				String defString = info.attributeValue("default");
				String classify = info.attributeValue("classify");
				String fileName = info.attributeValue("filename");
				String key = info.attributeValue("key");

				if (Objects.isNull(defString) || Objects.isNull(classify) || Objects.isNull(fileName)
						|| Objects.isNull(key)) {
					throw new LoadFailedException("属性缺失。");
				}

				if (ignoreCfgKeys.contains(key)) {
					continue;
				}

				URL def = DwarfUtil.class.getResource(defString);

				if (Objects.isNull(def)) {
					throw new LoadFailedException("资源路径不正确。");
				}

				Url2RepoRresource resource = new Url2RepoRresource(key, def, repoDir0, classify, fileName);
				resourceHandler.add(resource);

				if (autoReset && !resource.isValid()) {
					resource.reset();
				}
			}

		} catch (Exception e) {
			throw new LoadFailedException("无法读取指定配置数据。", e);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<LoadFailedException> countinuousLoad(ResourceHandler resourceHandler) throws IllegalStateException {
		if (readFlag)
			throw new IllegalStateException("读取器已经使用过了。");

		Objects.requireNonNull(resourceHandler, "入口参数 resourceHandler 不能为 null。");

		final Set<LoadFailedException> exceptions = new LinkedHashSet<>();
		try {
			readFlag = true;

			SAXReader reader = new SAXReader();
			Element root = reader.read(in).getRootElement();

			File repoDir0;

			if (Objects.nonNull(repoDir)) {
				repoDir0 = repoDir;
			} else {
				repoDir0 = new File(root.attributeValue("dir"));
			}

			/*
			 * 根据 dom4j 的相关说明，此处转换是安全的。
			 */
			@SuppressWarnings("unchecked")
			List<Element> infos = (List<Element>) root.elements("info");

			for (Element info : infos) {
				try {
					String defString = info.attributeValue("default");
					String classify = info.attributeValue("classify");
					String fileName = info.attributeValue("filename");
					String key = info.attributeValue("key");

					if (Objects.isNull(defString) || Objects.isNull(classify) || Objects.isNull(fileName)
							|| Objects.isNull(key)) {
						throw new LoadFailedException("属性缺失。");
					}

					if (ignoreCfgKeys.contains(key)) {
						continue;
					}

					URL def = DwarfUtil.class.getResource(defString);

					if (Objects.isNull(def)) {
						throw new LoadFailedException("资源路径不正确。");
					}

					Url2RepoRresource resource = new Url2RepoRresource(key, def, repoDir0, classify, fileName);
					resourceHandler.add(resource);

					if (autoReset && !resource.isValid()) {
						resource.reset();
					}
				} catch (Exception e) {
					exceptions.add(new LoadFailedException("无法读取指定配置数据。", e));
				}

			}

		} catch (Exception e) {
			exceptions.add(new LoadFailedException("无法读取指定配置数据。", e));
		}

		return exceptions;

	}

}
