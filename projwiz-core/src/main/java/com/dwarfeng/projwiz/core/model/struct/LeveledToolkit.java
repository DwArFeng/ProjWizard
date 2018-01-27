package com.dwarfeng.projwiz.core.model.struct;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
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
import com.dwarfeng.projwiz.core.util.Constants;
import com.dwarfeng.projwiz.core.view.gui.MainFrame;
import com.dwarfeng.projwiz.core.view.struct.GuiManager;
import com.dwarfeng.projwiz.core.view.struct.ProjectFileChooserSetting;
import com.dwarfeng.projwiz.core.view.struct.SystemFileChooserSetting;
import com.dwarfeng.projwiz.core.view.struct.WindowSuppiler;

/**
 * 分级工具包。
 * 
 * <p>
 * 根据其中的分级确定什么方法是有权限的。
 * <p>
 * 该工具包代理一个标准工具，如果当前的权限小于指定方法运行所需的最小权限，则抛出权限不足异常；
 * 如果大于运行指定方法所需的最小权限，则代理标准工具的相应方法。 <br>
 * 要求标准工具应该能够执行所有方法，每个方法都不会抛出权限异常。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public final class LeveledToolkit implements Toolkit {

	/**
	 * 工具包的权限分级。
	 * 
	 * @author DwArFeng
	 * @since 0.0.1-alpha
	 */
	public enum ToolkitLevel implements NumberValue {

		/** 最低权限，不可以调用任何方法。 */
		NONE(0),
		/** 只读权限，可以获得各个属性，但是无权修改它们。 */
		READ_ONLY(333),
		/** 有限的写权限，可以使用某些方法对模型或属性进行修改，但无权对模型直接修改。 */
		WRITE_LIMIT(667),
		/** 完全权限，可以调用任意方法。 */
		FULL(1000),

		;

		private final int level;

		private ToolkitLevel(int level) {
			this.level = level;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double doubleValue() {
			return level;
		}
	}

	private final NumberValue currentLevel;
	private final Toolkit standardToolkit;

	private boolean stopFlag = false;
	private final Object stopFlagLock = new Object();

	/**
	 * 新建一个分级工具包。
	 * 
	 * @param standardToolkit
	 *            指定的标准工具包。
	 * @param currentLevel
	 *            当前的分级。
	 */
	public LeveledToolkit(Toolkit standardToolkit, NumberValue currentLevel) {
		Objects.requireNonNull(standardToolkit, "入口参数 standardToolkit 不能为 null。");
		Objects.requireNonNull(currentLevel, "入口参数 currentLevel 不能为 null。");

		this.standardToolkit = standardToolkit;
		this.currentLevel = currentLevel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addCoreConfigObverser(ExconfigObverser coreConfigObverser) throws IllegalStateException {
		checkPermissionAndState(Method.ADDCORECONFIGOBVERSER);
		return standardToolkit.addCoreConfigObverser(coreConfigObverser);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addFile2ProjectAsNew(File src, Project project, File dest)
			throws IllegalStateException, IllegalArgumentException, ProcessException {
		checkPermissionAndState(Method.ADDFILE2PROJECTASNEW);
		standardToolkit.addFile2ProjectAsNew(src, project, dest);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addObverserToFile(File file, FileObverser obverser) throws IllegalStateException {
		checkPermissionAndState(Method.ADDOBVERSERTOFILE);
		return standardToolkit.addObverserToFile(file, obverser);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addProgramObverser(ProgramObverser obverser) throws IllegalStateException {
		checkPermissionAndState(Method.ADDPROGRAMOBVERSER);
		return standardToolkit.addProgramObverser(obverser);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File[] chooseProjectFile(ProjectFileChooserSetting setting) throws IllegalMonitorStateException {
		checkPermissionAndState(Method.CHOOSEPROJECTFILE);
		return standardToolkit.chooseProjectFile(setting);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.io.File[] chooseSystemFile(SystemFileChooserSetting setting) throws IllegalStateException {
		checkPermissionAndState(Method.CHOOSESYSTEMFILE);
		return standardToolkit.chooseSystemFile(setting);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clearProgramObverser() throws IllegalStateException {
		checkPermissionAndState(Method.CLEARPROGRAMOBVERSER);
		standardToolkit.clearProgramObverser();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsDialog(String key) throws IllegalStateException {
		checkPermissionAndState(Method.CONTAINSDIALOG);
		return standardToolkit.containsDialog(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(String message) throws IllegalStateException {
		checkPermissionAndState(Method.DEBUG);
		standardToolkit.debug(message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean disposeMainFrame() throws IllegalStateException {
		checkPermissionAndState(Method.DISPOSEMAINFRAME);
		return standardToolkit.disposeMainFrame();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(String message, Throwable t) throws IllegalStateException {
		checkPermissionAndState(Method.ERROR);
		standardToolkit.error(message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exit() throws IllegalStateException {
		checkPermissionAndState(Method.EXIT);
		standardToolkit.exit();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(String message, Throwable t) throws IllegalStateException {
		checkPermissionAndState(Method.FATAL);
		standardToolkit.fatal(message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SyncReferenceModel<File> getAnchorFileModel() throws IllegalStateException {
		checkPermissionAndState(Method.GETANCHORFILEMODEL);
		return standardToolkit.getAnchorFileModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReferenceModel<File> getAnchorFileModelReadOnly() throws IllegalStateException {
		checkPermissionAndState(Method.GETANCHORFILEMODELREADONLY);
		return standardToolkit.getAnchorFileModelReadOnly();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Background getBackground(BackgroundType type) throws IllegalStateException {
		checkPermissionAndState(Method.GETBACKGROUND);
		return standardToolkit.getBackground(type);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Background getBackgroundReadOnly(BackgroundType type) throws IllegalStateException {
		checkPermissionAndState(Method.GETBACKGROUNDREADONLY);
		return standardToolkit.getBackgroundReadOnly(type);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SyncResourceHandler getCfgHandler() throws IllegalStateException {
		checkPermissionAndState(Method.GETCFGHANDLER);
		return standardToolkit.getCfgHandler();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResourceHandler getCfgHandlerReadOnly() throws IllegalStateException {
		checkPermissionAndState(Method.GETCFGHANDLERREADONLY);
		return standardToolkit.getCfgHandlerReadOnly();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SyncComponentModel getComponentModel() throws IllegalStateException {
		checkPermissionAndState(Method.GETCOMPONENTMODEL);
		return standardToolkit.getComponentModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SyncExconfigModel getCoreConfigModel() throws IllegalStateException {
		checkPermissionAndState(Method.GETCORECONFIGMODEL);
		return standardToolkit.getCoreConfigModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExconfigModel getCoreConfigModelReadOnly() throws IllegalStateException {
		checkPermissionAndState(Method.GETCORECONFIGMODELREADONLY);
		return standardToolkit.getCoreConfigModelReadOnly();
	}

	/**
	 * 获取当前的权限等级。
	 * 
	 * @return 当前的权限等级。
	 */
	public NumberValue getCurrentLevel() {
		return currentLevel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SyncMapModel<ProjectFilePair, Editor> getEditorModel() throws IllegalStateException {
		checkPermissionAndState(Method.GETEDITORMODEL);
		return standardToolkit.getEditorModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SyncMapModel<ProjectFilePair, Editor> getEditorModelReadOnly() throws IllegalStateException {
		checkPermissionAndState(Method.GETEDITORMODELREADONLY);
		return standardToolkit.getEditorModelReadOnly();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getExitCode() throws IllegalStateException {
		checkPermissionAndState(Method.GETEXITCODE);
		return standardToolkit.getExitCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SyncKeySetModel<String, WindowSuppiler> getExternalWindowModel() throws IllegalStateException {
		checkPermissionAndState(Method.GETEXTERNALWINDOWMODEL);
		return standardToolkit.getExternalWindowModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public KeySetModel<String, WindowSuppiler> getExternalWindowModelReadOnly() throws IllegalStateException {
		checkPermissionAndState(Method.GETEXTERNALWINDOWMODELREADONLY);
		return standardToolkit.getExternalWindowModelReadOnly();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SyncMapModel<File, Image> getFileIconImageModel() throws IllegalStateException {
		checkPermissionAndState(Method.GETFILEICONIMAGEMODEL);
		return standardToolkit.getFileIconImageModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MapModel<File, Image> getFileIconImageModelReadOnly() throws IllegalStateException {
		checkPermissionAndState(Method.GETFILEICONIMAGEMODELREADONLY);
		return standardToolkit.getFileIconImageModelReadOnly();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SyncMapModel<File, FileObverser> getFileIconObvModel() throws IllegalStateException {
		checkPermissionAndState(Method.GETFILEICONOBVMODEL);
		return standardToolkit.getFileIconObvModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SyncMapModel<String, File> getFileIndicateModel() throws IllegalStateException {
		checkPermissionAndState(Method.GETFILEINDICATEMODEL);
		return standardToolkit.getFileIndicateModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public KeySetModel<String, FileProcessor> getFileProcessors() throws IllegalStateException {
		checkPermissionAndState(Method.GETFILEPROCESSORS);
		return standardToolkit.getFileProcessors();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SyncMapModel<Project, Editor> getFocusEditorModel() throws IllegalStateException {
		checkPermissionAndState(Method.GETFOCUSEDITORMODEL);
		return standardToolkit.getFocusEditorModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MapModel<Project, Editor> getFocusEditorModelReadOnly() throws IllegalStateException {
		checkPermissionAndState(Method.GETFOCUSEDITORMODELREADONLY);
		return standardToolkit.getFocusEditorModelReadOnly();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SyncSetModel<File> getFocusFileModel() throws IllegalStateException {
		checkPermissionAndState(Method.GETFOCUSFILEMODEL);
		return standardToolkit.getFocusFileModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SetModel<File> getFocusFileModelReadOnly() throws IllegalStateException {
		checkPermissionAndState(Method.GETFOCUSFILEMODELREADONLY);
		return standardToolkit.getFocusFileModelReadOnly();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SyncReferenceModel<Project> getFocusProjectModel() throws IllegalStateException {
		checkPermissionAndState(Method.GETFOCUSPROJECTMODEL);
		return standardToolkit.getFocusProjectModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReferenceModel<Project> getFocusProjectModelReadOnly() throws IllegalStateException {
		checkPermissionAndState(Method.GETFOCUSPROJECTMODELREADONLY);
		return standardToolkit.getFocusProjectModelReadOnly();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GuiManager getGuiManager() {
		checkPermissionAndState(Method.GETGUIMANAGER);
		return standardToolkit.getGuiManager();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SyncListModel<Project> getHoldProjectModel() throws IllegalStateException {
		checkPermissionAndState(Method.GETHOLDPROJECTMODEL);
		return standardToolkit.getHoldProjectModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ListModel<Project> getHoldProjectModelReadOnly() throws IllegalStateException {
		checkPermissionAndState(Method.GETHOLDPROJECTMODELREADONLY);
		return standardToolkit.getHoldProjectModelReadOnly();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SyncI18nHandler getLabelI18nHandler() throws IllegalStateException {
		checkPermissionAndState(Method.GETLABELI18NHANDLER);
		return standardToolkit.getLabelI18nHandler();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public I18nHandler getLabelI18nHandlerReadOnly() throws IllegalStateException {
		checkPermissionAndState(Method.GETLABELI18NHANDLERREADONLY);
		return standardToolkit.getLabelI18nHandlerReadOnly();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SyncLoggerHandler getLoggerHandler() throws IllegalStateException {
		checkPermissionAndState(Method.GETLOGGERHANDLER);
		return standardToolkit.getLoggerHandler();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LoggerHandler getLoggerHandlerReadOnly() throws IllegalStateException {
		checkPermissionAndState(Method.GETLOGGERHANDLERREADONLY);
		return standardToolkit.getLoggerHandlerReadOnly();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SyncI18nHandler getLoggerI18nHandler() throws IllegalStateException {
		checkPermissionAndState(Method.GETLOGGERI18NHANDLER);
		return standardToolkit.getLoggerI18nHandler();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public I18nHandler getLoggerI18nHandlerReadOnly() throws IllegalStateException {
		checkPermissionAndState(Method.GETLOGGERI18NHANDLERREADONLY);
		return standardToolkit.getLoggerI18nHandlerReadOnly();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MainFrame getMainFrame() throws IllegalStateException {
		checkPermissionAndState(Method.GETMAINFRAME);
		return standardToolkit.getMainFrame();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SyncExconfigModel getModalConfigModel() throws IllegalStateException {
		checkPermissionAndState(Method.GETMODALCONFIGMODEL);
		return standardToolkit.getModalConfigModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExconfigModel getModalConfigModelReadOnly() throws IllegalStateException {
		checkPermissionAndState(Method.GETMODALCONFIGMODELREADONLY);
		return standardToolkit.getModalConfigModelReadOnly();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PluginClassLoader getPluginClassLoader() throws IllegalStateException {
		checkPermissionAndState(Method.GETPLUGINCLASSLOADER);
		return standardToolkit.getPluginClassLoader();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<ProgramObverser> getProgramObversers() throws IllegalStateException {
		checkPermissionAndState(Method.GETPROGRAMOBVERSERS);
		return standardToolkit.getProgramObversers();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SyncMapModel<Project, Image> getProjectIconImageModel() throws IllegalStateException {
		checkPermissionAndState(Method.GETPROJECTICONIMAGEMODEL);
		return standardToolkit.getProjectIconImageModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MapModel<Project, Image> getProjectIconImageModelReadOnly() throws IllegalStateException {
		checkPermissionAndState(Method.GETPROJECTICONIMAGEMODELREADONLY);
		return standardToolkit.getProjectIconImageModelReadOnly();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SyncMapModel<Project, ProjectObverser> getProjectIconObvModel() throws IllegalStateException {
		checkPermissionAndState(Method.GETPROJECTICONOBVMODEL);
		return standardToolkit.getProjectIconObvModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SyncMapModel<String, Project> getProjectIndicateModel() throws IllegalStateException {
		checkPermissionAndState(Method.GETPROJECTINDICATEMODEL);
		return standardToolkit.getProjectIndicateModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public KeySetModel<String, ProjectProcessor> getProjectProcessors() throws IllegalStateException {
		checkPermissionAndState(Method.GETPROJECTPROCESSORS);
		return standardToolkit.getProjectProcessors();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getProperty(ProjWizProperty property) throws IllegalStateException {
		checkPermissionAndState(Method.GETPROPERTY);
		return standardToolkit.getProperty(property);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RuntimeState getRuntimeState() throws IllegalStateException {
		checkPermissionAndState(Method.GETRUNTIMESTATE);
		return standardToolkit.getRuntimeState();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasPermission(Method method) {
		Objects.requireNonNull(method, "入口参数 method 不能为 null。");
		return currentLevel.doubleValue() >= needLevel(method).doubleValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(String message) throws IllegalStateException {
		checkPermissionAndState(Method.INFO);
		standardToolkit.info(message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isMainFrameVisible() throws IllegalStateException {
		checkPermissionAndState(Method.ISMAINFRAMEVISIBLE);
		return standardToolkit.isMainFrameVisible();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean newMainFrame() throws IllegalStateException {
		checkPermissionAndState(Method.NEWMAINFRAME);
		return standardToolkit.newMainFrame();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void openFile(Project project, File file) throws IllegalStateException {
		checkPermissionAndState(Method.OPENFILE);
		standardToolkit.openFile(project, file);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream openFileInputStream(File file, String label)
			throws IllegalStateException, IllegalArgumentException, IOException {
		checkPermissionAndState(Method.OPENFILEINPUTSTREAM);
		return standardToolkit.openFileInputStream(file, label);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OutputStream openFileOutputStream(File file, String label)
			throws IllegalStateException, IllegalArgumentException, IOException {
		checkPermissionAndState(Method.OPENFILEOUTPUTSTREAM);
		return standardToolkit.openFileOutputStream(file, label);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeCoreConfigObverser(ExconfigObverser coreConfigObverser) throws IllegalStateException {
		checkPermissionAndState(Method.REMOVECORECONFIGOBVERSER);
		return standardToolkit.removeCoreConfigObverser(coreConfigObverser);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeObverserFromFile(File file, FileObverser obverser) {
		checkPermissionAndState(Method.REMOVEOBVERSERFROMFILE);
		return standardToolkit.removeObverserFromFile(file, obverser);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeProgramObverser(ProgramObverser obverser) throws IllegalStateException {
		checkPermissionAndState(Method.REMOVEPROGRAMOBVERSER);
		return standardToolkit.removeProgramObverser(obverser);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setExitCode(int exitCode) throws IllegalStateException {
		checkPermissionAndState(Method.SETEXITCODE);
		standardToolkit.setExitCode(exitCode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRuntimeState(RuntimeState runtimeState) throws IllegalStateException {
		checkPermissionAndState(Method.SETRUNTIMESTATE);
		standardToolkit.setRuntimeState(runtimeState);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DialogOption showConfirmDialog(Object message) throws IllegalStateException {
		checkPermissionAndState(Method.SHOWCONFIRMDIALOG);
		return standardToolkit.showConfirmDialog(message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DialogOption showConfirmDialog(Object message, String title, DialogOptionCombo dialogOptionCombo)
			throws IllegalStateException {
		checkPermissionAndState(Method.SHOWCONFIRMDIALOG);
		return standardToolkit.showConfirmDialog(message, title, dialogOptionCombo);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DialogOption showConfirmDialog(Object message, String title, DialogOptionCombo dialogOptionCombo,
			DialogMessage dialogMessage) throws IllegalStateException {
		checkPermissionAndState(Method.SHOWCONFIRMDIALOG);
		return standardToolkit.showConfirmDialog(message, title, dialogOptionCombo, dialogMessage);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DialogOption showConfirmDialog(Object message, String title, DialogOptionCombo dialogOptionCombo,
			DialogMessage dialogMessage, Icon icon) throws IllegalStateException {
		checkPermissionAndState(Method.SHOWCONFIRMDIALOG);
		return standardToolkit.showConfirmDialog(message, title, dialogOptionCombo, dialogMessage, icon);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showExternalWindow(String key) {
		checkPermissionAndState(Method.SHOWEXTERNALWINDOW);
		standardToolkit.showExternalWindow(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showExternalWindow(WindowSuppiler suppiler) {
		checkPermissionAndState(Method.SHOWEXTERNALWINDOW);
		standardToolkit.showExternalWindow(suppiler);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String showInputDialog(Object message) throws IllegalStateException {
		checkPermissionAndState(Method.SHOWINPUTDIALOG);
		return standardToolkit.showInputDialog(message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String showInputDialog(Object message, Object initialSelectionValue) throws IllegalStateException {
		checkPermissionAndState(Method.SHOWINPUTDIALOG);
		return standardToolkit.showInputDialog(message, initialSelectionValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String showInputDialog(Object message, String title, DialogMessage dialogMessage)
			throws IllegalStateException {
		checkPermissionAndState(Method.SHOWINPUTDIALOG);
		return standardToolkit.showInputDialog(message, title, dialogMessage);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object showInputDialog(Object message, String title, DialogMessage dialogMessage, Icon icon,
			Object[] selectionValues, Object initialSelectionValue) throws IllegalStateException {
		checkPermissionAndState(Method.SHOWINPUTDIALOG);
		return standardToolkit.showInputDialog(message, title, dialogMessage, icon, selectionValues,
				initialSelectionValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showMessageDialog(Object message) throws IllegalStateException {
		checkPermissionAndState(Method.SHOWMESSAGEDIALOG);
		standardToolkit.showMessageDialog(message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showMessageDialog(Object message, String title, DialogMessage dialogMessage)
			throws IllegalStateException {
		checkPermissionAndState(Method.SHOWMESSAGEDIALOG);
		standardToolkit.showMessageDialog(message, title, dialogMessage);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showMessageDialog(Object message, String title, DialogMessage dialogMessage, Icon icon)
			throws IllegalStateException {
		checkPermissionAndState(Method.SHOWMESSAGEDIALOG);
		standardToolkit.showMessageDialog(message, title, dialogMessage, icon);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int showOptionDialog(Object message, String title, DialogOptionCombo dialogOptionCombo,
			DialogMessage dialogMessage, Icon icon, Object[] options, Object initialValue)
			throws IllegalStateException {
		checkPermissionAndState(Method.SHOWOPTIONDIALOG);
		return standardToolkit.showOptionDialog(message, title, dialogOptionCombo, dialogMessage, icon, options,
				initialValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start() throws IllegalStateException {
		checkPermissionAndState(Method.START);
		standardToolkit.start();
	}

	public void stop() {
		synchronized (stopFlagLock) {
			stopFlag = true;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void submitTask(Task task, BackgroundType type) throws IllegalStateException {
		checkPermissionAndState(Method.SUBMITTASK);
		standardToolkit.submitTask(task, type);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "LeveledToolkit [standardToolkit=" + standardToolkit + ", currentLevel=" + currentLevel + ", stopFlag="
				+ stopFlag + "]";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void trace(String message) throws IllegalStateException {
		checkPermissionAndState(Method.TRACE);
		standardToolkit.trace(message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void tryExit() throws IllegalStateException {
		checkPermissionAndState(Method.TRYEXIT);
		standardToolkit.tryExit();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(String message) throws IllegalStateException {
		checkPermissionAndState(Method.WARN);
		standardToolkit.warn(message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(String message, Throwable t) throws IllegalStateException {
		checkPermissionAndState(Method.WARN);
		standardToolkit.warn(message, t);
	}

	private void checkPermissionAndState(Method method) throws IllegalStateException {
		Objects.requireNonNull(method, "入口参数 method 不能为 null。");

		synchronized (stopFlagLock) {
			if (stopFlag)
				throw new IllegalStateException("这个工具包已经被停用。");
		}
		if (currentLevel.doubleValue() < needLevel(method).doubleValue())
			throw new IllegalStateException(String.format("方法 %s 需要的最小权限为 %s，而当前的权限为 %s。", method,
					needLevel(method).toString(), currentLevel.toString()));
	}

	private NumberValue needLevel(Method method) {
		Objects.requireNonNull(method, "入口参数 method 不能为 null。");

		if (!Constants.LEVELEDTOOLKIT_MIN_LEVEL.containsKey(method)) {
			throw new IllegalArgumentException(String.format("权限表中没有指定的方法：%s。", method));
		}

		return Constants.LEVELEDTOOLKIT_MIN_LEVEL.get(method);
	}

}
