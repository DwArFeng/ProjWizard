package com.dwarfeng.projwiz.core.view.obv;

import com.dwarfeng.dutil.basic.prog.Obverser;

/**
 * 主界面可见性观察器。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface MainFrameVisibleObverser extends Obverser {

	/**
	 * 通知观察器北方面板的可见性发生了改变。
	 * 
	 * @param newValue
	 *            新值。
	 */
	public void fireNorthVisibleChanged(boolean newValue);

	/**
	 * 通知观察器南方面板的可见性发生了改变。
	 * 
	 * @param newValue
	 *            新值。
	 */
	public void fireSouthVisibleChanged(boolean newValue);

	/**
	 * 通知观察器东方面板的可见性发生了改变。
	 * 
	 * @param newValue
	 *            新值。
	 */
	public void fireEastVisibleChanged(boolean newValue);

	/**
	 * 通知观察器西方面板的可见性发生了改变。
	 * 
	 * @param newValue
	 *            新值。
	 */
	public void fireWestVisibleChanged(boolean newValue);

	/**
	 * 通知观察器主界面的最大化状态发生了改变。
	 * 
	 * @param newValue
	 *            新值。
	 */
	public void fireMaximumChanged(boolean newValue);

}
