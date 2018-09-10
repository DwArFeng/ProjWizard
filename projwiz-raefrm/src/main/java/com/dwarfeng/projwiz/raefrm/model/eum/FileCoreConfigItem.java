package com.dwarfeng.projwiz.raefrm.model.eum;

import com.dwarfeng.dutil.develop.setting.SettingEnumItem;
import com.dwarfeng.dutil.develop.setting.SettingInfo;
import com.dwarfeng.dutil.develop.setting.info.BooleanSettingInfo;

/**
 * 文件的核心配置入口。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public enum FileCoreConfigItem implements SettingEnumItem {

	/** 处理器是否支持新建工程。 */
	RAE_PROCESSOR_SUPPORTED_NEW_EDITOR("rae.processor.supported.new.editor", new BooleanSettingInfo("false")), //
	/** 处理器是否支持打开工程。 */
	RAE_PROCESSOR_SUPPORTED_NEW_FILE("rae.processor.supported.new.file", new BooleanSettingInfo("false")),//

	;
	private final String name;
	private final SettingInfo settingInfo;

	private FileCoreConfigItem(String name, SettingInfo settingInfo) {
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
