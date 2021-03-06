package com.dwarfeng.projwiz.core.model.io;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.dwarfeng.dutil.basic.cna.model.DefaultReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.MapModel;
import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.dutil.basic.io.LoadFailedException;
import com.dwarfeng.dutil.basic.io.StreamLoader;
import com.dwarfeng.projwiz.core.model.cm.ModuleModel;
import com.dwarfeng.projwiz.core.model.cm.ToolkitPermModel;
import com.dwarfeng.projwiz.core.model.struct.Module;
import com.dwarfeng.projwiz.core.model.struct.CotoPair;
import com.dwarfeng.projwiz.core.model.struct.DefaultMetaDataStorage;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;
import com.dwarfeng.projwiz.core.util.Constants;
import com.dwarfeng.projwiz.core.util.IOUtil;

/**
 * 组件读取器。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public final class ModuleLoader extends StreamLoader<CotoPair> {

	/** 忽略的键值集合。 */
	protected final Collection<Class<?>> ignoredModuleClazzes;
	/** 插件的类加载器。 */
	protected final PluginClassLoader pluginClassLoader;
	/** 工具包权限模型。 */
	protected final ToolkitPermModel toolkitPermModel;
	/** 标准工具包。 */
	protected final Toolkit standardToolkit;
	/** 元数据的根目录。 */
	protected final File metaDataRootDir;

	private boolean readFlag = false;

	/**
	 * 
	 * @param in
	 * @param ignoredModuleClazzes
	 * @param pluginClassLoader
	 * @param toolkitPermModel
	 * @param standardToolkit
	 * @param metaDataRootDir
	 */
	public ModuleLoader(InputStream in, Collection<Class<?>> ignoredModuleClazzes, PluginClassLoader pluginClassLoader,
			ToolkitPermModel toolkitPermModel, Toolkit standardToolkit, File metaDataRootDir) {
		super(in);

		Objects.requireNonNull(ignoredModuleClazzes, "入口参数 ignoredModuleClazzes 不能为 null。");
		Objects.requireNonNull(pluginClassLoader, "入口参数 pluginClassLoader 不能为 null。");
		Objects.requireNonNull(toolkitPermModel, "入口参数 toolkitPermModel 不能为 null。");
		Objects.requireNonNull(standardToolkit, "入口参数 standardToolkit 不能为 null。");
		Objects.requireNonNull(metaDataRootDir, "入口参数 metaDataRootDir 不能为 null。");

		this.ignoredModuleClazzes = ignoredModuleClazzes;
		this.pluginClassLoader = pluginClassLoader;
		this.toolkitPermModel = toolkitPermModel;
		this.standardToolkit = standardToolkit;
		this.metaDataRootDir = metaDataRootDir;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<LoadFailedException> countinuousLoad(CotoPair cotoPair) throws IllegalStateException {
		if (readFlag)
			throw new IllegalStateException("读取器已经使用过了。");

		Objects.requireNonNull(cotoPair, "入口参数 cotoPair 不能为 null。");

		final ModuleModel moduleModel = cotoPair.getModuleModel();
		final MapModel<Class<? extends Module>, ReferenceModel<Toolkit>> moduleToolkitModel = cotoPair
				.getModuleToolkitModel();

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
					load0(moduleToolkitModel, moduleModel, info);
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
	public void load(CotoPair cotoPair) throws LoadFailedException, IllegalStateException {
		if (readFlag)
			throw new IllegalStateException("读取器已经使用过了。");

		Objects.requireNonNull(cotoPair, "入口参数 cotoPair 不能为 null。");

		final ModuleModel moduleModel = cotoPair.getModuleModel();
		final MapModel<Class<? extends Module>, ReferenceModel<Toolkit>> moduleToolkitModel = cotoPair
				.getModuleToolkitModel();

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
				load0(moduleToolkitModel, moduleModel, info);
			}

		} catch (Exception e) {
			throw new LoadFailedException("无法读取指定组件数据。", e);
		}

	}

	private void load0(Map<Class<? extends Module>, ReferenceModel<Toolkit>> moduleToolkitModel,
			ModuleModel moduleModel, Element info) throws LoadFailedException {
		Class<? extends Module> clazz = IOUtil.parseClass(info);

		if (ignoredModuleClazzes.contains(clazz)) {
			return;
		}

		Toolkit toolkit = null;
		if (moduleToolkitModel.containsKey(clazz)) {
			toolkit = moduleToolkitModel.get(clazz).get();
		} else {
			Element toolkitElement = info.element("toolkit");
			if (Objects.isNull(toolkitElement)) {
				toolkit = Constants.NON_PERMISSION_TOOLKIT;
			} else {
				toolkit = IOUtil.parseToolkit(toolkitElement, toolkitPermModel, standardToolkit);
			}

			moduleToolkitModel.put(clazz, new DefaultReferenceModel<>(toolkit));
		}

		Module module = IOUtil.parseModule(info, pluginClassLoader, toolkit,
				new DefaultMetaDataStorage(metaDataRootDir, clazz.getName()));
		moduleModel.add(module);
	}

}
