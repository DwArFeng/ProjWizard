package com.dwarfeng.projwiz.core.model.struct;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import com.dwarfeng.projwiz.core.model.cm.SyncProcessorConfigHandler;
import com.dwarfeng.projwiz.core.model.eum.DialogMessage;
import com.dwarfeng.projwiz.core.model.eum.DialogOption;
import com.dwarfeng.projwiz.core.model.eum.DialogOptionCombo;
import com.dwarfeng.projwiz.core.model.eum.ProjWizProperty;
import com.dwarfeng.projwiz.core.model.io.PluginClassLoader;
import com.dwarfeng.projwiz.core.model.obv.FileObverser;
import com.dwarfeng.projwiz.core.model.obv.ProjectObverser;
import com.dwarfeng.projwiz.core.view.gui.MainFrame;
import com.dwarfeng.projwiz.core.view.struct.GuiManager;
import com.dwarfeng.projwiz.core.view.struct.ProjectFileChooserSetting;
import com.dwarfeng.projwiz.core.view.struct.SystemFileChooserSetting;
import com.dwarfeng.projwiz.core.view.struct.WindowSuppiler;

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
		ADDCORECONFIGOBVERSER, //
		ADDFILE2PROJECTASNEW, //
		ADDOBVERSERTOFILE, //
		ADDOBVERSERTOPROJECT, //
		ADDPROGRAMOBVERSER, //
		CHOOSEPROJECTFILE, //
		CHOOSESYSTEMFILE, //
		CLEARPROGRAMOBVERSER, //
		CONTAINSDIALOG, //
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
		GETCORECONFIGMODEL, //
		GETCORECONFIGMODELREADONLY, //
		GETEDITORMODEL, //
		GETEDITORMODELREADONLY, //
		GETEXITCODE, //
		GETEXTERNALWINDOWMODEL, //
		GETEXTERNALWINDOWMODELREADONLY, //
		GETFILEICONIMAGEMODEL, //
		GETFILEICONIMAGEMODELREADONLY(), //
		GETFILEICONOBVMODEL, //
		GETFILEINDICATEMODEL, //
		GETFILEPROCESSORMODEL, //
		GETFILEPROCESSORMODELREADONLY, //
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
		GETMODALCONFIGMODEL, //
		GETMODALCONFIGMODELREADONLY, //
		GETPLUGINCLASSLOADER, //
		GETPROCESSORCONFIGHANDLER, //
		GETPROGRAMOBVERSERS, //
		GETPROJECTICONIMAGEMODEL, //
		GETPROJECTICONIMAGEMODELREADONLY(), //
		GETPROJECTICONOBVMODEL, //
		GETPROJECTINDICATEMODEL, //
		GETPROJECTPROCESSORMODEL, //
		GETPROJECTPROCESSORMODELREADONLY, //
		GETPROPERTY, //
		GETRUNTIMESTATE, //
		INFO, //
		ISMAINFRAMEVISIBLE, //
		NEWMAINFRAME, //
		OPENFILE, //
		OPENFILEINPUTSTREAM, //
		OPENFILEOUTPUTSTREAM, //
		REGISTFILEPROCESSOR, //
		REGISTPROJECTPROCESSOR, //
		REMOVECORECONFIGOBVERSER, //
		REMOVEOBVERSERFROMFILE, //
		REMOVEOBVERSERFROMPROJECT, //
		REMOVEPROGRAMOBVERSER, //
		SETEXITCODE, //
		SETRUNTIMESTATE, //
		SHOWCOMPONENTDIALOG, //
		SHOWCONFIRMDIALOG, //
		SHOWDIALOG, //
		SHOWEXTERNALWINDOW, //
		SHOWINPUTDIALOG, //
		SHOWMESSAGEDIALOG, //
		SHOWNEWDIALOG, //
		SHOWOPTIONDIALOG, //
		START, //
		SUBMITTASK, //
		TRACE, //
		TRYEXIT, //
		UNREGISTFILEPROCESSOR, //
		UNREGISTPROJECTPROCESSOR, //
		WARN,//
	}

	/**
	 * 向程序中添加一个核心配置观察器。
	 * 
	 * @param coreConfigObverser
	 *            指定的核心配置观察器。
	 * @return 是否添加成功。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public boolean addCoreConfigObverser(ExconfigObverser coreConfigObverser) throws IllegalStateException;

	/**
	 * 将指定的文件当做新建的文件添加到指定的工程中。
	 * 
	 * @param src
	 *            指定的文件，需要可读可写。
	 * @param project
	 *            指定的工程。
	 * @param dest
	 *            指定的目标文件，只能是文件夹。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 * @throws IllegalArgumentException
	 *             文件不存在指定的标签，或者文件不可写，或者指示模型中不存在文件。
	 * @throws ProcessException
	 *             过程异常。
	 */
	public void addFile2ProjectAsNew(File src, Project project, File dest)
			throws IllegalStateException, IllegalArgumentException, ProcessException;

	/**
	 * 向指定的文件中添加侦听器。
	 * 
	 * @param file
	 *            指定的文件。
	 * @param obverser
	 *            指定的观察器。
	 * @return 观察器是否添加成功。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public boolean addObverserToFile(File file, FileObverser obverser) throws IllegalStateException;

	/**
	 * 向指定的工程中添加指定的观察器。
	 * 
	 * @param project
	 *            指定的工程。
	 * @param obverser
	 *            指定的观察器。
	 * @return 是否添加成功。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public boolean addObverserToProject(Project project, ProjectObverser obverser) throws IllegalStateException;

	/**
	 * 移除指定的程序观察器。
	 * 
	 * @param obverser
	 *            指定的程序观察器。
	 * @return 是否添加成功。
	 */
	public boolean addProgramObverser(ProgramObverser obverser) throws IllegalStateException;

	/**
	 * 选择一个工程文件。
	 * <p>
	 * 该方法会打开一个文件选择对话框，并让
	 * 
	 * @param setting
	 *            指定的设置。
	 * @return 选择的文件数组，如果没有，则长度为0。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public File[] chooseProjectFile(ProjectFileChooserSetting setting) throws IllegalMonitorStateException;

	/**
	 * 选择一个系统文件。
	 * <p>
	 * 该方法会打开一个文件选择对话框，并让
	 * 
	 * @param setting
	 *            指定的设置。
	 * @return 选择的文件数组，如果没有，则长度为0。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public java.io.File[] chooseSystemFile(SystemFileChooserSetting setting) throws IllegalStateException;

	/**
	 * 清除所有的GUI观察器（慎用）。
	 */
	public void clearProgramObverser() throws IllegalStateException;

	/**
	 * 返回程序中是否含有指定键的对话框。
	 * 
	 * <p>
	 * 对话框通过查重键来判断是否与已有的对话框重复。
	 * 
	 * @param key
	 *            指定的键。
	 * @return 程序中是否含有指定键的对话框。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public boolean containsDialog(String key) throws IllegalStateException;

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
	 * @throws ProcessException
	 *             过程异常。
	 */
	public void exit() throws IllegalStateException, ProcessException;

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
	public SyncExconfigModel getCoreConfigModel() throws IllegalStateException;

	/**
	 * 获取程序中的核心配置模型。
	 * <p>
	 * 配置模型是只读的。
	 * 
	 * @return 程序中的核心配置模型。
	 */
	public ExconfigModel getCoreConfigModelReadOnly() throws IllegalStateException;

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
	public SyncKeySetModel<String, WindowSuppiler> getExternalWindowModel() throws IllegalStateException;

	/**
	 * 获取外部窗口模型。
	 * 
	 * <p>
	 * 外部窗口模型是只读的。
	 * 
	 * @return 外部窗口模型。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public KeySetModel<String, WindowSuppiler> getExternalWindowModelReadOnly() throws IllegalStateException;

	/**
	 * 获取文件图标图片模型。
	 * 
	 * @return 文件图标图片模型。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public SyncMapModel<File, Image> getFileIconImageModel() throws IllegalStateException;

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
	 * 获取文件指示器。
	 * 
	 * @return 文件指示器。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public SyncMapModel<String, File> getFileIndicateModel() throws IllegalStateException;

	/**
	 * 获取程序中的文件处理器模型。
	 * 
	 * @return 程序中的文件处理器模型。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public SyncKeySetModel<String, FileProcessor> getFileProcessorModel() throws IllegalStateException;

	/**
	 * 获取程序中的文件处理器模型。
	 * <p>
	 * 文件处理器模型是只读的。
	 * 
	 * @return 程序中的文件处理器模型。
	 */
	public KeySetModel<String, FileProcessor> getFileProcessorModelReadOnly() throws IllegalStateException;

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
	 * 获取模态配置模型。
	 * 
	 * @return 模态配置模型。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public SyncExconfigModel getModalConfigModel() throws IllegalStateException;

	/**
	 * 获取程序中的模态配置模型。
	 * 
	 * <p>
	 * 模态配置模型是只读的。
	 * 
	 * @return 程序中的模态配置模型。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public ExconfigModel getModalConfigModelReadOnly() throws IllegalStateException;

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
	 * 获取处理器配置处理器。
	 * 
	 * @return 处理器配置处理器。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public SyncProcessorConfigHandler getProcessorConfigHandler() throws IllegalStateException;

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
	 * 获取工程指示器。
	 * 
	 * @return 工程指示器。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public SyncMapModel<String, Project> getProjectIndicateModel() throws IllegalStateException;

	/**
	 * 获取工程处理器模型。
	 * 
	 * @return 工程处理器模型。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public SyncKeySetModel<String, ProjectProcessor> getProjectProcessorModel() throws IllegalStateException;

	/**
	 * 获取工程处理器模型。
	 * <p>
	 * 该模型是只读的。
	 * 
	 * @return 工程处理器模型。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public KeySetModel<String, ProjectProcessor> getProjectProcessorModelReadOnly() throws IllegalStateException;

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
	 * 打开文件的输入流。
	 * 
	 * @param file
	 *            指定的文件。
	 * @param label
	 *            指定的标签。
	 * @return 指定的输入流。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 * @throws IllegalArgumentException
	 *             文件不存在指定的标签，或者文件不可读，或者指示模型中不存在文件。
	 * @throws IOException
	 *             IO异常。
	 */
	public InputStream openFileInputStream(File file, String label)
			throws IllegalStateException, IllegalArgumentException, IOException;

	/**
	 * 打开文件的输出流。
	 * 
	 * @param file
	 *            指定的文件。
	 * @param label
	 *            指定的标签。
	 * @return 指定的输出流。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 * @throws IllegalArgumentException
	 *             文件不存在指定的标签，或者文件不可读，或者指示模型中不存在文件。
	 * @throws IOException
	 *             IO异常。
	 */
	public OutputStream openFileOutputStream(File file, String label)
			throws IllegalStateException, IllegalArgumentException, IOException;

	/**
	 * 向程序中注册一个指定的文件处理器。
	 * 
	 * @param processor
	 *            指定的文件处理器。
	 * @return 是否注册成功。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public boolean registFileProcessor(FileProcessor processor) throws IllegalStateException;

	/**
	 * 向程序中注册一个指定的工程处理器。
	 * 
	 * @param 指定的工程处理器。
	 * @return 是否注册成功。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public boolean registProjectProcessor(ProjectProcessor processor) throws IllegalStateException;

	/**
	 * 向程序中移除一个核心配置观察器。
	 * 
	 * @param coreConfigObverser
	 *            指定的核心配置观察器。
	 * @return 是否移除成功。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public boolean removeCoreConfigObverser(ExconfigObverser coreConfigObverser) throws IllegalStateException;

	/**
	 * 从指定的文件中移除观察器。
	 * 
	 * @param file
	 *            指定的文件。
	 * @param obverser
	 *            指定的观察器。
	 * @return 是否移除成功。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public boolean removeObverserFromFile(File file, FileObverser obverser) throws IllegalStateException;

	/**
	 * 从指定的工程中移除指定的观察器。
	 * 
	 * @param project
	 *            指定的工程。
	 * @param obverser
	 *            指定的观察器。
	 * @return 是否移除成功。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public boolean removeObverserFromProject(Project project, ProjectObverser obverser) throws IllegalStateException;

	/**
	 * 移除指定的程序观察器。
	 * 
	 * @param obverser
	 *            指定的程序观察器。
	 * @return 是否移除成功。
	 */
	public boolean removeProgramObverser(ProgramObverser obverser) throws IllegalStateException;

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
	 * 在前台显示一个确认对话框。
	 * 
	 * @param message
	 *            指定的信息。
	 * @return 用户选择的选项。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public DialogOption showConfirmDialog(Object message) throws IllegalStateException;

	/**
	 * 在前台显示一个确认对话框。
	 * 
	 * @param message
	 *            指定的信息。
	 * @param title
	 *            指定的标题。
	 * @param dialogOptionCombo
	 *            指定的组合选项。
	 * @return 用户选择的选项。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public DialogOption showConfirmDialog(Object message, String title, DialogOptionCombo dialogOptionCombo)
			throws IllegalStateException;

	/**
	 * 在前台显示一个确认对话框。
	 * 
	 * @param message
	 *            指定的信息。
	 * @param title
	 *            指定的标题。
	 * @param dialogOptionCobo
	 *            指定的组合选项。
	 * @param dialogMessage
	 *            指定的信息类型。
	 * @return 用户选择的选项。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public DialogOption showConfirmDialog(Object message, String title, DialogOptionCombo dialogOptionCombo,
			DialogMessage dialogMessage) throws IllegalStateException;

	/**
	 * 在前台显示一个确认对话框。
	 * 
	 * @param message
	 *            指定的信息。
	 * @param title
	 *            指定的标题。
	 * @param dialogOptionCobo
	 *            指定的组合选项。
	 * @param dialogMessage
	 *            指定的信息类型。
	 * @param icon
	 *            指定的图标。
	 * @return 用户选择的选项。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public DialogOption showConfirmDialog(Object message, String title, DialogOptionCombo dialogOptionCombo,
			DialogMessage dialogMessage, Icon icon) throws IllegalStateException;

	/**
	 * 展示一个外部窗口。
	 * 
	 * <p>
	 * 该方法会在程序的外部窗口模型中查找具有指定键的窗口提供器， 如果找到了， 则将该提供器设为可见的；
	 * 如果在外部窗口模型中找不到指定的键，则什么也不做。
	 * 
	 * @param key
	 *            指定的键。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public void showExternalWindow(String key);

	/**
	 * 展示一个外部的窗口。
	 * 
	 * <p>
	 * 该方法会在程序的外部窗口模型中查找具有与指定的窗口提供器键相同的窗口提供器， 如果找不到，则按照流程向模型中添加该提供器，并显示提供器中的窗口；
	 * 如果在模型中找到了相同的键的提供器，则对比两个提供器是否严格相等， 如果严格相等，则显示该提供器的窗口；
	 * 如果不是严格相等，则用该窗口提供器替换掉眼线的窗口提供器。
	 * 
	 * @param suppiler
	 *            窗口提供器。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public void showExternalWindow(WindowSuppiler suppiler);

	/**
	 * 在前台显示一个输入对话框。
	 * <p>
	 * 该方法返回用户输入的文本，如果用户没有点击确定，而是关闭了对话框，则返回 <code>null</code>。
	 * 
	 * @param message
	 *            指定的提示文本。
	 * @return 用户的输入的信息。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public String showInputDialog(Object message) throws IllegalStateException;

	/**
	 * 在前台显示一个输入对话框。
	 * <p>
	 * 该方法返回用户输入的文本，如果用户没有点击确定，而是关闭了对话框，则返回 <code>null</code>。
	 * 
	 * @param message
	 *            指定的提示文本。
	 * @param initialSelectionValue
	 *            初始的默认值。
	 * @return 用户的输入的信息。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public String showInputDialog(Object message, Object initialSelectionValue) throws IllegalStateException;

	/**
	 * 在前台显示一个输入对话框。
	 * <p>
	 * 该方法返回用户输入的文本，如果用户没有点击确定，而是关闭了对话框，则返回 <code>null</code>。
	 * 
	 * @param message
	 *            指定的提示文本。
	 * @param title
	 *            指定的标题。
	 * @param dialogMessage
	 *            对话框的信息类型。
	 * @return 用户的输入的信息。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public String showInputDialog(Object message, String title, DialogMessage dialogMessage)
			throws IllegalStateException;

	/**
	 * 在前台显示一个输入对话框。
	 * <p>
	 * 该方法返回用户输入的文本，如果用户没有点击确定，而是关闭了对话框，则返回 <code>null</code>。
	 * 
	 * @param message
	 *            指定的提示文本。
	 * @param title
	 *            指定的标题。
	 * @param dialogMessage
	 *            对话框的信息类型。
	 * @param icon
	 *            指定的显示图标。
	 * @param selectionValues
	 *            有可能的值组成的数组。
	 * @param initialSelectionValue
	 *            初始的默认值。
	 * @return 用户的输入的信息。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public Object showInputDialog(Object message, String title, DialogMessage dialogMessage, Icon icon,
			Object[] selectionValues, Object initialSelectionValue) throws IllegalStateException;

	/**
	 * 在前台显示一个信息对话框。
	 * 
	 * @param message
	 *            指定的信息。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public void showMessageDialog(Object message) throws IllegalStateException;

	/**
	 * 在前台显示一个信息对话框。
	 * 
	 * @param message
	 *            指定的信息。
	 * @param title
	 *            指定的标题。
	 * @param dialogMessage
	 *            指定的信息类型。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public void showMessageDialog(Object message, String title, DialogMessage dialogMessage)
			throws IllegalStateException;

	/**
	 * 在前台显示一个信息对话框。
	 * 
	 * @param message
	 *            指定的信息。
	 * @param title
	 *            指定的标题。
	 * @param dialogMessage
	 *            指定的信息类型。
	 * @param icon
	 *            指定的图标。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public void showMessageDialog(Object message, String title, DialogMessage dialogMessage, Icon icon)
			throws IllegalStateException;

	/**
	 * 在前台显示一个选项对话框。
	 * 
	 * @param message
	 *            指定的信息。
	 * @param title
	 *            指定的标题。
	 * @param dialogOptionCobo
	 *            指定的组合选项。
	 * @param dialogMessage
	 *            指定的信息类型。
	 * @param icon
	 *            指定的图标。
	 * @param options
	 *            指定的选项组成的数组。
	 * @param initialValue
	 *            指定的初始值。
	 * @return 用户选择的选项的序号。
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 */
	public int showOptionDialog(Object message, String title, DialogOptionCombo dialogOptionCombo,
			DialogMessage dialogMessage, Icon icon, Object[] options, Object initialValue) throws IllegalStateException;

	/**
	 * 启动程序。
	 * 
	 * @throws IllegalStateException
	 *             因为没有权限而抛出的异常。
	 * @throws ProcessException
	 *             过程异常。
	 */
	public void start() throws IllegalStateException, ProcessException;

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
	 * 从程序中解除注册一个指定的文件处理器。
	 * 
	 * @param processor
	 *            指定的文件处理器。
	 * @return 是否注册成功。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public boolean unregistFileProcessor(FileProcessor processor) throws IllegalStateException;

	/**
	 * 从程序中解除注册一个指定的工程处理器。
	 * 
	 * @param processor
	 *            指定的工程处理器。
	 * @return 是否解除注册成功。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IllegalStateException
	 *             因为没有执行权限而抛出的异常。
	 */
	public boolean unregistProjectProcessor(ProjectProcessor processor) throws IllegalStateException;

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
