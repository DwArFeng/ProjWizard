package com.dwarfeng.projwiz.core.model.struct;

import com.dwarfeng.dutil.basic.prog.WithKey;

/**
 * 记录器信息。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface LoggerInfo extends WithKey<String> {

	/**
	 * 由该记录器信息对象中的信息生成一个新的记录器。
	 * 
	 * @return 由该记录器信息对象中的信息生成的一个新的记录器。
	 * @throws Exception 生成新的记录器时发生异常。
	 */
	public Logger newLogger() throws Exception;

}
