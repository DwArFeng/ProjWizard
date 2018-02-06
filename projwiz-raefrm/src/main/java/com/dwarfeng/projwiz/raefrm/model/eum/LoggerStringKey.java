package com.dwarfeng.projwiz.raefrm.model.eum;

import com.dwarfeng.dutil.basic.str.DefaultName;
import com.dwarfeng.dutil.basic.str.Name;

/**
 * Rae框架下的记录器文本键。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public enum LoggerStringKey implements Name {

	/** 初始化所需要的文本字段。 */
	RAE_CMPOENT_INIT_0(new DefaultName("rae.cmpoent.init.0")),
	/** 初始化所需要的文本字段。 */
	RAE_CMPOENT_INIT_1(new DefaultName("rae.cmpoent.init.1")),
	/** 初始化所需要的文本字段。 */
	RAE_CMPOENT_INIT_2(new DefaultName("rae.cmpoent.init.2")),
	/** 初始化所需要的文本字段。 */
	RAE_CMPOENT_INIT_3(new DefaultName("rae.cmpoent.init.3")),
	/** 初始化所需要的文本字段。 */
	RAE_CMPOENT_INIT_4(new DefaultName("rae.cmpoent.init.4")),
	/** 初始化所需要的文本字段。 */
	RAE_CMPOENT_INIT_5(new DefaultName("rae.cmpoent.init.5")),
	/** 初始化所需要的文本字段。 */
	RAE_CMPOENT_INIT_6(new DefaultName("rae.cmpoent.init.6")),
	/** 初始化所需要的文本字段。 */
	RAE_CMPOENT_INIT_7(new DefaultName("rae.cmpoent.init.7")),
	/** 初始化所需要的文本字段。 */
	RAE_CMPOENT_INIT_8(new DefaultName("rae.cmpoent.init.8")),
	/** 初始化所需要的文本字段。 */
	RAE_CMPOENT_INIT_9(new DefaultName("rae.cmpoent.init.9")),
	/** 初始化所需要的文本字段。 */
	RAE_CMPOENT_INIT_10(new DefaultName("rae.cmpoent.init.10")),
	/** 初始化所需要的文本字段。 */
	RAE_CMPOENT_INIT_11(new DefaultName("rae.cmpoent.init.11")),
	/** 初始化所需要的文本字段。 */
	RAE_CMPOENT_INIT_12(new DefaultName("rae.cmpoent.init.12")),
	
	;

	private Name name;

	private LoggerStringKey(Name name) {
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
