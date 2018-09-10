package com.dwarfeng.projwiz.core.view.gui;

import com.dwarfeng.dutil.basic.cna.model.SyncMapModel;
import com.dwarfeng.dutil.basic.cna.model.SyncReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.SyncSetModel;
import com.dwarfeng.dutil.develop.i18n.I18nHandler;
import com.dwarfeng.dutil.develop.setting.SyncSettingHandler;
import com.dwarfeng.projwiz.core.model.struct.Editor;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.ProjectFilePair;
import com.dwarfeng.projwiz.core.view.struct.GuiManager;
import com.dwarfeng.projwiz.core.view.struct.MainFrameVisibleModel;

/**
 * 主程序的菜单栏。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public final class MfMenuBar_01 extends ProjWizMenuBar {

	private static final long serialVersionUID = 8000675370807395012L;

	private final MfMenu_01 menu_01;
	private final MfMenu_02 menu_02;
	private final MfMenu_03 menu_03;
	private final MfMenu_04 menu_04;
	private final MfMenu_05 menu_05;
	private final MfMenu_06 menu_06;

	private SyncReferenceModel<File> anchorFileModel;
	private SyncReferenceModel<Project> focusProjectModel;
	private SyncSetModel<File> focusFileModel;
	private SyncMapModel<Project, Editor> focusEditorModel;
	private SyncSettingHandler coreSettingHandler;

	/**
	 * 新实例。
	 */
	public MfMenuBar_01() {
		this(null, null, null, null, null, null, null, null, null);
	}

	/**
	 * 
	 * @param guiManager
	 * @param i18nHandler
	 * @param anchorFileModel
	 * @param focusProjectModel
	 * @param focusFileModel
	 * @param focusEditorModel
	 * @param editorModel
	 * @param coreSettingHandler
	 * @param mainFrameVisibleModel
	 */
	public MfMenuBar_01(GuiManager guiManager, I18nHandler i18nHandler, SyncReferenceModel<File> anchorFileModel,
			SyncReferenceModel<Project> focusProjectModel, SyncSetModel<File> focusFileModel,
			SyncMapModel<Project, Editor> focusEditorModel, SyncMapModel<ProjectFilePair, Editor> editorModel,
			SyncSettingHandler coreSettingHandler, MainFrameVisibleModel mainFrameVisibleModel) {
		super(guiManager, i18nHandler);

		menu_01 = new MfMenu_01(guiManager, i18nHandler, focusProjectModel);
		add(menu_01);

		menu_02 = new MfMenu_02(guiManager, i18nHandler, anchorFileModel, focusProjectModel, focusFileModel,
				focusEditorModel, editorModel);
		add(menu_02);

		menu_03 = new MfMenu_03(guiManager, i18nHandler);
		add(menu_03);

		menu_04 = new MfMenu_04(guiManager, i18nHandler, mainFrameVisibleModel);
		add(menu_04);

		menu_05 = new MfMenu_05(guiManager, i18nHandler);
		add(menu_05);

		menu_06 = new MfMenu_06(guiManager, i18nHandler, coreSettingHandler);
		add(menu_06);

		this.anchorFileModel = anchorFileModel;
		this.focusProjectModel = focusProjectModel;
		this.focusFileModel = focusFileModel;
		this.focusEditorModel = focusEditorModel;
		this.coreSettingHandler = coreSettingHandler;

	}

	/**
	 * @return the anchorFileModel
	 */
	public SyncReferenceModel<File> getAnchorFileModel() {
		return anchorFileModel;
	}

	/**
	 * @return the coreSettingHandler
	 */
	public SyncSettingHandler getCoreSettingHandler() {
		return coreSettingHandler;
	}

	/**
	 * @return the focusEditorModel
	 */
	public SyncMapModel<Project, Editor> getFocusEditorModel() {
		return focusEditorModel;
	}

	/**
	 * @return the focusFileModel
	 */
	public SyncSetModel<File> getFocusFileModel() {
		return focusFileModel;
	}

	/**
	 * @return the focusProjectModel
	 */
	public SyncReferenceModel<Project> getFocusProjectModel() {
		return focusProjectModel;
	}

	/**
	 * @param anchorFileModel
	 *            the anchorFileModel to set
	 */
	public void setAnchorFileModel(SyncReferenceModel<File> anchorFileModel) {
		this.anchorFileModel = anchorFileModel;
		this.menu_02.setAnchorFileModel(anchorFileModel);
	}

	/**
	 * @param coreSettingHandler
	 *            the coreSettingHandler to set
	 */
	public void setCoreSettingHandler(SyncSettingHandler coreSettingHandler) {
		this.coreSettingHandler = coreSettingHandler;
		this.menu_06.setCoreSettingHandler(coreSettingHandler);
	}

	/**
	 * @param focusEditorModel
	 *            the focusEditorModel to set
	 */
	public void setFocusEditorModel(SyncMapModel<Project, Editor> focusEditorModel) {
		this.focusEditorModel = focusEditorModel;
		this.menu_02.setFocusEditorModel(focusEditorModel);
	}

	/**
	 * @param focusFileModel
	 *            the focusFileModel to set
	 */
	public void setFocusFileModel(SyncSetModel<File> focusFileModel) {
		this.focusFileModel = focusFileModel;
		this.menu_02.setFocusFileModel(focusFileModel);
	}

	/**
	 * @param focusProjectModel
	 *            the focusProjectModel to set
	 */
	public void setFocusProjectModel(SyncReferenceModel<Project> focusProjectModel) {
		this.focusProjectModel = focusProjectModel;
		this.menu_01.setFocusProjectModel(focusProjectModel);
		this.menu_02.setFocusProjectModel(focusProjectModel);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void refreshLabels() {
		// TODO Auto-generated method stub

	}

}
