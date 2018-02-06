package com.dwarfeng.projwiz.raefrm.model.eum;

import com.dwarfeng.dutil.develop.cfg.ConfigChecker;
import com.dwarfeng.dutil.develop.cfg.ConfigFirmProps;
import com.dwarfeng.dutil.develop.cfg.ConfigKey;
import com.dwarfeng.dutil.develop.cfg.DefaultConfigFirmProps;
import com.dwarfeng.dutil.develop.cfg.struct.ExconfigEntry;
import com.dwarfeng.dutil.develop.cfg.struct.ValueParser;

/**
 * 配置入口。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public enum ConfigEntry implements ExconfigEntry {
	// /*
	// * 以下配置适用于工程处理器，其它组件可以不实现。
	// */
	// /** 该工程处理器是否允许新建工程。 */
	// PROJPROC_SUPPORTED_NEWPROJECT("projproc.supported.newproject", "false",
	// new BooleanConfigChecker(), new BooleanValueParser()),
	// /** 该工程处理器是否允许打开工程。 */
	// PROJPROC_SUPPORTED_OPENPROJECT("projproc.supported.openproject", "false",
	// new BooleanConfigChecker(), new BooleanValueParser()),
	// /** 该工程处理器是否允许保存工程。 */
	// PROJPROC_SUPPORTED_SAVEPROJECT("projproc.supported.saveproject", "false",
	// new BooleanConfigChecker(), new BooleanValueParser()),

	;

	private final String name;
	private final String defaultValue;
	private final ConfigChecker configChecker;
	private final ValueParser valueParser;

	private ConfigEntry(String name, String defaultValue, ConfigChecker configChecker, ValueParser valueParser) {
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
