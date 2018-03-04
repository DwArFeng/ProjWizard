package com.dwarfeng.projwiz.basic4.model.eum;

import com.dwarfeng.dutil.develop.cfg.ConfigChecker;
import com.dwarfeng.dutil.develop.cfg.ConfigFirmProps;
import com.dwarfeng.dutil.develop.cfg.ConfigKey;
import com.dwarfeng.dutil.develop.cfg.DefaultConfigFirmProps;
import com.dwarfeng.dutil.develop.cfg.struct.ExconfigEntry;
import com.dwarfeng.dutil.develop.cfg.struct.ValueParser;

public enum FofpConfigEntry implements ExconfigEntry {

	;

	private final String name;
	private final String defaultValue;
	private final ConfigChecker configChecker;
	private final ValueParser valueParser;

	private FofpConfigEntry(String name, String defaultValue, ConfigChecker configChecker, ValueParser valueParser) {
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
