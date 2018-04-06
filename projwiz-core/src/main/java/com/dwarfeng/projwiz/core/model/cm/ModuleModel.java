package com.dwarfeng.projwiz.core.model.cm;

import java.util.Collection;

import com.dwarfeng.dutil.basic.cna.model.SetModel;
import com.dwarfeng.projwiz.core.model.struct.Module;

/**
 * 组件模型。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public interface ModuleModel extends SetModel<Module> {

	/**
	 * 获取指定类对应的组件。
	 * 
	 * <p>
	 * 当指定类不存在时，返回 <code>null</code>。
	 * 
	 * @param clazz
	 *            指定的类。
	 * @return 指定的类对应的组件。
	 */
	public <T extends Module> T get(Class<T> clazz);

	/**
	 * 获取属于指定类的所有组件。
	 * 
	 * <p>
	 * 当模型中没有组件属于指定的类时，返回空集。
	 * 
	 * @param clazz
	 *            指定的类。
	 * @return 属于指定类的所有组件组成的集合。
	 */
	public <T extends Module> Collection<T> getSubs(Class<T> clazz);

	/**
	 * 判断模型中是否含有类为指定类的组件。
	 * 
	 * @param clazz
	 *            指定的类。
	 * @return 模型中是否含有类为指定类的组件。
	 */
	public boolean containsClass(Class<?> clazz);

	/**
	 * 判断模型中是否含有所有类为指定类的组件。
	 * 
	 * @param c
	 *            所有指定的类组成的集合。
	 * @return 模型中是否含有所有类为指定的类的组件。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public boolean containsAllClass(Collection<Class<?>> c);

	/**
	 * 移除模型中指定的类对应的组件（可选操作）。
	 * 
	 * @param clazz
	 *            指定的类。
	 * @return 该操作是否改变了模型本身。
	 */
	public boolean removeClass(Class<?> clazz);

	/**
	 * 移除模型中所有指定的类对应的组件（可选操作）。
	 * 
	 * @param c
	 *            所有指定的类组成的集合。
	 * @return 该操作是否改变了模型本身。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public boolean removeAllClass(Collection<Class<?>> c);

	/**
	 * 保留模型中所有指定的类对应的组件，其余的移除（可选操作）。
	 * 
	 * @param c
	 *            所有指定的类组成的集合。
	 * @return 该操作是否改变了模型本身。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public boolean retainAllClass(Collection<Class<?>> c);

}
