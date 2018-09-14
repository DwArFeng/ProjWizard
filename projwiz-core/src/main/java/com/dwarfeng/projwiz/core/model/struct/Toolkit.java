package com.dwarfeng.projwiz.core.model.struct;

import java.awt.Image;
import java.awt.Window;
import java.util.Set;

import com.dwarfeng.dutil.basic.cna.model.ListModel;
import com.dwarfeng.dutil.basic.cna.model.MapModel;
import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.SetModel;
import com.dwarfeng.dutil.basic.cna.model.SyncListModel;
import com.dwarfeng.dutil.basic.cna.model.SyncMapModel;
import com.dwarfeng.dutil.basic.cna.model.SyncReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.SyncSetModel;
import com.dwarfeng.dutil.basic.prog.ProgramObverser;
import com.dwarfeng.dutil.basic.prog.RuntimeState;
import com.dwarfeng.dutil.develop.backgr.Background;
import com.dwarfeng.dutil.develop.backgr.Task;
import com.dwarfeng.dutil.develop.i18n.I18nHandler;
import com.dwarfeng.dutil.develop.i18n.SyncI18nHandler;
import com.dwarfeng.dutil.develop.logger.LoggerHandler;
import com.dwarfeng.dutil.develop.logger.SyncLoggerHandler;
import com.dwarfeng.dutil.develop.resource.ResourceHandler;
import com.dwarfeng.dutil.develop.resource.SyncResourceHandler;
import com.dwarfeng.dutil.develop.setting.SettingHandler;
import com.dwarfeng.dutil.develop.setting.SyncSettingHandler;
import com.dwarfeng.dutil.develop.setting.obv.SettingObverser;
import com.dwarfeng.dutil.develop.timer.Plain;
import com.dwarfeng.dutil.develop.timer.Timer;
import com.dwarfeng.projwiz.core.model.cm.ModuleModel;
import com.dwarfeng.projwiz.core.model.cm.SyncModuleModel;
import com.dwarfeng.projwiz.core.model.cm.SyncToolkitPermModel;
import com.dwarfeng.projwiz.core.model.cm.ToolkitPermModel;
import com.dwarfeng.projwiz.core.model.eum.ProjWizProperty;
import com.dwarfeng.projwiz.core.model.io.PluginClassLoader;
import com.dwarfeng.projwiz.core.model.obv.FileObverser;
import com.dwarfeng.projwiz.core.model.obv.ProjectObverser;
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
 * 功能处理器。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface Toolkit {

	/**
	 * 后台的类型。
	 * 
	 * @author DwArFeng
	 * @since 0.0.1-alpha
	 */
	public enum BackgroundType {
		/** 并发后台。 */
		CONCURRENT,
		/** 先进先出后台。 */
		FIFO,
	}

	public enum Method {
		ADDCORESETTINGOBVERSER, //
		ADDPROGRAMOBVERSER,//
		CHOOSEMODULE,//
		CHOOSEPROJECTFILE, //
		CHOOSESYSTEMFILE, //
		CLEARPROGRAMOBVERSER, //
		DEBUG, //
		DISPOSEMAINFRAME, //
		ERROR, //
		EXIT, //
		FATAL, //
		GETANCHORFILEMODEL, //
		GETANCHORFILEMODELREADONLY, //
		GETBACKGROUND, //
		GETBACKGROUNDREADONLY, //
		GETCFGHANDLER, //
		GETCFGHANDLERREADONLY, //
		GETCORESETTINGHANDLER, //
		GETCORESETTINGHANDLERREADONLY, //
		GETEDITORMODEL, //
		GETEDITORMODELREADONLY, //
		GETEXITCODE, //
		GETEXTERNALWINDOWMODEL, //
		GETEXTERNALWINDOWMODELREADONLY, //
		GETFILEICONIMAGEMODEL, //
		GETFILEICONIMAGEMODELREADONLY(), //
		GETFILEICONOBVMODEL, //
		GETFOCUSEDITORMODEL, //
		GETFOCUSEDITORMODELREADONLY, //
		GETFOCUSFILEMODEL, //
		GETFOCUSFILEMODELREADONLY, //
		GETFOCUSPROJECTMODEL, //
		GETFOCUSPROJECTMODELREADONLY, //
		GETGUIMANAGER, //
		GETHOLDPROJECTMODEL, //
		GETHOLDPROJECTMODELREADONLY, //
		GETLABELI18NHANDLER, //
		GETLABELI18NHANDLERREADONLY, //
		GETLOGGERHANDLER, //
		GETLOGGERHANDLERREADONLY, //
		GETLOGGERI18NHANDLER, //
		GETLOGGERI18NHANDLERREADONLY, //
		GETMAINFRAME, //
		GETMODULEMODEL, //
		GETMODULEMODELREADONLY, //
		GETMODULETOOLKITMODEL, //
		GETPLUGINCLASSLOADER, //
		GETPROGRAMOBVERSERS, //
		GETPROJECTICONIMAGEMODEL, //
		GETPROJECTICONIMAGEMODELREADONLY, //
		GETPROJECTICONOBVMODEL, //
		GETPROPERTY, //
		GETRUNTIMESTATE, //
		GETTIMER, //
		GETTIMERREADONLY, //
		GETTOOLKITPERMMODEL, //
		GETTOOLKITPERMMODELREADONLY, //
		GETVIEWSETTINGHANDLER, //
		GETVIEWSETTINGHANDLERREADONLY, //
		INFO, //
		ISMAINFRAMEVISIBLE, //
		NEWMAINFRAME, //
		OPENFILE, //
		REMOVECORESETTINGOBVERSER, //
		REMOVEPLAIN, //
		REMOVEPROGRAMOBVERSER, //
		SCHEDULEPLAIN, //
		SETEXITCODE, //
		SETRUNTIMESTATE, //
		SHOWCONFIRMDIALOG, //
		SHOWDIALOG, //
		SHOWEXTERNALWINDOW, //
		SHOWINPUTDIALOG, //
		SHOWMESSAGEDIALOG, //
		SHOWOPTIONDIALOG, //
		START, //
		SUBMITTASK, //
		TRACE, //
		TRYEXIT, //
		WARN,//
	}

	/**
	 * 向程序中添加一个核心配置观察器。
	 * 
	 * @param coreSettingObverser
	 *            指定的核心配置观察器。
	 * @return 是否添加成功。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public boolean addCoreSettingObverser(SettingObverser coreSettingObverser) throws IllegalStateException;

	/**
	 * 移除指定的程序观察器。
	 * 
	 * @param obverser
	 *            指定的程序观察器。
	 * @return 是否添加成功。
	 */
	public boolean addProgramObverser(ProgramObverser obverser) throws IllegalStateException;

	/**
	 * 选择组件。
	 * 
	 * <p>
	 * 该方法会打开一个组件选择对话框，并让用户在多个组件中选择一个或多个组件。
	 * <p>
	 * 该方法保证返回的组件数组中不含有 <code>null</code> 元素。
	 * 
	 * @param setting
	 *            指定的设置。
	 * @return 选择的组件数组，如果没有，则长度为0。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	public Module[] chooseModule(ModuleChooserSetting setting) throws IllegalStateException;

	/**
	 * 选择一个工程文件。
	 * <p>
	 * 该方法会打开一个文件选择对话框，并让
	 * <p>
	 * 该方法保证返回的组件数组中不含有 <code>null</code> 元素。
	 * 
	 * @param setting
	 *            指定的设置。
	 * @return 选择的文件数组，如果没有，则长度为0。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	public File[] chooseProjectFile(ProjectFileChooserSetting setting) throws IllegalMonitorStateException;

	/**
	 * 选择一个系统文件。
	 * <p>
	 * 该方法会打开一个文件选择对话框，并让
	 * <p>
	 * 该方法保证返回的组件数组中不含有 <code>null</code> 元素。
	 * 
	 * @param setting
	 *            指定的设置。
	 * @return 选择的文件数组，如果没有，则长度为0。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	public java.io.File[] chooseSystemFile(SystemFileChooserSetting setting) throws IllegalStateException;

	/**
	 * 清除所有的GUI观察器（慎用）。
	 */
	public void clearProgramObverser() throws IllegalStateException;

	/**
	 * 使用指定的记录器处理器 <code>debug</code> 一条信息。
	 * 
	 * @param message
	 *            指定的信息。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public void debug(String message) throws IllegalStateException;

	/**
	 * 释放主界面。
	 * 
	 * @return 是否释放成功。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public boolean disposeMainFrame() throws IllegalStateException;

	/**
	 * 使用指定的记录器处理器 <code>error</code> 一条信息。
	 * 
	 * @param message
	 *            指定的信息。
	 * @param t
	 *            指定的可抛出对象。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public void error(String message, Throwable t) throws IllegalStateException;

	/**
	 * 退出程序。
	 * 
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public void exit() throws IllegalStateException;

	/**
	 * 使用指定的记录器处理器 <code>fatal</code> 一条信息。
	 * 
	 * @param message
	 *            指定的信息。
	 * @param t
	 *            指定的可抛出对象。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public void fatal(String message, Throwable t) throws IllegalStateException;

	/**
	 * 获取锚点文件模型。
	 * 
	 * @return 锚点文件模型。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public SyncReferenceModel<File> getAnchorFileModel() throws IllegalStateException;

	/**
	 * 获取锚点文件模型。
	 * <p>
	 * 该模型是只读的。
	 * 
	 * @return 锚点文件模型。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public ReferenceModel<File> getAnchorFileModelReadOnly() throws IllegalStateException;

	/**
	 * 获取程序中的后台。
	 * 
	 * @param type
	 *            后台的类型。
	 * @return 程序中的后台。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public Background getBackground(BackgroundType type) throws IllegalStateException;

	/**
	 * 获取程序中的后台。
	 * 
	 * <p>
	 * 获取的后台是只读的。
	 * 
	 * @param type
	 *            后台的类型。
	 * @return 程序中的后台。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public Background getBackgroundReadOnly(BackgroundType type) throws IllegalStateException;

	/**
	 * 获取配置处理器。
	 * 
	 * @return 资源处理器。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public SyncResourceHandler getCfgHandler() throws IllegalStateException;

	/**
	 * 获取配置处理器。
	 * <p>
	 * 该资源处理器是只读的。
	 * 
	 * @return 资源处理器。
	 */
	public ResourceHandler getCfgHandlerReadOnly() throws IllegalStateException;

	/**
	 * 获取程序中的核心配置模型。
	 * 
	 * @return 程序中的核心配置模型。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public SyncSettingHandler getCoreSettingHandlerl() throws IllegalStateException;

	/**
	 * 获取程序中的核心配置模型。
	 * <p>
	 * 配置模型是只读的。
	 * 
	 * @return 程序中的核心配置模型。
	 */
	public SettingHandler getCoreSettingHandlerReadOnly() throws IllegalStateException;

	/**
	 * 获取编辑器模型。
	 * 
	 * @return 编辑器模型。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public SyncMapModel<ProjectFilePair, Editor> getEditorModel() throws IllegalStateException;

	/**
	 * 获取编辑器模型。
	 * 
	 * <p>
	 * 编辑器模型是只读的。
	 * 
	 * @return 编辑器模型。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public SyncMapModel<ProjectFilePair, Editor> getEditorModelReadOnly() throws IllegalStateException;

	/**
	 * 获取程序的退出代码。
	 * <p>
	 * 只有当程序退出时，该代码才有效。 虽然在程序正在运行或还未运行的时候仍能获取该代码，但是此时该代码是无效的。
	 * 
	 * @return 程序的退出代码。
	 */
	public int getExitCode() throws IllegalStateException;

	/**
	 * 获取外部窗口模型。
	 * 
	 * @return 外部窗口模型。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public SyncSetModel<Window> getExternalWindowModel() throws IllegalStateException;

	/**
	 * 获取外部窗口模型。
	 * <p>
	 * 外部窗口模型是只读的。
	 * 
	 * @return 外部窗口模型。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public SetModel<Window> getExternalWindowModelReadOnly() throws IllegalStateException;

	/**
	 * 获取文件图标图片模型。
	 * 
	 * @return 文件图标图片模型。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public SyncMapModel<File, Image> getFileIconImageModel() throws IllegalStateException;

	// /**
	// * 获取外部窗口模型。
	// *
	// * @return 外部窗口模型。
	// * @throws IllegalStateException
	// * 因为没有权限而抛出的异常。
	// */
	// public SyncKeySetModel<String, WindowSuppiler> getExternalWindowModel()
	// throws IllegalStateException;
	//
	// /**
	// * 获取外部窗口模型。
	// *
	// * <p>
	// * 外部窗口模型是只读的。
	// *
	// * @return 外部窗口模型。
	// * @throws IllegalStateException
	// * 因为没有权限而抛出的异常。
	// */
	// public KeySetModel<String, WindowSuppiler>
	// getExternalWindowModelReadOnly() throws IllegalStateException;

	/**
	 * 获取图片图标图片模型。
	 * 
	 * <p>
	 * 图片图标图片模型是只读的。
	 * 
	 * @return 图片图标图片模型。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public MapModel<File, Image> getFileIconImageModelReadOnly() throws IllegalStateException;

	/**
	 * 获取文件图片侦听器模型。
	 * 
	 * @return 文件图片侦听器模型。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public SyncMapModel<File, FileObverser> getFileIconObvModel() throws IllegalStateException;

	/**
	 * 获取焦点编辑器模型。
	 * <p>
	 * 该模型是只读的。
	 * 
	 * @return 焦点编辑器模型。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public SyncMapModel<Project, Editor> getFocusEditorModel() throws IllegalStateException;

	/**
	 * 获取程序中的后台。
	 * 
	 * @param type
	 *            后台的类型。
	 * @return 程序中的后台。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public MapModel<Project, Editor> getFocusEditorModelReadOnly() throws IllegalStateException;

	/**
	 * 获取焦点文件模型。
	 * 
	 * @return 焦点文件模型。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public SyncSetModel<File> getFocusFileModel() throws IllegalStateException;

	/**
	 * 获取焦点文件模型。
	 * <p>
	 * 该模型是只读的。
	 * 
	 * @return 焦点文件模型。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public SetModel<File> getFocusFileModelReadOnly() throws IllegalStateException;

	/**
	 * 获取焦点工程模型。
	 * 
	 * @return 焦点工程模型。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public SyncReferenceModel<Project> getFocusProjectModel() throws IllegalStateException;

	/**
	 * 获取焦点工程模型。
	 * <p>
	 * 该模型是只读的。
	 * 
	 * @return 焦点工程模型。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public ReferenceModel<Project> getFocusProjectModelReadOnly() throws IllegalStateException;

	/**
	 * 获取 GUI 管理器。
	 * 
	 * @return GUI 管理器。
	 */
	public GuiManager getGuiManager();

	/**
	 * 获取持有工程模型。
	 * 
	 * @return 持有工程模型。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public SyncListModel<Project> getHoldProjectModel() throws IllegalStateException;

	/**
	 * 获取持有工程模型。
	 * <p>
	 * 该模型是只读的。
	 * 
	 * @return 持有工程模型。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public ListModel<Project> getHoldProjectModelReadOnly() throws IllegalStateException;

	/**
	 * 获取标签国际化处理器。
	 * 
	 * @return 标签国际化处理器。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public SyncI18nHandler getLabelI18nHandler() throws IllegalStateException;

	/**
	 * 获取标签国际化处理器。
	 * 
	 * <p>
	 * 标签国际化处理器是只读的。
	 * 
	 * @return 标签国际化处理器。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public I18nHandler getLabelI18nHandlerReadOnly() throws IllegalStateException;

	/**
	 * 获取程序中的记录器处理器。
	 * 
	 * @return 程序中的记录器处理器。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public SyncLoggerHandler getLoggerHandler() throws IllegalStateException;

	/**
	 * 获取程序中的记录器处理器。
	 * <p>
	 * 获取的记录器是只读的。
	 * 
	 * @return 程序中的记录器处理器。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public LoggerHandler getLoggerHandlerReadOnly() throws IllegalStateException;

	/**
	 * 获取记录器国际化处理器。
	 * 
	 * @return 记录器国际化处理器。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public SyncI18nHandler getLoggerI18nHandler() throws IllegalStateException;

	/**
	 * 获取记录器国际化处理器。
	 * 
	 * <p>
	 * 记录器国际化处理器是只读的。
	 * 
	 * @return 记录器国际化处理器。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public I18nHandler getLoggerI18nHandlerReadOnly() throws IllegalStateException;

	/**
	 * 获取程序中的主界面。
	 * 
	 * @return 程序中的主界面。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public MainFrame getMainFrame() throws IllegalStateException;

	/**
	 * 获取组件模型。
	 * 
	 * @return 组件模型。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public SyncModuleModel getModuleModel() throws IllegalStateException;

	/**
	 * 获取组件模型。
	 * 
	 * <p>
	 * 组件模型是只读的。
	 * 
	 * @return 组件模型。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public ModuleModel getModuleModelReadOnly() throws IllegalStateException;

	/**
	 * 获取程序中的组件-映射模型。
	 * 
	 * @return 程序中的组件-映射模型。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public SyncMapModel<Class<? extends Module>, ReferenceModel<Toolkit>> getModuleToolkitModel()
			throws IllegalStateException;

	/**
	 * 获取接口中的插件类加载器。
	 * <p>
	 * 该插件类加载器是只读的。
	 * 
	 * @return 接口中的插件类加载器。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public PluginClassLoader getPluginClassLoader() throws IllegalStateException;

	/**
	 * 获取程序的程序观察器集合。
	 * 
	 * @return 程序中的程序观察器集合。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public Set<ProgramObverser> getProgramObversers() throws IllegalStateException;

	/**
	 * 获取工程图标图片模型。
	 * 
	 * @return 工程图标图片模型。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public SyncMapModel<Project, Image> getProjectIconImageModel() throws IllegalStateException;

	/**
	 * 获取工程图标图片模型。
	 * 
	 * <p>
	 * 工程图标图片模型是只读的。
	 * 
	 * @return 工程图标图片模型。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public MapModel<Project, Image> getProjectIconImageModelReadOnly() throws IllegalStateException;

	/**
	 * 获取工程图片侦听器模型。
	 * 
	 * @return 工程图片侦听器模型。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public SyncMapModel<Project, ProjectObverser> getProjectIconObvModel() throws IllegalStateException;

	/**
	 * 获取程序属性。
	 * 
	 * @param property
	 *            程序属性键。
	 * @return 属性键对应的属性。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public String getProperty(ProjWizProperty property) throws IllegalStateException;

	/**
	 * 获取程序的运行状态。
	 * 
	 * @return 程序的运行状态。
	 */
	public RuntimeState getRuntimeState() throws IllegalStateException;

	/**
	 * 获取程序中的计时器。
	 * 
	 * @return 程序中的计时器。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public Timer getTimer() throws IllegalStateException;

	/**
	 * 获取程序中的计时器。
	 * 
	 * <p>
	 * 计时器是只读的。
	 * 
	 * @return 程序中的计时器。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public Timer getTimerReadOnly() throws IllegalStateException;

	/**
	 * 获取程序中的工具包权限模型。
	 * 
	 * @return 程序中的工具包权限模型。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public SyncToolkitPermModel getToolkitPermModel() throws IllegalStateException;

	/**
	 * 获取程序中的工具包权限模型。
	 * 
	 * <p>
	 * 工具包权限模型是只读的。
	 * 
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public ToolkitPermModel getToolkitPermModelReadOnly() throws IllegalStateException;

	/**
	 * 获取视图配置模型。
	 * 
	 * @return 视图配置模型。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public SyncSettingHandler getViewSettingHandler() throws IllegalStateException;

	/**
	 * 获取程序中的视图配置模型。
	 * 
	 * <p>
	 * 视图配置模型是只读的。
	 * 
	 * @return 程序中的视图配置模型。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public SettingHandler getViewSettingHandlerReadOnly() throws IllegalStateException;

	/**
	 * 返回工具包是否拥有权限执行指定的方法。
	 * 
	 * @param method
	 *            指定的方法。
	 * @return 工具是否拥有权限执行指定的方法。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public boolean hasPermission(Method method);

	/**
	 * 使用指定的记录器处理器 <code>info</code> 一条信息。
	 * 
	 * @param message
	 *            指定的信息。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public void info(String message) throws IllegalStateException;

	/**
	 * 返回主界面是否可见。
	 * 
	 * @return 主界面是否可见。
	 */
	public boolean isMainFrameVisible() throws IllegalStateException;

	/**
	 * 新建主界面。
	 * 
	 * @return 是否新建成功。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public boolean newMainFrame() throws IllegalStateException;

	/**
	 * 判断该工具包是否没有权限指定指定的方法。
	 * 
	 * @param method
	 *            指定的方法。
	 * @return 该工具包是否没有权限执行指定的方法。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public default boolean notHasPermission(Method method) {
		return !hasPermission(method);
	}

	/**
	 * 打开指定的文件。
	 * 
	 * @param project
	 *            指定的文件所在的工程。
	 * @param file
	 *            指定的文件。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public void openFile(Project project, File file) throws IllegalStateException;

	/**
	 * 向程序中移除一个核心配置观察器。
	 * 
	 * @param coreSettingObverser
	 *            指定的核心配置观察器。
	 * @return 是否移除成功。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public boolean removeCoreSettingObverser(SettingObverser coreSettingObverser) throws IllegalStateException;

	/**
	 * 从计时器中移除指定的计划。
	 * 
	 * @param plain
	 *            指定的计划。
	 * @return 该操作是否移除成功。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public boolean removePlain(Plain plain) throws IllegalStateException;

	/**
	 * 移除指定的程序观察器。
	 * 
	 * @param obverser
	 *            指定的程序观察器。
	 * @return 是否移除成功。
	 */
	public boolean removeProgramObverser(ProgramObverser obverser) throws IllegalStateException;

	/**
	 * 向计时器中添加一条计划。
	 * 
	 * @param plain
	 *            指定的计划。
	 * @return 该计划是否添加成功。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public boolean schedulePlain(Plain plain) throws IllegalStateException;

	/**
	 * 设置程序的退出代码。
	 * 
	 * @param exitCode
	 *            指定的退出代码。
	 */
	public void setExitCode(int exitCode) throws IllegalStateException;

	/**
	 * 设置程序中的运行状态。
	 * <p>
	 * 参数不能为 <code>null</code>。
	 * 
	 * @param runtimeState
	 *            程序的运行状态。
	 */
	public void setRuntimeState(RuntimeState runtimeState) throws IllegalStateException;

	/**
	 * 显示一个确认对话框。
	 * 
	 * @param setting
	 *            确认对话框的设置。
	 * @return 用户选择的选项。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public DialogOption showConfirmDialog(ConfirmDialogSetting setting) throws IllegalStateException;

	/**
	 * 展示一个外部窗口。
	 * <p>
	 * 该方法会在主界面的中心展示指定的外部窗口，并持有该窗口。 <br>
	 * 当程序关闭时，会关闭所有打开的外部窗口。
	 * 
	 * @param window
	 *            指定的外部窗口。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public void showExternalWindow(Window window) throws IllegalStateException;

	/**
	 * 显示一个输入对话框。
	 * 
	 * @param setting
	 *            确认对话框的设置。
	 * @return 用户输入的对象。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public Object showInputDialog(InputDialogSetting setting) throws IllegalStateException;

	/**
	 * 显示一个信息对话框。
	 * 
	 * @param setting
	 *            信息对话框的设置。
	 * @return 用户选择的选项。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public void showMessageDialog(MessageDialogSetting setting) throws IllegalStateException;

	/**
	 * 在前台显示一个选项对话框。
	 * 
	 * @param setting
	 *            选项对话框的设置。
	 * @return 用户选择的选项的序号，或 <code>-1</code> （没有选择任何值或按下取消）。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public int showOptionDialog(OptionDialogSetting setting) throws IllegalStateException;

	/**
	 * 启动程序。
	 * 
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public void start() throws IllegalStateException;

	/**
	 * 向程序的后台提交一个任务。
	 * 
	 * @param task
	 *            指定的任务。
	 * @param type
	 *            向何种后台提交任务。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public void submitTask(Task task, BackgroundType type) throws IllegalStateException;

	/**
	 * 使用指定的记录器处理器 <code>trace</code> 一条信息。
	 * 
	 * @param message
	 *            指定的信息。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public void trace(String message) throws IllegalStateException;

	/**
	 * 尝试退出程序。
	 * 
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public void tryExit() throws IllegalStateException;

	/**
	 * 使用指定的记录器处理器 <code>warn</code> 一条信息。
	 * 
	 * @param message
	 *            指定的信息。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public void warn(String message) throws IllegalStateException;

	/**
	 * 使用指定的记录器处理器 <code>warn</code> 一条信息。
	 * 
	 * @param message
	 *            指定的信息。
	 * @param t
	 *            指定的可抛出对象。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public void warn(String message, Throwable t) throws IllegalStateException;

}
