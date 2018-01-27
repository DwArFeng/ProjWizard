package com.dwarfeng.projwiz.core.control;

import java.awt.Image;
import java.awt.Window;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
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

import com.dwarfeng.dutil.basic.cna.model.DefaultReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.DelegateListModel;
import com.dwarfeng.dutil.basic.cna.model.DelegateMapModel;
import com.dwarfeng.dutil.basic.cna.model.DelegateSetModel;
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
import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.dutil.basic.prog.DefaultVersion;
import com.dwarfeng.dutil.basic.prog.ProcessException;
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
import com.dwarfeng.projwiz.core.model.cm.DelegateComponentModel;
import com.dwarfeng.projwiz.core.model.cm.ExternalWindowModel;
import com.dwarfeng.projwiz.core.model.cm.SyncComponentModel;
import com.dwarfeng.projwiz.core.model.eum.CoreConfiguration;
import com.dwarfeng.projwiz.core.model.eum.DialogMessage;
import com.dwarfeng.projwiz.core.model.eum.DialogOption;
import com.dwarfeng.projwiz.core.model.eum.DialogOptionCombo;
import com.dwarfeng.projwiz.core.model.eum.ModalConfiguration;
import com.dwarfeng.projwiz.core.model.eum.ProjWizProperty;
import com.dwarfeng.projwiz.core.model.io.DefaultPluginClassLoader;
import com.dwarfeng.projwiz.core.model.io.PluginClassLoader;
import com.dwarfeng.projwiz.core.model.obv.FileObverser;
import com.dwarfeng.projwiz.core.model.obv.ProjectObverser;
import com.dwarfeng.projwiz.core.model.struct.Editor;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.FileProcessor;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.ProjectFilePair;
import com.dwarfeng.projwiz.core.model.struct.ProjectProcessor;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;
import com.dwarfeng.projwiz.core.model.struct.Toolkit.BackgroundType;
import com.dwarfeng.projwiz.core.util.Constants;
import com.dwarfeng.projwiz.core.util.ModelUtil;
import com.dwarfeng.projwiz.core.view.gui.MainFrame;
import com.dwarfeng.projwiz.core.view.gui.ProjectFileChooser;
import com.dwarfeng.projwiz.core.view.gui.ProjectFileChooser.ReturnOption;
import com.dwarfeng.projwiz.core.view.gui.SystemFileChooser;
import com.dwarfeng.projwiz.core.view.struct.DefaultMainFrameVisibleModel;
import com.dwarfeng.projwiz.core.view.struct.GuiManager;
import com.dwarfeng.projwiz.core.view.struct.ProjectFileChooserSetting;
import com.dwarfeng.projwiz.core.view.struct.SystemFileChooserSetting;
import com.dwarfeng.projwiz.core.view.struct.WindowSuppiler;

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
		public void showIndicateMonitor(ExecType type) {
			doTask(new ShowIndicateMonitorTask(ProjWizard.this), type);
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
		public void addFile2ProjectAsNew(File src, Project project, File dest)
				throws IllegalStateException, IllegalArgumentException, ProcessException {
			// TODO Auto-generated method stub
			Project modifiableProject = projectIndicateModel.get(project.getUniqueLabel());
			File modifiableDest = fileIndicateModel.get(dest.getUniqueLabel());

			if (Objects.isNull(modifiableProject) || Objects.isNull(modifiableDest)) {
				throw new ProcessException("未能在指示器模型中找到指定模型或目标文件");
			}

			if (!dest.isFolder()) {
				throw new ProcessException("目标文件不是文件夹");
			}

			File actualAddedFile = project.addFileByNew(src, dest);

			if (Objects.isNull(actualAddedFile)) {
				throw new ProcessException("由于工程处理器的原因，文件没有成功添加");
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean addObverserToFile(File file, FileObverser obverser) throws IllegalStateException {
			File modifiableFile = fileIndicateModel.get(file.getUniqueLabel());
			return modifiableFile.addObverser(obverser);
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
		public File[] chooseProjectFile(ProjectFileChooserSetting setting) throws IllegalStateException {
			Objects.requireNonNull(setting, "入口参数 setting 不能为 null。");

			ProjectFileChooser fileChooser = new ProjectFileChooser(guiManager, labelI18nHandler, holdProjectModel,
					componentModel);
			fileChooser.setCurrentDirectory(setting.getCurrentDirectory());
			fileChooser.setDialogType(setting.getDialogType());
			fileChooser.setFileFilters(setting.getFileFilters());
			// fileChooser.setFileSelectionMode(setting.getFileSelectionMode().getValue());
			fileChooser.setFileSelectionMode(setting.getFileSelectionMode());
			fileChooser.setAcceptAllFileFilterUsed(setting.isAcceptAllFileFilterUsed());
			fileChooser.setControlButtonsAreShown(setting.isControlButtonsAreShown());
			fileChooser.setDragEnabled(setting.isDragEnabled());
			fileChooser.setFileHidingEnabled(setting.isFileHidingEnabled());
			fileChooser.setMultiSelectionEnabled(setting.isMultiSelectionEnabled());
			fileChooser.setLocale(labelI18nHandler.getCurrentLocale());

			ReturnOption returnOption;
			switch (setting.getDialogType()) {
			case OPEN_DIALOG:
				returnOption = fileChooser.showOpenDialog(mainFrame);
				break;
			case SAVE_DIALOG:
				returnOption = fileChooser.showSaveDialog(mainFrame);
				break;
			default:
				returnOption = fileChooser.showOpenDialog(mainFrame);
				break;
			}

			switch (returnOption) {
			case APPROVE_OPTION:
				return new File[0];
			case CANCEL_OPTION:
				if (setting.isMultiSelectionEnabled()) {
					return fileChooser.getSelectedFiles();
				} else {
					return new File[] { fileChooser.getSelectedFile() };
				}
			case ERROR_OPTION:
				return new File[0];
			default:
				return new File[0];
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public java.io.File[] chooseSystemFile(SystemFileChooserSetting setting) throws IllegalStateException {
			Objects.requireNonNull(setting, "入口参数 setting 不能为 null。");

			SystemFileChooser fileChooser = new SystemFileChooser(guiManager, labelI18nHandler);
			fileChooser.setCurrentDirectory(setting.getCurrentDirectory());
			switch (setting.getDialogType()) {
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
			switch (setting.getDialogType()) {
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

			switch (selectResult) {
			case JFileChooser.CANCEL_OPTION:
				return new java.io.File[0];
			case JFileChooser.APPROVE_OPTION:
				if (setting.isMultiSelectionEnabled()) {
					return fileChooser.getSelectedFiles();
				} else {
					return new java.io.File[] { fileChooser.getSelectedFile() };
				}
			case JFileChooser.ERROR_OPTION:
				return new java.io.File[0];
			default:
				return new java.io.File[0];
			}

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
		public boolean containsDialog(String key) {
			return externalWindowModel.containsKey(key);
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

				externalWindowModel.clear();

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
			return com.dwarfeng.dutil.basic.cna.model.ModelUtil.readOnlyReferenceModel(anchorFileModel, (file) -> {
				return ModelUtil.unmodifiableFile(file);
			});
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
			return BackgroundUtil.readOnlyBackground(getBackground(type));
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
			return ResourceUtil.readOnlyResourceHandler(configurationHandler);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SyncComponentModel getComponentModel() throws IllegalStateException {
			return componentModel;
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
		public SyncKeySetModel<String, WindowSuppiler> getExternalWindowModel() {
			return externalWindowModel;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public KeySetModel<String, WindowSuppiler> getExternalWindowModelReadOnly() throws IllegalStateException {
			// TODO Auto-generated method stub
			return null;
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
			return com.dwarfeng.dutil.basic.cna.model.ModelUtil.readOnlyMapModel(fileIconImageModel, file -> {
				return ModelUtil.unmodifiableFile(file);
			}, image -> {
				// TODO image还没有实现只读。
				return image;
			});
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
		public SyncMapModel<String, File> getFileIndicateModel() throws IllegalStateException {
			return fileIndicateModel;
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
			return com.dwarfeng.dutil.basic.cna.model.ModelUtil.readOnlyMapModel(focusEditorModel, (project) -> {
				return ModelUtil.unmodifiableProject(project);
			}, (editor) -> {
				return ModelUtil.unmodifiableEditor(editor);
			});
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
			return com.dwarfeng.dutil.basic.cna.model.ModelUtil.readOnlySetModel(focusFileModel, (file) -> {
				return ModelUtil.unmodifiableFile(file);
			});
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
			return com.dwarfeng.dutil.basic.cna.model.ModelUtil.readOnlyReferenceModel(focusProjectModel, (project) -> {
				return ModelUtil.unmodifiableProject(project);
			});
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
			return com.dwarfeng.dutil.basic.cna.model.ModelUtil.readOnlyListModel(holdProjectModel, (project) -> {
				return ModelUtil.unmodifiableProject(project);
			});
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
			return I18nUtil.readOnlyI18nHandler(labelI18nHandler);
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
			return LoggerUtil.readOnlyLoggerHandler(loggerHandler);
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
			return I18nUtil.readOnlyI18nHandler(loggerI18nHandler);
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
		public SyncExconfigModel getModalConfigModel() {
			return modalConfigModel;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ExconfigModel getModalConfigModelReadOnly() {
			return ConfigUtil.unmodifiableExconfigModel(modalConfigModel);
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
			return com.dwarfeng.dutil.basic.cna.model.ModelUtil.readOnlyMapModel(projectIconImageModel, project -> {
				return ModelUtil.unmodifiableProject(project);
			}, image -> {
				// TODO image还没有实现只读。
				return image;
			});
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
		public SyncMapModel<String, Project> getProjectIndicateModel() throws IllegalStateException {
			return projectIndicateModel;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public KeySetModel<String, ProjectProcessor> getProjectProcessors() throws IllegalStateException {
			return com.dwarfeng.dutil.basic.cna.model.ModelUtil
					.readOnlyKeySetModel(componentModel.getAll(ProjectProcessor.class), projectProcessor -> {
						return ModelUtil.unmodifableProjectProcessor(projectProcessor);
					});
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

				mainFrame = new MainFrame(guiManager, labelI18nHandler, componentModel, editorModel,
						mainFrameVisibleModel, anchorFileModel, focusProjectModel, focusFileModel, holdProjectModel,
						focusEditorModel, coreConfigModel);
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
		public InputStream openFileInputStream(File file, String label)
				throws IllegalStateException, IllegalArgumentException, IOException {
			Objects.requireNonNull(file, "入口参数 file 不能为 null。");
			Objects.requireNonNull(label, "入口参数 label 不能为 null。");

			File modifiableFile = fileIndicateModel.get(file.getUniqueLabel());

			if (Objects.isNull(modifiableFile)) {
				throw new IllegalArgumentException("没有在文件指示模型中找到指定的文件");
			}
			if (!modifiableFile.getLabels().contains(label)) {
				throw new IllegalArgumentException("指定的文件不存在指定的标签: " + label);
			}
			if (!file.isReadSupported()) {
				throw new IllegalArgumentException("指定的文件不支持读取操作");
			}

			return modifiableFile.openInputStream(label);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public OutputStream openFileOutputStream(File file, String label)
				throws IllegalStateException, IllegalArgumentException, IOException {
			Objects.requireNonNull(file, "入口参数 file 不能为 null。");
			Objects.requireNonNull(label, "入口参数 label 不能为 null。");

			File modifiableFile = fileIndicateModel.get(file.getUniqueLabel());

			if (Objects.isNull(modifiableFile)) {
				throw new IllegalArgumentException("没有在文件指示模型中找到指定的文件");
			}
			if (!modifiableFile.getLabels().contains(label)) {
				throw new IllegalArgumentException("指定的文件不存在指定的标签: " + label);
			}
			if (!file.isWriteSupported()) {
				throw new IllegalArgumentException("指定的文件不支持写入操作");
			}

			return modifiableFile.openOutputStream(label);

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
		public boolean removeObverserFromFile(File file, FileObverser obverser) throws IllegalStateException {
			File modifiableFile = fileIndicateModel.get(file.getUniqueLabel());
			return modifiableFile.removeObverser(obverser);
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
		public DialogOption showConfirmDialog(Object message) {
			return int2DialogOption(JOptionPane.showConfirmDialog(getMainFrame(), message));
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public DialogOption showConfirmDialog(Object message, String title, DialogOptionCombo dialogOptionCombo) {
			return int2DialogOption(
					JOptionPane.showConfirmDialog(getMainFrame(), message, title, dialogOptionCombo.getValue()));
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public DialogOption showConfirmDialog(Object message, String title, DialogOptionCombo dialogOptionCombo,
				DialogMessage dialogMessage) {
			return int2DialogOption(JOptionPane.showConfirmDialog(getMainFrame(), message, title,
					dialogOptionCombo.getValue(), dialogMessage.getValue()));
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public DialogOption showConfirmDialog(Object message, String title, DialogOptionCombo dialogOptionCombo,
				DialogMessage dialogMessage, Icon icon) {
			return int2DialogOption(JOptionPane.showConfirmDialog(getMainFrame(), message, title,
					dialogOptionCombo.getValue(), dialogMessage.getValue(), icon));
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void showExternalWindow(String key) {
			externalWindowModel.getLock().readLock().lock();
			try {
				if (!externalWindowModel.containsKey(key)) {
					return;
				}

				WindowSuppiler suppiler = externalWindowModel.get(key);
				SwingUtil.invokeInEventQueue(() -> {
					Window window = suppiler.getWindow();
					window.setVisible(true);
					window.requestFocus();
				});
			} finally {
				externalWindowModel.getLock().readLock().unlock();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void showExternalWindow(WindowSuppiler suppiler) {
			Objects.requireNonNull(suppiler, "入口参数 suppiler 不能为 null。");

			externalWindowModel.getLock().writeLock().lock();
			try {
				if (!externalWindowModel.containsKey(suppiler.getKey())) {
					externalWindowModel.add(suppiler);
					SwingUtil.invokeInEventQueue(() -> {
						Window window = suppiler.getWindow();
						window.setLocationRelativeTo(getMainFrame());
						window.setVisible(true);
						window.requestFocus();
					});
				} else {
					WindowSuppiler suppilerAlreadyExists = externalWindowModel.get(suppiler.getKey());
					if (Objects.deepEquals(suppilerAlreadyExists, suppiler)) {
						SwingUtil.invokeInEventQueue(() -> {
							Window window = suppilerAlreadyExists.getWindow();
							window.setVisible(true);
							window.requestFocus();
						});
					} else {
						externalWindowModel.remove(suppilerAlreadyExists);
						externalWindowModel.add(suppiler);
						SwingUtil.invokeInEventQueue(() -> {
							Window window = suppiler.getWindow();
							window.setVisible(true);
							window.setLocationRelativeTo(getMainFrame());
							window.requestFocus();
						});
					}
				}
			} finally {
				externalWindowModel.getLock().writeLock().unlock();
			}

		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String showInputDialog(Object message) {
			return JOptionPane.showInputDialog(getMainFrame(), message);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String showInputDialog(Object message, Object initialSelectionValue) {
			return JOptionPane.showInputDialog(getMainFrame(), message, initialSelectionValue);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String showInputDialog(Object message, String title, DialogMessage dialogMessage) {
			return JOptionPane.showInputDialog(getMainFrame(), message, title, dialogMessage.getValue());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object showInputDialog(Object message, String title, DialogMessage dialogMessage, Icon icon,
				Object[] selectionValues, Object initialSelectionValue) {
			return JOptionPane.showInputDialog(getMainFrame(), message, title, dialogMessage.getValue(), icon,
					selectionValues, initialSelectionValue);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void showMessageDialog(Object message) {
			JOptionPane.showMessageDialog(getMainFrame(), message);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void showMessageDialog(Object message, String title, DialogMessage dialogMessage) {
			JOptionPane.showMessageDialog(getMainFrame(), message, title, dialogMessage.getValue());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void showMessageDialog(Object message, String title, DialogMessage dialogMessage, Icon icon) {
			JOptionPane.showMessageDialog(getMainFrame(), message, title, dialogMessage.getValue(), icon);
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

		/**
		 * {@inheritDoc}
		 */
		@Override
		public KeySetModel<String, FileProcessor> getFileProcessors() throws IllegalStateException {
			return com.dwarfeng.dutil.basic.cna.model.ModelUtil
					.readOnlyKeySetModel(componentModel.getAll(FileProcessor.class), fileProcessor -> {
						return ModelUtil.unmodifiableFileProcessor(fileProcessor);
					});
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
	// 并发后台
	private final Background concurrentBackground = new ExecutorServiceBackground(
			Executors.newFixedThreadPool(4, ExecutorServiceBackground.THREAD_FACTORY),
			Collections.newSetFromMap(new WeakHashMap<>()));
	private final Background queueBackground = new ExecutorServiceBackground(
			Executors.newSingleThreadExecutor(ExecutorServiceBackground.THREAD_FACTORY),
			Collections.newSetFromMap(new WeakHashMap<>()));
	// 核心配置模型
	private final SyncExconfigModel coreConfigModel = ConfigUtil
			.syncExconfigModel(new DefaultExconfigModel(Arrays.asList(CoreConfiguration.values())));
	// 标签国际化处理器
	private final SyncI18nHandler labelI18nHandler = I18nUtil.syncI18nHandler(new DelegateI18nHandler());
	// 记录器接口
	private final SyncLoggerHandler loggerHandler = LoggerUtil.syncLoggerHandler(new DelegateLoggerHandler());
	// 记录器国际化处理器
	private final SyncI18nHandler loggerI18nHandler = I18nUtil.syncI18nHandler(new DelegateI18nHandler());
	// 模态配置模型
	private final SyncExconfigModel modalConfigModel = ConfigUtil
			.syncExconfigModel(new DefaultExconfigModel(Arrays.asList(ModalConfiguration.values())));
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
	// 外部窗口
	private final SyncKeySetModel<String, WindowSuppiler> externalWindowModel = com.dwarfeng.dutil.basic.cna.model.ModelUtil
			.syncKeySetModel(new ExternalWindowModel());
	// 编辑器模型
	private final SyncMapModel<ProjectFilePair, Editor> editorModel = com.dwarfeng.dutil.basic.cna.model.ModelUtil
			.syncMapModel(new DelegateMapModel<>());
	// 工程指示器模型
	private final SyncMapModel<String, Project> projectIndicateModel = com.dwarfeng.dutil.basic.cna.model.ModelUtil
			.syncMapModel(new DelegateMapModel<>());
	// 文件指示器模型
	private final SyncMapModel<String, File> fileIndicateModel = com.dwarfeng.dutil.basic.cna.model.ModelUtil
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
	private final SyncComponentModel componentModel = ModelUtil.syncComponentModel(new DelegateComponentModel());

	// --------------------------------------------控制--------------------------------------------
	/** 程序的侦听器集合 */
	private final Set<ProgramObverser> programObversers = Collections.newSetFromMap(new WeakHashMap<>());

	/** 程序的属性集合。 */
	private final Map<String, String> properties = new HashMap<>();
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
			if (!Constants.DEFAULT_PROJWIZ_PROPERTIES.containsKey(key)) {
				throw new IllegalArgumentException("不存在的键：" + key);
			}

			properties.put(key, value);
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
