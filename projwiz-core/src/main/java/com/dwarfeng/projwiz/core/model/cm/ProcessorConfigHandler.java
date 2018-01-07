package com.dwarfeng.projwiz.core.model.cm;

import java.io.File;

import com.dwarfeng.dutil.basic.cna.model.KeySetModel;
import com.dwarfeng.dutil.develop.resource.Resource;
import com.dwarfeng.projwiz.core.model.struct.ProcessorConfigInfo;

/**
 * 处理器配置处理器。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface ProcessorConfigHandler extends KeySetModel<String, ProcessorConfigInfo> {

	/**
	 * 获取该处理器指向的目录。
	 * 
	 * @return 该处理器指向的目录。
	 */
	public File getDirection();

	/**
	 * 设置该处理器指向的目录。
	 * 
	 * @param direction
	 *            该处理器指向的目录，如果为 <code>null</code>，则表示当前文件夹。
	 * @return 是否设置成功。
	 */
	public boolean setDirection(File direction);

	/**
	 * 生成一个新的指定的键值对应的资源。
	 * 
	 * @param key
	 *            指定的键值。
	 * @return 指定的资源。
	 */
	public Resource newResource(String key);

}
