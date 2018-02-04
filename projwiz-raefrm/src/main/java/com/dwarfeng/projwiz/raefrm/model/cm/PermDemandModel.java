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
	 * <p>
	 * 如果模型不包含指定的权限键，则直接返回 <code>false</code>。
	 * 
	 * @param permKey
	 *            指定的权限键。
	 * @param method
	 *            指定的方法。
	 * @return 模型中指定的权限键是否需要指定的方法。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public boolean isPermDemand(String permKey, Method method);

	/**
	 * 返回模型中的指定权限键是否需要指定的方法。
	 * 
	 * <p>
	 * 如果模型不包含指定的权限键，则直接返回 <code>false</code>。
	 * 
	 * @param permKey
	 *            含有指定的权限键的名称对象。
	 * @param method
	 *            指定的方法。
	 * @return 模型中指定的权限键是否需要指定的方法。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public default boolean isPermDemand(Name permKey, Method method) {
		return isPermDemand(permKey.getName(), method);
	}

	/**
	 * 返回模型中的指定权限键是否不需要指定的方法。
	 * 
	 * <p>
	 * 如果模型不包含指定的权限键，则直接返回 <code>true</code>。
	 * 
	 * @param permKey
	 *            指定的权限键。
	 * @param method
	 *            指定的方法。
	 * @return 模型中指定的权限键是否不需要指定的方法。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public default boolean isNotPermDemand(String permKey, Method method) {
		return !isPermDemand(permKey, method);
	}

	/**
	 * 返回模型中的指定权限键是否不需要指定的方法。
	 * 
	 * <p>
	 * 如果模型不包含指定的权限键，则直接返回 <code>true</code>。
	 * 
	 * @param permKey
	 *            含有指定的权限键的名称对象。
	 * @param method
	 *            指定的方法。
	 * @return 模型中指定的权限键是否不需要指定的方法。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public default boolean isNotPermDemand(Name permKey, Method method) {
		return isNotPermDemand(permKey.getName(), method);
	}

	/**
	 * 返回指定的工具包是否能满足指定的权限键对应的所有需求。
	 * 
	 * <p>
	 * 如果 <code>toolkit</code> 为 <code>null</code>，直接返回 <code>false</code>;
	 * 否则，如果模型中不包含指定的 <code>permKey</code>，则返回 <code>true</code>。
	 * 
	 * @param permKey
	 *            指定的权限键。
	 * @param toolkit
	 *            指定的工具包。
	 * @return 指定的工具包是否能满足指定的权限键对应的所有需求。
	 */
	public boolean isPermKeyAvailable(String permKey, Toolkit toolkit);

	/**
	 * 返回指定的工具包是否能满足指定的权限键对应的所有需求。
	 * 
	 * <p>
	 * 如果 <code>toolkit</code> 为 <code>null</code>，直接返回 <code>false</code>;
	 * 否则，如果模型中不包含指定的 <code>permKey</code>，则返回 <code>true</code>。
	 * 
	 * @param permKey
	 *            含有指定的权限键的名称对象。
	 * @param toolkit
	 *            指定的工具包。
	 * @return 指定的工具包是否能满足指定的权限键对应的所有需求。
	 */
	public default boolean isPermKeyAvailable(Name permKey, Toolkit toolkit) {
		return isPermKeyAvailable(permKey.getName(), toolkit);
	}

	/**
	 * 返回指定的工具包是否不满足指定的权限键对应的所有需求。
	 * 
	 * <p>
	 * 如果 <code>toolkit</code> 为 <code>null</code>，直接返回 <code>true</code>;
	 * 否则，如果模型中不包含指定的 <code>permKey</code>，则返回 <code>false</code>。
	 * 
	 * @param permKey
	 *            指定的权限键。
	 * @param toolkit
	 *            指定的工具包。
	 * @return 指定的工具包是否不满足指定的权限键对应的所有需求。
	 */
	public default boolean isNotPermKeyAvaliable(String permKey, Toolkit toolkit) {
		return !isPermKeyAvailable(permKey, toolkit);
	}

	/**
	 * 返回指定的工具包是否不满足指定的权限键对应的所有需求。
	 * 
	 * <p>
	 * 如果 <code>toolkit</code> 为 <code>null</code>，直接返回 <code>true</code>;
	 * 否则，如果模型中不包含指定的 <code>permKey</code>，则返回 <code>false</code>。
	 * 
	 * @param permKey
	 *            含有指定的权限键的名称对象。
	 * @param toolkit
	 *            指定的工具包。
	 * @return 指定的工具包是否不满足指定的权限键对应的所有需求。
	 */
	public default boolean isNotPermKeyAvaliable(Name permKey, Toolkit toolkit) {
		return !isNotPermKeyAvaliable(permKey.getName(), toolkit);
	}

}
