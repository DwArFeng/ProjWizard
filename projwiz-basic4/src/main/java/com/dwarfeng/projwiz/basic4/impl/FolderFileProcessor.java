package com.dwarfeng.projwiz.basic4.impl;

import java.awt.Image;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.dutil.basic.gui.awt.ImageUtil;
import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.projwiz.basic4.model.eum.FileType;
import com.dwarfeng.projwiz.basic4.model.eum.ImageKey;
import com.dwarfeng.projwiz.basic4.model.eum.PermDemandKey;
import com.dwarfeng.projwiz.basic4.model.struct.FofpConstantsProvider;
import com.dwarfeng.projwiz.basic4.view.NewFolderFileDialog;
import com.dwarfeng.projwiz.core.model.struct.Editor;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.MetaDataStorage;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.PropUI;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;
import com.dwarfeng.projwiz.core.view.eum.DialogOption;
import com.dwarfeng.projwiz.raefrm.RaeFileProcessor;

/**
 * 文件夹文件处理器。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public class FolderFileProcessor extends RaeFileProcessor {

	/**
	 * 静态构造方法。
	 * 
	 * @param toolkitRef
	 *            指定的工具包引用模型。
	 * @param metaDataStorage
	 *            指定的元数据仓库。
	 * @return 新的内存工程处理器。
	 * @throws ProcessException
	 *             过程异常。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public static FolderFileProcessor newInstance(ReferenceModel<Toolkit> toolkitRef, MetaDataStorage metaDataStorage)
			throws ProcessException {
		return new FolderFileProcessor(toolkitRef, metaDataStorage);
	}

	/**
	 * 新实例。
	 * 
	 * @param toolkitRef
	 *            指定的工具包引用。
	 * @param metaDataStorage
	 *            指定的元数据仓库。
	 * @throws ProcessException
	 *             过程异常。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	protected FolderFileProcessor(ReferenceModel<? extends Toolkit> toolkitRef, MetaDataStorage metaDataStorage)
			throws ProcessException {
		super(toolkitRef, metaDataStorage, new FofpConstantsProvider());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canOpenFile(File file) {
		Objects.requireNonNull(file, "入口参数 file 不能为 null。");

		lock.readLock().lock();
		try {
			return Objects.equals(file.getFileType().getName(), FileType.FOLDER.getName());
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Image getFileIcon(File file) {
		return ImageUtil.getInternalImage(ImageKey.FOFP_FILE_ICON);
	}

	@Override
	public PropUI getPropUI(File file) {
		// TODO Auto-generated method stub
		return super.getPropUI(file);
	}

	@Override
	public boolean isNewEditorSupported() {
		// TODO Auto-generated method stub
		return super.isNewEditorSupported();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNewFileSupported() {
		return true;
	}

	@Override
	public Editor newEditor(Project editProject, File editFile) throws ProcessException {
		// TODO Auto-generated method stub
		return super.newEditor(editProject, editFile);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File newFile() throws ProcessException {
		lock.writeLock().lock();
		try {
			requirePermKeyAvailable(PermDemandKey.FOFP_PROCESSOR_NEWFILE);

			AtomicReference<NewFolderFileDialog> panelRef = new AtomicReference<NewFolderFileDialog>(null);

			try {
				SwingUtil.invokeAndWaitInEventQueue(() -> {
					panelRef.set(NewFolderFileDialog.newInstance(this, labelI18nHandler));
				});
			} catch (InvocationTargetException | InterruptedException ignore) {
				// 不会抛出该异常。
			}

			NewFolderFileDialog dialog = panelRef.get();
			getToolkit().showExternalWindow(dialog);
			try {
				dialog.waitDispose();
			} catch (InterruptedException ignore) {
				// 中断也要按照基本法。
			}

			// 如果用户取消，则直接退出。
			if (panelRef.get().getOption() != DialogOption.OK_YES) {
				return null;
			}

			String name = dialog.getFileName() == null || dialog.getFileName().equals("")
					? MyUtil.label(this, MyLabelStringKey.LABEL_FOFP_1) : dialog.getFileName();
			String description = dialog.getFileDescription() == null ? "" : dialog.getFileDescription();

			Map<String, ByteBuffer> buffers = new HashMap<>();
			buffers.put(LABEL_DESCRIPTION, MyUtil.string2ByteBuffer(description));

			return new CreatedFile(getKey(), true, name, buffers);
		} finally {
			lock.writeLock().unlock();
		}
	}

}
