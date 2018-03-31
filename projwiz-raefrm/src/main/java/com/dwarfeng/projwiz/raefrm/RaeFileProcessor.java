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
import com.dwarfeng.projwiz.raefrm.model.struct.ConstantsProvider;
import com.dwarfeng.projwiz.raefrm.model.struct.FileProcToolkit;

/**
 * Rae框架文件处理器。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public abstract class RaeFileProcessor extends RaeComponent implements FileProcessor {

	/**
	 * 文件处理器工具包的内部实现。
	 * 
	 * @author DwArFeng
	 * @since 0.0.3-alpha
	 */
	protected final class ProjProcToolkitImpl extends ComponentToolkitImpl implements FileProcToolkit {

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
	public boolean canOpenFile(File file) {
		Objects.requireNonNull(file, "入口参数 file 不能为 null。");

		return false;
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
	public boolean isNewEditorSupported() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNewFileSupported() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Editor newEditor(Project editProject, File editFile) throws ProcessException {
		throw new UnsupportedOperationException("newEditor");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File newFile() throws ProcessException {
		throw new UnsupportedOperationException("newFile");
	}

}
