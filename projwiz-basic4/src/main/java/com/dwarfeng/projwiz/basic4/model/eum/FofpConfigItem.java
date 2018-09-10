package com.dwarfeng.projwiz.basic4.model.eum;

import com.dwarfeng.dutil.develop.setting.SettingEnumItem;
import com.dwarfeng.dutil.develop.setting.SettingInfo;
import com.dwarfeng.dutil.develop.setting.info.StringSettingInfo;

public enum FofpConfigItem implements SettingEnumItem {

	/** 文件中描述对应的标签定义。 */
	FILE_DEFINE_LABEL_DESCRIPTION("file.define.label.description", new StringSettingInfo("description")),//

	;
	private final String name;
	private final SettingInfo settingInfo;

	private FofpConfigItem(String name, SettingInfo settingInfo) {
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
