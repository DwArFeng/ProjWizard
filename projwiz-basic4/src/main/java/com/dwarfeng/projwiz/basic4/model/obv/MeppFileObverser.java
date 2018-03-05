package com.dwarfeng.projwiz.basic4.model.obv;

import com.dwarfeng.projwiz.core.model.obv.FileObverser;

/**
 * 内存文件观察器。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public interface MeppFileObverser extends FileObverser {

	/**
	 * 通知内存文件的可读性发生了改变。
	 * 
	 * @param newValue
	 *            新值。
	 */
	public void fireReadSupportedChanged(boolean newValue);

	/**
	 * 通知内存文件的可写性发生了改变。
	 * 
	 * @param newValue
	 *            新值。
	 */
	public void fireWriteSupportedChanged(boolean newValue);

}
