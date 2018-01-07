package com.dwarfeng.projwiz.core.model.cm;

import java.util.Collection;

import com.dwarfeng.dutil.basic.cna.model.KeySetModel;
import com.dwarfeng.projwiz.core.model.struct.Logger;
import com.dwarfeng.projwiz.core.model.struct.LoggerInfo;

/**
 * 记录器处理器。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface LoggerHandler extends KeySetModel<String, LoggerInfo> {

	/**
	 * 使用某个键值对应的记录器。
	 * 
	 * @param keys
	 *            指定的键值。
	 * @return 是否改变了处理器。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public boolean use(String... keys);

	/**
	 * 停止使用某个键值。
	 * 
	 * @param keys
	 *            指定的键值。
	 * @return 是否改变了该记录器处理器。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public boolean unuse(String... keys);

	/**
	 * 使用处理器中的所有键值。
	 * 
	 * @return 是否改变了该记录器处理器。
	 */
	public boolean useAll();

	/**
	 * 停止使用处理器中的所有键值。
	 * 
	 * @return 是否改变了该记录器处理器。
	 */
	public boolean unuseAll();

	/**
	 * 获取该记录器处理中中所有正在使用的 <code>Logger</code>。
	 * 
	 * @return 该记录器处理中中所有正在使用的 <code>Logger</code>。
	 */
	public Collection<Logger> usedLoggers();

	/**
	 * 使用指定的记录器处理器 <code>trace</code> 一条信息。
	 * 
	 * @param message
	 *            指定的信息。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public void trace(String message);

	/**
	 * 使用指定的记录器处理器 <code>debug</code> 一条信息。
	 * 
	 * @param message
	 *            指定的信息。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public void debug(String message);

	/**
	 * 使用指定的记录器处理器 <code>info</code> 一条信息。
	 * 
	 * @param message
	 *            指定的信息。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public void info(String message);

	/**
	 * 使用指定的记录器处理器 <code>warn</code> 一条信息。
	 * 
	 * @param message
	 *            指定的信息。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public void warn(String message);

	/**
	 * 使用指定的记录器处理器 <code>warn</code> 一条信息。
	 * 
	 * @param message
	 *            指定的信息。
	 * @param t
	 *            指定的可抛出对象。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public void warn(String message, Throwable t);

	/**
	 * 使用指定的记录器处理器 <code>error</code> 一条信息。
	 * 
	 * @param message
	 *            指定的信息。
	 * @param t
	 *            指定的可抛出对象。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public void error(String message, Throwable t);

	/**
	 * 使用指定的记录器处理器 <code>fatal</code> 一条信息。
	 * 
	 * @param message
	 *            指定的信息。
	 * @param t
	 *            指定的可抛出对象。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public void fatal(String message, Throwable t);

}
