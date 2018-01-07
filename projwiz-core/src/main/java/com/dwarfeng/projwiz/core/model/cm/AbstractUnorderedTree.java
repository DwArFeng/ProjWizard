package com.dwarfeng.projwiz.core.model.cm;

import java.util.Objects;
import java.util.Set;

/**
 * 抽象无序树。
 * <p>
 * 无序树接口的抽象实现。
 * <p>
 * 该类最大化的实现了无序树，在 <code>AbstractTree</code> 的基础上实现了 <code>equals</code> 和
 * <code>hashCode</code> 方法。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public abstract class AbstractUnorderedTree<E> extends AbstractTree<E> implements UnorderedTree<E> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		for (Object obj : this) {
			hash += obj == null ? 0 : obj.hashCode();
		}
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (Objects.isNull(obj))
			return false;
		if (!(obj instanceof UnorderedTree))
			return false;

		UnorderedTree<?> that = (UnorderedTree<?>) obj;

		if (!Objects.equals(this.getRoot(), that.getRoot()))
			return false;

		return equalsNode(this, that, this.getRoot());
	}

	private boolean equalsNode(UnorderedTree<?> tree1, UnorderedTree<?> tree2, Object element) {
		if (!tree1.contains(element) || !tree2.contains(element))
			return false;

		if (tree1.isLeaf(element) && tree2.isLeaf(element))
			return true;

		if (tree1.isLeaf(element) || tree2.isLeaf(element))
			return false;

		Set<?> childs1 = tree1.getChilds(element);
		Set<?> childs2 = tree2.getChilds(element);

		if (!Objects.equals(childs1, childs2))
			return false;

		for (Object child : childs1) {
			if (!equalsNode(tree1, tree2, child))
				return false;
		}
		return true;
	}

}
