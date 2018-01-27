package com.dwarfeng.projwiz.core.model.io;

import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.dwarfeng.dutil.basic.io.LoadFailedException;
import com.dwarfeng.dutil.basic.io.StreamLoader;

/**
 * 忽略组件清单读取器。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public final class IgnoredComponentLoader extends StreamLoader<Collection<String>> {

	private boolean readFlag = false;

	/**
	 * 生成一个新的忽略组件清单读取器。
	 * 
	 * @param in
	 *            指定的输入流。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	public IgnoredComponentLoader(InputStream in) {
		super(in);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void load(Collection<String> collection) throws LoadFailedException, IllegalStateException {
		if (readFlag)
			throw new IllegalStateException("读取器已经使用过了。");

		Objects.requireNonNull(collection, "入口参数 collection 不能为 null。");

		try {
			readFlag = true;

			SAXReader reader = new SAXReader();
			Element root = reader.read(in).getRootElement();

			/*
			 * 根据 dom4j 的相关说明，此处转换是安全的。
			 */
			@SuppressWarnings("unchecked")
			List<Element> infos = (List<Element>) root.elements("info");

			for (Element info : infos) {
				String key = info.attributeValue("key");

				if (Objects.isNull(key)) {
					throw new LoadFailedException("属性缺失。");
				}

				collection.add(key);
			}

		} catch (Exception e) {
			throw new LoadFailedException("无法从资源管理器中读取指定数据。", e);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<LoadFailedException> countinuousLoad(Collection<String> collection) throws IllegalStateException {
		if (readFlag)
			throw new IllegalStateException("读取器已经使用过了。");

		Objects.requireNonNull(collection, "入口参数 collection 不能为 null。");

		if (readFlag)
			throw new IllegalStateException("读取器已经使用过了。");

		Objects.requireNonNull(collection, "入口参数 collection 不能为 null。");

		final Set<LoadFailedException> exceptions = new LinkedHashSet<>();
		try {
			readFlag = true;

			SAXReader reader = new SAXReader();
			Element root = reader.read(in).getRootElement();

			/*
			 * 根据 dom4j 的相关说明，此处转换是安全的。
			 */
			@SuppressWarnings("unchecked")
			List<Element> infos = (List<Element>) root.elements("info");

			for (Element info : infos) {

				try {
					String key = info.attributeValue("key");

					if (Objects.isNull(key)) {
						throw new LoadFailedException("属性缺失。");
					}

					collection.add(key);
				} catch (Exception e) {
					exceptions.add(new LoadFailedException("无法从资源管理器中读取指定数据。", e));
				}
			}

		} catch (Exception e) {
			exceptions.add(new LoadFailedException("无法从资源管理器中读取指定数据。", e));
		}

		return exceptions;

	}

}
