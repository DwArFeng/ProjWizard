package com.dwarfeng.projwiz.raefrm.model.eum;

import com.dwarfeng.dutil.basic.str.Name;

/**
 * 权限需求键。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public enum PermDemandKey implements Name {
	RAE_PROCESSOR_NEWPROJECT("rae.processor.newproject"), //
	RAE_PROCESSOR_OPENPROJECT("rae.processor.openproject"), //
	RAE_PROCESSOR_SAVEPROJECT("rae.processor.saveproject"), //

	RAE_PROJECT_ADDFILE_BYCOPY("rae.project.addfile.bycopy"), //
	RAE_PROJECT_ADDFILE_BYMOVE("rae.project.addfile.bymove"), //
	RAE_PROJECT_ADDFILE_BYNEW("rae.project.addfile.bynew"), //
	RAE_PROJECT_ADDFILE_BYOTHER("rae.project.addfile.byother"), //
	RAE_PROJECT_REMOVEFILE_BYDELETE("rae.project.removefile.bydelete"), //
	RAE_PROJECT_REMOVEFILE_BYMOVE("rae.project.removefile.bymove"), //
	RAE_PROJECT_REMOVEFILE_BYOTHER("rae.project.removefile.byother"), //

	RAE_FILE_NEWLABEL("rae.file.newlabel"), //
	RAE_FILE_DISCARDLABEL("rae.file.discardlabel"), //
	RAE_FILE_OPENINPUTSTREAM("rae.file.openinputstream"), //
	RAE_FILE_OPENOUTPUTSTREAM("rae.file.openoutputstream"), //

	RAE_PROCESSOR_NEWEDITOR("rae.processor.neweditor"), //
	RAE_PROCESSOR_NEWFILE("rae.processor.newfile"), //
	;

	private String name;

	private PermDemandKey(String name) {
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name;
	}
}
