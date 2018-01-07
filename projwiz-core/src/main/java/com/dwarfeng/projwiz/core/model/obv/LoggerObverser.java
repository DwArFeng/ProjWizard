package com.dwarfeng.projwiz.core.model.obv;

import com.dwarfeng.dutil.basic.cna.model.obv.SetObverser;
import com.dwarfeng.projwiz.core.model.struct.Logger;
import com.dwarfeng.projwiz.core.model.struct.LoggerInfo;

/**
 * 记录器观察器。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface LoggerObverser extends SetObverser<LoggerInfo> {

	/**
	 * 通知记录器观察器指定的键对应的记录器被使用。
	 * 
	 * @param key
	 *            指定的键。
	 * @param logger
	 *            指定的键对应的记录器。
	 */
	public void fireLoggerUsed(String key, Logger logger);

	/**
	 * 通知记录器观察器指定的键对应的记录器被移除。
	 * 
	 * @param key
	 *            指定的键。
	 * @param logger
	 *            指定的键对应的记录器。
	 */
	public void fireLoggerUnused(String key, Logger logger);

}
