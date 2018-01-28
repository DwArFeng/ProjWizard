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
	CORE_LOGGER_SETTING(Constants.RESOURCE_CLASSIFY_CORE, "logger.setting"),

	/** 测试下的记录器设置。 */
	TEST_LOGGER_SETTING(Constants.RESOURCE_CLASSIFY_TESTCASE, "logger.setting"),

	/** 主程序的配置。 */
	CFG_CORE(Constants.RESOURCE_CLASSIFY_CORE, "cfg.core"),

	/** 视图配置。 */
	CFG_VIEW(Constants.RESOURCE_CLASSIFY_CORE, "cfg.view"),

	/** 记录器国际化文件的设置。 */
	I18N_LOGGER_FILE_SETTING(Constants.RESOURCE_CLASSIFY_CORE, "i18n.logger.file.setting"),

	/** 标签国际化资源的设置。 */
	I18N_LABEL_FILE_SETTING(Constants.RESOURCE_CLASSIFY_CORE, "i18n.label.file.setting"),

	/** 记录器国际化文件的设置。 */
	I18N_LOGGER_RESOURCE_SETTING(Constants.RESOURCE_CLASSIFY_CORE, "i18n.logger.resource.setting"),

	/** 标签国际化资源的设置。 */
	I18N_LABEL_RESOURCE_SETTING(Constants.RESOURCE_CLASSIFY_CORE, "i18n.label.resource.setting"),

	/** 工程处理器的反射设置。 */
	REFLECT_PROJECT(Constants.RESOURCE_CLASSIFY_CORE, "reflect.projproc"),

	/** 文件处理器的反射设置。 */
	REFLECT_FILE(Constants.RESOURCE_CLASSIFY_CORE, "reflect.fileproc"),

	/** 配置忽略设置。 */
	CFG_IGNORE(Constants.RESOURCE_CLASSIFY_CORE, "cfg.ignore"),

	/** 配置额外设置。 */
	CFG_EXTRA(Constants.RESOURCE_CLASSIFY_CORE, "cfg.extra"),

	/** 工具包权限配置。 */
	TOOLKIT_PERM(Constants.RESOURCE_CLASSIFY_CORE, "toolkit.perm"),

	/** 组件忽略设置。 */
	CMPOENT_IGNORE(Constants.RESOURCE_CLASSIFY_CORE, "cmpoent.ignore"),

	/** 组件额外设置。 */
	CMPOENT_EXTRA(Constants.RESOURCE_CLASSIFY_CORE, "cmpoent.extra"),

	/** 标签国际化文本默认配置。 */
	I18N_LABEL(Constants.RESOURCE_CLASSIFY_I18N, "label"),

	/** 标签国际化文本简体中文配置。 */
	I18N_LABEL_ZH_CN(Constants.RESOURCE_CLASSIFY_I18N, "label_zh_CN"),

	/** 记录器国际化文本默认配置。 */
	I18N_LOGGER(Constants.RESOURCE_CLASSIFY_I18N, "logger"),

	/** 记录器国际化文本简体中文配置。 */
	I18N_LOGGER_ZH_CN(Constants.RESOURCE_CLASSIFY_I18N, "logger_zh_CN.pr")

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
