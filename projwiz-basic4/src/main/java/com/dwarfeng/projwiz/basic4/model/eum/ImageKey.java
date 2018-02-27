package com.dwarfeng.projwiz.basic4.model.eum;

import com.dwarfeng.dutil.basic.str.Name;
import com.dwarfeng.projwiz.basic4.util.Constants;

/**
 * 图片键。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public enum ImageKey implements Name {
	/** MEPP的工程图标。 */
	MEPP_PROJ_ICON(Constants.RESOURCE_MEPP_IMAGE_ROOT_PATH, "proj_icon.png"),

	;

	private final String rootPath;
	private final String name;

	private ImageKey(String rootPath, String name) {
		this.rootPath = rootPath;
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return new StringBuilder().append(rootPath).append(name).toString();
	}
}
