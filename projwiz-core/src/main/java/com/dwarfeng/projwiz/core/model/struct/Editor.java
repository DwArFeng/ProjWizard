package com.dwarfeng.projwiz.core.model.struct;

import java.awt.Component;

import com.dwarfeng.dutil.basic.prog.ObverserSet;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.dutil.basic.threads.ExternalReadWriteThreadSafe;
import com.dwarfeng.projwiz.core.model.obv.EditorObverser;

/**
 * 文档的编辑器。
 * 
 * <p>
 * 编辑器主要用于编辑指定的文件，也可以对指定的工程做编辑。
 * 
 * <p>
 * 编辑器接口的定义需要严格遵守以下的规定
 * 
 * <blockquote> 1.
 * <p>
 * 2.
 * <p>
 * 3. TODO 完善Editor的doc说明</blockquote>
 * 
 * <p>
 * 该接口线程安全。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface Editor extends ExternalReadWriteThreadSafe, ObverserSet<EditorObverser> {

	/**
	 * 获取正在编辑的文件。
	 * 
	 * @return 正在编辑的文件。
	 */
	public File getEditFile();

	/**
	 * 获取该文档编辑器用于编辑窗口的视图支持。
	 * 
	 * @return 该文档编辑器的用于编辑窗口的视图支持。
	 */
	public Component getEditorView();

	/**
	 * 获取正在编辑的工程。
	 * 
	 * @return 正在编辑的工程。
	 */
	public Project getEditProject();

	/**
	 * 获取当前编辑器的标题。
	 * 
	 * @return 编辑器的标题。
	 */
	public String getTitle();

	/**
	 * 该编辑器是否支持保存动作。
	 * 
	 * @return 是否支持保存动作。
	 */
	public boolean isSaveSupported();

	/**
	 * 是否建议停止编辑。
	 * 
	 * 执行该方法时，编辑器可以根据情况决定是否建议退出编辑； 在有些情况下，如果不建议退出编辑，则程序不会主动停止编辑。
	 * （经典的情形时询问用户是否保存，如果用户点击取消，则返回 <code>false</code>
	 * ，即不建议退出编辑——在某些情况下，该编辑器则不会退出，而是允许用户继续编辑）。
	 * 
	 * @return 是否建议停止编辑。
	 */
	public boolean isStopSuggest();

	/**
	 * 保存（可选操作）。
	 * 
	 * @throws ProcessException
	 *             过程失败。
	 */
	public void save() throws ProcessException;

	/**
	 * 停止编辑。
	 * <p>
	 * 该方法需要编辑器立即停止编辑。
	 */
	public void stop();
}