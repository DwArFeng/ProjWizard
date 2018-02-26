package com.dwarfeng.projwiz.core.view.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.dwarfeng.dutil.basic.cna.model.SyncMapModel;
import com.dwarfeng.dutil.basic.cna.model.obv.MapAdapter;
import com.dwarfeng.dutil.basic.cna.model.obv.MapObverser;
import com.dwarfeng.dutil.basic.gui.awt.CommonIconLib;
import com.dwarfeng.dutil.basic.gui.awt.ImageSize;
import com.dwarfeng.dutil.basic.gui.awt.ImageUtil;
import com.dwarfeng.dutil.basic.gui.swing.JAdjustableBorderPanel;
import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.dutil.develop.i18n.I18nHandler;
import com.dwarfeng.projwiz.core.model.cm.SyncComponentModel;
import com.dwarfeng.projwiz.core.model.eum.ImageKey;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.model.struct.Editor;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.FileProcessor;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.ProjectFilePair;
import com.dwarfeng.projwiz.core.model.struct.ProjectProcessor;
import com.dwarfeng.projwiz.core.view.struct.GuiManager;
import com.dwarfeng.projwiz.core.view.struct.WindowSuppiler;

/**
 * 
 * @author DwArFeng
 * @since 0.0.2-alpha
 */
public class EditorMonitor extends ProjWizDialog implements WindowSuppiler {

	private static final long serialVersionUID = -251047540510364086L;

	private final JTable focusTable;
	private final JTable mapTable;
	private final JLabel label_1;
	private final JLabel label_2;
	private final Icon editorIcon;

	private SyncMapModel<Project, Editor> focusEditorModel;
	private SyncMapModel<ProjectFilePair, Editor> editorModel;
	private SyncComponentModel componentModel;

	private final DefaultTableModel focusTableModel = new DefaultTableModel() {

		private static final long serialVersionUID = -7798822512172189454L;

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getColumnCount() {
			return 2;
		};

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}

	};
	private final DefaultTableModel mapTableModel = new DefaultTableModel() {

		private static final long serialVersionUID = 1869913903423429378L;

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getColumnCount() {
			return 3;
		};

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};

	private final DefaultTableCellRenderer focusTableRenderer = new DefaultTableCellRenderer() {

		private static final long serialVersionUID = 7022803549412121282L;

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			switch (column) {
			case 0:
				renderProject(this, (Project) value);
				break;
			case 1:
				renderEditor(this, (Editor) value);
				break;
			default:
				throw new IllegalArgumentException("不合法的列号:" + column);
			}
			return this;
		};

	};
	private final DefaultTableCellRenderer mapTableRenderer = new DefaultTableCellRenderer() {

		private static final long serialVersionUID = -7541700616437793145L;

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			switch (column) {
			case 0:
				renderProject(this, (Project) value);
				break;
			case 1:
				renderFile(this, (Project) mapTableModel.getValueAt(row, 0), (File) value);
				break;
			case 2:
				renderEditor(this, (Editor) value);
				break;
			default:
				throw new IllegalArgumentException("不合法的列号:" + column);
			}
			return this;
		};

	};

	private final MapObverser<Project, Editor> focusEditorObverser = new MapAdapter<Project, Editor>() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireChanged(Project key, Editor oldValue, Editor newValue) {
			SwingUtil.invokeInEventQueue(() -> {
				focusTableModel.setValueAt(newValue, findRow(focusTableModel, key), 1);
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireCleared() {
			SwingUtil.invokeInEventQueue(() -> {
				while (focusTableModel.getRowCount() > 0) {
					focusTableModel.removeRow(0);
				}
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void firePut(Project key, Editor value) {
			SwingUtil.invokeInEventQueue(() -> {
				focusTableModel.addRow(new Object[] { key, value });
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireRemoved(Project key, Editor value) {
			SwingUtil.invokeInEventQueue(() -> {
				focusTableModel.removeRow(findRow(focusTableModel, key));
			});
		}

		private int findRow(DefaultTableModel tableModel, Project key) {
			for (int i = 0; i < tableModel.getRowCount(); i++) {
				if (Objects.equals(tableModel.getValueAt(i, 0), key)) {
					return i;
				}
			}
			throw new IllegalArgumentException("无法在表格模型中找到指定的值");
		}

	};
	private final MapObverser<ProjectFilePair, Editor> editorObverser = new MapAdapter<ProjectFilePair, Editor>() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireChanged(ProjectFilePair key, Editor oldValue, Editor newValue) {
			SwingUtil.invokeInEventQueue(() -> {
				mapTableModel.setValueAt(newValue, findRow(mapTableModel, key.getProject(), key.getFile()), 2);
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireCleared() {
			SwingUtil.invokeInEventQueue(() -> {
				while (focusTableModel.getRowCount() > 0) {
					mapTableModel.removeRow(0);
				}
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void firePut(ProjectFilePair key, Editor value) {
			SwingUtil.invokeInEventQueue(() -> {
				mapTableModel.addRow(new Object[] { key.getProject(), key.getFile(), value });
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireRemoved(ProjectFilePair key, Editor value) {
			SwingUtil.invokeInEventQueue(() -> {
				mapTableModel.removeRow(findRow(mapTableModel, key.getProject(), key.getFile()));
			});
		}

		private int findRow(DefaultTableModel tableModel, Project project, File file) {
			for (int i = 0; i < tableModel.getRowCount(); i++) {
				if (Objects.equals(tableModel.getValueAt(i, 0), project)
						&& Objects.equals(tableModel.getValueAt(i, 1), file)) {
					return i;
				}
			}
			throw new IllegalArgumentException("无法在表格模型中找到指定的值");
		}

	};

	private boolean disposeFlag = false;
	private final Lock disposeLock = new ReentrantLock();

	/**
	 * 新实例。
	 */
	public EditorMonitor() {
		this(null, null, null, null, null);
	}

	/**
	 * 
	 * @param guiManager
	 * @param i18nHandler
	 * @param focusEditorModel
	 * @param editorModel
	 * @param componentModel
	 */
	public EditorMonitor(GuiManager guiManager, I18nHandler i18nHandler, SyncMapModel<Project, Editor> focusEditorModel,
			SyncMapModel<ProjectFilePair, Editor> editorModel, SyncComponentModel componentModel) {
		super(guiManager, i18nHandler);

		editorIcon = new ImageIcon(ImageUtil.getInternalImage(ImageKey.EDITOR, ImageSize.ICON_SMALL));

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
		setTitle(label(LabelStringKey.EDITORDIA_1));
		setIconImage(ImageUtil.getInternalImage(ImageKey.ICON));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModalityType(ModalityType.MODELESS);
		setPreferredSize(new Dimension(600, 400));
		getContentPane().setLayout(new BorderLayout(0, 0));

		JAdjustableBorderPanel adjustableBorderPanel = new JAdjustableBorderPanel();
		adjustableBorderPanel.setSeperatorThickness(5);
		adjustableBorderPanel.setWestEnabled(true);
		adjustableBorderPanel.setEastVisible(true);
		getContentPane().add(adjustableBorderPanel, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		adjustableBorderPanel.add(panel, BorderLayout.WEST);
		panel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);

		focusTable = new JTable();
		focusTable.setFillsViewportHeight(true);
		focusTable.setModel(focusTableModel);
		focusTableModel.setColumnIdentifiers(
				new String[] { label(LabelStringKey.EDITORDIA_4), label(LabelStringKey.EDITORDIA_6) });
		for (int i = 0; i < focusTableModel.getColumnCount(); i++) {
			focusTable.getColumnModel().getColumn(i).setCellRenderer(focusTableRenderer);
		}
		focusTable.getTableHeader().setReorderingAllowed(false);
		scrollPane.setViewportView(focusTable);

		label_1 = new JLabel(label(LabelStringKey.EDITORDIA_2));
		panel.add(label_1, BorderLayout.NORTH);

		JPanel panel_1 = new JPanel();
		adjustableBorderPanel.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane_1 = new JScrollPane();
		panel_1.add(scrollPane_1, BorderLayout.CENTER);

		mapTable = new JTable();
		mapTable.setFillsViewportHeight(true);
		mapTable.setModel(mapTableModel);
		mapTableModel.setColumnIdentifiers(new String[] { label(LabelStringKey.EDITORDIA_4),
				label(LabelStringKey.EDITORDIA_5), label(LabelStringKey.EDITORDIA_6) });
		for (int i = 0; i < mapTableModel.getColumnCount(); i++) {
			mapTable.getColumnModel().getColumn(i).setCellRenderer(mapTableRenderer);
		}
		mapTable.getTableHeader().setReorderingAllowed(false);
		scrollPane_1.setViewportView(mapTable);

		label_2 = new JLabel(label(LabelStringKey.EDITORDIA_3));
		panel_1.add(label_2, BorderLayout.NORTH);

		pack();

		if (Objects.nonNull(focusEditorModel)) {
			focusEditorModel.addObverser(focusEditorObverser);
		}
		if (Objects.nonNull(editorModel)) {
			editorModel.addObverser(editorObverser);
		}

		this.focusEditorModel = focusEditorModel;
		this.editorModel = editorModel;
		this.componentModel = componentModel;

		syncFocusEditorModel();
		syncEditorModel();
		syncComponentModel();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		disposeLock.lock();
		try {
			if (Objects.nonNull(this.focusEditorModel)) {
				this.focusEditorModel.removeObverser(focusEditorObverser);
			}
			if (Objects.nonNull(this.editorModel)) {
				this.editorModel.removeObverser(editorObverser);
			}

			super.dispose();
		} finally {
			disposeLock.unlock();
		}
	}

	/**
	 * 返回面板中的组件模型。
	 * 
	 * @return 面板中的组件模型。
	 */
	public SyncComponentModel getComponentModel() {
		return componentModel;
	}

	/**
	 * @return the editorModel
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
	 * 设置面板文件中的组件模型。
	 * 
	 * @param componentModel
	 *            指定的组件模型。
	 */
	public void setComponentModel(SyncComponentModel componentModel) {
		this.componentModel = componentModel;
		syncComponentModel();
	}

	/**
	 * @param editorModel
	 *            the editorModel to set
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
			focusEditorModel.removeObverser(focusEditorObverser);
		}

		this.focusEditorModel = focusEditorModel;
		syncFocusEditorModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void refreshLabels() {
		setTitle(label(LabelStringKey.EDITORDIA_1));
		label_1.setText(label(LabelStringKey.EDITORDIA_2));
		label_2.setText(label(LabelStringKey.EDITORDIA_3));
		focusTableModel.setColumnIdentifiers(
				new String[] { label(LabelStringKey.EDITORDIA_4), label(LabelStringKey.EDITORDIA_6) });
		mapTableModel.setColumnIdentifiers(new String[] { label(LabelStringKey.EDITORDIA_4),
				label(LabelStringKey.EDITORDIA_5), label(LabelStringKey.EDITORDIA_6) });
	}

	private void renderEditor(JLabel label, Editor editor) {
		label.setText(null);
		label.setIcon(null);
		label.setToolTipText(null);

		label.setIcon(editorIcon);
		if (Objects.isNull(editor)) {
			label.setText("null");
			label.setToolTipText("null");
		} else {
			label.setText(editor.getTitle());
			label.setToolTipText(editor.getTitle());
		}

	}

	private void renderFile(JLabel label, Project project, File file) {
		label.setText(null);
		label.setIcon(null);
		label.setToolTipText(null);

		if (Objects.isNull(file)) {
			return;
		}

		label.setText(project.getFileName(file));
		label.setToolTipText(project.getFileName(file));
		Image image = null;
		if (Objects.nonNull(componentModel)) {
			FileProcessor processor = componentModel.get(file.getProcessorClass());
			if (Objects.nonNull(processor)) {
				image = processor.getFileIcon(file);
			}
		}
		if (Objects.isNull(image)) {
			label.setIcon(new ImageIcon(ImageUtil.getInternalImage(CommonIconLib.UNKNOWN_BLUE, ImageSize.ICON_SMALL)));
		} else {
			label.setIcon(new ImageIcon(ImageUtil.scaleImage(image, ImageSize.ICON_SMALL)));
		}
	}

	private void renderProject(JLabel label, Project project) {
		label.setText(null);
		label.setIcon(null);
		label.setToolTipText(null);

		if (Objects.isNull(project)) {
			return;
		}

		label.setText(project.getName());
		label.setToolTipText(project.getName());
		Image image = null;
		if (Objects.nonNull(componentModel)) {
			ProjectProcessor processor = componentModel.get(project.getProcessorClass());
			if (Objects.nonNull(processor)) {
				image = processor.getProjectIcon(project);
			}
		}
		if (Objects.isNull(image)) {
			label.setIcon(new ImageIcon(ImageUtil.getInternalImage(CommonIconLib.UNKNOWN_BLUE, ImageSize.ICON_SMALL)));
		} else {
			label.setIcon(new ImageIcon(ImageUtil.scaleImage(image, ImageSize.ICON_SMALL)));
		}
	}

	private void syncComponentModel() {
		mapTable.repaint();
		mapTable.repaint();
		focusTable.repaint();
	}

	private void syncEditorModel() {
		while (mapTableModel.getRowCount() > 0) {
			mapTableModel.removeRow(0);
		}

		if (Objects.isNull(editorModel)) {
			return;
		}

		editorModel.getLock().readLock().lock();
		try {
			editorModel.forEach((key, value) -> {
				mapTableModel.addRow(new Object[] { key.getProject(), key.getFile(), value });
			});
		} finally {
			editorModel.getLock().readLock().unlock();
		}
	}

	private void syncFocusEditorModel() {
		while (focusTableModel.getRowCount() > 0) {
			focusTableModel.removeRow(0);
		}

		if (Objects.isNull(focusEditorModel)) {
			return;
		}

		focusEditorModel.getLock().readLock().lock();
		try {
			focusEditorModel.forEach((key, value) -> {
				focusTableModel.addRow(new Object[] { key, value });
			});
		} finally {
			focusEditorModel.getLock().readLock().unlock();
		}
	}

}
