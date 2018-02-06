package com.dwarfeng.projwiz.core.view.gui;

import java.util.Objects;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import com.dwarfeng.dutil.basic.gui.awt.ImageSize;
import com.dwarfeng.dutil.basic.gui.awt.ImageUtil;
import com.dwarfeng.dutil.basic.gui.swing.JMenuItemAction;
import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.dutil.develop.cfg.ConfigKey;
import com.dwarfeng.dutil.develop.cfg.SyncExconfigModel;
import com.dwarfeng.dutil.develop.cfg.obv.ExconfigAdapter;
import com.dwarfeng.dutil.develop.cfg.obv.ExconfigObverser;
import com.dwarfeng.dutil.develop.i18n.I18nHandler;
import com.dwarfeng.projwiz.core.model.eum.CoreConfigEntry;
import com.dwarfeng.projwiz.core.model.eum.ImageKey;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.view.struct.GuiManager;
import com.dwarfeng.projwiz.core.view.struct.GuiManager.ExecType;

/**
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class MfMenu_06 extends ProjWizMenu {

	private static final long serialVersionUID = 6049707588601509436L;

	private final JMenuItem mi_01;
	private final JMenuItem mi_02;
	private final JMenuItem mi_04;
	private final JMenuItem mi_05;
	private final JSeparator separator_01;

	private SyncExconfigModel coreConfigModel;

	private final ExconfigObverser coreConfigObverser = new ExconfigAdapter() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireCurrentValueChanged(ConfigKey configKey, String oldValue, String newValue, String validValue) {
			SwingUtil.invokeInEventQueue(() -> {
				if (Objects.equals(configKey, CoreConfigEntry.SUPER_SECRET_SETTINGS_ENABLED.getConfigKey())) {
					if ((boolean) CoreConfigEntry.SUPER_SECRET_SETTINGS_ENABLED.getValueParser()
							.parseValue(newValue)) {
						showSuperSecretSettingsMenu();
					} else {
						hideSuperSecretSettingsMenu();
					}
				}
			});
		}

	};

	/**
	 * 新实例。
	 */
	public MfMenu_06() {
		this(null, null, null);
	}

	/**
	 * 新实例。
	 * 
	 * @param obversers
	 *            观察器集合。
	 * @param i18n
	 *            国际化接口。
	 */
	public MfMenu_06(GuiManager guiManager, I18nHandler i18nHandler, SyncExconfigModel coreConfigModel) {
		super(guiManager, i18nHandler);

		setText(label(LabelStringKey.MFMENU_6_1));
		setMnemonic('T');

		// 后台监视器
		mi_01 = add(new JMenuItemAction.Builder().name(label(LabelStringKey.MFMENU_6_2)).mnemonic('B')
				.icon(new ImageIcon(ImageUtil.getInternalImage(ImageKey.BACKGROUND, ImageSize.ICON_SMALL)))
				.listener(e -> {
				}).build());

		// 工程与文件监视器
		mi_02 = add(new JMenuItemAction.Builder().name(label(LabelStringKey.MFMENU_6_3)).mnemonic('P')
				.icon(new ImageIcon(ImageUtil.getInternalImage(ImageKey.PROJECT_AND_FILE, ImageSize.ICON_SMALL)))
				.listener(e -> {
					MfMenu_06.this.guiManager.showProjectAndFileMonitor(ExecType.CONCURRENT);
				}).build());
		// 编辑器监视器
		mi_04 = add(new JMenuItemAction.Builder().name(label(LabelStringKey.MFMENU_6_4)).mnemonic('E')
				.icon(new ImageIcon(ImageUtil.getInternalImage(ImageKey.EDITOR, ImageSize.ICON_SMALL))).listener(e -> {
					MfMenu_06.this.guiManager.showEditorMonitor(ExecType.CONCURRENT);
				}).build());

		separator_01 = new JSeparator();
		add(separator_01);

		// 超级机密设置
		mi_05 = add(new JMenuItemAction.Builder().name(label(LabelStringKey.MFMENU_6_5)).listener(e -> {
			MfMenu_06.this.guiManager.superSecretSettings(ExecType.CONCURRENT);
		}).build());

		if (Objects.nonNull(coreConfigModel)) {
			coreConfigModel.addObverser(coreConfigObverser);
		}

		this.coreConfigModel = coreConfigModel;

		syncCoreConfigModel();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		hideSuperSecretSettingsMenu();

		if (Objects.nonNull(coreConfigModel)) {
			coreConfigModel.removeObverser(coreConfigObverser);
		}

		super.dispose();
	}

	/**
	 * @return the coreConfigModel
	 */
	public SyncExconfigModel getCoreConfigModel() {
		return coreConfigModel;
	}

	/**
	 * @param coreConfigModel
	 *            the coreConfigModel to set
	 */
	public void setCoreConfigModel(SyncExconfigModel coreConfigModel) {
		if (Objects.nonNull(this.coreConfigModel)) {
			this.coreConfigModel.removeObverser(coreConfigObverser);
		}

		if (Objects.nonNull(coreConfigModel)) {
			coreConfigModel.addObverser(coreConfigObverser);
		}

		this.coreConfigModel = coreConfigModel;
		syncCoreConfigModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void refreshLabels() {
		setText(label(LabelStringKey.MFMENU_6_1));
		mi_01.setText(label(LabelStringKey.MFMENU_6_2));
		mi_02.setText(label(LabelStringKey.MFMENU_6_3));
		mi_04.setText(label(LabelStringKey.MFMENU_6_4));
	}

	private void hideSuperSecretSettingsMenu() {
		separator_01.setVisible(false);
		mi_05.setVisible(false);
	}

	private void showSuperSecretSettingsMenu() {
		separator_01.setVisible(true);
		mi_05.setVisible(true);
	}

	private void syncCoreConfigModel() {
		hideSuperSecretSettingsMenu();

		if (Objects.isNull(coreConfigModel)) {
			return;
		}

		coreConfigModel.getLock().readLock().lock();
		try {
			if ((boolean) coreConfigModel
					.getParsedValue(CoreConfigEntry.SUPER_SECRET_SETTINGS_ENABLED.getConfigKey())) {
				showSuperSecretSettingsMenu();
			}
		} finally {
			coreConfigModel.getLock().readLock().unlock();
		}
	}

}
