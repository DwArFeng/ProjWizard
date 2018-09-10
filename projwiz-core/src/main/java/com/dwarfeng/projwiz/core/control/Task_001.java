package com.dwarfeng.projwiz.core.control;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.dutil.basic.io.FileUtil;
import com.dwarfeng.dutil.basic.io.LoadFailedException;
import com.dwarfeng.dutil.develop.i18n.PropUrlI18nInfo;
import com.dwarfeng.dutil.develop.i18n.SyncI18nHandler;
import com.dwarfeng.dutil.develop.i18n.io.XmlPropFileI18nLoader;
import com.dwarfeng.dutil.develop.i18n.io.XmlPropResourceI18nLoader;
import com.dwarfeng.dutil.develop.logger.SyncLoggerHandler;
import com.dwarfeng.dutil.develop.logger.SysOutLoggerInfo;
import com.dwarfeng.dutil.develop.logger.io.Log4jLoggerLoader;
import com.dwarfeng.dutil.develop.resource.Resource;
import com.dwarfeng.dutil.develop.setting.SettingUtil;
import com.dwarfeng.dutil.develop.setting.SyncSettingHandler;
import com.dwarfeng.dutil.develop.setting.io.PropSettingValueLoader;
import com.dwarfeng.projwiz.core.model.eum.CoreConfigItem;
import com.dwarfeng.projwiz.core.model.eum.LoggerStringKey;
import com.dwarfeng.projwiz.core.model.eum.ProjWizProperty;
import com.dwarfeng.projwiz.core.model.eum.ResourceKey;
import com.dwarfeng.projwiz.core.model.eum.ViewConfigItem;
import com.dwarfeng.projwiz.core.model.io.ConfigurationLoader;
import com.dwarfeng.projwiz.core.model.io.IgnoredConfigurationLoader;
import com.dwarfeng.projwiz.core.model.io.IgnoredModuleLoader;
import com.dwarfeng.projwiz.core.model.io.ModuleLoader;
import com.dwarfeng.projwiz.core.model.io.ModuleToolkitLoader;
import com.dwarfeng.projwiz.core.model.io.PluginClassLoader;
import com.dwarfeng.projwiz.core.model.io.ToolkitPermLoader;
import com.dwarfeng.projwiz.core.model.struct.CotoPair;
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
		// 定义变量
		File module_dir;
		File[] module_jars;
		File plugin_dir;
		File[] plugin_jars;
		Collection<String> ignoredCfgKeys;
		Collection<Class<?>> ignoredModuleClazzes;
		File metaDataFileDir;
		boolean isTestCase;
		boolean isForceRestCfg;

		// 是否属于测试环境。
		isTestCase = Boolean.parseBoolean(projWizard.getToolkit().getProperty(ProjWizProperty.TEST_CASE));

		// 用于在读取配置之前对模型进行必要的操作。
		initModel();

		// 向记录器中输出初始化信息。
		infoInitMessage(isTestCase);

		// 解析是否强行重置配置信息。
		isForceRestCfg = Boolean.parseBoolean(projWizard.getToolkit().getProperty(ProjWizProperty.CFG_FORCE_RESET));

		// 加载配置信息。
		loadCfg(isForceRestCfg);

		// 加载记录器资源。
		loadLogger(isTestCase);

		// 加载记录器国际化文件配置。
		loadLoggerI18nFile();

		// 加载记录器国际化资源配置。
		loadLoggerI18nResource();

		// 测试情形下并不加载界面，因此没必要加载标签国际化模型。
		if (!isTestCase) {
			// 加载标签国际化文件配置。
			loadLabelI18nFile();

			// 加载标签国际化文件配置。
			loadLabelI18nResource();
		}

		// 加载核心配置。
		loadCoreConfig();

		// 测试情形下并不加载界面，因此没必要加载视图配置。
		if (!isTestCase) {
			// 加载视图配置。
			loadViewConfig();
		}

		// 加载工具包权限配置。
		loadToolkitPerm();

		// 加载组件-工具包引用模型。
		loadModuleToolkit();

		// 加载配置忽略清单。
		ignoredCfgKeys = new HashSet<>();
		loadCfgIgnore(ignoredCfgKeys);

		// 加载组件忽略清单。
		ignoredModuleClazzes = new HashSet<>();
		loadModuleIgnore(ignoredModuleClazzes);

		// 获取元数据目录。
		metaDataFileDir = new File(projWizard.getToolkit().getProperty(ProjWizProperty.METADATA_PATH));
		FileUtil.createDirIfNotExists(metaDataFileDir);

		// 加载组件文件夹下jar包。
		formatInfo(LoggerStringKey.TASK_POSE_13, projWizard.getToolkit().getProperty(ProjWizProperty.MODULE_PATH));
		module_dir = new File(projWizard.getToolkit().getProperty(ProjWizProperty.MODULE_PATH));
		FileUtil.createDirIfNotExists(module_dir);
		module_jars = FileUtil.listAllFile(module_dir, file -> {
			if (file.getName().endsWith(".jar"))
				return true;
			return false;
		});

		// 加载插件文件夹下jar包。
		formatInfo(LoggerStringKey.TASK_POSE_13, projWizard.getToolkit().getProperty(ProjWizProperty.PLUGIN_PATH));
		plugin_dir = new File(projWizard.getToolkit().getProperty(ProjWizProperty.PLUGIN_PATH));
		FileUtil.createDirIfNotExists(plugin_dir);
		plugin_jars = FileUtil.listAllFile(plugin_dir, file -> {
			if (file.getName().endsWith(".jar"))
				return true;
			return false;
		});

		// 读取组件文件夹下的Jar包。
		loadJars(module_jars);

		// 读取插件文件夹下的Jar包。
		loadJars(plugin_jars);

		// 解析组件文件夹下的jar包中的配置。
		loadJarsCfg(module_jars, ignoredCfgKeys, isForceRestCfg);

		// 解析插件文件夹下的jar包中的配置。
		loadJarsCfg(plugin_jars, ignoredCfgKeys, isForceRestCfg);

		// 解析额外的配置文件中的配置。
		loadExtraCfg(ignoredCfgKeys, isForceRestCfg);

		// 解析并实例化组件文件夹下的jar包中的组件。
		loadJarsModule(module_jars, ignoredModuleClazzes, metaDataFileDir);

		// 解析并实例化插件文件夹下的jar包中的组件。
		loadJarsModule(plugin_jars, ignoredModuleClazzes, metaDataFileDir);

		// 解析并实例化额外组件文件中的组件。
		loadExtraModule(ignoredModuleClazzes, metaDataFileDir);

		// 测试情形下并不加载界面，因此没必要加载界面
		if (!isTestCase) {
			// 生成界面
			initGui();
		}

	}

	/**
	 * 
	 * @param isTestCase
	 *            是否在测试环境下。
	 */
	private void infoInitMessage(boolean isTestCase) {
		if (isTestCase) {
			info(LoggerStringKey.TASK_POSE_27);
		} else {
			info(LoggerStringKey.TASK_POSE_6);
		}
	}

	/**
	 * 生成GUI。
	 */
	private void initGui() {
		try {
			SwingUtil.invokeAndWaitInEventQueue(() -> {
				final SyncSettingHandler c = projWizard.getToolkit().getViewSettingHandler();

				final boolean westPanelVisible = c.getParsedValue(ViewConfigItem.GUI_VISIBLE_MAINFRAME_WEST,
						Boolean.class);
				final boolean eastPanelVisible = c.getParsedValue(ViewConfigItem.GUI_VISIBLE_MAINFRAME_EAST,
						Boolean.class);
				final boolean northPanelVisible = c.getParsedValue(ViewConfigItem.GUI_VISIBLE_MAINFRAME_NORTH,
						Boolean.class);
				final boolean southPanelVisible = c.getParsedValue(ViewConfigItem.GUI_VISIBLE_MAINFRAME_SOUTH,
						Boolean.class);

				final boolean maximum = c.getParsedValue(ViewConfigItem.GUI_MAXIMUM_MAINFRAME, Boolean.class);

				final int westPanelSize = c.getParsedValue(ViewConfigItem.GUI_SIZE_MAINFRAME_WEST, Integer.class);
				final int eastPanelSize = c.getParsedValue(ViewConfigItem.GUI_SIZE_MAINFRAME_EAST, Integer.class);
				final int southPanelSize = c.getParsedValue(ViewConfigItem.GUI_SIZE_MAINFRAME_SOUTH, Integer.class);

				final int frameWidth = c.getParsedValue(ViewConfigItem.GUI_SIZE_MAINFRAME_WIDTH, Integer.class);
				final int frameHeight = c.getParsedValue(ViewConfigItem.GUI_SIZE_MAINFRAME_HEIGHT, Integer.class);

				final int extendedState = c.getParsedValue(ViewConfigItem.GUI_STATE_MAINFRAME_EXTENDED, Integer.class);

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
		} catch (InvocationTargetException | InterruptedException ignore) {
			// 抛异常也要按照基本法。
		}
	}

	/**
	 * 用于在读取配置之前对模型进行必要的操作。
	 */
	private void initModel() {
		SyncLoggerHandler loggerHandler = projWizard.getToolkit().getLoggerHandler();
		SyncI18nHandler loggerI18nHandler = projWizard.getToolkit().getLoggerI18nHandler();

		SysOutLoggerInfo defaultLoggerInfo = new SysOutLoggerInfo(null, false);
		PropUrlI18nInfo defaultI18nInfo = new PropUrlI18nInfo(null, "初始化用国际化配置",
				ProjWizard.class.getResource(Constants.RESOURCE_I18N_LOGGER_PATH));

		loggerHandler.add(defaultLoggerInfo);
		loggerHandler.use(defaultLoggerInfo);
		loggerI18nHandler.add(defaultI18nInfo);
		loggerI18nHandler.setCurrentLocale(null);
	}

	/**
	 * 加载主程序的配置信息。
	 * 
	 * @param isForceRestCfg
	 * 
	 * @throws IOException
	 *             IO异常。
	 */
	private void loadCfg(boolean isForceRestCfg) throws IOException {
		info(LoggerStringKey.TASK_POSE_7);

		// 解析配置列表的URL。
		URL cfgListUrl;

		switch (projWizard.getToolkit().getProperty(ProjWizProperty.CFG_LIST_PATH_TYPE).toUpperCase()) {
		case "INJAR":
			cfgListUrl = ProjWizard.class
					.getResource(projWizard.getToolkit().getProperty(ProjWizProperty.CFG_LIST_PATH));
			break;
		case "FILE":
			cfgListUrl = new File(projWizard.getToolkit().getProperty(ProjWizProperty.CFG_LIST_PATH)).toURI().toURL();
			break;
		default:
			throw new IllegalArgumentException(String.format("无法识别的入口参数: cfg_list_path = %s",
					projWizard.getToolkit().getProperty(ProjWizProperty.CFG_LIST_PATH_TYPE).toUpperCase()));
		}

		loadCfg0(cfgListUrl.openStream(), Collections.emptySet(), isForceRestCfg);
	}

	/**
	 * 加载配置信息。
	 * 
	 * @param in
	 *            指定的输入流。
	 * @param ignoredCfgKeys
	 *            指定的忽略配置字段。
	 * @param isForceRestCfg
	 *            是否强行重置配置文件。
	 * @throws IOException
	 *             IO异常。
	 */
	private void loadCfg0(InputStream in, Collection<String> ignoredCfgKeys, boolean isForceRestCfg)
			throws IOException {
		String repoRootString = projWizard.getToolkit().getProperty(ProjWizProperty.CFGREPO_PATH);
		File reopRoot = new File(repoRootString);

		Set<LoadFailedException> eptSet = new LinkedHashSet<>();
		ConfigurationLoader loader = null;
		try {
			loader = new ConfigurationLoader(in, reopRoot, ignoredCfgKeys, isForceRestCfg);
			eptSet.addAll(loader.countinuousLoad(projWizard.getToolkit().getCfgHandler()));
		} finally {
			if (Objects.nonNull(loader)) {
				loader.close();
			}
		}

		for (LoadFailedException e : eptSet) {
			warn(LoggerStringKey.TASK_POSE_29, e);
		}
		eptSet = null;
		loader = null;
	}

	private void loadCfgIgnore(Collection<String> ignoredCfgKeys) throws IOException {
		info(LoggerStringKey.TASK_POSE_36);

		Set<LoadFailedException> eptSet = new LinkedHashSet<>();
		IgnoredConfigurationLoader loader = null;

		try {
			loader = new IgnoredConfigurationLoader(openResource(ResourceKey.CFG_IGNORE, LoggerStringKey.TASK_POSE_0));
			eptSet.addAll(loader.countinuousLoad(ignoredCfgKeys));
		} finally {
			if (Objects.nonNull(loader)) {
				loader.close();
			}
		}

		for (LoadFailedException e : eptSet) {
			warn(LoggerStringKey.TASK_POSE_37, e);
		}
		eptSet = null;
		loader = null;
	}

	private void loadModule0(InputStream inputStream, PluginClassLoader pluginClassLoader,
			Collection<Class<?>> ignoredModuleClazzes, File metaDataFileDir) throws IOException {
		Set<LoadFailedException> eptSet = new LinkedHashSet<>();
		ModuleLoader loader = null;
		try {
			loader = new ModuleLoader(inputStream, ignoredModuleClazzes, pluginClassLoader,
					projWizard.getToolkit().getToolkitPermModel(), projWizard.getToolkit(), metaDataFileDir);
			eptSet.addAll(loader.countinuousLoad(new CotoPair(projWizard.getToolkit().getModuleModel(),
					projWizard.getToolkit().getModuleToolkitModel())));
		} finally {
			if (Objects.nonNull(loader)) {
				loader.close();
			}
		}

		for (LoadFailedException e : eptSet) {
			warn(LoggerStringKey.TASK_POSE_16, e);
		}
		eptSet = null;
		loader = null;
	}

	private void loadModuleIgnore(Collection<Class<?>> ignoredModuleClazzes) throws IOException {
		info(LoggerStringKey.TASK_POSE_38);

		Set<LoadFailedException> eptSet = new LinkedHashSet<>();
		IgnoredModuleLoader loader = null;

		try {
			loader = new IgnoredModuleLoader(openResource(ResourceKey.MODULE_IGNORE, LoggerStringKey.TASK_POSE_0));
			eptSet.addAll(loader.countinuousLoad(ignoredModuleClazzes));
		} finally {
			if (Objects.nonNull(loader)) {
				loader.close();
			}
		}

		for (LoadFailedException e : eptSet) {
			warn(LoggerStringKey.TASK_POSE_39, e);
		}
		eptSet = null;
		loader = null;
	}

	/**
	 * 加载组件-工具包引用模型。
	 * 
	 * @throws IOException
	 *             IO异常。
	 */
	private void loadModuleToolkit() throws IOException {
		info(LoggerStringKey.TASK_POSE_44);

		Set<LoadFailedException> eptSet = new LinkedHashSet<>();
		ModuleToolkitLoader loader = null;

		try {
			loader = new ModuleToolkitLoader(openResource(ResourceKey.MODULE_TOOLKIT, LoggerStringKey.TASK_POSE_0),
					projWizard.getToolkit().getToolkitPermModel(), projWizard.getToolkit());
			eptSet.addAll(loader.countinuousLoad(projWizard.getToolkit().getModuleToolkitModel()));
		} finally {
			if (Objects.nonNull(loader)) {
				loader.close();
			}
		}

		for (LoadFailedException e : eptSet) {
			warn(LoggerStringKey.TASK_POSE_45, e);
		}
		eptSet = null;
		loader = null;
	}

	/**
	 * 加载核心配置。
	 * 
	 * @throws IOException
	 *             IO异常。
	 */
	private void loadCoreConfig() throws IOException {
		info(LoggerStringKey.TASK_POSE_11);

		SettingUtil.putEnumItems(CoreConfigItem.class, projWizard.getToolkit().getCoreSettingHandlerl());
		Set<LoadFailedException> eptSet = new LinkedHashSet<>();
		try (PropSettingValueLoader loader = new PropSettingValueLoader(
				openResource(ResourceKey.CFG_CORE, LoggerStringKey.TASK_POSE_0), true)) {
			eptSet.addAll(loader.countinuousLoad(projWizard.getToolkit().getCoreSettingHandlerl()));
		}

		for (LoadFailedException e : eptSet) {
			warn(LoggerStringKey.TASK_POSE_3, e);
		}
	}

	/**
	 * 解析额外的配置文件中的配置。
	 * 
	 * @param ignoredCfgKeys
	 *            配置的忽略清单。
	 * @param isForceRestCfg
	 *            是否强行重置配置文件。
	 * @throws IOException
	 *             IO异常。
	 */
	private void loadExtraCfg(Collection<String> ignoredCfgKeys, boolean isForceRestCfg) throws IOException {
		info(LoggerStringKey.TASK_POSE_46);

		loadCfg0(openResource(ResourceKey.CFG_EXTRA, LoggerStringKey.TASK_POSE_0), ignoredCfgKeys, isForceRestCfg);
	}

	/**
	 * 解析并实例化额外组件文件中的组件。
	 * 
	 * @param ignoredModuleClazzes
	 *            组件的忽略清单。
	 * @param metaDataFileDir
	 *            元数据的根目录。
	 * @throws IOException
	 *             IO异常。
	 */
	private void loadExtraModule(Collection<Class<?>> ignoredModuleClazzes, File metaDataFileDir)
			throws IllegalStateException, IOException {
		info(LoggerStringKey.TASK_POSE_47);

		loadModule0(openResource(ResourceKey.MODULE_EXTRA, LoggerStringKey.TASK_POSE_0),
				projWizard.getToolkit().getPluginClassLoader(), ignoredModuleClazzes, metaDataFileDir);
	}

	/**
	 * 加载指定数组中的jar包。
	 * 
	 * @param jars
	 *            指定的数组。
	 */
	private void loadJars(File[] jars) {
		formatInfo(LoggerStringKey.TASK_POSE_15, jars.length);

		for (File jar : jars) {
			formatInfo(LoggerStringKey.TASK_POSE_14, jar.getPath());
			try {
				projWizard.getToolkit().getPluginClassLoader().addURL(jar.toURI().toURL());
			} catch (IllegalStateException | MalformedURLException e) {
				warn(LoggerStringKey.TASK_POSE_24, e);
			}
		}

	}

	/**
	 * 解析指定数组中的jar包下的 cfg-list.xml。
	 * 
	 * @param jars
	 *            指定的数组。
	 * @param ignoredCfgKeys
	 *            配置的忽略清单。
	 * @param isForceRestCfg
	 *            是否强行重置配置文件。
	 * @throws IOException
	 *             IO异常。
	 */
	private void loadJarsCfg(File[] jars, Collection<String> ignoredCfgKeys, boolean isForceRestCfg)
			throws IOException {
		info(LoggerStringKey.TASK_POSE_34);

		for (File jar : jars) {
			formatInfo(LoggerStringKey.TASK_POSE_35, jar.getPath());

			JarFile jarFile = null;
			try {
				jarFile = new JarFile(jar);
				ZipEntry entry = jarFile.getEntry(Constants.CFG_LIST_PATH);
				if (Objects.nonNull(entry)) {
					loadCfg0(jarFile.getInputStream(entry), ignoredCfgKeys, isForceRestCfg);
				}
			} catch (IllegalStateException | MalformedURLException e) {
				warn(LoggerStringKey.TASK_POSE_24, e);
			} finally {
				if (Objects.nonNull(jarFile)) {
					jarFile.close();
				}
			}
		}

	}

	/**
	 * 解析并实例化jar包中的组件。
	 * 
	 * @param jars
	 *            指定的jar包组成的数组。
	 * @param ignoredModuleClazzes
	 *            组件的忽略清单。
	 * @param metaDataFileDir
	 *            元数据的根目录。
	 * @throws IOException
	 *             IO异常。
	 */
	private void loadJarsModule(File[] jars, Collection<Class<?>> ignoredModuleClazzes, File metaDataFileDir)
			throws IOException {
		info(LoggerStringKey.TASK_POSE_40);

		for (File jar : jars) {
			formatInfo(LoggerStringKey.TASK_POSE_41, jar.getPath());

			JarFile jarFile = null;
			try {
				jarFile = new JarFile(jar);
				ZipEntry entry = jarFile.getEntry(Constants.MODULE_LIST_PATH);
				if (Objects.nonNull(entry)) {
					loadModule0(jarFile.getInputStream(entry), projWizard.getToolkit().getPluginClassLoader(),
							ignoredModuleClazzes, metaDataFileDir);
				}
			} catch (IllegalStateException | MalformedURLException e) {
				warn(LoggerStringKey.TASK_POSE_24, e);
			} finally {
				if (Objects.nonNull(jarFile)) {
					jarFile.close();
				}
			}
		}

	}

	/**
	 * 加载标签国际化文件配置。
	 * 
	 * @throws IOException
	 *             IO异常。
	 */
	private void loadLabelI18nFile() throws IOException {
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

	}

	/**
	 * 加载标签国际化文件配置。
	 * 
	 * @throws IOException
	 *             IO异常。
	 */
	private void loadLabelI18nResource() throws IOException {
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
	 * 载记录器资源。
	 * 
	 * @param isTestCase
	 *            是否为测试环境。
	 * @throws IOException
	 *             异常。
	 */
	private void loadLogger(boolean isTestCase) throws IOException {
		ResourceKey resourceKey = null;

		if (isTestCase) {
			resourceKey = ResourceKey.TEST_LOGGER_SETTING;
		} else {
			resourceKey = ResourceKey.CORE_LOGGER_SETTING;
		}

		info(LoggerStringKey.TASK_POSE_8);
		projWizard.getToolkit().getLoggerHandler().unuseKey(null);
		projWizard.getToolkit().getLoggerHandler().removeKey(null);

		Set<LoadFailedException> eptSet = new LinkedHashSet<>();
		Log4jLoggerLoader loader = null;

		try {
			loader = new Log4jLoggerLoader(openResource(resourceKey, LoggerStringKey.TASK_POSE_0));
			eptSet.addAll(loader.countinuousLoad(projWizard.getToolkit().getLoggerHandler()));
		} finally {
			if (Objects.nonNull(loader)) {
				loader.close();
			}
		}

		for (LoadFailedException e : eptSet) {
			warn(LoggerStringKey.TASK_POSE_1, e);
		}
		eptSet = null;
		loader = null;

		projWizard.getToolkit().getLoggerHandler().useAll();
	}

	/**
	 * 加载记录器国际化资源配置。
	 * 
	 * @param isTestCase
	 *            是否为测试情形。
	 * @throws IOException
	 *             IO异常。
	 */
	private void loadLoggerI18nFile() throws IOException {
		info(LoggerStringKey.TASK_POSE_9);

		projWizard.getToolkit().getLoggerI18nHandler().removeKey(null);
		projWizard.getToolkit().getLoggerI18nHandler().setCurrentLocale(null);

		Set<LoadFailedException> eptSet = new LinkedHashSet<>();
		XmlPropFileI18nLoader loader = null;
		try {
			loader = new XmlPropFileI18nLoader(
					openResource(ResourceKey.I18N_LOGGER_FILE_SETTING, LoggerStringKey.TASK_POSE_0));
			eptSet.addAll(loader.countinuousLoad(projWizard.getToolkit().getLoggerI18nHandler()));
		} finally {
			if (Objects.nonNull(loader)) {
				loader.close();
			}
		}

		for (LoadFailedException e : eptSet) {
			warn(LoggerStringKey.TASK_POSE_2, e);
		}
		eptSet = null;
		loader = null;
	}

	/**
	 * 加载记录器国际化资源配置。
	 * 
	 * @throws IOException
	 *             IO异常。
	 */
	private void loadLoggerI18nResource() throws IOException {
		info(LoggerStringKey.TASK_POSE_30);

		Set<LoadFailedException> eptSet = new LinkedHashSet<>();
		XmlPropResourceI18nLoader loader = null;
		try {
			loader = new XmlPropResourceI18nLoader(
					openResource(ResourceKey.I18N_LOGGER_RESOURCE_SETTING, LoggerStringKey.TASK_POSE_0));
			eptSet.addAll(loader.countinuousLoad(projWizard.getToolkit().getLoggerI18nHandler()));
		} finally {
			if (Objects.nonNull(loader)) {
				loader.close();
			}
		}

		for (LoadFailedException e : eptSet) {
			warn(LoggerStringKey.TASK_POSE_31, e);
		}
		eptSet = null;
		loader = null;

		projWizard.getToolkit().getLoggerI18nHandler().setCurrentLocale(null);
	}

	/**
	 * 加载工具包权限配置。
	 * 
	 * @throws IOException
	 *             IO异常。
	 */
	private void loadToolkitPerm() throws IOException {
		info(LoggerStringKey.TASK_POSE_42);

		Set<LoadFailedException> eptSet = new LinkedHashSet<>();
		ToolkitPermLoader loader = null;

		try {
			loader = new ToolkitPermLoader(openResource(ResourceKey.TOOLKIT_PERM, LoggerStringKey.TASK_POSE_0));
			eptSet.addAll(loader.countinuousLoad(projWizard.getToolkit().getToolkitPermModel()));
		} finally {
			if (Objects.nonNull(loader)) {
				loader.close();
			}
		}

		for (LoadFailedException e : eptSet) {
			warn(LoggerStringKey.TASK_POSE_43, e);
		}
		eptSet = null;
		loader = null;
	}

	/**
	 * 加载视图配置。
	 * 
	 * @throws IOException
	 *             IO异常。
	 */
	private void loadViewConfig() throws IOException {
		info(LoggerStringKey.TASK_POSE_12);

		SettingUtil.putEnumItems(ViewConfigItem.class, projWizard.getToolkit().getViewSettingHandler());
		Set<LoadFailedException> eptSet = new LinkedHashSet<>();
		try (PropSettingValueLoader loader = new PropSettingValueLoader(
				openResource(ResourceKey.CFG_VIEW, LoggerStringKey.TASK_POSE_0), true)) {
			eptSet.addAll(loader.countinuousLoad(projWizard.getToolkit().getViewSettingHandler()));
		}

		for (LoadFailedException e : eptSet) {
			warn(LoggerStringKey.TASK_POSE_4, e);
		}
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

}
