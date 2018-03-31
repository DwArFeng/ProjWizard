package com.dwarfeng.projwiz.core.control;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import com.dwarfeng.dutil.basic.cna.model.SyncMapModel;
import com.dwarfeng.dutil.basic.cna.model.SyncReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.SyncSetModel;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.model.eum.LoggerStringKey;
import com.dwarfeng.projwiz.core.model.struct.Editor;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.ProjectFilePair;
import com.dwarfeng.projwiz.core.view.eum.DialogMessage;
import com.dwarfeng.projwiz.core.view.eum.DialogOption;
import com.dwarfeng.projwiz.core.view.eum.DialogOptionCombo;
import com.dwarfeng.projwiz.core.view.struct.ConfirmDialogSetting;
import com.dwarfeng.projwiz.core.view.struct.MessageDialogSetting;

final class DeleteFocusFileTask extends ProjWizTask {

	public DeleteFocusFileTask(ProjWizard projWizard) {
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
		AtomicReference<Integer> failedCounter = new AtomicReference<Integer>(0);

		Project focusProject;
		Collection<File> focusFiles;

		focusFileModel.getLock().writeLock().lock();
		focusProjectModel.getLock().writeLock().lock();
		try {
			focusProject = focusProjectModel.get();
			focusFiles = new HashSet<>(focusFileModel);
			focusFiles.remove(focusProject.getFileTree().getRoot());
		} finally {
			focusProjectModel.getLock().writeLock().unlock();
			focusFileModel.getLock().writeLock().unlock();
		}

		if (Objects.isNull(focusProject))
			return;

		if (!focusProject.isRemoveFileSupported(Project.RemovingSituation.BY_DELETE)) {
			projWizard.getToolkit()
					.showMessageDialog(new MessageDialogSetting.Builder().setMessage(label(LabelStringKey.MSGDIA_14))
							.setTitle(label(LabelStringKey.MSGDIA_15))
							.setDialogMessage(DialogMessage.INFORMATION_MESSAGE).build());
			return;
		}

		anchorFileModel.getLock().writeLock().lock();
		focusFileModel.getLock().writeLock().lock();
		try {
			focusFileModel.clear();
			anchorFileModel.clear();
		} finally {
			focusFileModel.getLock().writeLock().unlock();
			anchorFileModel.getLock().writeLock().unlock();
		}

		Map<File, Boolean> removeFlags = new HashMap<>();
		for (File file : focusFiles) {
			branchPut(focusProject, file, removeFlags);
		}

		DialogOption option = projWizard.getToolkit().showConfirmDialog(new ConfirmDialogSetting.Builder()
				.setMessage(formatLabel(LabelStringKey.CONFIRMDIA_1, removeFlags.size()))
				.setTitle(label(LabelStringKey.CONFIRMDIA_2)).setDialogOptionCombo(DialogOptionCombo.YES_NO_OPTION)
				.setDialogMessage(DialogMessage.QUESTION_MESSAGE).build());

		if (option != DialogOption.OK_YES) {
			return;
		}

		projWizard.getToolkit().getEditorModel().getLock().writeLock().lock();
		try {
			SyncMapModel<ProjectFilePair, Editor> editorModel = projWizard.getToolkit().getEditorModel();
			for (File file : removeFlags.keySet()) {
				ProjectFilePair pair = new ProjectFilePair(focusProject, file);
				Editor editor = editorModel.get(pair);

				if (Objects.nonNull(editor)) {
					editor.stop();
					editorModel.remove(pair);
				}
			}
		} finally {
			projWizard.getToolkit().getEditorModel().getLock().writeLock().unlock();
		}

		focusProject.getLock().writeLock().lock();
		try {
			for (File file : focusFiles) {
				branchRemove(focusProject, file, removeFlags, failedCounter);
			}
		} finally {
			focusProject.getLock().writeLock().unlock();
		}

		int failCount = failedCounter.get();
		int succCount = removeFlags.size() - failCount;
		double ratio = succCount / removeFlags.size();

		formatInfo(LoggerStringKey.TASK_DELETEFILE_3, succCount, failCount, ratio);

	}

	private void branchPut(Project focusProject, File file, Map<File, Boolean> removeFlags) {
		if (removeFlags.containsKey(file))
			return;

		if (file.isFolder()) {
			for (File subFile : focusProject.getFileTree().getChilds(file)) {
				branchPut(focusProject, subFile, removeFlags);
			}
		}

		removeFlags.put(file, false);
	}

	private boolean branchRemove(Project focusProject, File file, Map<File, Boolean> removeFlags,
			AtomicReference<Integer> failedCounter) {
		if (removeFlags.containsKey(file)) {
			if (removeFlags.get(file))
				return true;
		} else {
			removeFlags.put(file, false);
		}

		boolean removeAllFlag = true;

		if (file.isFolder()) {
			for (File subFile : focusProject.getFileTree().getChilds(file)) {
				if (!branchRemove(focusProject, subFile, removeFlags, failedCounter)) {
					removeAllFlag = false;
				}
			}
		}

		if (!removeAllFlag) {
			formatInfo(LoggerStringKey.TASK_DELETEFILE_0, focusProject.getFileName(file));
			failedCounter.set(failedCounter.get() + 1);
			return false;
		}

		removeFlags.put(file, true);

		String fileName = focusProject.getFileName(file);
		File removedFile = focusProject.removeFile(file, Project.RemovingSituation.BY_DELETE);

		if (Objects.nonNull(removedFile)) {
			formatInfo(LoggerStringKey.TASK_DELETEFILE_1, fileName);
			return true;
		} else {
			formatInfo(LoggerStringKey.TASK_DELETEFILE_2, focusProject.getFileName(file));
			failedCounter.set(failedCounter.get() + 1);
			return false;
		}
	}

}
