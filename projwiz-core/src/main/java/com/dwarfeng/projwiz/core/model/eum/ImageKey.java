package com.dwarfeng.projwiz.core.model.eum;

import com.dwarfeng.dutil.basic.str.Name;
import com.dwarfeng.projwiz.core.util.Constants;

/**
 * 程序中的图片键。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public enum ImageKey implements Name {

	/** 新建工程的图片键 */
	NEW_PROJECT("new_project.png"),
	/** 打开工程的图片键 */
	OPEN_PROJECT("open_project.png"),
	/** 保存工程的图片键 */
	SAVE_PROJECT("save_project.png"),
	/** 导入的图片键 */
	INPORT("inport.png"),
	/** 导出的图片键 */
	EXPORT("export.png"),
	/** 新建文件的图片键 */
	NEW_FILE("new_file.png"),
	/** 复制文件的图片键 */
	COPY_FILE("copy_file.png"),
	/** 删除文件的图片键 */
	DELETE_FILE("delete_file.png"),
	/** 打开文件的图片键 */
	OPEN_FILE("open_file.png"),
	/** 输出流控制台的图片键 */
	OUT_CONSOLE("out_console.png"),
	/** 后台的图片键 */
	BACKGROUND("background.png"),
	/** 焦点的图片键 */
	FOCUS("focus.png"),
	/** 指示器的图片键 */
	INDICATOR("indicator.png"),
	/** 文件树 */
	FILETREE("file_tree.png"),
	/** 通知 */
	NOTICE("notice.png"),
	/** 工具 */
	TOOL("tool.png"),
	/** 信息 */
	INFOMATION("infomation.png"),
	/** 窗口最大化 */
	WINDOWS_MAXIMUM("windows_maximum.png"),
	/** 图标 */
	ICON("icon.png"),
	/** 工程与文件图标 */
	PROJECT_AND_FILE("project_and_file.png"),
	/** 编辑器图标 */
	EDITOR("editor.png"),
	/** 上一级 */
	UPTO("upto.png"),
	/** 主页 */
	FRONTPAGE("frontpage.png"),
	/** 组件 */
	MODULE("module.png"),;

	private String name;

	private ImageKey(String name) {
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return Constants.RESOURCE_IMAGE_ROOT_PATH + name;
	}

}
