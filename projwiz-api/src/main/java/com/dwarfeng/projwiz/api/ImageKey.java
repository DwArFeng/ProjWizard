package com.dwarfeng.projwiz.api;

import com.dwarfeng.dutil.basic.str.Name;

/**
 * 程序中的图片键。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public enum ImageKey implements Name {

	/** 组件 */
	COMPONENT("component.png"),

	;

	private String name;

	private ImageKey(String name) {
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return Constants.RESOURCE_IMAGE_ROOT_PATH + name;
	}

}
