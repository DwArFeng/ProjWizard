package com.dwarfeng.projwiz.core.model.struct;

/**
 * 独立标签生成器。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface UniqueLabelGenerator {

	/**
	 * 获取下一个独立标签。
	 * 
	 * @return 下一个独立标签。
	 */
	public String nextUniqueLabel();

}
