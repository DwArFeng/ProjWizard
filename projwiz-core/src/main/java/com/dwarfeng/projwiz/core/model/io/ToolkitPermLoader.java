package com.dwarfeng.projwiz.core.model.io;

import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.dwarfeng.dutil.basic.io.LoadFailedException;
import com.dwarfeng.dutil.basic.io.StreamLoader;
import com.dwarfeng.projwiz.core.model.cm.ToolkitPermModel;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;

/**
 * 工具包权限读取器。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public final class ToolkitPermLoader extends StreamLoader<ToolkitPermModel> {

	private boolean readFlag = false;

	/**
	 * 生成一个新的 Properties 工具包权限读取器。
	 * 
	 * @param in
	 *            指定的输入流。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public ToolkitPermLoader(InputStream in) {
		super(in);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void load(ToolkitPermModel toolkitPermModel) throws LoadFailedException, IllegalStateException {
		if (readFlag)
			throw new IllegalStateException("读取器已经使用过了。");

		Objects.requireNonNull(toolkitPermModel, "入口参数 toolkitPermModel 不能为 null。");

		try {
			readFlag = true;

			SAXReader reader = new SAXReader();
			Element root = reader.read(in).getRootElement();

			// 1. 读取默认的权限等级。
			Element dplElement = root.element("dpl");
			String dplString;

			if (Objects.isNull(dplElement)) {
				throw new LoadFailedException("XML中缺少dpl定义。");
			}

			dplString = dplElement.attributeValue("value");

			if (Objects.isNull(dplString)) {
				throw new LoadFailedException("XML中dpl元素缺少定义。");
			}

			int dpl = Integer.parseInt(dplString);
			toolkitPermModel.setDpl(dpl);

			// 2. 读取定义文件。
			Map<String, Integer> defineMap = new HashMap<>();
			Element definesElement = root.element("defines");
			/*
			 * 根据 dom4j 的相关说明，此处转换是安全的。
			 */
			@SuppressWarnings("unchecked")
			List<Element> defineList = (List<Element>) definesElement.elements("define");

			for (Element define : defineList) {
				String key = define.attributeValue("key").toUpperCase();
				String valueString = define.attributeValue("value");

				if (Objects.isNull(key) || Objects.isNull(valueString)) {
					throw new LoadFailedException("XML中部分define元素属性缺失");
				}

				Integer value = Integer.parseInt(valueString);
				defineMap.put(key, value);
			}

			// 3.解析权限文件。
			Element permsElement = root.element("perms");
			/*
			 * 根据 dom4j 的相关说明，此处转换是安全的。
			 */
			@SuppressWarnings("unchecked")
			List<Element> permList = (List<Element>) permsElement.elements("perm");

			for (Element perm : permList) {
				String methodString = perm.attributeValue("method").toUpperCase();
				String plString = perm.attributeValue("pl").toUpperCase();

				if (Objects.isNull(methodString) || Objects.isNull(plString)) {
					throw new LoadFailedException("XML中部分perm元素属性缺失");
				}

				Toolkit.Method method = Toolkit.Method.valueOf(methodString);
				if (Objects.isNull(method)) {
					throw new LoadFailedException(String.format("不存在的方法名: %s", methodString));
				}

				Integer pl = null;
				if (defineMap.containsKey(plString)) {
					pl = defineMap.get(plString);
				} else {
					pl = Integer.parseInt(plString);
				}
				toolkitPermModel.put(method, pl);
			}

		} catch (Exception e) {
			throw new LoadFailedException("无法从资源管理器中读取指定数据。", e.getCause());
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<LoadFailedException> countinuousLoad(ToolkitPermModel toolkitPermModel) throws IllegalStateException {
		if (readFlag)
			throw new IllegalStateException("读取器已经使用过了。");

		Objects.requireNonNull(toolkitPermModel, "入口参数 toolkitPermModel 不能为 null。");

		final Set<LoadFailedException> exceptions = new LinkedHashSet<>();
		try {
			readFlag = true;

			SAXReader reader = new SAXReader();
			Element root = reader.read(in).getRootElement();

			// 1. 读取默认的权限等级。
			Element dplElement = root.element("dpl");
			String dplString;

			if (Objects.isNull(dplElement)) {
				throw new LoadFailedException("XML中缺少dpl定义。");
			}

			dplString = dplElement.attributeValue("value");

			if (Objects.isNull(dplString)) {
				throw new LoadFailedException("XML中dpl元素缺少定义。");
			}

			int dpl = Integer.parseInt(dplString);
			toolkitPermModel.setDpl(dpl);

			// 2. 读取定义文件。
			Map<String, Integer> defineMap = new HashMap<>();
			Element definesElement = root.element("defines");
			/*
			 * 根据 dom4j 的相关说明，此处转换是安全的。
			 */
			@SuppressWarnings("unchecked")
			List<Element> defineList = (List<Element>) definesElement.elements("define");

			for (Element define : defineList) {
				try {
					String key = define.attributeValue("key").toUpperCase();
					String valueString = define.attributeValue("value");

					if (Objects.isNull(key) || Objects.isNull(valueString)) {
						throw new LoadFailedException("XML中部分define元素属性缺失");
					}

					Integer value = Integer.parseInt(valueString);
					defineMap.put(key, value);
				} catch (Exception e) {
					exceptions.add(new LoadFailedException("无法从资源管理器中读取指定数据。", e.getCause()));
				}
			}

			// 3.解析权限文件。
			Element permsElement = root.element("perms");
			/*
			 * 根据 dom4j 的相关说明，此处转换是安全的。
			 */
			@SuppressWarnings("unchecked")
			List<Element> permList = (List<Element>) permsElement.elements("perm");

			for (Element perm : permList) {
				try {
					String methodString = perm.attributeValue("method").toUpperCase();
					String plString = perm.attributeValue("pl").toUpperCase();

					if (Objects.isNull(methodString) || Objects.isNull(plString)) {
						throw new LoadFailedException("XML中部分perm元素属性缺失");
					}

					Toolkit.Method method = Toolkit.Method.valueOf(methodString);
					if (Objects.isNull(method)) {
						throw new LoadFailedException(String.format("不存在的方法名: %s", methodString));
					}

					Integer pl = null;
					if (defineMap.containsKey(plString)) {
						pl = defineMap.get(plString);
					} else {
						pl = Integer.parseInt(plString);
					}
					toolkitPermModel.put(method, pl);
				} catch (Exception e) {
					exceptions.add(new LoadFailedException("无法从资源管理器中读取指定数据。", e.getCause()));
				}
			}

		} catch (Exception e) {
			exceptions.add(new LoadFailedException("无法从资源管理器中读取指定数据。", e.getCause()));
		}

		return exceptions;

	}

}
