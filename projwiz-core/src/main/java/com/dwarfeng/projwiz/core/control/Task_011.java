package com.dwarfeng.projwiz.core.control;

import java.util.concurrent.atomic.AtomicReference;

import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.projwiz.core.view.gui.EditorMonitor;
import com.dwarfeng.projwiz.core.view.gui.IndicateMonitor;
import com.dwarfeng.projwiz.core.view.gui.ProjectAndFileMonitor;

final class ShowProjectAndFileMonitorTask extends ProjWizTask {

	public ShowProjectAndFileMonitorTask(ProjWizard projWizard) {
		super(projWizard);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		if (projWizard.getToolkit().containsDialog(ProjectAndFileMonitor.class.toString())) {
			projWizard.getToolkit().showExternalWindow(ProjectAndFileMonitor.class.toString());
		} else {
			AtomicReference<ProjectAndFileMonitor> dialogRef = new AtomicReference<>();
			SwingUtil.invokeAndWaitInEventQueue(() -> {
				dialogRef.set(new ProjectAndFileMonitor(projWizard.getToolkit().getGuiManager(),
						projWizard.getToolkit().getLabelI18nHandler(), projWizard.getToolkit().getAnchorFileModel(),
						projWizard.getToolkit().getFocusProjectModel(), projWizard.getToolkit().getFocusFileModel(),
						projWizard.getToolkit().getHoldProjectModel(),
						projWizard.getToolkit().getProjectProcessorModel(),
						projWizard.getToolkit().getFileProcessorModel()));
			});
			projWizard.getToolkit().showExternalWindow(dialogRef.get());
		}
	}
}

final class ShowIndicateMonitorTask extends ProjWizTask {

	public ShowIndicateMonitorTask(ProjWizard projWizard) {
		super(projWizard);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		if (projWizard.getToolkit().containsDialog(IndicateMonitor.class.toString())) {
			projWizard.getToolkit().showExternalWindow(IndicateMonitor.class.toString());
		} else {
			AtomicReference<IndicateMonitor> dialogRef = new AtomicReference<>();
			SwingUtil.invokeAndWaitInEventQueue(() -> {
				dialogRef.set(new IndicateMonitor(projWizard.getToolkit().getGuiManager(),
						projWizard.getToolkit().getLabelI18nHandler(),
						projWizard.getToolkit().getProjectIndicateModel(),
						projWizard.getToolkit().getFileIndicateModel()));
			});
			projWizard.getToolkit().showExternalWindow(dialogRef.get());
		}
	}
}

final class ShowEditorMonitorTask extends ProjWizTask {

	public ShowEditorMonitorTask(ProjWizard projWizard) {
		super(projWizard);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		if (projWizard.getToolkit().containsDialog(EditorMonitor.class.toString())) {
			projWizard.getToolkit().showExternalWindow(EditorMonitor.class.toString());
		} else {
			AtomicReference<EditorMonitor> dialogRef = new AtomicReference<>();
			SwingUtil.invokeAndWaitInEventQueue(() -> {
				dialogRef.set(new EditorMonitor(projWizard.getToolkit().getGuiManager(),
						projWizard.getToolkit().getLabelI18nHandler(), projWizard.getToolkit().getFocusEditorModel(),
						projWizard.getToolkit().getEditorModel(), projWizard.getToolkit().getProjectProcessorModel(),
						projWizard.getToolkit().getFileProcessorModel()));
			});
			projWizard.getToolkit().showExternalWindow(dialogRef.get());
		}
	}
}
