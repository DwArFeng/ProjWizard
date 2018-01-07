package com.dwarfeng.projwiz.core.view.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.dwarfeng.dutil.basic.cna.model.SyncMapModel;
import com.dwarfeng.dutil.basic.cna.model.obv.MapAdapter;
import com.dwarfeng.dutil.basic.cna.model.obv.MapObverser;
import com.dwarfeng.dutil.basic.gui.awt.ImageSize;
import com.dwarfeng.dutil.basic.gui.awt.ImageUtil;
import com.dwarfeng.dutil.basic.gui.swing.JAdjustableBorderPanel;
import com.dwarfeng.dutil.basic.gui.swing.MuaListModel;
import com.dwarfeng.dutil.develop.i18n.I18nHandler;
import com.dwarfeng.projwiz.core.model.eum.ImageKey;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.view.struct.GuiManager;
import com.dwarfeng.projwiz.core.view.struct.WindowSuppiler;

/**
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class IndicateMonitor extends ProjWizDialog implements WindowSuppiler {

	private static final long serialVersionUID = -2901348045933720874L;

	private final JLabel label_1;
	private final JLabel label_2;
	private final JList<Map.Entry<String, Project>> projectList;
	private final JList<Map.Entry<String, File>> fileList;
	private final Icon rendererIcon;

	private SyncMapModel<String, Project> projectIndicateModel;
	private SyncMapModel<String, File> fileIndicateModel;

	private final MuaListModel<Map.Entry<String, Project>> projectListModel = new MuaListModel<>();
	private final MuaListModel<Map.Entry<String, File>> fileListModel = new MuaListModel<>();

	private final DefaultListCellRenderer projectRenderer = new DefaultListCellRenderer() {

		private static final long serialVersionUID = -8269947338933350830L;

		@Override
		public java.awt.Component getListCellRendererComponent(javax.swing.JList<?> list, Object value, int index,
				boolean isSelected, boolean cellHasFocus) {
			@SuppressWarnings("unchecked")
			Map.Entry<String, Project> entry = (Map.Entry<String, Project>) value;
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			setIcon(rendererIcon);
			setText(String.format("%s - %s", entry.getKey(), entry.getValue().getName()));
			return this;
		};
	};

	private final DefaultListCellRenderer fileRenderer = new DefaultListCellRenderer() {

		private static final long serialVersionUID = 9156574575988740500L;

		@Override
		public java.awt.Component getListCellRendererComponent(javax.swing.JList<?> list, Object value, int index,
				boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			@SuppressWarnings("unchecked")
			Map.Entry<String, File> entry = (Map.Entry<String, File>) value;
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			setIcon(rendererIcon);
			setText(String.format("%s - %s", entry.getKey(), entry.getValue().getName()));
			return this;
		};
	};

	private final MapObverser<String, Project> projectIndicateModelObverser = new MapAdapter<String, Project>() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireCleared() {
			projectListModel.clear();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void firePut(String key, Project value) {
			projectListModel.add(new AbstractMap.SimpleEntry<>(key, value));
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireRemoved(String key, Project value) {
			projectListModel.remove(new AbstractMap.SimpleEntry<>(key, value));
		}

	};

	private final MapObverser<String, File> fileIndicateModelObverser = new MapAdapter<String, File>() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireCleared() {
			fileListModel.clear();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void firePut(String key, File value) {
			fileListModel.add(new AbstractMap.SimpleEntry<>(key, value));
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireRemoved(String key, File value) {
			fileListModel.remove(new AbstractMap.SimpleEntry<>(key, value));
		}

	};

	private boolean disposeFlag = false;
	private final Lock disposeLock = new ReentrantLock();

	/**
	 * 新实例。
	 */
	public IndicateMonitor() {
		this(null, null, null, null);
	}

	/**
	 * 
	 * @param guiManager
	 * @param i18nHandler
	 * @param projectIndicateModel
	 * @param fileIndicateModel
	 */
	public IndicateMonitor(GuiManager guiManager, I18nHandler i18nHandler,
			SyncMapModel<String, Project> projectIndicateModel, SyncMapModel<String, File> fileIndicateModel) {
		super(guiManager, i18nHandler);

		rendererIcon = new ImageIcon(ImageUtil.getInternalImage(ImageKey.INDICATOR, ImageSize.ICON_SMALL));

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
		setTitle(label(LabelStringKey.INDICATEDIA_3));
		setIconImage(ImageUtil.getInternalImage(ImageKey.ICON));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModalityType(ModalityType.MODELESS);
		setPreferredSize(new Dimension(600, 400));
		setLayout(new BorderLayout(0, 0));

		JAdjustableBorderPanel adjustableBorderPanel = new JAdjustableBorderPanel();
		adjustableBorderPanel.setSeperatorThickness(5);
		adjustableBorderPanel.setWestPreferredValue(300);
		adjustableBorderPanel.setWestEnabled(true);
		add(adjustableBorderPanel, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		adjustableBorderPanel.add(panel, BorderLayout.WEST);
		panel.setLayout(new BorderLayout(0, 0));

		label_1 = new JLabel(label(LabelStringKey.INDICATEDIA_1));
		panel.add(label_1, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);

		projectList = new JList<>();
		scrollPane.setViewportView(projectList);
		projectList.setModel(projectListModel);
		projectList.setCellRenderer(projectRenderer);

		JPanel panel_1 = new JPanel();
		adjustableBorderPanel.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));

		label_2 = new JLabel(label(LabelStringKey.INDICATEDIA_2));
		panel_1.add(label_2, BorderLayout.NORTH);

		JScrollPane scrollPane_1 = new JScrollPane();
		panel_1.add(scrollPane_1, BorderLayout.CENTER);

		fileList = new JList<>();
		scrollPane_1.setViewportView(fileList);
		fileList.setModel(fileListModel);
		fileList.setCellRenderer(fileRenderer);

		pack();

		if (Objects.nonNull(projectIndicateModel)) {
			projectIndicateModel.addObverser(projectIndicateModelObverser);
		}
		if (Objects.nonNull(fileIndicateModel)) {
			fileIndicateModel.addObverser(fileIndicateModelObverser);
		}

		this.projectIndicateModel = projectIndicateModel;
		this.fileIndicateModel = fileIndicateModel;

		syncProjectIndicateModel();
		syncFileIndicateModel();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		disposeLock.lock();
		try {
			if (Objects.nonNull(this.projectIndicateModel)) {
				this.projectIndicateModel.removeObverser(projectIndicateModelObverser);
			}
			if (Objects.nonNull(this.fileIndicateModel)) {
				this.fileIndicateModel.removeObverser(fileIndicateModelObverser);
			}

			super.dispose();
		} finally {
			disposeLock.unlock();
		}
	}

	/**
	 * 获取程序中的文件指示模型。
	 * 
	 * @return 程序中的文件指示模型。
	 */
	public SyncMapModel<String, File> getFileIndicateModel() {
		return fileIndicateModel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getKey() {
		return this.getClass().toString();
	}

	/**
	 * 获取程序中的工程指示模型。
	 * 
	 * @return 程序中的工程指示模型。
	 */
	public SyncMapModel<String, Project> getProjectIndicateModel() {
		return projectIndicateModel;
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
	 * 设置程序中的文件指示模型。
	 * 
	 * @param fileIndicateModel
	 *            程序中的文件指示模型。
	 */
	public void setFileIndicateModel(SyncMapModel<String, File> fileIndicateModel) {
		if (Objects.nonNull(this.fileIndicateModel)) {
			this.fileIndicateModel.removeObverser(fileIndicateModelObverser);
		}

		if (Objects.nonNull(fileIndicateModel)) {
			fileIndicateModel.addObverser(fileIndicateModelObverser);
		}

		this.fileIndicateModel = fileIndicateModel;
		syncFileIndicateModel();
	}

	/**
	 * 设置程序中的工程指示模型。
	 * 
	 * @param projectIndicateModel
	 *            指定的工程指示模型。
	 */
	public void setProjectIndicateModel(SyncMapModel<String, Project> projectIndicateModel) {
		if (Objects.nonNull(this.projectIndicateModel)) {
			this.projectIndicateModel.removeObverser(projectIndicateModelObverser);
		}

		if (Objects.nonNull(projectIndicateModel)) {
			projectIndicateModel.addObverser(projectIndicateModelObverser);
		}

		this.projectIndicateModel = projectIndicateModel;
		syncProjectIndicateModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void refreshLabels() {
		setTitle(label(LabelStringKey.INDICATEDIA_3));
		label_1.setText(label(LabelStringKey.INDICATEDIA_1));
		label_2.setText(label(LabelStringKey.INDICATEDIA_2));
	}

	private void syncFileIndicateModel() {
		fileListModel.clear();

		if (Objects.isNull(fileListModel)) {
			return;
		}

		fileIndicateModel.getLock().readLock().lock();
		try {
			for (Map.Entry<String, File> entry : fileIndicateModel.entrySet()) {
				fileListModel.add(entry);
			}
		} finally {
			fileIndicateModel.getLock().readLock().unlock();
		}
	}

	private void syncProjectIndicateModel() {
		projectListModel.clear();

		if (Objects.isNull(projectListModel)) {
			return;
		}

		projectIndicateModel.getLock().readLock().lock();
		try {
			for (Map.Entry<String, Project> entry : projectIndicateModel.entrySet()) {
				projectListModel.add(entry);
			}
		} finally {
			projectIndicateModel.getLock().readLock().unlock();
		}
	}

}
