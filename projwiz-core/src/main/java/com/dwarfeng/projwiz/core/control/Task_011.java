package com.dwarfeng.projwiz.core.control;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import com.dwarfeng.dutil.basic.cna.model.DefaultReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.ModelUtil;
import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.projwiz.core.view.gui.EditorMonitor;
import com.dwarfeng.projwiz.core.view.gui.ProjectAndFileMonitor;

final class ShowProjectAndFileMonitorTask extends ProjWizTask {

	/** 对话框单例引用。 */
	private final static ReferenceModel<ProjectAndFileMonitor> dialogInstanceRef = ModelUtil
			.syncReferenceModel(new DefaultReferenceModel<>(null));

	public ShowProjectAndFileMonitorTask(ProjWizard projWizard) {
		super(projWizard);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		if (Objects.nonNull(dialogInstanceRef.get())) {
			SwingUtil.invokeInEventQueue(() -> {
				dialogInstanceRef.get().requestFocus();
			});
		} else {
			AtomicReference<ProjectAndFileMonitor> dialogRef = new AtomicReference<>();
			SwingUtil.invokeAndWaitInEventQueue(() -> {
				ProjectAndFileMonitor newDialogInstance = new ProjectAndFileMonitor(
						projWizard.getToolkit().getGuiManager(), projWizard.getToolkit().getLabelI18nHandler(),
						projWizard.getToolkit().getAnchorFileModel(), projWizard.getToolkit().getFocusProjectModel(),
						projWizard.getToolkit().getFocusFileModel(), projWizard.getToolkit().getHoldProjectModel(),
						projWizard.getToolkit().getModuleModel());
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

final class ShowEditorMonitorTask extends ProjWizTask {

	/** 对话框单例引用。 */
	private final static ReferenceModel<EditorMonitor> dialogInstanceRef = ModelUtil
			.syncReferenceModel(new DefaultReferenceModel<>(null));

	public ShowEditorMonitorTask(ProjWizard projWizard) {
		super(projWizard);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		if (Objects.nonNull(dialogInstanceRef.get())) {
			SwingUtil.invokeInEventQueue(() -> {
				dialogInstanceRef.get().requestFocus();
			});
		} else {
			AtomicReference<EditorMonitor> dialogRef = new AtomicReference<>();
			SwingUtil.invokeAndWaitInEventQueue(() -> {
				EditorMonitor newDialogInstance = new EditorMonitor(projWizard.getToolkit().getGuiManager(),
						projWizard.getToolkit().getLabelI18nHandler(), projWizard.getToolkit().getFocusEditorModel(),
						projWizard.getToolkit().getEditorModel(), projWizard.getToolkit().getModuleModel());
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
