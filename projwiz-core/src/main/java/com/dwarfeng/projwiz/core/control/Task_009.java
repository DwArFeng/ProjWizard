package com.dwarfeng.projwiz.core.control;

import java.util.Objects;

import com.dwarfeng.dutil.basic.cna.model.SyncReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.SyncSetModel;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.model.eum.LoggerStringKey;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.FileProcessor;
import com.dwarfeng.projwiz.core.model.struct.Module;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.util.ProjectFileUtil;
import com.dwarfeng.projwiz.core.view.eum.DialogMessage;
import com.dwarfeng.projwiz.core.view.struct.InputDialogSetting;
import com.dwarfeng.projwiz.core.view.struct.MessageDialogSetting;
import com.dwarfeng.projwiz.core.view.struct.ModuleChooserSetting;

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

		for (FileProcessor processor : projWizard.getToolkit().getModuleModel().getSubs(FileProcessor.class)) {
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

		Module[] modules = projWizard.getToolkit().chooseModule(
				new ModuleChooserSetting.Builder().setMultiSelectionEnabled(false).setModuleFilter(module -> {
					if (!(module instanceof FileProcessor)) {
						return false;
					}
					return ((FileProcessor) module).isNewFileSupported();
				}).build());
		if (modules.length == 0) {
			return;
		}
		FileProcessor processor = (FileProcessor) modules[0];

		// 询问该文件的名称。
		String exceptName = null;
		exceptName = (String) projWizard.getToolkit()
				.showInputDialog(new InputDialogSetting.Builder().setTitle(label(LabelStringKey.INPUTDIA_3))
						.setMessage(label(LabelStringKey.INPUTDIA_4)).setDialogMessage(DialogMessage.QUESTION_MESSAGE)
						.build());

		while (!isValidName(exceptName)) {
			if (Objects.isNull(exceptName)) {
				return;
			}

			exceptName = (String) projWizard.getToolkit()
					.showInputDialog(new InputDialogSetting.Builder().setTitle(label(LabelStringKey.INPUTDIA_3))
							.setMessage(label(LabelStringKey.INPUTDIA_5))
							.setDialogMessage(DialogMessage.QUESTION_MESSAGE).build());
		}

		// 通过处理器生成新文件。
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

		File actualAddedFile = focusProject.addFile(parentFile, newFile, exceptName, Project.AddingSituation.BY_NEW);

		if (Objects.isNull(actualAddedFile)) {
			warn(LoggerStringKey.TASK_NEWFILE_0);
			warn(LoggerStringKey.TASK_NEWFILE_1);
			formatWarn(LoggerStringKey.TASK_NEWFILE_2, newFile.getProcessorClass().getName(),
					newFile.getClass().toString());
			return;
		}

		info(LoggerStringKey.TASK_NEWFILE_5);
		formatInfo(LoggerStringKey.TASK_NEWFILE_4, processor.getClass().getName(), processor.getClass().toString());
		info(LoggerStringKey.TASK_NEWFILE_3);
		formatInfo(LoggerStringKey.TASK_NEWFILE_2, newFile.getProcessorClass().getName(),
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

	private boolean isValidName(String name) {
		return ProjectFileUtil.isValidFileName(name);
	}

}
