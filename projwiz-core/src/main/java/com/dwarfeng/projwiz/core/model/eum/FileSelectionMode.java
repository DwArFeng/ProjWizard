package com.dwarfeng.projwiz.core.model.eum;

/**
 * 文件选择模式。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public enum FileSelectionMode {

	/** 只允许选择目录。 */
	DIRECTORIES_ONLY(), //
	/** 可以选择文件和目录。 */
	FILES_AND_DIRECTORIES(), //
	/** 只允许选择文件。 */
	FILES_ONLY(),//

	;
}