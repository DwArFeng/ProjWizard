package com.dwarfeng.projwiz.raefrm.model.struct;

import java.util.Collection;

import com.dwarfeng.dutil.basic.str.Name;
import com.dwarfeng.dutil.develop.setting.SettingEnumItem;
import com.dwarfeng.projwiz.core.model.struct.FileProcessor;
import com.dwarfeng.projwiz.core.model.struct.ProjectProcessor;
import com.dwarfeng.projwiz.raefrm.model.eum.FileCoreConfigItem;
import com.dwarfeng.projwiz.raefrm.model.eum.ProjCoreConfigItem;

/**
 * 常量提供接口。
 * 
 * <p>
 * 该接口提供Rae框架下的个性化配置所需要的所有常量。
 * <p>
 * 由于提供的是常量，因此不需要实现线程安全。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public interface ConstantsProvider {

	public enum ResourceKeyType {
		/** 组件的核心配置。 */
		CONFIGURATION_CORE,

		/** 标签国际化处理器文件设置的资源键。 */
		I18N_LABEL_FILE_SETTING,
		/** 标签国际化处理器资源设置的资源键。 */
		I18N_LABEL_RESOURCE_SETTING,
		/** 记录器国际化处理器文件设置的资源键。 */
		I18N_LOGGER_FILE_SETTING,
		/** 记录器国际化处理器资源设置的资源键。 */
		I18N_LOGGER_RESOURCE_SETTING,

		/** 权限需求模型的配置。 */
		PERM_DEMAND_SETTING,

		/** 组件图片所对应的键。 */
		IMAGE_MODULE,

		;
	}

	/**
	 * 获取默认的记录器国际化接口所在的路径（包内路径）。
	 * 
	 * @return 默认的记录器国际化接口所在的路径（包内路径）。
	 */
	public String getDefaultLoggerI18nPath();

	/**
	 * 获取指定资源键类型对应的资源键的名称接口。
	 * 
	 * @param type
	 *            指定的资源键类型。
	 * @return 资源键类型对应的资源键的名称接口。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public Name getResourceKey(ResourceKeyType type);

	/**
	 * 获取该组件所有的额核心置入口。
	 * <p>
	 * 对于工程处理器 {@link ProjectProcessor} 来说，返回的集合应包含 {@link ProjCoreConfigItem}
	 * 中的所有元素。
	 * <p>
	 * 对于文件处理器 {@link FileProcessor} 来说，返回的集合应包含 {@link FileCoreConfigItem}
	 * 中的所有元素。
	 * 
	 * @return 该组件所有的核心配置入口组成的集合。
	 */
	public Collection<SettingEnumItem> getSettingEnumItems();
}
