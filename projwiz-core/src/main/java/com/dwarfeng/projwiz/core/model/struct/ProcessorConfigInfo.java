package com.dwarfeng.projwiz.core.model.struct;

import java.io.File;

import com.dwarfeng.dutil.basic.prog.WithKey;
import com.dwarfeng.dutil.develop.resource.Resource;

/**
 * 处理器配置信息。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface ProcessorConfigInfo extends WithKey<String> {

	/**
	 * 获取存储配置的文件的名称。
	 * 
	 * @return 文件的名称。
	 */
	public String getFileName();

	/**
	 * 从处理器配置信息中生成一个新的资源。
	 * 
	 * @param direction
	 *            指定的根目录。
	 * @return 从处理器配置信息中生成的新的资源。
	 */
	public Resource newResource(File direction);

}
