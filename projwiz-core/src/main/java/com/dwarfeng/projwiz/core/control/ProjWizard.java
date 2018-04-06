package com.dwarfeng.projwiz.core.control;

import java.awt.Image;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.WeakHashMap;
import java.util.concurrent.Executors;

import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.dwarfeng.dutil.basic.cna.ArrayUtil;
import com.dwarfeng.dutil.basic.cna.model.DefaultReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.DelegateListModel;
import com.dwarfeng.dutil.basic.cna.model.DelegateMapModel;
import com.dwarfeng.dutil.basic.cna.model.DelegateSetModel;
import com.dwarfeng.dutil.basic.cna.model.ListModel;
import com.dwarfeng.dutil.basic.cna.model.MapModel;
import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.SetModel;
import com.dwarfeng.dutil.basic.cna.model.SyncListModel;
import com.dwarfeng.dutil.basic.cna.model.SyncMapModel;
import com.dwarfeng.dutil.basic.cna.model.SyncReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.SyncSetModel;
import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.dutil.basic.prog.DefaultVersion;
import com.dwarfeng.dutil.basic.prog.ProgramObverser;
import com.dwarfeng.dutil.basic.prog.RuntimeState;
import com.dwarfeng.dutil.basic.prog.Version;
import com.dwarfeng.dutil.basic.prog.VersionType;
import com.dwarfeng.dutil.develop.backgr.Background;
import com.dwarfeng.dutil.develop.backgr.BackgroundUtil;
import com.dwarfeng.dutil.develop.backgr.ExecutorServiceBackground;
import com.dwarfeng.dutil.develop.backgr.Task;
import com.dwarfeng.dutil.develop.backgr.obv.BackgroundObverser;
import com.dwarfeng.dutil.develop.cfg.ConfigUtil;
import com.dwarfeng.dutil.develop.cfg.DefaultExconfigModel;
import com.dwarfeng.dutil.develop.cfg.ExconfigModel;
import com.dwarfeng.dutil.develop.cfg.SyncExconfigModel;
import com.dwarfeng.dutil.develop.cfg.obv.ExconfigObverser;
import com.dwarfeng.dutil.develop.i18n.DelegateI18nHandler;
import com.dwarfeng.dutil.develop.i18n.I18nHandler;
import com.dwarfeng.dutil.develop.i18n.I18nUtil;
import com.dwarfeng.dutil.develop.i18n.SyncI18nHandler;
import com.dwarfeng.dutil.develop.logger.DelegateLoggerHandler;
import com.dwarfeng.dutil.develop.logger.LoggerHandler;
import com.dwarfeng.dutil.develop.logger.LoggerUtil;
import com.dwarfeng.dutil.develop.logger.SyncLoggerHandler;
import com.dwarfeng.dutil.develop.resource.DelegateResourceHandler;
import com.dwarfeng.dutil.develop.resource.ResourceHandler;
import com.dwarfeng.dutil.develop.resource.ResourceUtil;
import com.dwarfeng.dutil.develop.resource.SyncResourceHandler;
import com.dwarfeng.projwiz.core.model.cm.ModuleModel;
import com.dwarfeng.projwiz.core.model.cm.DefaultModuleModel;
import com.dwarfeng.projwiz.core.model.cm.DefaultToolkitPermModel;
import com.dwarfeng.projwiz.core.model.cm.SyncModuleModel;
import com.dwarfeng.projwiz.core.model.cm.SyncToolkitPermModel;
import com.dwarfeng.projwiz.core.model.cm.ToolkitPermModel;
import com.dwarfeng.projwiz.core.model.eum.CoreConfigEntry;
import com.dwarfeng.projwiz.core.model.eum.ProjWizProperty;
import com.dwarfeng.projwiz.core.model.eum.ViewConfigEntry;
import com.dwarfeng.projwiz.core.model.io.DefaultPluginClassLoader;
import com.dwarfeng.projwiz.core.model.io.PluginClassLoader;
import com.dwarfeng.projwiz.core.model.obv.FileObverser;
import com.dwarfeng.projwiz.core.model.obv.ProjectObverser;
import com.dwarfeng.projwiz.core.model.struct.Module;
import com.dwarfeng.projwiz.core.model.struct.Editor;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.ProjectFilePair;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;
import com.dwarfeng.projwiz.core.model.struct.Toolkit.BackgroundType;
import com.dwarfeng.projwiz.core.util.Constants;
import com.dwarfeng.projwiz.core.util.ModelUtil;
import com.dwarfeng.projwiz.core.view.eum.ChooseOption;
import com.dwarfeng.projwiz.core.view.eum.DialogMessage;
import com.dwarfeng.projwiz.core.view.eum.DialogOption;
import com.dwarfeng.projwiz.core.view.eum.DialogOptionCombo;
import com.dwarfeng.projwiz.core.view.gui.ModuleChooser;
import com.dwarfeng.projwiz.core.view.gui.MainFrame;
import com.dwarfeng.projwiz.core.view.gui.ProjectFileChooser;
import com.dwarfeng.projwiz.core.view.gui.SystemFileChooser;
import com.dwarfeng.projwiz.core.view.struct.ModuleChooserSetting;
import com.dwarfeng.projwiz.core.view.struct.ConfirmDialogSetting;
import com.dwarfeng.projwiz.core.view.struct.DefaultMainFrameVisibleModel;
import com.dwarfeng.projwiz.core.view.struct.GuiManager;
import com.dwarfeng.projwiz.core.view.struct.InputDialogSetting;
import com.dwarfeng.projwiz.core.view.struct.MessageDialogSetting;
import com.dwarfeng.projwiz.core.view.struct.ProjectFileChooserSetting;
import com.dwarfeng.projwiz.core.view.struct.SystemFileChooserSetting;

/**
 * ProjWizard
 * <p>
 * 项目的核心控制类。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public final class ProjWizard {

	/**
	 * 供 ProjWizard 内部使用的默认的 GUI 管理器。
	 * 
	 * @author DwArFeng
	 * @since 0.0.1-alpha
	 */
	private final class ProjWizGuiManager implements GuiManager {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void addFocusFile(File file, ExecType type) {
			doTask(new AddFocusFileTask(ProjWizard.this, file), type);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void closeAllProject(ExecType type) {
			doTask(new TryCloseAllProjectTask(ProjWizard.this), type);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void deleteFocusFile(ExecType type) {
			doTask(new DeleteFocusFileTask(ProjWizard.this), type);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void forceExit(ExecType type) {
			doTask(new ForceExitTask(ProjWizard.this), type);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void newFile(ExecType type) {
			doTask(new NewFileTask(ProjWizard.this), type);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void newProject(ExecType type) {
			doTask(new NewProjectTask(ProjWizard.this), type);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void openAnchorFile(ExecType type) {
			doTask(new OpenAnchorFileTask(ProjWizard.this), type);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void openFocusFile(ExecType type) {
			doTask(new OpenFocusFileTask(ProjWizard.this), type);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void openProject(ExecType type) {
			doTask(new OpenProjectTask(ProjWizard.this), type);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void putFocusEditor(Project project, Editor editor, ExecType type) {
			doTask(new PutFocusEditorTask(ProjWizard.this, project, editor), type);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void removeFocusFile(File file, ExecType type) {
			doTask(new RemoveFocusFileTask(ProjWizard.this, file), type);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void renameAnchorFile(ExecType type) {
			doTask(new RenameAnchorFileTask(ProjWizard.this), type);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void saveAsFocusProject(ExecType type) {
			doTask(new SaveAsFocusProjectTask(ProjWizard.this), type);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void saveFocusEditor(ExecType type) {
			doTask(new SaveFocusEditorTask(ProjWizard.this), type);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void saveFocusProject(ExecType type) {
			doTask(new SaveFocusProjectTask(ProjWizard.this), type);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setAnchorFile(File file, ExecType type) {
			doTask(new SetAnchorFileTask(ProjWizard.this, file), type);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setFocusProject(Project project, ExecType type) {
			doTask(new SetFocusProjectTask(ProjWizard.this, project), type);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void showAnchorFilePropertiesDialog(ExecType type) {
			doTask(new ShowAnchorFilePropertiesDialogTask(ProjWizard.this), type);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void showEditorMonitor(ExecType type) {
			doTask(new ShowEditorMonitorTask(ProjWizard.this), type);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void showFocusProjectPropertiesDialog(ExecType type) {
			doTask(new ShowFocusProjectPropertiesDialogTask(ProjWizard.this), type);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void showProjectAndFileMonitor(ExecType type) {
			doTask(new ShowProjectAndFileMonitorTask(ProjWizard.this), type);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void submitTask(Task task, ExecType type) {
			Objects.requireNonNull(task, "入口参数 task 不能为 null。");
			Objects.requireNonNull(type, "入口参数 type 不能为 null。");

			switch (type) {
			case CONCURRENT:
				toolkit.submitTask(task, BackgroundType.CONCURRENT);
				break;
			case FIFO:
				toolkit.submitTask(task, BackgroundType.FIFO);
				break;
			default:
				throw new IllegalArgumentException("未知的值:" + type);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void superSecretSettings(ExecType type) {
			doTask(new SuperSecretSettingsTask(ProjWizard.this), type);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void tryCloseCertainProject(Project project, ExecType type) {
			doTask(new TryCloseCertainProjectTask(ProjWizard.this, project), type);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void tryCloseFocusProject(ExecType type) {
			doTask(new TryCloseFocusProjectTask(ProjWizard.this), type);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void tryExit(ExecType type) {
			doTask(new TryExitTask(ProjWizard.this), type);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void tryStopCertainEditor(Editor editor, ExecType type) {
			doTask(new TryStopCertainEditorTask(ProjWizard.this, editor), type);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void tryStopFocusEditor(ExecType type) {
			doTask(new TryStopFocusEditorTask(ProjWizard.this), type);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void tryStopFocusProjectEditor(ExecType type) {
			doTask(new TryStopFocusProjectEditorTask(ProjWizard.this), type);
		}

		private void doTask(Task task, ExecType type) {
			Objects.requireNonNull(type, "入口参数 type 不能为 null。");

			switch (type) {
			case CONCURRENT:
				getToolkit().submitTask(task, BackgroundType.CONCURRENT);
				return;
			case FIFO:
				getToolkit().submitTask(task, BackgroundType.FIFO);
				return;
			}
			throw new IllegalArgumentException("入口参数 " + type + " 不合法");
		}

	}

	/**
	 * 供 ProjWizard 内部使用的默认的工具包。
	 * 
	 * @author DwArFeng
	 * @since 0.0.1-alpha
	 */
	private final class ProjWizToolKit implements Toolkit {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean addCoreConfigObverser(ExconfigObverser coreConfigObverser) {
			return coreConfigModel.addObverser(coreConfigObverser);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean addProgramObverser(ProgramObverser obverser) {
			synchronized (programObversers) {
				return programObversers.add(obverser);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Module[] chooseModule(ModuleChooserSetting setting) throws IllegalStateException {
			Objects.requireNonNull(setting, "入口参数 setting 不能为 null。");

			ModuleChooser moduleChooser = new ModuleChooser(guiManager, labelI18nHandler, moduleModel);
			moduleChooser.setChooserDialogType(setting.getChooserDialogType());
			moduleChooser.setModuleFilter(setting.getModuleFilter());
			// fileChooser.setFileSelectionMode(setting.getFileSelectionMode().getValue());
			moduleChooser.setControlButtonsAreShown(setting.isControlButtonsAreShown());
			moduleChooser.setMultiSelectionEnabled(setting.isMultiSelectionEnabled());
			moduleChooser.setLocale(labelI18nHandler.getCurrentLocale());

			ChooseOption chooseOption = moduleChooser.showDialog(mainFrame);

			Module[] modules;
			switch (chooseOption) {
			case APPROVE_OPTION:
				if (setting.isMultiSelectionEnabled()) {
					modules = moduleChooser.getSelectedModules();
				} else {
					modules = new Module[] { moduleChooser.getSelectedModule() };
				}
				break;
			case CANCEL_OPTION:
				modules = new Module[0];
				break;
			case ERROR_OPTION:
				modules = new Module[0];
				break;
			default:
				modules = new Module[0];
				break;
			}
			return ArrayUtil.getNotNull(modules, new Module[0]);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public File[] chooseProjectFile(ProjectFileChooserSetting setting) throws IllegalStateException {
			Objects.requireNonNull(setting, "入口参数 setting 不能为 null。");

			ProjectFileChooser fileChooser = new ProjectFileChooser(guiManager, labelI18nHandler, holdProjectModel,
					moduleModel);
			fileChooser.setCurrentDirectory(setting.getCurrentDirectory());
			fileChooser.setChooserDialogType(setting.getChooserDialogType());
			fileChooser.setFileFilters(setting.getFileFilters());
			// fileChooser.setFileSelectionMode(setting.getFileSelectionMode().getValue());
			fileChooser.setFileSelectionMode(setting.getFileSelectionMode());
			fileChooser.setAcceptAllFileFilterUsed(setting.isAcceptAllFileFilterUsed());
			fileChooser.setControlButtonsAreShown(setting.isControlButtonsAreShown());
			fileChooser.setDragEnabled(setting.isDragEnabled());
			fileChooser.setFileHidingEnabled(setting.isFileHidingEnabled());
			fileChooser.setMultiSelectionEnabled(setting.isMultiSelectionEnabled());
			fileChooser.setLocale(labelI18nHandler.getCurrentLocale());

			ChooseOption chooseOption = fileChooser.showDialog(mainFrame);

			File[] files;
			switch (chooseOption) {
			case APPROVE_OPTION:
				if (setting.isMultiSelectionEnabled()) {
					files = fileChooser.getSelectedFiles();
				} else {
					files = new File[] { fileChooser.getSelectedFile() };
				}
				break;
			case CANCEL_OPTION:
				files = new File[0];
				break;
			case ERROR_OPTION:
				files = new File[0];
				break;
			default:
				files = new File[0];
				break;
			}
			return ArrayUtil.getNotNull(files, new File[0]);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public java.io.File[] chooseSystemFile(SystemFileChooserSetting setting) throws IllegalStateException {
			Objects.requireNonNull(setting, "入口参数 setting 不能为 null。");

			SystemFileChooser fileChooser = new SystemFileChooser(guiManager, labelI18nHandler);
			fileChooser.setCurrentDirectory(setting.getCurrentDirectory());
			switch (setting.getChooserDialogType()) {
			case OPEN_DIALOG:
				fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
				break;
			case SAVE_DIALOG:
				fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
				break;
			default:
				fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
				break;
			}
			setting.getFileFilters().forEach(filter -> {
				fileChooser.setFileFilter(filter);
			});
			// fileChooser.setFileSelectionMode(setting.getFileSelectionMode().getValue());
			switch (setting.getFileSelectionMode()) {
			case DIRECTORIES_ONLY:
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				break;
			case FILES_AND_DIRECTORIES:
				fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				break;
			case FILES_ONLY:
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				break;
			default:
				fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				break;
			}
			fileChooser.setAcceptAllFileFilterUsed(setting.isAcceptAllFileFilterUsed());
			fileChooser.setControlButtonsAreShown(setting.isControlButtonsAreShown());
			fileChooser.setDragEnabled(setting.isDragEnabled());
			fileChooser.setFileHidingEnabled(setting.isFileHidingEnabled());
			fileChooser.setMultiSelectionEnabled(setting.isMultiSelectionEnabled());
			fileChooser.setLocale(labelI18nHandler.getCurrentLocale());

			int selectResult;
			switch (setting.getChooserDialogType()) {
			case OPEN_DIALOG:
				selectResult = fileChooser.showOpenDialog(mainFrame);
				break;
			case SAVE_DIALOG:
				selectResult = fileChooser.showSaveDialog(mainFrame);
				break;
			default:
				selectResult = fileChooser.showOpenDialog(mainFrame);
				break;
			}

			java.io.File[] files;
			switch (selectResult) {
			case JFileChooser.CANCEL_OPTION:
				files = new java.io.File[0];
				break;
			case JFileChooser.APPROVE_OPTION:
				if (setting.isMultiSelectionEnabled()) {
					files = fileChooser.getSelectedFiles();
				} else {
					files = new java.io.File[] { fileChooser.getSelectedFile() };
				}
				break;
			case JFileChooser.ERROR_OPTION:
				files = new java.io.File[0];
				break;
			default:
				files = new java.io.File[0];
				break;
			}
			return ArrayUtil.getNotNull(files, new java.io.File[0]);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void clearProgramObverser() {
			synchronized (programObversers) {
				programObversers.clear();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void debug(String message) {
			Objects.requireNonNull(message, "入口参数 message 不能为 null。");
			loggerHandler.debug(message);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean disposeMainFrame() {
			synchronized (mainFrameLock) {
				if (Objects.isNull(mainFrame)) {
					return false;
				}

				mainFrame.dispose();
				mainFrame = null;
				return true;
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void error(String message, Throwable t) {
			Objects.requireNonNull(message, "入口参数 message 不能为 null。");
			Objects.requireNonNull(t, "入口参数 t 不能为 null。");
			loggerHandler.error(message, t);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void exit() {
			synchronized (startExitLock) {

				if (isDisposeFlag()) {
					if (getRuntimeState() == RuntimeState.RUNNING) {
						throw new IllegalStateException("程序正在运行");
					}
					if (getRuntimeState() == RuntimeState.NOT_START) {
						throw new IllegalStateException("程序还未启动");
					}
					throw new IllegalStateException("由于释放标志为 true，程序无法释放");
				}
				setDisposeFlag(true);

				// 开启释放过程
				final Task task = new DisposeTask(ProjWizard.this);
				task.run();

				// 解除自身引用
				INSTANCES.remove(this);

				// 反编织观察网络。
				concurrentBackground.removeObverser(backgroundObverser);

				// 判断是否出现异常。
				if (Objects.nonNull(task.getThrowable())) {
					// 当此过程出现异常时，启动应急退出机制。
					setExitCode(-12451);
				}

				// 设置程序的运行状态为已经结束
				setRuntimeState(RuntimeState.ENDED);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fatal(String message, Throwable t) {
			Objects.requireNonNull(message, "入口参数 message 不能为 null。");
			Objects.requireNonNull(t, "入口参数 t 不能为 null。");
			loggerHandler.fatal(message, t);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SyncReferenceModel<File> getAnchorFileModel() throws IllegalStateException {
			return anchorFileModel;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ReferenceModel<File> getAnchorFileModelReadOnly() throws IllegalStateException {
			return com.dwarfeng.dutil.basic.cna.model.ModelUtil.unmodifiableReferenceModel(anchorFileModel);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Background getBackground(BackgroundType type) {
			Objects.requireNonNull(type, "入口参数 type 不能为 null。");
			switch (type) {
			case CONCURRENT:
				return concurrentBackground;
			case FIFO:
				return queueBackground;
			}
			throw new IllegalArgumentException("入口参数 " + type + " 不合法");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Background getBackgroundReadOnly(BackgroundType type) {
			Objects.requireNonNull(type, "入口参数 type 不能为 null。");
			return BackgroundUtil.unmodifiableBackground(getBackground(type));
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SyncResourceHandler getCfgHandler() {
			return configurationHandler;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ResourceHandler getCfgHandlerReadOnly() {
			return ResourceUtil.unmodifiableResourceHandler(configurationHandler);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SyncMapModel<Class<? extends Module>, ReferenceModel<Toolkit>> getModuleToolkitModel()
				throws IllegalStateException {
			return moduleToolkitModel;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SyncExconfigModel getCoreConfigModel() {
			return coreConfigModel;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ExconfigModel getCoreConfigModelReadOnly() {
			return ConfigUtil.unmodifiableExconfigModel(coreConfigModel);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SyncMapModel<ProjectFilePair, Editor> getEditorModel() throws IllegalStateException {
			return editorModel;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SyncMapModel<ProjectFilePair, Editor> getEditorModelReadOnly() throws IllegalStateException {
			return com.dwarfeng.dutil.basic.cna.model.ModelUtil.syncMapModel(editorModel);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getExitCode() {
			synchronized (exitCodeLock) {
				return exitCode;
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SyncSetModel<Window> getExternalWindowModel() throws IllegalStateException {
			return externalWindowModel;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SetModel<Window> getExternalWindowModelReadOnly() throws IllegalStateException {
			return com.dwarfeng.dutil.basic.cna.model.ModelUtil.unmodifiableSetModel(externalWindowModel);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SyncMapModel<File, Image> getFileIconImageModel() throws IllegalStateException {
			return fileIconImageModel;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public MapModel<File, Image> getFileIconImageModelReadOnly() throws IllegalStateException {
			return com.dwarfeng.dutil.basic.cna.model.ModelUtil.unmodifiableMapModel(fileIconImageModel);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SyncMapModel<File, FileObverser> getFileIconObvModel() throws IllegalStateException {
			return fileIconObvModel;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SyncMapModel<Project, Editor> getFocusEditorModel() throws IllegalStateException {
			return focusEditorModel;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public MapModel<Project, Editor> getFocusEditorModelReadOnly() throws IllegalStateException {
			return com.dwarfeng.dutil.basic.cna.model.ModelUtil.unmodifiableMapModel(focusEditorModel);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SyncSetModel<File> getFocusFileModel() throws IllegalStateException {
			return focusFileModel;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SetModel<File> getFocusFileModelReadOnly() throws IllegalStateException {
			return com.dwarfeng.dutil.basic.cna.model.ModelUtil.unmodifiableSetModel(focusFileModel);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SyncReferenceModel<Project> getFocusProjectModel() throws IllegalStateException {
			return focusProjectModel;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ReferenceModel<Project> getFocusProjectModelReadOnly() throws IllegalStateException {
			return com.dwarfeng.dutil.basic.cna.model.ModelUtil.unmodifiableReferenceModel(focusProjectModel);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public GuiManager getGuiManager() {
			return guiManager;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SyncListModel<Project> getHoldProjectModel() throws IllegalStateException {
			return holdProjectModel;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ListModel<Project> getHoldProjectModelReadOnly() throws IllegalStateException {
			return com.dwarfeng.dutil.basic.cna.model.ModelUtil.unmodifiableListModel(holdProjectModel);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SyncI18nHandler getLabelI18nHandler() {
			return labelI18nHandler;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public I18nHandler getLabelI18nHandlerReadOnly() {
			return I18nUtil.unmodifiableI18nHandler(labelI18nHandler);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SyncLoggerHandler getLoggerHandler() {
			return loggerHandler;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public LoggerHandler getLoggerHandlerReadOnly() {
			return LoggerUtil.unmodifiableLoggerHandler(loggerHandler);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SyncI18nHandler getLoggerI18nHandler() {
			return loggerI18nHandler;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public I18nHandler getLoggerI18nHandlerReadOnly() {
			return I18nUtil.unmodifiableI18nHandler(loggerI18nHandler);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public MainFrame getMainFrame() {
			synchronized (mainFrameLock) {
				return mainFrame;
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SyncModuleModel getModuleModel() throws IllegalStateException {
			return moduleModel;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ModuleModel getModuleModelReadOnly() throws IllegalStateException {
			return ModelUtil.unmodifiableModuleModel(moduleModel);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public PluginClassLoader getPluginClassLoader() {
			return pluginClassLoader;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramObverser> getProgramObversers() {
			return programObversers;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SyncMapModel<Project, Image> getProjectIconImageModel() throws IllegalStateException {
			return projectIconImageModel;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public MapModel<Project, Image> getProjectIconImageModelReadOnly() throws IllegalStateException {
			return com.dwarfeng.dutil.basic.cna.model.ModelUtil.unmodifiableMapModel(projectIconImageModel);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SyncMapModel<Project, ProjectObverser> getProjectIconObvModel() throws IllegalStateException {
			return projectIconObvModel;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getProperty(ProjWizProperty property) {
			Objects.requireNonNull(property, "入口参数 key 不能为 null。");

			synchronized (property) {
				return properties.getOrDefault(property, Constants.DEFAULT_PROJWIZ_PROPERTIES.get(property));
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public RuntimeState getRuntimeState() {
			synchronized (runtimeStateLock) {
				return runtimeState;
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SyncToolkitPermModel getToolkitPermModel() throws IllegalStateException {
			return toolkitPermModel;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ToolkitPermModel getToolkitPermModelReadOnly() throws IllegalStateException {
			return ModelUtil.unmodifiableToolkitPermModel(toolkitPermModel);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SyncExconfigModel getViewConfigModel() {
			return viewConfigModel;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ExconfigModel getViewConfigModelReadOnly() {
			return ConfigUtil.unmodifiableExconfigModel(viewConfigModel);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasPermission(Method method) {
			return true;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void info(String message) {
			Objects.requireNonNull(message, "入口参数 message 不能为 null。");
			loggerHandler.info(message);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isMainFrameVisible() {
			synchronized (mainFrameLock) {
				if (Objects.isNull(mainFrame))
					return false;
				return mainFrame.isVisible();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean newMainFrame() {
			synchronized (mainFrameLock) {
				if (Objects.nonNull(mainFrame)) {
					return false;
				}

				DefaultMainFrameVisibleModel mainFrameVisibleModel = new DefaultMainFrameVisibleModel();

				mainFrame = new MainFrame(guiManager, labelI18nHandler, moduleModel, editorModel, mainFrameVisibleModel,
						anchorFileModel, focusProjectModel, focusFileModel, holdProjectModel, focusEditorModel,
						coreConfigModel);
				return true;
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void openFile(Project project, File file) throws IllegalStateException {
			Objects.requireNonNull(project, "入口参数 project 不能为 null。");
			Objects.requireNonNull(file, "入口参数 file 不能为 null。");

			final Task task = new OpenCertainFileTask(ProjWizard.this, project, file);
			submitTask(task, BackgroundType.CONCURRENT);

		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean removeCoreConfigObverser(ExconfigObverser coreConfigObverser) {
			return coreConfigModel.removeObverser(coreConfigObverser);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean removeProgramObverser(ProgramObverser obverser) {
			synchronized (programObversers) {
				return programObversers.remove(obverser);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setExitCode(int exitCode) {
			synchronized (exitCodeLock) {
				ProjWizard.this.exitCode = exitCode;
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setRuntimeState(RuntimeState runtimeState) {
			synchronized (runtimeStateLock) {
				RuntimeState oldState = ProjWizard.this.runtimeState;
				ProjWizard.this.runtimeState = runtimeState;
				fireRuntimeStateChanged(oldState, runtimeState);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public DialogOption showConfirmDialog(ConfirmDialogSetting setting) throws IllegalStateException {
			Objects.requireNonNull(setting, "入口参数 setting 不能为 null。");

			int result = JOptionPane.showConfirmDialog(getMainFrame(), setting.getMessage(), setting.getTitle(),
					setting.getDialogOptionCombo().getValue(), setting.getDialogMessage().getValue(),
					setting.getIcon());
			return int2DialogOption(result);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void showExternalWindow(Window window) throws IllegalStateException {
			Objects.requireNonNull(window, "入口参数 window 不能为 null。");

			externalWindowModel.getLock().writeLock().lock();
			try {
				if (externalWindowModel.contains(window)) {
					window.requestFocus();
					return;
				}

				SwingUtil.invokeInEventQueue(() -> {
					window.addWindowListener(new WindowAdapter() {

						@Override
						public void windowClosed(WindowEvent e) {
							if (externalWindowModel.contains(window)) {
								externalWindowModel.remove(window);
							}
						}
					});

					window.setLocationRelativeTo(mainFrame);
					window.setVisible(true);
					window.requestFocus();
				});

				externalWindowModel.add(window);
			} finally {
				externalWindowModel.getLock().writeLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object showInputDialog(InputDialogSetting setting) throws IllegalStateException {
			Objects.requireNonNull(setting, "入口参数 setting 不能为 null。");

			return JOptionPane.showInputDialog(getMainFrame(), setting.getMessage(), setting.getTitle(),
					setting.getDialogMessage().getValue(), setting.getIcon(), setting.getSelectionValues(),
					setting.getInitialSelectionValue());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void showMessageDialog(MessageDialogSetting setting) throws IllegalStateException {
			JOptionPane.showMessageDialog(getMainFrame(), setting.getMessage(), setting.getTitle(),
					setting.getDialogMessage().getValue(), setting.getIcon());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int showOptionDialog(Object message, String title, DialogOptionCombo dialogOptionCombo,
				DialogMessage dialogMessage, Icon icon, Object[] options, Object initialValue) {
			return JOptionPane.showOptionDialog(getMainFrame(), dialogMessage, title, dialogOptionCombo.getValue(),
					dialogMessage.getValue(), icon, options, initialValue);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void start() {
			synchronized (startExitLock) {
				if (isStartFlag()) {
					if (getRuntimeState() == RuntimeState.RUNNING) {
						throw new IllegalStateException("程序正在运行");
					}
					if (getRuntimeState() == RuntimeState.ENDED) {
						throw new IllegalStateException("程序已经结束");
					}
					throw new IllegalStateException("由于启动标志为 true，程序无法启动");
				}
				setStartFlag(true);

				// 编织观察网络
				concurrentBackground.addObverser(backgroundObverser);

				// 开启初始化过程
				final Task task = new PoseTask(ProjWizard.this);
				task.run();

				// 判断是否出现异常。
				Throwable throwable = task.getThrowable();
				if (Objects.nonNull(throwable)) {
					// 当此过程出现异常时，启动应急退出机制。
					// TODO 以后会做的更多，此处仅设置退出代码以及设置程序状态。
					fatal("异常", throwable);
					setExitCode(-12450);
					setRuntimeState(RuntimeState.ENDED);
				} else {
					// 设置程序的运行状态为正在运行
					setRuntimeState(RuntimeState.RUNNING);
				}
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void submitTask(Task task, BackgroundType type) {
			Objects.requireNonNull(task, "入口参数 task 不能为 null。");
			Objects.requireNonNull(type, "入口参数 type 不能为 null。");

			switch (type) {
			case CONCURRENT:
				concurrentBackground.submit(task);
				return;
			case FIFO:
				queueBackground.submit(task);
				return;
			}
			throw new IllegalArgumentException("入口参数 " + type + " 不合法");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void trace(String message) {
			Objects.requireNonNull(message, "入口参数 message 不能为 null。");
			loggerHandler.trace(message);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void tryExit() {
			// TODO 完善关闭程序的方法。
			// manager.getBackgroundModel().submit(flowProvider.newClosingFlow());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void warn(String message) {
			Objects.requireNonNull(message, "入口参数 message 不能为 null。");
			loggerHandler.warn(message);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void warn(String message, Throwable t) {
			Objects.requireNonNull(message, "入口参数 message 不能为 null。");
			Objects.requireNonNull(t, "入口参数 t 不能为 null。");
			loggerHandler.warn(message, t);
		}

		private DialogOption int2DialogOption(int i) {
			switch (i) {
			case JOptionPane.OK_OPTION:
				return DialogOption.OK_YES;
			case JOptionPane.NO_OPTION:
				return DialogOption.NO;
			case JOptionPane.CANCEL_OPTION:
				return DialogOption.CANCEL;
			case JOptionPane.CLOSED_OPTION:
				return DialogOption.CLOSED;
			default:
				return null;
			}
		}

		private boolean isDisposeFlag() {
			synchronized (disposeFlagLock) {
				return disposeFlag;
			}
		}

		private boolean isStartFlag() {
			synchronized (startFlagLock) {
				return startFlag;
			}
		}

		private void setDisposeFlag(boolean disposeFlag) {
			synchronized (disposeFlagLock) {
				ProjWizard.this.disposeFlag = disposeFlag;
			}
		}

		private void setStartFlag(boolean startFlag) {
			synchronized (startFlagLock) {
				ProjWizard.this.startFlag = startFlag;
			}
		}

	}

	/** 程序的版本 */
	public final static Version VERSION = new DefaultVersion.Builder().type(VersionType.ALPHA).firstVersion((byte) 0)
			.secondVersion((byte) 0).thirdVersion((byte) 3).buildDate("20171027").buildVersion('A').build();

	// /** 该类的线程工厂 */
	// private final static ThreadFactory THREAD_FACTORY = new
	// NumberedThreadFactory("ProjWizard");

	/** 程序的实例列表，用于持有引用 */
	private static final Set<ProjWizard> INSTANCES = Collections.synchronizedSet(new HashSet<>());

	// --------------------------------------------模型--------------------------------------------
	/** 并发后台。 */
	private final Background concurrentBackground = new ExecutorServiceBackground(
			Executors.newFixedThreadPool(4, ExecutorServiceBackground.THREAD_FACTORY),
			Collections.newSetFromMap(new WeakHashMap<>()));
	/** 队列后台。 */
	private final Background queueBackground = new ExecutorServiceBackground(
			Executors.newSingleThreadExecutor(ExecutorServiceBackground.THREAD_FACTORY),
			Collections.newSetFromMap(new WeakHashMap<>()));
	/** 核心配置模型。 */
	private final SyncExconfigModel coreConfigModel = ConfigUtil
			.syncExconfigModel(new DefaultExconfigModel(Arrays.asList(CoreConfigEntry.values())));
	/** 标签国际化处理器。 */
	private final SyncI18nHandler labelI18nHandler = I18nUtil.syncI18nHandler(new DelegateI18nHandler());
	// 记录器接口
	private final SyncLoggerHandler loggerHandler = LoggerUtil.syncLoggerHandler(new DelegateLoggerHandler());
	// 记录器国际化处理器
	private final SyncI18nHandler loggerI18nHandler = I18nUtil.syncI18nHandler(new DelegateI18nHandler());
	// 视图配置模型
	private final SyncExconfigModel viewConfigModel = ConfigUtil
			.syncExconfigModel(new DefaultExconfigModel(Arrays.asList(ViewConfigEntry.values())));
	// 配置处理器
	private final SyncResourceHandler configurationHandler = ResourceUtil
			.syncResourceHandler(new DelegateResourceHandler());
	// 锚点文件模型
	private final SyncReferenceModel<File> anchorFileModel = com.dwarfeng.dutil.basic.cna.model.ModelUtil
			.syncReferenceModel(new DefaultReferenceModel<>());
	// 焦点工程模型
	private final SyncReferenceModel<Project> focusProjectModel = com.dwarfeng.dutil.basic.cna.model.ModelUtil
			.syncReferenceModel(new DefaultReferenceModel<>());
	// 焦点文件模型
	private final SyncSetModel<File> focusFileModel = com.dwarfeng.dutil.basic.cna.model.ModelUtil
			.syncSetModel(new DelegateSetModel<>());
	// 持有工程模型
	private final SyncListModel<Project> holdProjectModel = com.dwarfeng.dutil.basic.cna.model.ModelUtil
			.syncListModel(new DelegateListModel<>());
	// 焦点编辑器模型
	private final SyncMapModel<Project, Editor> focusEditorModel = com.dwarfeng.dutil.basic.cna.model.ModelUtil
			.syncMapModel(new DelegateMapModel<>());
	// 编辑器模型
	private final SyncMapModel<ProjectFilePair, Editor> editorModel = com.dwarfeng.dutil.basic.cna.model.ModelUtil
			.syncMapModel(new DelegateMapModel<>());
	// 工程图标图片模型
	private final SyncMapModel<Project, Image> projectIconImageModel = com.dwarfeng.dutil.basic.cna.model.ModelUtil
			.syncMapModel(new DelegateMapModel<>());
	// 文件图标图片模型
	private final SyncMapModel<File, Image> fileIconImageModel = com.dwarfeng.dutil.basic.cna.model.ModelUtil
			.syncMapModel(new DelegateMapModel<>());
	// 工程的图标侦听器模型
	private final SyncMapModel<Project, ProjectObverser> projectIconObvModel = com.dwarfeng.dutil.basic.cna.model.ModelUtil
			.syncMapModel(new DelegateMapModel<>());
	// 文件的图标侦听器模型
	private final SyncMapModel<File, FileObverser> fileIconObvModel = com.dwarfeng.dutil.basic.cna.model.ModelUtil
			.syncMapModel(new DelegateMapModel<>());
	// 组件模型
	private final SyncModuleModel moduleModel = ModelUtil.syncModuleModel(new DefaultModuleModel());
	// 工具包权限模型
	private final SyncToolkitPermModel toolkitPermModel = ModelUtil.syncToolkitPermModel(new DefaultToolkitPermModel());
	// 组件-工具包引用模型
	private final SyncMapModel<Class<? extends Module>, ReferenceModel<Toolkit>> moduleToolkitModel = com.dwarfeng.dutil.basic.cna.model.ModelUtil
			.syncMapModel(new DelegateMapModel<>());
	/** 外部窗口模型。 */
	private final SyncSetModel<Window> externalWindowModel = com.dwarfeng.dutil.basic.cna.model.ModelUtil
			.syncSetModel(new DelegateSetModel<>());

	// --------------------------------------------控制--------------------------------------------
	/** 程序的侦听器集合 */
	private final Set<ProgramObverser> programObversers = Collections.newSetFromMap(new WeakHashMap<>());

	/** 程序的属性集合。 */
	private final Map<ProjWizProperty, String> properties = new EnumMap<>(ProjWizProperty.class);
	/** 插件类加载器 */
	private final PluginClassLoader pluginClassLoader = new DefaultPluginClassLoader();

	/** 启动标志 */
	private boolean startFlag = false;
	/** 启动标志同步锁 */
	private final Object startFlagLock = new Object();
	/** 释放标志 */
	private boolean disposeFlag = false;
	/** 释放标识同步锁 */
	private final Object disposeFlagLock = new Object();

	/** 程序的状态 */
	private RuntimeState runtimeState;
	/** 程序的状态的同步锁 */
	private final Object runtimeStateLock = new Object();

	/** 程序的退出代码 */
	private int exitCode = Integer.MIN_VALUE;
	/** 程序的退出代码的同步锁 */
	private final Object exitCodeLock = new Object();

	/** 程序的工具包 */
	private final Toolkit toolkit = new ProjWizToolKit();

	/** 程序的启动-退出同步锁 */
	private final Object startExitLock = new Object();

	// --------------------------------------------视图--------------------------------------------
	// 界面管理器。
	private final GuiManager guiManager = new ProjWizGuiManager();
	// 主界面。
	private MainFrame mainFrame;
	private final Object mainFrameLock = new Object();

	// --------------------------------------------观察器--------------------------------------------
	/** 后台模型侦听器 */
	private final BackgroundObverser backgroundObverser = new BackgroundObverserImpl(this);

	/**
	 * 新实例。
	 */
	public ProjWizard() {
		this(new String[0]);
	}

	/**
	 * 新实例。
	 * 
	 * @param args
	 *            参数名称与值。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IllegalArgumentException
	 *             参数<code>args</code> 不合法。
	 */
	public ProjWizard(String... args) {
		Objects.requireNonNull(args, "入口参数 args 不能为 null。");

		// 加载系统参数
		for (String string : args) {
			StringTokenizer st = new StringTokenizer(string, "=");
			String key;
			String value;
			try {
				// 忽视大小写
				key = st.nextToken().toUpperCase();
				value = st.nextToken();
			} catch (NoSuchElementException e) {
				throw new IllegalArgumentException("非法的参数：" + string);
			}

			ProjWizProperty property = null;
			if (Objects.isNull(property = ProjWizProperty.valueOf(key))) {
				throw new IllegalArgumentException("不存在的键：" + key);
			}

			properties.put(property, value);
		}

		// 为自己保留引用。
		INSTANCES.add(this);
	}

	/**
	 * 获取ProjWizard中的工具包。
	 * 
	 * @return ProjWizard中的工具包。
	 */
	public Toolkit getToolkit() {
		return toolkit;
	}

	private void fireRuntimeStateChanged(RuntimeState oldState, RuntimeState newState) {
		for (ProgramObverser obverser : programObversers) {
			if (Objects.nonNull(obverser))
				obverser.fireRuntimeStateChanged(oldState, newState);
		}
	}

}
