package com.dwarfeng.projwiz.core.model.cm;

import com.dwarfeng.dutil.basic.cna.model.SetModel;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.Project;

/**
 * 文件选区处理器。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface FileSelectionHandler extends SetModel<File> {
	
	/**
	 * 获取焦点工程。
	 * 
	 * @return 前台工程。
	 */
	public Project getFocusProject();

	/**
	 * 设置焦点工程。
	 * 
	 * @param project
	 *            指定的工程。
	 * @return 是否改变了模型本身。
	 */
	public boolean setFocusProject(Project project);

}
