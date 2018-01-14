package com.dwarfeng.projwiz.core.control;

import com.dwarfeng.dutil.basic.cna.model.obv.SetObverser;
import com.dwarfeng.dutil.develop.backgr.AbstractTask;
import com.dwarfeng.projwiz.core.model.eum.ToolkitLevel;
import com.dwarfeng.projwiz.core.model.struct.FileProcessor;
import com.dwarfeng.projwiz.core.model.struct.LeveledToolkit;
import com.dwarfeng.projwiz.core.model.struct.ProjectProcessor;
import com.dwarfeng.projwiz.core.model.struct.Toolkit.BackgroundType;

final class ProjectProcessorObverserImpl extends ProjWizObverser implements SetObverser<ProjectProcessor> {

	public ProjectProcessorObverserImpl(ProjWizard projWizard) {
		super(projWizard);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fireAdded(ProjectProcessor element) {
		projWizard.getToolkit().submitTask(new AbstractTask() {

			@Override
			protected void todo() throws Exception {
				element.setToolkit(new LeveledToolkit(projWizard.getToolkit(), ToolkitLevel.WRITE_LIMIT));
			}
		}, BackgroundType.CONCURRENT);

	}

	@Override
	public void fireRemoved(ProjectProcessor element) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fireCleared() {
		// TODO Auto-generated method stub

	}

}

final class FileProcessorObverserImpl extends ProjWizObverser implements SetObverser<FileProcessor> {

	public FileProcessorObverserImpl(ProjWizard projWizard) {
		super(projWizard);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fireAdded(FileProcessor element) {
		projWizard.getToolkit().submitTask(new AbstractTask() {

			@Override
			protected void todo() throws Exception {
				element.setToolkit(new LeveledToolkit(projWizard.getToolkit(), ToolkitLevel.WRITE_LIMIT));
			}
		}, BackgroundType.CONCURRENT);

	}

	@Override
	public void fireRemoved(FileProcessor element) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fireCleared() {
		// TODO Auto-generated method stub

	}

}