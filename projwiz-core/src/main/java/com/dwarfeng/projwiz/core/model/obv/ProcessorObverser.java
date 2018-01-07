package com.dwarfeng.projwiz.core.model.obv;

import com.dwarfeng.dutil.basic.prog.Obverser;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;

/**
 * 处理器观察器。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface ProcessorObverser extends Obverser {

	/**
	 * 通知处理器中的观察器发生了改变。
	 * 
	 * @param oldValue
	 *            旧观察器。
	 * @param newValue
	 *            新观察器。
	 */
	public void fireToolkitChanged(Toolkit oldValue, Toolkit newValue);

}
