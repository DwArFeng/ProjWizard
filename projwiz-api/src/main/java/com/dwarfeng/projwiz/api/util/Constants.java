package com.dwarfeng.projwiz.api.util;

import java.awt.Image;

/**
 * API常量。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public final class Constants {

	/** 代表图片不存在的图片。 */
	public final static Image IMAGE_LOAD_FAILED = com.dwarfeng.projwiz.core.util.Constants.IMAGE_LOAD_FAILED;

	/** 图片根所在的位置 */
	public final static String RESOURCE_IMAGE_ROOT_PATH = "/com/dwarfeng/projwiz/resources/api/image/";

	// 禁止外部实例化。
	private Constants() {
	}

}
