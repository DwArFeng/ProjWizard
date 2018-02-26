package com.dwarfeng.projwiz.core.control;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.dwarfeng.dutil.basic.cna.model.SyncListModel;
import com.dwarfeng.dutil.basic.cna.model.SyncMapModel;
import com.dwarfeng.dutil.basic.cna.model.SyncReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.SyncSetModel;
import com.dwarfeng.projwiz.core.model.eum.DialogMessage;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.model.eum.LoggerStringKey;
import com.dwarfeng.projwiz.core.model.struct.Editor;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.ProjectFilePair;
import com.dwarfeng.projwiz.core.view.struct.MessageDialogSetting;

final class TryCloseCertainProjectTask extends ProjWizTask {

	private final Project project;

	public TryCloseCertainProjectTask(ProjWizard projWizard, Project project) {
		super(projWizard);

		Objects.requireNonNull(project, "入口参数 project 不能为 null。");
		this.project = project;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		SyncMapModel<ProjectFilePair, Editor> editorModel = projWizard.getToolkit().getEditorModel();
		SyncMapModel<Project, Editor> focusEditorModel = projWizard.getToolkit().getFocusEditorModel();
		SyncReferenceModel<File> anchorFileModel = projWizard.getToolkit().getAnchorFileModel();
		SyncSetModel<File> focusFileModel = projWizard.getToolkit().getFocusFileModel();
		SyncReferenceModel<Project> focusProjectModel = projWizard.getToolkit().getFocusProjectModel();
		SyncListModel<Project> holdProjectModel = projWizard.getToolkit().getHoldProjectModel();

		final Project focusProject;

		focusEditorModel.getLock().readLock().lock();
		try {
			focusProject = projWizard.getToolkit().getFocusProjectModel().get();
		} finally {
			focusEditorModel.getLock().readLock().unlock();
		}

		// 如果该工程是前台工程，那么就按照关闭前台工程那样执行。
		if (Objects.nonNull(focusProject) && Objects.equals(focusProject, project)) {
			tryCloseProject(focusEditorModel, anchorFileModel, focusFileModel, focusProjectModel, holdProjectModel,
					editorModel, focusProject, true);
			return;
		}

		// 先把该工程推送到前台，以便显示所有的编辑器，移除前台工程，随后再将前台工程置为之前的前台工程。
		anchorFileModel.getLock().writeLock().lock();
		focusEditorModel.getLock().writeLock().lock();
		focusFileModel.getLock().writeLock().lock();
		focusProjectModel.getLock().writeLock().lock();
		holdProjectModel.getLock().writeLock().lock();
		try {
			if (!holdProjectModel.contains(project)) {
				holdProjectModel.add(project);
			}
			focusFileModel.clear();
			anchorFileModel.clear();
			focusProjectModel.set(project);
		} finally {
			holdProjectModel.getLock().writeLock().unlock();
			focusProjectModel.getLock().writeLock().unlock();
			focusFileModel.getLock().writeLock().unlock();
			focusEditorModel.getLock().writeLock().unlock();
			anchorFileModel.getLock().writeLock().unlock();
		}

		tryCloseProject(focusEditorModel, anchorFileModel, focusFileModel, focusProjectModel, holdProjectModel,
				editorModel, project, false);
	}

	/**
	 * 
	 * @param focusEditorModel
	 * @param anchorFileModel
	 * @param focusFileModel
	 * @param focusProjectModel
	 * @param holdProjectModel
	 * @param editorModel
	 * @param project
	 * @param aFlag
	 *            当删除的文件是程序的焦点文件时，为 <code>true</code>,否则为 <code>false</code>
	 */
	private void tryCloseProject(SyncMapModel<Project, Editor> focusEditorModel,
			SyncReferenceModel<File> anchorFileModel, SyncSetModel<File> focusFileModel,
			SyncReferenceModel<Project> focusProjectModel, SyncListModel<Project> holdProjectModel,
			SyncMapModel<ProjectFilePair, Editor> editorModel, Project project, boolean aFlag) {
		if (Objects.isNull(project))
			return;

		Set<Map.Entry<ProjectFilePair, Editor>> entry2Remove = new HashSet<>();

		lock.readLock().lock();
		try {
			editorModel.entrySet().forEach(entry -> {
				if (Objects.equals(entry.getKey().getProject(), project)) {
					entry2Remove.add(entry);
				}
			});

		} finally {
			lock.readLock().unlock();
		}

		Editor refuseEdior = null;
		for (Map.Entry<ProjectFilePair, Editor> entry : entry2Remove) {
			Editor editor = entry.getValue();
			Project project2 = entry.getKey().getProject();

			focusEditorModel.put(project2, editor);

			if (!editor.isStopSuggest()) {
				refuseEdior = editor;
				break;
			} else {
				editorModel.remove(entry.getKey());
				Editor nextFocusEditor = null;
				editorModel.getLock().readLock().lock();
				try {
					for (ProjectFilePair pair2 : editorModel.keySet()) {
						if (Objects.equals(pair2.getProject(), project2)) {
							nextFocusEditor = editorModel.get(pair2);
							break;
						}
					}
				} finally {
					editorModel.getLock().readLock().unlock();
				}

				focusEditorModel.getLock().writeLock().lock();
				try {
					focusEditorModel.put(project2, nextFocusEditor);
				} finally {
					focusEditorModel.getLock().writeLock().unlock();
				}
			}
		}

		if (Objects.nonNull(refuseEdior)) {
			projWizard.getToolkit()
					.showMessageDialog(new MessageDialogSetting.Builder().setMessage(label(LabelStringKey.MSGDIA_24))
							.setTitle(label(LabelStringKey.MSGDIA_25))
							.setDialogMessage(DialogMessage.INFORMATION_MESSAGE).build());
			warn(LoggerStringKey.TASK_TRYCLOSEPROJECT_2);
			formatWarn(LoggerStringKey.TASK_TRYCLOSEPROJECT_3, refuseEdior.getTitle(),
					refuseEdior.getEditProject().getName(),
					refuseEdior.getEditProject().getFileName(refuseEdior.getEditFile()));
			return;
		}

		if (!project.isStopSuggest()) {
			info(LoggerStringKey.TASK_TRYCLOSEPROJECT_0);
			formatInfo(LoggerStringKey.TASK_TRYCLOSEPROJECT_1, project.getProcessorClass().getName(), project.getName(),
					project.getClass().toString());
			return;
		}

		project.stop();

		anchorFileModel.getLock().writeLock().lock();
		focusEditorModel.getLock().writeLock().lock();
		focusFileModel.getLock().writeLock().lock();
		focusProjectModel.getLock().writeLock().lock();
		holdProjectModel.getLock().writeLock().lock();
		try {
			if (aFlag) {
				if (!holdProjectModel.contains(project)) {
					throw new IllegalArgumentException("工程焦点模型中没有该模型: " + project.getName());
				}

				if (holdProjectModel.size() == 1) {
					holdProjectModel.remove(project);
					focusProjectModel.clear();
					anchorFileModel.clear();
					focusFileModel.clear();
					focusEditorModel.remove(project);
					return;
				}

				Project newFocus = null;

				if (holdProjectModel.get(0) == project) {
					newFocus = holdProjectModel.get(1);
				} else {
					newFocus = holdProjectModel.get(0);
				}

				focusFileModel.clear();
				anchorFileModel.clear();
				focusProjectModel.set(newFocus);
				holdProjectModel.remove(project);
				focusFileModel.clear();
				focusEditorModel.remove(project);
			} else {
				if (!holdProjectModel.contains(project)) {
					throw new IllegalArgumentException("工程焦点模型中没有该模型: " + project.getName());
				}

				focusFileModel.clear();
				anchorFileModel.clear();
				focusProjectModel.set(project);
				holdProjectModel.remove(project);
				focusFileModel.clear();
				focusEditorModel.remove(project);
			}
		} finally {
			holdProjectModel.getLock().writeLock().unlock();
			focusProjectModel.getLock().writeLock().unlock();
			focusFileModel.getLock().writeLock().unlock();
			focusEditorModel.getLock().writeLock().unlock();
			anchorFileModel.getLock().writeLock().unlock();
		}

	}
}

final class TryCloseAllProjectTask extends ProjWizTask {

	public TryCloseAllProjectTask(ProjWizard projWizard) {
		super(projWizard);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		// TODO 该方法需要完全重写
		SyncReferenceModel<File> anchorFileModel = projWizard.getToolkit().getAnchorFileModel();
		SyncMapModel<Project, Editor> focusEditorModel = projWizard.getToolkit().getFocusEditorModel();
		SyncSetModel<File> focusFileModel = projWizard.getToolkit().getFocusFileModel();
		SyncReferenceModel<Project> focusProjectModel = projWizard.getToolkit().getFocusProjectModel();
		SyncListModel<Project> holdProjectModel = projWizard.getToolkit().getHoldProjectModel();
		SyncMapModel<ProjectFilePair, Editor> editorModel = projWizard.getToolkit().getEditorModel();

		final List<Project> projects2Remove = new ArrayList<>(holdProjectModel);

		for (Project project : projects2Remove) {
			focusFileModel.clear();
			anchorFileModel.clear();
			focusProjectModel.set(project);

			tryStopProject(focusEditorModel, anchorFileModel, focusFileModel, focusProjectModel, holdProjectModel,
					editorModel, project);
		}

	}

	private void tryStopProject(SyncMapModel<Project, Editor> focusEditorModel,
			SyncReferenceModel<File> anchorFileModel, SyncSetModel<File> focusFileModel,
			SyncReferenceModel<Project> focusProjectModel, SyncListModel<Project> holdProjectModel,
			SyncMapModel<ProjectFilePair, Editor> editorModel, Project project) {
		if (Objects.isNull(project))
			return;

		Set<Map.Entry<ProjectFilePair, Editor>> entry2Remove = new HashSet<>();

		lock.readLock().lock();
		try {
			editorModel.entrySet().forEach(entry -> {
				if (Objects.equals(entry.getKey().getProject(), project)) {
					entry2Remove.add(entry);
				}
			});

		} finally {
			lock.readLock().unlock();
		}

		Editor refuseEdior = null;
		for (Map.Entry<ProjectFilePair, Editor> entry : entry2Remove) {
			Editor editor = entry.getValue();
			Project project2 = entry.getKey().getProject();

			focusEditorModel.put(project2, editor);

			if (!editor.isStopSuggest()) {
				refuseEdior = editor;
				break;
			} else {
				editorModel.remove(entry.getKey());
				Editor nextFocusEditor = null;
				editorModel.getLock().readLock().lock();
				try {
					for (ProjectFilePair pair2 : editorModel.keySet()) {
						if (Objects.equals(pair2.getProject(), project2)) {
							nextFocusEditor = editorModel.get(pair2);
							break;
						}
					}
				} finally {
					editorModel.getLock().readLock().unlock();
				}

				focusEditorModel.getLock().writeLock().lock();
				try {
					focusEditorModel.put(project2, nextFocusEditor);
				} finally {
					focusEditorModel.getLock().writeLock().unlock();
				}
			}
		}

		if (Objects.nonNull(refuseEdior)) {
			projWizard.getToolkit()
					.showMessageDialog(new MessageDialogSetting.Builder().setMessage(label(LabelStringKey.MSGDIA_24))
							.setTitle(label(LabelStringKey.MSGDIA_25))
							.setDialogMessage(DialogMessage.INFORMATION_MESSAGE).build());
			warn(LoggerStringKey.TASK_TRYCLOSEPROJECT_2);
			formatWarn(LoggerStringKey.TASK_TRYCLOSEPROJECT_3, refuseEdior.getTitle(),
					refuseEdior.getEditProject().getName(),
					refuseEdior.getEditProject().getFileName(refuseEdior.getEditFile()));
			return;
		}

		if (!project.isStopSuggest()) {
			info(LoggerStringKey.TASK_TRYCLOSEPROJECT_0);
			formatInfo(LoggerStringKey.TASK_TRYCLOSEPROJECT_1, project.getProcessorClass().getName(), project.getName(),
					project.getClass().toString());
			return;
		}

		project.stop();

		anchorFileModel.getLock().writeLock().lock();
		focusEditorModel.getLock().writeLock().lock();
		focusFileModel.getLock().writeLock().lock();
		focusProjectModel.getLock().writeLock().lock();
		holdProjectModel.getLock().writeLock().lock();
		try {
			if (!holdProjectModel.contains(project)) {
				throw new IllegalArgumentException("工程焦点模型中没有该模型: " + project.getName());
			}

			if (holdProjectModel.size() == 1) {
				holdProjectModel.remove(project);
				focusProjectModel.clear();
				anchorFileModel.clear();
				focusFileModel.clear();
				focusEditorModel.remove(project);

				return;
			}

			Project newFocus = null;

			if (holdProjectModel.get(0) == project) {
				newFocus = holdProjectModel.get(1);
			} else {
				newFocus = holdProjectModel.get(0);
			}

			focusFileModel.clear();
			anchorFileModel.clear();
			focusProjectModel.set(newFocus);
			holdProjectModel.remove(project);
			focusEditorModel.remove(project);
		} finally {
			holdProjectModel.getLock().writeLock().unlock();
			focusProjectModel.getLock().writeLock().unlock();
			focusFileModel.getLock().writeLock().unlock();
			focusEditorModel.getLock().writeLock().unlock();
			anchorFileModel.getLock().writeLock().unlock();
		}
	}
}

final class TryCloseFocusProjectTask extends ProjWizTask {

	public TryCloseFocusProjectTask(ProjWizard projWizard) {
		super(projWizard);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		SyncReferenceModel<File> anchorFileModel = projWizard.getToolkit().getAnchorFileModel();
		SyncMapModel<Project, Editor> focusEditorModel = projWizard.getToolkit().getFocusEditorModel();
		SyncSetModel<File> focusFileModel = projWizard.getToolkit().getFocusFileModel();
		SyncReferenceModel<Project> focusProjectModel = projWizard.getToolkit().getFocusProjectModel();
		SyncListModel<Project> holdProjectModel = projWizard.getToolkit().getHoldProjectModel();
		SyncMapModel<ProjectFilePair, Editor> editorModel = projWizard.getToolkit().getEditorModel();

		final Project focusProject = focusProjectModel.get();

		if (Objects.isNull(focusProject))
			return;

		Set<Map.Entry<ProjectFilePair, Editor>> entry2Remove = new HashSet<>();

		editorModel.getLock().readLock().lock();
		try {
			editorModel.entrySet().forEach(entry -> {
				if (Objects.equals(entry.getKey().getProject(), focusProject)) {
					entry2Remove.add(entry);
				}
			});

		} finally {
			editorModel.getLock().readLock().unlock();
		}

		Editor refuseEdior = null;
		for (Map.Entry<ProjectFilePair, Editor> entry : entry2Remove) {
			Editor editor = entry.getValue();
			Project project = entry.getKey().getProject();

			focusEditorModel.put(project, editor);

			if (!editor.isStopSuggest()) {
				refuseEdior = editor;
				break;
			} else {
				editorModel.remove(entry.getKey());
				Editor nextFocusEditor = null;
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

				focusEditorModel.put(project, nextFocusEditor);
			}
		}

		if (Objects.nonNull(refuseEdior)) {
			projWizard.getToolkit()
					.showMessageDialog(new MessageDialogSetting.Builder().setMessage(label(LabelStringKey.MSGDIA_24))
							.setTitle(label(LabelStringKey.MSGDIA_25))
							.setDialogMessage(DialogMessage.INFORMATION_MESSAGE).build());
			warn(LoggerStringKey.TASK_TRYCLOSEPROJECT_2);
			formatWarn(LoggerStringKey.TASK_TRYCLOSEPROJECT_3, refuseEdior.getTitle(),
					refuseEdior.getEditProject().getName(),
					refuseEdior.getEditProject().getFileName(refuseEdior.getEditFile()));
			return;
		}

		if (!focusProject.isStopSuggest()) {
			info(LoggerStringKey.TASK_TRYCLOSEPROJECT_0);
			formatInfo(LoggerStringKey.TASK_TRYCLOSEPROJECT_1, focusProject.getProcessorClass().getName(),
					focusProject.getName(), focusProject.getClass().toString());
			return;
		}

		focusProject.stop();

		anchorFileModel.getLock().writeLock().lock();
		focusEditorModel.getLock().writeLock().lock();
		focusFileModel.getLock().writeLock().lock();
		focusProjectModel.getLock().writeLock().lock();
		holdProjectModel.getLock().writeLock().lock();
		try {
			if (!holdProjectModel.contains(focusProject)) {
				throw new IllegalArgumentException("工程焦点模型中没有该模型: " + focusProject.getName());
			}

			if (holdProjectModel.size() == 1) {
				holdProjectModel.remove(focusProject);
				focusProjectModel.clear();
				anchorFileModel.clear();
				focusFileModel.clear();
				focusEditorModel.remove(focusProject);

				return;
			}

			Project newFocus = null;

			if (holdProjectModel.get(0) == focusProject) {
				newFocus = holdProjectModel.get(1);
			} else {
				newFocus = holdProjectModel.get(0);
			}

			focusFileModel.clear();
			anchorFileModel.clear();
			focusProjectModel.set(newFocus);
			holdProjectModel.remove(focusProject);
			focusEditorModel.remove(focusProject);
		} finally {
			holdProjectModel.getLock().writeLock().unlock();
			focusProjectModel.getLock().writeLock().unlock();
			focusFileModel.getLock().writeLock().unlock();
			focusEditorModel.getLock().writeLock().unlock();
			anchorFileModel.getLock().writeLock().unlock();
		}
	}
}