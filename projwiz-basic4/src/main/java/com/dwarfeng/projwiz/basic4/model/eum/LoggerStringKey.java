package com.dwarfeng.projwiz.basic4.model.eum;

import com.dwarfeng.dutil.basic.str.DefaultName;
import com.dwarfeng.dutil.basic.str.Name;

/**
 * 记录器文本键。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public enum LoggerStringKey implements Name {
	MEPP_PROCESSOR_NEWPROJECT_0(new DefaultName("processor.newproject.0")), //

	MEPP_PROJECT_ADDFILE_0(new DefaultName("project.addFile.0 ")), //
	MEPP_PROJECT_ADDFILE_1(new DefaultName("project.addFile.1 ")), //
	MEPP_PROJECT_ADDFILE_2(new DefaultName("project.addFile.2 ")), //
	MEPP_PROJECT_ADDFILE_3(new DefaultName("project.addFile.3 ")), //
	MEPP_PROJECT_ADDFILE_4(new DefaultName("project.addFile.4 ")), //

	MEPP_PROJECT_REMOVEFILE_0(new DefaultName("project.removeFile.0")), //
	MEPP_PROJECT_REMOVEFILE_1(new DefaultName("project.removeFile.1")), //
	MEPP_PROJECT_REMOVEFILE_2(new DefaultName("project.removeFile.2")), //
	MEPP_PROJECT_REMOVEFILE_3(new DefaultName("project.removeFile.3")), //
	MEPP_PROJECT_REMOVEFILE_4(new DefaultName("project.removeFile.4")), //
	MEPP_PROJECT_REMOVEFILE_5(new DefaultName("project.removeFile.5")),//

	;

	private Name name;

	private LoggerStringKey(Name name) {
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return name.getName();
	}

}
