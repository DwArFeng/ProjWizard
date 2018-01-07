package com.dwarfeng.projwiz.core.model.io;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.dwarfeng.dutil.basic.cna.model.KeySetModel;
import com.dwarfeng.dutil.basic.io.LoadFailedException;
import com.dwarfeng.dutil.basic.io.StreamLoader;
import com.dwarfeng.dutil.develop.resource.Resource;
import com.dwarfeng.projwiz.core.model.cm.ProcessorConfigHandler;
import com.dwarfeng.projwiz.core.model.struct.DefaultProcessorConfigInfo;
import com.dwarfeng.projwiz.core.model.struct.ProjectProcessor;

public final class XmlProjectProcessorLoader extends StreamLoader<KeySetModel<String, ProjectProcessor>> {

	private final PluginClassLoader classLoader;
	private final ProcessorConfigHandler processorConfigHandler;

	private boolean readFlag = false;

	public XmlProjectProcessorLoader(InputStream in, PluginClassLoader classLoader,
			ProcessorConfigHandler processorConfigHandler) {
		super(in);
		Objects.requireNonNull(classLoader, "入口参数 classLoader 不能为 null。");
		Objects.requireNonNull(processorConfigHandler, "入口参数 processorConfigHandler 不能为 null。");

		this.classLoader = classLoader;
		this.processorConfigHandler = processorConfigHandler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dwarfeng.dutil.basic.io.Loader#load(java.lang.Object)
	 */
	@Override
	public void load(KeySetModel<String, ProjectProcessor> projectProcessorModel)
			throws LoadFailedException, IllegalStateException {
		if (readFlag)
			throw new IllegalStateException("该读取器已经使用过了");

		Objects.requireNonNull(projectProcessorModel, "入口参数 projectProcessorModel 不能为 null。");

		try {
			readFlag = true;

			SAXReader reader = new SAXReader();
			Element root = reader.read(in).getRootElement();

			/*
			 * 根据 dom4j 的相关说明，此处转换是安全的。
			 */
			@SuppressWarnings("unchecked")
			List<Element> clazz = (List<Element>) root.elements("class");

			for (Element claz : clazz) {
				String nameString = claz.attributeValue("name");

				if (Objects.isNull(nameString)) {
					throw new LoadFailedException("不存在的xml字段 name。");
				}

				if (!processorConfigHandler.containsKey(nameString)) {
					UUID uuid = UUID.randomUUID();
					String fileName = null;
					boolean repeatFlag = true;

					while (repeatFlag) {
						fileName = uuid.toString();
						File testExists = new File(processorConfigHandler.getDirection(), fileName);
						if (!testExists.exists()) {
							repeatFlag = false;
						}
					}

					processorConfigHandler.add(new DefaultProcessorConfigInfo(nameString, fileName));
				}

				Resource resource = processorConfigHandler.newResource(nameString);
				Method method = classLoader.loadClass(nameString).getMethod("newInstance", Resource.class);

				Object object = method.invoke(null, resource);
				projectProcessorModel.add(ProjectProcessor.class.cast(object));

			}

		} catch (Exception e) {
			throw new LoadFailedException("读取工程处理器时失败。");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dwarfeng.dutil.basic.io.Loader#countinuousLoad(java.lang.Object)
	 */
	@Override
	public Set<LoadFailedException> countinuousLoad(KeySetModel<String, ProjectProcessor> projectProcessorModel)
			throws IllegalStateException {
		if (readFlag)
			throw new IllegalStateException("该读取器已经使用过了");

		Objects.requireNonNull(projectProcessorModel, "入口参数 projectProcessorModel 不能为 null。");

		final Set<LoadFailedException> exceptions = new LinkedHashSet<>();
		try {
			readFlag = true;

			SAXReader reader = new SAXReader();
			Element root = reader.read(in).getRootElement();

			/*
			 * 根据 dom4j 的相关说明，此处转换是安全的。
			 */
			@SuppressWarnings("unchecked")
			List<Element> clazz = (List<Element>) root.elements("class");

			for (Element claz : clazz) {
				try {
					String nameString = claz.attributeValue("name");

					if (Objects.isNull(nameString)) {
						throw new LoadFailedException("不存在的xml字段 name。");
					}

					if (!processorConfigHandler.containsKey(nameString)) {
						UUID uuid = UUID.randomUUID();
						String fileName = null;
						boolean repeatFlag = true;

						while (repeatFlag) {
							fileName = uuid.toString();
							File testExists = new File(processorConfigHandler.getDirection(), fileName);
							if (!testExists.exists()) {
								repeatFlag = false;
							}
						}

						processorConfigHandler.add(new DefaultProcessorConfigInfo(nameString, fileName));
					}

					Resource resource = processorConfigHandler.newResource(nameString);
					Method method = classLoader.loadClass(nameString).getMethod("newInstance", Resource.class);

					Object object = method.invoke(null, resource);
					projectProcessorModel.add(ProjectProcessor.class.cast(object));

				} catch (Exception e) {
					exceptions.add(new LoadFailedException("读取工程处理器时失败", e));
				}
			}

		} catch (Throwable e) {
			exceptions.add(new LoadFailedException("读取工程处理器时失败", e));
		}

		return exceptions;

	}

}
