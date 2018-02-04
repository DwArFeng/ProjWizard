package com.dwarfeng.projwiz.raefrm.model.obv;

import java.util.Collection;

import com.dwarfeng.projwiz.core.model.struct.Toolkit.Method;

/**
 * 权限需求观察器适配器。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public abstract class PermDemandAdapter implements PermDemandObverser {

	@Override
	public void firePut(String key, Collection<Method> value) {
	}

	@Override
	public void fireChanged(String key, Collection<Method> oldValue, Collection<Method> newValue) {
	}

	@Override
	public void fireRemoved(String key, Collection<Method> value) {
	}

	@Override
	public void fireCleared() {
	}

}
