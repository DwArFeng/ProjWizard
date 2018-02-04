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

	/** 框架的核心配置的资源仓库类别。 */
	public static final String RESOURCE_CLASSIFY_CORE = "com.dwarfeng.projwiz.raefrm.core";
	/** 框架的国际化配置的资源仓库类别。 */
	public final static String RESOURCE_CLASSIFY_I18N = "com.dwarfeng.projwiz.raefrm.i18n";
	/** 默认记录器国际化文件所在的位置 */
	public final static String RESOURCE_I18N_LOGGER_PATH = "/com/dwarfeng/projwiz/resources/raefrm/configuration/i18n/logger.properties";
	/** 图片根所在的位置 */
	public final static String RESOURCE_IMAGE_ROOT_PATH = "/com/dwarfeng/projwiz/resources/core/image/";

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
