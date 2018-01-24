package com.dwarfeng.projwiz.core.util;

import java.awt.Image;
import java.util.Objects;

import com.dwarfeng.dutil.basic.gui.awt.Size2D;
import com.dwarfeng.dutil.basic.str.Name;

/**
 * 与图像有关的工具包。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class ImageUtil {

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
		return getInternalImage(name, null);
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
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");

		return com.dwarfeng.dutil.basic.gui.awt.ImageUtil.getInternalImage(name, Constants.IMAGE_LOAD_FAILED, size2D);
	}

	// 禁止外部实例化。
	private ImageUtil() {
	}

}
