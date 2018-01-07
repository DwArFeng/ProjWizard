package com.dwarfeng.projwiz.core.model.eum;

/**
 * 工具包的权限分级。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public enum ToolkitLevel {

	/** 最低权限，不可以调用任何方法。 */
	NONE(0),
	/** 只读权限，可以获得各个属性，但是无权修改它们。 */
	READ_ONLY(1000),
	/** 有限的写权限，可以使用某些方法对模型或属性进行修改，但无权对模型直接修改。 */
	WRITE_LIMIT(2000),
	/** 完全权限，可以调用任意方法。 */
	FULL(3000),

	;

	private final int level;

	private ToolkitLevel(int level) {
		this.level = level;
	}

	/**
	 * 获取工具包的权限等级。
	 * 
	 * @return
	 */
	public int getLevelValue() {
		return level;
	}
}
