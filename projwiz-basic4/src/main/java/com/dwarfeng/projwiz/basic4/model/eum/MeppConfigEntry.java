package com.dwarfeng.projwiz.basic4.model.eum;

import com.dwarfeng.dutil.develop.cfg.ConfigChecker;
import com.dwarfeng.dutil.develop.cfg.ConfigFirmProps;
import com.dwarfeng.dutil.develop.cfg.ConfigKey;
import com.dwarfeng.dutil.develop.cfg.DefaultConfigFirmProps;
import com.dwarfeng.dutil.develop.cfg.checker.BooleanConfigChecker;
import com.dwarfeng.dutil.develop.cfg.checker.IntegerConfigChecker;
import com.dwarfeng.dutil.develop.cfg.parser.BooleanValueParser;
import com.dwarfeng.dutil.develop.cfg.parser.IntegerValueParser;
import com.dwarfeng.dutil.develop.cfg.parser.StringValueParser;
import com.dwarfeng.dutil.develop.cfg.struct.ExconfigEntry;
import com.dwarfeng.dutil.develop.cfg.struct.ValueParser;
import com.dwarfeng.projwiz.core.util.ProjectFileUtil;

/**
 * 内存文件处理器的配置入口。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public enum MeppConfigEntry implements ExconfigEntry {

	/** MEPP处理器文件新标签的默认分配大小。 */
	PROCESSOR_DEFAULTBUFFCAPA_VALUE("processor.defaultbuffcapa.value", "1024", new IntegerConfigChecker(0, Integer.MAX_VALUE), new IntegerValueParser()),
	/** MEPP处理器是否在新建工程的时候询问用户文件新标签的默认分配大小。 */
	PROCESSOR_DEFAULTBUFFCAPA_ISASK("processor.defaultbuffcapa.isask", "true", new BooleanConfigChecker(), new BooleanValueParser()),
	/** MEPP处理在新建工程的时候是否由用户指派名称。 */
	PROCESSOR_PROJNAME_ISASSIGN("processor.projname.isassign", "true", new BooleanConfigChecker(), new BooleanValueParser()),

	/** MEPP处理在新建工程的时候是否由用户指派名称。 */
	PROJECT_NEWFILE_NAME_DEFAULT("project.newfile.name.default", "新建文件", ProjectFileUtil.newFileNameChecker(), new StringValueParser()),
	/** MEPP处理在新建工程的时候是否由用户指派名称。 */
	PROJECT_BUFFER_DATATRANS("project.data.datatrans", "4096", new IntegerConfigChecker(0, Integer.MAX_VALUE), new IntegerValueParser()),

	;

	private final String name;
	private final String defaultValue;
	private final ConfigChecker configChecker;
	private final ValueParser valueParser;

	private MeppConfigEntry(String name, String defaultValue, ConfigChecker configChecker, ValueParser valueParser) {
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
