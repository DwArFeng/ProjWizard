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
	/** 组件的核心配置。 */
	MEPP_CONFIGURATION_CORE(Constants.CONFIGURATION_MEPP_CLASSIFY, "configuration.core"),
	/** 权限需求模型的配置。 */
	MEPP_PERM_DEMAND_SETTING(Constants.CONFIGURATION_MEPP_CLASSIFY, "perm.demand.setting"),
	/** 标签国际化处理器文件设置的资源键。 */
	MEPP_I18N_LABEL_FILE_SETTING(Constants.CONFIGURATION_MEPP_CLASSIFY, "i18n.label.file.setting"),
	/** 标签国际化处理器资源设置的资源键。 */
	MEPP_I18N_LABEL_RESOURCE_SETTING(Constants.CONFIGURATION_MEPP_CLASSIFY, "i18n.label.resource.setting"),
	/** 记录器国际化处理器文件设置的资源键。 */
	MEPP_I18N_LOGGER_FILE_SETTING(Constants.CONFIGURATION_MEPP_CLASSIFY, "i18n.logger.file.setting"),
	/** 记录器国际化处理器资源设置的资源键。 */
	MEPP_I18N_LOGGER_RESOURCE_SETTING(Constants.CONFIGURATION_MEPP_CLASSIFY, "i18n.logger.resource.setting"),
	/** 组件图片所对应的键。 */
	MEPP_IMAGE_CMPOENT(Constants.CONFIGURATION_MEPP_CLASSIFY, "image.cmpoent"),

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
