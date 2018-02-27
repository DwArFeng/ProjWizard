package com.dwarfeng.projwiz.raefrm.model.cm;

import java.util.Collection;

import com.dwarfeng.dutil.basic.cna.model.MapModel;
import com.dwarfeng.dutil.basic.str.Name;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;
import com.dwarfeng.projwiz.core.model.struct.Toolkit.Method;

/**
 * 权限需求模型。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public interface PermDemandModel extends MapModel<String, Collection<Method>> {

	/**
	 * 返回模型中的指定权限键是否需要指定的方法。
	 * 
	 * @param permKey
	 *            指定的权限键。
	 * @param method
	 *            指定的方法。
	 * @return 模型中指定的权限键是否需要指定的方法。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IllegalArgumentException
	 *             模型中不含有指定的权限键。
	 */
	public boolean isPermDemand(String permKey, Method method) throws NullPointerException, IllegalArgumentException;

	/**
	 * 返回模型中的指定权限键是否需要指定的方法。
	 * 
	 * @param permKey
	 *            含有指定的权限键的名称对象。
	 * @param method
	 *            指定的方法。
	 * @return 模型中指定的权限键是否需要指定的方法。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IllegalArgumentException
	 *             模型中不含有指定的权限键。
	 */
	public default boolean isPermDemand(Name permKey, Method method)
			throws NullPointerException, IllegalArgumentException {
		return isPermDemand(permKey.getName(), method);
	}

	/**
	 * 返回模型中的指定权限键是否不需要指定的方法。
	 * 
	 * @param permKey
	 *            指定的权限键。
	 * @param method
	 *            指定的方法。
	 * @return 模型中指定的权限键是否不需要指定的方法。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IllegalArgumentException
	 *             模型中不含有指定的权限键。
	 */
	public default boolean isNotPermDemand(String permKey, Method method)
			throws NullPointerException, IllegalArgumentException {
		return !isPermDemand(permKey, method);
	}

	/**
	 * 返回模型中的指定权限键是否不需要指定的方法。
	 * 
	 * @param permKey
	 *            含有指定的权限键的名称对象。
	 * @param method
	 *            指定的方法。
	 * @return 模型中指定的权限键是否不需要指定的方法。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IllegalArgumentException
	 *             模型中不含有指定的权限键。
	 */
	public default boolean isNotPermDemand(Name permKey, Method method)
			throws NullPointerException, IllegalArgumentException {
		return isNotPermDemand(permKey.getName(), method);
	}

	/**
	 * 返回指定的工具包是否能满足指定的权限键对应的所有需求。
	 * 
	 * @param permKey
	 *            指定的权限键。
	 * @param toolkit
	 *            指定的工具包。
	 * @return 指定的工具包是否能满足指定的权限键对应的所有需求。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IllegalArgumentException
	 *             该模型中不含有指定的权限键。
	 */
	public boolean isPermKeyAvailable(String permKey, Toolkit toolkit)
			throws NullPointerException, IllegalArgumentException;

	/**
	 * 返回指定的工具包是否能满足指定的权限键对应的所有需求。
	 * 
	 * @param permKey
	 *            含有指定的权限键的名称对象。
	 * @param toolkit
	 *            指定的工具包。
	 * @return 指定的工具包是否能满足指定的权限键对应的所有需求。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IllegalArgumentException
	 *             该模型中不含有指定的权限键。
	 */
	public default boolean isPermKeyAvailable(Name permKey, Toolkit toolkit)
			throws NullPointerException, IllegalArgumentException {
		return isPermKeyAvailable(permKey.getName(), toolkit);
	}

	/**
	 * 返回指定的工具包是否不满足指定的权限键对应的所有需求。
	 * 
	 * @param permKey
	 *            指定的权限键。
	 * @param toolkit
	 *            指定的工具包。
	 * @return 指定的工具包是否不满足指定的权限键对应的所有需求。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IllegalArgumentException
	 *             该模型中不含有指定的权限键。
	 */
	public default boolean isNotPermKeyAvaliable(String permKey, Toolkit toolkit)
			throws NullPointerException, IllegalArgumentException {
		return !isPermKeyAvailable(permKey, toolkit);
	}

	/**
	 * 返回指定的工具包是否不满足指定的权限键对应的所有需求。
	 * 
	 * @param permKey
	 *            含有指定的权限键的名称对象。
	 * @param toolkit
	 *            指定的工具包。
	 * @return 指定的工具包是否不满足指定的权限键对应的所有需求。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IllegalArgumentException
	 *             该模型中不含有指定的权限键。
	 */
	public default boolean isNotPermKeyAvaliable(Name permKey, Toolkit toolkit)
			throws NullPointerException, IllegalArgumentException {
		return !isNotPermKeyAvaliable(permKey.getName(), toolkit);
	}

	/**
	 * 要求指定的工具包是否能满足指定的权限键对应的所有需求，否则抛出异常。
	 * 
	 * @param permKey
	 *            指定的权限键。
	 * @param toolkit
	 *            指定的工具包。
	 * @throws IllegalStateException
	 *             指定的工具包不能满足指定的权健键对应的所有需求。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IllegalArgumentException
	 *             该模型中不含有指定的权限键。
	 */
	public void requirePermKeyAvailable(String permKey, Toolkit toolkit)
			throws IllegalStateException, NullPointerException, IllegalArgumentException;

	/**
	 * 要求指定的工具包是否能满足指定的权限键对应的所有需求，否则抛出异常。
	 * 
	 * @param permKey
	 *            指定的权限键。
	 * @param toolkit
	 *            指定的工具包。
	 * @throws IllegalStateException
	 *             指定的工具包不能满足指定的权健键对应的所有需求。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IllegalArgumentException
	 *             该模型中不含有指定的权限键。
	 */
	public default void requirePermKeyAvailable(Name permKey, Toolkit toolkit)
			throws IllegalStateException, NullPointerException, IllegalArgumentException {
		requirePermKeyAvailable(permKey.getName(), toolkit);
	}

}
