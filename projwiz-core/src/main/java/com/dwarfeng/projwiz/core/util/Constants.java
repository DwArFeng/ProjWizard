package com.dwarfeng.projwiz.core.util;

import java.awt.Image;
import java.awt.Window;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import com.dwarfeng.dutil.basic.cna.model.ListModel;
import com.dwarfeng.dutil.basic.cna.model.MapModel;
import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.SetModel;
import com.dwarfeng.dutil.basic.cna.model.SyncListModel;
import com.dwarfeng.dutil.basic.cna.model.SyncMapModel;
import com.dwarfeng.dutil.basic.cna.model.SyncReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.SyncSetModel;
import com.dwarfeng.dutil.basic.gui.awt.CommonIconLib;
import com.dwarfeng.dutil.basic.gui.awt.ImageSize;
import com.dwarfeng.dutil.basic.gui.awt.ImageUtil;
import com.dwarfeng.dutil.basic.prog.ProgramObverser;
import com.dwarfeng.dutil.basic.prog.RuntimeState;
import com.dwarfeng.dutil.develop.backgr.Background;
import com.dwarfeng.dutil.develop.backgr.Task;
import com.dwarfeng.dutil.develop.cfg.ExconfigModel;
import com.dwarfeng.dutil.develop.cfg.SyncExconfigModel;
import com.dwarfeng.dutil.develop.cfg.obv.ExconfigObverser;
import com.dwarfeng.dutil.develop.i18n.I18nHandler;
import com.dwarfeng.dutil.develop.i18n.SyncI18nHandler;
import com.dwarfeng.dutil.develop.logger.LoggerHandler;
import com.dwarfeng.dutil.develop.logger.SyncLoggerHandler;
import com.dwarfeng.dutil.develop.resource.ResourceHandler;
import com.dwarfeng.dutil.develop.resource.SyncResourceHandler;
import com.dwarfeng.projwiz.core.model.cm.SyncModuleModel;
import com.dwarfeng.projwiz.core.model.cm.SyncToolkitPermModel;
import com.dwarfeng.projwiz.core.model.cm.ToolkitPermModel;
import com.dwarfeng.projwiz.core.model.eum.ProjWizProperty;
import com.dwarfeng.projwiz.core.model.io.PluginClassLoader;
import com.dwarfeng.projwiz.core.model.obv.FileObverser;
import com.dwarfeng.projwiz.core.model.obv.ProjectObverser;
import com.dwarfeng.projwiz.core.model.struct.Editor;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.Module;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.ProjectFilePair;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;
import com.dwarfeng.projwiz.core.view.eum.DialogOption;
import com.dwarfeng.projwiz.core.view.gui.MainFrame;
import com.dwarfeng.projwiz.core.view.struct.ConfirmDialogSetting;
import com.dwarfeng.projwiz.core.view.struct.GuiManager;
import com.dwarfeng.projwiz.core.view.struct.InputDialogSetting;
import com.dwarfeng.projwiz.core.view.struct.MessageDialogSetting;
import com.dwarfeng.projwiz.core.view.struct.ModuleChooserSetting;
import com.dwarfeng.projwiz.core.view.struct.OptionDialogSetting;
import com.dwarfeng.projwiz.core.view.struct.ProjectFileChooserSetting;
import com.dwarfeng.projwiz.core.view.struct.SystemFileChooserSetting;

/**
 * 程序常量。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public final class Constants {

	/**
	 * 无动作GUI管理器。
	 * <p>
	 * 该管理器仅简单的实现方法，不执行任何动作。
	 * 
	 * @author DwArFeng
	 * @since 0.0.2-alpha
	 */
	private static final class NonActionGuiManager implements GuiManager {

		@Override
		public void addFocusFile(File file, ExecType type) {
		}

		@Override
		public void closeAllProject(ExecType type) {
		}

		@Override
		public void deleteFocusFile(ExecType type) {
		}

		@Override
		public void forceExit(ExecType type) {
		}

		@Override
		public void newFile(ExecType type) {
		}

		@Override
		public void newProject(ExecType type) {
		}

		@Override
		public void openAnchorFile(ExecType type) {
		}

		@Override
		public void openFocusFile(ExecType type) {
		}

		@Override
		public void openProject(ExecType type) {
		}

		@Override
		public void putFocusEditor(Project project, Editor editor, ExecType type) {
		}

		@Override
		public void removeFocusFile(File file, ExecType type) {
		}

		@Override
		public void renameAnchorFile(ExecType type) {
		}

		@Override
		public void saveAsFocusProject(ExecType type) {
		}

		@Override
		public void saveFocusEditor(ExecType type) {
		}

		@Override
		public void saveFocusProject(ExecType type) {
		}

		@Override
		public void setAnchorFile(File file, ExecType type) {
		}

		@Override
		public void setFocusProject(Project project, ExecType type) {
		}

		@Override
		public void showAnchorFilePropertiesDialog(ExecType type) {
		}

		@Override
		public void showEditorMonitor(ExecType type) {
		}

		@Override
		public void showFocusProjectPropertiesDialog(ExecType type) {
		}

		@Override
		public void showProjectAndFileMonitor(ExecType type) {
		}

		@Override
		public void submitTask(Task task, ExecType type) {
		}

		@Override
		public void superSecretSettings(ExecType type) {
		}

		@Override
		public void tryCloseCertainProject(Project project, ExecType type) {
		}

		@Override
		public void tryCloseFocusProject(ExecType type) {
		}

		@Override
		public void tryExit(ExecType type) {
		}

		@Override
		public void tryStopCertainEditor(Editor editor, ExecType type) {
		}

		@Override
		public void tryStopFocusEditor(ExecType type) {
		}

		@Override
		public void tryStopFocusProjectEditor(ExecType type) {
		}

	}

	/**
	 * 无权限工具包。
	 * <p>
	 * 该工具包中的任何方法均无权限运行，该工具包是代替 <code>null</code> 工具包的方案之一。
	 * 
	 * @author DwArFeng
	 * @since 0.0.1-alpha
	 */
	private static final class NonPermissionToolkit implements Toolkit {

		@Override
		public boolean addCoreConfigObverser(ExconfigObverser coreConfigObverser) throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: addCoreConfigObverser");
		}

		@Override
		public boolean addProgramObverser(ProgramObverser obverser) throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: addProgramObverser");
		}

		@Override
		public Module[] chooseModule(ModuleChooserSetting setting) throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: chooseModule");
		}

		@Override
		public File[] chooseProjectFile(ProjectFileChooserSetting setting) throws IllegalMonitorStateException {
			throw new IllegalStateException("chooseProjectFile");
		}

		@Override
		public java.io.File[] chooseSystemFile(SystemFileChooserSetting setting) throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: chooseSystemFile");
		}

		@Override
		public void clearProgramObverser() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: clearProgramObverser");
		}

		@Override
		public void debug(String message) throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: debug");
		}

		@Override
		public boolean disposeMainFrame() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: disposeMainFrame");
		}

		@Override
		public void error(String message, Throwable t) throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: error");
		}

		@Override
		public void exit() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: exit");
		}

		@Override
		public void fatal(String message, Throwable t) throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: fatal");
		}

		@Override
		public SyncReferenceModel<File> getAnchorFileModel() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getAnchorFileModel");
		}

		@Override
		public ReferenceModel<File> getAnchorFileModelReadOnly() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getAnchorFileModelReadOnly");
		}

		@Override
		public Background getBackground(BackgroundType type) throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getBackground");
		}

		@Override
		public Background getBackgroundReadOnly(BackgroundType type) throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getBackgroundReadOnly");
		}

		@Override
		public SyncResourceHandler getCfgHandler() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getResourceHandler");
		}

		@Override
		public ResourceHandler getCfgHandlerReadOnly() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getResourceHandlerReadOnly");
		}

		@Override
		public SyncExconfigModel getCoreConfigModel() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getCoreConfigModel");
		}

		@Override
		public ExconfigModel getCoreConfigModelReadOnly() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getCoreConfigModelReadOnly");
		}

		@Override
		public SyncMapModel<ProjectFilePair, Editor> getEditorModel() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getEditorModel");
		}

		@Override
		public SyncMapModel<ProjectFilePair, Editor> getEditorModelReadOnly() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getEditorModelReadOnly");
		}

		@Override
		public int getExitCode() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getExitCode");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SyncSetModel<Window> getExternalWindowModel() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getExternalWindowModel");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SetModel<Window> getExternalWindowModelReadOnly() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getExternalWindowModelReadOnly");
		}

		@Override
		public SyncMapModel<File, Image> getFileIconImageModel() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getFileIconImageModel");
		}

		@Override
		public MapModel<File, Image> getFileIconImageModelReadOnly() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getFileIconImageModelReadOnly");
		}

		@Override
		public SyncMapModel<File, FileObverser> getFileIconObvModel() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getFileIconObvModel");
		}

		@Override
		public SyncMapModel<Project, Editor> getFocusEditorModel() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getFocusEditorModel");
		}

		@Override
		public MapModel<Project, Editor> getFocusEditorModelReadOnly() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getFocusEditorModelReadOnly");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SyncSetModel<File> getFocusFileModel() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getFocusFileModel");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SetModel<File> getFocusFileModelReadOnly() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getFocusFileModelReadOnly");
		}

		@Override
		public SyncReferenceModel<Project> getFocusProjectModel() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getFocusProjectModel");
		}

		@Override
		public ReferenceModel<Project> getFocusProjectModelReadOnly() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getFocusProjectModelReadOnly");
		}

		@Override
		public GuiManager getGuiManager() {
			throw new IllegalStateException("没有权限运行方法: getGuiManager");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SyncListModel<Project> getHoldProjectModel() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getHoldProjectModel");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ListModel<Project> getHoldProjectModelReadOnly() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getHoldProjectModelReadOnly");
		}

		@Override
		public SyncI18nHandler getLabelI18nHandler() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getLabelI18nHandler");
		}

		@Override
		public I18nHandler getLabelI18nHandlerReadOnly() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getLabelI18nHandlerReadOnly");
		}

		@Override
		public SyncLoggerHandler getLoggerHandler() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getLoggerHandler");
		}

		@Override
		public LoggerHandler getLoggerHandlerReadOnly() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getLoggerHandlerReadOnly");
		}

		@Override
		public SyncI18nHandler getLoggerI18nHandler() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getLoggerI18nHandler");
		}

		@Override
		public I18nHandler getLoggerI18nHandlerReadOnly() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getLoggerI18nHandlerReadOnly");
		}

		@Override
		public MainFrame getMainFrame() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getMainFrame");
		}

		@Override
		public SyncModuleModel getModuleModel() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getModuleModel");
		}

		@Override
		public SyncModuleModel getModuleModelReadOnly() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getModuleModelReadOnly");
		}

		@Override
		public SyncMapModel<Class<? extends Module>, ReferenceModel<Toolkit>> getModuleToolkitModel()
				throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getModuleToolkitModel");

		}

		@Override
		public PluginClassLoader getPluginClassLoader() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getPluginClassLoader");
		}

		@Override
		public Set<ProgramObverser> getProgramObversers() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getProgramObversers");
		}

		@Override
		public SyncMapModel<Project, Image> getProjectIconImageModel() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getProjectIconImageModel");
		}

		@Override
		public MapModel<Project, Image> getProjectIconImageModelReadOnly() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getProjectIconImageModelReadOnly");
		}

		@Override
		public SyncMapModel<Project, ProjectObverser> getProjectIconObvModel() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getProjectIconObvModel");
		}

		@Override
		public String getProperty(ProjWizProperty property) throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getProperty");
		}

		@Override
		public RuntimeState getRuntimeState() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getRuntimeState");
		}

		@Override
		public SyncToolkitPermModel getToolkitPermModel() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getToolkitPermModel");
		}

		@Override
		public ToolkitPermModel getToolkitPermModelReadOnly() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getToolkitPermModelReadOnly");
		}

		@Override
		public SyncExconfigModel getViewConfigModel() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getViewConfigModel");
		}

		@Override
		public ExconfigModel getViewConfigModelReadOnly() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getViewConfigModelReadOnly");
		}

		@Override
		public boolean hasPermission(Method method) {
			throw new IllegalStateException("没有权限运行方法: hasPermission");
		}

		@Override
		public void info(String message) throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: info");
		}

		@Override
		public boolean isMainFrameVisible() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: isMainFrameVisible");
		}

		@Override
		public boolean newMainFrame() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: newMainFrame");
		}

		@Override
		public void openFile(Project project, File file) throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: openFile");
		}

		@Override
		public boolean removeCoreConfigObverser(ExconfigObverser coreConfigObverser) throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: removeCoreConfigObverser");
		}

		@Override
		public boolean removeProgramObverser(ProgramObverser obverser) throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: removeProgramObverser");
		}

		@Override
		public void setExitCode(int exitCode) throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: setExitCode");
		}

		@Override
		public void setRuntimeState(RuntimeState runtimeState) throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: setRuntimeState");
		}

		@Override
		public DialogOption showConfirmDialog(ConfirmDialogSetting setting) throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: showConfirmDialog");
		}

		@Override
		public void showExternalWindow(Window window) throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: showExternalWindow");
		}

		@Override
		public DialogOption showInputDialog(InputDialogSetting setting) throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: showInputDialog");
		}

		@Override
		public void showMessageDialog(MessageDialogSetting setting) throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: showMessageDialog");
		}

		@Override
		public int showOptionDialog(OptionDialogSetting setting) throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: addCoreConfigObverser");
		}

		@Override
		public void start() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: start");
		}

		@Override
		public void submitTask(Task task, BackgroundType type) throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: submitTask");
		}

		@Override
		public void trace(String message) throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: trace");
		}

		@Override
		public void tryExit() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: tryExit");
		}

		@Override
		public void warn(String message) throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: warn");
		}

		@Override
		public void warn(String message, Throwable t) throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: warn");
		}

	}

	/** 默认的，不执行任何动作的 GUI 管理器 */
	public final static GuiManager DEFAULT_GUIMANAGER = new NonActionGuiManager();
	/** 处理器默认的，没有权限的工具包。 */
	public final static Toolkit NON_PERMISSION_TOOLKIT = new NonPermissionToolkit();

	/** 默认的丢失文本字段 */
	public final static String MISSING_LABEL = "！文本丢失";

	/** Jar包内配置列表的文件路径。 */
	public final static String CFG_LIST_PATH = "cfg-list.xml";
	/** Jar包内组件列表文件路径。 */
	public final static String MODULE_LIST_PATH = "module-list.xml";

	/** 默认记录器国际化文件所在的位置 */
	public final static String RESOURCE_I18N_LOGGER_PATH = "/com/dwarfeng/projwiz/resources/core/configuration/i18n/logger.properties";
	/** 图片根所在的位置 */
	public final static String RESOURCE_IMAGE_ROOT_PATH = "/com/dwarfeng/projwiz/resources/core/image/";

	/** 主程序的核心配置的资源仓库类别。 */
	public final static String CONFIGURATION_CLASSIFY_CORE = "com.dwarfeng.projwiz.core";
	/** 主程序的国际化配置的资源仓库类别。 */
	public final static String CONFIGURATION_CLASSIFY_I18N = "com.dwarfeng.projwiz.i18n";
	/** 主程序的测试情形配置的资源仓库类别。 */
	public final static String CONFIGURATION_CLASSIFY_TESTCASE = "com.dwarfeng.projwiz.testcase";

	/** 默认的程序属性表。 */
	public final static Map<ProjWizProperty, String> DEFAULT_PROJWIZ_PROPERTIES;

	/** 代表图片不存在的图片。 */
	public final static Image IMAGE_LOAD_FAILED = ImageUtil.getInternalImage(CommonIconLib.IMG_LOAD_FAILED_BLUE,
			ImageUtil.getDefaultImage(), ImageSize.ICON_SUPER_LARGE);

	static {
		Map<ProjWizProperty, String> init0 = new EnumMap<>(ProjWizProperty.class);
		init0.put(ProjWizProperty.PLUGIN_PATH, "plugins");
		init0.put(ProjWizProperty.TEMP_PATH, "temp");
		init0.put(ProjWizProperty.CFGREPO_PATH, "configuration");
		init0.put(ProjWizProperty.CFG_LISTS, "");
		init0.put(ProjWizProperty.MODULE_PATH, "modules");
		init0.put(ProjWizProperty.METADATA_PATH, "metadata");
		init0.put(ProjWizProperty.TEST_CASE, "false");
		init0.put(ProjWizProperty.CFG_LIST_PATH, "/com/dwarfeng/projwiz/resources/core/cfg-list.xml");
		init0.put(ProjWizProperty.CFG_LIST_PATH_TYPE, "INJAR");
		init0.put(ProjWizProperty.CFG_FORCE_RESET, "false");
		DEFAULT_PROJWIZ_PROPERTIES = Collections.unmodifiableMap(init0);
	}

	// 禁止外部实例化
	private Constants() {
	}

}
