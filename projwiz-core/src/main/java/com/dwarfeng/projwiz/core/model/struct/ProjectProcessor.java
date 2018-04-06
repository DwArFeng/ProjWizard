package com.dwarfeng.projwiz.core.model.struct;

import java.awt.Image;

import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.projwiz.core.model.eum.IconVariability;

/**
 * 工程处理器。
 * 
 * <p>
 * 该接口线程安全。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface ProjectProcessor extends Module {

	/**
	 * 获取文件处理器指定的文件对应的属性用户接口。
	 * 
	 * <p>
	 * 如果不需要指定的属性用户接口，返回 <code>null</code>即可。
	 * 
	 * @param project
	 *            指定文件所在的工程。
	 * @param file
	 *            指定的文件。
	 * @return 指定的文件对应的属性用户接口。
	 */
	public PropUI getFilePropUI(Project project, File file);

	/**
	 * 获取指定工程的图标。
	 * <p>
	 * 如果不需要指定的属性用户接口，返回 <code>null</code>即可。
	 * 
	 * @param project
	 *            指定的工程。
	 * @return 指定工程的图标。
	 */
	public Image getProjectIcon(Project project);

	/**
	 * 获取指定工程的图标的固定类型。
	 * 
	 * @param project
	 *            指定的工程。
	 * @return 指定的工程的图标的固定类型。
	 */
	public IconVariability getProjectIconFixType(Project project);

	/**
	 * 获取工程处理器指定的工程对应的属性用户接口。
	 * 
	 * @param project
	 *            指定的工程。
	 * @return 指定的文件对应的属性用户接口。
	 */
	public PropUI getProjectPropUI(Project project);

	/**
	 * 获取指定工程的缩略图。
	 * 
	 * @param project
	 *            指定的工程。
	 * @return 指定工程的缩略图。
	 */
	public Image getProjectThumb(Project project);

	/**
	 * 获取指定的工程的缩略图的固定类型。
	 * 
	 * @param project
	 * @return
	 */
	public IconVariability getProjectThumbFixType(Project project);

	/**
	 * 是否支持新建工程操作。
	 * 
	 * @return 是否支持。
	 */
	public boolean isNewProjectSupported();

	/**
	 * 是否支持打开工程操作。
	 * 
	 * @return 是否支持。
	 */
	public boolean isOpenProjectSupported();

	/**
	 * 是否支持保存工程操作。
	 * 
	 * @return 是否支持。
	 */
	public boolean isSaveProjectSupported();

	/**
	 * 新建一个工程（可选操作）。
	 * 
	 * @return 新建的工程。
	 * @throws ProcessException
	 *             过程异常。
	 * @throws UnsupportedOperationException
	 *             不支持该操作。
	 */
	public Project newProject() throws ProcessException;

	/**
	 * 打开一个工程（可选操作）。
	 * 
	 * @return 打开的工程。
	 * @throws ProcessException
	 *             过程异常。
	 * @throws UnsupportedOperationException
	 *             不支持该操作。
	 */
	public Project openProject() throws ProcessException;

	/**
	 * 保存指定的工程（可选操作）。
	 * <p>
	 * 请注意：该方法应该能够实现其它工程的“另存为”功能，任何一个工程的实现都应该能通过这个方法另存为此工程。
	 * 
	 * @param project
	 *            指定的工程。
	 * @return 由指定的工程保存得到的新工程。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws ProcessException
	 *             过程异常。
	 * @throws UnsupportedOperationException
	 *             不支持该操作。
	 */
	public Project saveProject(Project project) throws ProcessException;

}
