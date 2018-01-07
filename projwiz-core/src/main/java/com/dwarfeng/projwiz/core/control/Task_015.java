package com.dwarfeng.projwiz.core.control;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.dwarfeng.dutil.basic.cna.model.SyncMapModel;
import com.dwarfeng.dutil.basic.cna.model.SyncReferenceModel;
import com.dwarfeng.projwiz.core.model.eum.LoggerStringKey;
import com.dwarfeng.projwiz.core.model.struct.Editor;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.ProjectFilePair;

final class TryStopCertainEditorTask extends ProjWizTask {

	private final Editor editor;

	public TryStopCertainEditorTask(ProjWizard projWizard, Editor editor) {
		super(projWizard);

		Objects.requireNonNull(editor, "入口参数 editor 不能为 null。");
		this.editor = editor;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		if (!editor.isStopSuggest()) {
			info(LoggerStringKey.TASK_TRYSTOPEDITOR_0);
			formatInfo(LoggerStringKey.TASK_TRYSTOPEDITOR_1, editor.getTitle(), editor.getEditProject().getName(),
					editor.getEditFile().getName());
			return;
		}

		editor.stop();

		ProjectFilePair pair = new ProjectFilePair(editor.getEditProject(), editor.getEditFile());
		projWizard.getToolkit().getEditorModel().remove(pair);

		Project project = editor.getEditProject();

		Editor nextFocusEditor = null;

		SyncMapModel<ProjectFilePair, Editor> editorModel = projWizard.getToolkit().getEditorModel();
		editorModel.getLock().readLock().lock();
		try {
			for (ProjectFilePair pair2 : editorModel.keySet()) {
				if (Objects.equals(pair2.getProject(), project)) {
					nextFocusEditor = editorModel.get(pair2);
					break;
				}
			}
		} finally {
			editorModel.getLock().readLock().unlock();
		}

		projWizard.getToolkit().getFocusEditorModel().put(project, nextFocusEditor);

	}
}

final class TryStopFocusProjectEditorTask extends ProjWizTask {

	public TryStopFocusProjectEditorTask(ProjWizard projWizard) {
		super(projWizard);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		SyncMapModel<ProjectFilePair, Editor> editorModel = projWizard.getToolkit().getEditorModel();

		final Project focusProject = projWizard.getToolkit().getFocusProjectModel().get();

		final Set<ProjectFilePair> pair2Remove = new HashSet<>();

		projWizard.getToolkit().getEditorModel().getLock().readLock().lock();
		try {
			editorModel.keySet().forEach(pair -> {
				if (Objects.equals(pair.getProject(), focusProject)) {
					pair2Remove.add(pair);
				}
			});
		} finally {
			projWizard.getToolkit().getEditorModel().getLock().readLock().unlock();
		}

		projWizard.getToolkit().getEditorModel().getLock().writeLock().lock();
		try {
			pair2Remove.forEach(pair -> {
				Editor editor = editorModel.get(pair);

				if (!editor.isStopSuggest()) {
					info(LoggerStringKey.TASK_TRYSTOPEDITOR_0);
					formatInfo(LoggerStringKey.TASK_TRYSTOPEDITOR_1, editor.getTitle(),
							editor.getEditProject().getName(), editor.getEditFile().getName());
					return;
				}

				editor.stop();

				projWizard.getToolkit().getEditorModel().remove(pair);
			});
		} finally {
			projWizard.getToolkit().getEditorModel().getLock().writeLock().unlock();
		}

		Editor nextFocusEditor = null;

		editorModel.getLock().readLock().lock();
		try {
			for (ProjectFilePair pair2 : editorModel.keySet()) {
				if (Objects.equals(pair2.getProject(), focusProject)) {
					nextFocusEditor = editorModel.get(pair2);
					break;
				}
			}
		} finally {
			editorModel.getLock().readLock().unlock();
		}

		projWizard.getToolkit().getFocusEditorModel().put(focusProject, nextFocusEditor);

	}
}

final class TryStopFocusEditorTask extends ProjWizTask {

	public TryStopFocusEditorTask(ProjWizard projWizard) {
		super(projWizard);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		SyncMapModel<Project, Editor> focusEditorModel = projWizard.getToolkit().getFocusEditorModel();
		SyncReferenceModel<Project> focusProjectModel = projWizard.getToolkit().getFocusProjectModel();
		SyncMapModel<ProjectFilePair, Editor> editorModel = projWizard.getToolkit().getEditorModel();

		final Project focusProject;
		final Editor focusEditor;

		focusProjectModel.getLock().readLock().lock();
		focusEditorModel.getLock().readLock().lock();
		try {
			focusProject = focusProjectModel.get();
			focusEditor = focusEditorModel.get(focusProject);
		} finally {
			focusEditorModel.getLock().readLock().unlock();
			focusProjectModel.getLock().readLock().unlock();
		}

		if (!focusEditor.isStopSuggest()) {
			info(LoggerStringKey.TASK_TRYSTOPEDITOR_0);
			formatInfo(LoggerStringKey.TASK_TRYSTOPEDITOR_1, focusEditor.getTitle(),
					focusEditor.getEditProject().getName(), focusEditor.getEditFile().getName());
			return;
		}

		focusEditor.stop();

		ProjectFilePair pair = new ProjectFilePair(focusEditor.getEditProject(), focusEditor.getEditFile());
		projWizard.getToolkit().getEditorModel().remove(pair);

		Editor nextFocusEditor = null;

		editorModel.getLock().readLock().lock();
		try {
			for (ProjectFilePair pair2 : editorModel.keySet()) {
				if (Objects.equals(pair2.getProject(), focusProject)) {
					nextFocusEditor = editorModel.get(pair2);
					break;
				}
			}
		} finally {
			editorModel.getLock().readLock().unlock();
		}

		focusEditorModel.put(focusProject, nextFocusEditor);

	}
}
