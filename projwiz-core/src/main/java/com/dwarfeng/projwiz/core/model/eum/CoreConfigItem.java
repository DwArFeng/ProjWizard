package com.dwarfeng.projwiz.core.model.eum;

import com.dwarfeng.dutil.develop.setting.SettingEnumItem;
import com.dwarfeng.dutil.develop.setting.SettingInfo;
import com.dwarfeng.dutil.develop.setting.info.BooleanSettingInfo;
import com.dwarfeng.dutil.develop.setting.info.LocaleSettingInfo;

/**
 * 核心配置入口。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public enum CoreConfigItem implements SettingEnumItem {

	/** 记录器的国际化配置 */
	I18N_LOGGER("i18n.logger", new LocaleSettingInfo("zh_CN")),
	/** 标签的国际化配置 */
	I18N_LABEL("i18n.label", new LocaleSettingInfo("zh_CN")),
	/** 是否启用超级机密设置 */
	SUPER_SECRET_SETTINGS_ENABLED("supersecretsettings.enabled", new BooleanSettingInfo("false"))

	;
	private final String name;
	private final SettingInfo settingInfo;

	private CoreConfigItem(String name, SettingInfo settingInfo) {
		this.name = name;
		this.settingInfo = settingInfo;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SettingInfo getSettingInfo() {
		return settingInfo;
	}

}
