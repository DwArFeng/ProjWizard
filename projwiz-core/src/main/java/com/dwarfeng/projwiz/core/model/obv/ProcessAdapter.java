package com.dwarfeng.projwiz.core.model.obv;

import com.dwarfeng.projwiz.core.model.struct.Toolkit;

/**
 * 处理器观察器适配器。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class ProcessAdapter implements ProcessorObverser {

	@Override
	public void fireToolkitChanged(Toolkit oldValue, Toolkit newValue) {
	}

}
