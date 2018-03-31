package com.dwarfeng.projwiz.raefrm.model.struct;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.dwarfeng.dutil.basic.str.Name;
import com.dwarfeng.dutil.basic.threads.ExternalReadWriteThreadSafe;
import com.dwarfeng.dutil.develop.cfg.SyncExconfigModel;
import com.dwarfeng.dutil.develop.i18n.SyncI18nHandler;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;
import com.dwarfeng.projwiz.raefrm.model.cm.SyncPermDemandModel;

/**
 * 组件工具包。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public interface ComponentToolkit extends ExternalReadWriteThreadSafe{

	/**
	 * 向记录器中输出一条调试。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public void debug(Name name);

	/**
	 * 向记录器中输出一条错误。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @param e
	 *            指定的可抛出对象。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public void error(Name name, Throwable e);

	/**
	 * 向记录器中输出一条致命错误。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @param e
	 *            指定的可抛出对象。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public void fatal(Name name, Throwable e);

	/**
	 * 向记录器中格式化输出一条调试。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @param args
	 *            参数。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public void formatDebug(Name name, Object... args);

	/**
	 * 向记录器中格式化输出一条错误。
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
	public void formatError(Name name, Throwable e, Object... args);

	/**
	 * 向记录器中格式化输出一条致命错误。
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
	public void formatFatal(Name name, Throwable e, Object... args);

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
	public void formatInfo(Name name, Object... args);

	/**
	 * 返回指定标签键对应的标签格式化文本。
	 * 
	 * @param name
	 *            指定的标签键。
	 * @param args
	 *            参数。
	 * @return 指定标签键对应的格式化标签文本。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	public String formatLabel(Name name, Object... args);

	/**
	 * 向记录器中格式化输出一条显示。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @param args
	 *            参数。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public void formatTrace(Name name, Object... args);

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
	public void formatWarn(Name name, Object... args);

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
	public void formatWarn(Name name, Throwable e, Object... args);

	/**
	 * 获取组件中的常量提供器。
	 * 
	 * @return 组件中的常量提供器。
	 */
	public ConstantsProvider getConstantsProvider();

	/**
	 * 获取组件中的核心配置处理器。
	 * 
	 * @return 组件中的核心配置处理器。
	 */
	public SyncExconfigModel getCoreConfigModel();

	/**
	 * 获取组件中的标签国际化处理器。
	 * 
	 * @return 组件中的标签国际化处理器。
	 */
	public SyncI18nHandler getLabelI18nHandler();

	/**
	 * 获取组件中的记录器国际化处理器。
	 * 
	 * @return 组件中的记录器国际化处理器。
	 */
	public SyncI18nHandler getLoggerI18nHandler();

	/**
	 * 获取组件中的权限需求模型。
	 * 
	 * @return 组件中的权限需求模型。
	 */
	public SyncPermDemandModel getPermDemandModel();

	/**
	 * 获取组件中的工具包。
	 * 
	 * @return 组件中的工具包。
	 */
	public Toolkit getToolkit();

	/**
	 * 向记录器中输出一条信息。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public void info(Name name);

	/**
	 * 返回此组件的工具包是否不满足指定的权限键对应的所有需求。
	 * 
	 * @param permKey
	 *            指定的权限键。
	 * @return 此组件的工具包是否不满足指定的权限键对应的所有需求。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public boolean isNotPermKeyAvaliable(Name permKey);

	/**
	 * 返回此组件的工具包是否能满足指定的权限键对应的所有需求。
	 * 
	 * @param permKey
	 *            含有指定的权限键的名称对象。
	 * @return 此组件的工具包是否能满足指定的权限键对应的所有需求。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public boolean isPermKeyAvailable(Name permKey);

	/**
	 * 返回指定标签键对应的标签文本。
	 * 
	 * @param name
	 *            指定的标签键。
	 * @return 指定的标签键对应的标签文本。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	public String label(Name name);

	/**
	 * 打开指定资源键对应的资源。
	 * 
	 * <p>
	 * 该方法通过 Toolkit 获取只读配置处理器，随后获取指定资源键对应的资源，在资源自动重置之后，打开输入流。
	 * 
	 * @param resourceKey
	 *            指定的资源键对应的名称接口。
	 * @return 指定资源键对应的资源的输入流。
	 * @throws IOException
	 *             IO异常。
	 */
	public InputStream openResource(Name resourceKey) throws IOException;

	/**
	 * 打开指定资源键对应的资源。
	 * 
	 * <p>
	 * 该方法通过 Toolkit 获取只读配置处理器，随后获取指定资源键对应的资源，在资源自动重置之后，打开输出流。
	 * 
	 * @param resourceKey
	 *            指定的资源键对应的名称接口。
	 * @param autoReset
	 *            是否自动重置。
	 * @return 指定资源键对应的资源的输入流。
	 * @throws IOException
	 *             IO异常。
	 */
	public InputStream openResource(Name resourceKey, boolean autoReset) throws IOException;

	/**
	 * 要求此组件的工具包满足指定的权限键对应的所有需求。
	 * 
	 * @param permKey
	 *            指定的权限键。
	 * @return 此组件的工具包是否不满足指定的权限键对应的所有需求。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public void requirePermKeyAvailable(Name permKey) throws IllegalStateException;

	/**
	 * 向记录器中输出一条显示。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public void trace(Name name);

	/**
	 * 向记录器中输出一条信息。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public void warn(Name name);

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
	public void warn(Name name, Throwable e);

	/**
	 * 写入指定资源键对应的资源。
	 * 
	 * <p>
	 * 该方法通过 Toolkit 获取只读配置处理器，随后获取指定资源键对应的资源，在资源自动重置之后，打开输出流。
	 * 
	 * @param resourceKey
	 *            指定的资源键对应的名称接口。
	 * @return 指定资源键对应的资源的输出流。
	 * @throws IOException
	 *             IO异常。
	 */
	public OutputStream writeResource(Name resourceKey) throws IOException;

	/**
	 * 写入指定资源键对应的资源。
	 * 
	 * <p>
	 * 该方法通过 Toolkit 获取只读配置处理器，随后获取指定资源键对应的资源，打开输出流。
	 * 
	 * @param resourceKey
	 *            指定的资源键对应的名称接口。
	 * @param autoReset
	 *            是否自动重置。
	 * @return 指定资源键对应的资源的输出流。
	 * @throws IOException
	 *             IO异常。
	 */
	public OutputStream writeResource(Name resourceKey, boolean autoReset) throws IOException;

}
