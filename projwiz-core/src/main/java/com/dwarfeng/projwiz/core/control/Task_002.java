package com.dwarfeng.projwiz.core.control;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.dutil.basic.io.SaveFailedException;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.dutil.develop.cfg.io.PropConfigSaver;
import com.dwarfeng.dutil.develop.resource.Resource;
import com.dwarfeng.projwiz.core.model.eum.LoggerStringKey;
import com.dwarfeng.projwiz.core.model.eum.ModalConfiguration;
import com.dwarfeng.projwiz.core.model.eum.ResourceKey;
import com.dwarfeng.projwiz.core.model.io.XmlProcessorConfigSaver;
import com.dwarfeng.projwiz.core.model.struct.Editor;
import com.dwarfeng.projwiz.core.model.struct.FileProcessor;
import com.dwarfeng.projwiz.core.model.struct.Processor;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.ProjectFilePair;
import com.dwarfeng.projwiz.core.model.struct.ProjectProcessor;
import com.dwarfeng.projwiz.core.model.struct.Toolkit.BackgroundType;

final class DisposeTask extends ProjWizTask {

	private final class ModalGetter implements Runnable {

		private boolean westPanelVisible;
		private boolean eastPanelVisible;
		private boolean northPanelVisible;
		private boolean southPanelVisible;

		private boolean maximum;

		private int westPanelSize;
		private int eastPanelSize;
		private int southPanelSize;

		private int frameWidth;
		private int frameHeight;

		private int extendedState;

		/**
		 * @return the eastPanelSize
		 */
		public int getEastPanelSize() {
			return eastPanelSize;
		}

		/**
		 * @return the extendedState
		 */
		public int getExtendedState() {
			return extendedState;
		}

		/**
		 * @return the frameHeight
		 */
		public int getFrameHeight() {
			return frameHeight;
		}

		/**
		 * @return the frameWidth
		 */
		public int getFrameWidth() {
			return frameWidth;
		}

		/**
		 * @return the southPanelSize
		 */
		public int getSouthPanelSize() {
			return southPanelSize;
		}

		/**
		 * @return the westPanelSize
		 */
		public int getWestPanelSize() {
			return westPanelSize;
		}

		/**
		 * @return the eastPanelVisible
		 */
		public boolean isEastPanelVisible() {
			return eastPanelVisible;
		}

		/**
		 * 
		 * @return
		 */
		public boolean isNorthPanelVisible() {
			return northPanelVisible;
		}

		/**
		 * @return the southPanelVisible
		 */
		public boolean isSouthPanelVisible() {
			return southPanelVisible;
		}

		/**
		 * @return the westPanelVisible
		 */
		public boolean isWestPanelVisible() {
			return westPanelVisible;
		}

		/**
		 * @return the maximum
		 */
		public boolean isMaximum() {
			return maximum;
		}

		@Override
		public void run() {
			westPanelVisible = projWizard.getToolkit().getMainFrame().getVisibleModel().isWestVisible();
			eastPanelVisible = projWizard.getToolkit().getMainFrame().getVisibleModel().isEastVisible();
			northPanelVisible = projWizard.getToolkit().getMainFrame().getVisibleModel().isNorthVisible();
			southPanelVisible = projWizard.getToolkit().getMainFrame().getVisibleModel().isSouthVisible();

			maximum = projWizard.getToolkit().getMainFrame().getVisibleModel().isMaximum();

			westPanelSize = projWizard.getToolkit().getMainFrame().getWestPreferredValue();
			eastPanelSize = projWizard.getToolkit().getMainFrame().getEastPreferredValue();
			southPanelSize = projWizard.getToolkit().getMainFrame().getSouthPreferredValue();

			frameWidth = projWizard.getToolkit().getMainFrame().getFrameWidth();
			frameHeight = projWizard.getToolkit().getMainFrame().getFrameHeight();

			extendedState = projWizard.getToolkit().getMainFrame().getExtendedState();
		}

	}

	private static final int BACKGROUND_MASK = 1;

	private static final int MODAL_CONFIG_TASK = 2;
	private int backgroundMask = 0;

	private int modalConfigMask = 0;

	public DisposeTask(ProjWizard projWizard) {
		super(projWizard);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		// 强制关闭正在编辑的文件。
		info(LoggerStringKey.TASK_DISPOSE_7);
		Map<ProjectFilePair, Editor> tempEditorModel = new HashMap<>(projWizard.getToolkit().getEditorModel());
		tempEditorModel.forEach((pair, editor) -> {
			editor.stop();
			projWizard.getToolkit().getEditorModel().remove(pair);
		});

		// 强制关闭已经打开的工程。
		info(LoggerStringKey.TASK_DISPOSE_8);
		projWizard.getToolkit().getFocusProjectModel().clear();
		projWizard.getToolkit().getHoldProjectModel().getLock().readLock().lock();
		try {
			List<Project> tempOpenedProjects = new ArrayList<>(projWizard.getToolkit().getHoldProjectModel());
			tempOpenedProjects.forEach(project -> {
				project.stop();
				projWizard.getToolkit().getHoldProjectModel().remove(project);
			});
		} finally {
			projWizard.getToolkit().getHoldProjectModel().getLock().readLock().unlock();
		}

		// 卸载处理器并存储配置
		info(LoggerStringKey.TASK_DISPOSE_9);
		Set<ProjectProcessor> tempProjectProcessors = new HashSet<>(projWizard.getToolkit().getProjectProcessorModel());
		tempProjectProcessors.forEach(processor -> {
			saveConfig(processor);
			projWizard.getToolkit().getProjectProcessorModel().remove(processor);
		});
		Set<FileProcessor> tempFileProcessors = new HashSet<>(projWizard.getToolkit().getFileProcessorModel());
		tempFileProcessors.forEach(processor -> {
			saveConfig(processor);
			projWizard.getToolkit().getFileProcessorModel().remove(processor);
		});

		// 试图停止后台。
		info(LoggerStringKey.TASK_DISPOSE_0);
		projWizard.getToolkit().getBackground(BackgroundType.CONCURRENT).shutdown();
		projWizard.getToolkit().getBackground(BackgroundType.FIFO).shutdown();

		// 当后台没有停止时执行的调度。
		if (!projWizard.getToolkit().getBackground(BackgroundType.CONCURRENT).isTerminated()) {
			backgroundMask = BACKGROUND_MASK;
			formatWarn(LoggerStringKey.TASK_DISPOSE_1,
					projWizard.getToolkit().getBackground(BackgroundType.CONCURRENT).tasks().size());
			projWizard.getToolkit().getBackground(BackgroundType.CONCURRENT).getLock().readLock().lock();
			try {
				projWizard.getToolkit().getBackground(BackgroundType.CONCURRENT).tasks().forEach(task -> {
					formatWarn(LoggerStringKey.TASK_DISPOSE_2, task.toString(), task.getClass().toString());
				});
			} finally {
				projWizard.getToolkit().getBackground(BackgroundType.CONCURRENT).getLock().readLock().unlock();
			}
		}
		if (!projWizard.getToolkit().getBackground(BackgroundType.FIFO).isTerminated()) {
			backgroundMask = BACKGROUND_MASK;
			formatWarn(LoggerStringKey.TASK_DISPOSE_1,
					projWizard.getToolkit().getBackground(BackgroundType.FIFO).tasks().size());
			projWizard.getToolkit().getBackground(BackgroundType.FIFO).getLock().readLock().lock();
			try {
				projWizard.getToolkit().getBackground(BackgroundType.FIFO).tasks().forEach(task -> {
					formatWarn(LoggerStringKey.TASK_DISPOSE_2, task.toString(), task.getClass().toString());
				});
			} finally {
				projWizard.getToolkit().getBackground(BackgroundType.FIFO).getLock().readLock().unlock();
			}
		}

		// 保存模态配置。
		ModalGetter modalGetter = new ModalGetter();
		try {
			SwingUtil.invokeAndWaitInEventQueue(modalGetter);
		} catch (InterruptedException ignore) {
			// 中断也要按照基本法。
		}
		projWizard.getToolkit().getModalConfigModel().setParsedValue(
				ModalConfiguration.GUI_VISIBLE_MAINFRAME_WEST.getConfigKey(), modalGetter.isWestPanelVisible());
		projWizard.getToolkit().getModalConfigModel().setParsedValue(
				ModalConfiguration.GUI_VISIBLE_MAINFRAME_EAST.getConfigKey(), modalGetter.isEastPanelVisible());
		projWizard.getToolkit().getModalConfigModel().setParsedValue(
				ModalConfiguration.GUI_VISIBLE_MAINFRAME_NORTH.getConfigKey(), modalGetter.isNorthPanelVisible());
		projWizard.getToolkit().getModalConfigModel().setParsedValue(
				ModalConfiguration.GUI_VISIBLE_MAINFRAME_SOUTH.getConfigKey(), modalGetter.isSouthPanelVisible());

		projWizard.getToolkit().getModalConfigModel()
				.setParsedValue(ModalConfiguration.GUI_MAXIMUM_MAINFRAME.getConfigKey(), modalGetter.isMaximum());

		projWizard.getToolkit().getModalConfigModel().setParsedValue(
				ModalConfiguration.GUI_SIZE_MAINFRAME_WEST.getConfigKey(), modalGetter.getWestPanelSize());
		projWizard.getToolkit().getModalConfigModel().setParsedValue(
				ModalConfiguration.GUI_SIZE_MAINFRAME_EAST.getConfigKey(), modalGetter.getEastPanelSize());
		projWizard.getToolkit().getModalConfigModel().setParsedValue(
				ModalConfiguration.GUI_SIZE_MAINFRAME_SOUTH.getConfigKey(), modalGetter.getSouthPanelSize());

		projWizard.getToolkit().getModalConfigModel().setParsedValue(
				ModalConfiguration.GUI_SIZE_MAINFRAME_WIDTH.getConfigKey(), modalGetter.getFrameWidth());
		projWizard.getToolkit().getModalConfigModel().setParsedValue(
				ModalConfiguration.GUI_SIZE_MAINFRAME_HEIGHT.getConfigKey(), modalGetter.getFrameHeight());

		projWizard.getToolkit().getModalConfigModel().setParsedValue(
				ModalConfiguration.GUI_STATE_MAINFRAME_EXTENDED.getConfigKey(), modalGetter.getExtendedState());

		info(LoggerStringKey.TASK_DISPOSE_3);
		PropConfigSaver modalConfigSaver = null;
		try {
			modalConfigSaver = new PropConfigSaver(
					forceOpenOutputStream(ResourceKey.CONFIGURATION_MODAL, LoggerStringKey.TASK_DISPOSE_4));
			Set<SaveFailedException> saveFailedExceptions = modalConfigSaver
					.countinuousSave(projWizard.getToolkit().getModalConfigModel());
			for (SaveFailedException e : saveFailedExceptions) {
				warn(LoggerStringKey.TASK_DISPOSE_5, e);
			}
			if (!saveFailedExceptions.isEmpty()) {
				modalConfigMask = MODAL_CONFIG_TASK;
			}
		} finally {
			if (Objects.nonNull(modalConfigSaver)) {
				modalConfigSaver.close();
			}
		}

		// 保存处理器配置
		XmlProcessorConfigSaver processorConfigSaver = null;
		try {
			processorConfigSaver = new XmlProcessorConfigSaver(
					forceOpenOutputStream(ResourceKey.PROCESSOR_CONFIG_SETTING, LoggerStringKey.TASK_DISPOSE_4));
			Set<SaveFailedException> saveFailedExceptions = processorConfigSaver
					.countinuousSave(projWizard.getToolkit().getProcessorConfigHandler());
			for (SaveFailedException e : saveFailedExceptions) {
				warn(LoggerStringKey.TASK_DISPOSE_6, e);
			}
			if (!saveFailedExceptions.isEmpty()) {
				modalConfigMask = MODAL_CONFIG_TASK;
			}
		} finally {
			if (Objects.nonNull(processorConfigSaver)) {
				processorConfigSaver.close();
			}
		}

		// 释放界面。
		SwingUtil.invokeAndWaitInEventQueue(() -> {
			projWizard.getToolkit().disposeMainFrame();
		});

		// 设置退出代码。
		projWizard.getToolkit().setExitCode(0 | backgroundMask | modalConfigMask);
	}

	private OutputStream forceOpenOutputStream(ResourceKey resourceKey, LoggerStringKey loggerStringKey)
			throws IOException {
		Resource resource = projWizard.getToolkit().getResourceHandler().get(resourceKey.getName());
		try {
			return resource.openOutputStream();
		} catch (IOException e) {
			warn(loggerStringKey, e);
			resource.reset();
			return resource.openOutputStream();
		}
	}

	private void saveConfig(Processor processor) {
		try {
			processor.saveConfig();
		} catch (ProcessException e) {
			warn(LoggerStringKey.TASK_DISPOSE_10);
			formatWarn(LoggerStringKey.TASK_DISPOSE_11, processor.getKey(), processor.getClass().toString());
			warn(LoggerStringKey.TASK_DISPOSE_12, e);
		}
	}
}