package com.dwarfeng.projwiz.core.view.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.dwarfeng.dutil.basic.cna.model.SyncListModel;
import com.dwarfeng.dutil.basic.cna.model.SyncReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.SyncSetModel;
import com.dwarfeng.dutil.basic.cna.model.obv.ListAdapter;
import com.dwarfeng.dutil.basic.cna.model.obv.ListObverser;
import com.dwarfeng.dutil.basic.cna.model.obv.ReferenceAdapter;
import com.dwarfeng.dutil.basic.cna.model.obv.ReferenceObverser;
import com.dwarfeng.dutil.basic.cna.model.obv.SetAdapter;
import com.dwarfeng.dutil.basic.cna.model.obv.SetObverser;
import com.dwarfeng.dutil.basic.gui.awt.CommonIconLib;
import com.dwarfeng.dutil.basic.gui.awt.ImageSize;
import com.dwarfeng.dutil.basic.gui.awt.ImageUtil;
import com.dwarfeng.dutil.basic.gui.swing.JAdjustableBorderPanel;
import com.dwarfeng.dutil.basic.gui.swing.MuaListModel;
import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.dutil.develop.i18n.I18nHandler;
import com.dwarfeng.projwiz.core.model.cm.SyncComponentModel;
import com.dwarfeng.projwiz.core.model.eum.ImageKey;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.FileProcessor;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.ProjectProcessor;
import com.dwarfeng.projwiz.core.view.struct.GuiManager;
import com.dwarfeng.projwiz.core.view.struct.WindowSuppiler;

/**
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class ProjectAndFileMonitor extends ProjWizDialog implements WindowSuppiler {

	private static final long serialVersionUID = -1595187740965306253L;

	private final JLabel label_1;
	private final JLabel label_2;
	private final JLabel label_3;
	private final JLabel label_4;
	private final JLabel label_5;
	private final JLabel label_6;
	private final JList<Project> projectList;
	private final JList<File> fileList;

	private SyncReferenceModel<File> anchorFileModel;
	private SyncReferenceModel<Project> focusProjectModel;
	private SyncSetModel<File> focusFileModel;
	private SyncListModel<Project> holdProjectModel;
	private SyncComponentModel componentModel;

	private final MuaListModel<Project> holdProjects = new MuaListModel<>();
	private final MuaListModel<File> focusFiles = new MuaListModel<>();

	private final DefaultListCellRenderer projectRenderer = new DefaultListCellRenderer() {

		private static final long serialVersionUID = -1918212081384630363L;

		@Override
		public java.awt.Component getListCellRendererComponent(javax.swing.JList<?> list, Object value, int index,
				boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			Project project = (Project) value;
			if (Objects.isNull(project))
				return this;
			setText(project.getName());
			Image image = null;
			if (Objects.nonNull(componentModel)) {
				ProjectProcessor processor = componentModel.get(project.getProcessorClass());
				if (Objects.nonNull(processor)) {
					image = processor.getProjectIcon(project);
				}
			}
			if (Objects.isNull(image)) {
				setIcon(new ImageIcon(ImageUtil.getInternalImage(CommonIconLib.UNKNOWN_BLUE, ImageSize.ICON_SMALL)));
			} else {
				setIcon(new ImageIcon(ImageUtil.scaleImage(image, ImageSize.ICON_SMALL)));
			}
			return this;
		};
	};

	private final DefaultListCellRenderer fileRenderer = new DefaultListCellRenderer() {

		private static final long serialVersionUID = 401566907987607482L;

		@Override
		public java.awt.Component getListCellRendererComponent(javax.swing.JList<?> list, Object value, int index,
				boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			File file = (File) value;
			if (Objects.isNull(file))
				return this;
			setText(file.getName());
			Image image = null;
			if (Objects.nonNull(componentModel)) {
				FileProcessor processor = componentModel.get(file.getProcessorClass());
				if (Objects.nonNull(processor)) {
					image = processor.getFileIcon(file);
				}
			}
			if (Objects.isNull(image)) {
				setIcon(new ImageIcon(ImageUtil.getInternalImage(CommonIconLib.UNKNOWN_BLUE, ImageSize.ICON_SMALL)));
			} else {
				setIcon(new ImageIcon(ImageUtil.scaleImage(image, ImageSize.ICON_SMALL)));
			}
			return this;
		};
	};

	private final ReferenceObverser<File> anchorFileObverser = new ReferenceAdapter<File>() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireCleared() {
			SwingUtil.invokeInEventQueue(() -> {
				label_6.setIcon(null);
				label_6.setText("null");
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireSet(File oldValue, File newValue) {
			SwingUtil.invokeInEventQueue(() -> {
				if (Objects.isNull(newValue)) {
					label_6.setIcon(null);
					label_6.setText("null");
				} else {
					FileProcessor processor = Objects.isNull(componentModel) ? null
							: componentModel.get(newValue.getProcessorClass());
					Image image = Objects.isNull(processor) ? null : processor.getFileIcon(newValue);
					if (Objects.isNull(image)) {
						label_6.setIcon(new ImageIcon(
								ImageUtil.getInternalImage(CommonIconLib.UNKNOWN_BLUE, ImageSize.ICON_SMALL)));
					} else {
						label_6.setIcon(new ImageIcon(ImageUtil.scaleImage(image, ImageSize.ICON_SMALL)));
					}
					label_6.setText(newValue.getName());
				}
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
				focusFiles.add(element);
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireCleared() {
			SwingUtil.invokeInEventQueue(() -> {
				focusFiles.clear();
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireRemoved(File element) {
			SwingUtil.invokeInEventQueue(() -> {
				focusFiles.remove(element);
			});
		}

	};
	private final ReferenceObverser<Project> focusProjectObverser = new ReferenceAdapter<Project>() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireCleared() {
			SwingUtil.invokeInEventQueue(() -> {
				label_5.setIcon(null);
				label_5.setText("null");
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireSet(Project oldValue, Project newValue) {
			SwingUtil.invokeInEventQueue(() -> {
				if (Objects.isNull(newValue)) {
					label_5.setIcon(null);
					label_5.setText("null");
				} else {
					ProjectProcessor processor = Objects.isNull(componentModel) ? null
							: componentModel.get(newValue.getProcessorClass());
					Image image = Objects.isNull(processor) ? null : processor.getProjectIcon(newValue);
					if (Objects.isNull(image)) {
						label_5.setIcon(new ImageIcon(
								ImageUtil.getInternalImage(CommonIconLib.UNKNOWN_BLUE, ImageSize.ICON_SMALL)));
					} else {
						label_5.setIcon(new ImageIcon(ImageUtil.scaleImage(image, ImageSize.ICON_SMALL)));
					}
					label_5.setText(newValue.getName());
				}
			});
		}

	};
	private ListObverser<Project> holdProjectObverser = new ListAdapter<Project>() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireAdded(int index, Project element) {
			SwingUtil.invokeInEventQueue(() -> {
				holdProjects.add(index, element);
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireChanged(int index, Project oldElement, Project newElement) {
			SwingUtil.invokeInEventQueue(() -> {
				holdProjects.set(index, newElement);
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireCleared() {
			SwingUtil.invokeInEventQueue(() -> {
				holdProjects.clear();
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireRemoved(int index, Project element) {
			SwingUtil.invokeInEventQueue(() -> {
				holdProjects.remove(index);
			});
		}

	};

	private boolean disposeFlag = false;
	private final Lock disposeLock = new ReentrantLock();

	/**
	 * 新实例。
	 */
	public ProjectAndFileMonitor() {
		this(null, null, null, null, null, null, null);
	}

	/**
	 * 新实例。
	 * 
	 * @param obversers
	 *            观察器集合。
	 * @param i18n
	 *            国际化接口。
	 */
	public ProjectAndFileMonitor(GuiManager guiManager, I18nHandler i18nHandler,
			SyncReferenceModel<File> anchorFileModel, SyncReferenceModel<Project> focusProjectModel,
			SyncSetModel<File> focusFileModel, SyncListModel<Project> holdProjectModel,
			SyncComponentModel componentModel) {
		super(guiManager, i18nHandler);

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosed(WindowEvent e) {
				disposeLock.lock();
				try {
					disposeFlag = true;
				} finally {
					disposeLock.unlock();
				}
			}
		});

		setAlwaysOnTop(true);
		setTitle(label(LabelStringKey.OAFDIA_5));
		setIconImage(ImageUtil.getInternalImage(ImageKey.ICON));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModalityType(ModalityType.MODELESS);
		setPreferredSize(new Dimension(600, 400));
		getContentPane().setLayout(new BorderLayout(0, 0));

		JAdjustableBorderPanel adjustableBorderPanel = new JAdjustableBorderPanel();
		adjustableBorderPanel.setSeperatorThickness(5);
		adjustableBorderPanel.setWestPreferredValue(300);
		adjustableBorderPanel.setWestEnabled(true);
		getContentPane().add(adjustableBorderPanel, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		adjustableBorderPanel.add(panel, BorderLayout.WEST);
		panel.setLayout(new BorderLayout(0, 0));

		label_1 = new JLabel(label(LabelStringKey.OAFDIA_1));
		panel.add(label_1, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);

		projectList = new JList<>();
		scrollPane.setViewportView(projectList);
		projectList.setModel(holdProjects);
		projectList.setCellRenderer(projectRenderer);

		JPanel panel_1 = new JPanel();
		adjustableBorderPanel.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));

		label_2 = new JLabel(label(LabelStringKey.OAFDIA_2));
		panel_1.add(label_2, BorderLayout.NORTH);

		JScrollPane scrollPane_1 = new JScrollPane();
		panel_1.add(scrollPane_1, BorderLayout.CENTER);

		fileList = new JList<>();
		scrollPane_1.setViewportView(fileList);
		fileList.setModel(focusFiles);
		fileList.setCellRenderer(fileRenderer);

		JPanel panel_2 = new JPanel();
		getContentPane().add(panel_2, BorderLayout.SOUTH);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[] { 60, 48, 0 };
		gbl_panel_2.rowHeights = new int[] { 15, 0, 0 };
		gbl_panel_2.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel_2.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		panel_2.setLayout(gbl_panel_2);

		label_3 = new JLabel(label(LabelStringKey.OAFDIA_3));
		GridBagConstraints gbc_label_3 = new GridBagConstraints();
		gbc_label_3.anchor = GridBagConstraints.NORTHWEST;
		gbc_label_3.insets = new Insets(0, 0, 5, 5);
		gbc_label_3.gridx = 0;
		gbc_label_3.gridy = 0;
		panel_2.add(label_3, gbc_label_3);

		label_5 = new JLabel("null");
		GridBagConstraints gbc_label_5 = new GridBagConstraints();
		gbc_label_5.anchor = GridBagConstraints.WEST;
		gbc_label_5.insets = new Insets(0, 0, 5, 0);
		gbc_label_5.gridx = 1;
		gbc_label_5.gridy = 0;
		panel_2.add(label_5, gbc_label_5);

		label_4 = new JLabel(label(LabelStringKey.OAFDIA_4));
		GridBagConstraints gbc_label_4 = new GridBagConstraints();
		gbc_label_4.anchor = GridBagConstraints.NORTHWEST;
		gbc_label_4.insets = new Insets(0, 0, 0, 5);
		gbc_label_4.gridx = 0;
		gbc_label_4.gridy = 1;
		panel_2.add(label_4, gbc_label_4);

		label_6 = new JLabel("null");
		GridBagConstraints gbc_label_6 = new GridBagConstraints();
		gbc_label_6.anchor = GridBagConstraints.WEST;
		gbc_label_6.gridx = 1;
		gbc_label_6.gridy = 1;
		panel_2.add(label_6, gbc_label_6);

		pack();

		if (Objects.nonNull(anchorFileModel)) {
			anchorFileModel.addObverser(anchorFileObverser);
		}
		if (Objects.nonNull(focusProjectModel)) {
			focusProjectModel.addObverser(focusProjectObverser);
		}
		if (Objects.nonNull(focusFileModel)) {
			focusFileModel.addObverser(focusFileObverser);
		}
		if (Objects.nonNull(holdProjectModel)) {
			holdProjectModel.addObverser(holdProjectObverser);
		}

		this.anchorFileModel = anchorFileModel;
		this.focusProjectModel = focusProjectModel;
		this.focusFileModel = focusFileModel;
		this.holdProjectModel = holdProjectModel;
		this.componentModel = componentModel;

		// TODO 此处的处理不是特别好，有几个步骤是重复的。
		syncOpenedAndFocusModel();
		syncComponentModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		disposeLock.lock();
		try {
			if (Objects.nonNull(this.anchorFileModel)) {
				this.anchorFileModel.removeObverser(anchorFileObverser);
			}
			if (Objects.nonNull(this.focusFileModel)) {
				this.focusFileModel.removeObverser(focusFileObverser);
			}
			if (Objects.nonNull(this.focusProjectModel)) {
				this.focusProjectModel.removeObverser(focusProjectObverser);
			}
			if (Objects.nonNull(this.holdProjectModel)) {
				this.holdProjectModel.removeObverser(holdProjectObverser);
			}

			super.dispose();
		} finally {
			disposeLock.unlock();
		}
	}

	/**
	 * @return the anchorFileModel
	 */
	public SyncReferenceModel<File> getAnchorFileModel() {
		return anchorFileModel;
	}

	/**
	 * 获取面板的组件模型。
	 * 
	 * @return 面板的组件模型。
	 */
	public SyncComponentModel getComponentModel() {
		return componentModel;
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
	 * @return the holdProjectModel
	 */
	public SyncListModel<Project> getHoldProjectModel() {
		return holdProjectModel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getKey() {
		return this.getClass().toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Window getWindow() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDispose() {
		disposeLock.lock();
		try {
			return disposeFlag;
		} finally {
			disposeLock.unlock();
		}
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
	 * 设置面板的组件模型。
	 * 
	 * @param componentModel
	 *            指定的面板组件模型。
	 */
	public void setComponentModel(SyncComponentModel componentModel) {
		this.componentModel = componentModel;
		syncComponentModel();
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
	 * @param holdProjectModel
	 *            the holdProjectModel to set
	 */
	public void setHoldProjectModel(SyncListModel<Project> holdProjectModel) {
		if (Objects.nonNull(this.holdProjectModel)) {
			this.holdProjectModel.removeObverser(holdProjectObverser);
		}

		if (Objects.nonNull(holdProjectModel)) {
			holdProjectModel.addObverser(holdProjectObverser);
		}

		this.holdProjectModel = holdProjectModel;
		syncHoldProjectModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void refreshLabels() {
		setTitle(label(LabelStringKey.OAFDIA_5));
		label_1.setText(label(LabelStringKey.OAFDIA_1));
		label_2.setText(label(LabelStringKey.OAFDIA_2));
		label_3.setText(label(LabelStringKey.OAFDIA_3));
		label_4.setText(label(LabelStringKey.OAFDIA_4));
	}

	private void syncAnchorFileModel() {
		label_6.setIcon(null);
		label_6.setText("null");

		if (Objects.isNull(anchorFileModel)) {
			return;
		}

		if (Objects.isNull(componentModel)) {
			return;
		}

		// TODO 此处对于fileProcessorModel的处理不是太好。
		anchorFileModel.getLock().readLock().lock();
		try {
			if (Objects.nonNull(anchorFileModel.get())) {
				FileProcessor processor = Objects.isNull(componentModel) ? null
						: componentModel.get(anchorFileModel.get().getProcessorClass());
				Image image = Objects.isNull(processor) ? null : processor.getFileIcon(anchorFileModel.get());
				if (Objects.isNull(image)) {
					label_6.setIcon(new ImageIcon(
							ImageUtil.getInternalImage(CommonIconLib.UNKNOWN_BLUE, ImageSize.ICON_SMALL)));
				} else {
					label_6.setIcon(new ImageIcon(ImageUtil.scaleImage(image, ImageSize.ICON_SMALL)));
				}
				label_6.setText(anchorFileModel.get().getName());
			}
		} finally {
			anchorFileModel.getLock().readLock().unlock();
		}
	}

	private void syncComponentModel() {
		syncFileProcessor();
		syncProjectProcessor();
	}

	private void syncFileProcessor() {
		projectList.repaint();
		label_6.setIcon(null);
		label_6.setText("null");

		if (Objects.isNull(anchorFileModel)) {
			return;
		}

		if (Objects.isNull(anchorFileModel.get())) {
			return;
		}

		if (Objects.isNull(componentModel)) {
			return;
		}

		// TODO 此处对于projectProcessorModel的处理不是太好。
		componentModel.getLock().readLock().lock();
		try {
			projectList.repaint();

			FileProcessor processor = Objects.isNull(componentModel) ? null
					: componentModel.get(anchorFileModel.get().getProcessorClass());
			Image image = Objects.isNull(processor) ? null : processor.getFileIcon(anchorFileModel.get());
			if (Objects.isNull(image)) {
				label_6.setIcon(
						new ImageIcon(ImageUtil.getInternalImage(CommonIconLib.UNKNOWN_BLUE, ImageSize.ICON_SMALL)));
			} else {
				label_6.setIcon(new ImageIcon(ImageUtil.scaleImage(image, ImageSize.ICON_SMALL)));
			}
			label_6.setText(anchorFileModel.get().getName());
		} finally {
			componentModel.getLock().readLock().unlock();
		}
	}

	private void syncFocusFileModel() {
		focusFiles.clear();

		if (Objects.isNull(focusFileModel)) {
			return;
		}

		focusFileModel.getLock().readLock().lock();
		try {
			for (File file : focusFileModel) {
				focusFiles.add(file);
			}
		} finally {
			focusFileModel.getLock().readLock().unlock();
		}
	}

	private void syncFocusProjectModel() {
		label_5.setIcon(null);
		label_5.setText("null");

		if (Objects.isNull(focusProjectModel)) {
			return;
		}

		if (Objects.isNull(componentModel)) {
			return;
		}

		focusProjectModel.getLock().readLock().lock();
		componentModel.getLock().readLock().lock();
		try {
			if (Objects.nonNull(focusProjectModel.get())) {
				ProjectProcessor processor = Objects.isNull(componentModel) ? null
						: componentModel.get(focusProjectModel.get().getProcessorClass());
				Image image = Objects.isNull(processor) ? null : processor.getProjectIcon(focusProjectModel.get());
				if (Objects.isNull(image)) {
					label_5.setIcon(new ImageIcon(
							ImageUtil.getInternalImage(CommonIconLib.UNKNOWN_BLUE, ImageSize.ICON_SMALL)));
				} else {
					label_5.setIcon(new ImageIcon(ImageUtil.scaleImage(image, ImageSize.ICON_SMALL)));
				}
				label_5.setText(focusProjectModel.get().getName());
			}
		} finally {
			componentModel.getLock().readLock().unlock();
			focusProjectModel.getLock().readLock().unlock();
		}
	}

	private void syncHoldProjectModel() {
		holdProjects.clear();

		if (Objects.isNull(holdProjectModel)) {
			return;
		}

		holdProjectModel.getLock().readLock().lock();
		try {
			for (Project project : holdProjectModel) {
				holdProjects.add(project);
			}
		} finally {
			holdProjectModel.getLock().readLock().unlock();
		}
	}

	private void syncOpenedAndFocusModel() {
		focusFiles.clear();
		holdProjects.clear();
		label_5.setIcon(null);
		label_5.setText("null");
		label_6.setIcon(null);
		label_6.setText("null");

		if (Objects.isNull(anchorFileModel)) {
			return;
		}
		if (Objects.isNull(focusProjectModel)) {
			return;
		}
		if (Objects.isNull(focusFileModel)) {
			return;
		}
		if (Objects.isNull(holdProjectModel)) {
			return;
		}
		if (Objects.isNull(componentModel)) {
			return;
		}

		anchorFileModel.getLock().readLock().lock();
		focusProjectModel.getLock().readLock().lock();
		focusFileModel.getLock().readLock().lock();
		holdProjectModel.getLock().readLock().lock();
		componentModel.getLock().readLock().lock();
		try {

			if (Objects.nonNull(focusProjectModel.get())) {
				ProjectProcessor processor = Objects.isNull(componentModel) ? null
						: componentModel.get(focusProjectModel.get().getProcessorClass());
				Image image = Objects.isNull(processor) ? null : processor.getProjectIcon(focusProjectModel.get());
				if (Objects.isNull(image)) {
					label_5.setIcon(new ImageIcon(
							ImageUtil.getInternalImage(CommonIconLib.UNKNOWN_BLUE, ImageSize.ICON_SMALL)));
				} else {
					label_5.setIcon(new ImageIcon(ImageUtil.scaleImage(image, ImageSize.ICON_SMALL)));
				}
				label_5.setText(focusProjectModel.get().getName());
			}

			if (Objects.nonNull(anchorFileModel.get())) {
				FileProcessor processor = Objects.isNull(componentModel) ? null
						: componentModel.get(anchorFileModel.get().getProcessorClass());
				Image image = Objects.isNull(processor) ? null : processor.getFileIcon(anchorFileModel.get());
				if (Objects.isNull(image)) {
					label_6.setIcon(new ImageIcon(
							ImageUtil.getInternalImage(CommonIconLib.UNKNOWN_BLUE, ImageSize.ICON_SMALL)));
				} else {
					label_6.setIcon(new ImageIcon(ImageUtil.scaleImage(image, ImageSize.ICON_SMALL)));
				}
				label_6.setText(anchorFileModel.get().getName());
			}

			for (Project project : holdProjectModel) {
				holdProjects.add(project);
			}

			for (File file : focusFileModel) {
				focusFiles.add(file);
			}

		} finally {
			componentModel.getLock().readLock().unlock();
			holdProjectModel.getLock().readLock().unlock();
			focusFileModel.getLock().readLock().unlock();
			focusProjectModel.getLock().readLock().unlock();
			anchorFileModel.getLock().readLock().unlock();
		}

	}

	private void syncProjectProcessor() {
		fileList.repaint();
		label_5.setIcon(null);
		label_5.setText("null");

		if (Objects.isNull(focusProjectModel)) {
			return;
		}

		if (Objects.isNull(focusProjectModel.get())) {
			return;
		}

		if (Objects.isNull(componentModel)) {
			return;
		}

		componentModel.getLock().readLock().lock();
		try {
			projectList.repaint();

			ProjectProcessor processor = Objects.isNull(componentModel) ? null
					: componentModel.get(focusProjectModel.get().getProcessorClass());
			Image image = Objects.isNull(processor) ? null : processor.getProjectIcon(focusProjectModel.get());
			if (Objects.isNull(image)) {
				label_5.setIcon(
						new ImageIcon(ImageUtil.getInternalImage(CommonIconLib.UNKNOWN_BLUE, ImageSize.ICON_SMALL)));
			} else {
				label_5.setIcon(new ImageIcon(ImageUtil.scaleImage(image, ImageSize.ICON_SMALL)));
			}
			label_5.setText(focusProjectModel.get().getName());
		} finally {
			componentModel.getLock().readLock().unlock();
		}

	}

}
