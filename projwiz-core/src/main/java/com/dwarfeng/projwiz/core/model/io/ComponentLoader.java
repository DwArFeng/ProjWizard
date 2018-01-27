package com.dwarfeng.projwiz.core.model.io;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.dwarfeng.dutil.basic.cna.model.DefaultReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.dutil.basic.io.LoadFailedException;
import com.dwarfeng.dutil.basic.io.StreamLoader;
import com.dwarfeng.projwiz.core.model.cm.ComponentModel;
import com.dwarfeng.projwiz.core.model.struct.Component;
import com.dwarfeng.projwiz.core.model.struct.MetaDataStorage;
import com.dwarfeng.projwiz.core.model.struct.TempMetaDataStorage;
import com.dwarfeng.projwiz.core.util.Constants;

/**
 * 组件读取器。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public final class ComponentLoader extends StreamLoader<ComponentModel> {

	/** 忽略的键值集合。 */
	protected final Collection<String> ignoreCmpoentKeys;
	/** 插件的类加载器。 */
	protected final PluginClassLoader classLoader;

	private boolean readFlag = false;

	/**
	 * 
	 * @param in
	 * @param ignoreCmpoentKeys
	 * @param classLoader
	 */
	public ComponentLoader(InputStream in, Collection<String> ignoreCmpoentKeys, PluginClassLoader classLoader) {
		super(in);
		Objects.requireNonNull(ignoreCmpoentKeys, "入口参数 ignoreCmpoentKeys 不能为 null。");
		Objects.requireNonNull(classLoader, "入口参数 classLoader 不能为 null。");
		this.ignoreCmpoentKeys = ignoreCmpoentKeys;
		this.classLoader = classLoader;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void load(ComponentModel componentModel) throws LoadFailedException, IllegalStateException {
		if (readFlag)
			throw new IllegalStateException("读取器已经使用过了。");

		Objects.requireNonNull(componentModel, "入口参数 componentModel 不能为 null。");

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
				String classString = info.attributeValue("class");

				if (Objects.isNull(Objects.isNull(key)) || Objects.isNull(classString)) {
					throw new LoadFailedException("属性缺失。");
				}

				if (ignoreCmpoentKeys.contains(classString)) {
					continue;
				}

				Method method = classLoader.loadClass(classString).getMethod("newInstance", String.class,
						ReferenceModel.class, MetaDataStorage.class);

				// 此处类型转换是否安全需要靠配置文件保证。
				Component cmpoent = (Component) method.invoke(null, key,
						new DefaultReferenceModel<>(Constants.NON_PERMISSION_TOOLKIT), new TempMetaDataStorage());

				componentModel.add(cmpoent);
			}

		} catch (Exception e) {
			throw new LoadFailedException("无法读取指定组件数据。", e);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<LoadFailedException> countinuousLoad(ComponentModel componentModel) throws IllegalStateException {
		if (readFlag)
			throw new IllegalStateException("读取器已经使用过了。");

		Objects.requireNonNull(componentModel, "入口参数 componentModel 不能为 null。");

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
					String classString = info.attributeValue("class");

					if (Objects.isNull(Objects.isNull(key)) || Objects.isNull(classString)) {
						throw new LoadFailedException("属性缺失。");
					}

					if (ignoreCmpoentKeys.contains(classString)) {
						continue;
					}

					Method method = classLoader.loadClass(classString).getMethod("newInstance", String.class,
							ReferenceModel.class, MetaDataStorage.class);

					// 此处类型转换是否安全需要靠配置文件保证。
					Component cmpoent = (Component) method.invoke(null, key,
							new DefaultReferenceModel<>(Constants.NON_PERMISSION_TOOLKIT), new TempMetaDataStorage());

					componentModel.add(cmpoent);
				} catch (Exception e) {
					exceptions.add(new LoadFailedException("无法读取指定组件数据。", e));
				}
			}

		} catch (Exception e) {
			exceptions.add(new LoadFailedException("无法读取指定组件数据。", e));
		}

		return exceptions;

	}

}
