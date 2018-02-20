package com.dwarfeng.projwiz.core.util;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import org.dom4j.Element;

import com.dwarfeng.dutil.basic.DwarfUtil;
import com.dwarfeng.dutil.basic.cna.model.DefaultReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.dutil.basic.io.LoadFailedException;
import com.dwarfeng.dutil.develop.resource.Resource;
import com.dwarfeng.dutil.develop.resource.Url2RepoRresource;
import com.dwarfeng.projwiz.core.model.cm.ToolkitPermModel;
import com.dwarfeng.projwiz.core.model.io.PluginClassLoader;
import com.dwarfeng.projwiz.core.model.struct.Component;
import com.dwarfeng.projwiz.core.model.struct.LnpToolkit;
import com.dwarfeng.projwiz.core.model.struct.MetaDataStorage;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;

/**
 * 与IO有关的工具包。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public final class IOUtil {

	/**
	 * 根据指定的XML节点解析组件。
	 * 
	 * @param componentElement
	 *            指定的节点。
	 * @param pluginClassLoader
	 *            指定的插件类加载器。
	 * @param toolkit
	 *            组件使用的工具包。
	 * @param metaDataStorage
	 *            组件使用的元数据仓库。
	 * @return 根据指定的节点解析得到的组件。
	 * @throws LoadFailedException
	 *             读取失败。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	public static Component parseComponent(Element componentElement, PluginClassLoader pluginClassLoader,
			Toolkit toolkit, MetaDataStorage metaDataStorage) throws LoadFailedException {
		Objects.requireNonNull(componentElement, "入口参数 componentElement 不能为 null。");
		Objects.requireNonNull(pluginClassLoader, "入口参数 pluginClassLoader 不能为 null。");
		Objects.requireNonNull(toolkit, "入口参数 toolkit 不能为 null。");
		Objects.requireNonNull(metaDataStorage, "入口参数 metaDataStorage 不能为 null。");

		String key = componentElement.attributeValue("key");
		String classString = componentElement.attributeValue("class");

		if (Objects.isNull(Objects.isNull(key)) || Objects.isNull(classString)) {
			throw new LoadFailedException("属性缺失。");
		}

		Component cmpoent = null;
		try {
			Method method = pluginClassLoader.loadClass(classString).getMethod("newInstance", String.class,
					ReferenceModel.class, MetaDataStorage.class);
			cmpoent = (Component) method.invoke(null, key, new DefaultReferenceModel<>(toolkit), metaDataStorage);// TODO
																													// 参数待完善。
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			throw new LoadFailedException("组件初始化失败", e);
		}

		if (Objects.isNull(cmpoent)) {
			throw new LoadFailedException("未知错误。");
		}

		return cmpoent;

	}

	/**
	 * 根据指定的XML节点解析资源。
	 * 
	 * @param resourceElement
	 *            指定的XML节点。
	 * @param repoDir
	 *            仓库的根目录。
	 * @return 根据指定的XML节点解析得到的资源。
	 * @throws LoadFailedException
	 *             读取失败。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	public static Resource parseResource(Element resourceElement, File repoDir) throws LoadFailedException {
		Objects.requireNonNull(resourceElement, "入口参数 resourceElement 不能为 null。");
		Objects.requireNonNull(repoDir, "入口参数 repoDir 不能为 null。");

		String defString = resourceElement.attributeValue("default");
		String classify = resourceElement.attributeValue("classify");
		String fileName = resourceElement.attributeValue("filename");
		String key = resourceElement.attributeValue("key");

		if (Objects.isNull(defString) || Objects.isNull(classify) || Objects.isNull(fileName) || Objects.isNull(key)) {
			throw new LoadFailedException("属性缺失。");
		}

		URL def = DwarfUtil.class.getResource(defString);

		if (Objects.isNull(def)) {
			throw new LoadFailedException("资源路径不正确。");
		}

		Url2RepoRresource resource = new Url2RepoRresource(key, def, repoDir, classify, fileName);
		return resource;
	}

	/**
	 * 根据指定的XML节点解析工具包。
	 * 
	 * @param toolkitElement
	 *            指定的节点。
	 * @param toolkitPermModel
	 *            指定的工具包权限模型。
	 * @param standardToolkit
	 *            指定的标准工具包。
	 * @return 根据指定的XML节点解析得到的工具包。
	 * @throws LoadFailedException
	 *             读取失败。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	public static Toolkit parseToolkit(Element toolkitElement, ToolkitPermModel toolkitPermModel,
			Toolkit standardToolkit) throws LoadFailedException {
		Objects.requireNonNull(toolkitElement, "入口参数 toolkitElement 不能为 null。");
		Objects.requireNonNull(toolkitPermModel, "入口参数 toolkitPermModel 不能为 null。");
		Objects.requireNonNull(standardToolkit, "入口参数 standardToolkit 不能为 null。");

		String permLevelString = toolkitElement.attributeValue("perm-level");
		if (Objects.isNull(permLevelString)) {
			throw new LoadFailedException("属性缺失。");
		}

		Collection<Toolkit.Method> privileges = new HashSet<>();
		/*
		 * 根据 dom4j 的相关说明，此处转换是安全的。
		 */
		@SuppressWarnings("unchecked")
		List<Element> privilegeElements = (List<Element>) toolkitElement.elements("privilege");

		for (Element privilegeElement : privilegeElements) {
			String methodString = privilegeElement.getStringValue();
			methodString = Objects.isNull(methodString) ? null : methodString.toUpperCase();
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
		return toolkit;
	}

	// 禁止外部实例化。
	private IOUtil() {
	}

}
