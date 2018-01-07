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
import com.dwarfeng.projwiz.core.model.eum.DialogMessage;
import com.dwarfeng.projwiz.core.model.eum.DialogOption;
import com.dwarfeng.projwiz.core.model.eum.DialogOptionCombo;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.model.eum.LoggerStringKey;
import com.dwarfeng.projwiz.core.model.struct.Editor;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.ProjectFilePair;
import com.dwarfeng.projwiz.core.util.ModelUtil;

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

		if (!focusProject.isRemoveFileByDeleteSupported()) {
			projWizard.getToolkit().showMessageDialog(label(LabelStringKey.MSGDIA_14), label(LabelStringKey.MSGDIA_15),
					DialogMessage.INFORMATION_MESSAGE);
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

		DialogOption option = projWizard.getToolkit().showConfirmDialog(
				formatLabel(LabelStringKey.CONFIRMDIA_1, removeFlags.size()), label(LabelStringKey.CONFIRMDIA_2),
				DialogOptionCombo.YES_NO_OPTION, DialogMessage.QUESTION_MESSAGE);

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
			formatInfo(LoggerStringKey.TASK_DELETEFILE_0, file.getName());
			failedCounter.set(failedCounter.get() + 1);
			return false;
		}

		removeFlags.put(file, true);

		File actualRemovedFile = focusProject.removeFileByDelete(ModelUtil.unmodifiableFile(file));

		if (Objects.nonNull(actualRemovedFile)) {
			formatInfo(LoggerStringKey.TASK_DELETEFILE_1, file.getName());
			projWizard.getToolkit().getFileIndicateModel().remove(file.getUniqueLabel());
			return true;
		} else {
			formatInfo(LoggerStringKey.TASK_DELETEFILE_2, actualRemovedFile.getName());
			failedCounter.set(failedCounter.get() + 1);
			return false;
		}
	}

}
