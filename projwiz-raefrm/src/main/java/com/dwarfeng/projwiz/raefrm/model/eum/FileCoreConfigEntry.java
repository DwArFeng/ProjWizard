package com.dwarfeng.projwiz.raefrm.model.eum;

import com.dwarfeng.dutil.develop.cfg.ConfigChecker;
import com.dwarfeng.dutil.develop.cfg.ConfigFirmProps;
import com.dwarfeng.dutil.develop.cfg.ConfigKey;
import com.dwarfeng.dutil.develop.cfg.DefaultConfigFirmProps;
import com.dwarfeng.dutil.develop.cfg.checker.BooleanConfigChecker;
import com.dwarfeng.dutil.develop.cfg.parser.BooleanValueParser;
import com.dwarfeng.dutil.develop.cfg.struct.ExconfigEntry;
import com.dwarfeng.dutil.develop.cfg.struct.ValueParser;

/**
 * 文件的核心配置入口。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public enum FileCoreConfigEntry implements ExconfigEntry {

	/** 处理器是否支持新建工程。 */
	RAE_PROCESSOR_SUPPORTED_NEW_EDITOR("rae.processor.supported.new.editor", "false", new BooleanConfigChecker(), new BooleanValueParser()), //
	/** 处理器是否支持打开工程。 */
	RAE_PROCESSOR_SUPPORTED_NEW_FILE("rae.processor.supported.new.file", "false", new BooleanConfigChecker(), new BooleanValueParser()), //

	;

	private final String name;
	private final String defaultValue;
	private final ConfigChecker configChecker;
	private final ValueParser valueParser;

	private FileCoreConfigEntry(String name, String defaultValue, ConfigChecker configChecker,
			ValueParser valueParser) {
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
