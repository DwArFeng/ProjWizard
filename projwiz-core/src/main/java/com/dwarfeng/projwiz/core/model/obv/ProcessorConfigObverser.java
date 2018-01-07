package com.dwarfeng.projwiz.core.model.obv;

import java.io.File;

import com.dwarfeng.dutil.basic.cna.model.obv.SetObverser;
import com.dwarfeng.projwiz.core.model.struct.ProcessorConfigInfo;

/**
 * 处理器配置处理器观察器。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface ProcessorConfigObverser extends SetObverser<ProcessorConfigInfo> {

	/**
	 * 通知处理器的目录发生了改变。
	 * 
	 * @param oldValue
	 *            旧的目录。
	 * @param newValue
	 *            新的目录。
	 */
	public void fireDirectionChanged(File oldValue, File newValue);

}
