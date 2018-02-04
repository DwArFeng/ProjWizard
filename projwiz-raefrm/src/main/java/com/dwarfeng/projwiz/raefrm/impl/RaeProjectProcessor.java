package com.dwarfeng.projwiz.raefrm.impl;

import java.awt.Image;

import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.projwiz.core.model.eum.IconVariability;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.MetaDataStorage;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.ProjectProcessor;
import com.dwarfeng.projwiz.core.model.struct.PropSuppiler;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;
import com.dwarfeng.projwiz.raefrm.RaeComponent;
import com.dwarfeng.projwiz.raefrm.model.struct.ConstantsProvider;

/**
 * Rae框架工程处理器。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public abstract class RaeProjectProcessor extends RaeComponent implements ProjectProcessor {

	protected RaeProjectProcessor(String key, ReferenceModel<? extends Toolkit> toolkitRef,
			MetaDataStorage metaDataStorage, ConstantsProvider constantsProvider) throws ProcessException {
		super(key, toolkitRef, metaDataStorage, constantsProvider);
	}

	@Override
	public PropSuppiler getFilePropSuppiler(File file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Image getProjectIcon(Project project) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IconVariability getProjectIconFixType(Project project) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PropSuppiler getProjectPropSuppiler(Project project) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Image getProjectThumb(Project project) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IconVariability getProjectThumbFixType(Project project) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isNewProjectSupported() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOpenProjectSupported() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveProjectSupported() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Project newProject() throws ProcessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Project openProject() throws ProcessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Project saveProject(Project project) throws ProcessException {
		// TODO Auto-generated method stub
		return null;
	}

}
