package com.dwarfeng.projwiz.core.util;

import java.util.Objects;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.dwarfeng.projwiz.core.model.cm.Tree;
import com.dwarfeng.projwiz.core.model.cm.Tree.Path;
import com.dwarfeng.projwiz.core.model.struct.File;

/**
 * Swing中树的工具方法。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class SwingTreeUtil {

	/**
	 * 由指定的树生成Swing树节点。
	 * 
	 * <p>
	 * 指定的文件树要求必须拥有根元素——这也是工程中文件树的要求之一。
	 * 
	 * @param tree
	 *            指定的文件树。
	 * @return 由指定的文件树生成的 Swing树节点。
	 * @throws IllegalArgumentException
	 *             指定的文件树不合法。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	public static DefaultMutableTreeNode newTreeNodeFromTree(Tree<? extends File> tree) {
		Objects.requireNonNull(tree, "入口参数 tree 不能为 null。");

		if (Objects.isNull(tree.getRoot())) {
			throw new IllegalArgumentException("入口参数 tree 没有根元素。");
		}
		return genNode(tree, tree.getRoot());
	}

	private static DefaultMutableTreeNode genNode(Tree<? extends File> tree, File file) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode();
		node.setUserObject(file);

		for (File childFile : tree.getChilds(file)) {
			insertTreeNodeByOrder(node, genNode(tree, childFile));
		}

		return node;
	}

	/**
	 * 通过指定的Path查找Swing树节点中的TreePath。
	 * 
	 * @param path
	 *            指定的路径。
	 * @param rootNode
	 *            Swing树的根节点。
	 * @return Swing中的TreePath。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 * @throws IllegalArgumentException
	 *             Path与Swing树节点不匹配。
	 */
	public static TreePath findTreePath(Path<? extends File> path, DefaultMutableTreeNode rootNode) {
		Objects.requireNonNull(path, "入口参数 path 不能为 null。");
		Objects.requireNonNull(rootNode, "入口参数 rootNode 不能为 null。");

		Object[] nodes = new Object[path.depth()];

		if (!Objects.equals(rootNode.getUserObject(), path.get(0)))
			throw new IllegalArgumentException();

		nodes[0] = rootNode;
		if (path.depth() == 1)
			return new TreePath(nodes);

		bk: for (int i = 1; i < path.depth(); i++) {
			Object tar = path.get(i);
			for (int j = 0; j < rootNode.getChildCount(); j++) {
				if (Objects.equals(tar, ((DefaultMutableTreeNode) rootNode.getChildAt(j)).getUserObject())) {
					nodes[i] = rootNode.getChildAt(j);
					rootNode = (DefaultMutableTreeNode) rootNode.getChildAt(j);
					continue bk;
				}
			}
			throw new IllegalArgumentException();
		}

		return new TreePath(nodes);

	}

	/**
	 * 将指定的节点按照顺序插入到指定的父节点中。
	 * 
	 * <p>
	 * 该方法将用指定的比较器逐个比较指定的对象与列表中的对象，并将指定的对象插入到列表中<b>第一个</b>大于等于其的元素之前， 并返回插入的位置。
	 * <br>
	 * 如果指定的节点在之前已经按照顺序排列好，那么调用该方法之后，此节点依然遵循顺序，
	 * 事实上，该方法就是为此设计的——对一个没有排序的节点调用此方法是没有意义的。 <br>
	 * 节点中的对象元素必须是 {@link File}或其子类。
	 *
	 * @param parentNode
	 *            父节点。
	 * @param node
	 *            子节点。
	 * @return 子节点被插入的位置。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public static int insertTreeNodeByOrder(DefaultMutableTreeNode parentNode, DefaultMutableTreeNode node) {
		Objects.requireNonNull(parentNode, "入口参数 parentNode 不能为 null。");
		Objects.requireNonNull(node, "入口参数 node 不能为 null。");

		File file2Insert = (File) node.getUserObject();

		for (int i = 0; i < parentNode.getChildCount(); i++) {
			File fileCurr = (File) ((DefaultMutableTreeNode) parentNode.getChildAt(i)).getUserObject();
			if (ProjectFileUtil.defaultFileComparator().compare(file2Insert, fileCurr) <= 0) {
				parentNode.insert(node, i);
				return i;
			}
		}

		parentNode.add(node);
		return parentNode.getChildCount() - 1;
	}

	// 禁止外部实例化。
	private SwingTreeUtil() {
	}
}
