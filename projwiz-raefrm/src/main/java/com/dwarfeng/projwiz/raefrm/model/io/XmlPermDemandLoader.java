package com.dwarfeng.projwiz.raefrm.model.io;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.dwarfeng.dutil.basic.io.LoadFailedException;
import com.dwarfeng.dutil.basic.io.StreamLoader;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;
import com.dwarfeng.projwiz.raefrm.model.cm.PermDemandModel;

/**
 * XML权限需求读取器。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public final class XmlPermDemandLoader extends StreamLoader<PermDemandModel> {

	private boolean readFlag = false;

	/**
	 * 生成一个新的XML权限需求读取器。
	 * 
	 * @param in
	 *            指定的输入流。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public XmlPermDemandLoader(InputStream in) {
		super(in);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void load(PermDemandModel permDemandModel) throws LoadFailedException, IllegalStateException {
		if (readFlag)
			throw new IllegalStateException("读取器已经使用过了。");

		Objects.requireNonNull(permDemandModel, "入口参数 permDemandModel 不能为 null。");

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

				Collection<Toolkit.Method> demands = new HashSet<>();

				/*
				 * 根据 dom4j 的相关说明，此处转换是安全的。
				 */
				@SuppressWarnings("unchecked")
				List<Element> demandElements = (List<Element>) info.elements("demand");

				for (Element demandElement : demandElements) {
					String methodString = demandElement.attributeValue("value");
					methodString = Objects.isNull(methodString) ? null : methodString.toUpperCase();

					if (Objects.isNull(methodString)) {
						throw new LoadFailedException("XML中部分demand元素属性缺失");
					}

					Toolkit.Method method = Toolkit.Method.valueOf(methodString);

					if (Objects.isNull(method)) {
						throw new LoadFailedException(String.format("不存在的方法名: %s", methodString));
					}

					demands.add(method);
				}

				permDemandModel.put(key, demands);

			}

		} catch (Exception e) {
			throw new LoadFailedException("无法从资源管理器中读取指定数据。", e.getCause());
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<LoadFailedException> countinuousLoad(PermDemandModel permDemandModel) throws IllegalStateException {
		if (readFlag)
			throw new IllegalStateException("读取器已经使用过了。");

		Objects.requireNonNull(permDemandModel, "入口参数 permDemandModel 不能为 null。");

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

					Collection<Toolkit.Method> demands = new HashSet<>();

					/*
					 * 根据 dom4j 的相关说明，此处转换是安全的。
					 */
					@SuppressWarnings("unchecked")
					List<Element> demandElements = (List<Element>) info.elements("demand");

					for (Element demandElement : demandElements) {
						try {
							String methodString = demandElement.attributeValue("value");
							methodString = Objects.isNull(methodString) ? null : methodString.toUpperCase();

							if (Objects.isNull(methodString)) {
								throw new LoadFailedException("XML中部分demand元素属性缺失");
							}

							Toolkit.Method method = Toolkit.Method.valueOf(methodString);

							if (Objects.isNull(method)) {
								throw new LoadFailedException(String.format("不存在的方法名: %s", methodString));
							}

							demands.add(method);
						} catch (Exception e) {
							exceptions.add(new LoadFailedException("无法解析 demand 元素。", e.getCause()));
						}
					}

					permDemandModel.put(key, demands);
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
