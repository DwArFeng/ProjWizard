package com.dwarfeng.projwiz.basic4.model.eum;

import com.dwarfeng.dutil.basic.num.Interval;
import com.dwarfeng.dutil.develop.setting.SettingEnumItem;
import com.dwarfeng.dutil.develop.setting.SettingInfo;
import com.dwarfeng.dutil.develop.setting.info.BooleanSettingInfo;
import com.dwarfeng.dutil.develop.setting.info.IntegerSettingInfo;
import com.dwarfeng.dutil.develop.setting.info.StringSettingInfo;

/**
 * 内存文件处理器的配置入口。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public enum MeppConfigItem implements SettingEnumItem {

	/** MEPP处理器文件新标签的默认分配大小。 */
	PROCESSOR_DEFAULTBUFFCAPA_VALUE("processor.defaultbuffcapa.value", new IntegerSettingInfo("1024", Interval.INTERVAL_NOT_NEGATIVE)), //
	/** MEPP处理器是否在新建工程的时候询问用户文件新标签的默认分配大小。 */
	PROCESSOR_DEFAULTBUFFCAPA_ISASK("processor.defaultbuffcapa.isask", new BooleanSettingInfo("true")), //
	/** MEPP处理在新建工程的时候是否由用户指派名称。 */
	PROCESSOR_PROJNAME_ISASSIGN("processor.projname.isassign", new BooleanSettingInfo("true")), //

	/** MEPP处理在新建工程的时候是否由用户指派名称。//TODO 使用国际化来实现这项功能。 */
	PROJECT_NEWFILE_NAME_DEFAULT("project.newfile.name.default", new StringSettingInfo("新建文件")), //
	/** MEPP处理在新建工程的时候是否由用户指派名称。 */
	PROJECT_BUFFER_DATATRANS("project.data.datatrans", new IntegerSettingInfo("4096", Interval.INTERVAL_NOT_NEGATIVE)),//

	;
	private final String name;
	private final SettingInfo settingInfo;

	private MeppConfigItem(String name, SettingInfo settingInfo) {
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
