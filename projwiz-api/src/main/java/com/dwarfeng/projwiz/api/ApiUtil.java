package com.dwarfeng.projwiz.api;

import java.awt.Image;

import com.dwarfeng.dutil.basic.gui.awt.Size2D;
import com.dwarfeng.dutil.basic.str.Name;
import com.dwarfeng.projwiz.core.util.Constants;
import com.dwarfeng.projwiz.core.util.ImageUtil;

/**
 * API中的常用方法。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public final class ApiUtil {

	/** 读取失败显示的图像。 */
	public final static Image IMAGE_LOAD_FAILED = Constants.IMAGE_LOAD_FAILED;

	/**
	 * 获取包内图像。
	 * 
	 * @param name
	 *            指定的图像名称。
	 * @return 包内图像。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public static Image getInternalImage(Name name) {
		return ImageUtil.getInternalImage(name);
	}

	/**
	 * 获取包内图像。
	 * 
	 * @param name
	 *            指定的图像名称。
	 * @param size2D
	 *            图像的缩放尺寸。
	 * @return 包内图像。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public static Image getInternalImage(Name name, Size2D size2D) {
		return ImageUtil.getInternalImage(name, size2D);
	}

	// 禁止外部实例化。
	private ApiUtil() {
	}

}
