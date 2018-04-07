package com.dwarfeng.projwiz.raefrm;

import java.awt.Image;
import java.util.Objects;

import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.projwiz.core.model.eum.IconVariability;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.MetaDataStorage;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.ProjectProcessor;
import com.dwarfeng.projwiz.core.model.struct.PropUI;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;
import com.dwarfeng.projwiz.raefrm.model.eum.PermDemandKey;
import com.dwarfeng.projwiz.raefrm.model.eum.ProjCoreConfigEntry;
import com.dwarfeng.projwiz.raefrm.model.struct.ConstantsProvider;
import com.dwarfeng.projwiz.raefrm.model.struct.ProjProcToolkit;

/**
 * Rae框架工程处理器。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public abstract class RaeProjectProcessor extends RaeModule implements ProjectProcessor {

	/**
	 * 工程处理器工具包的内部实现。
	 * 
	 * @author DwArFeng
	 * @since 0.0.3-alpha
	 */
	protected final class ProjProcToolkitImpl extends ModuleToolkitImpl implements ProjProcToolkit {

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
		lock.readLock().lock();
		try {
			return coreConfigModel.getParsedValue(ProjCoreConfigEntry.RAE_PROCESSOR_SUPPORTED_NEW_PROJECT.getConfigKey(),
					Boolean.class);
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOpenProjectSupported() {
		lock.readLock().lock();
		try {
			return coreConfigModel.getParsedValue(ProjCoreConfigEntry.RAE_PROCESSOR_SUPPORTED_OPEN_PROJECT.getConfigKey(),
					Boolean.class);
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSaveProjectSupported() {
		lock.readLock().lock();
		try {
			return coreConfigModel.getParsedValue(ProjCoreConfigEntry.RAE_PROCESSOR_SUPPORTED_SAVE_PROJECT.getConfigKey(),
					Boolean.class);
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * 该方法首先检查 {@link #isNewProjectSupported()} , 确认该工程处理器是否允许新建工程，如果不允许，则直接抛出
	 * {@link UnsupportedOperationException}；否则，调用
	 * {@link #newProject_Sub()}，返回要求的结果。
	 */
	@Override
	public Project newProject() throws ProcessException {
		lock.writeLock().lock();
		try {
			if (!isNewProjectSupported()) {
				throw new UnsupportedOperationException("newProject");
			} else {
				requirePermKeyAvailable(PermDemandKey.RAE_PROCESSOR_NEWPROJECT);
				return newProject_Sub();
			}
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * 该方法首先检查 {@link #isOpenProjectSupported()} , 确认该工程处理器是否允许新建工程，如果不允许，则直接抛出
	 * {@link UnsupportedOperationException}；否则，调用
	 * {@link #openProject_Sub()}，返回要求的结果。
	 */
	@Override
	public Project openProject() throws ProcessException {
		lock.writeLock().lock();
		try {
			if (!isOpenProjectSupported()) {
				throw new UnsupportedOperationException("openProject");
			} else {
				requirePermKeyAvailable(PermDemandKey.RAE_PROCESSOR_OPENPROJECT);
				return openProject_Sub();
			}
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * 该方法首先检查 {@link #isSaveProjectSupported()} , 确认该工程处理器是否允许新建工程，如果不允许，则直接抛出
	 * {@link UnsupportedOperationException}；否则，调用
	 * {@link #saveProject_Sub()}，返回要求的结果。
	 */
	@Override
	public Project saveProject(Project project) throws ProcessException {
		Objects.requireNonNull(project, "入口参数 project 不能为 null。");

		lock.writeLock().lock();
		try {
			if (!isSaveProjectSupported()) {
				throw new UnsupportedOperationException("saveProject");
			} else {
				requirePermKeyAvailable(PermDemandKey.RAE_PROCESSOR_SAVEPROJECT);
				return saveProject_Sub();
			}
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * 新建工程的子方法。
	 * <p>
	 * 该方法被 {@link #newProject()} 调用。
	 * 
	 * @return 新建的工程。
	 * @throws ProcessException
	 *             过程异常。
	 * @throws UnsupportedOperationException
	 *             不支持该操作。
	 */
	protected abstract Project newProject_Sub() throws ProcessException, UnsupportedOperationException;

	/**
	 * 打开工程的子方法。
	 * <p>
	 * 该方法被 {@link #openProject()} 调用。
	 * 
	 * @return 打开的工程。
	 * @throws ProcessException
	 *             过程异常。
	 * @throws UnsupportedOperationException
	 *             不支持该操作。
	 */
	protected abstract Project openProject_Sub() throws ProcessException, UnsupportedOperationException;

	/**
	 * 保存工程的子方法。
	 * <p>
	 * 该方法被 {@link #saveProject(Project)} 调用。
	 * 
	 * @return 由指定的工程保存得到的新工程。
	 * @throws ProcessException
	 *             过程异常。
	 * @throws UnsupportedOperationException
	 *             不支持该操作。
	 */
	protected abstract Project saveProject_Sub() throws ProcessException, UnsupportedOperationException;

}
