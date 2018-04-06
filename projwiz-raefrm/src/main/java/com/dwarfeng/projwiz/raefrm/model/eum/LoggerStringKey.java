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
	RAE_MODULE_INIT_0(new DefaultName("rae.module.init.0")),
	/** 初始化所需要的文本字段。 */
	RAE_MODULE_INIT_1(new DefaultName("rae.module.init.1")),
	/** 初始化所需要的文本字段。 */
	RAE_MODULE_INIT_2(new DefaultName("rae.module.init.2")),
	/** 初始化所需要的文本字段。 */
	RAE_MODULE_INIT_3(new DefaultName("rae.module.init.3")),
	/** 初始化所需要的文本字段。 */
	RAE_MODULE_INIT_4(new DefaultName("rae.module.init.4")),
	/** 初始化所需要的文本字段。 */
	RAE_MODULE_INIT_5(new DefaultName("rae.module.init.5")),
	/** 初始化所需要的文本字段。 */
	RAE_MODULE_INIT_6(new DefaultName("rae.module.init.6")),
	/** 初始化所需要的文本字段。 */
	RAE_MODULE_INIT_7(new DefaultName("rae.module.init.7")),
	/** 初始化所需要的文本字段。 */
	RAE_MODULE_INIT_8(new DefaultName("rae.module.init.8")),
	/** 初始化所需要的文本字段。 */
	RAE_MODULE_INIT_9(new DefaultName("rae.module.init.9")),
	/** 初始化所需要的文本字段。 */
	RAE_MODULE_INIT_10(new DefaultName("rae.module.init.10")),
	/** 初始化所需要的文本字段。 */
	RAE_MODULE_INIT_11(new DefaultName("rae.module.init.11")),
	/** 初始化所需要的文本字段。 */
	RAE_MODULE_INIT_12(new DefaultName("rae.module.init.12")),
	/** 初始化所需要的文本字段。 */
	RAE_MODULE_INIT_13(new DefaultName("rae.module.init.13")),
	/** 初始化所需要的文本字段。 */
	RAE_MODULE_INIT_14(new DefaultName("rae.module.init.14")),
	/** 初始化所需要的文本字段。 */
	RAE_MODULE_INIT_15(new DefaultName("rae.module.init.15")),;

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
