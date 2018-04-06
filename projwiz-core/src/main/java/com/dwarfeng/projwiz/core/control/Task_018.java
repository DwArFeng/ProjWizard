package com.dwarfeng.projwiz.core.control;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import com.dwarfeng.dutil.basic.cna.model.DefaultReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.ModelUtil;
import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.SyncReferenceModel;
import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.view.gui.FilePropertiesDialog;
import com.dwarfeng.projwiz.core.view.gui.ProjectPropertiesDialog;

final class ShowAnchorFilePropertiesDialogTask extends ProjWizTask {

	/** 对话框单例引用。 */
	private final static ReferenceModel<FilePropertiesDialog> dialogInstanceRef = ModelUtil
			.syncReferenceModel(new DefaultReferenceModel<>(null));

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

		if (Objects.nonNull(dialogInstanceRef.get())) {
			SwingUtil.invokeInEventQueue(() -> {
				dialogInstanceRef.get().requestFocus();
			});
		} else {
			AtomicReference<FilePropertiesDialog> dialogRef = new AtomicReference<>();
			SwingUtil.invokeAndWaitInEventQueue(() -> {
				FilePropertiesDialog newDialogInstance = new FilePropertiesDialog(
						projWizard.getToolkit().getGuiManager(), projWizard.getToolkit().getLabelI18nHandler(),
						projWizard.getToolkit().getMainFrame(), projWizard.getToolkit().getModuleModel(),
						focusProject, anchorFile);
				dialogRef.set(newDialogInstance);
				newDialogInstance.addWindowListener(new WindowAdapter() {

					@Override
					public void windowClosed(WindowEvent e) {
						dialogInstanceRef.set(null);
					}

				});
				dialogRef.set(newDialogInstance);
				dialogInstanceRef.set(newDialogInstance);
			});
			projWizard.getToolkit().showExternalWindow(dialogRef.get());
		}
	}
}

final class ShowFocusProjectPropertiesDialogTask extends ProjWizTask {

	/** 对话框单例引用。 */
	private final static ReferenceModel<ProjectPropertiesDialog> dialogInstanceRef = ModelUtil
			.syncReferenceModel(new DefaultReferenceModel<>(null));

	public ShowFocusProjectPropertiesDialogTask(ProjWizard projWizard) {
		super(projWizard);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		final Project focusProject = projWizard.getToolkit().getFocusProjectModel().get();

		if (Objects.nonNull(dialogInstanceRef.get())) {
			SwingUtil.invokeInEventQueue(() -> {
				dialogInstanceRef.get().requestFocus();
			});
		} else {
			AtomicReference<ProjectPropertiesDialog> dialogRef = new AtomicReference<>();
			SwingUtil.invokeAndWaitInEventQueue(() -> {
				ProjectPropertiesDialog newDialogInstance = new ProjectPropertiesDialog(
						projWizard.getToolkit().getGuiManager(), projWizard.getToolkit().getLabelI18nHandler(),
						projWizard.getToolkit().getMainFrame(), projWizard.getToolkit().getModuleModel(),
						focusProject);
				dialogRef.set(newDialogInstance);
				newDialogInstance.addWindowListener(new WindowAdapter() {

					@Override
					public void windowClosed(WindowEvent e) {
						dialogInstanceRef.set(null);
					}

				});
				dialogRef.set(newDialogInstance);
				dialogInstanceRef.set(newDialogInstance);
			});
			projWizard.getToolkit().showExternalWindow(dialogRef.get());
		}
	}
}
