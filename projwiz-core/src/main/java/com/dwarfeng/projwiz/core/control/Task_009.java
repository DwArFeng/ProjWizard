package com.dwarfeng.projwiz.core.control;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import com.dwarfeng.dutil.basic.cna.model.SyncReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.SyncSetModel;
import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.projwiz.core.model.eum.DialogMessage;
import com.dwarfeng.projwiz.core.model.eum.DialogOption;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.model.eum.LoggerStringKey;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.FileProcessor;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.view.gui.ComponentSelectDialog;
import com.dwarfeng.projwiz.core.view.struct.MessageDialogSetting;

final class NewFileTask extends ProjWizTask {

	public NewFileTask(ProjWizard projWizard) {
		super(projWizard);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		SyncReferenceModel<Project> focusProjectModel = projWizard.getToolkit().getFocusProjectModel();
		SyncReferenceModel<File> anchorFileModel = projWizard.getToolkit().getAnchorFileModel();
		SyncSetModel<File> focusFileModel = projWizard.getToolkit().getFocusFileModel();

		final Project focusProject;
		final File anchorFile;

		focusProjectModel.getLock().readLock().lock();
		anchorFileModel.getLock().readLock().lock();
		try {
			focusProject = focusProjectModel.get();
			anchorFile = anchorFileModel.get();
		} finally {
			anchorFileModel.getLock().readLock().unlock();
			focusProjectModel.getLock().readLock().unlock();
		}

		if (Objects.isNull(focusProject))
			return;

		if (!focusProject.isAddFileSupported(Project.AddingSituation.BY_NEW)) {
			projWizard.getToolkit()
					.showMessageDialog(new MessageDialogSetting.Builder().setMessage(label(LabelStringKey.MSGDIA_12))
							.setTitle(label(LabelStringKey.MSGDIA_13))
							.setDialogMessage(DialogMessage.INFORMATION_MESSAGE).build());
			return;
		}

		boolean emptyFlag = true;

		for (FileProcessor processor : projWizard.getToolkit().getComponentModel().getAll(FileProcessor.class)) {
			if (processor.isNewFileSupported()) {
				emptyFlag = false;
				break;
			}
		}

		if (emptyFlag) {
			projWizard.getToolkit()
					.showMessageDialog(new MessageDialogSetting.Builder().setMessage(label(LabelStringKey.MSGDIA_5))
							.setTitle(label(LabelStringKey.MSGDIA_6))
							.setDialogMessage(DialogMessage.INFORMATION_MESSAGE).build());
		}

		AtomicReference<ComponentSelectDialog> dialogRef = new AtomicReference<>();

		SwingUtil.invokeAndWaitInEventQueue(() -> {
			dialogRef.set(new ComponentSelectDialog(projWizard.getToolkit().getGuiManager(),
					projWizard.getToolkit().getLabelI18nHandler(), projWizard.getToolkit().getMainFrame(),
					projWizard.getToolkit().getComponentModel(), component -> {
						if (!(component instanceof FileProcessor)) {
							return false;
						}
						return ((FileProcessor) component).isNewFileSupported();
					}));
		});

		projWizard.getToolkit().showExternalWindow(dialogRef.get());
		dialogRef.get().waitDispose();

		if (dialogRef.get().getOption() != DialogOption.OK_YES) {
			return;
		}

		FileProcessor processor = dialogRef.get().getCurrentComponent(FileProcessor.class);
		if (Objects.isNull(processor)) {
			return;
		}

		File newFile = processor.newFile();
		if (Objects.isNull(newFile)) {
			return;
		}

		File parentFile;
		if (Objects.isNull(anchorFile)) {
			parentFile = focusProject.getFileTree().getRoot();
		} else if (anchorFile.isFolder()) {
			parentFile = anchorFile;
		} else {
			parentFile = focusProject.getFileTree().getParent(anchorFile);
		}

		// 检查 parentFile 中是否已经存在与 newFile 名称相同的文件。
		parentFile.getLock().readLock().lock();
		try {
			if (isFileNameRepetitionExists(focusProject, parentFile, newFile.getName())) {
				projWizard.getToolkit()
						.showMessageDialog(new MessageDialogSetting.Builder()
								.setMessage(formatLabel(LabelStringKey.MSGDIA_37, newFile.getName()))
								.setTitle(label(LabelStringKey.MSGDIA_13))
								.setDialogMessage(DialogMessage.WARNING_MESSAGE).build());
				return;
			}
		} finally {
			parentFile.getLock().readLock().unlock();
		}

		File actualAddedFile = focusProject.addFile(parentFile, newFile, Project.AddingSituation.BY_NEW);

		if (Objects.isNull(actualAddedFile)) {
			warn(LoggerStringKey.TASK_NEWFILE_0);
			warn(LoggerStringKey.TASK_NEWFILE_1);
			formatWarn(LoggerStringKey.TASK_NEWFILE_2, newFile.getComponentKey(), newFile.getName(),
					newFile.getClass().toString());
			return;
		}

		info(LoggerStringKey.TASK_NEWFILE_5);
		formatInfo(LoggerStringKey.TASK_NEWFILE_4, processor.getKey(), processor.getClass().toString());
		info(LoggerStringKey.TASK_NEWFILE_3);
		formatInfo(LoggerStringKey.TASK_NEWFILE_2, newFile.getComponentKey(), newFile.getName(),
				newFile.getClass().toString());

		focusProjectModel.getLock().writeLock().lock();
		focusFileModel.getLock().writeLock().lock();
		try {
			anchorFileModel.set(actualAddedFile);
			focusFileModel.clear();
			focusFileModel.add(actualAddedFile);
		} finally {
			focusFileModel.getLock().writeLock().unlock();
			focusProjectModel.getLock().writeLock().unlock();
		}
	}

	private boolean isFileNameRepetitionExists(Project project, File parentFile, String name) {
		for (File file : project.getFileTree().getChilds(parentFile)) {
			if (Objects.equals(name, file.getName())) {
				return true;
			}
		}
		return false;
	}
}
