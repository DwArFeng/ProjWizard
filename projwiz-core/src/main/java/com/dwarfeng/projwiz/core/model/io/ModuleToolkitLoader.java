package com.dwarfeng.projwiz.core.model.io;

import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
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
import com.dwarfeng.projwiz.core.model.struct.Module;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;
import com.dwarfeng.projwiz.core.util.IOUtil;

/**
 * 组件-工具引用模型读取器。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public final class ModuleToolkitLoader
		extends StreamLoader<MapModel<Class<? extends Module>, ReferenceModel<Toolkit>>> {

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
	 * @param toolkitPermModel
	 *            指定的工具包权限模型。
	 * @param standardToolkit
	 *            指定的标准工具包。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public ModuleToolkitLoader(InputStream in, ToolkitPermModel toolkitPermModel, Toolkit standardToolkit) {
		super(in);
		Objects.requireNonNull(toolkitPermModel, "入口参数 toolkitPermModel 不能为 null。");
		Objects.requireNonNull(standardToolkit, "入口参数 standardToolkit 不能为 null。");

		this.toolkitPermModel = toolkitPermModel;
		this.standardToolkit = standardToolkit;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<LoadFailedException> countinuousLoad(
			MapModel<Class<? extends Module>, ReferenceModel<Toolkit>> moduleToolkitModel)
			throws IllegalStateException {
		if (readFlag)
			throw new IllegalStateException("读取器已经使用过了。");

		Objects.requireNonNull(moduleToolkitModel, "入口参数 moduleToolkitModel 不能为 null。");

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
					load0(moduleToolkitModel, info);
				} catch (Exception e) {
					exceptions.add(new LoadFailedException("无法读取指定组件数据。", e));
				}
			}

		} catch (Exception e) {
			exceptions.add(new LoadFailedException("无法读取指定组件数据。", e));
		}

		return exceptions;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void load(MapModel<Class<? extends Module>, ReferenceModel<Toolkit>> moduleToolkitModel)
			throws LoadFailedException, IllegalStateException {

		if (readFlag)
			throw new IllegalStateException("读取器已经使用过了。");

		Objects.requireNonNull(moduleToolkitModel, "入口参数 moduleToolkitModel 不能为 null。");

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
				load0(moduleToolkitModel, info);
			}

		} catch (Exception e) {
			throw new LoadFailedException("无法读取指定组件数据。", e);
		}

	}

	private void load0(Map<Class<? extends Module>, ReferenceModel<Toolkit>> moduleToolkitModel, Element info)
			throws LoadFailedException {
		Class<? extends Module> clazz = IOUtil.parseClass(info);

		if (!Module.class.isAssignableFrom(clazz)) {
			throw new LoadFailedException("类 " + clazz.toString() + " 不是 Module 的子类");
		}

		Element toolkitElement = info.element("toolkit");
		if (Objects.isNull(toolkitElement)) {
			throw new LoadFailedException("属性缺失。");
		}

		Toolkit toolkit = IOUtil.parseToolkit(toolkitElement, toolkitPermModel, standardToolkit);

		moduleToolkitModel.put(clazz, ModelUtil.syncReferenceModel(new DefaultReferenceModel<>(toolkit)));
	}

}
