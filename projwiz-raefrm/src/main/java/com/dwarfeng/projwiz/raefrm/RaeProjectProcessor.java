package com.dwarfeng.projwiz.raefrm;

import java.awt.Image;

import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.projwiz.core.model.eum.IconVariability;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.MetaDataStorage;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.ProjectProcessor;
import com.dwarfeng.projwiz.core.model.struct.PropUI;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;
import com.dwarfeng.projwiz.raefrm.model.struct.ConstantsProvider;
import com.dwarfeng.projwiz.raefrm.model.struct.ProjProcToolkit;

/**
 * Rae框架工程处理器。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public abstract class RaeProjectProcessor extends RaeComponent implements ProjectProcessor {

	/**
	 * 工程处理器工具包的内部实现。
	 * 
	 * @author DwArFeng
	 * @since 0.0.3-alpha
	 */
	protected final class ProjProcToolkitImpl extends ComponentToolkitImpl implements ProjProcToolkit {

		/**
		 * 新实例。
		 */
		public ProjProcToolkitImpl() {
			super();
		}

	}

	/**
	 * 新实例。
	 * 
	 * @param key
	 *            指定的键值。
	 * @param toolkitRef
	 *            指定的工具包引用。
	 * @param metaDataStorage
	 *            指定的元数据仓库。
	 * @param constantsProvider
	 *            指定的常量提供器。
	 * @throws ProcessException
	 *             过程异常。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	protected RaeProjectProcessor(ReferenceModel<? extends Toolkit> toolkitRef, MetaDataStorage metaDataStorage,
			ConstantsProvider constantsProvider) throws ProcessException {
		super(toolkitRef, metaDataStorage, constantsProvider);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PropUI getFilePropUI(Project project, File file) {
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
	public PropUI getProjectPropUI(Project project) {
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
	public Project newProject() throws ProcessException {
		throw new UnsupportedOperationException("newProject");
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
	public Project openProject() throws ProcessException {
		throw new UnsupportedOperationException("openProject");
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
	public Project saveProject(Project project) throws ProcessException {
		throw new UnsupportedOperationException("saveProject");
	}

}
