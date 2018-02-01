package com.dwarfeng.projwiz.core.control;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import com.dwarfeng.dutil.basic.cna.model.SyncMapModel;
import com.dwarfeng.dutil.basic.cna.model.SyncReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.SyncSetModel;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.projwiz.core.model.cm.SyncComponentModel;
import com.dwarfeng.projwiz.core.model.eum.DialogMessage;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.model.eum.LoggerStringKey;
import com.dwarfeng.projwiz.core.model.struct.Editor;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.FileProcessor;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.ProjectFilePair;
import com.dwarfeng.projwiz.core.view.struct.MessageDialogSetting;

final class OpenFocusFileTask extends ProjWizTask {

	public OpenFocusFileTask(ProjWizard projWizard) {
		super(projWizard);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		SyncMapModel<Project, Editor> focusEditorModel = projWizard.getToolkit().getFocusEditorModel();
		SyncSetModel<File> focusFileModel = projWizard.getToolkit().getFocusFileModel();
		SyncReferenceModel<Project> focusProjectModel = projWizard.getToolkit().getFocusProjectModel();

		final Collection<File> focusFiles;
		final Project focusProject;

		focusFileModel.getLock().readLock().lock();
		focusProjectModel.getLock().readLock().lock();
		try {
			focusProject = focusProjectModel.get();
			focusFiles = new HashSet<>(focusFileModel);
		} finally {
			focusProjectModel.getLock().readLock().unlock();
			focusFileModel.getLock().readLock().unlock();
		}

		SyncMapModel<ProjectFilePair, Editor> editorModel = projWizard.getToolkit().getEditorModel();
		SyncComponentModel componentModel = projWizard.getToolkit().getComponentModel();

		Editor lastEditor = null;

		bk: for (File file : focusFiles) {
			FileProcessor fileProcessor = componentModel.getAll(FileProcessor.class).get(file.getRegisterKey());
			ProjectFilePair pair = new ProjectFilePair(focusProject, file);
			boolean alreadyContainsFlag = editorModel.containsKey(pair);

			if (!fileProcessor.isNewEditorSupported() && !alreadyContainsFlag) {
				continue bk;
			}

			focusEditorModel.getLock().writeLock().lock();
			editorModel.getLock().readLock().lock();
			try {
				if (alreadyContainsFlag) {
					Editor editorAlreadyExists = editorModel.get(pair);
					focusEditorModel.put(focusProject, editorAlreadyExists);
					lastEditor = editorAlreadyExists;
					return;
				}
			} finally {
				editorModel.getLock().readLock().unlock();
				focusEditorModel.getLock().writeLock().unlock();
			}

			Editor editor2Add;
			try {
				editor2Add = fileProcessor.newEditor(focusProject, file);
			} catch (ProcessException e) {
				warn(LoggerStringKey.TASK_OPENFILE_0);
				warn(LoggerStringKey.TASK_OPENFILE_1, e);
				continue bk;
			}
			if (Objects.isNull(editor2Add)) {
				continue bk;
			}

			editorModel.put(pair, editor2Add);
			lastEditor = editor2Add;
		}

		if (Objects.isNull(lastEditor)) {
			projWizard.getToolkit()
					.showMessageDialog(new MessageDialogSetting.Builder().setMessage(label(LabelStringKey.MSGDIA_16))
							.setTitle(label(LabelStringKey.MSGDIA_17))
							.setDialogMessage(DialogMessage.INFORMATION_MESSAGE).build());
			return;
		}

		focusEditorModel.put(focusProject, lastEditor);
	}
}

final class OpenAnchorFileTask extends ProjWizTask {

	public OpenAnchorFileTask(ProjWizard projWizard) {
		super(projWizard);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		SyncReferenceModel<File> anchorFileModel = projWizard.getToolkit().getAnchorFileModel();
		SyncMapModel<Project, Editor> focusEditorModel = projWizard.getToolkit().getFocusEditorModel();
		SyncReferenceModel<Project> focusProjectModel = projWizard.getToolkit().getFocusProjectModel();

		final File anchorFile;
		final Project focusProject;

		anchorFileModel.getLock().readLock().lock();
		focusProjectModel.getLock().readLock().lock();
		try {
			focusProject = focusProjectModel.get();
			anchorFile = anchorFileModel.get();
		} finally {
			focusProjectModel.getLock().readLock().unlock();
			anchorFileModel.getLock().readLock().unlock();
		}

		SyncMapModel<ProjectFilePair, Editor> editorModel = projWizard.getToolkit().getEditorModel();
		SyncComponentModel componentModel = projWizard.getToolkit().getComponentModel();

		FileProcessor fileProcessor = componentModel.getAll(FileProcessor.class).get(anchorFile.getRegisterKey());

		ProjectFilePair pair = new ProjectFilePair(focusProject, anchorFile);
		boolean alreadyContainsFlag = editorModel.containsKey(pair);

		if (!fileProcessor.isNewEditorSupported() && !alreadyContainsFlag) {
			projWizard.getToolkit()
					.showMessageDialog(new MessageDialogSetting.Builder().setMessage(label(LabelStringKey.MSGDIA_16))
							.setTitle(label(LabelStringKey.MSGDIA_17))
							.setDialogMessage(DialogMessage.INFORMATION_MESSAGE).build());
			return;
		}

		if (alreadyContainsFlag) {
			focusEditorModel.getLock().writeLock().lock();
			editorModel.getLock().readLock().lock();
			try {
				focusEditorModel.put(focusProject, editorModel.get(pair));
				return;
			} finally {
				focusEditorModel.getLock().writeLock().unlock();
				editorModel.getLock().readLock().unlock();
			}
		}

		Editor editor = null;
		try {
			editor = fileProcessor.newEditor(focusProject, anchorFile);
		} catch (ProcessException e) {
			warn(LoggerStringKey.TASK_OPENFILE_0);
			warn(LoggerStringKey.TASK_OPENFILE_1, e);
			return;
		}
		if (Objects.isNull(editor)) {
			return;
		}

		editorModel.put(pair, editor);
		focusEditorModel.put(focusProject, editor);
	}

}

final class OpenCertainFileTask extends ProjWizTask {

	private final Project project;
	private final File file;

	public OpenCertainFileTask(ProjWizard projWizard, Project project, File file) {
		super(projWizard);

		Objects.requireNonNull(project, "入口参数 project 不能为 null。");
		Objects.requireNonNull(file, "入口参数 file 不能为 null。");

		this.project = project;
		this.file = file;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		SyncMapModel<Project, Editor> focusEditorModel = projWizard.getToolkit().getFocusEditorModel();

		SyncMapModel<ProjectFilePair, Editor> editorModel = projWizard.getToolkit().getEditorModel();
		SyncComponentModel componentModel = projWizard.getToolkit().getComponentModel();

		FileProcessor fileProcessor = componentModel.getAll(FileProcessor.class).get(file.getRegisterKey());

		ProjectFilePair pair = new ProjectFilePair(project, file);
		boolean alreadyContainsFlag = editorModel.containsKey(pair);

		if (!fileProcessor.isNewEditorSupported() && !alreadyContainsFlag) {
			projWizard.getToolkit()
					.showMessageDialog(new MessageDialogSetting.Builder().setMessage(label(LabelStringKey.MSGDIA_16))
							.setTitle(label(LabelStringKey.MSGDIA_17))
							.setDialogMessage(DialogMessage.INFORMATION_MESSAGE).build());
			return;
		}

		if (alreadyContainsFlag) {
			focusEditorModel.getLock().writeLock().lock();
			editorModel.getLock().readLock().lock();
			try {
				focusEditorModel.put(project, editorModel.get(pair));
				return;
			} finally {
				focusEditorModel.getLock().writeLock().unlock();
				editorModel.getLock().readLock().unlock();
			}
		}

		Editor editor = null;
		try {
			editor = fileProcessor.newEditor(project, file);
		} catch (ProcessException e) {
			warn(LoggerStringKey.TASK_OPENFILE_0);
			warn(LoggerStringKey.TASK_OPENFILE_1, e);
			return;
		}
		if (Objects.isNull(editor)) {
			return;
		}

		editorModel.put(pair, editor);
		focusEditorModel.put(project, editor);
	}
}
