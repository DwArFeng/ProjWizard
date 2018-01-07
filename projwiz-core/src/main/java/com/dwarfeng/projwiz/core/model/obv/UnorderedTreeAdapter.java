package com.dwarfeng.projwiz.core.model.obv;

import com.dwarfeng.projwiz.core.model.cm.Tree.Path;

/**
 * 无序树观察器适配器。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public abstract class UnorderedTreeAdapter<E> implements UnorderedTreeObverser<E> {

	@Override
	public void fireAdded(Path<E> path, E parent, E element) {
	}

	@Override
	public void fireRemoved(Path<E> path, E parent, E element) {
	}

	@Override
	public void fireCleared() {
	}

	@Override
	public void fireRootAdded(E root) {
	}

}
