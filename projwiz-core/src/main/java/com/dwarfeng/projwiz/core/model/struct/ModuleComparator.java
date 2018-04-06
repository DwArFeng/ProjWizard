package com.dwarfeng.projwiz.core.model.struct;

import java.util.Comparator;
import java.util.Objects;

/**
 * 工程处理器键值比较器。
 * 
 * TODO 以后需要实现更复杂的比较算法。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class ModuleComparator implements Comparator<Module> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compare(Module o1, Module o2) {
		Objects.requireNonNull(o1, "入口参数 o1 不能为 null。");
		Objects.requireNonNull(o2, "入口参数 o2 不能为 null。");

		return o1.getClass().getName().compareTo(o2.getClass().getName());
	}

}
