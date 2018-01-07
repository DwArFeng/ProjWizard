package com.dwarfeng.projwiz.core.view.gui;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Objects;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import com.dwarfeng.dutil.basic.cna.model.SyncMapModel;
import com.dwarfeng.dutil.basic.cna.model.SyncReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.SyncSetModel;
import com.dwarfeng.dutil.basic.cna.model.obv.MapAdapter;
import com.dwarfeng.dutil.basic.cna.model.obv.MapObverser;
import com.dwarfeng.dutil.basic.cna.model.obv.ReferenceAdapter;
import com.dwarfeng.dutil.basic.cna.model.obv.ReferenceObverser;
import com.dwarfeng.dutil.basic.cna.model.obv.SetAdapter;
import com.dwarfeng.dutil.basic.cna.model.obv.SetObverser;
import com.dwarfeng.dutil.basic.gui.awt.CommonIconLib;
import com.dwarfeng.dutil.basic.gui.awt.ImageSize;
import com.dwarfeng.dutil.basic.gui.awt.ImageUtil;
import com.dwarfeng.dutil.basic.gui.swing.JMenuItemAction;
import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.dutil.develop.i18n.I18nHandler;
import com.dwarfeng.projwiz.core.model.eum.ImageKey;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.model.struct.Editor;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.ProjectFilePair;
import com.dwarfeng.projwiz.core.view.struct.GuiManager;
import com.dwarfeng.projwiz.core.view.struct.GuiManager.ExecType;

class MfMenu_02 extends ProjWizMenu {

	private static final long serialVersionUID = 4409021622806521231L;

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
	private final JMenuItem mi_12;
	private final JMenuItem mi_13;
	private final JSeparator sp_01;
	private final JSeparator sp_02;
	private final JSeparator sp_03;
	private final JSeparator sp_04;

	private SyncReferenceModel<File> anchorFileModel;
	private SyncReferenceModel<Project> focusProjectModel;
	private SyncSetModel<File> focusFileModel;
	private SyncMapModel<Project, Editor> focusEditorModel;
	private SyncMapModel<ProjectFilePair, Editor> editorModel;

	private final ReferenceObverser<File> anchorFileObverser = new ReferenceAdapter<File>() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireCleared() {
			SwingUtil.invokeInEventQueue(() -> {
				disableAnchorFile();
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireSet(File oldValue, File newValue) {
			SwingUtil.invokeInEventQueue(() -> {
				if (Objects.nonNull(newValue)) {
					enableAnchorFile();
				} else {
					disableAnchorFile();
				}

			});
		}

	};

	private final MapObverser<Project, Editor> focusEditorObverser = new MapAdapter<Project, Editor>() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireChanged(Project key, Editor oldValue, Editor newValue) {
			SwingUtil.invokeInEventQueue(() -> {
				Project focusProject = Objects.isNull(focusProjectModel) ? null : focusProjectModel.get();

				if (!Objects.equals(key, focusProject))
					return;

				if (Objects.nonNull(newValue)) {
					enableFocusEditor();
				} else {
					disableFocusEditor();
				}
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireCleared() {
			SwingUtil.invokeInEventQueue(() -> {
				disableFocusEditor();
			});
		}

		@Override
		public void firePut(Project key, Editor value) {
			SwingUtil.invokeInEventQueue(() -> {
				Project focusProject = Objects.isNull(focusProjectModel) ? null : focusProjectModel.get();

				if (!Objects.equals(key, focusProject))
					return;

				if (Objects.nonNull(value)) {
					enableFocusEditor();
				} else {
					disableFocusEditor();
				}
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireRemoved(Project key, Editor value) {
			SwingUtil.invokeInEventQueue(() -> {
				Project focusProject = Objects.isNull(focusProjectModel) ? null : focusProjectModel.get();

				if (!Objects.equals(key, focusProject))
					return;

				disableFocusEditor();
			});
		}

	};

	private final SetObverser<File> focusFileObverser = new SetAdapter<File>() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireAdded(File element) {
			SwingUtil.invokeInEventQueue(() -> {
				if (!focusFileModel.isEmpty()) {
					enableFocusFiles();
				} else {
					disableFocusFiles();
				}

			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireCleared() {
			SwingUtil.invokeInEventQueue(() -> {
				disableFocusFiles();
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireRemoved(File element) {
			SwingUtil.invokeInEventQueue(() -> {
				if (!focusFileModel.isEmpty()) {
					enableFocusFiles();
				} else {
					disableFocusFiles();
				}

			});
		}

	};

	private final ReferenceObverser<Project> focusProjectObverser = new ReferenceAdapter<Project>() {

		@Override
		public void fireCleared() {
			SwingUtil.invokeInEventQueue(() -> {
				disableFocusProject();
				disableNonFocusEditor();
				disableFocusEditor();
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireSet(Project oldValue, Project newValue) {
			SwingUtil.invokeInEventQueue(() -> {
				// if (Objects.nonNull(newValue)) {
				// enableFocusProject();
				// } else {
				// disableFocusProject();
				// }
				//
				// boolean atLeastOne = false;
				//
				// editorModel.getLock().readLock().lock();
				// try {
				// for (ProjectFilePair pair : editorModel.keySet()) {
				// if (Objects.equals(pair.getProject(), newValue)) {
				// atLeastOne = true;
				// break;
				// }
				// }
				// } finally {
				// editorModel.getLock().readLock().unlock();
				// }
				//
				// if (!atLeastOne) {
				// disableNonFocusEditor();
				// } else {
				// enableNonFocusEditor();
				// }
				//
				// if
				// (Objects.nonNull(openedAndFocusModel.getFocusEditorMap().get(newValue)))
				// {
				// enableFocusEditor();
				// } else {
				// disableFocusEditor();
				// }

				if (Objects.nonNull(newValue)) {
					enableFocusProject();

					boolean atLeastOne = false;

					editorModel.getLock().readLock().lock();
					try {
						for (ProjectFilePair pair : editorModel.keySet()) {
							if (Objects.equals(pair.getProject(), newValue)) {
								atLeastOne = true;
								break;
							}
						}
					} finally {
						editorModel.getLock().readLock().unlock();
					}

					if (!atLeastOne) {
						disableNonFocusEditor();
					} else {
						enableNonFocusEditor();
					}

					if (Objects.nonNull(focusEditorModel.get(newValue))) {
						enableFocusEditor();
					} else {
						disableFocusEditor();
					}
				} else {
					disableFocusProject();
					disableNonFocusEditor();
					disableFocusEditor();
				}
			});
		}

	};

	private final MapObverser<ProjectFilePair, Editor> editorObverser = new MapAdapter<ProjectFilePair, Editor>() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireCleared() {
			SwingUtil.invokeInEventQueue(() -> {
				disableNonFocusEditor();
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void firePut(ProjectFilePair key, Editor value) {
			SwingUtil.invokeInEventQueue(() -> {
				Project focusProject = Objects.isNull(focusProjectModel) ? null : focusProjectModel.get();
				if (Objects.equals(key.getProject(), focusProject)) {
					enableNonFocusEditor();
				}
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireRemoved(ProjectFilePair key, Editor value) {
			SwingUtil.invokeInEventQueue(() -> {
				Project focusProject = Objects.isNull(focusProjectModel) ? null : focusProjectModel.get();
				boolean atLeastOne = false;

				editorModel.getLock().readLock().lock();
				try {
					for (ProjectFilePair pair : editorModel.keySet()) {
						if (Objects.equals(pair.getProject(), focusProject)) {
							atLeastOne = true;
							break;
						}
					}
				} finally {
					editorModel.getLock().readLock().unlock();
				}

				if (!atLeastOne) {
					disableNonFocusEditor();
				} else {
					enableNonFocusEditor();
				}
			});
		}

	};

	/**
	 * 新实例。
	 */
	public MfMenu_02() {
		this(null, null, null, null, null, null, null);
	}

	/**
	 * 
	 * @param guiManager
	 * @param i18nHandler
	 * @param anchorFileModel
	 * @param focusProjectModel
	 * @param focusFileModel
	 * @param holdProjectModel
	 * @param focusEditorModel
	 * @param editorModel
	 */
	public MfMenu_02(GuiManager guiManager, I18nHandler i18nHandler, SyncReferenceModel<File> anchorFileModel,
			SyncReferenceModel<Project> focusProjectModel, SyncSetModel<File> focusFileModel,
			SyncMapModel<Project, Editor> focusEditorModel, SyncMapModel<ProjectFilePair, Editor> editorModel) {
		super(guiManager, i18nHandler);

		setText(label(LabelStringKey.MFMENU_2_1));
		setMnemonic('F');

		mi_01 = add(new JMenuItemAction.Builder().name(label(LabelStringKey.MFMENU_2_2)).mnemonic('N')
				.keyStorke(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK))
				.icon(new ImageIcon(ImageUtil.getInternalImage(ImageKey.NEW_FILE, ImageSize.ICON_SMALL)))
				.listener(e -> {
					MfMenu_02.this.guiManager.newFile(ExecType.CONCURRENT);
				}).build());
		mi_02 = add(new JMenuItemAction.Builder().name(label(LabelStringKey.MFMENU_2_3)).mnemonic('O')
				.keyStorke(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK))
				.icon(new ImageIcon(ImageUtil.getInternalImage(ImageKey.OPEN_FILE, ImageSize.ICON_SMALL)))
				.listener(e -> {
					MfMenu_02.this.guiManager.openFocusFile(ExecType.CONCURRENT);
				}).build());

		mi_12 = add(new JMenuItemAction.Builder().name(label(LabelStringKey.MFMENU_2_13)).mnemonic('H')
				.keyStorke(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK))
				.icon(new ImageIcon(ImageUtil.getInternalImage(ImageKey.OPEN_FILE, ImageSize.ICON_SMALL))).build());

		sp_01 = new JSeparator();
		add(sp_01);

		mi_03 = add(new JMenuItemAction.Builder().name(label(LabelStringKey.MFMENU_2_4)).mnemonic('E')
				.keyStorke(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK)).listener(e -> {
					MfMenu_02.this.guiManager.tryStopFocusEditor(ExecType.CONCURRENT);
				}).build());
		mi_04 = add(new JMenuItemAction.Builder().name(label(LabelStringKey.MFMENU_2_5)).mnemonic('L')
				.keyStorke(
						KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK))
				.listener(e -> {
					MfMenu_02.this.guiManager.tryStopFocusProjectEditor(ExecType.CONCURRENT);
				}).build());

		sp_02 = new JSeparator();
		add(sp_02);

		mi_05 = add(new JMenuItemAction.Builder().name(label(LabelStringKey.MFMENU_2_6)).mnemonic('S')
				.keyStorke(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK))
				.icon(new ImageIcon(ImageUtil.getInternalImage(CommonIconLib.SAVE_BLUE, ImageSize.ICON_SMALL)))
				.listener(e -> {
					MfMenu_02.this.guiManager.saveFocusEditor(ExecType.CONCURRENT);
				}).build());
		mi_06 = add(new JMenuItemAction.Builder().name(label(LabelStringKey.MFMENU_2_7)).mnemonic('A')
				.icon(new ImageIcon(ImageUtil.getInternalImage(CommonIconLib.SAVEAS_BLUE, ImageSize.ICON_SMALL)))
				.build());
		mi_07 = add(new JMenuItemAction.Builder().name(label(LabelStringKey.MFMENU_2_8)).mnemonic('V')
				.keyStorke(
						KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK))
				.build());

		sp_03 = new JSeparator();
		add(sp_03);

		mi_13 = add(new JMenuItemAction.Builder().name(label(LabelStringKey.MFMENU_2_14)).mnemonic('R')
				.keyStorke(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0)).listener(e -> {
					MfMenu_02.this.guiManager.renameAnchorFile(ExecType.CONCURRENT);
				}).build());

		mi_08 = add(new JMenuItemAction.Builder().name(label(LabelStringKey.MFMENU_2_9)).mnemonic('C')
				.keyStorke(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK))
				.icon(new ImageIcon(ImageUtil.getInternalImage(ImageKey.COPY_FILE, ImageSize.ICON_SMALL))).build());
		mi_09 = add(new JMenuItemAction.Builder().name(label(LabelStringKey.MFMENU_2_10)).mnemonic('V')
				.keyStorke(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.ALT_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK))
				.build());
		mi_10 = add(new JMenuItemAction.Builder().name(label(LabelStringKey.MFMENU_2_11)).mnemonic('D')
				.keyStorke(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0))
				.icon(new ImageIcon(ImageUtil.getInternalImage(ImageKey.DELETE_FILE, ImageSize.ICON_SMALL)))
				.listener(e -> {
					MfMenu_02.this.guiManager.deleteFocusFile(ExecType.CONCURRENT);
				}).build());

		sp_04 = new JSeparator();
		add(sp_04);

		mi_11 = add(new JMenuItemAction.Builder().name(label(LabelStringKey.MFMENU_2_12)).mnemonic('P')
				.keyStorke(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.ALT_DOWN_MASK)).listener(e -> {
					MfMenu_02.this.guiManager.showAnchorFilePropertiesDialog(ExecType.CONCURRENT);
				}).build());

		if (Objects.nonNull(editorModel)) {
			editorModel.addObverser(editorObverser);
		}
		if (Objects.nonNull(anchorFileModel)) {
			anchorFileModel.addObverser(anchorFileObverser);
		}
		if (Objects.nonNull(focusEditorModel)) {
			focusEditorModel.addObverser(focusEditorObverser);
		}
		if (Objects.nonNull(focusFileModel)) {
			focusFileModel.addObverser(focusFileObverser);
		}
		if (Objects.nonNull(focusProjectModel)) {
			focusProjectModel.addObverser(focusProjectObverser);
		}

		this.editorModel = editorModel;
		this.anchorFileModel = anchorFileModel;
		this.focusEditorModel = focusEditorModel;
		this.focusFileModel = focusFileModel;
		this.focusProjectModel = focusProjectModel;

		syncModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		super.dispose();
		if (Objects.nonNull(this.anchorFileModel)) {
			this.anchorFileModel.removeObverser(anchorFileObverser);
		}
		if (Objects.nonNull(this.focusEditorModel)) {
			this.focusEditorModel.removeObverser(focusEditorObverser);
		}
		if (Objects.nonNull(this.focusFileModel)) {
			this.focusFileModel.removeObverser(focusFileObverser);
		}
		if (Objects.nonNull(this.focusProjectModel)) {
			this.focusProjectModel.removeObverser(focusProjectObverser);
		}
		if (Objects.nonNull(editorModel)) {
			this.editorModel.removeObverser(editorObverser);
		}
	}

	/**
	 * @return the anchorFileModel
	 */
	public SyncReferenceModel<File> getAnchorFileModel() {
		return anchorFileModel;
	}

	/**
	 * 返回界面中的编辑器模型。
	 * 
	 * @return the editorModel 界面中的编辑器模型。
	 */
	public SyncMapModel<ProjectFilePair, Editor> getEditorModel() {
		return editorModel;
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
		if (Objects.nonNull(this.anchorFileModel)) {
			this.anchorFileModel.removeObverser(anchorFileObverser);
		}

		if (Objects.nonNull(anchorFileModel)) {
			anchorFileModel.addObverser(anchorFileObverser);
		}

		this.anchorFileModel = anchorFileModel;
		syncAnchorFileModel();
	}

	/**
	 * 设置界面中的编辑器模型。
	 * 
	 * @param editorModel
	 *            指定的界面中的编辑器模型。
	 */
	public void setEditorModel(SyncMapModel<ProjectFilePair, Editor> editorModel) {
		if (Objects.nonNull(this.editorModel)) {
			this.editorModel.removeObverser(editorObverser);
		}

		if (Objects.nonNull(editorModel)) {
			editorModel.addObverser(editorObverser);
		}

		this.editorModel = editorModel;
		syncEditorModel();
	}

	/**
	 * @param focusEditorModel
	 *            the focusEditorModel to set
	 */
	public void setFocusEditorModel(SyncMapModel<Project, Editor> focusEditorModel) {
		if (Objects.nonNull(this.focusEditorModel)) {
			this.focusEditorModel.removeObverser(focusEditorObverser);
		}

		if (Objects.nonNull(focusEditorModel)) {
			focusEditorModel.addObverser(focusEditorObverser);
		}

		this.focusEditorModel = focusEditorModel;
		syncFocusEditorModel();
	}

	/**
	 * @param focusFileModel
	 *            the focusFileModel to set
	 */
	public void setFocusFileModel(SyncSetModel<File> focusFileModel) {
		if (Objects.nonNull(this.focusFileModel)) {
			this.focusFileModel.removeObverser(focusFileObverser);
		}

		if (Objects.nonNull(focusFileModel)) {
			focusFileModel.addObverser(focusFileObverser);
		}

		this.focusFileModel = focusFileModel;
		syncFocusFileModel();
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
		setText(label(LabelStringKey.MFMENU_2_1));
		mi_01.setText(label(LabelStringKey.MFMENU_2_2));
		mi_02.setText(label(LabelStringKey.MFMENU_2_3));
		mi_03.setText(label(LabelStringKey.MFMENU_2_4));
		mi_04.setText(label(LabelStringKey.MFMENU_2_5));
		mi_05.setText(label(LabelStringKey.MFMENU_2_6));
		mi_06.setText(label(LabelStringKey.MFMENU_2_7));
		mi_07.setText(label(LabelStringKey.MFMENU_2_8));
		mi_08.setText(label(LabelStringKey.MFMENU_2_9));
		mi_09.setText(label(LabelStringKey.MFMENU_2_10));
		mi_10.setText(label(LabelStringKey.MFMENU_2_11));
		mi_11.setText(label(LabelStringKey.MFMENU_2_12));
		mi_12.setText(label(LabelStringKey.MFMENU_2_13));
		mi_13.setText(label(LabelStringKey.MFMENU_2_14));

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

	private void disableAnchorFile() {
		mi_02.setEnabled(false);
		mi_08.setEnabled(false);
		mi_09.setEnabled(false);
		mi_10.setEnabled(false);
		mi_11.setEnabled(false);
		mi_13.setEnabled(false);
	}

	private void disableFocusEditor() {
		mi_03.setEnabled(false);
		mi_05.setEnabled(false);
		mi_06.setEnabled(false);
	}

	private void disableFocusFiles() {
		mi_02.setEnabled(false);
		mi_08.setEnabled(false);
		mi_09.setEnabled(false);
		mi_10.setEnabled(false);
		mi_11.setEnabled(false);
		mi_12.setEnabled(false);
	}

	private void disableFocusProject() {
		mi_01.setEnabled(false);
	}

	private void disableNonFocusEditor() {
		mi_04.setEnabled(false);
		mi_07.setEnabled(false);
	}

	private void enableAnchorFile() {
		mi_02.setEnabled(true);
		mi_08.setEnabled(true);
		mi_09.setEnabled(true);
		mi_10.setEnabled(true);
		mi_11.setEnabled(true);
		mi_13.setEnabled(true);
	}

	private void enableFocusEditor() {
		mi_03.setEnabled(true);
		mi_05.setEnabled(true);
		mi_06.setEnabled(true);
	}

	private void enableFocusFiles() {
		mi_02.setEnabled(true);
		mi_08.setEnabled(true);
		mi_09.setEnabled(true);
		mi_10.setEnabled(true);
		mi_11.setEnabled(true);
		mi_12.setEnabled(true);
	}

	private void enableFocusProject() {
		mi_01.setEnabled(true);
	}

	private void enableNonFocusEditor() {
		mi_04.setEnabled(true);
		mi_07.setEnabled(true);
	}

	private void syncAnchorFileModel() {
		disableAnchorFile();

		if (Objects.isNull(anchorFileModel)) {
			return;
		}

		anchorFileModel.getLock().readLock().lock();
		try {
			if (Objects.nonNull(anchorFileModel.get())) {
				enableAnchorFile();
			}
		} finally {
			anchorFileModel.getLock().readLock().unlock();
		}

	}

	private void syncEditorModel() {
		disableNonFocusEditor();

		if (Objects.isNull(editorModel)) {
			return;
		}

		editorModel.getLock().readLock().lock();
		try {
			if (editorModel.size() > 0) {
				enableNonFocusEditor();
			}
		} finally {
			editorModel.getLock().readLock().unlock();
		}
	}

	private void syncFocusEditorModel() {
		disableFocusEditor();

		if (Objects.isNull(focusProjectModel)) {
			return;
		}

		if (Objects.isNull(focusEditorModel)) {
			return;
		}

		focusEditorModel.getLock().readLock().lock();
		focusProjectModel.getLock().readLock().lock();
		try {
			if (Objects.nonNull(focusProjectModel.get())) {
				Project focusProject = focusProjectModel.get();
				if (Objects.nonNull(focusEditorModel.get(focusProject))) {
					enableFocusEditor();
				}
			}
		} finally {
			focusProjectModel.getLock().readLock().unlock();
			focusEditorModel.getLock().readLock().unlock();
		}

	}

	private void syncFocusFileModel() {
		disableFocusFiles();

		if (Objects.isNull(focusFileModel)) {
			return;
		}

		focusFileModel.getLock().readLock().lock();
		try {
			if (!focusFileModel.isEmpty()) {
				enableFocusFiles();
			}
		} finally {
			focusFileModel.getLock().readLock().unlock();
		}
	}

	private void syncFocusProjectModel() {
		disableFocusProject();

		if (Objects.isNull(focusProjectModel)) {
			return;
		}

		focusProjectModel.getLock().readLock().lock();
		try {
			if (Objects.nonNull(focusProjectModel.get())) {
				enableFocusProject();
			}
		} finally {
			focusProjectModel.getLock().readLock().unlock();
		}
	}

	private void syncModel() {
		disableFocusProject();
		disableAnchorFile();
		disableFocusFiles();
		disableFocusEditor();
		disableNonFocusEditor();

		if (Objects.nonNull(focusProjectModel)) {
			focusProjectModel.getLock().readLock().lock();
			try {
				if (Objects.nonNull(focusProjectModel.get())) {
					enableFocusProject();
				}

				if (Objects.nonNull(focusEditorModel)) {
					focusEditorModel.getLock().readLock().lock();
					try {
						Project focusProject = focusProjectModel.get();
						if (Objects.nonNull(focusEditorModel.get(focusProject))) {
							enableFocusEditor();
						}
					} finally {
						focusEditorModel.getLock().readLock().unlock();
					}
				}
			} finally {
				focusProjectModel.getLock().readLock().unlock();
			}
		}

		if (Objects.nonNull(anchorFileModel)) {
			anchorFileModel.getLock().readLock().lock();
			try {
				if (Objects.nonNull(anchorFileModel.get())) {
					enableAnchorFile();
				}
			} finally {
				anchorFileModel.getLock().readLock().unlock();
			}
		}

		if (Objects.nonNull(focusFileModel)) {
			focusFileModel.getLock().readLock().lock();
			try {
				if (!focusFileModel.isEmpty()) {
					enableFocusFiles();
				}
			} finally {
				focusFileModel.getLock().readLock().unlock();
			}
		}

		if (Objects.nonNull(editorModel)) {
			editorModel.getLock().readLock().lock();
			try {
				if (editorModel.size() > 0) {
					enableNonFocusEditor();
				}
			} finally {
				editorModel.getLock().readLock().unlock();
			}
		}

	}

}
