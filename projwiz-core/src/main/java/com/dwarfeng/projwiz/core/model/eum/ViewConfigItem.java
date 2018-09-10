package com.dwarfeng.projwiz.core.model.eum;

import com.dwarfeng.dutil.basic.num.Interval;
import com.dwarfeng.dutil.develop.setting.SettingEnumItem;
import com.dwarfeng.dutil.develop.setting.SettingInfo;
import com.dwarfeng.dutil.develop.setting.info.BooleanSettingInfo;
import com.dwarfeng.dutil.develop.setting.info.IntegerSettingInfo;

/**
 * 界面配置。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public enum ViewConfigItem implements SettingEnumItem {

	/** 主界面的西方面板是否可见。 */
	GUI_VISIBLE_MAINFRAME_WEST("gui.visible.mainframe.west", new BooleanSettingInfo("true")),
	/** 主界面的东方面板是否可见。 */
	GUI_VISIBLE_MAINFRAME_EAST("gui.visible.mainframe.east", new BooleanSettingInfo("true")),
	/** 主界面的被方面板是否可见。 */
	GUI_VISIBLE_MAINFRAME_NORTH("gui.visible.mainframe.north", new BooleanSettingInfo("true")),
	/** 主界面的南方面板是否可见。 */
	GUI_VISIBLE_MAINFRAME_SOUTH("gui.visible.mainframe.south", new BooleanSettingInfo("true")),

	/** 主界面是否最大化。 */
	GUI_MAXIMUM_MAINFRAME("gui.maximum.mainframe", new BooleanSettingInfo("false")),

	/** 主界面的西方面板的尺寸。 */
	GUI_SIZE_MAINFRAME_WEST("gui.size.mainframe.west", new IntegerSettingInfo("150", Interval.INTERVAL_NOT_NEGATIVE)),
	/** 主界面的南方面板的尺寸。 */
	GUI_SIZE_MAINFRAME_SOUTH("gui.size.mainframe.south", new IntegerSettingInfo("80", Interval.INTERVAL_NOT_NEGATIVE)),
	/** 主界面的东方面板的尺寸。 */
	GUI_SIZE_MAINFRAME_EAST("gui.size.mainframe.east", new IntegerSettingInfo("150", Interval.INTERVAL_NOT_NEGATIVE)),

	/** 主界面的宽度。 */
	GUI_SIZE_MAINFRAME_WIDTH("gui.size.mainframe.width", new IntegerSettingInfo("800", Interval.INTERVAL_NOT_NEGATIVE)),
	/** 主界面的高度。 */
	GUI_SIZE_MAINFRAME_HEIGHT("gui.size.mainframe.height", new IntegerSettingInfo("600", Interval.INTERVAL_NOT_NEGATIVE)),

	/** 主界面的扩展状态。 */
	GUI_STATE_MAINFRAME_EXTENDED("gui.state.mainframe.extended", new IntegerSettingInfo("0"));

	;
	private final String name;
	private final SettingInfo settingInfo;

	private ViewConfigItem(String name, SettingInfo settingInfo) {
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
