package com.dwarfeng.projwiz.raefrm.model.eum;

import com.dwarfeng.dutil.basic.str.DefaultName;
import com.dwarfeng.dutil.basic.str.Name;

/**
 * * Rae框架下的标签文本键。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public enum LabelStringKey implements Name {

	/** 组件的名称。 */
	RAEFRM_CMPOENT_NAME(new DefaultName("raefrm.cmpoent.name")),
	/** 组件的描述。 */
	RAEFRM_CMPOENT_DESCRIPTION(new DefaultName("raefrm.cmpoent.description")),

	;

	private Name name;

	private LabelStringKey(Name name) {
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
