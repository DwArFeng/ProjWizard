package com.dwarfeng.projwiz.basic4.model.eum;

import com.dwarfeng.dutil.basic.str.DefaultName;
import com.dwarfeng.dutil.basic.str.Name;

/**
 * 文件类型枚举。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public enum FileType implements Name {
	/** 文件夹。 */
	FOLDER(new DefaultName("folder"));

	;

	private Name name;

	private FileType(Name name) {
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return name.getName();
	}

}
