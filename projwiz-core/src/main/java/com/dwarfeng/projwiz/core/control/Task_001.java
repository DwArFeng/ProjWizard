package com.dwarfeng.projwiz.core.control;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.dutil.basic.io.FileUtil;
import com.dwarfeng.dutil.basic.io.LoadFailedException;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.dutil.develop.cfg.ExconfigModel;
import com.dwarfeng.dutil.develop.cfg.io.PropConfigLoader;
import com.dwarfeng.dutil.develop.i18n.io.XmlPropFileI18nLoader;
import com.dwarfeng.dutil.develop.resource.Resource;
import com.dwarfeng.dutil.develop.resource.io.XmlJar2FileResourceLoader;
import com.dwarfeng.projwiz.core.model.eum.LoggerStringKey;
import com.dwarfeng.projwiz.core.model.eum.ModalConfiguration;
import com.dwarfeng.projwiz.core.model.eum.ProjWizProperty;
import com.dwarfeng.projwiz.core.model.eum.ResourceKey;
import com.dwarfeng.projwiz.core.model.io.Log4jLoggerLoader;
import com.dwarfeng.projwiz.core.model.io.XmlFileProcessorLoader;
import com.dwarfeng.projwiz.core.model.io.XmlProcessorConfigLoader;
import com.dwarfeng.projwiz.core.model.io.XmlProjectProcessorLoader;
import com.dwarfeng.projwiz.core.model.struct.FileProcessor;
import com.dwarfeng.projwiz.core.model.struct.Processor;
import com.dwarfeng.projwiz.core.model.struct.ProjectProcessor;
import com.dwarfeng.projwiz.core.util.Constants;

/**
 * 用于程序初始化的任务。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
final class PoseTask extends ProjWizTask {

	public PoseTask(ProjWizard projWizard) {
		super(projWizard);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {

		// 输出初始化信息
		info(LoggerStringKey.TASK_POSE_6);

		// 加载所有资源。
		info(LoggerStringKey.TASK_POSE_7);
		XmlJar2FileResourceLoader resourceLoader = null;
		try {
			resourceLoader = new XmlJar2FileResourceLoader(
					this.getClass().getResourceAsStream(Constants.RESOURCE_PATH));
			resourceLoader.load(projWizard.getToolkit().getResourceHandler());

		} finally {
			if (Objects.nonNull(resourceLoader)) {
				resourceLoader.close();
			}
		}

		// 加载记录器资源。
		info(LoggerStringKey.TASK_POSE_8);
		Log4jLoggerLoader loggerLoader = null;
		try {
			loggerLoader = new Log4jLoggerLoader(
					forceOpenInputStream(ResourceKey.LOGGER_SETTING, LoggerStringKey.TASK_POSE_0));
			Set<LoadFailedException> loadFailedExceptions = loggerLoader
					.countinuousLoad(projWizard.getToolkit().getLoggerHandler());
			projWizard.getToolkit().getLoggerHandler().useAll();
			for (LoadFailedException e : loadFailedExceptions) {
				warn(LoggerStringKey.TASK_POSE_1, e);
			}

		} finally {
			if (Objects.nonNull(loggerLoader)) {
				loggerLoader.close();
			}
		}

		// 加载记录器国际化资源。
		info(LoggerStringKey.TASK_POSE_9);
		XmlPropFileI18nLoader loggerI18nLoader = null;
		try {
			loggerI18nLoader = new XmlPropFileI18nLoader(
					forceOpenInputStream(ResourceKey.I18N_LOGGER_SETTING, LoggerStringKey.TASK_POSE_0));
			Set<LoadFailedException> loadFailedExceptions = loggerI18nLoader
					.countinuousLoad(projWizard.getToolkit().getLoggerI18nHandler());
			for (LoadFailedException e : loadFailedExceptions) {
				warn(LoggerStringKey.TASK_POSE_2, e);
			}
		} finally {
			if (Objects.nonNull(loggerI18nLoader)) {
				loggerI18nLoader.close();
			}
		}

		// 加载标签国际化资源。
		info(LoggerStringKey.TASK_POSE_10);
		XmlPropFileI18nLoader labelI18nLoader = null;
		try {
			labelI18nLoader = new XmlPropFileI18nLoader(
					forceOpenInputStream(ResourceKey.I18N_LABEL_SETTING, LoggerStringKey.TASK_POSE_0));
			Set<LoadFailedException> loadFailedExceptions = labelI18nLoader
					.countinuousLoad(projWizard.getToolkit().getLoggerI18nHandler());
			for (LoadFailedException e : loadFailedExceptions) {
				warn(LoggerStringKey.TASK_POSE_5, e);
			}
		} finally {
			if (Objects.nonNull(labelI18nLoader)) {
				labelI18nLoader.close();
			}
		}

		// 加载核心配置。
		info(LoggerStringKey.TASK_POSE_11);
		PropConfigLoader coreConfigLoader = null;
		try {
			coreConfigLoader = new PropConfigLoader(
					forceOpenInputStream(ResourceKey.CONFIGURATION_CORE, LoggerStringKey.TASK_POSE_0));
			Set<LoadFailedException> loadFailedExceptions = coreConfigLoader
					.countinuousLoad(projWizard.getToolkit().getCoreConfigModel());
			for (LoadFailedException e : loadFailedExceptions) {
				warn(LoggerStringKey.TASK_POSE_3, e);
			}
		} finally {
			if (Objects.nonNull(coreConfigLoader)) {
				coreConfigLoader.close();
			}
		}

		// 加载模态配置。
		info(LoggerStringKey.TASK_POSE_12);
		PropConfigLoader modalConfigLoader = null;
		try {
			modalConfigLoader = new PropConfigLoader(
					forceOpenInputStream(ResourceKey.CONFIGURATION_MODAL, LoggerStringKey.TASK_POSE_0));
			Set<LoadFailedException> loadFailedExceptions = modalConfigLoader
					.countinuousLoad(projWizard.getToolkit().getModalConfigModel());
			for (LoadFailedException e : loadFailedExceptions) {
				warn(LoggerStringKey.TASK_POSE_4, e);
			}
		} finally {
			if (Objects.nonNull(modalConfigLoader)) {
				modalConfigLoader.close();
			}
		}

		// 加载处理器配置。
		info(LoggerStringKey.TASK_POSE_21);
		XmlProcessorConfigLoader processorConfigLoader = null;
		try {
			processorConfigLoader = new XmlProcessorConfigLoader(
					forceOpenInputStream(ResourceKey.PROCESSOR_CONFIG_SETTING, LoggerStringKey.TASK_POSE_0));
			Set<LoadFailedException> loadFailedExceptions = processorConfigLoader
					.countinuousLoad(projWizard.getToolkit().getProcessorConfigHandler());
			for (LoadFailedException e : loadFailedExceptions) {
				warn(LoggerStringKey.TASK_POSE_4, e);
			}
		} finally {
			if (Objects.nonNull(processorConfigLoader)) {
				processorConfigLoader.close();
			}
		}

		// 加载外部jar包。
		formatInfo(LoggerStringKey.TASK_POSE_13, projWizard.getToolkit().getProperty(ProjWizProperty.PLUGIN_PATH));
		File plugins_dir = new File(projWizard.getToolkit().getProperty(ProjWizProperty.PLUGIN_PATH));
		File[] plugin_jars = FileUtil.listAllFile(plugins_dir, file -> {
			if (file.getName().endsWith(".jar"))
				return true;
			return false;
		});

		formatInfo(LoggerStringKey.TASK_POSE_15, plugin_jars.length);
		for (File plugin_jar : plugin_jars) {
			formatInfo(LoggerStringKey.TASK_POSE_14, plugin_jar.getPath());
			projWizard.getToolkit().getPluginClassLoader().addURL(plugin_jar.toURI().toURL());

		}

		// 加载并实例化ProjectProcessor
		info(LoggerStringKey.TASK_POSE_16);
		XmlProjectProcessorLoader projectProcessorLoader = null;
		try {
			projectProcessorLoader = new XmlProjectProcessorLoader(
					forceOpenInputStream(ResourceKey.REFLECT_PROJPROC, LoggerStringKey.TASK_POSE_0),
					projWizard.getToolkit().getPluginClassLoader(),
					projWizard.getToolkit().getProcessorConfigHandler());
			Set<LoadFailedException> loadFailedExceptions = projectProcessorLoader
					.countinuousLoad(projWizard.getToolkit().getProjectProcessorModel());
			for (LoadFailedException e : loadFailedExceptions) {
				warn(LoggerStringKey.TASK_POSE_27, e);
			}
		} finally {
			if (Objects.nonNull(projectProcessorLoader)) {
				projectProcessorLoader.close();
			}
		}

		formatInfo(LoggerStringKey.TASK_POSE_17, projWizard.getToolkit().getProjectProcessorModel().size());
		projWizard.getToolkit().getProjectProcessorModel().getLock().readLock().lock();
		try {
			for (ProjectProcessor processor : projWizard.getToolkit().getProjectProcessorModel()) {
				formatInfo(LoggerStringKey.TASK_POSE_18, processor.getKey(), processor.getClass());
			}
		} finally {
			projWizard.getToolkit().getProjectProcessorModel().getLock().readLock().unlock();
		}

		// 加载并实例化FileProcessor
		info(LoggerStringKey.TASK_POSE_19);
		XmlFileProcessorLoader fileProcessorLoader = null;
		try {
			fileProcessorLoader = new XmlFileProcessorLoader(
					forceOpenInputStream(ResourceKey.REFLECT_FILEPROC, LoggerStringKey.TASK_POSE_0),
					projWizard.getToolkit().getPluginClassLoader(),
					projWizard.getToolkit().getProcessorConfigHandler());
			Set<LoadFailedException> loadFailedExceptions = fileProcessorLoader
					.countinuousLoad(projWizard.getToolkit().getFileProcessorModel());
			for (LoadFailedException e : loadFailedExceptions) {
				warn(LoggerStringKey.TASK_POSE_28, e);
			}
		} finally {
			if (Objects.nonNull(fileProcessorLoader)) {
				fileProcessorLoader.close();
			}
		}

		formatInfo(LoggerStringKey.TASK_POSE_20, projWizard.getToolkit().getFileProcessorModel().size());
		projWizard.getToolkit().getFileProcessorModel().getLock().readLock().lock();
		try {
			for (FileProcessor processor : projWizard.getToolkit().getFileProcessorModel()) {
				formatInfo(LoggerStringKey.TASK_POSE_18, processor.getKey(), processor.getClass());
			}
		} finally {
			projWizard.getToolkit().getFileProcessorModel().getLock().readLock().unlock();
		}

		// 处理器载入配置
		info(LoggerStringKey.TASK_POSE_23);
		Set<ProjectProcessor> tempProjectProcessors = new HashSet<>(projWizard.getToolkit().getProjectProcessorModel());
		tempProjectProcessors.forEach(processor -> {
			loadConfig(processor);
		});
		Set<FileProcessor> tempFileProcessors = new HashSet<>(projWizard.getToolkit().getFileProcessorModel());
		tempFileProcessors.forEach(processor -> {
			loadConfig(processor);
		});

		// 生成界面
		SwingUtil.invokeAndWaitInEventQueue(() -> {
			final ExconfigModel c = projWizard.getToolkit().getModalConfigModel();

			final boolean westPanelVisible = c
					.getParsedValue(ModalConfiguration.GUI_VISIBLE_MAINFRAME_WEST.getConfigKey(), Boolean.class);
			final boolean eastPanelVisible = c
					.getParsedValue(ModalConfiguration.GUI_VISIBLE_MAINFRAME_EAST.getConfigKey(), Boolean.class);
			final boolean northPanelVisible = c
					.getParsedValue(ModalConfiguration.GUI_VISIBLE_MAINFRAME_NORTH.getConfigKey(), Boolean.class);
			final boolean southPanelVisible = c
					.getParsedValue(ModalConfiguration.GUI_VISIBLE_MAINFRAME_SOUTH.getConfigKey(), Boolean.class);

			final boolean maximum = c.getParsedValue(ModalConfiguration.GUI_MAXIMUM_MAINFRAME.getConfigKey(),
					Boolean.class);

			final int westPanelSize = c.getParsedValue(ModalConfiguration.GUI_SIZE_MAINFRAME_WEST.getConfigKey(),
					Integer.class);
			final int eastPanelSize = c.getParsedValue(ModalConfiguration.GUI_SIZE_MAINFRAME_EAST.getConfigKey(),
					Integer.class);
			final int southPanelSize = c.getParsedValue(ModalConfiguration.GUI_SIZE_MAINFRAME_SOUTH.getConfigKey(),
					Integer.class);

			final int frameWidth = c.getParsedValue(ModalConfiguration.GUI_SIZE_MAINFRAME_WIDTH.getConfigKey(),
					Integer.class);
			final int frameHeight = c.getParsedValue(ModalConfiguration.GUI_SIZE_MAINFRAME_HEIGHT.getConfigKey(),
					Integer.class);

			final int extendedState = c.getParsedValue(ModalConfiguration.GUI_STATE_MAINFRAME_EXTENDED.getConfigKey(),
					Integer.class);

			projWizard.getToolkit().newMainFrame();

			projWizard.getToolkit().getMainFrame().getVisibleModel().setWestVisible(westPanelVisible);
			projWizard.getToolkit().getMainFrame().getVisibleModel().setEastVisible(eastPanelVisible);
			projWizard.getToolkit().getMainFrame().getVisibleModel().setNorthVisible(northPanelVisible);
			projWizard.getToolkit().getMainFrame().getVisibleModel().setSouthVisible(southPanelVisible);

			projWizard.getToolkit().getMainFrame().getVisibleModel().setMaximum(maximum);

			projWizard.getToolkit().getMainFrame().setWestPreferredValue(westPanelSize);
			projWizard.getToolkit().getMainFrame().setEastPreferredValue(eastPanelSize);
			projWizard.getToolkit().getMainFrame().setSouthPreferredValue(southPanelSize);

			projWizard.getToolkit().getMainFrame().setSize(frameWidth, frameHeight);
			projWizard.getToolkit().getMainFrame().setExtendedState(extendedState);

			projWizard.getToolkit().getMainFrame().setLocationRelativeTo(null);
			projWizard.getToolkit().getMainFrame().setVisible(true);
		});

	}

	private InputStream forceOpenInputStream(ResourceKey resourceKey, LoggerStringKey loggerStringKey)
			throws IOException {
		Resource resource = projWizard.getToolkit().getResourceHandler().get(resourceKey.getName());
		try {
			return resource.openInputStream();
		} catch (IOException e) {
			warn(loggerStringKey, e);
			resource.reset();
			return resource.openInputStream();
		}
	}

	private void loadConfig(Processor processor) {
		try {
			processor.loadConfig();
		} catch (ProcessException e) {
			warn(LoggerStringKey.TASK_POSE_24);
			formatWarn(LoggerStringKey.TASK_POSE_25, processor.getKey(), processor.getClass().toString());
			warn(LoggerStringKey.TASK_POSE_26, e);
		}
	}

}
