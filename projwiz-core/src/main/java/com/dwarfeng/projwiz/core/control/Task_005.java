package com.dwarfeng.projwiz.core.control;

import java.util.Objects;

import com.dwarfeng.dutil.basic.cna.model.SyncReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.SyncSetModel;
import com.dwarfeng.projwiz.core.model.struct.Editor;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.Project;

final class SetFocusProjectTask extends ProjWizTask {

	private final Project focusProject;

	public SetFocusProjectTask(ProjWizard projWizard, Project focusProject) {
		super(projWizard);

		this.focusProject = focusProject;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		SyncReferenceModel<File> anchorFileModel = projWizard.getToolkit().getAnchorFileModel();
		SyncSetModel<File> focusFileModel = projWizard.getToolkit().getFocusFileModel();
		SyncReferenceModel<Project> focusProjectModel = projWizard.getToolkit().getFocusProjectModel();

		anchorFileModel.getLock().writeLock().lock();
		focusFileModel.getLock().writeLock().lock();
		focusProjectModel.getLock().writeLock().lock();
		try {
			focusProjectModel.set(focusProject);
			focusFileModel.clear();
			anchorFileModel.clear();
		} finally {
			focusProjectModel.getLock().writeLock().unlock();
			focusFileModel.getLock().writeLock().unlock();
			anchorFileModel.getLock().writeLock().unlock();
		}
	}

}

final class AddFocusFileTask extends ProjWizTask {

	private final File focusFile;

	public AddFocusFileTask(ProjWizard projWizard, File focusFile) {
		super(projWizard);

		Objects.requireNonNull(focusFile, "入口参数 focusFile 不能为 null。");
		this.focusFile = focusFile;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		projWizard.getToolkit().getFocusFileModel().add(focusFile);
	}

}

final class RemoveFocusFileTask extends ProjWizTask {

	private final File focusFile;

	public RemoveFocusFileTask(ProjWizard projWizard, File focusFile) {
		super(projWizard);

		Objects.requireNonNull(focusFile, "入口参数 focusFile 不能为 null。");
		this.focusFile = focusFile;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		projWizard.getToolkit().getFocusFileModel().remove(focusFile);
	}

}

final class ClearFocusFileTask extends ProjWizTask {

	public ClearFocusFileTask(ProjWizard projWizard) {
		super(projWizard);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		projWizard.getToolkit().getFocusFileModel().clear();
	}

}

final class SetAnchorFileTask extends ProjWizTask {

	private final File anchorFile;

	public SetAnchorFileTask(ProjWizard projWizard, File anchorFile) {
		super(projWizard);

		this.anchorFile = anchorFile;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		projWizard.getToolkit().getAnchorFileModel().set(anchorFile);
	}

}

final class PutFocusEditorTask extends ProjWizTask {

	private final Project project;
	private final Editor editor;

	public PutFocusEditorTask(ProjWizard projWizard, Project project, Editor editor) {
		super(projWizard);

		Objects.requireNonNull(project, "入口参数 project 不能为 null。");
		Objects.requireNonNull(editor, "入口参数 editor 不能为 null。");

		this.project = project;
		this.editor = editor;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		projWizard.getToolkit().getFocusEditorModel().put(project, editor);
	}
}