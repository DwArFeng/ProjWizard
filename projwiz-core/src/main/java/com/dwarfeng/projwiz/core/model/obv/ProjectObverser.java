package com.dwarfeng.projwiz.core.model.obv;

import com.dwarfeng.dutil.basic.prog.Obverser;
import com.dwarfeng.projwiz.core.model.cm.Tree;
import com.dwarfeng.projwiz.core.model.struct.File;

/**
 * 工程观察器。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface ProjectObverser extends Obverser {

	/**
	 * 通知指定的文件以复制的方式被添加。
	 * 
	 * @param path
	 *            指定的文件所在的路径。
	 * @param parent
	 *            指定文件的父节点。
	 * @param file
	 *            指定的文件。
	 */
	public void fireFileAddedByCopy(Tree.Path<File> path, File parent, File file);

	/**
	 * 通知指定的文件以移动的方式被添加。
	 * 
	 * @param path
	 *            指定的文件所在的路径。
	 * @param parent
	 *            指定文件的父节点。
	 * @param file
	 *            指定的文件。
	 */
	public void fireFileAddedByMove(Tree.Path<File> path, File parent, File file);

	/**
	 * 通知指定的文件以新建的方式被添加。
	 * 
	 * @param path
	 *            指定的文件所在的路径。
	 * @param parent
	 *            指定文件的父节点。
	 * @param file
	 *            指定的文件。
	 */
	public void fireFileAddedByNew(Tree.Path<File> path, File parent, File file);

	/**
	 * 通知指定的文件以删除的方式被移除。
	 * 
	 * @param path
	 *            指定的文件所在的路径。
	 * @param parent
	 *            指定的文件的父节点。
	 * @param file
	 *            指定的文件。
	 */
	public void fireFileRemovedByDelete(Tree.Path<File> path, File parent, File file);

	/**
	 * 通知指定的文件以删除的方式被移除。
	 * 
	 * @param path
	 *            指定的文件所在的路径。
	 * @param parent
	 *            指定的文件的父节点。
	 * @param file
	 *            指定的文件。
	 */
	public void fireFileRemovedByMove(Tree.Path<File> path, File parent, File file);

	/**
	 * 通知指定的文件被重命名。
	 * 
	 * @param path
	 *            指定的文件所在的路径。
	 * @param file
	 *            指定的文件。
	 * @param oldName
	 *            指定文件的旧名字。
	 * @param newName
	 *            指定文件的新名字。
	 */
	public void fireFileRenamed(Tree.Path<File> path, File file, String oldName, String newName);

	/**
	 * 通知工程被保存。
	 */
	public void fireSaved();

	/**
	 * 通知观察器该工程通知运行。
	 */
	public void fireStopped();

}
