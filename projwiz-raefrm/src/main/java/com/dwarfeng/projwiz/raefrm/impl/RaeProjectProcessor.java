package com.dwarfeng.projwiz.raefrm.impl;

import java.awt.Image;
import java.util.concurrent.locks.ReadWriteLock;

import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.projwiz.api.AbstractProjectProcessor;
import com.dwarfeng.projwiz.core.model.eum.IconVariability;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.MetaDataStorage;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.PropSuppiler;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;

/**
 * Rae框架工程处理器。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public abstract class RaeProjectProcessor extends AbstractProjectProcessor {

	protected RaeProjectProcessor(String key, ReferenceModel<? extends Toolkit> toolkitRef,
			MetaDataStorage metaDataStorage) {
		super(key, toolkitRef, metaDataStorage);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PropSuppiler getFilePropSuppiler(File file) {
		// TODO Auto-generated method stub
		return super.getFilePropSuppiler(file);
	}

	@Override
	public Image getIcon() {
		// TODO Auto-generated method stub
		return super.getIcon();
	}

	@Override
	public Image getProjectIcon(Project project) {
		// TODO Auto-generated method stub
		return super.getProjectIcon(project);
	}

	@Override
	public IconVariability getProjectIconFixType(Project project) {
		// TODO Auto-generated method stub
		return super.getProjectIconFixType(project);
	}

	@Override
	public PropSuppiler getProjectPropSuppiler(Project project) {
		// TODO Auto-generated method stub
		return super.getProjectPropSuppiler(project);
	}

	@Override
	public Image getProjectThumb(Project project) {
		// TODO Auto-generated method stub
		return super.getProjectThumb(project);
	}

	@Override
	public IconVariability getProjectThumbFixType(Project project) {
		// TODO Auto-generated method stub
		return super.getProjectThumbFixType(project);
	}

	@Override
	public boolean isNewProjectSupported() {
		// TODO Auto-generated method stub
		return super.isNewProjectSupported();
	}

	@Override
	public boolean isOpenProjectSupported() {
		// TODO Auto-generated method stub
		return super.isOpenProjectSupported();
	}

	@Override
	public boolean isSaveProjectSupported() {
		// TODO Auto-generated method stub
		return super.isSaveProjectSupported();
	}

	@Override
	public Project newProject() throws ProcessException {
		// TODO Auto-generated method stub
		return super.newProject();
	}

	@Override
	public Project openProject() throws ProcessException {
		// TODO Auto-generated method stub
		return super.openProject();
	}

	@Override
	public Project saveProject(Project project) throws ProcessException {
		// TODO Auto-generated method stub
		return super.saveProject(project);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
	}

	@Override
	public ReadWriteLock getLock() {
		// TODO Auto-generated method stub
		return super.getLock();
	}

	@Override
	public IconVariability getIconVarialibity() {
		// TODO Auto-generated method stub
		return super.getIconVarialibity();
	}

}
