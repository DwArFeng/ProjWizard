package com.dwarfeng.projwiz.core.model.io;

import java.io.OutputStream;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.dwarfeng.dutil.basic.io.SaveFailedException;
import com.dwarfeng.dutil.basic.io.StreamSaver;
import com.dwarfeng.projwiz.core.model.cm.ProcessorConfigHandler;
import com.dwarfeng.projwiz.core.model.struct.ProcessorConfigInfo;

/**
 * 处理器配置存储器。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class XmlProcessorConfigSaver extends StreamSaver<ProcessorConfigHandler> {

	private boolean saveFlag;

	/**
	 * 新实例。
	 * 
	 * @param out
	 *            指定的输出流。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public XmlProcessorConfigSaver(OutputStream out) {
		super(out);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save(ProcessorConfigHandler processorConfigHandler) throws SaveFailedException, IllegalStateException {
		if (saveFlag)
			throw new IllegalStateException("该存储器已经使用过了");

		Objects.requireNonNull(processorConfigHandler, "入口参数 processorConfigHandler 不能为 null。");

		try {

			Element root = DocumentHelper.createElement("root");
			root.addAttribute("dir", processorConfigHandler.getDirection().getPath());

			for (ProcessorConfigInfo processorConfigInfo : processorConfigHandler) {
				Element config = DocumentHelper.createElement("config");
				config.addAttribute("class", processorConfigInfo.getKey());
				config.addAttribute("file", processorConfigInfo.getFileName());
				root.add(config);
			}

			Document document = DocumentHelper.createDocument(root);

			OutputFormat outputFormat = OutputFormat.createPrettyPrint();
			outputFormat.setEncoding("UTF-8");
			XMLWriter writer = new XMLWriter(out, outputFormat);

			try {
				writer.write(document);
			} finally {
				writer.close();
			}

		} catch (Exception e) {
			throw new SaveFailedException("读取处理器配置信息时失败", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<SaveFailedException> countinuousSave(ProcessorConfigHandler processorConfigHandler) {
		if (saveFlag)
			throw new IllegalStateException("该存储器已经使用过了");

		Objects.requireNonNull(processorConfigHandler, "入口参数 processorConfigHandler 不能为 null。");

		final Set<SaveFailedException> exceptions = new LinkedHashSet<>();
		try {

			Element root = DocumentHelper.createElement("root");
			root.addAttribute("dir", processorConfigHandler.getDirection().getPath());

			for (ProcessorConfigInfo processorConfigInfo : processorConfigHandler) {
				Element config = DocumentHelper.createElement("config");
				config.addAttribute("key", processorConfigInfo.getKey());
				config.addAttribute("file", processorConfigInfo.getFileName());
				root.add(config);
			}

			Document document = DocumentHelper.createDocument(root);

			OutputFormat outputFormat = OutputFormat.createPrettyPrint();
			outputFormat.setEncoding("UTF-8");
			XMLWriter writer = new XMLWriter(out, outputFormat);

			try {
				writer.write(document);
			} finally {
				writer.close();
			}

		} catch (Exception e) {
			exceptions.add(new SaveFailedException("读取处理器配置信息时失败", e));
		}

		return exceptions;

	}

}
