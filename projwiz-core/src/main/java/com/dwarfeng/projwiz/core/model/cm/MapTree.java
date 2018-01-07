package com.dwarfeng.projwiz.core.model.cm;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

import com.dwarfeng.dutil.basic.cna.CollectionUtil;

/**
 * 映射树。
 * <p>
 * 通过映射实现的树。
 * <p>
 * 映射树要求树中的所有元素都不得重复，而且允许其中含有一个 <code>null</code> 元素，默认<code>null</code>元素是根。
 * <p>
 * 映射树的默认迭代器是无序的，且不保证层次顺序，这是为了考虑效率。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public final class MapTree<E> extends AbstractUnorderedTree<E> {

	protected final Map<E, Node<E>> map = new HashMap<>();

	/**
	 * 生成一个空树。
	 */
	public MapTree() {
		super();
	}

	/**
	 * 根据指定的树生成一个无序树。
	 * 
	 * @param tree
	 *            指定的树。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IllegalArgumentException
	 *             该树不符合无序树的要求（有重复元素等）。
	 */
	public MapTree(Tree<? extends E> tree) {
		putTree(tree);
	}

	final void putTree(Tree<? extends E> tree) {
		root = tree.getRoot();
		putNode(tree, root, null, tree.getChilds(root));
	}

	final void putNode(Tree<? extends E> tree, E element, Node<E> parent, Collection<? extends E> childs) {
		if (map.containsKey(element))
			throw new IllegalArgumentException("重复的元素：" + element);

		Node<E> thisNode = new Node<>(element, parent);

		if (Objects.nonNull(parent))
			parent.getChilds().add(thisNode);
		map.put(element, thisNode);

		if (tree.isLeaf(element))
			return;

		for (E child : childs) {
			putNode(tree, child, thisNode, tree.getChilds(child));
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return map.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E getParent(Object o) {
		if (!map.containsKey(o))
			return null;
		if (isRoot(o))
			return null;
		return map.get(o).getParent().getElement();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<E> getChilds(Object o) {
		if (!map.containsKey(o))
			return null;

		Set<E> collection = new HashSet<>();
		for (Node<E> childNode : map.get(o).getChilds()) {
			collection.add(childNode.getElement());
		}
		return collection;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<E> iterator() {
		return CollectionUtil.unmodifiableIterator(map.keySet().iterator());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Tree<E> subTree(Object root) {
		if (!contains(root))
			return null;

		if (Objects.equals(this.root, root))
			return this;

		// 如果树中包含指定的root，那么 root 一定属于类型E，该转换是安全的。
		@SuppressWarnings("unchecked")
		E dejavu = (E) root;
		return new SubTree(dejavu);
	}

	private class SubTree extends AbstractTree<E> {

		private final Node<E> parentNode;
		private int size = -1;
		private boolean emptyFlag = false;

		public SubTree(E root) {
			this.root = root;
			parentNode = map.get(root);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Collection<E> getChilds(Object o) {
			if (!contains(o))
				return null;
			return MapTree.this.getChilds(o);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public E getParent(Object o) {
			if (!contains(o))
				return null;
			if (isRoot(o))
				return null;
			return map.get(o).getParent().getElement();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Iterator<E> iterator() {
			return new SubIterator(MapTree.this.iterator());
		}

		private class SubIterator implements Iterator<E> {

			private final Iterator<E> iterator;
			private E next = null;

			public SubIterator(Iterator<E> iterator) {
				this.iterator = iterator;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public boolean hasNext() {
				while (iterator.hasNext()) {
					E element = iterator().next();
					if (SubTree.this.isAncestor(SubTree.this.root, element)) {
						next = element;
						return true;
					}
				}

				next = null;
				return false;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public E next() {
				if (Objects.nonNull(next)) {
					E dejavu = next;
					next = null;
					return dejavu;
				} else {
					while (iterator.hasNext()) {
						E element = iterator().next();
						if (SubTree.this.isAncestor(SubTree.this.root, element)) {
							return element;
						}
					}
					throw new NoSuchElementException();
				}
			}

		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int size() {
			if (emptyFlag)
				return 0;

			if (size >= 0)
				return size;

			int count = 0;
			for (E element : MapTree.this) {
				if (isAncestor(this.root, element))
					count++;
			}

			size = count;
			return size;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Tree<E> subTree(Object root) {
			if (Objects.equals(SubTree.this.root, root))
				return SubTree.this;
			if (!contains(root))
				return null;
			return MapTree.this.subTree(root);
		}

		/**
		 * 重写以提高执行效率。
		 * <p>
		 * {@inheritDoc}
		 */
		@Override
		public boolean contains(Object o) {
			if (emptyFlag)
				return false;
			if (!map.keySet().contains(o))
				return false;

			Path<E> path = MapTree.this.getPath(o);
			if (!path.contains(this.root))
				return false;

			return true;
		}

		/**
		 * 实现树的添加方法。
		 * <p>
		 * {@inheritDoc}
		 */
		@Override
		public boolean add(E parent, E element) {
			if (emptyFlag)
				return false;
			if (!contains(parent))
				return false;
			if (MapTree.this.add(parent, element)) {
				size++;
				return true;
			} else {
				return false;
			}
		}

		/**
		 * 实现树的添加方法。
		 * <p>
		 * {@inheritDoc}
		 */
		@Override
		public boolean add(Tree.ParentChildPair<E> pair) {
			return add(pair.getParent(), pair.getChild());
		}

		/**
		 * 实现该树的全部添加方法。
		 * <p>
		 * {@inheritDoc}
		 */
		@Override
		public boolean addAll(E parent, Collection<? extends E> c) {
			Objects.requireNonNull(c, "入口参数 c 不能为 null。");

			if (!contains(parent))
				return false;

			boolean aFlag = false;
			for (E element : c) {
				if (add(parent, element)) {
					aFlag = true;
				}
			}
			return aFlag;
		}

		/**
		 * 实现该树的全部添加方法。
		 * <p>
		 * {@inheritDoc}
		 */
		@Override
		public boolean addAll(Collection<? extends Tree.ParentChildPair<E>> c) {
			Objects.requireNonNull(c, "入口参数 c 不能为 null。");

			boolean aFlag = false;
			for (ParentChildPair<E> pair : c) {
				if (add(pair.getParent(), pair.getChild()))
					aFlag = true;
			}
			return aFlag;
		}

		/**
		 * 实现该树的设置根节点的方法。
		 * <p>
		 * {@inheritDoc}
		 */
		@Override
		public boolean addRoot(E root) {
			if (MapTree.this.contains(root))
				return false;
			if (!emptyFlag)
				return false;
			if (Objects.isNull(parentNode))
				return false;
			SubTree.this.root = root;
			parentNode.getChilds().add(new Node<>(root, parentNode));
			size = 1;
			emptyFlag = false;
			return true;
		}

		/**
		 * 实现该树的清除方法。
		 * <p>
		 * {@inheritDoc}
		 */
		@Override
		public void clear() {
			MapTree.this.remove(root);
			SubTree.this.root = null;
			emptyFlag = true;
			size = 0;
		}

		/**
		 * 实现该树的移除方法。
		 * <p>
		 * {@inheritDoc}
		 */
		@Override
		public boolean remove(Object o) {
			if (!contains(o))
				return false;
			size = -1;
			return MapTree.this.remove(o);
		}

		/**
		 * 实现该树的全部移除方法。
		 * <p>
		 * {@inheritDoc}
		 */
		@Override
		public boolean removeAll(Collection<?> c) {
			Objects.requireNonNull(c, "入口参数 c 不能为 null。");

			boolean aFlag = false;
			for (Object o : c) {
				if (remove(o)) {
					aFlag = true;
				}
			}

			return aFlag;
		}

	}

	/**
	 * 重写以提高执行效率。
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(Object o) {
		return map.keySet().contains(o);
	}

	/**
	 * 重写以提高执行效率。
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		return map.keySet().containsAll(c);
	}

	/**
	 * 实现该树的设置根节点的方法。
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public boolean addRoot(E element) {
		if (!isEmpty())
			return false;
		root = element;
		map.put(root, new Node<>(element, null));
		return true;
	}

	/**
	 * 实现该树的添加方法。
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public boolean add(E parent, E element) {
		if (!contains(parent))
			return false;
		if (contains(element))
			return false;

		Node<E> eNode = new Node<>(element, map.get(parent));
		map.put(element, eNode);

		Node<E> pNode = map.get(parent);
		pNode.getChilds().add(eNode);

		return true;
	}

	/**
	 * 实现该树的添加方法。
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public boolean add(ParentChildPair<E> pair) {
		return add(pair.getParent(), pair.getChild());
	}

	/**
	 * 实现该树的全部添加方法。
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAll(Collection<? extends ParentChildPair<E>> c) {
		Objects.requireNonNull(c, "入口参数 c 不能为 null。");

		boolean aFlag = false;
		for (ParentChildPair<E> pair : c) {
			if (add(pair.getParent(), pair.getChild()))
				aFlag = true;
		}
		return aFlag;
	}

	/**
	 * 实现该树的全部添加方法。
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAll(E parent, Collection<? extends E> c) {
		Objects.requireNonNull(c, "入口参数 c 不能为 null。");

		if (!contains(parent))
			return false;

		boolean aFlag = false;
		for (E element : c) {
			if (add(parent, element)) {
				aFlag = true;
			}
		}

		return aFlag;
	}

	/**
	 * 实现该树的清除方法。
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		map.clear();
		root = null;
	}

	/**
	 * 实现该树的移除方法。
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(Object o) {
		if (!contains(o))
			return false;

		// 针对指定对象是根元素的情况的优化
		if (isRoot(o)) {
			clear();
			return true;
		}

		// 由于该树中含有对象 o，对象o一定属于类型E，此处转换是安全的。
		@SuppressWarnings("unchecked")
		E element = (E) o;

		Node<E> cNode = map.get(element);
		Node<E> pNode = cNode.getParent();

		Set<E> childs = new HashSet<>();
		for (E toRemove : this) {
			if (isAncestor(element, toRemove)) {
				childs.add(toRemove);
			}
		}

		map.keySet().removeAll(childs);

		pNode.getChilds().remove(cNode);

		return true;
	}

	// private void removeNode(Node<E> node) {
	// //CT.trace( node.element"+"isRoot(node.element));
	// map.remove(node.getElement());
	// if(! isRoot(node.element)){
	// node.getParent().getChilds().remove(node);
	// }
	//
	// if (node.getChilds().isEmpty())
	// return;
	// for (Node<E> childNode : node.getChilds()) {
	// removeNode(childNode);
	// }
	// }

	/**
	 * 实现该树的全部移除方法。
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		Objects.requireNonNull(c, "入口参数 c 不能为 null。");

		boolean aFlag = false;
		for (Object o : c) {
			if (remove(o)) {
				aFlag = true;
			}
		}

		return aFlag;
	}

	private static class Node<E> {

		private E element = null;
		private final Set<Node<E>> childs = new HashSet<>();
		private Node<E> parent = null;

		public Node(E element, Node<E> parent) {
			this.element = element;
			this.parent = parent;
		}

		// public Node(E element, Node<E> parent, Collection<? extends Node<E>>
		// childs) {
		// this.element = element;
		// this.parent = parent;
		// this.childs.addAll(childs);
		// }

		public E getElement() {
			return element;
		}

		// public void setElement(E element) {
		// this.element = element;
		// }

		public Node<E> getParent() {
			return parent;
		}

		// public void setParent(Node<E> parent) {
		// this.parent = parent;
		// }

		public Set<Node<E>> getChilds() {
			return childs;
		}

	}

}
