package com.dwarfeng.projwiz.core.model.obv;

import java.io.File;

import com.dwarfeng.projwiz.core.model.struct.ProcessorConfigInfo;

/**
 * 处理器配置观察器。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public abstract class ProcessorConfigAdapter implements ProcessorConfigObverser {

	@Override
	public void fireAdded(ProcessorConfigInfo element) {
	}

	@Override
	public void fireRemoved(ProcessorConfigInfo element) {
	}

	@Override
	public void fireCleared() {
	}

	@Override
	public void fireDirectionChanged(File oldValue, File newValue) {
	}

}
