package com.dwarfeng.projwiz.core.model.obv;

import com.dwarfeng.projwiz.core.model.struct.Toolkit.Method;

/**
 * 工具包权限模型观察器适配器。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public abstract class ToolkitPermAdapter implements ToolkitPermObverser {

	@Override
	public void firePut(Method key, Integer value) {
	}

	@Override
	public void fireChanged(Method key, Integer oldValue, Integer newValue) {
	}

	@Override
	public void fireRemoved(Method key, Integer value) {
	}

	@Override
	public void fireCleared() {
	}

	@Override
	public void fireDplChanged(Integer oldValue, Integer newValue) {
	}

}
