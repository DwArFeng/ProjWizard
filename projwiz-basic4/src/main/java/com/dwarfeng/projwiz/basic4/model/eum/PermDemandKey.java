package com.dwarfeng.projwiz.basic4.model.eum;

import com.dwarfeng.dutil.basic.str.Name;

/**
 * 权限需求键。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public enum PermDemandKey implements Name {
	MEPP_PROCESSOR_NEWPROJECT("processor.newproject");

	;

	private String name;

	private PermDemandKey(String name) {
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name;
	}
}
