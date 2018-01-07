package com.dwarfeng.projwiz.core.model.eum;

/**
 * 固定的类型。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public enum FixType {

	/**
	 * 固定不变的。
	 * <p>
	 * 无论文档的中的内容是否变化，缩略图或图标始终保持不变。
	 */
	FIX,

	/**
	 * 可变的。
	 * <p>
	 * 当文档中的内容发生改动后，缩略图发生变化。
	 */
	VAR,

	/**
	 * 动态的。
	 * <p>
	 * 缩略图或图标一直发生变化，不管文档是否发生变化。
	 */
	DYN

}
