package com.dwarfeng.projwiz.core.model.cm;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * 无序树。
 * <p>
 * 无序树是指在一个根下的子节点是没有顺序的，以 {@link Set}的方式存储它们。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface UnorderedTree<E> extends Tree<E> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean add(E root, E element);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean add(ParentChildPair<E> pair);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAll(Collection<? extends ParentChildPair<E>> c);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAll(E parent, Collection<? extends E> c);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addRoot(E root);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(Object o);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsAll(Collection<?> c);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<E> getChilds(Object o);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getDepth(Object o);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E getParent(Object o);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Path<E> getPath(Object o);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E getRoot();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAncestor(Object anscetor, Object o);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLeaf(Object o);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRoot(Object o);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<E> iterator();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(Object o);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAll(Collection<?> c);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Tree<E> subTree(Object root);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T[] toArray(T[] a);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString();

}
