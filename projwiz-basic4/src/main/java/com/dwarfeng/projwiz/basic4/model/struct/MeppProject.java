package com.dwarfeng.projwiz.basic4.model.struct;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.dwarfeng.dutil.basic.io.ByteBufferOutputStream;
import com.dwarfeng.dutil.basic.io.IOUtil;
import com.dwarfeng.dutil.basic.num.Interval;
import com.dwarfeng.dutil.basic.num.NumberUtil;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.projwiz.basic4.impl.MemoryProjectProcessor;
import com.dwarfeng.projwiz.basic4.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.basic4.model.eum.LoggerStringKey;
import com.dwarfeng.projwiz.basic4.model.eum.MeppConfigEntry;
import com.dwarfeng.projwiz.core.model.cm.Tree;
import com.dwarfeng.projwiz.core.model.cm.Tree.Path;
import com.dwarfeng.projwiz.core.model.obv.ProjectObverser;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.ProjectProcessor;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;
import com.dwarfeng.projwiz.core.view.eum.DialogMessage;
import com.dwarfeng.projwiz.core.view.struct.MessageDialogSetting;
import com.dwarfeng.projwiz.raefrm.RaeProject;
import com.dwarfeng.projwiz.raefrm.model.struct.ProjProcToolkit;

/**
 * 内存工程。
 * 
 * <p>
 * 内存工程处理器生成的工程。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public class MeppProject extends RaeProject {

	/**
	 * 内存工程的构造器。
	 * 
	 * @author DwArFeng
	 * @since 0.0.3-alpha
	 */
	public static class Builder extends RaeProjectBuilder {

		/** 新文件默认的缓冲容量。 */
		protected int defaultBuffCapa = 1024;

		/**
		 * 新实例。
		 * 
		 * @param name
		 *            指定的工程名称。
		 * @param projProcToolkit
		 *            指定的工程处理器工具包。
		 * @param fileTree
		 *            指定的文件树。
		 * @param fileNameMap
		 *            指定的文件-名称映射。
		 * @throws NullPointerException
		 *             入口参数为 <code>null</code>。
		 */
		public Builder(String name, ProjProcToolkit projProcToolkit, Tree<File> fileTree,
				Map<File, String> fileNameMap) {
			super(MemoryProjectProcessor.class, name, projProcToolkit, fileTree, fileNameMap);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public RaeProject build() {
			return new MeppProject(processorClass, name, fileTree, fileNameMap, projProcToolkit, obversers,
					defaultBuffCapa);
		}

		/**
		 * 设置默认分配大小。
		 * 
		 * @param defaultBuffCapa
		 *            指定的默认缓冲容量。
		 * @return 构造器自身。
		 */
		public Builder setDefaultBuffCapa(int defaultBuffCapa) {
			this.defaultBuffCapa = Math.max(0, defaultBuffCapa);
			return this;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Builder setObversers(Set<ProjectObverser> obversers) {
			super.setObversers(obversers);
			return this;
		}

	}

	/** 文件的默认缓冲容量。 */
	protected int defaultBuffCapa;

	/**
	 * 新实例。
	 * 
	 * @param processorClass
	 * @param name
	 * @param fileTree
	 * @param fileNameMap
	 * @param projProcToolkit
	 * @param obversers
	 * @param defaultBufferCapa
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected MeppProject(Class<? extends ProjectProcessor> processorClass, String name, Tree<File> fileTree,
			Map<File, String> fileNameMap, ProjProcToolkit projProcToolkit, Set<ProjectObverser> obversers,
			int defaultBufferCapa) {
		super(processorClass, name, fileTree, fileNameMap, projProcToolkit, obversers);

		NumberUtil.requireInInterval(defaultBufferCapa, Interval.INTERVAL_NOT_NEGATIVE, "新文件默认缓冲容量不能小于0");
		this.defaultBuffCapa = defaultBufferCapa;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File addFile(File parent, File file, String exceptName, AddingSituation situation) {
		Objects.requireNonNull(parent, "入口参数 parent 不能为 null。");
		Objects.requireNonNull(file, "入口参数 file 不能为 null。");
		Objects.requireNonNull(situation, "入口参数 situation 不能为 null。");

		lock.writeLock().lock();
		file.getLock().writeLock().unlock();
		try {
			switch (situation) {
			case BY_COPY:
				return addFileByCopy(parent, file, exceptName);
			case BY_MOVE:
				return addFileByMove(parent, file, exceptName);
			case BY_NEW:
				return addFileByNew(parent, file, exceptName);
			case OTHER:
				return addFileByOther(parent, file, exceptName);
			default:
				throw new IllegalArgumentException("未知的添加文件情形: " + situation);
			}

		} finally {
			file.getLock().writeLock().unlock();
			lock.writeLock().unlock();
		}
	}

	/**
	 * 获取工程的默认缓冲容量。
	 * 
	 * @return 工程的默认缓冲容量。
	 */
	public int getDefaultBuffCapa() {
		return defaultBuffCapa;
	}

	/**
	 * 设置工程的默认的缓冲容量。
	 * 
	 * @param defaultBuffCapa
	 *            工程的默认的缓冲容量。
	 */
	public void setDefaultBuffCapa(int defaultBuffCapa) {
		this.defaultBuffCapa = Math.max(0, defaultBuffCapa);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stop() {
		super.stop();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected File addFile_Sub(File parent, File file, String exceptName, AddingSituation situation)
			throws UnsupportedOperationException {
		file.getLock().writeLock().unlock();
		try {
			switch (situation) {
			case BY_COPY:
				return addFileByCopy(parent, file, exceptName);
			case BY_MOVE:
				return addFileByMove(parent, file, exceptName);
			case BY_NEW:
				return addFileByNew(parent, file, exceptName);
			case OTHER:
				return addFileByOther(parent, file, exceptName);
			default:
				throw new IllegalArgumentException("未知的添加文件情形: " + situation);
			}
		} finally {
			file.getLock().writeLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected File removeFile_Sub(File file, RemovingSituation situation) throws UnsupportedOperationException {
		switch (situation) {
		case BY_DELETE:
			return removeFileByDelete(file);
		case BY_MOVE:
			return removeFileByMove(file);
		case OTHER:
			return removeFileByOther(file);
		default:
			throw new IllegalArgumentException("未知的移除文件情形: " + situation);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected File renameFile_Sub(File file, String newName) throws UnsupportedOperationException {
		if (!fileTree.contains(file)) {
			projProcToolkit.formatWarn(LoggerStringKey.MEPP_PROJECT_REMOVEFILE_5, getName());
			projProcToolkit.warn(LoggerStringKey.MEPP_PROJECT_REMOVEFILE_1);
			projProcToolkit.formatWarn(LoggerStringKey.MEPP_PROJECT_REMOVEFILE_2, file.getProcessorClass().toString(),
					file.getClass().toString());
			return null;
		}

		// 检查文件在同级中是否重名，有重名则报错。
		if (isNameRepeat(fileTree.getParent(file), newName)) {
			Toolkit toolkit = projProcToolkit.getToolkit();
			toolkit.showMessageDialog(new MessageDialogSetting.Builder()
					.setMessage(projProcToolkit.label(LabelStringKey.MEPP_PROJECT_RENAMEFILE_0))
					.setTitle(projProcToolkit.label(LabelStringKey.MEPP_PROJECT_RENAMEFILE_1))
					.setDialogMessage(DialogMessage.INFORMATION_MESSAGE).build());
			return null;
		}

		String oldName = getFileName(file);
		fileNameMap.put(file, newName);
		Path<File> path = fileTree.getPath(file);
		fireFileRenamed(path, file, oldName, newName);

		return file;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void save_Sub() throws ProcessException, UnsupportedOperationException {
		throw new UnsupportedOperationException("save_Sub");
	}

	private File addFileByCopy(File parent, File file, String exceptName) {
		// TODO Auto-generated method stub
		return null;
	}

	private File addFileByMove(File parent, File file, String exceptName) {
		// TODO Auto-generated method stub
		return null;
	}

	private File addFileByNew(File parent, File file, String exceptName) {
		// 检查文件树是否含有父文件，不包含父文件则报错。
		if (!fileTree.contains(parent)) {
			projProcToolkit.formatWarn(LoggerStringKey.MEPP_PROJECT_ADDFILE_0, getName());
			projProcToolkit.formatWarn(LoggerStringKey.MEPP_PROJECT_ADDFILE_1);
			projProcToolkit.formatWarn(LoggerStringKey.MEPP_PROJECT_ADDFILE_2, parent.getProcessorClass().toString(),
					parent.getClass().toString());
			return null;
		}

		// 检查文件树是否含有文件，已含有文件则报错。
		if (fileTree.contains(file)) {
			projProcToolkit.formatWarn(LoggerStringKey.MEPP_PROJECT_ADDFILE_3, getName());
			projProcToolkit.formatWarn(LoggerStringKey.MEPP_PROJECT_ADDFILE_1);
			projProcToolkit.formatWarn(LoggerStringKey.MEPP_PROJECT_ADDFILE_2, parent.getProcessorClass().toString(),
					parent.getClass().toString());
			return null;
		}

		String actualFileName;

		// 进行文件名称的进一步处理
		if (Objects.isNull(exceptName)) {
			// 将名称设置为默认的名称，必要时加上序号。
			String fileName = projProcToolkit.getCoreConfigModel()
					.getParsedValue(MeppConfigEntry.PROJECT_NEWFILE_NAME_DEFAULT.getConfigKey(), String.class);

			if (!isNameRepeat(parent, fileName)) {
				actualFileName = fileName;
			} else {
				int index = 1;
				String indexFileName = indexFileName(fileName, index);
				while (isNameRepeat(parent, fileName)) {
					indexFileName = indexFileName(fileName, index);
				}
				actualFileName = indexFileName;
			}
		} else {
			// 检查文件在同级中是否重名，按照指定的配置向下进行。
			String fileName = exceptName;
			if (!isNameRepeat(parent, fileName)) {
				actualFileName = fileName;
			} else {
				int index = 1;
				String indexFileName = indexFileName(fileName, index);
				while (isNameRepeat(parent, fileName)) {
					indexFileName = indexFileName(fileName, index);
				}
				actualFileName = indexFileName;
			}
		}

		Map<String, ByteBuffer> buffers = new HashMap<>();

		// 复制指定文件的数据。
		int transBufferSize = projProcToolkit.getCoreConfigModel()
				.getParsedValue(MeppConfigEntry.PROJECT_BUFFER_DATATRANS.getConfigKey(), Integer.class);

		if (file.isReadSupported()) {
			for (String label : file.getLabels()) {
				ByteBufferOutputStream out = null;
				InputStream in = null;
				try {
					ByteBuffer buffer = ByteBuffer.allocate(defaultBuffCapa);
					out = new ByteBufferOutputStream(buffer);
					in = file.openInputStream(label);
					IOUtil.trans(in, out, transBufferSize);
					buffers.put(label, buffer);
				} catch (IOException e) {
					Toolkit toolkit = projProcToolkit.getToolkit();
					toolkit.showMessageDialog(new MessageDialogSetting.Builder()
							.setTitle(projProcToolkit.label(LabelStringKey.MEPP_PROJECT_ADDFILE_1))
							.setMessage(projProcToolkit.label(LabelStringKey.MEPP_PROJECT_ADDFILE_0))
							.setDialogMessage(DialogMessage.WARNING_MESSAGE).build());

					projProcToolkit.warn(LoggerStringKey.MEPP_PROJECT_ADDFILE_4, e);
					return null;
				} finally {
					if (Objects.nonNull(out)) {
						try {
							out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					if (Objects.nonNull(in)) {
						try {
							in.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		MeppFile actualAddedFile = new MeppFile.Builder(file.isFolder(), projProcToolkit, file.getFileType(), buffers)
				.setAccessTime(file.getAccessTime()).setModifyTime(file.getModifyTime()).setBuffCapa(defaultBuffCapa)
				.build();

		fileTree.add(parent, actualAddedFile);
		fileNameMap.put(actualAddedFile, actualFileName);
		fireFileAdded(fileTree.getPath(actualAddedFile), parent, actualAddedFile, AddingSituation.BY_NEW);

		return actualAddedFile;
	}

	private File addFileByOther(File parent, File file, String exceptName) {
		// TODO Auto-generated method stub
		return null;
	}

	private String indexFileName(String fileName, int index) {
		return String.format(fileName + "(%d)", index);
	}

	private boolean isNameRepeat(File parent, String fileName) {
		for (File checkFile : fileTree.getChilds(parent)) {
			if (Objects.equals(getFileName(checkFile), fileName)) {
				return true;
			}
		}
		return false;
	}

	private File removeFileByDelete(File file) {
		Objects.requireNonNull(file, "入口参数 file 不能为 null。");

		lock.writeLock().lock();
		try {
			if (!fileTree.contains(file)) {
				projProcToolkit.formatWarn(LoggerStringKey.MEPP_PROJECT_REMOVEFILE_0, getName());
				projProcToolkit.warn(LoggerStringKey.MEPP_PROJECT_REMOVEFILE_1);
				projProcToolkit.formatWarn(LoggerStringKey.MEPP_PROJECT_REMOVEFILE_2,
						file.getProcessorClass().toString(), file.getClass().toString());
				return null;
			}

			if (fileTree.getChilds(file).size() > 0) {
				projProcToolkit.formatWarn(LoggerStringKey.MEPP_PROJECT_REMOVEFILE_3, getName());
				projProcToolkit.warn(LoggerStringKey.MEPP_PROJECT_REMOVEFILE_1);
				projProcToolkit.formatWarn(LoggerStringKey.MEPP_PROJECT_REMOVEFILE_2,
						file.getProcessorClass().toString(), file.getClass().toString());
				return null;
			}

			if (Objects.equals(file, fileTree.getRoot())) {
				projProcToolkit.formatWarn(LoggerStringKey.MEPP_PROJECT_REMOVEFILE_3, getName());
				projProcToolkit.warn(LoggerStringKey.MEPP_PROJECT_REMOVEFILE_1);
				projProcToolkit.formatWarn(LoggerStringKey.MEPP_PROJECT_REMOVEFILE_2,
						file.getProcessorClass().toString(), file.getClass().toString());
				return null;
			}

			Path<File> path = fileTree.getPath(file);
			File parent = fileTree.getParent(file);

			fileTree.remove(file);
			fireFileRemoved(path, parent, file, RemovingSituation.BY_DELETE);

			return file;
		} finally {
			lock.writeLock().unlock();
		}
	}

	private File removeFileByMove(File file) {
		// TODO Auto-generated method stub
		return null;
	}

	private File removeFileByOther(File file) {
		// TODO Auto-generated method stub
		return null;
	}

}
