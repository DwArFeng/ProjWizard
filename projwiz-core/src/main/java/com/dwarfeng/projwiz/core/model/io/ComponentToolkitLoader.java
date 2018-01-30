package com.dwarfeng.projwiz.core.model.io;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.dwarfeng.dutil.basic.cna.model.DefaultReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.MapModel;
import com.dwarfeng.dutil.basic.cna.model.ModelUtil;
import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.dutil.basic.io.LoadFailedException;
import com.dwarfeng.dutil.basic.io.StreamLoader;
import com.dwarfeng.projwiz.core.model.cm.ToolkitPermModel;
import com.dwarfeng.projwiz.core.model.struct.LnpToolkit;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;

/**
 * 组件-工具引用模型读取器。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public final class ComponentToolkitLoader extends StreamLoader<MapModel<String, ReferenceModel<Toolkit>>> {

	/** 忽略的键值集合。 */
	protected final Collection<String> ignoreCmpoentKeys;
	/** 工具包权限模型。 */
	protected final ToolkitPermModel toolkitPermModel;
	/** 标准工具包。 */
	protected final Toolkit standardToolkit;

	private boolean readFlag = false;

	/**
	 * 新实例。
	 * 
	 * @param in
	 *            指定的输入流。
	 * @param ignoreCmpoentKeys
	 *            指定的忽略的键值集合。
	 * @param toolkitPermModel
	 *            指定的工具包权限模型。
	 * @param standardToolkit
	 *            指定的标准工具包。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public ComponentToolkitLoader(InputStream in, Collection<String> ignoreCmpoentKeys,
			ToolkitPermModel toolkitPermModel, Toolkit standardToolkit) {
		super(in);
		Objects.requireNonNull(ignoreCmpoentKeys, "入口参数 ignoreCmpoentKeys 不能为 null。");
		Objects.requireNonNull(toolkitPermModel, "入口参数 toolkitPermModel 不能为 null。");
		Objects.requireNonNull(standardToolkit, "入口参数 standardToolkit 不能为 null。");

		this.ignoreCmpoentKeys = ignoreCmpoentKeys;
		this.toolkitPermModel = toolkitPermModel;
		this.standardToolkit = standardToolkit;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void load(MapModel<String, ReferenceModel<Toolkit>> cmpoentToolkitModel)
			throws LoadFailedException, IllegalStateException {

		if (readFlag)
			throw new IllegalStateException("读取器已经使用过了。");

		Objects.requireNonNull(cmpoentToolkitModel, "入口参数 cmpoentToolkitModel 不能为 null。");

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

				if (Objects.isNull(Objects.isNull(key))) {
					throw new LoadFailedException("属性缺失。");
				}

				Element toolkitElement = info.element("toolkit");
				if (Objects.isNull(toolkitElement)) {
					throw new LoadFailedException("属性缺失。");
				}

				String permLevelString = toolkitElement.attributeValue("perm-level");
				if (Objects.isNull(permLevelString)) {
					throw new LoadFailedException("属性缺失。");
				}

				Collection<Toolkit.Method> privileges = new HashSet<>();
				/*
				 * 根据 dom4j 的相关说明，此处转换是安全的。
				 */
				@SuppressWarnings("unchecked")
				List<Element> privilegeElements = (List<Element>) root.elements("privilege");
				for (Element privilegeElement : privilegeElements) {
					String methodString = privilegeElement.getStringValue().toUpperCase();
					if (Objects.isNull(methodString)) {
						throw new LoadFailedException("属性缺失。");
					}

					Toolkit.Method method = Toolkit.Method.valueOf(methodString);
					if (Objects.isNull(methodString)) {
						throw new LoadFailedException(String.format("不存在指定的方法: %s", methodString));
					}

					privileges.add(method);
				}

				int permLevel = Integer.parseInt(permLevelString);
				LnpToolkit toolkit = new LnpToolkit(toolkitPermModel, permLevel, standardToolkit, privileges);

				cmpoentToolkitModel.put(key, ModelUtil.syncReferenceModel(new DefaultReferenceModel<>(toolkit)));
			}

		} catch (Exception e) {
			throw new LoadFailedException("无法读取指定组件数据。", e);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<LoadFailedException> countinuousLoad(MapModel<String, ReferenceModel<Toolkit>> cmpoentToolkitModel)
			throws IllegalStateException {
		if (readFlag)
			throw new IllegalStateException("读取器已经使用过了。");

		Objects.requireNonNull(cmpoentToolkitModel, "入口参数 cmpoentToolkitModel 不能为 null。");

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

					if (Objects.isNull(Objects.isNull(key))) {
						throw new LoadFailedException("属性缺失。");
					}

					Element toolkitElement = info.element("toolkit");
					if (Objects.isNull(toolkitElement)) {
						throw new LoadFailedException("属性缺失。");
					}

					String permLevelString = toolkitElement.attributeValue("perm-level");
					if (Objects.isNull(permLevelString)) {
						throw new LoadFailedException("属性缺失。");
					}

					Collection<Toolkit.Method> privileges = new HashSet<>();
					/*
					 * 根据 dom4j 的相关说明，此处转换是安全的。
					 */
					@SuppressWarnings("unchecked")
					List<Element> privilegeElements = (List<Element>) root.elements("privilege");
					for (Element privilegeElement : privilegeElements) {
						try {
							String methodString = privilegeElement.getStringValue().toUpperCase();
							if (Objects.isNull(methodString)) {
								throw new LoadFailedException("属性缺失。");
							}

							Toolkit.Method method = Toolkit.Method.valueOf(methodString);
							if (Objects.isNull(methodString)) {
								throw new LoadFailedException(String.format("不存在指定的方法: %s", methodString));
							}

							privileges.add(method);
						} catch (Exception e) {
							exceptions.add(new LoadFailedException("无法读取指定组件数据。", e));
						}
					}

					int permLevel = Integer.parseInt(permLevelString);
					LnpToolkit toolkit = new LnpToolkit(toolkitPermModel, permLevel, standardToolkit, privileges);

					cmpoentToolkitModel.put(key, ModelUtil.syncReferenceModel(new DefaultReferenceModel<>(toolkit)));
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
