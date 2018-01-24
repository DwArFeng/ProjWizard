package com.dwarfeng.projwiz.core.control;

import java.util.concurrent.atomic.AtomicReference;

import com.dwarfeng.dutil.basic.cna.model.SyncReferenceModel;
import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.view.gui.FilePropertiesDialog;
import com.dwarfeng.projwiz.core.view.gui.ProjectPropertiesDialog;

final class ShowAnchorFilePropertiesDialogTask extends ProjWizTask {

	public ShowAnchorFilePropertiesDialogTask(ProjWizard projWizard) {
		super(projWizard);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		SyncReferenceModel<File> anchorFileModel = projWizard.getToolkit().getAnchorFileModel();
		SyncReferenceModel<Project> focusProjectModel = projWizard.getToolkit().getFocusProjectModel();

		final Project focusProject;
		final File anchorFile;

		anchorFileModel.getLock().readLock().lock();
		focusProjectModel.getLock().readLock().lock();
		try {
			focusProject = focusProjectModel.get();
			anchorFile = anchorFileModel.get();
		} finally {
			focusProjectModel.getLock().readLock().unlock();
			anchorFileModel.getLock().readLock().unlock();
		}

		AtomicReference<FilePropertiesDialog> dialogRef = new AtomicReference<>();

		if (projWizard.getToolkit().containsDialog(FilePropertiesDialog.class.toString())) {
			dialogRef.set((FilePropertiesDialog) projWizard.getToolkit().getExternalWindowModel()
					.get(FilePropertiesDialog.class.toString()));
			projWizard.getToolkit().showExternalWindow(FilePropertiesDialog.class.toString());
		} else {
			SwingUtil.invokeAndWaitInEventQueue(() -> {
				dialogRef.set(new FilePropertiesDialog(projWizard.getToolkit().getGuiManager(),
						projWizard.getToolkit().getLabelI18nHandler(), projWizard.getToolkit().getMainFrame(),
						projWizard.getToolkit().getComponentModel(), focusProject, anchorFile));
			});
			projWizard.getToolkit().showExternalWindow(dialogRef.get());

		}

		dialogRef.get().waitDispose();
	}
}

final class ShowFocusProjectPropertiesDialogTask extends ProjWizTask {

	public ShowFocusProjectPropertiesDialogTask(ProjWizard projWizard) {
		super(projWizard);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		final Project focusProject = projWizard.getToolkit().getFocusProjectModel().get();

		AtomicReference<ProjectPropertiesDialog> dialogRef = new AtomicReference<>();

		if (projWizard.getToolkit().containsDialog(FilePropertiesDialog.class.toString())) {
			dialogRef.set((ProjectPropertiesDialog) projWizard.getToolkit().getExternalWindowModel()
					.get(FilePropertiesDialog.class.toString()));
			projWizard.getToolkit().showExternalWindow(FilePropertiesDialog.class.toString());
		} else {
			SwingUtil.invokeAndWaitInEventQueue(() -> {
				dialogRef.set(new ProjectPropertiesDialog(projWizard.getToolkit().getGuiManager(),
						projWizard.getToolkit().getLabelI18nHandler(), projWizard.getToolkit().getMainFrame(),
						projWizard.getToolkit().getComponentModel(), focusProject));
			});
			projWizard.getToolkit().showExternalWindow(dialogRef.get());

		}

		dialogRef.get().waitDispose();
	}
}
