package com.dwarfeng.projwiz.core.model.cm;

import java.util.Collection;
import java.util.ListIterator;

/**
 * 树。
 * <p>
 * 数据结构中的 <code>Tree</code>。
 * <p>
 * 根据树的实现方式的不同，有的树允许其中含有 <code>null</code>元素，有的树不允许；有的树是只读的，有的树是可读写的。 <br>
 * 如果一个树是只读的，那么这个树的所有可选方法都会抛出 {@link UnsupportedOperationException}；如果一个树不允许含有
 * <code>null</code>元素，那么试图向其中添加 <code>null</code>元素则会抛出
 * {@link NullPointerException}。
 * <p>
 * 所有的树均要求其中的元素是不重复的。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface Tree<E> extends Iterable<E> {

	/**
	 * 树的父子对。
	 * 
	 * @author DwArFeng
	 * @since 0.0.1-alpha
	 */
	public interface ParentChildPair<E> {

		/**
		 * 获取父子对中的父亲。
		 * 
		 * @return 父子对中的父亲。
		 */
		public E getParent();

		/**
		 * 获取父子对中的儿子。
		 * 
		 * @return 父子对中的儿子。
		 */
		public E getChild();
	}

	/**
	 * 树的路径。
	 * <p>
	 * 树的路径为一组元素，代表一个树从根节点一直到指定的元素所经历的所有元素。
	 * 
	 * @author DwArFeng
	 * @since 0.0.1-alpha
	 */
	public interface Path<E> extends Iterable<E> {

		/**
		 * 返回该路径的深度。
		 * 
		 * @return 该路径的深度。
		 */
		public int depth();

		/**
		 * 获取该路径中指定位置的元素。
		 * 
		 * @param index
		 *            指定的位置。
		 * @return 该路径中指定位置的元素。
		 * @throws IndexOutOfBoundsException
		 *             序号越界。
		 */
		public E get(int index);

		/**
		 * 获取该路径中是否包含指定的元素。
		 * 
		 * @param o
		 *            指定的元素。
		 * @return 该路径中是否包含指定的元素。
		 */
		public boolean contains(Object o);

		/**
		 * 获取该路径中是否含有指定集合中的所有元素。
		 * 
		 * @param c
		 *            指定的集合。
		 * @return 该路径中是否含有指定集合中的所有元素。
		 * @throws NullPointerException
		 *             指定的集合为 <code>null</code>。
		 */
		public boolean containsAll(Collection<?> c);

		/**
		 * 返回该路径是否为空。
		 * 
		 * @return 该路径是否为空。
		 */
		public boolean isEmpty();

		/**
		 * 返回该路径的列表迭代器。
		 * 
		 * @return 该路径的列表迭代器。
		 */
		public ListIterator<E> listIterator();

		/**
		 * 返回该路径的列表迭代器，从指定位置开始。
		 * 
		 * @param index
		 *            指定的位置。
		 * @return 从指定的位置返回的列表迭代器。
		 * @throws IndexOutOfBoundsException
		 *             位置越界。
		 */
		public ListIterator<E> listIterator(int index);

		/**
		 * 按照适当顺序（从第一个到最后一个）以数组的形式返回该路径中的所有的元素。
		 * 
		 * @return 该路径中所有元素的数组形式。
		 */
		public Object[] toArray();

		/**
		 * 按照适当顺序（从第一个到最后一个）以数组的形式返回该路径中的所有的元素， 数组的类型是指定的数组的类型。
		 * 
		 * @param a
		 *            指定的数组。
		 * @return 以数组的形式返回该路径中的所有的元素， 数组的类型是指定的数组的类型。
		 */
		public <T> T[] toArray(T[] a);
	}

	/**
	 * 向该树中指定的父节点下添加指定的元素（可选操作）。
	 * 
	 * @param parent
	 *            指定的父节点。
	 * @param element
	 *            指定的元素。
	 * @return 该操作是否改变了该树。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws UnsupportedOperationException
	 *             不支持该方法。
	 */
	public boolean add(E parent, E element);

	/**
	 * 向该树中添加指定的元素，父节点和待添加元素在<code>pair</code>中指定（可选操作）。
	 * 
	 * @param pair
	 *            指定的父子对。
	 * @return 该操作是否改变了该树。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws UnsupportedOperationException
	 *             不支持该方法。
	 */
	public boolean add(ParentChildPair<E> pair);

	/**
	 * 向该元素中的指定父节点下添加指定的集合中的所有元素（可选操作）。
	 * 
	 * @param parent
	 *            指定的父节点。
	 * @param c
	 *            指定的元素集合。
	 * @return 该操作是否改变了该树。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws UnsupportedOperationException
	 *             不支持该方法。
	 */
	public boolean addAll(E parent, Collection<? extends E> c);

	/**
	 * 向该元素中添加指定的集合中的所有父子对（可选参数）。
	 * 
	 * @param c
	 *            指定集合。
	 * @return 指定的父子对。
	 */
	public boolean addAll(Collection<? extends ParentChildPair<E>> c);

	/**
	 * 向树中添加根节点。
	 * <p>
	 * 该方法在树是“空树（<code>isEmpty() == true</code>）”的时候，用于初始化根节点，
	 * 如果该树已经有根节点了，则该方法不进行任何操作。
	 * <p>
	 * 根据树的不同，该方法可能会有不同的表现。
	 * 
	 * @param root
	 *            指定的根元素。
	 * @return 该操作是否改变了树。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws UnsupportedOperationException
	 *             不支持此操作。
	 */
	public boolean addRoot(E root);

	/**
	 * 清除该树中的所有内容，并把根元素设置为默认值，具体值由树的具体实现而决定。
	 */
	public void clear();

	/**
	 * 判断该树中是否含有指定的对象。
	 * 
	 * @param o
	 *            指定的对象。
	 * @return 该树中是否含有指定的元素。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public boolean contains(Object o);

	/**
	 * 判断该树中是否含有指定集合中的所有元素。
	 * 
	 * @param c
	 *            指定的集合。
	 * @return 该树中是否含有指定集合中的所有元素。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public boolean containsAll(Collection<?> c);

	/**
	 * 返回该树与指定的对象是否相等。
	 * 
	 * @param obj
	 *            指定的对象。
	 * @return 该树与另外的对象是否相等。
	 */
	@Override
	public boolean equals(Object obj);

	/**
	 * 返回该树中以指定元素为根的子树的第一级子元素。
	 * <p>
	 * 如果树中不存在指定的根元素，则返回 <code>null</code>。
	 * 
	 * @param o
	 *            指定的根元素。
	 * @return 该树中以指定元素为根的子树的第一级子元素。
	 */
	public Collection<E> getChilds(Object o);

	/**
	 * 返回该树中指定元素的深度。
	 * <p>
	 * 如果树中不存在指定的元素，则返回 <code>-1</code>。
	 * 
	 * @param o
	 *            指定的元素。
	 * @return 指定元素的深度。
	 */
	public int getDepth(Object o);

	/**
	 * 返回树中指定元素的父亲。
	 * <p>
	 * 如果树中不存在指定的元素，则返回 <code>null</code>。
	 * 
	 * @param o
	 *            指定的元素。
	 * @return 指定元素的父亲。
	 */
	public E getParent(Object o);

	/**
	 * 返回该树中从根元素到指定元素的路径。
	 * <p>
	 * 如果树中不存在指定的元素，则返回 <code>null</code>。
	 * 
	 * @param o
	 *            指定的元素。
	 * @return 该树中从根元素到指定元素的路径。
	 */
	public Path<E> getPath(Object o);

	/**
	 * 获取树中的根元素。
	 * 
	 * @return 树中的根元素。
	 */
	public E getRoot();

	/**
	 * 返回该树的哈希码值。
	 * 
	 * @return 该树的哈希码值。
	 */
	@Override
	public int hashCode();

	/**
	 * 返回在树中，指定的对象 <code>anscetor</code> 是否是指定对象 <code>o</code> 的祖先或自身。
	 * <p>
	 * 如果两个对象有任何一个对象不在树中，则返回 <code>false</code>。
	 * 
	 * @param anscetor
	 *            指定的祖先对象。
	 * @param o
	 *            指定的对象。
	 * @return 在树中，指定的对象 <code>anscetor</code> 是否是指定对象 <code>o</code> 的祖先。
	 */
	public boolean isAncestor(Object anscetor, Object o);

	/**
	 * 返回该树是否为空树。
	 * 
	 * @return 该树是否为空树。
	 */
	public boolean isEmpty();

	/**
	 * 返回一个对象是否是该树的叶节点。
	 * <p>
	 * 如果树中不存在该对象的话，则返回 <code>false</code>。
	 * 
	 * @param o
	 *            指定的对象。
	 * @return 该对象是否是该树的叶节点。
	 */
	public boolean isLeaf(Object o);

	/**
	 * 返回一个对象是否是该树的根节点。
	 * <p>
	 * 如果树中不存在该对象的话，则返回 <code>false</code>。
	 * 
	 * @param o
	 *            指定的对象。
	 * @return 该对象是否是该树的根节点。
	 */
	public boolean isRoot(Object o);

	/**
	 * 从该树中移除指定的元素（可选操作）。
	 * <p>
	 * 如果指定的元素不是叶元素的话，则移除该元素以及其所有子孙元素。
	 * 
	 * <p>
	 * 在仅调用该方法的情况下，树中的根节点是永远不会移除的，如果要移除根节点，则等于清除整个树，应该调用 {@link #clear()} 方法。
	 * 
	 * @param o
	 *            指定的元素。
	 * @return 该操作是否改变了该树。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws UnsupportedOperationException
	 *             不支持该方法。
	 */
	public boolean remove(Object o);

	/**
	 * 从该树中移除指定的集合中的所有元素（可选操作）。
	 * <p>
	 * 如果指定的元素不是叶元素的话，则移除该元素以及其所有子孙元素。
	 * <p>
	 * 在仅调用该方法的情况下，树中的根节点是永远不会移除的，如果要移除根节点，则等于清除整个树，应该调用 {@link #clear()} 方法。
	 * 
	 * @param c
	 *            指定的集合。
	 * @return 该操作是否改变了该树。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>
	 * @throws UnsupportedOperationException
	 *             不支持该方法。
	 */
	public boolean removeAll(Collection<?> c);

	/**
	 * 返回该树的长度。
	 * 
	 * @return 该树的长度。
	 */
	public int size();

	/**
	 * 返回该树中以指定元素为根的子树。
	 * <p>
	 * 如果树中不存在指定的根，则返回 <code>null</code>。
	 * <p>
	 * 该子树由此树支持，因此该子树的任何非结构性编辑都将反应在此树中，反之亦然。返回的子树支持该树的所有可选操作。
	 * 
	 * @param root
	 *            指定的根。
	 * @return 该树中以指定元素为根的子树。
	 */
	public Tree<E> subTree(Object root);

	/**
	 * 返回该树中的所有元素。
	 * 
	 * @return 该树中的所有元素。
	 */
	public Object[] toArray();

	/**
	 * 返回该树中所有的元素。
	 * <p>
	 * 返回一个包含此 set 中所有元素的数组；返回数组的运行时类型是指定数组的类型。
	 * <p>
	 * 如果指定的数组能容纳该 set，则它将在其中返回。否则，将分配一个具有指定数组的运行时类型和此 set 大小的新数组。 如果指定的数组能容纳此
	 * set，并有剩余的空间（即该数组的元素比此 set 多），那么会将列表中紧接该 set 尾部的元素设置为 null。（只有在调用者知道此 set
	 * 不包含任何 null 元素时才能用此方法确定此 set 的长度）。
	 * 
	 * @param a
	 *            指定的数组。
	 * @return 该树中的所有元素。
	 */
	public <T> T[] toArray(T[] a);

	/**
	 * 返回该树的文本形式。
	 * 
	 * @return 该树的文本形式。
	 */
	@Override
	public String toString();

}
