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
import com.dwarfeng.projwiz.core.model.struct.Module;
import com.dwarfeng.projwiz.core.util.IOUtil;

/**
 * 忽略组件清单读取器。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public final class IgnoredModuleLoader extends StreamLoader<Collection<Class<?>>> {

	private boolean readFlag = false;

	/**
	 * 生成一个新的忽略组件清单读取器。
	 * 
	 * @param in
	 *            指定的输入流。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	public IgnoredModuleLoader(InputStream in) {
		super(in);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void load(Collection<Class<?>> collection) throws LoadFailedException, IllegalStateException {
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
				load0(collection, info);
			}

		} catch (Exception e) {
			throw new LoadFailedException("无法从资源管理器中读取指定数据。", e);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<LoadFailedException> countinuousLoad(Collection<Class<?>> collection) throws IllegalStateException {
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
					load0(collection, info);
				} catch (Exception e) {
					exceptions.add(new LoadFailedException("无法从资源管理器中读取指定数据。", e));
				}
			}

		} catch (Exception e) {
			exceptions.add(new LoadFailedException("无法从资源管理器中读取指定数据。", e));
		}

		return exceptions;

	}

	private void load0(Collection<Class<?>> collection, Element info) throws LoadFailedException {
		Class<? extends Module> clazz = IOUtil.parseClass(info);
		collection.add(clazz);
	}

}
