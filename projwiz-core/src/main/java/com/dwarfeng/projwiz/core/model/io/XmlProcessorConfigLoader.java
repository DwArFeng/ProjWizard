package com.dwarfeng.projwiz.core.model.io;

import java.io.File;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.dwarfeng.dutil.basic.io.LoadFailedException;
import com.dwarfeng.dutil.basic.io.StreamLoader;
import com.dwarfeng.projwiz.core.model.cm.ProcessorConfigHandler;
import com.dwarfeng.projwiz.core.model.struct.DefaultProcessorConfigInfo;

/**
 * 处理器配置读取器。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public final class XmlProcessorConfigLoader extends StreamLoader<ProcessorConfigHandler> {

	private boolean readFlag;

	public XmlProcessorConfigLoader(InputStream in) {
		super(in);
	}

	@Override
	public void load(ProcessorConfigHandler processorConfigHandler) throws LoadFailedException, IllegalStateException {
		if (readFlag)
			throw new IllegalStateException("该读取器已经使用过了");

		Objects.requireNonNull(processorConfigHandler, "入口参数 processorConfigHandler 不能为 null。");

		try {
			readFlag = true;

			SAXReader reader = new SAXReader();
			Element root = reader.read(in).getRootElement();

			String dirString = root.attributeValue("dir");
			if (Objects.isNull(dirString)) {
				throw new LoadFailedException("无法解析配置信息中的目录");
			}

			File direction = new File(dirString);
			processorConfigHandler.setDirection(direction);

			/*
			 * 根据 dom4j 的相关说明，此处转换是安全的。
			 */
			@SuppressWarnings("unchecked")
			List<Element> infos = (List<Element>) root.elements("config");

			for (Element info : infos) {
				String classString = info.attributeValue("class");
				String fileString = info.attributeValue("file");

				if (Objects.isNull(classString) || Objects.isNull(fileString)) {
					throw new LoadFailedException("不存在期望的属性字段");
				}

				processorConfigHandler.add(new DefaultProcessorConfigInfo(classString, fileString));
			}

		} catch (Exception e) {
			throw new LoadFailedException("读取处理器配置信息时失败", e);
		}

	}

	@Override
	public Set<LoadFailedException> countinuousLoad(ProcessorConfigHandler processorConfigHandler)
			throws IllegalStateException {
		if (readFlag)
			throw new IllegalStateException("该读取器已经使用过了");

		Objects.requireNonNull(processorConfigHandler, "入口参数 processorConfigHandler 不能为 null。");

		final Set<LoadFailedException> exceptions = new LinkedHashSet<>();
		try {
			readFlag = true;

			SAXReader reader = new SAXReader();
			Element root = reader.read(in).getRootElement();

			String dirString = root.attributeValue("dir");
			if (Objects.isNull(dirString)) {
				throw new LoadFailedException("无法解析配置信息中的目录");
			}

			File direction = new File(dirString);
			processorConfigHandler.setDirection(direction);

			/*
			 * 根据 dom4j 的相关说明，此处转换是安全的。
			 */
			@SuppressWarnings("unchecked")
			List<Element> infos = (List<Element>) root.elements("config");

			for (Element info : infos) {
				try {
					String keyString = info.attributeValue("key");
					String fileString = info.attributeValue("file");

					if (Objects.isNull(keyString) || Objects.isNull(fileString)) {
						throw new LoadFailedException("不存在期望的属性字段");
					}

					processorConfigHandler.add(new DefaultProcessorConfigInfo(keyString, fileString));
				} catch (Exception e) {
					exceptions.add(new LoadFailedException("读取处理器配置信息时失败", e));
				}

			}

		} catch (Exception e) {
			exceptions.add(new LoadFailedException("读取处理器配置信息时失败", e));
		}

		return exceptions;

	}

}
