package com.dwarfeng.projwiz.basic4.impl;

import java.awt.Image;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.dwarfeng.dutil.basic.cna.model.DefaultReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.dutil.basic.gui.awt.ImageUtil;
import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.projwiz.basic4.model.eum.FileType;
import com.dwarfeng.projwiz.basic4.model.eum.FofpConfigEntry;
import com.dwarfeng.projwiz.basic4.model.eum.ImageKey;
import com.dwarfeng.projwiz.basic4.model.struct.FofpConstantsProvider;
import com.dwarfeng.projwiz.basic4.view.FofpNewFileDialog;
import com.dwarfeng.projwiz.core.model.struct.Editor;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.MetaDataStorage;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.PropUI;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;
import com.dwarfeng.projwiz.core.view.eum.DialogOption;
import com.dwarfeng.projwiz.raefrm.RaeCreatedFile;
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
	protected Editor newEditor_Sub(Project editProject, File editFile)
			throws ProcessException, UnsupportedOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected File newFile_Sub() throws ProcessException, UnsupportedOperationException {
		ReferenceModel<FofpNewFileDialog> dialogRef = new DefaultReferenceModel<>();
		String description = null;

		try {
			SwingUtil.invokeAndWaitInEventQueue(() -> {
				FofpNewFileDialog dialog = new FofpNewFileDialog(labelI18nHandler);
				dialogRef.set(dialog);
			});
		} catch (InvocationTargetException | InterruptedException ignore) {
			// 抛异常也要按照基本法。
		}

		getToolkit().showExternalWindow(dialogRef.get());

		// 如果用户取消，则直接退出。
		if (dialogRef.get().getOption() != DialogOption.OK_YES) {
			return null;
		}

		description = Objects.isNull(dialogRef.get().getFileDescription()) ? "" : dialogRef.get().getFileDescription();

		Map<String, ByteBuffer> buffers = new HashMap<>();
		buffers.put(coreConfigModel.getParsedValue(FofpConfigEntry.FILE_DEFINE_LABEL_DESCRIPTION.getConfigKey(),
				String.class), string2ByteBuffer(description));

		return new RaeCreatedFile.Builder(true, FileType.FOLDER).setProcessorClass(FolderFileProcessor.class)
				.setBuffers(buffers).build();
	}

	/**
	 * 通过文本生成一个字节缓冲，该缓冲中拥有指定的文本。
	 * 
	 * @param string
	 *            指定的文本。
	 * @return 通过指定的文本生成的字节缓冲。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	private ByteBuffer string2ByteBuffer(String string) {
		Objects.requireNonNull(string, "入口参数 string 不能为 null。");
		byte[] bytes = string.getBytes();
		ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
		buffer.clear();
		buffer.put(bytes);
		buffer.rewind();
		return buffer;
	}

}
