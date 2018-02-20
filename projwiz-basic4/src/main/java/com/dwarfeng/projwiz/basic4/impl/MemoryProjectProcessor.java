package com.dwarfeng.projwiz.basic4.impl;

import java.awt.Image;

import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.projwiz.core.model.eum.IconVariability;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.MetaDataStorage;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.PropSuppiler;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;
import com.dwarfeng.projwiz.raefrm.ConstantsProvider;
import com.dwarfeng.projwiz.raefrm.RaeProjectProcessor;

/**
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public class MemoryProjectProcessor extends RaeProjectProcessor {

	/**
	 * 
	 * @param key
	 * @param toolkitRef
	 * @param metaDataStorage
	 * @return
	 * @throws ProcessException
	 */
	public static MemoryProjectProcessor newInstance(String key, ReferenceModel<Toolkit> toolkitRef,
			MetaDataStorage metaDataStorage) throws ProcessException {
		return new MemoryProjectProcessor(key, toolkitRef, metaDataStorage, constantsProvider);
	}

	/**
	 * 
	 * @param key
	 * @param toolkitRef
	 * @param metaDataStorage
	 * @param constantsProvider
	 * @throws ProcessException
	 */
	protected MemoryProjectProcessor(String key, ReferenceModel<? extends Toolkit> toolkitRef,
			MetaDataStorage metaDataStorage, ConstantsProvider constantsProvider) throws ProcessException {
		super(key, toolkitRef, metaDataStorage, constantsProvider);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PropSuppiler getFilePropSuppiler(File file) {
		// TODO Auto-generated method stub
		return super.getFilePropSuppiler(file);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Image getProjectIcon(Project project) {
		// TODO Auto-generated method stub
		return super.getProjectIcon(project);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IconVariability getProjectIconFixType(Project project) {
		// TODO Auto-generated method stub
		return super.getProjectIconFixType(project);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PropSuppiler getProjectPropSuppiler(Project project) {
		// TODO Auto-generated method stub
		return super.getProjectPropSuppiler(project);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNewProjectSupported() {
		// TODO Auto-generated method stub
		return super.isNewProjectSupported();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Project newProject() throws ProcessException {
		// TODO Auto-generated method stub
		return super.newProject();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOpenProjectSupported() {
		// TODO Auto-generated method stub
		return super.isOpenProjectSupported();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Project openProject() throws ProcessException {
		// TODO Auto-generated method stub
		return super.openProject();
	}

}
