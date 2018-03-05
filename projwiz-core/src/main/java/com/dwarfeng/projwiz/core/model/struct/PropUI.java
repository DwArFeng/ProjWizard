package com.dwarfeng.projwiz.core.model.struct;

import java.awt.Component;

/**
 * 属性用户接口。
 * 
 * <p>
 * 和用户交流的属性接口，为用户提供文件或工程的属性的查询、修改等功能。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface PropUI {

	/**
	 * 返回该提供器提供的用于显示的组件。
	 * 
	 * @return 提供器提供的用于显示的组件。
	 */
	public Component getComponent();

	/**
	 * 应用时的调度。
	 */
	public void fireApplied();

	/**
	 * 确定时的调度。
	 */
	public void fireConfirmed();

	/**
	 * 取消时的调度。
	 */
	public void fireCanceled();

}
