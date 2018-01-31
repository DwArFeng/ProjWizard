package com.dwarfeng.projwiz.core.control;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import com.dwarfeng.dutil.basic.cna.model.SyncListModel;
import com.dwarfeng.dutil.basic.cna.model.SyncMapModel;
import com.dwarfeng.dutil.basic.cna.model.SyncReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.SyncSetModel;
import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.projwiz.core.model.eum.DialogMessage;
import com.dwarfeng.projwiz.core.model.eum.DialogOption;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.model.eum.LoggerStringKey;
import com.dwarfeng.projwiz.core.model.struct.Editor;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.ProjectProcessor;
import com.dwarfeng.projwiz.core.view.gui.ComponentSelectDialog;

final class NewProjectTask extends ProjWizTask {

	public NewProjectTask(ProjWizard projWizard) {
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

		boolean emptyFlag = true;

		for (ProjectProcessor processor : projWizard.getToolkit().getComponentModel().getAll(ProjectProcessor.class)) {
			if (processor.isNewProjectSupported()) {
				emptyFlag = false;
				break;
			}
		}

		if (emptyFlag) {
			projWizard.getToolkit().showMessageDialog(label(LabelStringKey.MSGDIA_1), label(LabelStringKey.MSGDIA_2),
					DialogMessage.INFORMATION_MESSAGE, null);
		}

		AtomicReference<ComponentSelectDialog> dialogRef = new AtomicReference<>();

		SwingUtil.invokeAndWaitInEventQueue(() -> {
			dialogRef.set(new ComponentSelectDialog(projWizard.getToolkit().getGuiManager(),
					projWizard.getToolkit().getLabelI18nHandler(), projWizard.getToolkit().getMainFrame(),
					projWizard.getToolkit().getComponentModel(), component -> {
						if (!(component instanceof ProjectProcessor)) {
							return false;
						}
						return ((ProjectProcessor) component).isNewProjectSupported();
					}));
		});

		projWizard.getToolkit().showExternalWindow(dialogRef.get());
		dialogRef.get().waitDispose();

		if (dialogRef.get().getOption() != DialogOption.OK_YES) {
			return;
		}

		ProjectProcessor processor = dialogRef.get().getCurrentComponent(ProjectProcessor.class);
		if (Objects.isNull(processor)) {
			return;
		}

		Project project = null;

		try {
			project = processor.newProject();
		} catch (ProcessException e) {
			warn(LoggerStringKey.TASK_NEWPROJECT_0);
			formatWarn(LoggerStringKey.TASK_NEWPROJECT_1, processor.getKey(), processor.getClass().toString());
			warn(LoggerStringKey.TASK_NEWPROJECT_2, e);
			return;
		}

		if (Objects.isNull(project)) {
			return;
		}

		// 判断project是否与已经打开的工程重名。
		String projectName = project.getName();
		if (isNameAlreadyExists(projectName)) {
			projWizard.getToolkit().showMessageDialog(formatLabel(LabelStringKey.MSGDIA_32, projectName, projectName),
					label(LabelStringKey.MSGDIA_33), DialogMessage.WARNING_MESSAGE);
			return;
		}

		info(LoggerStringKey.TASK_NEWPROJECT_3);
		formatInfo(LoggerStringKey.TASK_NEWPROJECT_1, processor.getKey(), processor.getClass().toString());
		info(LoggerStringKey.TASK_NEWPROJECT_5);
		formatInfo(LoggerStringKey.TASK_NEWPROJECT_6, project.getRegisterKey(), project.getName(),
				project.getClass().toString());

		anchorFileModel.getLock().writeLock().lock();
		focusEditorModel.getLock().writeLock().lock();
		focusFileModel.getLock().writeLock().lock();
		focusProjectModel.getLock().writeLock().lock();
		holdProjectModel.getLock().writeLock().lock();
		try {
			holdProjectModel.add(project);
			focusProjectModel.set(project);
			anchorFileModel.clear();
			focusFileModel.clear();
			focusEditorModel.put(project, null);
		} finally {
			holdProjectModel.getLock().writeLock().unlock();
			focusProjectModel.getLock().writeLock().unlock();
			focusFileModel.getLock().writeLock().unlock();
			focusEditorModel.getLock().writeLock().unlock();
			anchorFileModel.getLock().writeLock().unlock();
		}

	}

	private boolean isNameAlreadyExists(String name) {
		SyncListModel<Project> holdProjectModel = projWizard.getToolkit().getHoldProjectModel();

		holdProjectModel.getLock().readLock().lock();
		try {
			for (Project project : holdProjectModel) {
				String nameAlreadyExists = project.getName();
				if (Objects.equals(name, nameAlreadyExists)) {
					return true;
				}
			}
		} finally {
			holdProjectModel.getLock().readLock().unlock();
		}
		return false;
	}
}
