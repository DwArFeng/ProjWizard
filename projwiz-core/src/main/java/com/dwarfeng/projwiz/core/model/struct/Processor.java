package com.dwarfeng.projwiz.core.model.struct;

import java.awt.Image;

import com.dwarfeng.dutil.basic.prog.ObverserSet;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.dutil.basic.prog.WithKey;
import com.dwarfeng.dutil.basic.str.Tag;
import com.dwarfeng.dutil.basic.threads.ExternalReadWriteThreadSafe;
import com.dwarfeng.dutil.develop.resource.Resource;
import com.dwarfeng.projwiz.core.model.obv.ProcessorObverser;

/**
 * 处理器接口。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface Processor extends WithKey<String>, ExternalReadWriteThreadSafe, Tag, ObverserSet<ProcessorObverser> {

	/**
	 * 获取指向其配置的资源。
	 * 
	 * @return 指向其配置的资源。
	 */
	public Resource getConfigResource();

	/**
	 * 获取该工程处理器的图标。
	 * 
	 * @return 该工程处理器的图标。
	 */
	public Image getIcon();

	/**
	 * 获取该处理器的工具包。
	 * 
	 * @return 该处理器的工具包。
	 */
	public Toolkit getToolkit();

	/**
	 * 读取处理器配置。
	 * <p>
	 * 该方法在程序开启或者装载处理器时调用，意图是让处理器将配置文件载入处理器。
	 * 
	 * @throws ProcessException
	 *             过程异常。
	 */
	public void loadConfig() throws ProcessException;

	/**
	 * 保存处理器的配置。
	 * <p>
	 * 该方法在程序关闭或者卸载处理器时调用，意图是让处理器将现有的配置持久化。
	 * 
	 * @throws ProcessException
	 *             过程异常。
	 */
	public void saveConfig() throws ProcessException;

	/**
	 * 设置该处理器的工具包。
	 * 
	 * @param toolkit
	 *            指定的工具包。
	 * @return 是否设置成功。
	 */
	public boolean setToolkit(Toolkit toolkit);

}
