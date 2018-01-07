package com.dwarfeng.projwiz.core.control;

import java.util.Objects;

import com.dwarfeng.dutil.basic.cna.model.SyncReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.SyncSetModel;
import com.dwarfeng.projwiz.core.model.eum.DialogMessage;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.model.eum.LoggerStringKey;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.util.ModelUtil;

final class RenameAnchorFileTask extends ProjWizTask {

	public RenameAnchorFileTask(ProjWizard projWizard) {
		super(projWizard);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		SyncReferenceModel<File> anchorFileModel = projWizard.getToolkit().getAnchorFileModel();
		SyncSetModel<File> focusFileModel = projWizard.getToolkit().getFocusFileModel();
		SyncReferenceModel<Project> focusProjectModel = projWizard.getToolkit().getFocusProjectModel();

		File anchorFile;
		Project focusProject;

		anchorFileModel.getLock().readLock().lock();
		focusProjectModel.getLock().readLock().lock();
		try {
			focusProject = focusProjectModel.get();
			anchorFile = anchorFileModel.get();
		} finally {
			focusProjectModel.getLock().readLock().unlock();
			anchorFileModel.getLock().readLock().unlock();
		}

		if (!focusProject.isRenameFileSupported()) {
			projWizard.getToolkit().showMessageDialog(label(LabelStringKey.MSGDIA_10), label(LabelStringKey.MSGDIA_11),
					DialogMessage.INFORMATION_MESSAGE);
			return;
		}

		String newName = projWizard.getToolkit().showInputDialog(label(LabelStringKey.INPUTDIA_1),
				label(LabelStringKey.INPUTDIA_2), DialogMessage.QUESTION_MESSAGE);

		// 确保该名称在相同的目录下没有被使用。
		focusProject.getLock().readLock().lock();
		try {
			if (isFileNameRepetitionExists(focusProject, focusProject.getFileTree().getParent(anchorFile), newName)) {
				projWizard.getToolkit().showMessageDialog(formatLabel(LabelStringKey.MSGDIA_38, newName),
						label(LabelStringKey.MSGDIA_39), DialogMessage.WARNING_MESSAGE);
				return;
			}
		} finally {
			focusProject.getLock().readLock().unlock();
		}

		if (Objects.isNull(newName) || newName.equals("")) {
			return;
		}

		if (Objects.isNull(focusProject.renameFile(ModelUtil.unmodifiableFile(anchorFile), newName))) {
			warn(LoggerStringKey.TASK_RENAMEFILE_0);
			warn(LoggerStringKey.TASK_RENAMEFILE_1);
			formatWarn(LoggerStringKey.TASK_RENAMEFILE_2, anchorFile.getRegisterKey(), anchorFile.getName(),
					anchorFile.getClass().toString());
		}

		anchorFileModel.getLock().writeLock().lock();
		focusFileModel.getLock().writeLock().lock();
		try {
			focusFileModel.clear();
			focusFileModel.add(anchorFile);
			anchorFileModel.set(anchorFile);
		} finally {
			focusFileModel.getLock().writeLock().unlock();
			anchorFileModel.getLock().writeLock().unlock();
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
