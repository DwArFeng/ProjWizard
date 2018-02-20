package com.dwarfeng.projwiz.core.view.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.dutil.basic.num.NumberUtil;
import com.dwarfeng.dutil.basic.num.unit.DataSize;
import com.dwarfeng.dutil.develop.i18n.I18nHandler;
import com.dwarfeng.projwiz.core.model.cm.SyncComponentModel;
import com.dwarfeng.projwiz.core.model.cm.Tree.Path;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.model.obv.FileAdapter;
import com.dwarfeng.projwiz.core.model.obv.FileObverser;
import com.dwarfeng.projwiz.core.model.obv.ProjectAdapter;
import com.dwarfeng.projwiz.core.model.obv.ProjectObverser;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.FileProcessor;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.ProjectProcessor;
import com.dwarfeng.projwiz.core.model.struct.PropSuppiler;
import com.dwarfeng.projwiz.core.util.FileUtil;
import com.dwarfeng.projwiz.core.view.struct.GuiManager;
import com.dwarfeng.projwiz.core.view.struct.WindowSuppiler;

public class FilePropertiesDialog extends ProjWizDialog implements WindowSuppiler {

	private static final long serialVersionUID = -777552149357910518L;
	
	private final JButton cancelButton;
	private final JButton applyButton;
	private final JButton confirmButton;

	private final JTabbedPane tabbedPane;
	private final JLabel label1;
	private final JLabel label2;
	private final JLabel label3;
	private final JLabel label4;
	private final JLabel label5;
	private final JLabel label6;
	private final JLabel label7;
	private final JLabel errLabel;
	private final JTextField textField1;
	private final JTextField textField2;
	private final JTextField textField3;
	private final JTextField textField4;
	private final JTextField textField5;
	private final JTextField textField6;
	private final JTextField textField7;
	private final JPanel fromFileProcessor;
	private final JPanel fromProjectProcessor;

	private Project project;
	private File file;
	private SyncComponentModel componentModel;

	private final Lock disposeLock = new ReentrantLock();
	private final Condition disposeCondition = disposeLock.newCondition();

	private final ProjectObverser projectObverser = new ProjectAdapter() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireFileRemovedByDelete(Path<File> path, File parent, File file) {
			SwingUtil.invokeInEventQueue(() -> {
				fireRemoved(path, parent, file);
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireFileRemovedByMove(Path<File> path, File parent, File file) {
			SwingUtil.invokeInEventQueue(() -> {
				fireRemoved(path, parent, file);
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireFileRenamed(Path<File> path, File file, String oldName, String newName) {
			SwingUtil.invokeInEventQueue(() -> {
				if (!Objects.equals(file, FilePropertiesDialog.this.file))
					return;

				textField1.setText(label(LabelStringKey.FILEPROPDIA_13));
				textField2.setText(label(LabelStringKey.FILEPROPDIA_13));

				if (Objects.isNull(file))
					return;

				file.getLock().readLock().lock();
				try {
					textField1.setText(newName);
				} finally {
					file.getLock().readLock().unlock();
				}

				if (Objects.isNull(project))
					return;

				project.getLock().readLock().lock();
				try {
					if (project.getFileTree().contains(file)) {
						textField2.setText(FileUtil.getStdPath(project, file));
					}
				} finally {
					project.getLock().readLock().unlock();
				}
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireStopped() {
			SwingUtil.invokeInEventQueue(() -> {
				if (project.getFileTree().contains(file)) {
					setFile(null);
				}
				setProject(null);
			});
		}

		private void fireRemoved(Path<File> path, File parent, File file) {
			if (!Objects.equals(file, FilePropertiesDialog.this.file))
				return;

			setFile(null);
		}

	};

	private final FileObverser fileObverser = new FileAdapter() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireAccessTimeChanged(long oldValue, long newValue) {
			SwingUtil.invokeInEventQueue(() -> {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				textField5.setText(dateFormat.format(new Date(file.getAccessTime())));
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireCreateTimeChanged(long oldValue, long newValue) {
			SwingUtil.invokeInEventQueue(() -> {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				textField4.setText(dateFormat.format(new Date(file.getCreateTime())));
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireLengthChanged(long oldValue, long newValue) {
			SwingUtil.invokeInEventQueue(() -> {
				long length;
				if ((length = file.getLength()) < 0) {
					textField6.setText(label(LabelStringKey.FILEPROPDIA_13));
				} else {
					textField6.setText(byteFormat(length));
				}
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireModifyTimeChanged(long oldValue, long newValue) {
			SwingUtil.invokeInEventQueue(() -> {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				textField3.setText(dateFormat.format(new Date(file.getModifyTime())));
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireOccupiedSizeChanged(long oldValue, long newValue) {
			SwingUtil.invokeInEventQueue(() -> {
				long occupiedSize;
				if ((occupiedSize = file.getOccupiedSize()) < 0) {
					textField7.setText(label(LabelStringKey.FILEPROPDIA_13));
				} else {
					textField7.setText(byteFormat(occupiedSize));
				}
			});
		}

	};

	private PropSuppiler propSuppilerFromProject;
	private PropSuppiler propSuppilerFromFile;

	private boolean disposeFlag = false;

	private boolean projectFitFlag = false;

	/**
	 * 新实例。
	 */
	public FilePropertiesDialog() {
		this(null, null, null, null, null, null);
	}

	/**
	 * 新实例。
	 * 
	 * @param guiManager
	 *            指定的界面管理器。
	 * @param i18nHandler
	 *            指定的国际化处理器。
	 * @param owner
	 *            窗口的所有者。
	 * @param projectProcessorModel
	 *            指定的工程处理器模型。
	 * @param fileProcessorModel
	 *            指定的文件处理器模型。
	 * @param project
	 *            指定的工程。
	 * @param file
	 *            指定的文件。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public FilePropertiesDialog(GuiManager guiManager, I18nHandler i18nHandler, Window owner,
			SyncComponentModel componentModel, Project project, File file) {
		super(guiManager, i18nHandler, owner);

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosed(WindowEvent e) {
				disposeLock.lock();
				try {
					disposeFlag = true;
					disposeCondition.signalAll();
				} finally {
					disposeLock.unlock();
				}
			}
		});

		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle(label(LabelStringKey.FILEPROPDIA_1));
		setResizable(false);

		getContentPane().setPreferredSize(new Dimension(450, 300));
		getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		panel.add(tabbedPane, BorderLayout.CENTER);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		tabbedPane.addTab(label(LabelStringKey.FILEPROPDIA_2), null, panel_2, null);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[] { 0, 0, 0, 0, 0 };
		gbl_panel_2.rowHeights = new int[] { 0, 35, 35, 35, 35, 35, 0, 0 };
		gbl_panel_2.columnWeights = new double[] { 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel_2.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		panel_2.setLayout(gbl_panel_2);

		label1 = new JLabel(label(LabelStringKey.FILEPROPDIA_6));
		GridBagConstraints gbc_label1 = new GridBagConstraints();
		gbc_label1.anchor = GridBagConstraints.EAST;
		gbc_label1.insets = new Insets(0, 0, 5, 5);
		gbc_label1.gridx = 0;
		gbc_label1.gridy = 0;
		panel_2.add(label1, gbc_label1);

		textField1 = new JTextField();
		// textField1.setBorder(null);
		textField1.setEditable(false);
		GridBagConstraints gbc_textField1 = new GridBagConstraints();
		gbc_textField1.gridwidth = 3;
		gbc_textField1.insets = new Insets(0, 0, 5, 0);
		gbc_textField1.fill = GridBagConstraints.BOTH;
		gbc_textField1.gridx = 1;
		gbc_textField1.gridy = 0;
		panel_2.add(textField1, gbc_textField1);
		textField1.setColumns(10);

		label2 = new JLabel(label(LabelStringKey.FILEPROPDIA_7));
		GridBagConstraints gbc_label2 = new GridBagConstraints();
		gbc_label2.anchor = GridBagConstraints.EAST;
		gbc_label2.insets = new Insets(0, 0, 5, 5);
		gbc_label2.gridx = 0;
		gbc_label2.gridy = 1;
		panel_2.add(label2, gbc_label2);

		textField2 = new JTextField();
		// textField2.setBorder(null);
		textField2.setEditable(false);
		GridBagConstraints gbc_textField2 = new GridBagConstraints();
		gbc_textField2.gridwidth = 3;
		gbc_textField2.insets = new Insets(0, 0, 5, 0);
		gbc_textField2.fill = GridBagConstraints.BOTH;
		gbc_textField2.gridx = 1;
		gbc_textField2.gridy = 1;
		panel_2.add(textField2, gbc_textField2);
		textField2.setColumns(10);

		label3 = new JLabel(label(LabelStringKey.FILEPROPDIA_8));
		GridBagConstraints gbc_label3 = new GridBagConstraints();
		gbc_label3.anchor = GridBagConstraints.EAST;
		gbc_label3.insets = new Insets(0, 0, 5, 5);
		gbc_label3.gridx = 0;
		gbc_label3.gridy = 2;
		panel_2.add(label3, gbc_label3);

		textField3 = new JTextField();
		// textField3.setBorder(null);
		textField3.setEditable(false);
		GridBagConstraints gbc_textField3 = new GridBagConstraints();
		gbc_textField3.insets = new Insets(0, 0, 5, 5);
		gbc_textField3.fill = GridBagConstraints.BOTH;
		gbc_textField3.gridx = 1;
		gbc_textField3.gridy = 2;
		panel_2.add(textField3, gbc_textField3);
		textField3.setColumns(10);

		label4 = new JLabel(label(LabelStringKey.FILEPROPDIA_9));
		GridBagConstraints gbc_label4 = new GridBagConstraints();
		gbc_label4.anchor = GridBagConstraints.EAST;
		gbc_label4.insets = new Insets(0, 0, 5, 5);
		gbc_label4.gridx = 0;
		gbc_label4.gridy = 3;
		panel_2.add(label4, gbc_label4);

		textField4 = new JTextField();
		// textField4.setBorder(null);
		textField4.setEditable(false);
		GridBagConstraints gbc_textField4 = new GridBagConstraints();
		gbc_textField4.insets = new Insets(0, 0, 5, 5);
		gbc_textField4.fill = GridBagConstraints.BOTH;
		gbc_textField4.gridx = 1;
		gbc_textField4.gridy = 3;
		panel_2.add(textField4, gbc_textField4);
		textField4.setColumns(10);

		label5 = new JLabel(label(LabelStringKey.FILEPROPDIA_10));
		GridBagConstraints gbc_label5 = new GridBagConstraints();
		gbc_label5.anchor = GridBagConstraints.EAST;
		gbc_label5.insets = new Insets(0, 0, 5, 5);
		gbc_label5.gridx = 0;
		gbc_label5.gridy = 4;
		panel_2.add(label5, gbc_label5);

		textField5 = new JTextField();
		// textField5.setBorder(null);
		textField5.setEditable(false);
		GridBagConstraints gbc_textField5 = new GridBagConstraints();
		gbc_textField5.insets = new Insets(0, 0, 5, 5);
		gbc_textField5.fill = GridBagConstraints.BOTH;
		gbc_textField5.gridx = 1;
		gbc_textField5.gridy = 4;
		panel_2.add(textField5, gbc_textField5);
		textField5.setColumns(10);

		label6 = new JLabel(label(LabelStringKey.FILEPROPDIA_11));
		GridBagConstraints gbc_label6 = new GridBagConstraints();
		gbc_label6.anchor = GridBagConstraints.EAST;
		gbc_label6.insets = new Insets(0, 0, 5, 5);
		gbc_label6.gridx = 0;
		gbc_label6.gridy = 5;
		panel_2.add(label6, gbc_label6);

		textField6 = new JTextField();
		// textField6.setBorder(null);
		textField6.setEditable(false);
		GridBagConstraints gbc_textField6 = new GridBagConstraints();
		gbc_textField6.insets = new Insets(0, 0, 5, 5);
		gbc_textField6.fill = GridBagConstraints.BOTH;
		gbc_textField6.gridx = 1;
		gbc_textField6.gridy = 5;
		panel_2.add(textField6, gbc_textField6);
		textField6.setColumns(10);

		label7 = new JLabel(label(LabelStringKey.FILEPROPDIA_12));
		GridBagConstraints gbc_label7 = new GridBagConstraints();
		gbc_label7.anchor = GridBagConstraints.EAST;
		gbc_label7.insets = new Insets(0, 0, 5, 5);
		gbc_label7.gridx = 2;
		gbc_label7.gridy = 5;
		panel_2.add(label7, gbc_label7);

		textField7 = new JTextField();
		// textField7.setBorder(null);
		textField7.setEditable(false);
		GridBagConstraints gbc_textField7 = new GridBagConstraints();
		gbc_textField7.insets = new Insets(0, 0, 5, 0);
		gbc_textField7.fill = GridBagConstraints.BOTH;
		gbc_textField7.gridx = 3;
		gbc_textField7.gridy = 5;
		panel_2.add(textField7, gbc_textField7);
		textField7.setColumns(10);

		fromProjectProcessor = new JPanel();
		fromProjectProcessor.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		tabbedPane.addTab(label(LabelStringKey.FILEPROPDIA_3), null, fromProjectProcessor, null);
		fromProjectProcessor.setLayout(new BorderLayout(0, 0));

		fromFileProcessor = new JPanel();
		fromFileProcessor.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		tabbedPane.addTab(label(LabelStringKey.FILEPROPDIA_4), null, fromFileProcessor, null);
		fromFileProcessor.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.SOUTH);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[] { 393, 52, 0 };
		gbl_panel_1.rowHeights = new int[] { 30, 0, 0 };
		gbl_panel_1.columnWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		gbl_panel_1.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		panel_1.setLayout(gbl_panel_1);

		errLabel = new JLabel();
		GridBagConstraints gbc_errLabel = new GridBagConstraints();
		gbc_errLabel.anchor = GridBagConstraints.WEST;
		gbc_errLabel.insets = new Insets(0, 0, 5, 5);
		gbc_errLabel.gridx = 0;
		gbc_errLabel.gridy = 0;
		panel_1.add(errLabel, gbc_errLabel);

		JPanel panel_3 = new JPanel();
		FlowLayout fl_panel_3 = (FlowLayout) panel_3.getLayout();
		fl_panel_3.setAlignment(FlowLayout.RIGHT);
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.insets = new Insets(0, 0, 5, 0);
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 1;
		gbc_panel_3.gridy = 0;
		panel_1.add(panel_3, gbc_panel_3);

		confirmButton = new JButton(label(LabelStringKey.FILEPROPDIA_14));
		confirmButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (Objects.nonNull(propSuppilerFromFile)) {
					propSuppilerFromFile.fireApplied();
				}
				if (Objects.nonNull(propSuppilerFromProject)) {
					propSuppilerFromProject.fireApplied();
				}

				dispose();
			}
		});
		panel_3.add(confirmButton);

		cancelButton = new JButton(label(LabelStringKey.FILEPROPDIA_5));
		panel_3.add(cancelButton);
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		getRootPane().setDefaultButton(confirmButton);

		applyButton = new JButton(label(LabelStringKey.FILEPROPDIA_15));
		applyButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (Objects.nonNull(propSuppilerFromFile)) {
					propSuppilerFromFile.fireApplied();
				}
				if (Objects.nonNull(propSuppilerFromProject)) {
					propSuppilerFromProject.fireApplied();
				}
			}
		});
		panel_3.add(applyButton);

		contentPane.registerKeyboardAction(e -> {
			dispose();
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

		pack();

		if (Objects.nonNull(project)) {
			project.addObverser(projectObverser);
		}
		if (Objects.nonNull(file)) {
			file.addObverser(fileObverser);
		}

		this.componentModel = componentModel;
		this.project = project;
		this.file = file;

		syncModel();

	}

	@Override
	public void dispose() {
		if (Objects.nonNull(project)) {
			project.removeObverser(projectObverser);
		}
		if (Objects.nonNull(file)) {
			file.removeObverser(fileObverser);
		}

		super.dispose();
	}

	/**
	 * 获取当前对话框的组件模型。
	 * 
	 * @return 当前对话框的组件模型。
	 */
	public SyncComponentModel getComponentModel() {
		return componentModel;
	}

	/**
	 * 获取面板中的文件。
	 * 
	 * @return 面板中的文件。
	 */
	public File getFile() {
		return file;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getKey() {
		return this.getClass().toString();
	}

	/**
	 * 获取当前的工程。
	 * 
	 * @return 当前的工程。
	 */
	public Project getProject() {
		return project;
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
	 * 设置当前对话框的组件模型。
	 * 
	 * @param componentModel
	 *            指定的组件模型。
	 */
	public void setComponentModel(SyncComponentModel componentModel) {
		this.componentModel = componentModel;
		syncModel();
	}

	/**
	 * 设置面板中的文件。
	 * 
	 * @param file
	 *            指定的文件。
	 */
	public void setFile(File file) {
		if (Objects.nonNull(this.file)) {
			this.file.removeObverser(fileObverser);
		}

		if (Objects.nonNull(file)) {
			file.addObverser(fileObverser);
		}

		this.file = file;

		syncModel();
	}

	/**
	 * 设置当前的工程。
	 * 
	 * @param project
	 *            指定的工程。
	 */
	public void setProject(Project project) {
		if (Objects.nonNull(this.project)) {
			this.project.removeObverser(projectObverser);
		}

		if (Objects.nonNull(project)) {
			project.addObverser(projectObverser);
		}

		this.project = project;

		syncModel();
	}

	/**
	 * 等待该窗口释放。
	 * 
	 * @throws InterruptedException
	 *             等待过程被外部中断。
	 */
	public void waitDispose() throws InterruptedException {
		disposeLock.lock();
		try {
			while (!disposeFlag)
				disposeCondition.await();
		} finally {
			disposeLock.unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void refreshLabels() {
		setTitle(label(LabelStringKey.FILEPROPDIA_1));
		tabbedPane.setTitleAt(0, label(LabelStringKey.FILEPROPDIA_2));
		tabbedPane.setTitleAt(1, label(LabelStringKey.FILEPROPDIA_3));
		tabbedPane.setTitleAt(2, label(LabelStringKey.FILEPROPDIA_4));

		confirmButton.setText(label(LabelStringKey.FILEPROPDIA_14));
		cancelButton.setText(label(LabelStringKey.FILEPROPDIA_5));
		applyButton.setText(label(LabelStringKey.FILEPROPDIA_15));

		label1.setText(label(LabelStringKey.FILEPROPDIA_6));
		label2.setText(label(LabelStringKey.FILEPROPDIA_7));
		label3.setText(label(LabelStringKey.FILEPROPDIA_8));
		label4.setText(label(LabelStringKey.FILEPROPDIA_9));
		label5.setText(label(LabelStringKey.FILEPROPDIA_10));
		label6.setText(label(LabelStringKey.FILEPROPDIA_11));
		label7.setText(label(LabelStringKey.FILEPROPDIA_12));
	}

	private String byteFormat(long length) {
		double lengthKIB = NumberUtil.unitTrans(length, DataSize.BYTE, DataSize.KIB).doubleValue();
		if (lengthKIB < 0.9) {
			return String.format("%d b", length);
		}
		double lengthMIB = NumberUtil.unitTrans(length, DataSize.BYTE, DataSize.MIB).doubleValue();
		if (lengthMIB < 0.9) {
			return String.format("%.2f KIB", lengthKIB);
		}
		double lengthGIB = NumberUtil.unitTrans(length, DataSize.BYTE, DataSize.GIB).doubleValue();
		if (lengthGIB < 0.9) {
			return String.format("%.2f MIB", lengthMIB);
		}
		double lengthTIB = NumberUtil.unitTrans(length, DataSize.BYTE, DataSize.TIB).doubleValue();
		if (lengthGIB < 0.9) {
			return String.format("%.2f GIB", lengthGIB);
		}
		return String.format("%.2f TIB", lengthTIB);
	}

	private void syncModel() {
		textField1.setText(label(LabelStringKey.FILEPROPDIA_13));
		textField2.setText(label(LabelStringKey.FILEPROPDIA_13));
		textField3.setText(label(LabelStringKey.FILEPROPDIA_13));
		textField4.setText(label(LabelStringKey.FILEPROPDIA_13));
		textField5.setText(label(LabelStringKey.FILEPROPDIA_13));
		textField6.setText(label(LabelStringKey.FILEPROPDIA_13));
		textField7.setText(label(LabelStringKey.FILEPROPDIA_13));
		tabbedPane.setSelectedIndex(0);
		tabbedPane.setEnabledAt(1, false);
		fromProjectProcessor.removeAll();
		fromProjectProcessor.repaint();
		projectFitFlag = false;
		tabbedPane.setEnabledAt(2, false);
		fromFileProcessor.removeAll();
		fromFileProcessor.repaint();

		if (Objects.isNull(file))
			return;

		file.getLock().readLock().lock();
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			textField1.setText(file.getName());
			textField3.setText(dateFormat.format(new Date(file.getModifyTime())));
			textField4.setText(dateFormat.format(new Date(file.getCreateTime())));
			textField5.setText(dateFormat.format(new Date(file.getAccessTime())));
			long length;
			long occupiedSize;
			if ((length = file.getLength()) < 0) {
				textField6.setText(label(LabelStringKey.FILEPROPDIA_13));
			} else {
				textField6.setText(byteFormat(length));
			}

			if ((occupiedSize = file.getOccupiedSize()) < 0) {
				textField7.setText(label(LabelStringKey.FILEPROPDIA_13));
			} else {
				textField7.setText(byteFormat(occupiedSize));
			}

		} finally {
			file.getLock().readLock().unlock();
		}

		if (Objects.isNull(project))
			return;

		project.getLock().readLock().lock();
		try {
			if (project.getFileTree().contains(file)) {
				textField2.setText(FileUtil.getStdPath(project, file));
				projectFitFlag = true;
			}
		} finally {
			project.getLock().readLock().unlock();
		}

		if (Objects.isNull(componentModel) || Objects.isNull(project) || !projectFitFlag)
			return;

		componentModel.getLock().readLock().lock();
		try {
			ProjectProcessor processor = componentModel.getAll(ProjectProcessor.class).get(project.getComponentKey());
			if (Objects.nonNull(processor)
					&& Objects.nonNull((propSuppilerFromProject = processor.getFilePropSuppiler(file)))) {
				Component component = null;
				if (Objects.nonNull(component = propSuppilerFromProject.getComponent())) {
					fromProjectProcessor.add(component, BorderLayout.CENTER);
				}
				fromProjectProcessor.repaint();
				tabbedPane.setEnabledAt(1, true);
			}

		} finally {
			componentModel.getLock().readLock().unlock();
		}

		componentModel.getLock().readLock().lock();
		try {
			FileProcessor processor = componentModel.getAll(FileProcessor.class).get(file.getComponentKey());
			if (Objects.nonNull(processor)
					&& Objects.nonNull((propSuppilerFromFile = processor.getPropSuppiler(file)))) {
				Component component = null;
				if (Objects.nonNull(component = propSuppilerFromFile.getComponent())) {
					fromFileProcessor.add(component, BorderLayout.CENTER);
				}
				fromFileProcessor.repaint();
				tabbedPane.setEnabledAt(2, true);
			}

		} finally {
			componentModel.getLock().readLock().unlock();
		}

	}

}
