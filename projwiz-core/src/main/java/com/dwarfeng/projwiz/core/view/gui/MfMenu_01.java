package com.dwarfeng.projwiz.core.view.gui;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Objects;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import com.dwarfeng.dutil.basic.cna.model.SyncReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.obv.ReferenceAdapter;
import com.dwarfeng.dutil.basic.cna.model.obv.ReferenceObverser;
import com.dwarfeng.dutil.basic.gui.awt.ImageSize;
import com.dwarfeng.dutil.basic.gui.awt.ImageUtil;
import com.dwarfeng.dutil.basic.gui.swing.JMenuItemAction;
import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.dutil.develop.i18n.I18nHandler;
import com.dwarfeng.projwiz.core.model.eum.ImageKey;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.view.struct.GuiManager;
import com.dwarfeng.projwiz.core.view.struct.GuiManager.ExecType;

public final class MfMenu_01 extends ProjWizMenu {

	private static final long serialVersionUID = -1329002353676609189L;

	private final JMenuItem mi_01;
	private final JMenuItem mi_02;
	private final JMenuItem mi_03;
	private final JMenuItem mi_04;
	private final JMenuItem mi_05;
	private final JMenuItem mi_06;
	private final JMenuItem mi_07;
	private final JMenuItem mi_08;
	private final JMenuItem mi_09;
	private final JMenuItem mi_10;
	private final JMenuItem mi_11;
	private final JSeparator sp_01;
	private final JSeparator sp_02;
	private final JSeparator sp_03;
	private final JSeparator sp_04;
	private final JSeparator sp_05;

	private SyncReferenceModel<Project> focusProjectModel;

	private final ReferenceObverser<Project> focusProjectObverser = new ReferenceAdapter<Project>() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireCleared() {
			SwingUtil.invokeInEventQueue(() -> {
				disableFocus();
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireSet(Project oldValue, Project newValue) {
			SwingUtil.invokeInEventQueue(() -> {
				if (Objects.nonNull(newValue)) {
					enableFocus();
				} else {
					disableFocus();
				}
			});
		}

	};

	/**
	 * 新实例。
	 */
	public MfMenu_01() {
		this(null, null, null);
	}

	/**
	 * 
	 * @param guiManager
	 * @param i18nHandler
	 * @param openedAndFocusModel
	 */
	public MfMenu_01(GuiManager guiManager, I18nHandler i18nHandler, SyncReferenceModel<Project> focusProjectModel) {
		super(guiManager, i18nHandler);

		setText(label(LabelStringKey.MFMENU_1_1));
		setMnemonic('P');

		// 新建
		mi_01 = add(new JMenuItemAction.Builder().name(label(LabelStringKey.MFMENU_1_2)).mnemonic('N')
				.keyStorke(
						KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK))
				.icon(new ImageIcon(ImageUtil.getInternalImage(ImageKey.NEW_PROJECT, ImageSize.ICON_SMALL)))
				.listener(e -> {
					MfMenu_01.this.guiManager.newProject(ExecType.CONCURRENT);
				}).build());

		// 打开
		mi_02 = add(new JMenuItemAction.Builder().name(label(LabelStringKey.MFMENU_1_3)).mnemonic('O')
				.keyStorke(
						KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK))
				.icon(new ImageIcon(ImageUtil.getInternalImage(ImageKey.OPEN_PROJECT, ImageSize.ICON_SMALL)))
				.listener(e -> {
					MfMenu_01.this.guiManager.openProject(ExecType.CONCURRENT);
				}).build());

		sp_01 = new JSeparator();
		add(sp_01);

		mi_03 = add(new JMenuItemAction.Builder().name(label(LabelStringKey.MFMENU_1_4)).mnemonic('C')
				.keyStorke(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK)).listener(e -> {
					if (Objects.isNull(MfMenu_01.this.focusProjectModel)) {
						return;
					}

					Project project = MfMenu_01.this.focusProjectModel.get();

					if (Objects.nonNull(project))
						MfMenu_01.this.guiManager.tryCloseFocusProject(ExecType.CONCURRENT);
				}).build());
		mi_04 = add(new JMenuItemAction.Builder().name(label(LabelStringKey.MFMENU_1_5)).mnemonic('L')
				.keyStorke(
						KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK))
				.listener(e -> {
					MfMenu_01.this.guiManager.closeAllProject(ExecType.CONCURRENT);
				}).build());

		sp_02 = new JSeparator();
		add(sp_02);

		mi_05 = add(new JMenuItemAction.Builder().name(label(LabelStringKey.MFMENU_1_6)).mnemonic('S')
				.keyStorke(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_DOWN_MASK))
				.icon(new ImageIcon(ImageUtil.getInternalImage(ImageKey.SAVE_PROJECT, ImageSize.ICON_SMALL)))
				.listener(e -> {
					MfMenu_01.this.guiManager.saveFocusProject(ExecType.CONCURRENT);
				}).build());
		mi_06 = add(new JMenuItemAction.Builder().name(label(LabelStringKey.MFMENU_1_7)).mnemonic('A').listener(e -> {
			MfMenu_01.this.guiManager.saveAsFocusProject(ExecType.CONCURRENT);
		}).build());
		mi_07 = add(new JMenuItemAction.Builder().name(label(LabelStringKey.MFMENU_1_8)).mnemonic('V')
				.keyStorke(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK))
				.build());

		sp_03 = new JSeparator();
		add(sp_03);

		mi_08 = add(new JMenuItemAction.Builder().name(label(LabelStringKey.MFMENU_1_9)).mnemonic('I')
				.icon(new ImageIcon(ImageUtil.getInternalImage(ImageKey.INPORT, ImageSize.ICON_SMALL))).build());
		mi_09 = add(new JMenuItemAction.Builder().name(label(LabelStringKey.MFMENU_1_10)).mnemonic('E')
				.icon(new ImageIcon(ImageUtil.getInternalImage(ImageKey.EXPORT, ImageSize.ICON_SMALL))).build());

		sp_04 = new JSeparator();
		add(sp_04);

		mi_10 = add(new JMenuItemAction.Builder().name(label(LabelStringKey.MFMENU_1_11)).mnemonic('R')
				.keyStorke(
						KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.ALT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK))
				.listener(e -> {
					MfMenu_01.this.guiManager.showFocusProjectPropertiesDialog(ExecType.CONCURRENT);
				}).build());

		sp_05 = new JSeparator();
		add(sp_05);

		mi_11 = add(new JMenuItemAction.Builder().name(label(LabelStringKey.MFMENU_1_12)).mnemonic('X').listener(e -> {
			MfMenu_01.this.guiManager.tryExit(ExecType.CONCURRENT);
		}).build());

		if (Objects.nonNull(focusProjectModel)) {
			focusProjectModel.addObverser(focusProjectObverser);
		}

		this.focusProjectModel = focusProjectModel;

		syncFocusProjectModel();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		super.dispose();
		if (Objects.nonNull(this.focusProjectModel)) {
			this.focusProjectModel.removeObverser(focusProjectObverser);
		}
	}

	/**
	 * @return the focusProjectModel
	 */
	public SyncReferenceModel<Project> getFocusProjectModel() {
		return focusProjectModel;
	}

	/**
	 * @param focusProjectModel
	 *            the focusProjectModel to set
	 */
	public void setFocusProjectModel(SyncReferenceModel<Project> focusProjectModel) {
		if (Objects.nonNull(this.focusProjectModel)) {
			this.focusProjectModel.removeObverser(focusProjectObverser);
		}

		if (Objects.nonNull(focusProjectModel)) {
			focusProjectModel.addObverser(focusProjectObverser);
		}

		this.focusProjectModel = focusProjectModel;
		syncFocusProjectModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void refreshLabels() {
		setText(label(LabelStringKey.MFMENU_1_1));
		mi_01.setText(label(LabelStringKey.MFMENU_1_2));
		mi_02.setText(label(LabelStringKey.MFMENU_1_3));
		mi_03.setText(label(LabelStringKey.MFMENU_1_4));
		mi_04.setText(label(LabelStringKey.MFMENU_1_5));
		mi_05.setText(label(LabelStringKey.MFMENU_1_6));
		mi_06.setText(label(LabelStringKey.MFMENU_1_7));
		mi_07.setText(label(LabelStringKey.MFMENU_1_8));
		mi_08.setText(label(LabelStringKey.MFMENU_1_9));
		mi_09.setText(label(LabelStringKey.MFMENU_1_10));
		mi_10.setText(label(LabelStringKey.MFMENU_1_11));
		mi_11.setText(label(LabelStringKey.MFMENU_1_12));
	}

	/**
	 * @return the mi_01
	 */
	final JMenuItem getMi_01() {
		return mi_01;
	}

	/**
	 * @return the mi_02
	 */
	final JMenuItem getMi_02() {
		return mi_02;
	}

	/**
	 * @return the mi_03
	 */
	final JMenuItem getMi_03() {
		return mi_03;
	}

	/**
	 * @return the mi_04
	 */
	final JMenuItem getMi_04() {
		return mi_04;
	}

	/**
	 * @return the mi_05
	 */
	final JMenuItem getMi_05() {
		return mi_05;
	}

	/**
	 * @return the mi_06
	 */
	final JMenuItem getMi_06() {
		return mi_06;
	}

	/**
	 * @return the mi_07
	 */
	final JMenuItem getMi_07() {
		return mi_07;
	}

	/**
	 * @return the mi_08
	 */
	final JMenuItem getMi_08() {
		return mi_08;
	}

	/**
	 * @return the mi_09
	 */
	final JMenuItem getMi_09() {
		return mi_09;
	}

	/**
	 * @return the mi_10
	 */
	final JMenuItem getMi_10() {
		return mi_10;
	}

	/**
	 * @return the mi_11
	 */
	final JMenuItem getMi_11() {
		return mi_11;
	}

	/**
	 * @return the sp_01
	 */
	final JSeparator getSp_01() {
		return sp_01;
	}

	/**
	 * @return the sp_02
	 */
	final JSeparator getSp_02() {
		return sp_02;
	}

	/**
	 * @return the sp_03
	 */
	final JSeparator getSp_03() {
		return sp_03;
	}

	/**
	 * @return the sp_04
	 */
	final JSeparator getSp_04() {
		return sp_04;
	}

	/**
	 * @return the sp_05
	 */
	final JSeparator getSp_05() {
		return sp_05;
	}

	private void disableFocus() {
		mi_03.setEnabled(false);
		mi_04.setEnabled(false);
		mi_05.setEnabled(false);
		mi_06.setEnabled(false);
		mi_07.setEnabled(false);
		mi_08.setEnabled(false);
		mi_09.setEnabled(false);
		mi_10.setEnabled(false);
	}

	private void enableFocus() {
		mi_03.setEnabled(true);
		mi_04.setEnabled(true);
		mi_05.setEnabled(true);
		mi_06.setEnabled(true);
		mi_07.setEnabled(true);
		mi_08.setEnabled(true);
		mi_09.setEnabled(true);
		mi_10.setEnabled(true);
	}

	private void syncFocusProjectModel() {
		disableFocus();

		if (Objects.isNull(focusProjectModel)) {
			return;
		}

		focusProjectModel.getLock().readLock().lock();
		try {
			if (Objects.nonNull(focusProjectModel.get())) {
				enableFocus();
			}
		} finally {
			focusProjectModel.getLock().readLock().unlock();
		}
	}

}
