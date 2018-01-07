package com.dwarfeng.projwiz.core.model.struct;

import java.awt.Component;

/**
 * 属性提供器。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface PropSuppiler {

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
	 * 点击确定时的调度。
	 */
	public void fireConfirmed();

	/**
	 * 点击取消时的调度。
	 */
	public void fireCanceled();

}
