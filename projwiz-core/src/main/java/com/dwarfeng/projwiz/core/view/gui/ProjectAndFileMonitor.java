package com.dwarfeng.projwiz.core.view.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

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
import com.dwarfeng.projwiz.core.model.cm.SyncModuleModel;
import com.dwarfeng.projwiz.core.model.eum.ImageKey;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.FileProcessor;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.ProjectProcessor;
import com.dwarfeng.projwiz.core.view.struct.GuiManager;

/**
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class ProjectAndFileMonitor extends ProjWizDialog {

	private static final long serialVersionUID = -5248687618763902919L;
	
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
	private SyncModuleModel moduleModel;

	// 将文件与名称存放在映射中，以向渲染器提供文件的名称。
	private final Map<File, Project> fileProjectMap = new HashMap<>();
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
			if (Objects.nonNull(moduleModel)) {
				ProjectProcessor processor = moduleModel.get(project.getProcessorClass());
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
			setText(fileProjectMap.get(file).getFileName(file));
			Image image = null;
			if (Objects.nonNull(moduleModel)) {
				FileProcessor processor = moduleModel.get(file.getProcessorClass());
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
					FileProcessor processor = Objects.isNull(moduleModel) ? null
							: moduleModel.get(newValue.getProcessorClass());
					Image image = Objects.isNull(processor) ? null : processor.getFileIcon(newValue);
					if (Objects.isNull(image)) {
						label_6.setIcon(new ImageIcon(
								ImageUtil.getInternalImage(CommonIconLib.UNKNOWN_BLUE, ImageSize.ICON_SMALL)));
					} else {
						label_6.setIcon(new ImageIcon(ImageUtil.scaleImage(image, ImageSize.ICON_SMALL)));
					}
					label_6.setText(fileProjectMap.get(newValue).getFileName(newValue));
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
					ProjectProcessor processor = Objects.isNull(moduleModel) ? null
							: moduleModel.get(newValue.getProcessorClass());
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
				for (File file : element.getFileTree()) {
					fileProjectMap.put(file, element);
				}
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireChanged(int index, Project oldElement, Project newElement) {
			SwingUtil.invokeInEventQueue(() -> {
				holdProjects.set(index, newElement);

				Collection<File> file2Remove = new HashSet<>();
				for (Map.Entry<File, Project> entry : fileProjectMap.entrySet()) {
					if (Objects.equals(oldElement, entry.getValue())) {
						file2Remove.add(entry.getKey());
					}
				}
				fileProjectMap.keySet().removeAll(file2Remove);

				for (File file : newElement.getFileTree()) {
					fileProjectMap.put(file, newElement);
				}
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireCleared() {
			SwingUtil.invokeInEventQueue(() -> {
				holdProjects.clear();
				fileProjectMap.clear();
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireRemoved(int index, Project element) {
			SwingUtil.invokeInEventQueue(() -> {
				holdProjects.remove(index);

				Collection<File> file2Remove = new HashSet<>();
				for (Map.Entry<File, Project> entry : fileProjectMap.entrySet()) {
					if (Objects.equals(element, entry.getValue())) {
						file2Remove.add(entry.getKey());
					}
				}
				fileProjectMap.keySet().removeAll(file2Remove);
			});
		}

	};

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
			SyncSetModel<File> focusFileModel, SyncListModel<Project> holdProjectModel, SyncModuleModel moduleModel) {
		super(guiManager, i18nHandler);

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
		this.moduleModel = moduleModel;

		// TODO 此处的处理不是特别好，有几个步骤是重复的。
		syncOpenedAndFocusModel();
		syncModuleModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
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
	}

	/**
	 * @return the anchorFileModel
	 */
	public SyncReferenceModel<File> getAnchorFileModel() {
		return anchorFileModel;
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
	 * 获取面板的组件模型。
	 * 
	 * @return 面板的组件模型。
	 */
	public SyncModuleModel getModuleModel() {
		return moduleModel;
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
	 * 设置面板的组件模型。
	 * 
	 * @param moduleModel
	 *            指定的面板组件模型。
	 */
	public void setModuleModel(SyncModuleModel moduleModel) {
		this.moduleModel = moduleModel;
		syncModuleModel();
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

		if (Objects.isNull(moduleModel)) {
			return;
		}

		// TODO 此处对于fileProcessorModel的处理不是太好。
		anchorFileModel.getLock().readLock().lock();
		try {
			File anchorFile = anchorFileModel.get();
			if (Objects.nonNull(anchorFile)) {
				FileProcessor processor = Objects.isNull(moduleModel) ? null
						: moduleModel.get(anchorFile.getProcessorClass());
				Image image = Objects.isNull(processor) ? null : processor.getFileIcon(anchorFile);
				if (Objects.isNull(image)) {
					label_6.setIcon(new ImageIcon(
							ImageUtil.getInternalImage(CommonIconLib.UNKNOWN_BLUE, ImageSize.ICON_SMALL)));
				} else {
					label_6.setIcon(new ImageIcon(ImageUtil.scaleImage(image, ImageSize.ICON_SMALL)));
				}
				label_6.setText(fileProjectMap.get(anchorFile).getFileName(anchorFile));
			}
		} finally {
			anchorFileModel.getLock().readLock().unlock();
		}
	}

	private void syncFileProcessor() {
		projectList.repaint();
		label_6.setIcon(null);
		label_6.setText("null");

		if (Objects.isNull(anchorFileModel)) {
			return;
		}

		File anchorFile = anchorFileModel.get();

		if (Objects.isNull(anchorFile)) {
			return;
		}

		if (Objects.isNull(moduleModel)) {
			return;
		}

		// TODO 此处对于projectProcessorModel的处理不是太好。
		moduleModel.getLock().readLock().lock();
		try {
			projectList.repaint();

			FileProcessor processor = Objects.isNull(moduleModel) ? null
					: moduleModel.get(anchorFile.getProcessorClass());
			Image image = Objects.isNull(processor) ? null : processor.getFileIcon(anchorFile);
			if (Objects.isNull(image)) {
				label_6.setIcon(
						new ImageIcon(ImageUtil.getInternalImage(CommonIconLib.UNKNOWN_BLUE, ImageSize.ICON_SMALL)));
			} else {
				label_6.setIcon(new ImageIcon(ImageUtil.scaleImage(image, ImageSize.ICON_SMALL)));
			}
			label_6.setText(fileProjectMap.get(anchorFile).getFileName(anchorFile));
		} finally {
			moduleModel.getLock().readLock().unlock();
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

		if (Objects.isNull(moduleModel)) {
			return;
		}

		focusProjectModel.getLock().readLock().lock();
		moduleModel.getLock().readLock().lock();
		try {
			if (Objects.nonNull(focusProjectModel.get())) {
				ProjectProcessor processor = Objects.isNull(moduleModel) ? null
						: moduleModel.get(focusProjectModel.get().getProcessorClass());
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
			moduleModel.getLock().readLock().unlock();
			focusProjectModel.getLock().readLock().unlock();
		}
	}

	private void syncHoldProjectModel() {
		holdProjects.clear();
		fileProjectMap.clear();

		if (Objects.isNull(holdProjectModel)) {
			return;
		}

		holdProjectModel.getLock().readLock().lock();
		try {
			for (Project project : holdProjectModel) {
				holdProjects.add(project);
				for (File file : project.getFileTree()) {
					fileProjectMap.put(file, project);
				}
			}
		} finally {
			holdProjectModel.getLock().readLock().unlock();
		}
	}

	private void syncModuleModel() {
		syncFileProcessor();
		syncProjectProcessor();
	}

	private void syncOpenedAndFocusModel() {
		focusFiles.clear();
		holdProjects.clear();
		label_5.setIcon(null);
		label_5.setText("null");
		label_6.setIcon(null);
		label_6.setText("null");
		fileProjectMap.clear();

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
		if (Objects.isNull(moduleModel)) {
			return;
		}

		anchorFileModel.getLock().readLock().lock();
		focusProjectModel.getLock().readLock().lock();
		focusFileModel.getLock().readLock().lock();
		holdProjectModel.getLock().readLock().lock();
		moduleModel.getLock().readLock().lock();
		try {

			Project focusProject = focusProjectModel.get();
			if (Objects.nonNull(focusProject)) {
				ProjectProcessor processor = Objects.isNull(moduleModel) ? null
						: moduleModel.get(focusProject.getProcessorClass());
				Image image = Objects.isNull(processor) ? null : processor.getProjectIcon(focusProject);
				if (Objects.isNull(image)) {
					label_5.setIcon(new ImageIcon(
							ImageUtil.getInternalImage(CommonIconLib.UNKNOWN_BLUE, ImageSize.ICON_SMALL)));
				} else {
					label_5.setIcon(new ImageIcon(ImageUtil.scaleImage(image, ImageSize.ICON_SMALL)));
				}
				label_5.setText(focusProject.getName());
			}

			File anchorFile = anchorFileModel.get();
			if (Objects.nonNull(anchorFile)) {
				FileProcessor processor = Objects.isNull(moduleModel) ? null
						: moduleModel.get(anchorFile.getProcessorClass());
				Image image = Objects.isNull(processor) ? null : processor.getFileIcon(anchorFile);
				if (Objects.isNull(image)) {
					label_6.setIcon(new ImageIcon(
							ImageUtil.getInternalImage(CommonIconLib.UNKNOWN_BLUE, ImageSize.ICON_SMALL)));
				} else {
					label_6.setIcon(new ImageIcon(ImageUtil.scaleImage(image, ImageSize.ICON_SMALL)));
				}

				label_6.setText(fileProjectMap.get(anchorFile).getFileName(anchorFile));
			}

			for (Project project : holdProjectModel) {
				holdProjects.add(project);
				for (File file : project.getFileTree()) {
					fileProjectMap.put(file, project);
				}
			}

			for (File file : focusFileModel) {
				focusFiles.add(file);
			}

		} finally {
			moduleModel.getLock().readLock().unlock();
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

		if (Objects.isNull(moduleModel)) {
			return;
		}

		moduleModel.getLock().readLock().lock();
		try {
			projectList.repaint();

			ProjectProcessor processor = Objects.isNull(moduleModel) ? null
					: moduleModel.get(focusProjectModel.get().getProcessorClass());
			Image image = Objects.isNull(processor) ? null : processor.getProjectIcon(focusProjectModel.get());
			if (Objects.isNull(image)) {
				label_5.setIcon(
						new ImageIcon(ImageUtil.getInternalImage(CommonIconLib.UNKNOWN_BLUE, ImageSize.ICON_SMALL)));
			} else {
				label_5.setIcon(new ImageIcon(ImageUtil.scaleImage(image, ImageSize.ICON_SMALL)));
			}
			label_5.setText(focusProjectModel.get().getName());
		} finally {
			moduleModel.getLock().readLock().unlock();
		}

	}

}
