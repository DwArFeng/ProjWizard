package com.dwarfeng.projwiz.core.model.obv;

import com.dwarfeng.dutil.basic.prog.Obverser;
import com.dwarfeng.projwiz.core.model.cm.Tree;

/**
 * 树模型观察器。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface UnorderedTreeObverser<E> extends Obverser {

	/**
	 * 通知指定的元素被添加。
	 * 
	 * @param path
	 *            指定的元素所在的路径。
	 * @param parent
	 *            指定元素的父节点。
	 * @param element
	 *            指定的元素。
	 */
	public void fireAdded(Tree.Path<E> path, E parent, E element);

	/**
	 * 通知指定的元素被移除。
	 * 
	 * @param path
	 *            指定的元素躲在的路径。
	 * @param parent
	 *            指定的元素的父节点。
	 * @param element
	 *            指定的元素。
	 */
	public void fireRemoved(Tree.Path<E> path, E parent, E element);

	/**
	 * 通知树模型被清空。
	 */
	public void fireCleared();

	/**
	 * 通知树模型指定的根被添加。
	 * 
	 * @param root
	 *            指定的根。
	 */
	public void fireRootAdded(E root);

}
