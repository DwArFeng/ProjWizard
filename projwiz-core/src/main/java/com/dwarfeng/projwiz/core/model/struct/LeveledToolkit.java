package com.dwarfeng.projwiz.core.model.struct;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.EnumMap;
import java.util.Map;
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
import com.dwarfeng.dutil.develop.resource.ResourceHandler;
import com.dwarfeng.dutil.develop.resource.SyncResourceHandler;
import com.dwarfeng.projwiz.core.model.cm.LoggerHandler;
import com.dwarfeng.projwiz.core.model.cm.SyncLoggerHandler;
import com.dwarfeng.projwiz.core.model.cm.SyncProcessorConfigHandler;
import com.dwarfeng.projwiz.core.model.eum.DialogMessage;
import com.dwarfeng.projwiz.core.model.eum.DialogOption;
import com.dwarfeng.projwiz.core.model.eum.DialogOptionCombo;
import com.dwarfeng.projwiz.core.model.eum.ProjWizProperty;
import com.dwarfeng.projwiz.core.model.eum.ToolkitLevel;
import com.dwarfeng.projwiz.core.model.io.PluginClassLoader;
import com.dwarfeng.projwiz.core.model.obv.FileObverser;
import com.dwarfeng.projwiz.core.model.obv.ProjectObverser;
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

	private static final Map<Method, ToolkitLevel> MIN_LEVEL_MAP = new EnumMap<>(Method.class);

	static {
		// 为各种方法提供权限等级
		MIN_LEVEL_MAP.put(Method.ADDCORECONFIGOBVERSER, ToolkitLevel.WRITE_LIMIT);
		MIN_LEVEL_MAP.put(Method.ADDOBVERSERTOFILE, ToolkitLevel.WRITE_LIMIT);
		MIN_LEVEL_MAP.put(Method.ADDOBVERSERTOPROJECT, ToolkitLevel.WRITE_LIMIT);
		MIN_LEVEL_MAP.put(Method.ADDPROGRAMOBVERSER, ToolkitLevel.FULL);
		MIN_LEVEL_MAP.put(Method.CHOOSEPROJECTFILE, ToolkitLevel.READ_ONLY);
		MIN_LEVEL_MAP.put(Method.CHOOSESYSTEMFILE, ToolkitLevel.READ_ONLY);
		MIN_LEVEL_MAP.put(Method.CLEARPROGRAMOBVERSER, ToolkitLevel.FULL);
		MIN_LEVEL_MAP.put(Method.CONTAINSDIALOG, ToolkitLevel.READ_ONLY);
		MIN_LEVEL_MAP.put(Method.DEBUG, ToolkitLevel.WRITE_LIMIT);
		MIN_LEVEL_MAP.put(Method.DISPOSEMAINFRAME, ToolkitLevel.FULL);
		MIN_LEVEL_MAP.put(Method.ERROR, ToolkitLevel.WRITE_LIMIT);
		MIN_LEVEL_MAP.put(Method.EXIT, ToolkitLevel.FULL);
		MIN_LEVEL_MAP.put(Method.FATAL, ToolkitLevel.WRITE_LIMIT);
		MIN_LEVEL_MAP.put(Method.GETANCHORFILEMODEL, ToolkitLevel.FULL);
		MIN_LEVEL_MAP.put(Method.GETANCHORFILEMODELREADONLY, ToolkitLevel.READ_ONLY);
		MIN_LEVEL_MAP.put(Method.GETBACKGROUND, ToolkitLevel.FULL);
		MIN_LEVEL_MAP.put(Method.GETBACKGROUNDREADONLY, ToolkitLevel.READ_ONLY);
		MIN_LEVEL_MAP.put(Method.GETCORECONFIGMODEL, ToolkitLevel.FULL);
		MIN_LEVEL_MAP.put(Method.GETCORECONFIGMODELREADONLY, ToolkitLevel.READ_ONLY);
		MIN_LEVEL_MAP.put(Method.GETEDITORMODEL, ToolkitLevel.FULL);
		MIN_LEVEL_MAP.put(Method.GETEDITORMODELREADONLY, ToolkitLevel.READ_ONLY);
		MIN_LEVEL_MAP.put(Method.GETEXITCODE, ToolkitLevel.READ_ONLY);
		MIN_LEVEL_MAP.put(Method.GETEXTERNALWINDOWMODEL, ToolkitLevel.FULL);
		MIN_LEVEL_MAP.put(Method.GETEXTERNALWINDOWMODELREADONLY, ToolkitLevel.READ_ONLY);
		MIN_LEVEL_MAP.put(Method.GETFILEICONIMAGEMODEL, ToolkitLevel.FULL);
		MIN_LEVEL_MAP.put(Method.GETFILEICONIMAGEMODELREADONLY, ToolkitLevel.READ_ONLY);
		MIN_LEVEL_MAP.put(Method.GETFILEICONOBVMODEL, ToolkitLevel.FULL);
		MIN_LEVEL_MAP.put(Method.GETFILEINDICATEMODEL, ToolkitLevel.FULL);
		MIN_LEVEL_MAP.put(Method.GETFILEPROCESSORMODEL, ToolkitLevel.FULL);
		MIN_LEVEL_MAP.put(Method.GETFILEPROCESSORMODELREADONLY, ToolkitLevel.READ_ONLY);
		MIN_LEVEL_MAP.put(Method.GETFOCUSEDITORMODEL, ToolkitLevel.FULL);
		MIN_LEVEL_MAP.put(Method.GETFOCUSEDITORMODELREADONLY, ToolkitLevel.READ_ONLY);
		MIN_LEVEL_MAP.put(Method.GETFOCUSFILEMODEL, ToolkitLevel.FULL);
		MIN_LEVEL_MAP.put(Method.GETFOCUSFILEMODELREADONLY, ToolkitLevel.READ_ONLY);
		MIN_LEVEL_MAP.put(Method.GETFOCUSPROJECTMODEL, ToolkitLevel.FULL);
		MIN_LEVEL_MAP.put(Method.GETFOCUSPROJECTMODELREADONLY, ToolkitLevel.READ_ONLY);
		MIN_LEVEL_MAP.put(Method.GETGUIMANAGER, ToolkitLevel.FULL);
		MIN_LEVEL_MAP.put(Method.GETHOLDPROJECTMODEL, ToolkitLevel.FULL);
		MIN_LEVEL_MAP.put(Method.GETHOLDPROJECTMODELREADONLY, ToolkitLevel.READ_ONLY);
		MIN_LEVEL_MAP.put(Method.GETLABELI18NHANDLER, ToolkitLevel.FULL);
		MIN_LEVEL_MAP.put(Method.GETLABELI18NHANDLERREADONLY, ToolkitLevel.READ_ONLY);
		MIN_LEVEL_MAP.put(Method.GETLOGGERHANDLER, ToolkitLevel.FULL);
		MIN_LEVEL_MAP.put(Method.GETLOGGERHANDLERREADONLY, ToolkitLevel.READ_ONLY);
		MIN_LEVEL_MAP.put(Method.GETLOGGERI18NHANDLER, ToolkitLevel.FULL);
		MIN_LEVEL_MAP.put(Method.GETLOGGERI18NHANDLERREADONLY, ToolkitLevel.READ_ONLY);
		MIN_LEVEL_MAP.put(Method.GETMAINFRAME, ToolkitLevel.FULL);
		MIN_LEVEL_MAP.put(Method.GETMODALCONFIGMODEL, ToolkitLevel.FULL);
		MIN_LEVEL_MAP.put(Method.GETMODALCONFIGMODELREADONLY, ToolkitLevel.READ_ONLY);
		MIN_LEVEL_MAP.put(Method.GETPROGRAMOBVERSERS, ToolkitLevel.FULL);
		MIN_LEVEL_MAP.put(Method.GETPROJECTICONIMAGEMODEL, ToolkitLevel.FULL);
		MIN_LEVEL_MAP.put(Method.GETPROJECTICONIMAGEMODELREADONLY, ToolkitLevel.READ_ONLY);
		MIN_LEVEL_MAP.put(Method.GETPROJECTICONOBVMODEL, ToolkitLevel.FULL);
		MIN_LEVEL_MAP.put(Method.GETPROJECTINDICATEMODEL, ToolkitLevel.FULL);
		MIN_LEVEL_MAP.put(Method.GETPLUGINCLASSLOADER, ToolkitLevel.FULL);
		MIN_LEVEL_MAP.put(Method.GETPROCESSORCONFIGHANDLER, ToolkitLevel.FULL);
		MIN_LEVEL_MAP.put(Method.GETPROJECTPROCESSORMODEL, ToolkitLevel.FULL);
		MIN_LEVEL_MAP.put(Method.GETPROJECTPROCESSORMODELREADONLY, ToolkitLevel.READ_ONLY);
		MIN_LEVEL_MAP.put(Method.GETPROPERTY, ToolkitLevel.READ_ONLY);
		MIN_LEVEL_MAP.put(Method.GETRESOURCEHANDLER, ToolkitLevel.FULL);
		MIN_LEVEL_MAP.put(Method.GETRESOURCEHANDLERREADONLY, ToolkitLevel.READ_ONLY);
		MIN_LEVEL_MAP.put(Method.GETRUNTIMESTATE, ToolkitLevel.READ_ONLY);
		MIN_LEVEL_MAP.put(Method.INFO, ToolkitLevel.WRITE_LIMIT);
		MIN_LEVEL_MAP.put(Method.ISMAINFRAMEVISIBLE, ToolkitLevel.READ_ONLY);
		MIN_LEVEL_MAP.put(Method.NEWMAINFRAME, ToolkitLevel.FULL);
		MIN_LEVEL_MAP.put(Method.OPENFILE, ToolkitLevel.WRITE_LIMIT);
		MIN_LEVEL_MAP.put(Method.OPENFILEINPUTSTREAM, ToolkitLevel.WRITE_LIMIT);
		MIN_LEVEL_MAP.put(Method.OPENFILEOUTPUTSTREAM, ToolkitLevel.WRITE_LIMIT);
		MIN_LEVEL_MAP.put(Method.REGISTFILEPROCESSOR, ToolkitLevel.WRITE_LIMIT);
		MIN_LEVEL_MAP.put(Method.REGISTPROJECTPROCESSOR, ToolkitLevel.WRITE_LIMIT);
		MIN_LEVEL_MAP.put(Method.REMOVECORECONFIGOBVERSER, ToolkitLevel.WRITE_LIMIT);
		MIN_LEVEL_MAP.put(Method.REMOVEOBVERSERFROMFILE, ToolkitLevel.WRITE_LIMIT);
		MIN_LEVEL_MAP.put(Method.REMOVEOBVERSERFROMPROJECT, ToolkitLevel.WRITE_LIMIT);
		MIN_LEVEL_MAP.put(Method.REMOVEPROGRAMOBVERSER, ToolkitLevel.FULL);
		MIN_LEVEL_MAP.put(Method.SETRUNTIMESTATE, ToolkitLevel.FULL);
		MIN_LEVEL_MAP.put(Method.SHOWCOMPONENTDIALOG, ToolkitLevel.WRITE_LIMIT);
		MIN_LEVEL_MAP.put(Method.SHOWCONFIRMDIALOG, ToolkitLevel.WRITE_LIMIT);
		MIN_LEVEL_MAP.put(Method.SHOWDIALOG, ToolkitLevel.WRITE_LIMIT);
		MIN_LEVEL_MAP.put(Method.SHOWEXTERNALWINDOW, ToolkitLevel.WRITE_LIMIT);
		MIN_LEVEL_MAP.put(Method.SHOWINPUTDIALOG, ToolkitLevel.WRITE_LIMIT);
		MIN_LEVEL_MAP.put(Method.SHOWMESSAGEDIALOG, ToolkitLevel.WRITE_LIMIT);
		MIN_LEVEL_MAP.put(Method.SHOWNEWDIALOG, ToolkitLevel.WRITE_LIMIT);
		MIN_LEVEL_MAP.put(Method.SHOWOPTIONDIALOG, ToolkitLevel.WRITE_LIMIT);
		MIN_LEVEL_MAP.put(Method.START, ToolkitLevel.FULL);
		MIN_LEVEL_MAP.put(Method.SUBMITTASK, ToolkitLevel.WRITE_LIMIT);
		MIN_LEVEL_MAP.put(Method.TRACE, ToolkitLevel.WRITE_LIMIT);
		MIN_LEVEL_MAP.put(Method.TRYEXIT, ToolkitLevel.FULL);
		MIN_LEVEL_MAP.put(Method.UNREGISTFILEPROCESSOR, ToolkitLevel.WRITE_LIMIT);
		MIN_LEVEL_MAP.put(Method.UNREGISTPROJECTPROCESSOR, ToolkitLevel.WRITE_LIMIT);
		MIN_LEVEL_MAP.put(Method.WARN, ToolkitLevel.WRITE_LIMIT);

	}

	private final ToolkitLevel currentLevel;
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
	public LeveledToolkit(Toolkit standardToolkit, ToolkitLevel currentLevel) {
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
	public boolean addObverserToProject(Project project, ProjectObverser obverser) {
		checkPermissionAndState(Method.ADDOBVERSERTOPROJECT);
		return standardToolkit.addObverserToProject(project, obverser);
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
	public void exit() throws IllegalStateException, ProcessException {
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
	public ToolkitLevel getCurrentLevel() {
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
	public SyncKeySetModel<String, FileProcessor> getFileProcessorModel() throws IllegalStateException {
		checkPermissionAndState(Method.GETFILEPROCESSORMODEL);
		return standardToolkit.getFileProcessorModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public KeySetModel<String, FileProcessor> getFileProcessorModelReadOnly() throws IllegalStateException {
		checkPermissionAndState(Method.GETFILEPROCESSORMODELREADONLY);
		return standardToolkit.getFileProcessorModelReadOnly();
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
	public SyncProcessorConfigHandler getProcessorConfigHandler() throws IllegalStateException {
		checkPermissionAndState(Method.GETPROCESSORCONFIGHANDLER);
		return standardToolkit.getProcessorConfigHandler();
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
	public SyncKeySetModel<String, ProjectProcessor> getProjectProcessorModel() throws IllegalStateException {
		checkPermissionAndState(Method.GETPROJECTPROCESSORMODEL);
		return standardToolkit.getProjectProcessorModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public KeySetModel<String, ProjectProcessor> getProjectProcessorModelReadOnly() throws IllegalStateException {
		checkPermissionAndState(Method.GETPROJECTPROCESSORMODELREADONLY);
		return standardToolkit.getProjectProcessorModelReadOnly();
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
	public SyncResourceHandler getResourceHandler() throws IllegalStateException {
		checkPermissionAndState(Method.GETRESOURCEHANDLER);
		return standardToolkit.getResourceHandler();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResourceHandler getResourceHandlerReadOnly() throws IllegalStateException {
		checkPermissionAndState(Method.GETRESOURCEHANDLERREADONLY);
		return standardToolkit.getResourceHandlerReadOnly();
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
		return currentLevel.getLevelValue() >= needLevel(method).getLevelValue();
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
	public boolean registFileProcessor(FileProcessor processor) throws IllegalStateException {
		checkPermissionAndState(Method.REGISTFILEPROCESSOR);
		return standardToolkit.registFileProcessor(processor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean registProjectProcessor(ProjectProcessor processor) throws IllegalStateException {
		checkPermissionAndState(Method.REGISTPROJECTPROCESSOR);
		return standardToolkit.registProjectProcessor(processor);
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
	public boolean removeObverserFromProject(Project project, ProjectObverser obverser) {
		checkPermissionAndState(Method.REMOVEOBVERSERFROMPROJECT);
		return standardToolkit.removeObverserFromProject(project, obverser);
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
	public void start() throws IllegalStateException, ProcessException {
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
	public boolean unregistFileProcessor(FileProcessor processor) throws IllegalStateException {
		checkPermissionAndState(Method.UNREGISTFILEPROCESSOR);
		return standardToolkit.unregistFileProcessor(processor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean unregistProjectProcessor(ProjectProcessor processor) throws IllegalStateException {
		checkPermissionAndState(Method.UNREGISTPROJECTPROCESSOR);
		return standardToolkit.unregistProjectProcessor(processor);
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
		if (currentLevel.getLevelValue() < needLevel(method).getLevelValue())
			throw new IllegalStateException(String.format("方法 %s 需要的最小权限为 %s，而当前的权限为 %s。", method,
					needLevel(method).toString(), currentLevel.toString()));
	}

	private ToolkitLevel needLevel(Method method) {
		Objects.requireNonNull(method, "入口参数 method 不能为 null。");

		if (!MIN_LEVEL_MAP.containsKey(method)) {
			throw new IllegalArgumentException(String.format("权限表中没有指定的方法：%s。", method));
		}

		return MIN_LEVEL_MAP.get(method);
	}

}
