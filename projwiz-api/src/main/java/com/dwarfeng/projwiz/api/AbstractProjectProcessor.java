package com.dwarfeng.projwiz.api;

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

/**
 * 抽象工程处理器。
 * 
 * <p>
 * 工程处理器的抽象实现。
 * 
 * <p>
 * 虽然该抽象工程处理器没有无参数的公共构造器，但是其子类必须要有一个无参数的公共构造器。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public abstract class AbstractProjectProcessor extends AbstractComponent implements ProjectProcessor {

	/**
	 * 新实例。
	 * 
	 * @param key
	 *            指定的键值。
	 * @param toolkitRef
	 *            指定的工具包引用。
	 * @param metaDataStorage
	 *            指定的元数据仓库。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	public AbstractProjectProcessor(String key, ReferenceModel<? extends Toolkit> toolkitRef,
			MetaDataStorage metaDataStorage) {
		super(key, toolkitRef, metaDataStorage);
		// TODO Auto-generated constructor stub
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PropSuppiler getFilePropSuppiler(File file) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Image getIcon() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Image getProjectIcon(Project project) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IconVariability getProjectIconFixType(Project project) {
		return IconVariability.FIX;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PropSuppiler getProjectPropSuppiler(Project project) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Image getProjectThumb(Project project) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IconVariability getProjectThumbFixType(Project project) {
		return IconVariability.FIX;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNewProjectSupported() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOpenProjectSupported() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSaveProjectSupported() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Project newProject() throws ProcessException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Project openProject() throws ProcessException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Project saveProject(Project project) throws ProcessException {
		throw new UnsupportedOperationException();
	}

}
