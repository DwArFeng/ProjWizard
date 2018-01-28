package com.dwarfeng.projwiz.core.model.obv;

import com.dwarfeng.dutil.basic.cna.model.obv.MapObverser;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;

/**
 * 工具包权限模型观察器。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public interface ToolkitPermObverser extends MapObverser<Toolkit.Method, Integer> {

	/**
	 * 通知模型中的默认权限等级被改变。
	 * 
	 * @param oldValue
	 *            旧的默认权限等级。
	 * @param newValue
	 *            新的默认权限等级。
	 */
	public void fireDplChanged(Integer oldValue, Integer newValue);

}
