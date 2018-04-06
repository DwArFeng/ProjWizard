package com.dwarfeng.projwiz.core.control;

import com.dwarfeng.projwiz.core.view.gui.ModuleChooser;

final class SuperSecretSettingsTask extends ProjWizTask {

	public SuperSecretSettingsTask(ProjWizard projWizard) {
		super(projWizard);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		ModuleChooser chooser = new ModuleChooser(projWizard.getToolkit().getGuiManager(),
				projWizard.getToolkit().getLabelI18nHandler(), projWizard.getToolkit().getModuleModel());
		chooser.showDialog(null);
	}
}
