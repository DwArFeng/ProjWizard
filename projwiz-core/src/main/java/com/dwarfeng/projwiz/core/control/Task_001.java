package com.dwarfeng.projwiz.core.control;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
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
import com.dwarfeng.dutil.develop.cfg.ExconfigModel;
import com.dwarfeng.dutil.develop.cfg.io.PropConfigLoader;
import com.dwarfeng.dutil.develop.i18n.PropUrlI18nInfo;
import com.dwarfeng.dutil.develop.i18n.SyncI18nHandler;
import com.dwarfeng.dutil.develop.i18n.io.XmlPropFileI18nLoader;
import com.dwarfeng.dutil.develop.i18n.io.XmlPropResourceI18nLoader;
import com.dwarfeng.dutil.develop.logger.SyncLoggerHandler;
import com.dwarfeng.dutil.develop.logger.SysOutLoggerInfo;
import com.dwarfeng.dutil.develop.logger.io.Log4jLoggerLoader;
import com.dwarfeng.dutil.develop.resource.Resource;
import com.dwarfeng.projwiz.core.model.eum.LoggerStringKey;
import com.dwarfeng.projwiz.core.model.eum.ProjWizProperty;
import com.dwarfeng.projwiz.core.model.eum.ResourceKey;
import com.dwarfeng.projwiz.core.model.eum.ViewConfiguration;
import com.dwarfeng.projwiz.core.model.io.ComponentLoader;
import com.dwarfeng.projwiz.core.model.io.ConfigurationLoader;
import com.dwarfeng.projwiz.core.model.io.IgnoredComponentLoader;
import com.dwarfeng.projwiz.core.model.io.IgnoredConfigurationLoader;
import com.dwarfeng.projwiz.core.model.io.PluginClassLoader;
import com.dwarfeng.projwiz.core.model.io.ToolkitPermLoader;
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
		File cmpoent_dir;
		File[] cmpoent_jars;
		File plugin_dir;
		File[] plugin_jars;
		Collection<String> ignoreCfgKeys;
		Collection<String> ignoreCmpoentKeys;
		boolean isTestCase;

		// 是否属于测试环境
		isTestCase = Boolean.parseBoolean(projWizard.getToolkit().getProperty(ProjWizProperty.TEST_CASE));

		// 用于在读取配置之前对模型进行必要的操作。
		initModel();

		// 向记录器中输出初始化信息
		infoInitMessage(isTestCase);

		// 加载配置信息。
		loadCfg();

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

		// 加载配置忽略清单。
		ignoreCfgKeys = new HashSet<>();
		loadCfgIgnore(ignoreCfgKeys);

		// 加载组件忽略清单。
		ignoreCmpoentKeys = new HashSet<>();
		loadCmpoentIgnore(ignoreCmpoentKeys);

		// 加载组件文件夹下jar包。
		formatInfo(LoggerStringKey.TASK_POSE_13, projWizard.getToolkit().getProperty(ProjWizProperty.COMPONENT_PATH));
		cmpoent_dir = new File(projWizard.getToolkit().getProperty(ProjWizProperty.COMPONENT_PATH));
		FileUtil.createDirIfNotExists(cmpoent_dir);
		cmpoent_jars = FileUtil.listAllFile(cmpoent_dir, file -> {
			if (file.getName().endsWith(".jar"))
				return true;
			return false;
		});

		// 读取组件文件夹下的Jar包。
		loadJars(cmpoent_jars);

		// 解析组件文件夹下的jar包中的配置。
		loadJarsCfg(cmpoent_jars, ignoreCfgKeys);

		// 解析并实例化组件文件夹下的jar包中的组件。
		loadJarsCmpoent(cmpoent_jars, ignoreCmpoentKeys);

		// 加载插件文件夹下jar包。
		formatInfo(LoggerStringKey.TASK_POSE_13, projWizard.getToolkit().getProperty(ProjWizProperty.PLUGIN_PATH));
		plugin_dir = new File(projWizard.getToolkit().getProperty(ProjWizProperty.PLUGIN_PATH));
		FileUtil.createDirIfNotExists(plugin_dir);
		plugin_jars = FileUtil.listAllFile(plugin_dir, file -> {
			if (file.getName().endsWith(".jar"))
				return true;
			return false;
		});

		// 读取插件文件夹下的Jar包。
		loadJars(plugin_jars);

		// 解析插件文件夹下的jar包中的配置。
		loadJarsCfg(plugin_jars, ignoreCfgKeys);

		// 解析并实例化插件文件夹下的jar包中的组件。
		loadJarsCmpoent(plugin_jars, ignoreCmpoentKeys);

		// 测试情形下并不加载界面，因此没必要加载界面
		if (!isTestCase) {
			// 生成界面
			initGui();
		}

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
			warn(LoggerStringKey.TASK_POSE_3, e);
		}
		eptSet = null;
		loader = null;
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
				final ExconfigModel c = projWizard.getToolkit().getViewConfigModel();

				final boolean westPanelVisible = c
						.getParsedValue(ViewConfiguration.GUI_VISIBLE_MAINFRAME_WEST.getConfigKey(), Boolean.class);
				final boolean eastPanelVisible = c
						.getParsedValue(ViewConfiguration.GUI_VISIBLE_MAINFRAME_EAST.getConfigKey(), Boolean.class);
				final boolean northPanelVisible = c
						.getParsedValue(ViewConfiguration.GUI_VISIBLE_MAINFRAME_NORTH.getConfigKey(), Boolean.class);
				final boolean southPanelVisible = c
						.getParsedValue(ViewConfiguration.GUI_VISIBLE_MAINFRAME_SOUTH.getConfigKey(), Boolean.class);

				final boolean maximum = c.getParsedValue(ViewConfiguration.GUI_MAXIMUM_MAINFRAME.getConfigKey(),
						Boolean.class);

				final int westPanelSize = c.getParsedValue(ViewConfiguration.GUI_SIZE_MAINFRAME_WEST.getConfigKey(),
						Integer.class);
				final int eastPanelSize = c.getParsedValue(ViewConfiguration.GUI_SIZE_MAINFRAME_EAST.getConfigKey(),
						Integer.class);
				final int southPanelSize = c.getParsedValue(ViewConfiguration.GUI_SIZE_MAINFRAME_SOUTH.getConfigKey(),
						Integer.class);

				final int frameWidth = c.getParsedValue(ViewConfiguration.GUI_SIZE_MAINFRAME_WIDTH.getConfigKey(),
						Integer.class);
				final int frameHeight = c.getParsedValue(ViewConfiguration.GUI_SIZE_MAINFRAME_HEIGHT.getConfigKey(),
						Integer.class);

				final int extendedState = c
						.getParsedValue(ViewConfiguration.GUI_STATE_MAINFRAME_EXTENDED.getConfigKey(), Integer.class);

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
	 * 加载配置信息。
	 * 
	 * @throws IOException
	 *             IO异常。
	 */
	private void loadCfg() throws IOException {
		info(LoggerStringKey.TASK_POSE_7);
		loadCfg0(ProjWizard.class.getResource(Constants.CFG_DEFAULT_LIST_PATH).openStream(), Collections.emptySet());
	}

	private void loadCfg0(InputStream in, Collection<String> ignoreCfgKeys) throws IOException {
		String repoRootString = projWizard.getToolkit().getProperty(ProjWizProperty.CFGREPO_PATH);
		File reopRoot = new File(repoRootString);

		Set<LoadFailedException> eptSet = new LinkedHashSet<>();
		ConfigurationLoader loader = null;
		try {
			loader = new ConfigurationLoader(in, reopRoot, ignoreCfgKeys, true);
			eptSet.addAll(loader.countinuousLoad(projWizard.getToolkit().getCfgHandler()));
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

	private void loadCfgIgnore(Collection<String> ignoreCfgKeys) throws IOException {
		info(LoggerStringKey.TASK_POSE_36);

		Set<LoadFailedException> eptSet = new LinkedHashSet<>();
		IgnoredConfigurationLoader loader = null;

		try {
			loader = new IgnoredConfigurationLoader(openResource(ResourceKey.CFG_IGNORE, LoggerStringKey.TASK_POSE_0));
			eptSet.addAll(loader.countinuousLoad(ignoreCfgKeys));
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

	private void loadCmpoent0(InputStream inputStream, PluginClassLoader pluginClassLoader,
			Collection<String> ignoreCmpoentKeys) throws IOException {
		Set<LoadFailedException> eptSet = new LinkedHashSet<>();
		ComponentLoader loader = null;
		try {
			loader = new ComponentLoader(inputStream, ignoreCmpoentKeys, pluginClassLoader);
			eptSet.addAll(loader.countinuousLoad(projWizard.getToolkit().getComponentModel()));
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

	private void loadCmpoentIgnore(Collection<String> ignoreCmpoentKeys) throws IOException {
		info(LoggerStringKey.TASK_POSE_38);

		Set<LoadFailedException> eptSet = new LinkedHashSet<>();
		IgnoredComponentLoader loader = null;

		try {
			loader = new IgnoredComponentLoader(openResource(ResourceKey.CMPOENT_IGNORE, LoggerStringKey.TASK_POSE_0));
			eptSet.addAll(loader.countinuousLoad(ignoreCmpoentKeys));
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
	 * @throws IOException
	 *             IO异常。
	 */
	private void loadJarsCfg(File[] jars, Collection<String> ignoreCfgKeys) throws IOException {
		info(LoggerStringKey.TASK_POSE_34);

		for (File jar : jars) {
			formatInfo(LoggerStringKey.TASK_POSE_35, jar.getPath());

			JarFile jarFile = null;
			try {
				jarFile = new JarFile(jar);
				ZipEntry entry = jarFile.getEntry(Constants.CFG_LIST_PATH);
				if (Objects.nonNull(entry)) {
					loadCfg0(jarFile.getInputStream(entry), ignoreCfgKeys);
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
	 * @param ignoreCmpoentKeys
	 *            组件的忽略清单。
	 * @throws IOException
	 *             IO异常。
	 */
	private void loadJarsCmpoent(File[] jars, Collection<String> ignoreCmpoentKeys) throws IOException {
		info(LoggerStringKey.TASK_POSE_40);

		for (File jar : jars) {
			formatInfo(LoggerStringKey.TASK_POSE_41, jar.getPath());

			JarFile jarFile = null;
			try {
				jarFile = new JarFile(jar);
				ZipEntry entry = jarFile.getEntry(Constants.CMPOENT_LIST_PATH);
				if (Objects.nonNull(entry)) {
					loadCmpoent0(jarFile.getInputStream(entry), projWizard.getToolkit().getPluginClassLoader(),
							ignoreCmpoentKeys);
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
	 * 加载视图配置。
	 * 
	 * @throws IOException
	 *             IO异常。
	 */
	private void loadViewConfig() throws IOException {
		info(LoggerStringKey.TASK_POSE_12);

		Set<LoadFailedException> eptSet = new LinkedHashSet<>();
		PropConfigLoader loader = null;

		try {
			loader = new PropConfigLoader(openResource(ResourceKey.CFG_VIEW, LoggerStringKey.TASK_POSE_0));
			eptSet.addAll(loader.countinuousLoad(projWizard.getToolkit().getViewConfigModel()));
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

}
