package com.dwarfeng.projwiz.basic4.impl;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.dutil.basic.gui.awt.ImageUtil;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.projwiz.basic4.model.eum.FileType;
import com.dwarfeng.projwiz.basic4.model.eum.ImageKey;
import com.dwarfeng.projwiz.basic4.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.basic4.model.eum.MeppConfigEntry;
import com.dwarfeng.projwiz.basic4.model.eum.PermDemandKey;
import com.dwarfeng.projwiz.basic4.model.struct.MeppConstantsProvider;
import com.dwarfeng.projwiz.basic4.model.struct.MeppFile;
import com.dwarfeng.projwiz.basic4.model.struct.MeppProject;
import com.dwarfeng.projwiz.basic4.util.Constants;
import com.dwarfeng.projwiz.basic4.view.MeppProjectPropUI;
import com.dwarfeng.projwiz.core.model.cm.MapTree;
import com.dwarfeng.projwiz.core.model.cm.Tree;
import com.dwarfeng.projwiz.core.model.eum.DialogMessage;
import com.dwarfeng.projwiz.core.model.eum.IconVariability;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.MetaDataStorage;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.PropUI;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;
import com.dwarfeng.projwiz.core.util.ProjectFileUtil;
import com.dwarfeng.projwiz.core.view.struct.InputDialogSetting;
import com.dwarfeng.projwiz.raefrm.RaeProjectProcessor;

/**
 * 内存工程处理器。
 * 
 * <p>
 * 将工程存储在内存中。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public class MemoryProjectProcessor extends RaeProjectProcessor {

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
	public static MemoryProjectProcessor newInstance(ReferenceModel<Toolkit> toolkitRef,
			MetaDataStorage metaDataStorage) throws ProcessException {
		return new MemoryProjectProcessor(toolkitRef, metaDataStorage);
	}

	/**
	 * 新实例。
	 * 
	 * @param toolkitRef
	 *            指定的工具包引用模型。
	 * @param metaDataStorage
	 *            指定的元数据仓库。
	 * @throws ProcessException
	 *             初始化过程异常。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected MemoryProjectProcessor(ReferenceModel<? extends Toolkit> toolkitRef, MetaDataStorage metaDataStorage)
			throws ProcessException {
		super(toolkitRef, metaDataStorage, new MeppConstantsProvider());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PropUI getFilePropUI(Project project, File file) {
		lock.writeLock().lock();
		try {
			if (!(project instanceof MeppProject)) {
				return null;
			}

			// TODO return new MeppProjectPropUI.Builder(new
			// ProjProcToolkitImpl(), (MeppProject) project).build();
			return null;
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Image getProjectIcon(Project project) {
		return ImageUtil.getInternalImage(ImageKey.MEPP_PROJ_ICON);
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
		lock.writeLock().lock();
		try {
			if (!(project instanceof MeppProject)) {
				return null;
			}

			return new MeppProjectPropUI.Builder(new ProjProcToolkitImpl(), (MeppProject) project).build();
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNewProjectSupported() {
		return true;
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
	public Project newProject() throws ProcessException {
		lock.writeLock().lock();
		try {
			requirePermKeyAvailable(PermDemandKey.MEPP_PROCESSOR_NEWPROJECT);
			// TODO 检查是否为测试环境。

			String name = null;
			boolean isNameAssign = coreConfigModel
					.getParsedValue(MeppConfigEntry.PROCESSOR_PROJNAME_ISASSIGN.getConfigKey(), Boolean.class);

			int buffCapa = coreConfigModel
					.getParsedValue(MeppConfigEntry.PROCESSOR_DEFAULTBUFFCAPA_VALUE.getConfigKey(), Integer.class);
			boolean isAskSize = coreConfigModel
					.getParsedValue(MeppConfigEntry.PROCESSOR_DEFAULTBUFFCAPA_ISASK.getConfigKey(), Boolean.class);

			if (isNameAssign) {
				name = (String) getToolkit().showInputDialog(
						new InputDialogSetting.Builder().setTitle(label(LabelStringKey.MEPP_PROCESSOR_NEWPROJECT_3))
								.setMessage(label(LabelStringKey.MEPP_PROCESSOR_NEWPROJECT_4))
								.setDialogMessage(DialogMessage.QUESTION_MESSAGE).build());

				while (!isValidName(name)) {
					if (Objects.isNull(name)) {
						return null;
					}

					name = (String) getToolkit().showInputDialog(
							new InputDialogSetting.Builder().setTitle(label(LabelStringKey.MEPP_PROCESSOR_NEWPROJECT_3))
									.setMessage(label(LabelStringKey.MEPP_PROCESSOR_NEWPROJECT_5))
									.setDialogMessage(DialogMessage.QUESTION_MESSAGE).build());
				}
			} else {
				name = UUID.randomUUID().toString();
			}

			if (isAskSize) {
				String inputValue = null;
				inputValue = (String) getToolkit().showInputDialog(new InputDialogSetting.Builder()
						.setTitle(label(LabelStringKey.MEPP_PROCESSOR_NEWPROJECT_0))
						.setMessage(label(LabelStringKey.MEPP_PROCESSOR_NEWPROJECT_1))
						.setDialogMessage(DialogMessage.QUESTION_MESSAGE).setInitialSelectionValue(buffCapa).build());

				while (!isNotNegNumber(inputValue)) {
					if (Objects.isNull(inputValue)) {
						return null;
					}

					inputValue = (String) getToolkit().showInputDialog(
							new InputDialogSetting.Builder().setTitle(label(LabelStringKey.MEPP_PROCESSOR_NEWPROJECT_0))
									.setMessage(label(LabelStringKey.MEPP_PROCESSOR_NEWPROJECT_2))
									.setDialogMessage(DialogMessage.QUESTION_MESSAGE).setInitialSelectionValue(buffCapa)
									.build());
				}

				buffCapa = Integer.parseInt(inputValue);

			} else {
				buffCapa = coreConfigModel
						.getParsedValue(MeppConfigEntry.PROCESSOR_DEFAULTBUFFCAPA_VALUE.getConfigKey(), Integer.class);
			}

			File rootFile = new MeppFile.Builder(true, new ProjProcToolkitImpl(), FileType.FOLDER, new HashMap<>())
					.setBuffCapa(0).setReadSupported(false).setWriteSupported(false).build();

			Tree<File> fileTree = new MapTree<>();
			fileTree.addRoot(rootFile);
			Map<File, String> fileNameMap = new HashMap<>();
			fileNameMap.put(rootFile, Constants.ROOT_FILE_NAME);

			return new MeppProject.Builder(name, new ProjProcToolkitImpl(), fileTree, fileNameMap)
					.setDefaultBuffCapa(buffCapa).build();
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * 判断一个字符串是否是一个大于等于0的数。
	 * 
	 * @param string
	 *            指定的字符串
	 * @return 指定的字符串是否是一个大于等于0的数。
	 */
	private boolean isNotNegNumber(String string) {
		if (Objects.isNull(string)) {
			return false;
		}

		try {
			Integer.parseInt(string);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 判断指定的名称是否是合法的文件名称。
	 * 
	 * @param string
	 *            指定的名称。
	 * @return 指定的名称是否是合法的文件名称。
	 */
	private boolean isValidName(String string) {
		return ProjectFileUtil.isValidProjectName(string);
	}

}
