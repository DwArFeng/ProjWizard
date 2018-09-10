package com.dwarfeng.projwiz.raefrm.model.eum;

import com.dwarfeng.dutil.develop.setting.SettingEnumItem;
import com.dwarfeng.dutil.develop.setting.SettingInfo;
import com.dwarfeng.dutil.develop.setting.info.BooleanSettingInfo;

/**
 * 工程的核心配置入口。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public enum ProjCoreConfigItem implements SettingEnumItem {

	/** 处理器是否支持新建工程。 */
	RAE_PROCESSOR_SUPPORTED_NEW_PROJECT("rae.processor.supported.new.project", new BooleanSettingInfo("false")), //
	/** 处理器是否支持打开工程。 */
	RAE_PROCESSOR_SUPPORTED_OPEN_PROJECT("rae.processor.supported.open.project", new BooleanSettingInfo("false")), //
	/** 处理器是否支持保存工程。 */
	RAE_PROCESSOR_SUPPORTED_SAVE_PROJECT("rae.processor.supported.save.project", new BooleanSettingInfo("false")), //

	/** 工程是否支持通过复制的方式添加文件。 */
	RAE_PROJECT_SUPPORTED_ADDING_BYCOPY("rae.project.supported.adding.bycopy", new BooleanSettingInfo("false")), //
	/** 工程是否支持通过移动的方式添加文件。 */
	RAE_PROJECT_SUPPORTED_ADDING_BYMOVE("rae.project.supported.adding.bymove", new BooleanSettingInfo("false")), //
	/** 工程是否支持通过添加的方式添加文件。 */
	RAE_PROJECT_SUPPORTED_ADDING_BYNEW("rae.project.supported.adding.bynew", new BooleanSettingInfo("false")), //
	/** 工程是否支持通过其它方式添加文件。 */
	RAE_PROJECT_SUPPORTED_ADDING_BYOTHER("rae.project.supported.adding.byother", new BooleanSettingInfo("false")), //
	/** 工程是否支持通过删除的方式移除文件。 */
	RAE_PROJECT_SUPPORTED_REMOVING_BYDELETE("rae.project.supported.removing.bydelete", new BooleanSettingInfo("false")), //
	/** 工程是否支持通过删除的方式移除文件。 */
	RAE_PROJECT_SUPPORTED_REMOVING_BYMOVE("rae.project.supported.removing.bymove", new BooleanSettingInfo("false")), //
	/** 工程是否支持通过删除的方式移除文件。 */
	RAE_PROJECT_SUPPORTED_REMOVING_BYOTHER("rae.project.supported.removing.byother", new BooleanSettingInfo("false")), //
	/** 工程是否支持重命名文件。 */
	RAE_PROJECT_SUPPORTED_RENAME_FILE("rae.project.supported.rename.file", new BooleanSettingInfo("false")), //
	/** 工程是否支持即时保存。 */
	RAE_PROJECT_SUPPORTED_SAVE("rae.project.supported.save", new BooleanSettingInfo("false")), //

	/** 文件是否支持读取。 */
	RAE_FILE_SUPPORTED_READ("rae.file.supported.read", new BooleanSettingInfo("false")), //
	/** 文件是否支持写入。 */
	RAE_FILE_SUPPORTED_WRITE("rae.file.supported.write", new BooleanSettingInfo("false")),//

	;
	private final String name;
	private final SettingInfo settingInfo;

	private ProjCoreConfigItem(String name, SettingInfo settingInfo) {
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
