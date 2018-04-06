package com.dwarfeng.projwiz.core.model.io;

import java.io.OutputStream;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.dwarfeng.dutil.basic.cna.model.MapModel;
import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.dutil.basic.io.SaveFailedException;
import com.dwarfeng.dutil.basic.io.StreamSaver;
import com.dwarfeng.projwiz.core.model.struct.LnpToolkit;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;
import com.dwarfeng.projwiz.core.model.struct.Toolkit.Method;

public final class ModuleToolkitSaver extends StreamSaver<MapModel<String, ReferenceModel<Toolkit>>> {

	private boolean saveFlag;

	/**
	 * 新实例。
	 * 
	 * @param out
	 *            指定的输出流。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public ModuleToolkitSaver(OutputStream out) {
		super(out);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save(MapModel<String, ReferenceModel<Toolkit>> moduleToolkitModel)
			throws SaveFailedException, IllegalStateException {
		if (saveFlag)
			throw new IllegalStateException("该存储器已经使用过了");

		Objects.requireNonNull(moduleToolkitModel, "入口参数 moduleToolkitModel 不能为 null。");

		try {
			saveFlag = true;

			Element root = DocumentHelper.createElement("root");

			for (Map.Entry<String, ReferenceModel<Toolkit>> entry : moduleToolkitModel.entrySet()) {
				Element info = DocumentHelper.createElement("info");
				info.addAttribute("key", entry.getKey());

				Element toolkitElement = DocumentHelper.createElement("toolkit");
				Toolkit toolkit = entry.getValue().get();
				if (Objects.isNull(toolkit)) {
					toolkitElement.addAttribute("perm-level", "0");

				} else if (toolkit instanceof LnpToolkit) {
					LnpToolkit toolkit0 = (LnpToolkit) toolkit;
					toolkitElement.addAttribute("perm-level", Integer.toString(toolkit0.getPermLevel()));

					for (Toolkit.Method privilege : toolkit0.getPrivileges()) {
						Element privilegeElement = DocumentHelper.createElement("privilege");
						privilegeElement.addText(privilege.toString());
						toolkitElement.add(privilegeElement);
					}

				} else {
					toolkitElement.addAttribute("perm-level", "0");
					for (Method method : Method.values()) {
						if (toolkit.hasPermission(method)) {
							Element privilegeElement = DocumentHelper.createElement("privilege");
							privilegeElement.addText(method.toString());
							toolkitElement.add(privilegeElement);
						}
					}
				}

				info.add(toolkitElement);
				root.add(info);
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
			throw new SaveFailedException("存储处理器配置信息时失败", e);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<SaveFailedException> countinuousSave(MapModel<String, ReferenceModel<Toolkit>> moduleToolkitModel)
			throws IllegalStateException {
		if (saveFlag)
			throw new IllegalStateException("该存储器已经使用过了");

		Objects.requireNonNull(moduleToolkitModel, "入口参数 moduleToolkitModel 不能为 null。");

		final Set<SaveFailedException> exceptions = new LinkedHashSet<>();
		try {
			saveFlag = true;

			Element root = DocumentHelper.createElement("root");

			for (Map.Entry<String, ReferenceModel<Toolkit>> entry : moduleToolkitModel.entrySet()) {
				Element info = DocumentHelper.createElement("info");
				info.addAttribute("key", entry.getKey());

				Element toolkitElement = DocumentHelper.createElement("toolkit");
				Toolkit toolkit = entry.getValue().get();
				if (Objects.isNull(toolkit)) {
					toolkitElement.addAttribute("perm-level", "0");

				} else if (toolkit instanceof LnpToolkit) {
					LnpToolkit toolkit0 = (LnpToolkit) toolkit;
					toolkitElement.addAttribute("perm-level", Integer.toString(toolkit0.getPermLevel()));

					for (Toolkit.Method privilege : toolkit0.getPrivileges()) {
						Element privilegeElement = DocumentHelper.createElement("privilege");
						privilegeElement.addText(privilege.toString());
						toolkitElement.add(privilegeElement);
					}

				} else {
					toolkitElement.addAttribute("perm-level", "0");
					for (Method method : Method.values()) {
						if (toolkit.hasPermission(method)) {
							Element privilegeElement = DocumentHelper.createElement("privilege");
							privilegeElement.addText(method.toString());
							toolkitElement.add(privilegeElement);
						}
					}
				}

				info.add(toolkitElement);
				root.add(info);
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
