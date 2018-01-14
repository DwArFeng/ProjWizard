package com.dwarfeng.projwiz.core.control;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.dutil.basic.io.CT;
import com.dwarfeng.dutil.basic.io.FileUtil;
import com.dwarfeng.dutil.basic.io.LoadFailedException;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.dutil.develop.cfg.ExconfigModel;
import com.dwarfeng.dutil.develop.cfg.io.PropConfigLoader;
import com.dwarfeng.dutil.develop.i18n.I18n;
import com.dwarfeng.dutil.develop.i18n.PropertiesI18n;
import com.dwarfeng.dutil.develop.i18n.io.XmlPropFileI18nLoader;
import com.dwarfeng.dutil.develop.i18n.io.XmlPropResourceI18nLoader;
import com.dwarfeng.dutil.develop.logger.io.Log4jLoggerLoader;
import com.dwarfeng.dutil.develop.resource.Resource;
import com.dwarfeng.dutil.develop.resource.io.XmlJar2RepoResourceLoader;
import com.dwarfeng.projwiz.core.model.eum.LoggerStringKey;
import com.dwarfeng.projwiz.core.model.eum.ModalConfiguration;
import com.dwarfeng.projwiz.core.model.eum.ProjWizProperty;
import com.dwarfeng.projwiz.core.model.eum.ResourceKey;
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

	public static void main(String[] args) {
		CT.trace(ProjWizard.class
				.getResourceAsStream("/com/dwarfeng/projwiz/resources/configuration/core/i18n.logger.setting.xml"));
	}

	public PoseTask(ProjWizard projWizard) {
		super(projWizard);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		// 在记录器与记录器国际化接口加载完毕之前进行的读取操作。
		// 包括以下步骤：
		// -----------------------------------------------------------------------
		//
		// 在记录器与记录器国际化接口加载完毕之前进行的读取操作。
		// 加载所有资源。
		// 加载记录器资源。
		// 加载记录器国际化资源。
		preSet();

		// 加载标签国际化配置。
		loadLabelI18n();

		// 加载核心配置。
		loadCoreConfig();

		// 加载模态配置。
		loadModalConfig();

		// 加载处理器配置。
		info(LoggerStringKey.TASK_POSE_21);
		XmlProcessorConfigLoader processorConfigLoader = null;
		try {
			processorConfigLoader = new XmlProcessorConfigLoader(
					openResource(ResourceKey.PROCESSOR_CONFIG_SETTING, LoggerStringKey.TASK_POSE_0));
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
					openResource(ResourceKey.REFLECT_PROJECT, LoggerStringKey.TASK_POSE_0),
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
					openResource(ResourceKey.REFLECT_FILE, LoggerStringKey.TASK_POSE_0),
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

	/**
	 * 
	 */
	private void loadComponentCfg() {

		// 加载组件配置。
		// 1. 向记录器输出字段。
		// 2. 解析仓库的根文件位置。
		// 3. 初始化URL列表。
		// 4. 添加程序默认的URL。
		// 5. 解析程序参数，生成其它的URL。
		// 6. 依次读取资源，并将所有生成异常的资源记录到LinkHashSet中。
		// 7. 向记录器中输入所有的异常信息。

		// projWizard.getToolkit().info(Constants.I18N_DEFAULT_1);
		//
		// String repoRootString =
		// projWizard.getToolkit().getProperty(ProjWizProperty.CFGREPO_PATH);
		// File reopRoot = new File(repoRootString);
		//
		// List<URL> urls = new ArrayList<>();
		// urls.add(ProjWizard.class.getResource(Constants.CFG_DEFAULT_LIST));
		//
		// String cfgListsString =
		// projWizard.getToolkit().getProperty(ProjWizProperty.CFG_LISTS);
		// for (StringTokenizer st = new StringTokenizer(cfgListsString, ";");
		// st.hasMoreTokens();) {
		// urls.add(ProjWizard.class.getResource(st.nextToken()));
		// }
		//
		// Set<Exception> exceptions = new LinkedHashSet<>();
		//
		// for (URL url : urls) {
		// XmlJar2RepoResourceLoader loader = null;
		// try {
		// loader = new XmlJar2RepoResourceLoader(url.openStream(), reopRoot);
		// exceptions.addAll(loader.countinuousLoad(projWizard.getToolkit().getResourceHandler()));
		// } finally {
		// if (Objects.nonNull(loader)) {
		// loader.close();
		// }
		// }
		// }
		//
		// XmlJar2FileResourceLoader resourceLoader = null;
		// try {
		// resourceLoader = new XmlJar2FileResourceLoader(
		// this.getClass().getResourceAsStream(Constants.RESOURCE_PATH));
		// resourceLoader.load(projWizard.getToolkit().getResourceHandler());
		//
		// } finally {
		// if (Objects.nonNull(resourceLoader)) {
		// resourceLoader.close();
		// }
		// }
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

	/**
	 * 加载核心配置。
	 * 
	 * @throws IOException
	 *             IO异常。
	 */
	private void loadCoreConfig() throws IOException {
		info(LoggerStringKey.TASK_POSE_11);

		Set<LoadFailedException> eptSet = new LinkedHashSet<>();
		PropConfigLoader loader = null;

		try {
			loader = new PropConfigLoader(openResource(ResourceKey.CFG_CORE, LoggerStringKey.TASK_POSE_0));
			eptSet.addAll(loader.countinuousLoad(projWizard.getToolkit().getCoreConfigModel()));
		} finally {
			if (Objects.nonNull(loader)) {
				loader.close();
			}
		}

		for (LoadFailedException e : eptSet) {
			warn(LoggerStringKey.TASK_POSE_3, e);
		}
		eptSet = null;
		loader = null;
	}

	/**
	 * 加载标签国际化资源。
	 * 
	 * @throws IOException
	 *             IO异常。
	 */
	private void loadLabelI18n() throws IOException {
		info(LoggerStringKey.TASK_POSE_10);

		Set<LoadFailedException> eptSet0 = new LinkedHashSet<>();
		XmlPropFileI18nLoader loader0 = null;

		try {
			loader0 = new XmlPropFileI18nLoader(
					openResource(ResourceKey.I18N_LABEL_FILE_SETTING, LoggerStringKey.TASK_POSE_0));
			eptSet0.addAll(loader0.countinuousLoad(projWizard.getToolkit().getLabelI18nHandler()));
		} finally {
			if (Objects.nonNull(loader0)) {
				loader0.close();
			}
		}

		for (LoadFailedException e : eptSet0) {
			warn(LoggerStringKey.TASK_POSE_5, e);
		}
		eptSet0 = null;
		loader0 = null;

		info(LoggerStringKey.TASK_POSE_32);

		Set<LoadFailedException> eptSet1 = new LinkedHashSet<>();
		XmlPropResourceI18nLoader loader1 = null;

		try {
			loader1 = new XmlPropResourceI18nLoader(
					openResource(ResourceKey.I18N_LABEL_RESOURCE_SETTING, LoggerStringKey.TASK_POSE_0));
			eptSet1.addAll(loader1.countinuousLoad(projWizard.getToolkit().getLabelI18nHandler()));
		} finally {
			if (Objects.nonNull(loader1)) {
				loader1.close();
			}
		}

		for (LoadFailedException e : eptSet1) {
			warn(LoggerStringKey.TASK_POSE_33, e);
		}
		eptSet1 = null;
		loader1 = null;

		projWizard.getToolkit().getLabelI18nHandler().setCurrentLocale(null);
	}

	/**
	 * 加载模态配置。
	 * 
	 * @throws IOException
	 *             IO异常。
	 */
	private void loadModalConfig() throws IOException {
		info(LoggerStringKey.TASK_POSE_12);

		Set<LoadFailedException> eptSet = new LinkedHashSet<>();
		PropConfigLoader loader = null;

		try {
			loader = new PropConfigLoader(openResource(ResourceKey.CFG_MODAL, LoggerStringKey.TASK_POSE_0));
			eptSet.addAll(loader.countinuousLoad(projWizard.getToolkit().getModalConfigModel()));
		} finally {
			if (Objects.nonNull(loader)) {
				loader.close();
			}
		}

		for (LoadFailedException e : eptSet) {
			warn(LoggerStringKey.TASK_POSE_4, e);
		}
		eptSet = null;
		loader = null;
	}

	private InputStream openResource(ResourceKey resourceKey, LoggerStringKey loggerStringKey) throws IOException {
		Resource resource = projWizard.getToolkit().getCfgHandler().get(resourceKey.getName());
		try {
			return resource.openInputStream();
		} catch (IOException e) {
			warn(loggerStringKey, e);
			resource.reset();
			return resource.openInputStream();
		}
	}

	/**
	 * 在记录器与记录器国际化接口加载完毕之前进行的读取操作。
	 * 
	 * @throws IOException
	 *             IO异常。
	 */
	private void preSet() throws IOException {
		// -----------------------------------------------------------------------
		// 在记录器与记录器国际化接口加载完毕之前进行的读取操作。
		//
		// 1. 从文件中读取默认的记录器国际化属性文件。
		// 2. 向记录器中输出初始化信息
		// -----------------------------------------------------------------------
		// 加载所有资源。
		//
		// 3. 向记录器输出字段。
		// 4. 解析仓库的根文件位置。
		// 5. 添加主程序 cfg-list 所在文件的URL。
		// 6. 读取资源，并将所有生成异常的资源记录到LinkHashSet中。
		// 7. 向记录器中输出所有的异常信息。
		// -----------------------------------------------------------------------
		// 加载记录器资源。
		//
		// 8.向记录器输入字段。
		// 9.读取配置，并将所有生成异常的资源记录到LinkHashSet中。
		// 10.向记录器中输出所有的异常信息。
		// 10.1.使用所有的记录器。
		// -----------------------------------------------------------------------
		// 加载记录器国际化文件配置。
		//
		// 11.向记录器输入字段。
		// 12.读取配置，并将所有生成异常的资源记录到LinkHashSet中。
		// 13.向记录器中输出所有的异常信息。
		// -----------------------------------------------------------------------
		// 加载记录器国际化资源配置。
		//
		// 14.向记录器输入字段。
		// 15.读取配置，并将所有生成异常的资源记录到LinkHashSet中。
		// 16.向记录器中输出所有的异常信息。
		// 17.记录器国际化处理器使用默认配置。

		// 1
		Properties loggerProperties = new Properties();
		try {
			loggerProperties.load(ProjWizard.class.getResourceAsStream(Constants.RESOURCE_I18N_LOGGER_PATH));
		} catch (IOException e) {
			// 由于该资源在包内，在不破坏包结构的情况下不可能出现异常。
			throw new IOException("无法读取包内的默认国际化资源，请检查包内资源的完整性或者配置路径的正确性。", e);
		}
		final I18n loggerI18n = new PropertiesI18n(loggerProperties);

		// 2
		projWizard.getToolkit().info(preSetGetString(loggerI18n, LoggerStringKey.TASK_POSE_6));

		// 3
		projWizard.getToolkit().info(preSetGetString(loggerI18n, LoggerStringKey.TASK_POSE_7));

		// 4
		String repoRootString = projWizard.getToolkit().getProperty(ProjWizProperty.CFGREPO_PATH);
		File reopRoot = new File(repoRootString);

		// 5
		URL cfgListUrl = ProjWizard.class.getResource(Constants.CFG_DEFAULT_LIST_PATH);

		// 6
		Set<LoadFailedException> eptSet0 = new LinkedHashSet<>();
		XmlJar2RepoResourceLoader loader0 = null;
		try {
			loader0 = new XmlJar2RepoResourceLoader(cfgListUrl.openStream(), reopRoot);
			eptSet0.addAll(loader0.countinuousLoad(projWizard.getToolkit().getCfgHandler()));
		} finally {
			if (Objects.nonNull(loader0)) {
				loader0.close();
			}
		}

		// 7
		for (LoadFailedException e : eptSet0) {
			projWizard.getToolkit().warn(preSetGetString(loggerI18n, LoggerStringKey.TASK_POSE_29), e);
		}
		eptSet0 = null;
		loader0 = null;

		// 8
		projWizard.getToolkit().info(preSetGetString(loggerI18n, LoggerStringKey.TASK_POSE_8));

		// 9
		Set<LoadFailedException> eptSet1 = new LinkedHashSet<>();
		Log4jLoggerLoader loader1 = null;
		try {
			loader1 = new Log4jLoggerLoader(
					preSetOpenResource(ResourceKey.LOGGER_SETTING, loggerI18n, LoggerStringKey.TASK_POSE_0));
			eptSet1.addAll(loader1.countinuousLoad(projWizard.getToolkit().getLoggerHandler()));
		} finally {
			if (Objects.nonNull(loader1)) {
				loader1.close();
			}
		}

		// 10
		for (LoadFailedException e : eptSet1) {
			projWizard.getToolkit().warn(preSetGetString(loggerI18n, LoggerStringKey.TASK_POSE_1), e);
		}
		eptSet1 = null;
		loader1 = null;

		// 10.1
		projWizard.getToolkit().getLoggerHandler().useAll();

		// 11
		projWizard.getToolkit().info(preSetGetString(loggerI18n, LoggerStringKey.TASK_POSE_9));

		// 12
		Set<LoadFailedException> eptSet2 = new LinkedHashSet<>();
		XmlPropFileI18nLoader loader2 = null;
		try {
			loader2 = new XmlPropFileI18nLoader(
					preSetOpenResource(ResourceKey.I18N_LOGGER_FILE_SETTING, loggerI18n, LoggerStringKey.TASK_POSE_0));
			eptSet2.addAll(loader2.countinuousLoad(projWizard.getToolkit().getLoggerI18nHandler()));
		} finally {
			if (Objects.nonNull(loader2)) {
				loader2.close();
			}
		}

		// 13
		for (LoadFailedException e : eptSet2) {
			projWizard.getToolkit().warn(preSetGetString(loggerI18n, LoggerStringKey.TASK_POSE_2), e);
		}
		eptSet2 = null;
		loader2 = null;

		// 14
		projWizard.getToolkit().info(preSetGetString(loggerI18n, LoggerStringKey.TASK_POSE_30));

		// 15
		Set<LoadFailedException> eptSet3 = new LinkedHashSet<>();
		XmlPropResourceI18nLoader loader3 = null;
		try {
			loader3 = new XmlPropResourceI18nLoader(preSetOpenResource(ResourceKey.I18N_LOGGER_RESOURCE_SETTING,
					loggerI18n, LoggerStringKey.TASK_POSE_0));
			eptSet3.addAll(loader3.countinuousLoad(projWizard.getToolkit().getLoggerI18nHandler()));
		} finally {
			if (Objects.nonNull(loader3)) {
				loader3.close();
			}
		}

		// 16
		for (LoadFailedException e : eptSet3) {
			projWizard.getToolkit().warn(preSetGetString(loggerI18n, LoggerStringKey.TASK_POSE_31), e);
		}
		eptSet3 = null;
		loader3 = null;

		// 17
		projWizard.getToolkit().getLoggerI18nHandler().setCurrentLocale(null);
	}

	/**
	 * 根据指定的国际化接口以及指定的记录器文本键生成国际化文本字段。
	 * 
	 * @param i18n
	 *            指定的国际化接口。
	 * @param loggerStringKey
	 *            指定的记录器文本键。
	 * @return 根据指定的国际化几口以及指定的记录器文本键生成的国际化文本字段。
	 */
	private String preSetGetString(I18n i18n, LoggerStringKey loggerStringKey) {
		return i18n.getStringOrDefault(loggerStringKey.getName(), Constants.MISSING_LABEL);
	}

	/**
	 * 尝试打开指定的资源，当打开资源失败时，尝试重置资源并输出警告，并再次打开。
	 * 
	 * @param resourceKey
	 *            指定的资源键。
	 * @param i18n
	 *            输出警告所需的国际化接口。
	 * @param loggerStringKey
	 *            输出警告所需的记录器文本键。
	 * @return 指定资源的输入流。
	 * @throws IOException
	 *             重置过程或再次打开资源时发生的异常。
	 */
	private InputStream preSetOpenResource(ResourceKey resourceKey, I18n i18n, LoggerStringKey loggerStringKey)
			throws IOException {
		Resource resource = projWizard.getToolkit().getCfgHandler().get(resourceKey.getName());
		try {
			return resource.openInputStream();
		} catch (IOException e) {
			projWizard.getToolkit().warn(preSetGetString(i18n, loggerStringKey), e);
			resource.reset();
			return resource.openInputStream();
		}
	}

}
