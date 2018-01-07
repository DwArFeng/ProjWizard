package com.dwarfeng.projwiz.core.view.gui;

import com.dwarfeng.dutil.develop.i18n.I18nHandler;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.view.struct.GuiManager;

public class MfMenu_05 extends ProjWizMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3047647660664376393L;

	/**
	 * 新实例。
	 */
	public MfMenu_05() {
		this(null, null);
	}

	/**
	 * 新实例。
	 * 
	 * @param obversers
	 *            观察器集合。
	 * @param i18n
	 *            国际化接口。
	 */
	public MfMenu_05(GuiManager guiManager, I18nHandler i18nHandler) {
		super(guiManager, i18nHandler);

		setText(label(LabelStringKey.MFMENU_3_1));
		setMnemonic('T');

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		super.dispose();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void refreshLabels() {
		setText(label(LabelStringKey.MFMENU_5_1));
	}

}
