package com.dwarfeng.projwiz.raefrm.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import com.dwarfeng.projwiz.core.model.struct.Toolkit;

/**
 * 框架常量。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public final class Constants {
	
	/***/

	/** 运行框架所需要的最基本权限。 */
	public final static Collection<Toolkit.Method> BASIC_PERMS = Collections
			.unmodifiableCollection(Arrays.asList(new Toolkit.Method[] { Toolkit.Method.GETCFGHANDLERREADONLY,
					Toolkit.Method.TRACE, Toolkit.Method.DEBUG, Toolkit.Method.INFO, Toolkit.Method.WARN,
					Toolkit.Method.ERROR, Toolkit.Method.FATAL, Toolkit.Method.ADDCORECONFIGOBVERSER,
					Toolkit.Method.REMOVECORECONFIGOBVERSER, Toolkit.Method.GETCORECONFIGMODELREADONLY }));

	// 禁止外部实例化。
	private Constants() {

	}

}
