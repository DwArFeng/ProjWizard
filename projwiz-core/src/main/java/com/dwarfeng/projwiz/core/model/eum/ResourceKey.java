package com.dwarfeng.projwiz.core.model.eum;

import com.dwarfeng.dutil.basic.str.Name;
import com.dwarfeng.projwiz.core.util.Constants;

/**
 * 资源键。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public enum ResourceKey implements Name {

	/** 核心下的记录器设置。 */
	CORE_LOGGER_SETTING(Constants.CONFIGURATION_CLASSIFY_CORE, "logger.setting"),

	/** 测试下的记录器设置。 */
	TEST_LOGGER_SETTING(Constants.CONFIGURATION_CLASSIFY_TESTCASE, "logger.setting"),

	/** 主程序的配置。 */
	CFG_CORE(Constants.CONFIGURATION_CLASSIFY_CORE, "cfg.core"),

	/** 视图配置。 */
	CFG_VIEW(Constants.CONFIGURATION_CLASSIFY_CORE, "cfg.view"),

	/** 记录器国际化文件的设置。 */
	I18N_LOGGER_FILE_SETTING(Constants.CONFIGURATION_CLASSIFY_CORE, "i18n.logger.file.setting"),

	/** 标签国际化资源的设置。 */
	I18N_LABEL_FILE_SETTING(Constants.CONFIGURATION_CLASSIFY_CORE, "i18n.label.file.setting"),

	/** 记录器国际化文件的设置。 */
	I18N_LOGGER_RESOURCE_SETTING(Constants.CONFIGURATION_CLASSIFY_CORE, "i18n.logger.resource.setting"),

	/** 标签国际化资源的设置。 */
	I18N_LABEL_RESOURCE_SETTING(Constants.CONFIGURATION_CLASSIFY_CORE, "i18n.label.resource.setting"),

	/** 工程处理器的反射设置。 */
	REFLECT_PROJECT(Constants.CONFIGURATION_CLASSIFY_CORE, "reflect.projproc"),

	/** 文件处理器的反射设置。 */
	REFLECT_FILE(Constants.CONFIGURATION_CLASSIFY_CORE, "reflect.fileproc"),

	/** 配置忽略设置。 */
	CFG_IGNORE(Constants.CONFIGURATION_CLASSIFY_CORE, "cfg.ignore"),

	/** 配置额外设置。 */
	CFG_EXTRA(Constants.CONFIGURATION_CLASSIFY_CORE, "cfg.extra"),

	/** 工具包权限配置。 */
	TOOLKIT_PERM(Constants.CONFIGURATION_CLASSIFY_CORE, "toolkit.perm"),

	/** 组件忽略设置。 */
	MODULE_IGNORE(Constants.CONFIGURATION_CLASSIFY_CORE, "module.ignore"),

	/** 组件额外设置。 */
	MODULE_EXTRA(Constants.CONFIGURATION_CLASSIFY_CORE, "module.extra"),

	/** 组件工具包设置。 */
	MODULE_TOOLKIT(Constants.CONFIGURATION_CLASSIFY_CORE, "module.toolkit"),

	/** 标签国际化文本默认配置。 */
	I18N_LABEL(Constants.CONFIGURATION_CLASSIFY_I18N, "label"),

	/** 记录器国际化文本默认配置。 */
	I18N_LOGGER(Constants.CONFIGURATION_CLASSIFY_I18N, "logger"),

	;

	private final String classify;
	private final String name;

	private ResourceKey(String classify, String name) {
		this.classify = classify;
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return new StringBuilder().append(classify).append(".").append(name).toString();
	}

}
