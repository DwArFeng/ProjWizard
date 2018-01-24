package com.dwarfeng.projwiz.api;

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
import com.dwarfeng.projwiz.core.model.struct.PropSuppiler;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;

/**
 * 抽象文件处理器。
 * 
 * <p>
 * 文件处理器的抽象实现。
 * 
 * <p>
 * 虽然该抽象文件处理器没有无参数的公共构造器，但是其子类必须要有一个无参数的公共构造器。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public abstract class AbstractFileProcessor extends AbstractComponent implements FileProcessor {

	/**
	 * 新实例。
	 * 
	 * @param key
	 *            指定的键。
	 * @param toolkitRef
	 *            指定的工具包引用。
	 * @param metaDataStorage
	 *            指定的元数据仓库。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	public AbstractFileProcessor(String key, ReferenceModel<? extends Toolkit> toolkitRef,
			MetaDataStorage metaDataStorage) {
		super(key, toolkitRef, metaDataStorage);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canOpenFile(File file) {
		Objects.requireNonNull(file, "入口参数 file 不能为 null。");

		if (file.getRegisterKey().equals(getKey()))
			return true;
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
	public Image getIcon() {
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
	public boolean isNewFileSupported() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File newFile() throws ProcessException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PropSuppiler getPropSuppiler(File file) {
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
	public Editor newEditor(Project editProject, File editFile) throws ProcessException {
		throw new UnsupportedOperationException();
	}

}
