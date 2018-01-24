package com.dwarfeng.projwiz.core.model.eum;

/**
 * 图标的可变性。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public enum IconVariability {

	/**
	 * 固定不变的。
	 * <p>
	 * 无论文档的中的内容是否变化，图标始终保持不变。
	 */
	FIX,

	/**
	 * 可变的。
	 * <p>
	 * 当图标对应的对象中的内容发生改动后，图标发生变化。
	 */
	VAR,

	/**
	 * 动态的。
	 * <p>
	 * 图标或图标一直发生变化，不管文档是否发生变化。
	 */
	DYN

}
