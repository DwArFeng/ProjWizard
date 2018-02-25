package com.dwarfeng.projwiz.core.model.struct;

import java.awt.Image;
import java.util.Collection;
import java.util.Collections;
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
import com.dwarfeng.projwiz.core.model.cm.ComponentModel;
import com.dwarfeng.projwiz.core.model.cm.SyncComponentModel;
import com.dwarfeng.projwiz.core.model.cm.SyncToolkitPermModel;
import com.dwarfeng.projwiz.core.model.cm.ToolkitPermModel;
import com.dwarfeng.projwiz.core.model.eum.DialogMessage;
import com.dwarfeng.projwiz.core.model.eum.DialogOption;
import com.dwarfeng.projwiz.core.model.eum.DialogOptionCombo;
import com.dwarfeng.projwiz.core.model.eum.ProjWizProperty;
import com.dwarfeng.projwiz.core.model.io.PluginClassLoader;
import com.dwarfeng.projwiz.core.model.obv.FileObverser;
import com.dwarfeng.projwiz.core.model.obv.ProjectObverser;
import com.dwarfeng.projwiz.core.view.gui.MainFrame;
import com.dwarfeng.projwiz.core.view.struct.ConfirmDialogSetting;
import com.dwarfeng.projwiz.core.view.struct.GuiManager;
import com.dwarfeng.projwiz.core.view.struct.InputDialogSetting;
import com.dwarfeng.projwiz.core.view.struct.MessageDialogSetting;
import com.dwarfeng.projwiz.core.view.struct.ProjectFileChooserSetting;
import com.dwarfeng.projwiz.core.view.struct.SystemFileChooserSetting;
import com.dwarfeng.projwiz.core.view.struct.WindowSuppiler;

/**
 * 等级与特权工具包。
 * 
 * <p>
 * LnpToolkit的权限管理分为两个方面：权限等级和特权。
 * <p>
 * 权限等级基于 LeveledToolkit。<br>
 * 特权允许Toolkit进行一部分特定的权限之外的操作。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public final class LnpToolkit implements Toolkit {

	/** 工具包权限模型。 */
	protected final ToolkitPermModel toolkitPermModel;
	/** 工具包当前的权限等级。 */
	protected final int permLevel;
	/** 具有完整权限的标准工具包。 */
	protected final Toolkit standardToolkit;
	/** 该工具包的特权。 */
	protected final Collection<Method> privileges;

	private final Object stopFlagLock = new Object();
	private boolean stopFlag = false;

	/**
	 * 新实例。
	 * 
	 * @param toolkitPermModel
	 * @param permLevel
	 * @param standardToolkit
	 * @param privileges
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	public LnpToolkit(ToolkitPermModel toolkitPermModel, int permLevel, Toolkit standardToolkit,
			Collection<Method> privileges) {
		Objects.requireNonNull(toolkitPermModel, "入口参数 toolkitPermModel 不能为 null。");
		Objects.requireNonNull(standardToolkit, "入口参数 standardToolkit 不能为 null。");
		Objects.requireNonNull(privileges, "入口参数 privileges 不能为 null。");

		this.toolkitPermModel = toolkitPermModel;
		this.permLevel = permLevel;
		this.standardToolkit = standardToolkit;
		this.privileges = privileges;
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
	public SyncMapModel<Class<? extends Component>, ReferenceModel<Toolkit>> getCmpoentToolkitModel()
			throws IllegalStateException {
		checkPermissionAndState(Method.WARN);
		return standardToolkit.getCmpoentToolkitModel();
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
	public ComponentModel getComponentModelReadOnly() throws IllegalStateException {
		checkPermissionAndState(Method.GETCOMPONENTMODELREADONLY);
		return standardToolkit.getComponentModelReadOnly();
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
	 * 获取工具包的权限等级。
	 * 
	 * @return 工具包的权限等级。
	 */
	public int getPermLevel() {
		return permLevel;
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
	 * 获取该工具包的特权方法。
	 * 
	 * @return 该工具包的特权方法。
	 */
	public Collection<Method> getPrivileges() {
		return Collections.unmodifiableCollection(privileges);
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
	public SyncToolkitPermModel getToolkitPermModel() throws IllegalStateException {
		checkPermissionAndState(Method.WARN);
		return standardToolkit.getToolkitPermModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ToolkitPermModel getToolkitPermModelReadOnly() throws IllegalStateException {
		checkPermissionAndState(Method.GETTOOLKITPERMMODELREADONLY);
		return standardToolkit.getToolkitPermModelReadOnly();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SyncExconfigModel getViewConfigModel() throws IllegalStateException {
		checkPermissionAndState(Method.GETVIEWCONFIGMODEL);
		return standardToolkit.getViewConfigModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExconfigModel getViewConfigModelReadOnly() throws IllegalStateException {
		checkPermissionAndState(Method.GETVIEWCONFIGMODELREADONLY);
		return standardToolkit.getViewConfigModelReadOnly();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasPermission(Method method) {
		Objects.requireNonNull(method, "入口参数 method 不能为 null。");
		return toolkitPermModel.hasPerm(method, permLevel) || privileges.contains(method);
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
	public boolean removeCoreConfigObverser(ExconfigObverser coreConfigObverser) throws IllegalStateException {
		checkPermissionAndState(Method.REMOVECORECONFIGOBVERSER);
		return standardToolkit.removeCoreConfigObverser(coreConfigObverser);
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
	public DialogOption showConfirmDialog(ConfirmDialogSetting setting) throws IllegalStateException {
		checkPermissionAndState(Method.SHOWCONFIRMDIALOG);
		return standardToolkit.showConfirmDialog(setting);
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
	public Object showInputDialog(InputDialogSetting setting) throws IllegalStateException {
		checkPermissionAndState(Method.SHOWINPUTDIALOG);
		return standardToolkit.showInputDialog(setting);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showMessageDialog(MessageDialogSetting setting) throws IllegalStateException {
		checkPermissionAndState(Method.SHOWMESSAGEDIALOG);
		standardToolkit.showMessageDialog(setting);
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
		return "LeveledToolkit [standardToolkit=" + standardToolkit + ", permLevel=" + permLevel + ", stopFlag="
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
		if (!toolkitPermModel.hasPerm(method, permLevel) && !privileges.contains(method)) {
			throw new IllegalStateException(String.format("方法 %s 需要的最小权限为 %d，而当前的权限为 %d，且没有特权。", method,
					toolkitPermModel.getPermLevel(method), permLevel));
		}
	}

}
