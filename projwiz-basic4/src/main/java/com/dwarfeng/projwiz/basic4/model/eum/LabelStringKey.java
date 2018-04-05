package com.dwarfeng.projwiz.basic4.model.eum;

import com.dwarfeng.dutil.basic.str.DefaultName;
import com.dwarfeng.dutil.basic.str.Name;

/**
 * 标签文本键。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public enum LabelStringKey implements Name {

	MEPP_PROCESSOR_NEWPROJECT_0(new DefaultName("processor.newproject.0")), //
	MEPP_PROCESSOR_NEWPROJECT_1(new DefaultName("processor.newproject.1")), //
	MEPP_PROCESSOR_NEWPROJECT_2(new DefaultName("processor.newproject.2")), //
	MEPP_PROCESSOR_NEWPROJECT_3(new DefaultName("processor.newproject.3")), //
	MEPP_PROCESSOR_NEWPROJECT_4(new DefaultName("processor.newproject.4")), //
	MEPP_PROCESSOR_NEWPROJECT_5(new DefaultName("processor.newproject.5")), //

	MEPP_PROJECT_ADDFILE_0(new DefaultName("project.addfile.0")), //
	MEPP_PROJECT_ADDFILE_1(new DefaultName("project.addfile.1")), //

	MEPP_PROJECT_RENAMEFILE_0(new DefaultName("project.renamefile.0")), //
	MEPP_PROJECT_RENAMEFILE_1(new DefaultName("project.renamefile.1")), //

	MEPP_PROJECT_PROPUI_0(new DefaultName("project.propui.0")), //

	FOFP_PROCESSOR_NEWFILE_0(new DefaultName("fofp.processor.newfile.0")), //
	FOFP_PROCESSOR_NEWFILE_1(new DefaultName("fofp.processor.newfile.1")), //
	FOFP_PROCESSOR_NEWFILE_2(new DefaultName("fofp.processor.newfile.2")), //
	FOFP_PROCESSOR_NEWFILE_3(new DefaultName("fofp.processor.newfile.3")), //
	FOFP_PROCESSOR_NEWFILE_4(new DefaultName("fofp.processor.newfile.4")), //

	;

	private Name name;

	private LabelStringKey(Name name) {
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
