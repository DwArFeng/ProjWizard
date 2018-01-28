package com.dwarfeng.projwiz.core.model.cm;

import com.dwarfeng.dutil.basic.cna.model.MapModel;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;

/**
 * 工具包权限模型。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public interface ToolkitPermModel extends MapModel<Toolkit.Method, Integer> {

	/**
	 * 获取默认的权限等级。
	 * 
	 * @return 默认的权限等级。
	 */
	public int getDpl();

	/**
	 * 设置默认权限等级为指定等级。
	 * 
	 * @param value
	 *            指定的等级。
	 * @return 该操作是否改变了模型。
	 */
	public boolean setDpl(int value);

	/**
	 * 检查指定的权限等级是否有权限执行指定的方法。
	 * 
	 * @param method
	 *            指定的方法。
	 * @param permLevel
	 *            指定的权限等级。
	 * @return 指定的权限等级是否有权限执行指定的方法。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public boolean hasPerm(Toolkit.Method method, int permLevel);

	/**
	 * 检查指定的方法的权限等级。
	 * 
	 * @param method
	 *            指定的方法。
	 * @return 指定的方法对应的权限等级。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public int getPermLevel(Toolkit.Method method);

}
