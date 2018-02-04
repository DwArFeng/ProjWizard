package com.dwarfeng.projwiz.raefrm.model.struct;

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

		/** 标签国际化处理器文件设置的资源键。 */
		I18N_LABEL_FILE_SETTING,
		/** 标签国际化处理器资源设置的资源键。 */
		I18N_LABEL_RESOURCE_SETTING,
		/** 记录器国际化处理器文件设置的资源键。 */
		I18N_LOGGER_FILE_SETTING,
		/** 记录器国际化处理器资源设置的资源键。 */
		I18N_LOGGER_RESOURCE_SETTING,
		/** 权限需求模型的配置。 */
		DEMAND_MODEL_SETTING,

	}

	/**
	 * 获取默认的记录器国际化接口所在的路径。
	 * 
	 * @return 默认的记录器国际化接口所在的路径。
	 */
	public String getDefaultLoggerI18nPath();

	/**
	 * 获取指定资源键类型对应的资源键。
	 * 
	 * @param type
	 *            指定的资源键类型。
	 * @return 资源键类型对应的资源键。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public String getResourceKey(ResourceKeyType type);
}
