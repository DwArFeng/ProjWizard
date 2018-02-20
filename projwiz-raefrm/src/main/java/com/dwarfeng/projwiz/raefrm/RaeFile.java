package com.dwarfeng.projwiz.raefrm;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import com.dwarfeng.dutil.basic.str.Name;
import com.dwarfeng.dutil.develop.resource.Resource;
import com.dwarfeng.dutil.develop.resource.ResourceHandler;
import com.dwarfeng.projwiz.api.AbstractFile;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;
import com.dwarfeng.projwiz.raefrm.model.struct.ProjProcToolkit;

/**
 * Rae框架文件。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public abstract class RaeFile extends AbstractFile {

	/** 对应的工程处理器的工具包。 */
	protected final ProjProcToolkit projprocToolkit;

	/**
	 * 新实例。
	 * 
	 * @param registerKey
	 *            指定的注册键。
	 * @param isFolder
	 *            是否为文件夹。
	 * @param name
	 *            文件的名称。
	 * @param projprocToolkit
	 *            对应的工程处理器的工具包。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	protected RaeFile(String registerKey, boolean isFolder, String name, ProjProcToolkit projprocToolkit) {
		this(registerKey, isFolder, name, -1, -1, -1, projprocToolkit);
	}

	/**
	 * 新实例。
	 * 
	 * @param registerKey
	 *            指定的注册键。
	 * @param isFolder
	 *            是否为文件夹。
	 * @param name
	 *            文件的名称。
	 * @param accessTime
	 *            文件的接触时间。
	 * @param createTime
	 *            文件的创建时间。
	 * @param modifyTime
	 *            文件的编辑时间。
	 * @param projprocToolkit
	 *            对应的工程处理器的工具包。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	protected RaeFile(String registerKey, boolean isFolder, String name, long accessTime, long createTime,
			long modifyTime, ProjProcToolkit projprocToolkit) {
		super(registerKey, isFolder, name, accessTime, createTime, modifyTime);

		Objects.requireNonNull(projprocToolkit, "入口参数 projprocToolkit 不能为 null。");
		this.projprocToolkit = projprocToolkit;
	}

	/**
	 * 获取组件的工具包。
	 * 
	 * @return 组件的工具包。
	 */
	protected final Toolkit getToolkit() {
		return projprocToolkit.getToolkit();
	}

	/**
	 * 向记录器中格式化输出一条信息。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @param args
	 *            参数。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void formatInfo(Name name, Object... args) {
		Objects.requireNonNull(name, "入口参数 loggerStringKey 不能为 null。");
		Objects.requireNonNull(args, "入口参数 args 不能为 null。");

		getToolkit().info(String.format(projprocToolkit.getLoggerI18nHandler().getStringOrDefault(name,
				com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL), args));
	}

	/**
	 * 向记录器中格式化输出一条警告。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @param args
	 *            参数。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void formatWarn(Name name, Object... args) {
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");
		Objects.requireNonNull(args, "入口参数 args 不能为 null。");

		getToolkit().warn(String.format(projprocToolkit.getLoggerI18nHandler().getStringOrDefault(name,
				com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL), args));
	}

	/**
	 * 向记录器中格式化输出一条警告。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @param e
	 *            指定的可抛出对象。
	 * @param args
	 *            参数。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void formatWarn(Name name, Throwable e, Object... args) {
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");
		Objects.requireNonNull(e, "入口参数 e 不能为 null。");
		Objects.requireNonNull(args, "入口参数 args 不能为 null。");

		getToolkit().warn(String.format(projprocToolkit.getLoggerI18nHandler().getStringOrDefault(name,
				com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL), args), e);
	}

	/**
	 * 向记录器中输出一条信息。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void info(Name name) {
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");

		getToolkit().info(projprocToolkit.getLoggerI18nHandler().getStringOrDefault(name,
				com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL));
	}

	/**
	 * 返回此组件的工具包是否不满足指定的权限键对应的所有需求。
	 * 
	 * @param permKey
	 *            指定的权限键。
	 * @return 此组件的工具包是否不满足指定的权限键对应的所有需求。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected boolean isNotPermKeyAvaliable(Name permKey) {
		Objects.requireNonNull(permKey, "入口参数 permKey 不能为 null。");
		return projprocToolkit.getPermDemandModel().isNotPermKeyAvaliable(permKey, getToolkit());
	}

	/**
	 * 返回此组件的工具包是否能满足指定的权限键对应的所有需求。
	 * 
	 * @param permKey
	 *            含有指定的权限键的名称对象。
	 * @return 此组件的工具包是否能满足指定的权限键对应的所有需求。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected boolean isPermKeyAvailable(Name permKey) {
		Objects.requireNonNull(permKey, "入口参数 permKey 不能为 null。");
		return projprocToolkit.getPermDemandModel().isPermKeyAvailable(permKey, getToolkit());
	}

	/**
	 * 打开指定资源键对应的资源。
	 * 
	 * <p>
	 * 该方法通过 Toolkit 获取只读配置处理器，随后获取指定资源键对应的资源，在资源自动重置之后，打开输入流。
	 * 
	 * @param resourceKey
	 *            指定的资源键对应的名称接口。
	 * @return 指定资源对应的资源键。
	 * @throws IOException
	 *             IO异常。
	 */
	protected InputStream openResource(Name resourceKey) throws IOException {
		return openResource(resourceKey, true);
	}

	/**
	 * 打开指定资源键对应的资源。
	 * 
	 * <p>
	 * 该方法通过 Toolkit 获取只读配置处理器，随后获取指定资源键对应的资源，打开输入流。
	 * 
	 * @param resourceKey
	 *            指定的资源键对应的名称接口。
	 * @param autoReset
	 *            是否自动重置。
	 * @return 指定资源对应的资源键。
	 * @throws IOException
	 *             IO异常。
	 */
	protected InputStream openResource(Name resourceKey, boolean autoReset) throws IOException {
		Objects.requireNonNull(resourceKey, "入口参数 resourceKey 不能为 null。");

		ResourceHandler cfgHandlerReadOnly = getToolkit().getCfgHandlerReadOnly();
		if (!cfgHandlerReadOnly.containsKey(resourceKey)) {
			throw new IOException(String.format("不存在指定的资源: %s", resourceKey.getName()));
		}

		Resource resource = cfgHandlerReadOnly.get(resourceKey.getName());
		if (autoReset) {
			resource.autoReset();
		}
		return resource.openInputStream();
	}

	/**
	 * 向记录器中输出一条信息。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void warn(Name name) {
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");

		getToolkit().warn(projprocToolkit.getLoggerI18nHandler().getStringOrDefault(name,
				com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL));
	}

	/**
	 * 向记录器中输出一条警告。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @param e
	 *            指定的可抛出对象。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void warn(Name name, Throwable e) {
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");

		getToolkit().warn(projprocToolkit.getLoggerI18nHandler().getStringOrDefault(name,
				com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL), e);
	}

}
