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
 * 工程的核心配置入口。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public enum ProjCoreConfigEntry implements ExconfigEntry {

	/** 处理器是否支持新建工程。 */
	RAE_PROCESSOR_SUPPORTED_NEW_PROJECT("rae.processor.supported.new.project", "false", new BooleanConfigChecker(), new BooleanValueParser()), //
	/** 处理器是否支持打开工程。 */
	RAE_PROCESSOR_SUPPORTED_OPEN_PROJECT("rae.processor.supported.open.project", "false", new BooleanConfigChecker(), new BooleanValueParser()), //
	/** 处理器是否支持保存工程。 */
	RAE_PROCESSOR_SUPPORTED_SAVE_PROJECT("rae.processor.supported.save.project", "false", new BooleanConfigChecker(), new BooleanValueParser()), //

	/** 工程是否支持通过复制的方式添加文件。 */
	RAE_PROJECT_SUPPORTED_ADDING_BYCOPY("rae.project.supported.adding.bycopy", "false", new BooleanConfigChecker(), new BooleanValueParser()), //
	/** 工程是否支持通过移动的方式添加文件。 */
	RAE_PROJECT_SUPPORTED_ADDING_BYMOVE("rae.project.supported.adding.bymove", "false", new BooleanConfigChecker(), new BooleanValueParser()), //
	/** 工程是否支持通过添加的方式添加文件。 */
	RAE_PROJECT_SUPPORTED_ADDING_BYNEW("rae.project.supported.adding.bynew", "false", new BooleanConfigChecker(), new BooleanValueParser()), //
	/** 工程是否支持通过其它方式添加文件。 */
	RAE_PROJECT_SUPPORTED_ADDING_BYOTHER("rae.project.supported.adding.byother", "false", new BooleanConfigChecker(), new BooleanValueParser()), //
	/** 工程是否支持通过删除的方式移除文件。 */
	RAE_PROJECT_SUPPORTED_REMOVING_BYDELETE("rae.project.supported.removing.bydelete", "false", new BooleanConfigChecker(), new BooleanValueParser()), //
	/** 工程是否支持通过删除的方式移除文件。 */
	RAE_PROJECT_SUPPORTED_REMOVING_BYMOVE("rae.project.supported.removing.bymove", "false", new BooleanConfigChecker(), new BooleanValueParser()), //
	/** 工程是否支持通过删除的方式移除文件。 */
	RAE_PROJECT_SUPPORTED_REMOVING_BYOTHER("rae.project.supported.removing.byother", "false", new BooleanConfigChecker(), new BooleanValueParser()), //
	/** 工程是否支持重命名文件。 */
	RAE_PROJECT_SUPPORTED_RENAME_FILE("rae.project.supported.rename.file", "false", new BooleanConfigChecker(), new BooleanValueParser()), //
	/** 工程是否支持即时保存。 */
	RAE_PROJECT_SUPPORTED_SAVE("rae.project.supported.save", "false", new BooleanConfigChecker(), new BooleanValueParser()), //

	/** 文件是否支持读取。 */
	RAE_FILE_SUPPORTED_READ("rae.file.supported.read", "false", new BooleanConfigChecker(), new BooleanValueParser()), //
	/** 文件是否支持写入。 */
	RAE_FILE_SUPPORTED_WRITE("rae.file.supported.write", "false", new BooleanConfigChecker(), new BooleanValueParser()), //

	;

	private final String name;
	private final String defaultValue;
	private final ConfigChecker configChecker;
	private final ValueParser valueParser;

	private ProjCoreConfigEntry(String name, String defaultValue, ConfigChecker configChecker,
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
