package com.dwarfeng.projwiz.core.model.eum;

import com.dwarfeng.dutil.develop.cfg.ConfigChecker;
import com.dwarfeng.dutil.develop.cfg.ConfigFirmProps;
import com.dwarfeng.dutil.develop.cfg.ConfigKey;
import com.dwarfeng.dutil.develop.cfg.DefaultConfigFirmProps;
import com.dwarfeng.dutil.develop.cfg.checker.BooleanConfigChecker;
import com.dwarfeng.dutil.develop.cfg.checker.LocaleConfigChecker;
import com.dwarfeng.dutil.develop.cfg.parser.BooleanValueParser;
import com.dwarfeng.dutil.develop.cfg.parser.LocaleValueParser;
import com.dwarfeng.dutil.develop.cfg.struct.ExconfigEntry;
import com.dwarfeng.dutil.develop.cfg.struct.ValueParser;

/**
 * 核心配置入口。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public enum CoreConfigEntry implements ExconfigEntry {

	/** 记录器的国际化配置 */
	I18N_LOGGER("i18n.logger", "zh_CN", new LocaleConfigChecker(), new LocaleValueParser()),
	/** 标签的国际化配置 */
	I18N_LABEL("i18n.label", "zh_CN", new LocaleConfigChecker(), new LocaleValueParser()),
	/** 是否启用超级机密设置 */
	SUPER_SECRET_SETTINGS_ENABLED("supersecretsettings.enabled", "false", new BooleanConfigChecker(), new BooleanValueParser()),

	;

	private final String name;
	private final String defaultValue;
	private final ConfigChecker configChecker;
	private final ValueParser valueParser;

	private CoreConfigEntry(String name, String defaultValue, ConfigChecker configChecker, ValueParser valueParser) {
		this.name = name;
		this.defaultValue = defaultValue;
		this.configChecker = configChecker;
		this.valueParser = valueParser;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigKey getConfigKey() {
		return new ConfigKey(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConfigFirmProps getConfigFirmProps() {
		return new DefaultConfigFirmProps(configChecker, defaultValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValueParser getValueParser() {
		return valueParser;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCurrentValue() {
		return defaultValue;
	}

}
