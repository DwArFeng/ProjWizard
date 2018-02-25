package com.dwarfeng.projwiz.raefrm;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.imageio.ImageIO;

import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.dutil.basic.io.LoadFailedException;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.dutil.basic.str.Name;
import com.dwarfeng.dutil.develop.cfg.ConfigKey;
import com.dwarfeng.dutil.develop.cfg.ConfigUtil;
import com.dwarfeng.dutil.develop.cfg.DefaultExconfigModel;
import com.dwarfeng.dutil.develop.cfg.SyncExconfigModel;
import com.dwarfeng.dutil.develop.cfg.io.PropConfigLoader;
import com.dwarfeng.dutil.develop.cfg.obv.ExconfigAdapter;
import com.dwarfeng.dutil.develop.cfg.obv.ExconfigObverser;
import com.dwarfeng.dutil.develop.cfg.struct.ExconfigEntry;
import com.dwarfeng.dutil.develop.i18n.DelegateI18nHandler;
import com.dwarfeng.dutil.develop.i18n.I18nUtil;
import com.dwarfeng.dutil.develop.i18n.PropUrlI18nInfo;
import com.dwarfeng.dutil.develop.i18n.SyncI18nHandler;
import com.dwarfeng.dutil.develop.i18n.io.XmlPropFileI18nLoader;
import com.dwarfeng.dutil.develop.i18n.io.XmlPropResourceI18nLoader;
import com.dwarfeng.dutil.develop.resource.Resource;
import com.dwarfeng.dutil.develop.resource.ResourceHandler;
import com.dwarfeng.projwiz.core.model.eum.CoreConfigEntry;
import com.dwarfeng.projwiz.core.model.eum.IconVariability;
import com.dwarfeng.projwiz.core.model.struct.Component;
import com.dwarfeng.projwiz.core.model.struct.MetaDataStorage;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;
import com.dwarfeng.projwiz.raefrm.model.cm.DelegatePermDemandModel;
import com.dwarfeng.projwiz.raefrm.model.cm.SyncPermDemandModel;
import com.dwarfeng.projwiz.raefrm.model.eum.ConfigEntry;
import com.dwarfeng.projwiz.raefrm.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.raefrm.model.eum.LoggerStringKey;
import com.dwarfeng.projwiz.raefrm.model.io.XmlPermDemandLoader;
import com.dwarfeng.projwiz.raefrm.model.struct.ConstantsProvider;
import com.dwarfeng.projwiz.raefrm.model.struct.ConstantsProvider.ResourceKeyType;
import com.dwarfeng.projwiz.raefrm.util.Constants;
import com.dwarfeng.projwiz.raefrm.util.ModelUtil;

/**
 * Rae框架组件。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public abstract class RaeComponent implements Component {

	/** 该组件使用的工具包引用。 */
	protected final ReferenceModel<? extends Toolkit> toolkitRef;
	/** 该组件使用的元数据仓库。 */
	protected final MetaDataStorage metaDataStorage;

	/** 该组件的同步读写锁。 */
	protected final ReadWriteLock lock = new ReentrantReadWriteLock();

	/** 常量提供器 */
	protected final ConstantsProvider constantsProvider;

	/** 记录器国际化处理器。 */
	protected final SyncI18nHandler loggerI18nHandler = I18nUtil.syncI18nHandler(new DelegateI18nHandler());
	/** 标签国际化处理器。 */
	protected final SyncI18nHandler labelI18nHandler = I18nUtil.syncI18nHandler(new DelegateI18nHandler());
	/** 配置处理器。 */
	protected final SyncExconfigModel configModel = ConfigUtil.syncExconfigModel(new DefaultExconfigModel());
	/** 权限需求模型。 */
	protected final SyncPermDemandModel permDemandModel = ModelUtil.syncPermDemandModel(new DelegatePermDemandModel());

	private final ExconfigObverser coreConfigObverser = new ExconfigAdapter() {

		@Override
		public void fireCurrentValueChanged(ConfigKey configKey, String oldValue, String newValue, String validValue) {
			if (Objects.equals(configKey, CoreConfigEntry.I18N_LOGGER.getConfigKey())) {
				chg_I18N_LOGGER(oldValue, newValue, validValue);
			}

			if (Objects.equals(configKey, CoreConfigEntry.I18N_LABEL.getConfigKey())) {
				chg_I18N_LABEL(oldValue, newValue, validValue);
			}
		}

		private void chg_I18N_LABEL(String oldValue, String newValue, String validValue) {
			labelI18nHandler.setCurrentLocale(getToolkit().getCoreConfigModelReadOnly()
					.getParsedValue(CoreConfigEntry.I18N_LABEL.getConfigKey(), Locale.class));
		}

		private void chg_I18N_LOGGER(String oldValue, String newValue, String validValue) {
			loggerI18nHandler.setCurrentLocale((Locale) getToolkit().getCoreConfigModelReadOnly()
					.getParsedValue(CoreConfigEntry.I18N_LOGGER.getConfigKey()));
		}

	};

	protected RaeComponent(ReferenceModel<? extends Toolkit> toolkitRef, MetaDataStorage metaDataStorage,
			ConstantsProvider constantsProvider) throws ProcessException {
		Objects.requireNonNull(toolkitRef, "入口参数 toolkitRef 不能为 null。");
		Objects.requireNonNull(metaDataStorage, "入口参数 metaDataStorage 不能为 null。");
		Objects.requireNonNull(constantsProvider, "入口参数 constantsProvider 不能为 null。");

		this.toolkitRef = toolkitRef;
		this.metaDataStorage = metaDataStorage;
		this.constantsProvider = constantsProvider;

		try {
			init();
		} catch (Exception e) {
			throw new ProcessException("初始化组件时发生异常", e);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		// TODO 保存配置文件。
		saveConfigModel();

		getToolkit().removeCoreConfigObverser(coreConfigObverser);
		loggerI18nHandler.clear();
		labelI18nHandler.clear();
		configModel.clear();
		permDemandModel.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDescription() {
		return labelI18nHandler.getStringOrDefault(LabelStringKey.RAEFRM_CMPOENT_DESCRIPTION,
				com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Image getIcon() {
		Image icon = null;
		try {
			icon = ImageIO.read(openResource(constantsProvider.getResourceKey(ResourceKeyType.IMAGE_CMPOENT)));
		} catch (Exception e) {
			icon = null;
		}

		if (Objects.isNull(icon)) {
			icon = com.dwarfeng.projwiz.core.util.Constants.IMAGE_LOAD_FAILED;
		}

		return icon;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IconVariability getIconVarialibity() {
		return IconVariability.FIX;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReadWriteLock getLock() {
		return lock;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return labelI18nHandler.getStringOrDefault(LabelStringKey.RAEFRM_CMPOENT_NAME,
				com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL);
	}

	/**
	 * 向记录器中格式化输出一条信息。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @param args
	 *            参数。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void formatInfo(Name name, Object... args) {
		Objects.requireNonNull(name, "入口参数 loggerStringKey 不能为 null。");
		Objects.requireNonNull(args, "入口参数 args 不能为 null。");

		getToolkit().info(String.format(
				loggerI18nHandler.getStringOrDefault(name, com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL),
				args));
	}

	/**
	 * 向记录器中格式化输出一条警告。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @param args
	 *            参数。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void formatWarn(Name name, Object... args) {
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");
		Objects.requireNonNull(args, "入口参数 args 不能为 null。");

		getToolkit().warn(String.format(
				loggerI18nHandler.getStringOrDefault(name, com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL),
				args));
	}

	/**
	 * 向记录器中格式化输出一条警告。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @param e
	 *            指定的可抛出对象。
	 * @param args
	 *            参数。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void formatWarn(Name name, Throwable e, Object... args) {
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");
		Objects.requireNonNull(e, "入口参数 e 不能为 null。");
		Objects.requireNonNull(args, "入口参数 args 不能为 null。");

		getToolkit().warn(String.format(
				loggerI18nHandler.getStringOrDefault(name, com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL),
				args), e);
	}

	/**
	 * 获取组件的工具包。
	 * 
	 * @return 组件的工具包。
	 */
	protected final Toolkit getToolkit() {
		return toolkitRef.get();
	}

	/**
	 * 向记录器中输出一条信息。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void info(Name name) {
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");

		getToolkit().info(
				loggerI18nHandler.getStringOrDefault(name, com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL));
	}

	/**
	 * 返回此组件的工具包是否不满足指定的权限键对应的所有需求。
	 * 
	 * @param permKey
	 *            指定的权限键。
	 * @return 此组件的工具包是否不满足指定的权限键对应的所有需求。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected boolean isNotPermKeyAvaliable(Name permKey) {
		Objects.requireNonNull(permKey, "入口参数 permKey 不能为 null。");
		return permDemandModel.isNotPermKeyAvaliable(permKey, getToolkit());
	}

	/**
	 * 返回此组件的工具包是否能满足指定的权限键对应的所有需求。
	 * 
	 * @param permKey
	 *            含有指定的权限键的名称对象。
	 * @return 此组件的工具包是否能满足指定的权限键对应的所有需求。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected boolean isPermKeyAvailable(Name permKey) {
		Objects.requireNonNull(permKey, "入口参数 permKey 不能为 null。");
		return permDemandModel.isPermKeyAvailable(permKey, getToolkit());
	}

	/**
	 * 打开指定资源键对应的资源。
	 * 
	 * <p>
	 * 该方法通过 Toolkit 获取只读配置处理器，随后获取指定资源键对应的资源，在资源自动重置之后，打开输入流。
	 * 
	 * @param resourceKey
	 *            指定的资源键对应的名称接口。
	 * @return 指定资源对应的资源键。
	 * @throws IOException
	 *             IO异常。
	 */
	protected InputStream openResource(Name resourceKey) throws IOException {
		return openResource(resourceKey, true);
	}

	/**
	 * 打开指定资源键对应的资源。
	 * 
	 * <p>
	 * 该方法通过 Toolkit 获取只读配置处理器，随后获取指定资源键对应的资源，打开输入流。
	 * 
	 * @param resourceKey
	 *            指定的资源键对应的名称接口。
	 * @param autoReset
	 *            是否自动重置。
	 * @return 指定资源对应的资源键。
	 * @throws IOException
	 *             IO异常。
	 */
	protected InputStream openResource(Name resourceKey, boolean autoReset) throws IOException {
		Objects.requireNonNull(resourceKey, "入口参数 resourceKey 不能为 null。");

		ResourceHandler cfgHandlerReadOnly = getToolkit().getCfgHandlerReadOnly();
		if (!cfgHandlerReadOnly.containsKey(resourceKey.getName())) {
			throw new IOException(String.format("不存在指定的资源: %s", resourceKey.getName()));
		}

		Resource resource = cfgHandlerReadOnly.get(resourceKey.getName());
		if (autoReset) {
			resource.autoReset();
		}
		return resource.openInputStream();
	}

	/**
	 * 向记录器中输出一条信息。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void warn(Name name) {
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");

		getToolkit().warn(
				loggerI18nHandler.getStringOrDefault(name, com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL));
	}

	/**
	 * 向记录器中输出一条警告。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @param e
	 *            指定的可抛出对象。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void warn(Name name, Throwable e) {
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");

		getToolkit().warn(
				loggerI18nHandler.getStringOrDefault(name, com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL), e);
	}

	/**
	 * 检查基本权限。
	 */
	private void checkBasicPerm() {
		for (Toolkit.Method method : Constants.BASIC_PERMS) {
			checkPerm(method);
		}
	}

	/**
	 * 检查该组件的工具包是否拥有指定的方法的权限。
	 * 
	 * @param method
	 *            指定的方法
	 * @throws IllegalStateException
	 *             不具有权限时抛出的异常。
	 */
	private void checkPerm(Toolkit.Method method) throws IllegalStateException {
		if (!getToolkit().hasPermission(method)) {
			throw new IllegalStateException("组件的工具包不具备指定的权限" + method.toString());
		}
	}

	/**
	 * 初始化Rae框架。
	 * 
	 * @throws Exception
	 *             异常。
	 */
	private void init() throws Exception {
		// 用于在读取配置之前对模型进行必要的操作。
		initModel();

		// 检查基本权限。
		checkBasicPerm();

		// 注册核心配置侦听
		getToolkit().addCoreConfigObverser(coreConfigObverser);

		// 输出初始化信息。
		getToolkit().info(loggerI18nHandler.getString(LoggerStringKey.RAE_CMPOENT_INIT_0));

		// 加载记录器国际化文件配置。
		loadLoggerI18nFile();

		// 加载记录器国际化资源配置。
		loadLoggerI18nResource();

		// 初始化配置模型入口。
		initConfigEntry();

		// 加载配置模型。
		loadConfigModel();

		// 读取权限需求表。
		loadPermDemandModel();

		// 加载标签国际化文件配置。
		loadLabelI18nFile();

		// 加载标签国际化文件配置。
		loadLabelI18nResource();

	}

	/**
	 * 初始化配置模型入口。
	 */
	private void initConfigEntry() {
		configModel.addAll(Arrays.asList(ConfigEntry.values()));
		Collection<ExconfigEntry> configEntries = constantsProvider.getConfigEntries();
		if (Objects.nonNull(configEntries)) {
			configModel.addAll(configEntries);
		}
	}

	/**
	 * 用于在读取配置之前对模型进行必要的操作。
	 */
	private void initModel() {
		PropUrlI18nInfo defaultI18nInfo = new PropUrlI18nInfo(null, "初始化用国际化配置",
				RaeComponent.class.getResource(constantsProvider.getDefaultLoggerI18nPath()));

		loggerI18nHandler.add(defaultI18nInfo);
		loggerI18nHandler.setCurrentLocale(null);
	}

	/**
	 * 加载配置模型。
	 * 
	 * @throws IOException
	 *             IO异常。
	 */
	private void loadConfigModel() throws IOException {
		info(LoggerStringKey.RAE_CMPOENT_INIT_1);

		Set<LoadFailedException> eptSet = new LinkedHashSet<>();
		PropConfigLoader loader = null;

		try {
			loader = new PropConfigLoader(
					openResource(constantsProvider.getResourceKey(ResourceKeyType.CONFIGURATION_CORE)));
			eptSet.addAll(loader.countinuousLoad(configModel));
		} finally {
			if (Objects.nonNull(loader)) {
				loader.close();
			}
		}

		for (LoadFailedException e : eptSet) {
			warn(LoggerStringKey.RAE_CMPOENT_INIT_12, e);
		}
		eptSet = null;
		loader = null;
	}

	/**
	 * 加载标签国际化文件配置。
	 * 
	 * @throws IOException
	 *             IO异常。
	 */
	private void loadLabelI18nFile() throws IOException {
		info(LoggerStringKey.RAE_CMPOENT_INIT_6);

		Set<LoadFailedException> eptSet0 = new LinkedHashSet<>();
		XmlPropFileI18nLoader loader0 = null;

		try {
			loader0 = new XmlPropFileI18nLoader(
					openResource(constantsProvider.getResourceKey(ResourceKeyType.I18N_LABEL_FILE_SETTING)));
			eptSet0.addAll(loader0.countinuousLoad(labelI18nHandler));
		} finally {
			if (Objects.nonNull(loader0)) {
				loader0.close();
			}
		}

		for (LoadFailedException e : eptSet0) {
			warn(LoggerStringKey.RAE_CMPOENT_INIT_7, e);
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
		info(LoggerStringKey.RAE_CMPOENT_INIT_8);

		Set<LoadFailedException> eptSet1 = new LinkedHashSet<>();
		XmlPropResourceI18nLoader loader1 = null;

		try {
			loader1 = new XmlPropResourceI18nLoader(
					openResource(constantsProvider.getResourceKey(ResourceKeyType.I18N_LABEL_RESOURCE_SETTING)));
			eptSet1.addAll(loader1.countinuousLoad(labelI18nHandler));
		} finally {
			if (Objects.nonNull(loader1)) {
				loader1.close();
			}
		}

		for (LoadFailedException e : eptSet1) {
			warn(LoggerStringKey.RAE_CMPOENT_INIT_9, e);
		}
		eptSet1 = null;
		loader1 = null;

		labelI18nHandler.setCurrentLocale(null);
		labelI18nHandler.setCurrentLocale(getToolkit().getCoreConfigModelReadOnly()
				.getParsedValue(CoreConfigEntry.I18N_LABEL.getConfigKey(), Locale.class));
	}

	/**
	 * 加载记录器国际化文件配置。
	 * 
	 * @throws IOException
	 */
	private void loadLoggerI18nFile() throws IOException {
		info(LoggerStringKey.RAE_CMPOENT_INIT_2);

		loggerI18nHandler.removeKey(null);
		loggerI18nHandler.setCurrentLocale(null);

		Set<LoadFailedException> eptSet = new LinkedHashSet<>();
		XmlPropFileI18nLoader loader = null;
		try {
			loader = new XmlPropFileI18nLoader(
					openResource(constantsProvider.getResourceKey(ResourceKeyType.I18N_LOGGER_FILE_SETTING)));
			eptSet.addAll(loader.countinuousLoad(loggerI18nHandler));
		} finally {
			if (Objects.nonNull(loader)) {
				loader.close();
			}
		}

		for (LoadFailedException e : eptSet) {
			warn(LoggerStringKey.RAE_CMPOENT_INIT_3, e);
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
		info(LoggerStringKey.RAE_CMPOENT_INIT_4);

		Set<LoadFailedException> eptSet = new LinkedHashSet<>();
		XmlPropResourceI18nLoader loader = null;
		try {
			loader = new XmlPropResourceI18nLoader(
					openResource(constantsProvider.getResourceKey(ResourceKeyType.I18N_LOGGER_RESOURCE_SETTING)));
			eptSet.addAll(loader.countinuousLoad(loggerI18nHandler));
		} finally {
			if (Objects.nonNull(loader)) {
				loader.close();
			}
		}

		for (LoadFailedException e : eptSet) {
			warn(LoggerStringKey.RAE_CMPOENT_INIT_5, e);
		}
		eptSet = null;
		loader = null;

		loggerI18nHandler.setCurrentLocale(null);
		loggerI18nHandler.setCurrentLocale(getToolkit().getCoreConfigModelReadOnly()
				.getParsedValue(CoreConfigEntry.I18N_LOGGER.getConfigKey(), Locale.class));
	}

	/**
	 * 读取权限需求表。
	 * 
	 * @throws IOException
	 *             IO异常。
	 */
	private void loadPermDemandModel() throws IOException {
		info(LoggerStringKey.RAE_CMPOENT_INIT_10);

		Set<LoadFailedException> eptSet = new LinkedHashSet<>();
		XmlPermDemandLoader loader = null;
		try {
			loader = new XmlPermDemandLoader(
					openResource(constantsProvider.getResourceKey(ResourceKeyType.PERM_DEMAND_SETTING)));
			eptSet.addAll(loader.countinuousLoad(permDemandModel));
		} finally {
			if (Objects.nonNull(loader)) {
				loader.close();
			}
		}

		for (LoadFailedException e : eptSet) {
			warn(LoggerStringKey.RAE_CMPOENT_INIT_11, e);
		}
		eptSet = null;
		loader = null;

		loggerI18nHandler.setCurrentLocale(getToolkit().getCoreConfigModelReadOnly()
				.getParsedValue(CoreConfigEntry.I18N_LOGGER.getConfigKey(), Locale.class));
	}

	/**
	 * 保存配置文件。
	 */
	private void saveConfigModel() {
		// TODO Auto-generated method stub

	}

}
