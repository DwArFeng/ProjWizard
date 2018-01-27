package com.dwarfeng.projwiz.core.model.struct;

import java.util.Comparator;
import java.util.Objects;

/**
 * 工程处理器键值比较器。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class ComponentKeyComparator implements Comparator<Component> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compare(Component o1, Component o2) {
		Objects.requireNonNull(o1, "入口参数 o1 不能为 null。");
		Objects.requireNonNull(o2, "入口参数 o2 不能为 null。");

		return o1.getKey().compareTo(o2.getKey());
	}

}