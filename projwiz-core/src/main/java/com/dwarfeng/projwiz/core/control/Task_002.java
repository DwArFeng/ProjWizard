package com.dwarfeng.projwiz.core.control;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.dutil.basic.io.SaveFailedException;
import com.dwarfeng.dutil.develop.cfg.io.PropConfigSaver;
import com.dwarfeng.dutil.develop.resource.Resource;
import com.dwarfeng.projwiz.core.model.eum.LoggerStringKey;
import com.dwarfeng.projwiz.core.model.eum.ProjWizProperty;
import com.dwarfeng.projwiz.core.model.eum.ResourceKey;
import com.dwarfeng.projwiz.core.model.eum.ViewConfigEntry;
import com.dwarfeng.projwiz.core.model.struct.Editor;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.ProjectFilePair;
import com.dwarfeng.projwiz.core.model.struct.Toolkit.BackgroundType;

final class DisposeTask extends ProjWizTask {

	private final class ViewGetter implements Runnable {

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
		 * {@inheritDoc}
		 */
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

	private static final int VIEW_CONFIG_TASK = 2;
	private int backgroundMask = 0;

	private int viewConfigMask = 0;

	public DisposeTask(ProjWizard projWizard) {
		super(projWizard);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		// 定义变量
		boolean isTestCase;

		// 是否属于测试环境
		isTestCase = Boolean.parseBoolean(projWizard.getToolkit().getProperty(ProjWizProperty.TEST_CASE));

		// 强制关闭正在编辑的文件。
		closeEditingFile();

		// 强制关闭已经打开的工程。
		closeOpeningProject();

		// 试图停止后台。
		stopBackground();

		// 测试情形下并不加载界面，因此没必要释放界面
		if (!isTestCase) {
			// 释放图形交互界面。
			disposeGui();
		}

		// 设置退出代码。
		projWizard.getToolkit().setExitCode(0 | backgroundMask | viewConfigMask);
	}

	/**
	 * 释放图形交互界面。
	 * 
	 * @throws IOException
	 *             IO异常。
	 */
	private void disposeGui() throws IOException {
		// 保存视图配置。
		ViewGetter viewGetter = new ViewGetter();
		try {
			SwingUtil.invokeAndWaitInEventQueue(viewGetter);
		} catch (InterruptedException | InvocationTargetException ignore) {
			// 中断也要按照基本法。
		}
		projWizard.getToolkit().getViewConfigModel().setParsedValue(
				ViewConfigEntry.GUI_VISIBLE_MAINFRAME_WEST.getConfigKey(), viewGetter.westPanelVisible);
		projWizard.getToolkit().getViewConfigModel().setParsedValue(
				ViewConfigEntry.GUI_VISIBLE_MAINFRAME_EAST.getConfigKey(), viewGetter.eastPanelVisible);
		projWizard.getToolkit().getViewConfigModel().setParsedValue(
				ViewConfigEntry.GUI_VISIBLE_MAINFRAME_NORTH.getConfigKey(), viewGetter.northPanelVisible);
		projWizard.getToolkit().getViewConfigModel().setParsedValue(
				ViewConfigEntry.GUI_VISIBLE_MAINFRAME_SOUTH.getConfigKey(), viewGetter.southPanelVisible);

		projWizard.getToolkit().getViewConfigModel()
				.setParsedValue(ViewConfigEntry.GUI_MAXIMUM_MAINFRAME.getConfigKey(), viewGetter.maximum);

		projWizard.getToolkit().getViewConfigModel()
				.setParsedValue(ViewConfigEntry.GUI_SIZE_MAINFRAME_WEST.getConfigKey(), viewGetter.westPanelSize);
		projWizard.getToolkit().getViewConfigModel()
				.setParsedValue(ViewConfigEntry.GUI_SIZE_MAINFRAME_EAST.getConfigKey(), viewGetter.eastPanelSize);
		projWizard.getToolkit().getViewConfigModel()
				.setParsedValue(ViewConfigEntry.GUI_SIZE_MAINFRAME_SOUTH.getConfigKey(), viewGetter.southPanelSize);

		projWizard.getToolkit().getViewConfigModel()
				.setParsedValue(ViewConfigEntry.GUI_SIZE_MAINFRAME_WIDTH.getConfigKey(), viewGetter.frameWidth);
		projWizard.getToolkit().getViewConfigModel()
				.setParsedValue(ViewConfigEntry.GUI_SIZE_MAINFRAME_HEIGHT.getConfigKey(), viewGetter.frameHeight);

		projWizard.getToolkit().getViewConfigModel().setParsedValue(
				ViewConfigEntry.GUI_STATE_MAINFRAME_EXTENDED.getConfigKey(), viewGetter.extendedState);

		info(LoggerStringKey.TASK_DISPOSE_3);
		PropConfigSaver viewConfigSaver = null;
		try {
			viewConfigSaver = new PropConfigSaver(
					forceOpenOutputStream(ResourceKey.CFG_VIEW, LoggerStringKey.TASK_DISPOSE_4));
			Set<SaveFailedException> saveFailedExceptions = viewConfigSaver
					.countinuousSave(projWizard.getToolkit().getViewConfigModel());
			for (SaveFailedException e : saveFailedExceptions) {
				warn(LoggerStringKey.TASK_DISPOSE_5, e);
			}
			if (!saveFailedExceptions.isEmpty()) {
				viewConfigMask = VIEW_CONFIG_TASK;
			}
		} finally {
			if (Objects.nonNull(viewConfigSaver)) {
				viewConfigSaver.close();
			}
		}

		// 释放界面。
		try {
			SwingUtil.invokeAndWaitInEventQueue(() -> {
				projWizard.getToolkit().disposeMainFrame();
			});
		} catch (InvocationTargetException | InterruptedException ignore) {
			// 抛异常也要按照基本法。
		}
	}

	/**
	 * 停止后台。
	 */
	private void stopBackground() {
		// 停止后台。
		info(LoggerStringKey.TASK_DISPOSE_0);
		projWizard.getToolkit().getBackground(BackgroundType.CONCURRENT).shutdown();
		projWizard.getToolkit().getBackground(BackgroundType.FIFO).shutdown();

		// 当后台没有停止时执行的调度。
		// TODO 此时后台不会停止，因为正在运行本任务。
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
	}

	/**
	 * 强制关闭已经打开的文件。
	 */
	private void closeOpeningProject() {
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
	}

	/**
	 * 强制关闭正在编辑的文件。
	 */
	private void closeEditingFile() {
		info(LoggerStringKey.TASK_DISPOSE_7);
		Map<ProjectFilePair, Editor> tempEditorModel = new HashMap<>(projWizard.getToolkit().getEditorModel());
		tempEditorModel.forEach((pair, editor) -> {
			editor.stop();
			projWizard.getToolkit().getEditorModel().remove(pair);
		});
	}

	private OutputStream forceOpenOutputStream(ResourceKey resourceKey, LoggerStringKey loggerStringKey)
			throws IOException {
		Resource resource = projWizard.getToolkit().getCfgHandler().get(resourceKey.getName());
		try {
			return resource.openOutputStream();
		} catch (IOException e) {
			warn(loggerStringKey, e);
			resource.reset();
			return resource.openOutputStream();
		}
	}

}