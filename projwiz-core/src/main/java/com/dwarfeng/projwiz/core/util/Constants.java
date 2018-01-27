package com.dwarfeng.projwiz.core.util;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import javax.swing.Icon;

import com.dwarfeng.dutil.basic.cna.model.KeySetModel;
import com.dwarfeng.dutil.basic.cna.model.ListModel;
import com.dwarfeng.dutil.basic.cna.model.MapModel;
import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.SetModel;
import com.dwarfeng.dutil.basic.cna.model.SyncKeySetModel;
import com.dwarfeng.dutil.basic.cna.model.SyncListModel;
import com.dwarfeng.dutil.basic.cna.model.SyncMapModel;
import com.dwarfeng.dutil.basic.cna.model.SyncReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.SyncSetModel;
import com.dwarfeng.dutil.basic.gui.awt.CommonIconLib;
import com.dwarfeng.dutil.basic.gui.awt.ImageSize;
import com.dwarfeng.dutil.basic.gui.awt.ImageUtil;
import com.dwarfeng.dutil.basic.num.NumberValue;
import com.dwarfeng.dutil.basic.prog.ProcessException;
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
import com.dwarfeng.projwiz.core.model.cm.SyncComponentModel;
import com.dwarfeng.projwiz.core.model.eum.DialogMessage;
import com.dwarfeng.projwiz.core.model.eum.DialogOption;
import com.dwarfeng.projwiz.core.model.eum.DialogOptionCombo;
import com.dwarfeng.projwiz.core.model.eum.ProjWizProperty;
import com.dwarfeng.projwiz.core.model.io.PluginClassLoader;
import com.dwarfeng.projwiz.core.model.obv.FileObverser;
import com.dwarfeng.projwiz.core.model.obv.ProjectObverser;
import com.dwarfeng.projwiz.core.model.struct.Editor;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.FileProcessor;
import com.dwarfeng.projwiz.core.model.struct.LeveledToolkit.ToolkitLevel;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.ProjectFilePair;
import com.dwarfeng.projwiz.core.model.struct.ProjectProcessor;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;
import com.dwarfeng.projwiz.core.model.struct.Toolkit.Method;
import com.dwarfeng.projwiz.core.view.gui.MainFrame;
import com.dwarfeng.projwiz.core.view.struct.GuiManager;
import com.dwarfeng.projwiz.core.view.struct.ProjectFileChooserSetting;
import com.dwarfeng.projwiz.core.view.struct.SystemFileChooserSetting;
import com.dwarfeng.projwiz.core.view.struct.WindowSuppiler;

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
		public void showIndicateMonitor(ExecType type) {
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
		public void addFile2ProjectAsNew(File src, Project project, File dest)
				throws IllegalStateException, IllegalArgumentException, ProcessException {
			throw new IllegalStateException("没有权限运行方法: addFile2ProjectAsNew");
		}

		@Override
		public boolean addObverserToFile(File file, FileObverser obverser) throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: addObverserToFile");
		}

		@Override
		public boolean addProgramObverser(ProgramObverser obverser) throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: addProgramObverser");
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
		public boolean containsDialog(String key) throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: containsDialog");
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
		public SyncComponentModel getComponentModel() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getComponentModel");
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

		@Override
		public SyncKeySetModel<String, WindowSuppiler> getExternalWindowModel() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getExternalWindowModel");
		}

		@Override
		public KeySetModel<String, WindowSuppiler> getExternalWindowModelReadOnly() throws IllegalStateException {
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
		public SyncMapModel<String, File> getFileIndicateModel() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getFileIndicateModel");
		}

		@Override
		public KeySetModel<String, FileProcessor> getFileProcessors() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getFileProcessors");
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
		public SyncMapModel<String, Project> getProjectIndicateModel() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getProjectIndicateModel");
		}

		@Override
		public KeySetModel<String, ProjectProcessor> getProjectProcessors() throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: getProjectProcessors");
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
		public InputStream openFileInputStream(File file, String label)
				throws IllegalStateException, IllegalArgumentException, IOException {
			throw new IllegalStateException("没有权限运行方法: openFileInputStream");
		}

		@Override
		public OutputStream openFileOutputStream(File file, String label)
				throws IllegalStateException, IllegalArgumentException, IOException {
			throw new IllegalStateException("没有权限运行方法: openFileOutputStream");
		}

		@Override
		public boolean removeCoreConfigObverser(ExconfigObverser coreConfigObverser) throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: removeCoreConfigObverser");
		}

		@Override
		public boolean removeObverserFromFile(File file, FileObverser obverser) {
			throw new IllegalStateException("没有权限运行方法: removeObverserFromFile");
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
		public DialogOption showConfirmDialog(Object message) throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: showConfirmDialog");
		}

		@Override
		public DialogOption showConfirmDialog(Object message, String title, DialogOptionCombo dialogOptionCombo)
				throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: showConfirmDialog");
		}

		@Override
		public DialogOption showConfirmDialog(Object message, String title, DialogOptionCombo dialogOptionCombo,
				DialogMessage dialogMessage) throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: showConfirmDialog");
		}

		@Override
		public DialogOption showConfirmDialog(Object message, String title, DialogOptionCombo dialogOptionCombo,
				DialogMessage dialogMessage, Icon icon) throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: showConfirmDialog");
		}

		@Override
		public void showExternalWindow(String key) {
			throw new IllegalStateException("没有权限运行方法: showExternalWindow");
		}

		@Override
		public void showExternalWindow(WindowSuppiler suppiler) {
			throw new IllegalStateException("没有权限运行方法: showExternalWindow");
		}

		@Override
		public String showInputDialog(Object message) throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: showInputDialog");
		}

		@Override
		public String showInputDialog(Object message, Object initialSelectionValue) throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: showInputDialog");
		}

		@Override
		public String showInputDialog(Object message, String title, DialogMessage dialogMessage)
				throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: showInputDialog");
		}

		@Override
		public Object showInputDialog(Object message, String title, DialogMessage dialogMessage, Icon icon,
				Object[] selectionValues, Object initialSelectionValue) throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: addCoreConfigObverser");
		}

		@Override
		public void showMessageDialog(Object message) throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: showMessageDialog");
		}

		@Override
		public void showMessageDialog(Object message, String title, DialogMessage dialogMessage)
				throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: showMessageDialog");
		}

		@Override
		public void showMessageDialog(Object message, String title, DialogMessage dialogMessage, Icon icon)
				throws IllegalStateException {
			throw new IllegalStateException("没有权限运行方法: showMessageDialog");
		}

		@Override
		public int showOptionDialog(Object message, String title, DialogOptionCombo dialogOptionCombo,
				DialogMessage dialogMessage, Icon icon, Object[] options, Object initialValue)
				throws IllegalStateException {
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
	public final static String CMPOENT_LIST_PATH = "cmpoent-list.xml";

	/** 默认的配置列表所在的位置。 */
	public final static String CFG_DEFAULT_LIST_PATH = "/com/dwarfeng/projwiz/resources/cfg-list.xml";
	/** 默认记录器多语言文件所在的位置 */
	public final static String RESOURCE_I18N_LOGGER_PATH = "/com/dwarfeng/projwiz/resources/configuration/i18n/logger.properties";
	/** 图片根所在的位置 */
	public final static String RESOURCE_IMAGE_ROOT_PATH = "/com/dwarfeng/projwiz/resources/image/";

	/** 主程序的核心配置的资源仓库类别。 */
	public final static String RESOURCE_CLASSIFY_CORE = "com.dwarfeng.projwiz.core";
	/** 主程序的国际化配置的资源仓库类别。 */
	public final static String RESOURCE_CLASSIFY_I18N = "com.dwarfeng.projwiz.i18n";
	/** 主程序的测试情形配置的资源仓库类别。 */
	public final static String RESOURCE_CLASSIFY_TESTCASE = "com.dwarfeng.projwiz.testcase";

	/** 默认的程序属性表。 */
	public final static Map<ProjWizProperty, String> DEFAULT_PROJWIZ_PROPERTIES;

	/** LeveledToolkit 类中执行每个方法所需要的最小权限。 */
	public final static Map<Method, NumberValue> LEVELEDTOOLKIT_MIN_LEVEL;

	/** 代表图片不存在的图片。 */
	public final static Image IMAGE_LOAD_FAILED = ImageUtil.getInternalImage(CommonIconLib.IMG_LOAD_FAILED_BLUE,
			ImageUtil.getDefaultImage(), ImageSize.ICON_SUPER_LARGE);

	static {
		Map<ProjWizProperty, String> init0 = new EnumMap<>(ProjWizProperty.class);
		init0.put(ProjWizProperty.PLUGIN_PATH, "plugins");
		init0.put(ProjWizProperty.TEMP_PATH, "temp");
		init0.put(ProjWizProperty.CFGREPO_PATH, "configuration");
		init0.put(ProjWizProperty.CFG_LISTS, "");
		init0.put(ProjWizProperty.COMPONENT_PATH, "components");
		init0.put(ProjWizProperty.METADATA_PATH, "metadata");
		init0.put(ProjWizProperty.TEST_CASE, "false");
		DEFAULT_PROJWIZ_PROPERTIES = Collections.unmodifiableMap(init0);

		Map<Method, ToolkitLevel> init1 = new EnumMap<>(Method.class);
		// 为各种方法提供权限等级
		init1.put(Method.ADDCORECONFIGOBVERSER, ToolkitLevel.WRITE_LIMIT);
		init1.put(Method.ADDOBVERSERTOFILE, ToolkitLevel.WRITE_LIMIT);
		init1.put(Method.ADDPROGRAMOBVERSER, ToolkitLevel.FULL);
		init1.put(Method.CHOOSEPROJECTFILE, ToolkitLevel.READ_ONLY);
		init1.put(Method.CHOOSESYSTEMFILE, ToolkitLevel.READ_ONLY);
		init1.put(Method.CLEARPROGRAMOBVERSER, ToolkitLevel.FULL);
		init1.put(Method.CONTAINSDIALOG, ToolkitLevel.READ_ONLY);
		init1.put(Method.DEBUG, ToolkitLevel.WRITE_LIMIT);
		init1.put(Method.DISPOSEMAINFRAME, ToolkitLevel.FULL);
		init1.put(Method.ERROR, ToolkitLevel.WRITE_LIMIT);
		init1.put(Method.EXIT, ToolkitLevel.FULL);
		init1.put(Method.FATAL, ToolkitLevel.WRITE_LIMIT);
		init1.put(Method.GETANCHORFILEMODEL, ToolkitLevel.FULL);
		init1.put(Method.GETANCHORFILEMODELREADONLY, ToolkitLevel.READ_ONLY);
		init1.put(Method.GETBACKGROUND, ToolkitLevel.FULL);
		init1.put(Method.GETCFGHANDLER, ToolkitLevel.FULL);
		init1.put(Method.GETCFGHANDLERREADONLY, ToolkitLevel.READ_ONLY);
		init1.put(Method.GETBACKGROUNDREADONLY, ToolkitLevel.READ_ONLY);
		init1.put(Method.GETCOMPONENTMODEL, ToolkitLevel.FULL);
		init1.put(Method.GETCORECONFIGMODEL, ToolkitLevel.FULL);
		init1.put(Method.GETCORECONFIGMODELREADONLY, ToolkitLevel.READ_ONLY);
		init1.put(Method.GETEDITORMODEL, ToolkitLevel.FULL);
		init1.put(Method.GETEDITORMODELREADONLY, ToolkitLevel.READ_ONLY);
		init1.put(Method.GETEXITCODE, ToolkitLevel.READ_ONLY);
		init1.put(Method.GETEXTERNALWINDOWMODEL, ToolkitLevel.FULL);
		init1.put(Method.GETEXTERNALWINDOWMODELREADONLY, ToolkitLevel.READ_ONLY);
		init1.put(Method.GETFILEICONIMAGEMODEL, ToolkitLevel.FULL);
		init1.put(Method.GETFILEICONIMAGEMODELREADONLY, ToolkitLevel.READ_ONLY);
		init1.put(Method.GETFILEICONOBVMODEL, ToolkitLevel.FULL);
		init1.put(Method.GETFILEINDICATEMODEL, ToolkitLevel.FULL);
		init1.put(Method.GETFILEPROCESSORS, ToolkitLevel.READ_ONLY);
		init1.put(Method.GETFOCUSEDITORMODEL, ToolkitLevel.FULL);
		init1.put(Method.GETFOCUSEDITORMODELREADONLY, ToolkitLevel.READ_ONLY);
		init1.put(Method.GETFOCUSFILEMODEL, ToolkitLevel.FULL);
		init1.put(Method.GETFOCUSFILEMODELREADONLY, ToolkitLevel.READ_ONLY);
		init1.put(Method.GETFOCUSPROJECTMODEL, ToolkitLevel.FULL);
		init1.put(Method.GETFOCUSPROJECTMODELREADONLY, ToolkitLevel.READ_ONLY);
		init1.put(Method.GETGUIMANAGER, ToolkitLevel.FULL);
		init1.put(Method.GETHOLDPROJECTMODEL, ToolkitLevel.FULL);
		init1.put(Method.GETHOLDPROJECTMODELREADONLY, ToolkitLevel.READ_ONLY);
		init1.put(Method.GETLABELI18NHANDLER, ToolkitLevel.FULL);
		init1.put(Method.GETLABELI18NHANDLERREADONLY, ToolkitLevel.READ_ONLY);
		init1.put(Method.GETLOGGERHANDLER, ToolkitLevel.FULL);
		init1.put(Method.GETLOGGERHANDLERREADONLY, ToolkitLevel.READ_ONLY);
		init1.put(Method.GETLOGGERI18NHANDLER, ToolkitLevel.FULL);
		init1.put(Method.GETLOGGERI18NHANDLERREADONLY, ToolkitLevel.READ_ONLY);
		init1.put(Method.GETMAINFRAME, ToolkitLevel.FULL);
		init1.put(Method.GETPROGRAMOBVERSERS, ToolkitLevel.FULL);
		init1.put(Method.GETPROJECTICONIMAGEMODEL, ToolkitLevel.FULL);
		init1.put(Method.GETPROJECTPROCESSORS, ToolkitLevel.READ_ONLY);
		init1.put(Method.GETPROJECTICONIMAGEMODELREADONLY, ToolkitLevel.READ_ONLY);
		init1.put(Method.GETPROJECTICONOBVMODEL, ToolkitLevel.FULL);
		init1.put(Method.GETPROJECTINDICATEMODEL, ToolkitLevel.FULL);
		init1.put(Method.GETPLUGINCLASSLOADER, ToolkitLevel.FULL);
		init1.put(Method.GETPROPERTY, ToolkitLevel.READ_ONLY);
		init1.put(Method.GETRUNTIMESTATE, ToolkitLevel.READ_ONLY);
		init1.put(Method.GETVIEWCONFIGMODEL, ToolkitLevel.FULL);
		init1.put(Method.GETVIEWCONFIGMODELREADONLY, ToolkitLevel.READ_ONLY);
		init1.put(Method.INFO, ToolkitLevel.WRITE_LIMIT);
		init1.put(Method.ISMAINFRAMEVISIBLE, ToolkitLevel.READ_ONLY);
		init1.put(Method.NEWMAINFRAME, ToolkitLevel.FULL);
		init1.put(Method.OPENFILE, ToolkitLevel.WRITE_LIMIT);
		init1.put(Method.OPENFILEINPUTSTREAM, ToolkitLevel.WRITE_LIMIT);
		init1.put(Method.OPENFILEOUTPUTSTREAM, ToolkitLevel.WRITE_LIMIT);
		init1.put(Method.REMOVECORECONFIGOBVERSER, ToolkitLevel.WRITE_LIMIT);
		init1.put(Method.REMOVEOBVERSERFROMFILE, ToolkitLevel.WRITE_LIMIT);
		init1.put(Method.REMOVEPROGRAMOBVERSER, ToolkitLevel.FULL);
		init1.put(Method.SETRUNTIMESTATE, ToolkitLevel.FULL);
		init1.put(Method.SHOWCOMPONENTDIALOG, ToolkitLevel.WRITE_LIMIT);
		init1.put(Method.SHOWCONFIRMDIALOG, ToolkitLevel.WRITE_LIMIT);
		init1.put(Method.SHOWDIALOG, ToolkitLevel.WRITE_LIMIT);
		init1.put(Method.SHOWEXTERNALWINDOW, ToolkitLevel.WRITE_LIMIT);
		init1.put(Method.SHOWINPUTDIALOG, ToolkitLevel.WRITE_LIMIT);
		init1.put(Method.SHOWMESSAGEDIALOG, ToolkitLevel.WRITE_LIMIT);
		init1.put(Method.SHOWNEWDIALOG, ToolkitLevel.WRITE_LIMIT);
		init1.put(Method.SHOWOPTIONDIALOG, ToolkitLevel.WRITE_LIMIT);
		init1.put(Method.START, ToolkitLevel.FULL);
		init1.put(Method.SUBMITTASK, ToolkitLevel.WRITE_LIMIT);
		init1.put(Method.TRACE, ToolkitLevel.WRITE_LIMIT);
		init1.put(Method.TRYEXIT, ToolkitLevel.FULL);
		init1.put(Method.WARN, ToolkitLevel.WRITE_LIMIT);
		LEVELEDTOOLKIT_MIN_LEVEL = Collections.unmodifiableMap(init1);

	}

	// 禁止外部实例化
	private Constants() {
	}

}
