package com.dwarfeng.projwiz.basic4.model.eum;

import com.dwarfeng.dutil.basic.str.Name;
import com.dwarfeng.projwiz.basic4.util.Constants;

/**
 * 资源键。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public enum ResourceKey implements Name {
	/** 内存工程管理器的组件的核心配置。 */
	MEPP_CONFIGURATION_CORE(Constants.CONFIGURATION_MEPP_CLASSIFY, "configuration.core"),
	/** 内存工程管理器的权限需求模型的配置。 */
	MEPP_PERM_DEMAND_SETTING(Constants.CONFIGURATION_MEPP_CLASSIFY, "perm.demand.setting"),
	/** 内存工程管理器的标签国际化处理器文件设置的资源键。 */
	MEPP_I18N_LABEL_FILE_SETTING(Constants.CONFIGURATION_MEPP_CLASSIFY, "i18n.label.file.setting"),
	/** 内存工程管理器的标签国际化处理器资源设置的资源键。 */
	MEPP_I18N_LABEL_RESOURCE_SETTING(Constants.CONFIGURATION_MEPP_CLASSIFY, "i18n.label.resource.setting"),
	/** 内存工程管理器的记录器国际化处理器文件设置的资源键。 */
	MEPP_I18N_LOGGER_FILE_SETTING(Constants.CONFIGURATION_MEPP_CLASSIFY, "i18n.logger.file.setting"),
	/** 内存工程管理器的记录器国际化处理器资源设置的资源键。 */
	MEPP_I18N_LOGGER_RESOURCE_SETTING(Constants.CONFIGURATION_MEPP_CLASSIFY, "i18n.logger.resource.setting"),
	/** 内存工程管理器的组件图片所对应的键。 */
	MEPP_IMAGE_CMPOENT(Constants.CONFIGURATION_MEPP_CLASSIFY, "image.cmpoent"),

	/** 文件夹文件管理器的组件的核心配置。 */
	FOFP_CONFIGURATION_CORE(Constants.CONFIGURATION_FOFP_CLASSIFY, "configuration.core"),
	/** 文件夹文件管理器的权限需求模型的配置。 */
	FOFP_PERM_DEMAND_SETTING(Constants.CONFIGURATION_FOFP_CLASSIFY, "perm.demand.setting"),
	/** 文件夹文件管理器的标签国际化处理器文件设置的资源键。 */
	FOFP_I18N_LABEL_FILE_SETTING(Constants.CONFIGURATION_FOFP_CLASSIFY, "i18n.label.file.setting"),
	/** 文件夹文件管理器的标签国际化处理器资源设置的资源键。 */
	FOFP_I18N_LABEL_RESOURCE_SETTING(Constants.CONFIGURATION_FOFP_CLASSIFY, "i18n.label.resource.setting"),
	/** 文件夹文件管理器的记录器国际化处理器文件设置的资源键。 */
	FOFP_I18N_LOGGER_FILE_SETTING(Constants.CONFIGURATION_FOFP_CLASSIFY, "i18n.logger.file.setting"),
	/** 文件夹文件管理器的记录器国际化处理器资源设置的资源键。 */
	FOFP_I18N_LOGGER_RESOURCE_SETTING(Constants.CONFIGURATION_FOFP_CLASSIFY, "i18n.logger.resource.setting"),
	/** 文件夹文件管理器的组件图片所对应的键。 */
	FOFP_IMAGE_CMPOENT(Constants.CONFIGURATION_FOFP_CLASSIFY, "image.cmpoent"),

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
