package com.dwarfeng.projwiz.core.model.cm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

/**
 * 抽象树。
 * <p>
 * 树接口的抽象实现。
 * <p>
 * 
 * 此类提供 Collection 接口的骨干实现，以最大限度地减少了实现此接口所需的工作。
 * <p>
 * 
 * 要实现一个不可修改的 collection，编程人员只需扩展此类，并提供 iterator 和 size 方法的实现。（iterator
 * 方法返回的迭代器必须实现 hasNext 和 next。）
 * <p>
 * 
 * 要实现可修改的 collection，编程人员必须另外重写此类的 add 方法（否则，会抛出
 * UnsupportedOperationException），iterator 方法返回的迭代器还必须另外实现其 remove 方法。
 * <p>
 * 
 * 按照 Collection 接口规范中的建议，编程人员通常应提供一个 void （无参数）和 Collection 构造方法。
 * <p>
 * 
 * 此类中每个非抽象方法的文档详细描述了其实现。如果要实现的 collection 允许更有效的实现，则可以重写这些方法中的每个方法。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public abstract class AbstractTree<E> implements Tree<E> {

	/**
	 * 简单父子对。
	 * <p>
	 * 父子对接口的简单实现。
	 * 
	 * @author DwArFeng
	 * @since 0.0.1-alpha
	 */
	public static class SimpleParentChildPair<E> implements ParentChildPair<E> {

		private final E parent;
		private final E child;

		/**
		 * 
		 * @param parent
		 * @param child
		 */
		public SimpleParentChildPair(E parent, E child) {
			this.parent = parent;
			this.child = child;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public E getParent() {
			return parent;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public E getChild() {
			return child;
		}

	}

	/**
	 * 简单路径。
	 * <p>
	 * 树的路径的简单的实现。
	 * 
	 * @author DwArFeng
	 * @since 0.0.1-alpha
	 */
	public static class SimplePath<E> implements Tree.Path<E> {

		private final List<E> delegate = new ArrayList<>();

		/**
		 * 生成一个空的简单路径。
		 */
		public SimplePath() {
		}

		/**
		 * 生成一个拥有指定元素的简单路径。
		 * 
		 * @param c
		 *            指定的元素组成的集合。
		 */
		public SimplePath(Collection<? extends E> c) {
			Objects.requireNonNull(c, "入口参数 c 不能为 null。");
			delegate.addAll(c);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Iterator<E> iterator() {
			return delegate.iterator();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int depth() {
			return delegate.size();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public E get(int index) {
			return delegate.get(index);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean contains(Object o) {
			return delegate.contains(o);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean containsAll(Collection<?> c) {
			Objects.requireNonNull(c, "入口参数 c 不能为 null。");
			return delegate.containsAll(c);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isEmpty() {
			return delegate.isEmpty();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ListIterator<E> listIterator() {
			return delegate.listIterator();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ListIterator<E> listIterator(int index) {
			return delegate.listIterator(index);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object[] toArray() {
			return delegate.toArray();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public <T> T[] toArray(T[] a) {
			return delegate.toArray(a);
		}

	}

	/**
	 * The maximum size of array to allocate. Some VMs reserve some header words
	 * in an array. Attempts to allocate larger arrays may result in
	 * OutOfMemoryError: Requested array size exceeds VM limit
	 */
	private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

	/**
	 * Reallocates the array being used within toArray when the iterator
	 * returned more elements than expected, and finishes filling it from the
	 * iterator.
	 *
	 * @param r
	 *            the array, replete with previously stored elements
	 * @param it
	 *            the in-progress iterator over this collection
	 * @return array containing the elements in the given array, plus any
	 *         further elements returned by the iterator, trimmed to size
	 */
	@SuppressWarnings("unchecked")
	private static <T> T[] finishToArray(T[] r, Iterator<?> it) {
		int i = r.length;
		while (it.hasNext()) {
			int cap = r.length;
			if (i == cap) {
				int newCap = cap + (cap >> 1) + 1;
				// overflow-conscious code
				if (newCap - MAX_ARRAY_SIZE > 0)
					newCap = hugeCapacity(cap + 1);
				r = Arrays.copyOf(r, newCap);
			}
			r[i++] = (T) it.next();
		}
		// trim if overallocated
		return (i == r.length) ? r : Arrays.copyOf(r, i);
	}

	private static int hugeCapacity(int minCapacity) {
		if (minCapacity < 0) // overflow
			throw new OutOfMemoryError("Required array size too large");
		return (minCapacity > MAX_ARRAY_SIZE) ? Integer.MAX_VALUE : MAX_ARRAY_SIZE;
	}

	/** 树的根节点 */
	protected E root;

	/**
	 * 唯一的构造方法。
	 */
	protected AbstractTree() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean add(E parent, E element) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean add(ParentChildPair<E> pair) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAll(E parent, Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAll(Collection<? extends Tree.ParentChildPair<E>> c) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addRoot(E root) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(Object o) {
		Iterator<E> it = iterator();
		if (o == null) {
			while (it.hasNext())
				if (it.next() == null)
					return true;
		} else {
			while (it.hasNext())
				if (o.equals(it.next()))
					return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object e : c)
			if (!contains(e))
				return false;
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getDepth(Object o) {
		if (!contains(o))
			return -1;

		int count = 0;

		while (!isRoot(o)) {
			count++;
			o = getParent(o);
		}

		return count;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Path<E> getPath(Object o) {
		if (!contains(o))
			return null;

		// 获得路径的反向栈。
		List<E> list = new LinkedList<>();

		// 由于已经确定了 o 存在于该树中，那么o一定属于类型e，此处转换类型安全。
		@SuppressWarnings("unchecked")
		E e = (E) o;

		list.add(0, e);
		while (!isRoot(e)) {
			e = getParent(e);
			list.add(0, e);
		}

		return new SimplePath<>(list);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E getRoot() {
		return root;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAncestor(Object anscetor, Object o) {
		if (!contains(anscetor))
			return false;
		if (!contains(o))
			return false;

		if (Objects.equals(anscetor, o))
			return true;

		// 由于 o 存在于该树之中，因此 o 一定属于类型 E，该转换类型安全。
		@SuppressWarnings("unchecked")
		E element = (E) o;

		while (!isRoot(element)) {
			element = getParent(element);
			if (Objects.equals(anscetor, element))
				return true;
		}

		return false;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLeaf(Object o) {
		if (!contains(o))
			return false;
		return getChilds(o).size() == 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRoot(Object o) {
		return Objects.equals(o, root);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		// Estimate size of array; be prepared to see more or fewer elements
		Object[] r = new Object[size()];
		Iterator<E> it = iterator();
		for (int i = 0; i < r.length; i++) {
			if (!it.hasNext()) // fewer elements than expected
				return Arrays.copyOf(r, i);
			r[i] = it.next();
		}
		return it.hasNext() ? finishToArray(r, it) : r;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) {
		// Estimate size of array; be prepared to see more or fewer elements
		int size = size();
		T[] r = a.length >= size ? a : (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
		Iterator<E> it = iterator();

		for (int i = 0; i < r.length; i++) {
			if (!it.hasNext()) { // fewer elements than expected
				if (a == r) {
					r[i] = null; // null-terminate
				} else if (a.length < i) {
					return Arrays.copyOf(r, i);
				} else {
					System.arraycopy(r, 0, a, 0, i);
					if (a.length > i) {
						a[i] = null;
					}
				}
				return a;
			}
			r[i] = (T) it.next();
		}
		// more elements than expected
		return it.hasNext() ? finishToArray(r, it) : r;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		Iterator<E> it = iterator();
		if (!it.hasNext())
			return "[]";

		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for (;;) {
			E e = it.next();
			sb.append(e == this ? "(this Collection)" : e);
			if (!it.hasNext())
				return sb.append(']').toString();
			sb.append(',').append(' ');
		}
	}

}
