package com.dwarfeng.projwiz.core.model.struct;

/**
 * 筛选器接口。
 * <p>
 * 筛选器接提供是否接受某个指定对象的方法。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface Filter<T> {

	/**
	 * 是否接受指定的对象。
	 * 
	 * @param t
	 *            指定的对象。
	 * @return 是否接受指定的对象。
	 */
	public boolean accept(T t);

}
