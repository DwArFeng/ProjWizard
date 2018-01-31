package com.dwarfeng.projwiz.core.view.struct;

import com.dwarfeng.dutil.develop.backgr.Task;
import com.dwarfeng.projwiz.core.model.struct.Editor;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.Project;

/**
 * GUI 管理器。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface GuiManager {

	/**
	 * 执行方式。
	 * 
	 * @author DwArFeng
	 * @since 0.0.1-alpha
	 */
	public enum ExecType {
		/** 并发执行 */
		CONCURRENT,
		/** 先进先出执行 */
		FIFO,
	}

	/**
	 * 添加指定的焦点文件。
	 * 
	 * @param file
	 *            指定的文件。
	 * @param type
	 *            以何种方式执行。
	 */
	public void addFocusFile(File file, ExecType type);

	/**
	 * 关闭所有工程。
	 * 
	 * @param type
	 *            以何种方式执行。
	 */
	public void closeAllProject(ExecType type);

	/**
	 * 删除文件。
	 * 
	 * @param type
	 *            以何种方式执行。
	 */
	public void deleteFocusFile(ExecType type);

	/**
	 * 强制关闭程序。
	 * 
	 * @param type
	 *            以何种方式执行。
	 */
	public void forceExit(ExecType type);

	/**
	 * 新建一个文件。
	 * 
	 * @param type
	 *            以何种方式执行。
	 */
	public void newFile(ExecType type);

	/**
	 * 新建一个工程。
	 * 
	 * @param type
	 *            以何种方式执行。
	 */
	public void newProject(ExecType type);

	/**
	 * 打开锚点文件。
	 * 
	 * @param type
	 *            以何种方式执行。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public void openAnchorFile(ExecType type);

	/**
	 * 打开焦点文件。
	 */
	public void openFocusFile(ExecType type);

	/**
	 * 打开工程。
	 * 
	 * @param type
	 *            以何种方式执行。
	 */
	public void openProject(ExecType type);

	/**
	 * 为指定的工程指定焦点编辑器。
	 * 
	 * @param project
	 *            指定的工程。
	 * @param editor
	 *            指定的编辑器。
	 * @param type
	 *            以何种方式执行。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public void putFocusEditor(Project project, Editor editor, ExecType type);

	/**
	 * 移除指定的焦点工程。
	 * 
	 * @param file
	 *            指定的工程。
	 * @param type
	 *            以何种方式执行。
	 */
	public void removeFocusFile(File file, ExecType type);

	/**
	 * 重命名锚点文件。
	 * 
	 * @param type
	 *            以何种方式执行。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public void renameAnchorFile(ExecType type);

	/**
	 * 尝试另存为焦点工程。
	 * 
	 * @param type
	 *            以何种方式执行。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public void saveAsFocusProject(ExecType type);

	/**
	 * 保存前台编辑器。
	 * 
	 * @param type
	 *            以何种方式执行。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public void saveFocusEditor(ExecType type);

	/**
	 * 保存工程。
	 * 
	 * @param type
	 *            以何种方式执行。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public void saveFocusProject(ExecType type);

	/**
	 * 设置锚点文件。
	 * 
	 * @param file
	 *            指定的文件。
	 * @param type
	 *            以何种方式执行。
	 */
	public void setAnchorFile(File file, ExecType type);

	/**
	 * 将前台工程设置为指定的工程。
	 * 
	 * @param project
	 *            指定的工程。
	 * @param type
	 *            以何种方式执行。
	 */
	public void setFocusProject(Project project, ExecType type);

	/**
	 * 显示锚点文件的文件属性对话框。
	 * 
	 * @param type
	 *            以何种方式执行。
	 */
	public void showAnchorFilePropertiesDialog(ExecType type);

	/**
	 * 编辑器监视器界面。
	 * 
	 * @param type
	 *            以何种方式执行。
	 */
	public void showEditorMonitor(ExecType type);

	/**
	 * 显示焦点工程的属性对话框。
	 * 
	 * @param type
	 *            以何种方式执行。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public void showFocusProjectPropertiesDialog(ExecType type);

	/**
	 * 显示工程与文件监视器界面。
	 * 
	 * @param type
	 *            以何种方式执行。
	 */
	public void showProjectAndFileMonitor(ExecType type);

	/**
	 * 向程序的后台提交一个任务。
	 * 
	 * @param task
	 *            指定的任务。
	 * @param type
	 *            以何种方式执行。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public void submitTask(Task task, ExecType type);

	/**
	 * 关闭指定的工程。
	 * 
	 * @param project
	 *            指定的工程。
	 * @param type
	 *            以何种方式执行。
	 */
	public void tryCloseCertainProject(Project project, ExecType type);

	/**
	 * 尝试关闭前端工程。
	 * 
	 * @param type
	 *            以何种方式执行。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public void tryCloseFocusProject(ExecType type);

	/**
	 * 尝试关闭程序。
	 * <p>
	 * 该方法会尝试逐步关闭正在编辑的文件和正在打开的工程，当其中任意一个工程不建议关闭时，即停止关闭程序的过程。
	 * 
	 * @param type
	 *            以何种方式执行。
	 */
	public void tryExit(ExecType type);

	/**
	 * 尝试关闭一个编辑器。
	 * 
	 * @param editor
	 *            尝试关闭的编辑器。
	 * @param type
	 *            以何种方式执行。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public void tryStopCertainEditor(Editor editor, ExecType type);

	/**
	 * 尝试关闭焦点编辑器。
	 * 
	 * @param type
	 *            以何种方式执行。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public void tryStopFocusEditor(ExecType type);

	/**
	 * 尝试关闭焦点工程下的所有编辑器。
	 * 
	 * @param type
	 *            以何种方式执行。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public void tryStopFocusProjectEditor(ExecType type);

	/**
	 * 执行超级机密设置。
	 * 
	 * @param type
	 *            以何种方式执行。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public void superSecretSettings(ExecType type);

}
