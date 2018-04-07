package com.dwarfeng.projwiz.raefrm;

import java.awt.Image;
import java.util.Objects;

import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.projwiz.core.model.eum.IconVariability;
import com.dwarfeng.projwiz.core.model.struct.Editor;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.FileProcessor;
import com.dwarfeng.projwiz.core.model.struct.MetaDataStorage;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.PropUI;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;
import com.dwarfeng.projwiz.raefrm.model.eum.FileCoreConfigEntry;
import com.dwarfeng.projwiz.raefrm.model.eum.PermDemandKey;
import com.dwarfeng.projwiz.raefrm.model.struct.ConstantsProvider;
import com.dwarfeng.projwiz.raefrm.model.struct.FileProcToolkit;

/**
 * Rae框架文件处理器。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public abstract class RaeFileProcessor extends RaeModule implements FileProcessor {

	/**
	 * 文件处理器工具包的内部实现。
	 * 
	 * @author DwArFeng
	 * @since 0.0.3-alpha
	 */
	protected final class ProjProcToolkitImpl extends ModuleToolkitImpl implements FileProcToolkit {

		/**
		 * 新实例。
		 */
		protected ProjProcToolkitImpl() {
			super();
		}

	}

	/**
	 * 新实例。
	 * 
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
	protected RaeFileProcessor(ReferenceModel<? extends Toolkit> toolkitRef, MetaDataStorage metaDataStorage,
			ConstantsProvider constantsProvider) throws ProcessException {
		super(toolkitRef, metaDataStorage, constantsProvider);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Image getFileIcon(File file) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IconVariability getFileIconVariability(File file) {
		return IconVariability.FIX;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IconVariability getFileThumbVariability(File file) {
		return IconVariability.FIX;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PropUI getPropUI(File file) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Image getThumb(File file) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canOpenFile(File file) {
		Objects.requireNonNull(file, "入口参数 file 不能为 null。");
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNewEditorSupported() {
		lock.readLock().lock();
		try {
			return coreConfigModel.getParsedValue(FileCoreConfigEntry.RAE_PROCESSOR_SUPPORTED_NEW_EDITOR.getConfigKey(),
					Boolean.class);
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNewFileSupported() {
		lock.readLock().lock();
		try {
			return coreConfigModel.getParsedValue(FileCoreConfigEntry.RAE_PROCESSOR_SUPPORTED_NEW_FILE.getConfigKey(),
					Boolean.class);
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * 该方法首先检查 {@link #isNewEditorSupported()} , 确认该工程处理器是否允许新建工程，如果不允许，则直接抛出
	 * {@link UnsupportedOperationException}；否则，调用
	 * {@link #newEditor_Sub(Project, File)}，返回要求的结果。
	 */
	@Override
	public Editor newEditor(Project editProject, File editFile) throws ProcessException {
		lock.writeLock().lock();
		try {
			if (!isNewEditorSupported()) {
				throw new UnsupportedOperationException("newEditor");
			} else {
				requirePermKeyAvailable(PermDemandKey.RAE_PROCESSOR_NEWEDITOR);
				return newEditor_Sub(editProject, editFile);
			}
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * 该方法首先检查 {@link #isNewFileSupported()} , 确认该工程处理器是否允许新建工程，如果不允许，则直接抛出
	 * {@link UnsupportedOperationException}；否则，调用
	 * {@link #newFile_Sub()}，返回要求的结果。
	 */
	@Override
	public File newFile() throws ProcessException {
		lock.writeLock().lock();
		try {
			if (!isNewFileSupported()) {
				throw new UnsupportedOperationException("newFile");
			} else {
				requirePermKeyAvailable(PermDemandKey.RAE_PROCESSOR_NEWFILE);
				return newFile_Sub();
			}
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * 生成新编辑器的子方法。
	 * <p>
	 * 该方法被 {@link #newFile()} 调用。
	 * 
	 * @param editProject
	 *            指定的编辑工程。
	 * @param editFile
	 *            指定的编辑文件。
	 * @return 新的编辑器。
	 * @throws ProcessException
	 *             过程异常。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected abstract Editor newEditor_Sub(Project editProject, File editFile)
			throws ProcessException, UnsupportedOperationException;

	/**
	 * 创建新文件的子方法。
	 * <p>
	 * 该方法被 {@link #newFile()} 调用。
	 * 
	 * @return 新的文件。
	 * @throws ProcessException
	 *             过程异常。
	 * @throws UnsupportedOperationException
	 *             操作不可用。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected abstract File newFile_Sub() throws ProcessException, UnsupportedOperationException;

}
