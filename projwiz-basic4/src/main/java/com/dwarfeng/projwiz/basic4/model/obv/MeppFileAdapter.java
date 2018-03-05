package com.dwarfeng.projwiz.basic4.model.obv;

import com.dwarfeng.projwiz.core.model.obv.FileAdapter;

/**
 * 内存文件观察器适配器。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public abstract class MeppFileAdapter extends FileAdapter implements MeppFileObverser {

	@Override
	public void fireReadSupportedChanged(boolean newValue) {
	}

	@Override
	public void fireWriteSupportedChanged(boolean newValue) {
	}

}
