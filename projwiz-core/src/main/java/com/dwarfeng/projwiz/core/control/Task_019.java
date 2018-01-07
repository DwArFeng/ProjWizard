package com.dwarfeng.projwiz.core.control;

import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.util.FileUtil;
import com.dwarfeng.projwiz.core.view.gui.ProjectFileChooser;
import com.dwarfeng.projwiz.core.view.gui.ProjectFileChooser.ReturnOption;

final class SuperSecretSettingsTask extends ProjWizTask {

	public SuperSecretSettingsTask(ProjWizard projWizard) {
		super(projWizard);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void todo() throws Exception {
		ProjectFileChooser projectFileChooser = new ProjectFileChooser(projWizard.getToolkit().getGuiManager(),
				projWizard.getToolkit().getLabelI18nHandler(), projWizard.getToolkit().getHoldProjectModel(),
				projWizard.getToolkit().getFileProcessorModel(), projWizard.getToolkit().getProjectProcessorModel());

		projectFileChooser.setMultiSelectionEnabled(true);

		ReturnOption returnOption = projectFileChooser.showOpenDialog(projWizard.getToolkit().getMainFrame());

		projWizard.getToolkit().info("工程文件选择器返回的类型是 : " + returnOption.toString());
		projWizard.getToolkit().info(String.format("一共选择了 %d 个文件", projectFileChooser.getSelectedFiles().length));
		projWizard.getToolkit().info("选择的文件是 : ");
		File[] files = projectFileChooser.getSelectedFiles();
		for (File file : files) {
			projWizard.getToolkit().info(FileUtil.getStdPath(projWizard.getToolkit().getHoldProjectModel(), file));
		}
	}
}
