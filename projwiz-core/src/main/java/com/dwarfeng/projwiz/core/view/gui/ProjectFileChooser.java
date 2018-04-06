package com.dwarfeng.projwiz.core.view.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.dwarfeng.dutil.basic.cna.CollectionUtil;
import com.dwarfeng.dutil.basic.cna.model.DefaultReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.SyncListModel;
import com.dwarfeng.dutil.basic.cna.model.obv.ListAdapter;
import com.dwarfeng.dutil.basic.cna.model.obv.ListObverser;
import com.dwarfeng.dutil.basic.gui.awt.CommonIconLib;
import com.dwarfeng.dutil.basic.gui.awt.ImageSize;
import com.dwarfeng.dutil.basic.gui.awt.ImageUtil;
import com.dwarfeng.dutil.basic.gui.swing.MuaListModel;
import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.dutil.basic.prog.Filter;
import com.dwarfeng.dutil.develop.i18n.I18nHandler;
import com.dwarfeng.projwiz.core.model.cm.SyncModuleModel;
import com.dwarfeng.projwiz.core.model.cm.Tree.Path;
import com.dwarfeng.projwiz.core.model.eum.ImageKey;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.model.obv.ProjectAdapter;
import com.dwarfeng.projwiz.core.model.obv.ProjectObverser;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.FileProcessor;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.Project.AddingSituation;
import com.dwarfeng.projwiz.core.model.struct.Project.RemovingSituation;
import com.dwarfeng.projwiz.core.model.struct.ProjectProcessor;
import com.dwarfeng.projwiz.core.util.ProjectFileUtil;
import com.dwarfeng.projwiz.core.view.eum.ChooseOption;
import com.dwarfeng.projwiz.core.view.eum.FileSelectionMode;
import com.dwarfeng.projwiz.core.view.struct.GuiManager;

/**
 * 
 * @author DwArFeng
 * @since 0.0.2-alpha
 */
public final class ProjectFileChooser extends ProjWizChooser {

	/**
	 * 文件过滤器。
	 * 
	 * @author DwArFeng
	 * @since 0.0.2-alpha
	 */
	public static interface FileFilter extends Filter<File> {

		/**
		 * 该文件过滤器的描述。
		 * 
		 * @return 文件过滤器的描述。
		 */
		public String getDescription();
	}

	/**
	 * 焦点组件。
	 * 
	 * @author DwArFeng
	 * @since 0.0.2-alpha
	 */
	private enum FocusModule {
		/** 代表文件选择列表。 */
		LIST,
		/** 代表文件路径输入框。 */
		TEXT_FIELD,
	}

	/**
	 * 文件夹文件筛选器。
	 * <p>
	 * 允许所有的文件夹。
	 * 
	 * @author DwArFeng
	 * @since 0.0.2-alpha
	 */
	private final class FolderFileFilter implements FileFilter {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean accept(File file) {
			if (Objects.isNull(file)) {
				return false;
			}

			return file.isFolder();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return null;
		}

	}

	private final JLabel label_projectName;
	private final JComboBox<Project> comboBox_project;
	private final JList<File> list_file;
	private final JLabel label_pathName;
	private final JLabel label_fileFilter;
	private final JTextField textField_filePath;
	private final JButton uptoButton;
	private final JButton frontpageButton;
	private JComboBox<FileFilter> comboBox_fileFilter;

	private SyncListModel<Project> holdProjectModel;
	private SyncModuleModel moduleModel;

	private final ReferenceModel<File> selectedFile = new DefaultReferenceModel<>();
	private final List<File> selectedFiles = new ArrayList<>();
	// 将文件与名称存放在映射中，避免在Renderer中由于使用 File.getName()导致同步锁死锁。
	private final Map<File, String> fileNameMap = new HashMap<>();
	private final DefaultComboBoxModel<Project> projectComboBoxModel = new DefaultComboBoxModel<>();
	private final MuaListModel<File> fileListModel = new MuaListModel<>();
	private final ComboBoxModel<FileFilter> fileFilterComboBoxModel = new DefaultComboBoxModel<>();
	private final ListSelectionModel fileListSelectionModel = new DefaultListSelectionModel();

	private ListCellRenderer<Object> projectComboBoxRenderer = new DefaultListCellRenderer() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
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
		}

	};
	private ListCellRenderer<Object> fileListRenderer = new DefaultListCellRenderer() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			File file = (File) value;
			if (Objects.isNull(file))
				return this;
			// 用注释中的文本会使文件上锁，进而导致已知的死锁。
			// setText(file.getName());
			setText(fileNameMap.get(file));
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
		}

	};

	private final ListObverser<Project> holdProjectObverser = new ListAdapter<Project>() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireAdded(int index, Project element) {
			SwingUtil.invokeInEventQueue(() -> {
				projectComboBoxModel.insertElementAt(element, index);
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireChanged(int index, Project oldElement, Project newElement) {
			SwingUtil.invokeInEventQueue(() -> {
				projectComboBoxModel.removeElementAt(index);
				projectComboBoxModel.insertElementAt(newElement, index);
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireCleared() {
			SwingUtil.invokeInEventQueue(() -> {
				projectComboBoxModel.removeAllElements();
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireRemoved(int index, Project element) {
			SwingUtil.invokeInEventQueue(() -> {
				projectComboBoxModel.removeElementAt(index);
			});
		}

	};
	private final ProjectObverser currentProjectObverser = new ProjectAdapter() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireFileAdded(Path<File> path, File parent, File file, AddingSituation situation) {
			SwingUtil.invokeInEventQueue(() -> {
				// TODO Auto-generated method stub
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireFileRemoved(Path<File> path, File parent, File file, RemovingSituation situation) {
			SwingUtil.invokeInEventQueue(() -> {
				// TODO Auto-generated method stub
			});
		}

		@Override
		public void fireFileRenamed(Path<File> path, File file, String oldName, String newName) {
			SwingUtil.invokeInEventQueue(() -> {
				// TODO Auto-generated method stub
			});
		}

		@Override
		public void fireSaved() {
			SwingUtil.invokeInEventQueue(() -> {
				// TODO Auto-generated method stub
			});
		}

		@Override
		public void fireStopped() {
			SwingUtil.invokeInEventQueue(() -> {
				// TODO Auto-generated method stub
			});
		}

	};
	private Project currentProject;
	private File currentRootFile;
	private boolean rootFileAdjustFlag = false;

	private boolean acceptAllFileFilterUsed;
	private boolean controlButtonsAreShown;
	private File currentDirectory;
	private boolean dragEnabled;
	private Set<FileFilter> fileFilters;
	private boolean fileHidingEnabled;
	private FileSelectionMode fileSelectionMode;
	private boolean multiSelectionEnabled;
	private ProjWizDialog currentDialog;
	private FocusModule focusModule;

	/**
	 * 新实例。
	 */
	public ProjectFileChooser() {
		this(null, null, null, null);
	}

	/**
	 * 新实例。
	 * 
	 * @param guiManager
	 *            程序管理器。
	 * @param i18n
	 *            国际化接口。
	 */
	public ProjectFileChooser(GuiManager guiManager, I18nHandler i18nHandler, SyncListModel<Project> holdProjectModel,
			SyncModuleModel moduleModel) {
		super(guiManager, i18nHandler, LabelStringKey.PROJFC_4);

		setPreferredSize(new Dimension(500, 300));

		if (Objects.nonNull(holdProjectModel)) {
			holdProjectModel.addObverser(holdProjectObverser);
		}

		this.holdProjectModel = holdProjectModel;
		this.moduleModel = moduleModel;
		setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.NORTH);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[] { 0, 48, 28, 28, 0 };
		gbl_panel_1.rowHeights = new int[] { 28, 0 };
		gbl_panel_1.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_panel_1.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel_1.setLayout(gbl_panel_1);

		label_projectName = new JLabel(label(LabelStringKey.PROJFC_1));
		GridBagConstraints gbc_label_projectName = new GridBagConstraints();
		gbc_label_projectName.anchor = GridBagConstraints.WEST;
		gbc_label_projectName.insets = new Insets(0, 0, 0, 5);
		gbc_label_projectName.gridx = 0;
		gbc_label_projectName.gridy = 0;
		panel_1.add(label_projectName, gbc_label_projectName);

		comboBox_project = new JComboBox<>();
		GridBagConstraints gbc_comboBox_project = new GridBagConstraints();
		gbc_comboBox_project.insets = new Insets(0, 0, 0, 5);
		gbc_comboBox_project.fill = GridBagConstraints.BOTH;
		gbc_comboBox_project.gridx = 1;
		gbc_comboBox_project.gridy = 0;
		panel_1.add(comboBox_project, gbc_comboBox_project);
		comboBox_project.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (Objects.equals(currentProject, projectComboBoxModel.getSelectedItem())) {
					return;
				}

				if (Objects.nonNull(currentProject)) {
					currentProject.removeObverser(currentProjectObverser);
				}
				currentProject = (Project) projectComboBoxModel.getSelectedItem();
				if (Objects.nonNull(currentProject)) {
					currentProject.addObverser(currentProjectObverser);
				}

				currentRootFile = currentProject.getFileTree().getRoot();
				showRootFile();
			}
		});
		comboBox_project.setModel(projectComboBoxModel);
		comboBox_project.setRenderer(projectComboBoxRenderer);

		uptoButton = new JButton();
		uptoButton.setPreferredSize(new Dimension(28, 28));
		uptoButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (Objects.nonNull(currentProject) && Objects.nonNull(currentRootFile)) {
					if (!Objects.equals(currentRootFile, currentProject.getFileTree().getRoot())) {
						currentRootFile = currentProject.getFileTree().getParent(currentRootFile);
					} else {
						currentRootFile = currentProject.getFileTree().getRoot();
					}
					showRootFile();
					showFilePath();
				}
			}

		});
		uptoButton.setBorder(null);
		uptoButton.setIcon(new ImageIcon(ImageUtil.getInternalImage(ImageKey.UPTO, ImageSize.ICON_SMALL)));
		GridBagConstraints gbc_uptoButton = new GridBagConstraints();
		gbc_uptoButton.fill = GridBagConstraints.BOTH;
		gbc_uptoButton.gridx = 2;
		gbc_uptoButton.gridy = 0;
		panel_1.add(uptoButton, gbc_uptoButton);

		frontpageButton = new JButton();
		frontpageButton.setIcon(new ImageIcon(ImageUtil.getInternalImage(ImageKey.FRONTPAGE, ImageSize.ICON_SMALL)));
		frontpageButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (Objects.nonNull(currentProject)) {
					currentRootFile = currentProject.getFileTree().getRoot();
					showRootFile();
					showFilePath();
				}
			}
		});
		frontpageButton.setPreferredSize(new Dimension(28, 28));
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.BOTH;
		gbc_btnNewButton.gridx = 3;
		gbc_btnNewButton.gridy = 0;
		panel_1.add(frontpageButton, gbc_btnNewButton);

		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 48, 0, 0, 0 };
		gbl_panel.rowHeights = new int[] { 30, 0, 0, 30, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 4;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		panel.add(scrollPane, gbc_scrollPane);

		list_file = new JList<>();
		list_file.addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent e) {
				focusModule = FocusModule.LIST;
			}
		});
		list_file.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {
				if (isItemSelected() && !isClickAtItem(e.getPoint())) {
					fileListSelectionModel.clearSelection();
				}
			}

			private boolean isClickAtItem(Point point) {
				int index = list_file.locationToIndex(point);
				if (index < 0) {
					return false;
				}

				Rectangle rect = list_file.getCellBounds(index, index);
				return rect.contains(point);
			}

			private boolean isItemSelected() {
				return list_file.getSelectedIndex() >= 0;
			}
		});

		scrollPane.setViewportView(list_file);
		list_file.setVisibleRowCount(0);
		list_file.setLayoutOrientation(JList.VERTICAL_WRAP);
		list_file.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (isItemSelected() && !isClickAtItem(e.getPoint())) {
					fileListSelectionModel.clearSelection();
				}

				if (e.getClickCount() == 2 && isClickAtItem(e.getPoint())) {
					File rootFile = list_file.getSelectedValue();
					if (rootFile.isFolder()) {
						currentRootFile = list_file.getSelectedValue();
						showRootFile();
					} else {
						if (Objects.isNull(currentDialog)) {
							chooseOption = ChooseOption.ERROR_OPTION;
							return;
						}

						chooseOption = ChooseOption.APPROVE_OPTION;
						currentDialog.dispose();
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (isItemSelected() && !isClickAtItem(e.getPoint())) {
					fileListSelectionModel.clearSelection();
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (isItemSelected() && !isClickAtItem(e.getPoint())) {
					fileListSelectionModel.clearSelection();
				}
			}

			private boolean isClickAtItem(Point point) {
				int index = list_file.locationToIndex(point);
				if (index < 0) {
					return false;
				}

				Rectangle rect = list_file.getCellBounds(index, index);
				return rect.contains(point);
			}

			private boolean isItemSelected() {
				return list_file.getSelectedIndex() >= 0;
			}

		});
		list_file.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (rootFileAdjustFlag) {
					return;
				}

				if (e.getValueIsAdjusting()) {
					return;
				}

				showFilePath();
				// TODO Auto-generated method stub
			}
		});
		list_file.setModel(fileListModel);
		list_file.setSelectionModel(fileListSelectionModel);
		list_file.setCellRenderer(fileListRenderer);

		label_pathName = new JLabel(label(LabelStringKey.PROJFC_2));
		GridBagConstraints gbc_label_pathName = new GridBagConstraints();
		gbc_label_pathName.insets = new Insets(0, 0, 5, 5);
		gbc_label_pathName.gridx = 0;
		gbc_label_pathName.gridy = 1;
		panel.add(label_pathName, gbc_label_pathName);

		textField_filePath = new JTextField();
		textField_filePath.addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent e) {
				focusModule = FocusModule.TEXT_FIELD;
			}
		});
		GridBagConstraints gbc_textField_filePath = new GridBagConstraints();
		gbc_textField_filePath.insets = new Insets(0, 0, 5, 0);
		gbc_textField_filePath.gridwidth = 3;
		gbc_textField_filePath.fill = GridBagConstraints.BOTH;
		gbc_textField_filePath.gridx = 1;
		gbc_textField_filePath.gridy = 1;
		panel.add(textField_filePath, gbc_textField_filePath);
		textField_filePath.setColumns(10);

		label_fileFilter = new JLabel(label(LabelStringKey.PROJFC_3));
		GridBagConstraints gbc_label_fileFilter = new GridBagConstraints();
		gbc_label_fileFilter.insets = new Insets(0, 0, 5, 5);
		gbc_label_fileFilter.gridx = 0;
		gbc_label_fileFilter.gridy = 2;
		panel.add(label_fileFilter, gbc_label_fileFilter);
		label_fileFilter.setEnabled(false);

		comboBox_fileFilter = new JComboBox<>();
		comboBox_fileFilter.setModel(fileFilterComboBoxModel);
		GridBagConstraints gbc_comboBox_fileFilter = new GridBagConstraints();
		gbc_comboBox_fileFilter.fill = GridBagConstraints.BOTH;
		gbc_comboBox_fileFilter.gridwidth = 3;
		gbc_comboBox_fileFilter.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox_fileFilter.gridx = 1;
		gbc_comboBox_fileFilter.gridy = 2;
		panel.add(comboBox_fileFilter, gbc_comboBox_fileFilter);
		comboBox_fileFilter.setEnabled(false);

		GridBagConstraints gbc_button_approve = new GridBagConstraints();
		gbc_button_approve.insets = new Insets(0, 0, 0, 5);
		gbc_button_approve.gridx = 2;
		gbc_button_approve.gridy = 3;
		panel.add(button_approve, gbc_button_approve);

		GridBagConstraints gbc_button_cancel = new GridBagConstraints();
		gbc_button_cancel.gridx = 3;
		gbc_button_cancel.gridy = 3;
		panel.add(button_cancel, gbc_button_cancel);

		syncSettings();
		syncHoldProjectModel();
		syncModuleModel();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		if (Objects.nonNull(this.currentProject)) {
			currentProject.removeObverser(currentProjectObverser);
		}

		// 同步选择文件
		syncSelectedFile();
	}

	/**
	 * @return the currentDirectory
	 */
	public File getCurrentDirectory() {
		return currentDirectory;
	}

	/**
	 * @return the fileFilters
	 */
	public Set<FileFilter> getFileFilters() {
		return fileFilters;
	}

	/**
	 * @return the fileSelectionMode
	 */
	public FileSelectionMode getFileSelectionMode() {
		return fileSelectionMode;
	}

	/**
	 * @return the holdProjectModel
	 */
	public SyncListModel<Project> getHoldProjectModel() {
		return holdProjectModel;
	}

	/**
	 * @return the moduleModel
	 */
	public SyncModuleModel getModuleModel() {
		return moduleModel;
	}

	/**
	 * 获取选择的文件。
	 * 
	 * @return 选择的文件。
	 */
	public File getSelectedFile() {
		return selectedFile.get();
	}

	/**
	 * 获取选择的文件。
	 * 
	 * @return 选择的文件。
	 */
	public File[] getSelectedFiles() {
		return selectedFiles.toArray(new File[0]);
	}

	/**
	 * @return the acceptAllFileFilterUsed
	 */
	public boolean isAcceptAllFileFilterUsed() {
		return acceptAllFileFilterUsed;
	}

	/**
	 * 获取控制按钮是否显示。
	 * 
	 * @return 控制按钮是否显示。
	 */
	public boolean isControlButtonsAreShown() {
		return controlButtonsAreShown;
	}

	/**
	 * @return the dragEnabled
	 */
	public boolean isDragEnabled() {
		return dragEnabled;
	}

	/**
	 * @return the fileHidingEnabled
	 */
	public boolean isFileHidingEnabled() {
		return fileHidingEnabled;
	}

	/**
	 * @return the multiSelectionEnabled
	 */
	public boolean isMultiSelectionEnabled() {
		return multiSelectionEnabled;
	}

	/**
	 * @param acceptAllFileFilterUsed
	 *            the acceptAllFileFilterUsed to set
	 */
	public void setAcceptAllFileFilterUsed(boolean acceptAllFileFilterUsed) {
		this.acceptAllFileFilterUsed = acceptAllFileFilterUsed;
	}

	/**
	 * 设置控制按钮是否显示。
	 * 
	 * @param aFlag
	 *            是否显示。
	 */
	public void setControlButtonsAreShown(boolean controlButtonsAreShown) {
		this.controlButtonsAreShown = controlButtonsAreShown;
	}

	/**
	 * @param currentDirectory
	 *            the currentDirectory to set
	 */
	public void setCurrentDirectory(File currentDirectory) {
		this.currentDirectory = currentDirectory;
	}

	/**
	 * @param dragEnabled
	 *            the dragEnabled to set
	 */
	public void setDragEnabled(boolean dragEnabled) {
		this.dragEnabled = dragEnabled;
	}

	/**
	 * @param fileFilters
	 *            the fileFilters to set
	 */
	public void setFileFilters(Set<FileFilter> fileFilters) {
		this.fileFilters = fileFilters;
	}

	/**
	 * @param fileHidingEnabled
	 *            the fileHidingEnabled to set
	 */
	public void setFileHidingEnabled(boolean fileHidingEnabled) {
		this.fileHidingEnabled = fileHidingEnabled;
	}

	/**
	 * @param fileSelectionMode
	 *            the fileSelectionMode to set
	 */
	public void setFileSelectionMode(FileSelectionMode fileSelectionMode) {
		this.fileSelectionMode = fileSelectionMode;
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
	 * @param moduleModel
	 *            the moduleModel to set
	 */
	public void setModuleModel(SyncModuleModel moduleModel) {
		this.moduleModel = moduleModel;
		syncModuleModel();
	}

	/**
	 * @param multiSelectionEnabled
	 *            the multiSelectionEnabled to set
	 */
	public void setMultiSelectionEnabled(boolean multiSelectionEnabled) {
		this.multiSelectionEnabled = multiSelectionEnabled;
		if (multiSelectionEnabled) {
			list_file.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		} else {
			list_file.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void refreshLabels() {
		label_pathName.setText(label(LabelStringKey.PROJFC_2));
		label_fileFilter.setText(label(LabelStringKey.PROJFC_3));
		super.refreshLabels();
	}

	private void showFilePath() {
		if (Objects.nonNull(currentProject) && Objects.nonNull(currentRootFile)) {
			List<File> selectedFiles = new ArrayList<>();

			for (int i = 0; i < fileListModel.getSize(); i++) {
				if (fileListSelectionModel.isSelectedIndex(i)) {
					selectedFiles.add(fileListModel.get(i));
				}
			}

			if (selectedFiles.isEmpty()) {
				textField_filePath.setText(ProjectFileUtil.getStdPath(currentProject, currentRootFile));
			} else {
				textField_filePath.setText(ProjectFileUtil.getStdPath(Arrays.asList(currentProject), selectedFiles));
			}
		} else {
			textField_filePath.setText(null);
		}
	}

	private void showRootFile() {
		rootFileAdjustFlag = true;
		fileListModel.clear();
		fileNameMap.clear();
		if (Objects.nonNull(currentProject) && Objects.nonNull(currentRootFile)) {
			currentProject.getLock().readLock().lock();
			try {
				currentProject.getFileTree().getChilds(currentRootFile).forEach(file -> {
					CollectionUtil.insertByOrder(fileListModel, file, ProjectFileUtil.defaultFileComparator());
					fileNameMap.put(file, currentProject.getFileName(file));
				});
			} finally {
				currentProject.getLock().readLock().unlock();
			}
		}
		rootFileAdjustFlag = false;
	}

	private void syncModuleModel() {
		comboBox_project.repaint();
		list_file.repaint();
	}

	private void syncHoldProjectModel() {
		projectComboBoxModel.removeAllElements();

		if (Objects.isNull(holdProjectModel)) {
			return;
		}

		holdProjectModel.getLock().readLock().lock();
		try {
			holdProjectModel.forEach((project) -> {
				projectComboBoxModel.addElement(project);
			});
		} finally {
			holdProjectModel.getLock().readLock().unlock();
		}
	}

	private void syncSelectedFile() {
		selectedFiles.clear();
		selectedFile.set(null);

		switch (chooseOption) {
		case APPROVE_OPTION:
			if (fileListSelectionModel.getMinSelectionIndex() == -1) {
				selectedFiles.add(currentRootFile);
				selectedFile.set(currentRootFile);
			} else {
				for (int i = 0; i < fileListModel.size(); i++) {
					if (fileListSelectionModel.isSelectedIndex(i)) {
						selectedFiles.add(fileListModel.get(i));
					}
				}
				if (!fileListModel.isEmpty() && fileListSelectionModel.getMinSelectionIndex() >= 0) {
					selectedFile.set(fileListModel.get(fileListSelectionModel.getAnchorSelectionIndex()));
				}
			}
			break;
		default:
			break;
		}
	}

	private void syncSettings() {
		if (multiSelectionEnabled) {
			list_file.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		} else {
			list_file.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		// TODO Auto-generated method stub.
	}

}
