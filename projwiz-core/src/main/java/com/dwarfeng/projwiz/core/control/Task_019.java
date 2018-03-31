package com.dwarfeng.projwiz.core.control;

import com.dwarfeng.projwiz.core.view.gui.ComponentChooser;

final class SuperSecretSettingsTask extends ProjWizTask {

	public SuperSecretSettingsTask(ProjWizard projWizard) {
		super(projWizard);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		ComponentChooser chooser = new ComponentChooser(projWizard.getToolkit().getGuiManager(),
				projWizard.getToolkit().getLabelI18nHandler(), projWizard.getToolkit().getComponentModel());
		chooser.showDialog(null);
	}
}
