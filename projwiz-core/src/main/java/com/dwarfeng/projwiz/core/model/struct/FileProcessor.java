package com.dwarfeng.projwiz.core.model.struct;

import java.awt.Image;

import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.projwiz.core.model.eum.IconVariability;

/**
 * 注册处理器。
 * 
 * <p>
 * 该接口线程安全。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface FileProcessor extends Module {

	/**
	 * 返回文件处理器是否能打开指定的文件。
	 * 
	 * @param file
	 *            指定的文件。
	 * @return 该文件处理器是否能打开指定的文件。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	public boolean canOpenFile(File file);

	/**
	 * 返回指定的文档对应的图标。
	 * 
	 * @param file
	 *            指定的文档。
	 * @return 指定的文档对应的图标。
	 * @throws IllegalArgumentException
	 *             注册信息无法处理指定的文件。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public Image getFileIcon(File file);

	/**
	 * 返回指定的文档对应的图标的可变性。
	 * <p>
	 * 良好的定义该方法可以更高效的获取图标。
	 * 
	 * @param file
	 *            指定的文档。
	 * @return 指定的文档对应的图标的可变性。
	 * @throws IllegalArgumentException
	 *             注册信息无法处理指定的文件。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public IconVariability getFileIconVariability(File file);

	/**
	 * 返回指定的文档对应的缩略图的可变性。
	 * <p>
	 * 良好的定义该方法可以更高效的获取缩略图。
	 * 
	 * @param file
	 *            指定的文档。
	 * @return 指定的文档对应的缩略图。
	 * @throws IllegalArgumentException
	 *             注册信息无法处理指定的文件。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public IconVariability getFileThumbVariability(File file);

	/**
	 * 获取文件处理器指定的文件对应的属性用户接口。
	 * 
	 * @param file
	 *            指定的文件。
	 * @return 指定的文件对应的属性用户接口。
	 */
	public PropUI getPropUI(File file);

	/**
	 * 返回指定的文档对应的缩略图。
	 * 
	 * @param file
	 *            指定的文档。
	 * @return 指定的文档对应的缩略图。
	 * @throws IllegalArgumentException
	 *             注册信息无法处理指定的文件。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public Image getThumb(File file);

	/**
	 * 该文件处理器是否支持生成新编辑器。
	 * 
	 * @return 是否支持。
	 */
	public boolean isNewEditorSupported();

	/**
	 * 返回是否支持新文件。
	 * 
	 * @return 是否支持新文件。
	 */
	public boolean isNewFileSupported();

	/**
	 * 生成一个新的编辑器（可选操作）。
	 * 
	 * <p>
	 * 编辑器侧重于编辑文件，但同时也允许编辑工程。
	 * 
	 * @param editProject
	 *            指定的编辑工程。
	 * @param editFile
	 *            指定的编辑文件。
	 * @return 新的编辑器。
	 * @throws ProcessException
	 *             过程异常。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public Editor newEditor(Project editProject, File editFile) throws ProcessException;

	/**
	 * 创建一个新文件（可选操作）。
	 * <p>
	 * 若未能成功创建文件，则返回 <code>null</code> ；若在创建时遇到异常，则抛出 {@link ProcessException}。
	 * 
	 * @return 新的文件。
	 * @throws ProcessException
	 *             过程异常。
	 * @throws UnsupportedOperationException
	 *             操作不可用。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public File newFile() throws ProcessException;

}
