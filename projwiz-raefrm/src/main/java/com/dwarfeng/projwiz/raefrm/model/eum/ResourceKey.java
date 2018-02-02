package com.dwarfeng.projwiz.raefrm.model.eum;

import com.dwarfeng.dutil.basic.str.Name;
import com.dwarfeng.projwiz.core.util.Constants;

/**
 * 资源键。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public enum ResourceKey implements Name {

	/** 标签国际化文本默认配置。 */
	I18N_LABEL(Constants.RESOURCE_CLASSIFY_I18N, "label"),

	/** 记录器国际化文本默认配置。 */
	I18N_LOGGER(Constants.RESOURCE_CLASSIFY_I18N, "logger"),

	;

	private final String classify;
	private final String name;

	private ResourceKey(String classify, String name) {
		this.classify = classify;
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return new StringBuilder().append(classify).append(".").append(name).toString();
	}

}
