package com.dwarfeng.projwiz.core.model.obv;

import com.dwarfeng.dutil.basic.prog.Obverser;

/**
 * 编辑器观察器。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface EditorObverser extends Obverser {

	/**
	 * 通知观察器编辑器进行保存。
	 */
	public void fireSaved();

	/**
	 * 通知观察器该编辑器退出编辑。
	 */
	public void fireStopped();

	/**
	 * 通知观察器建议保存标记改变。
	 * 
	 * @param newValue
	 *            新的值。
	 */
	public void fireStopSuggestChanged(boolean newValue);

	/**
	 * 通知观察器标题改变了。
	 * 
	 * @param oldTitle
	 *            旧的标题。
	 * @param newTitle
	 *            新的标题。
	 */
	public void fireTitleChanged(String oldTitle, String newTitle);

}
