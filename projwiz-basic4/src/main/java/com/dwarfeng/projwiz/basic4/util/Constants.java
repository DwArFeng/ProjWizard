package com.dwarfeng.projwiz.basic4.util;

/**
 * 内存工程管理器的常量。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public final class Constants {

	/** 内存工程管理器的默认记录器国际化文件所在的位置。 */
	public final static String RESOURCE_MEPP_I18N_LOGGER_PATH = "/com/dwarfeng/progwiz/resources/basic4/mepp/configuration/logger.properties";
	/** 文件夹文件管理器的默认记录器国际化文件所在的位置。 */
	public final static String RESOURCE_FOFP_I18N_LOGGER_PATH = "/com/dwarfeng/progwiz/resources/basic4/fofp/configuration/logger.properties";

	/** 内存工程管理器的管理器的资源仓库类别。 */
	public final static String CONFIGURATION_MEPP_CLASSIFY = "com.dwarfeng.projwiz.basic4.mepp";
	/** 文件夹文件管理器的管理器的资源仓库类别。 */
	public final static String CONFIGURATION_FOFP_CLASSIFY = "com.dwarfeng.projwiz.basic4.fofp";

	/** 内存工程管理器的图片根所在的位置。 */
	public final static String RESOURCE_MEPP_IMAGE_ROOT_PATH = "/com/dwarfeng/progwiz/resources/basic4/mepp/image/";
	/** 文件夹文件管理器的图片根所在的位置。 */
	public final static String RESOURCE_FOFP_IMAGE_ROOT_PATH = "/com/dwarfeng/progwiz/resources/basic4/fofp/image/";

	/** 工程的根文件的名称。 */
	public final static String ROOT_FILE_NAME = "root";

	// 禁止外部实例化。
	private Constants() {
	}
}
