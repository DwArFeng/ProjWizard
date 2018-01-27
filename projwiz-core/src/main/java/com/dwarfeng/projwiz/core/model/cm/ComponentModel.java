package com.dwarfeng.projwiz.core.model.cm;

import com.dwarfeng.dutil.basic.cna.model.KeySetModel;
import com.dwarfeng.projwiz.core.model.struct.Component;

/**
 * 组件模型。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public interface ComponentModel extends KeySetModel<String, Component> {

	/**
	 * 以指定的类获取对应键的组件（可选操作）。
	 * 
	 * @param key
	 *            对应的键。
	 * @param clas
	 *            指定的类。
	 * @return 指定的类对应的组件
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws ClassCastException
	 *             类型转换异常。
	 */
	public <T extends Component> T get(String key, Class<T> clas) throws ClassCastException, NullPointerException;

	/**
	 * 获取模型中所有属于指定类的组件（可选操作）。
	 * 
	 * <p>
	 * 注意：获取的集合是不可编辑的。
	 * 
	 * @param clas
	 *            指定的类。
	 * @return 模型中所有属于指定类的组件。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public <T extends Component> KeySetModel<String, T> getAll(Class<T> clas) throws NullPointerException;

}
