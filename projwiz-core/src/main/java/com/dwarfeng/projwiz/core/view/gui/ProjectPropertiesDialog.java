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
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import com.dwarfeng.dutil.basic.cna.model.SyncKeySetModel;
import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.dutil.develop.i18n.I18nHandler;
import com.dwarfeng.projwiz.core.model.cm.Tree.Path;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.model.obv.ProjectAdapter;
import com.dwarfeng.projwiz.core.model.obv.ProjectObverser;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.ProjectProcessor;
import com.dwarfeng.projwiz.core.model.struct.PropSuppiler;
import com.dwarfeng.projwiz.core.view.struct.GuiManager;
import com.dwarfeng.projwiz.core.view.struct.WindowSuppiler;

public class ProjectPropertiesDialog extends ProjWizDialog implements WindowSuppiler {

	private static final long serialVersionUID = -9110138970715746022L;

	private final JButton applyButton;
	private final JButton cancelButton;
	private final JButton confirmButton;

	private final JTabbedPane tabbedPane;
	private final JLabel label1;
	private final JLabel label2;
	private final JLabel label5;
	private final JLabel errLabel;
	private final JTextField textField1;
	private final JTextField textField2;
	private final JTextField textField5;
	private final JPanel fromProjectProcessor;

	private Project project;
	private SyncKeySetModel<String, ProjectProcessor> projectProcessorModel;

	private final Lock disposeLock = new ReentrantLock();
	private final Condition disposeCondition = disposeLock.newCondition();

	private final ProjectObverser projectObverser = new ProjectAdapter() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireFileAddedByCopy(Path<File> path, File parent, File file) {
			SwingUtil.invokeInEventQueue(() -> {
				fireAdded(path, parent, file);
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireFileAddedByMove(Path<File> path, File parent, File file) {
			SwingUtil.invokeInEventQueue(() -> {
				fireAdded(path, parent, file);
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireFileAddedByNew(Path<File> path, File parent, File file) {
			SwingUtil.invokeInEventQueue(() -> {
				fireAdded(path, parent, file);
			});
		}

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
		public void fireStopped() {
			SwingUtil.invokeInEventQueue(() -> {
				setProject(null);
			});
		}

		private void fireAdded(Path<File> path, File parent, File file) {
			fileSize += 1;
			textField5.setText(Integer.toString(fileSize));
		}

		private void fireRemoved(Path<File> path, File parent, File file) {
			fileSize -= 1;
			textField5.setText(Integer.toString(fileSize));

		}

	};

	private PropSuppiler propSuppiler;

	private int fileSize = 0;
	private boolean disposeFlag = false;
	private JPanel panel_3;

	/**
	 * 新实例。
	 */
	public ProjectPropertiesDialog() {
		this(null, null, null, null, null);
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
	public ProjectPropertiesDialog(GuiManager guiManager, I18nHandler i18nHandler, Window owner,
			SyncKeySetModel<String, ProjectProcessor> projectProcessorModel, Project project) {
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
		setTitle(label(LabelStringKey.PROJPROPDIA_1));
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
		tabbedPane.addTab(label(LabelStringKey.PROJPROPDIA_2), null, panel_2, null);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[] { 0, 140, 0 };
		gbl_panel_2.rowHeights = new int[] { 0, 35, 35, 35, 35, 35, 0, 0 };
		gbl_panel_2.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel_2.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		panel_2.setLayout(gbl_panel_2);

		label1 = new JLabel(label(LabelStringKey.PROJPROPDIA_5));
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
		gbc_textField1.insets = new Insets(0, 0, 5, 0);
		gbc_textField1.fill = GridBagConstraints.BOTH;
		gbc_textField1.gridx = 1;
		gbc_textField1.gridy = 0;
		panel_2.add(textField1, gbc_textField1);
		textField1.setColumns(10);

		label2 = new JLabel(label(LabelStringKey.PROJPROPDIA_6));
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
		gbc_textField2.insets = new Insets(0, 0, 5, 0);
		gbc_textField2.fill = GridBagConstraints.BOTH;
		gbc_textField2.gridx = 1;
		gbc_textField2.gridy = 1;
		panel_2.add(textField2, gbc_textField2);
		textField2.setColumns(10);

		label5 = new JLabel(label(LabelStringKey.PROJPROPDIA_7));
		GridBagConstraints gbc_label5 = new GridBagConstraints();
		gbc_label5.anchor = GridBagConstraints.EAST;
		gbc_label5.insets = new Insets(0, 0, 5, 5);
		gbc_label5.gridx = 0;
		gbc_label5.gridy = 2;
		panel_2.add(label5, gbc_label5);

		textField5 = new JTextField();
		textField5.setHorizontalAlignment(SwingConstants.TRAILING);
		// textField5.setBorder(null);
		textField5.setEditable(false);
		GridBagConstraints gbc_textField5 = new GridBagConstraints();
		gbc_textField5.insets = new Insets(0, 0, 5, 0);
		gbc_textField5.fill = GridBagConstraints.BOTH;
		gbc_textField5.gridx = 1;
		gbc_textField5.gridy = 2;
		panel_2.add(textField5, gbc_textField5);
		textField5.setColumns(10);

		fromProjectProcessor = new JPanel();
		fromProjectProcessor.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		tabbedPane.addTab(label(LabelStringKey.PROJPROPDIA_3), null, fromProjectProcessor, null);
		fromProjectProcessor.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.SOUTH);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[] { 137, 0, 0 };
		gbl_panel_1.rowHeights = new int[] { 30, 0 };
		gbl_panel_1.columnWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		gbl_panel_1.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel_1.setLayout(gbl_panel_1);

		errLabel = new JLabel();
		GridBagConstraints gbc_errLabel = new GridBagConstraints();
		gbc_errLabel.anchor = GridBagConstraints.WEST;
		gbc_errLabel.insets = new Insets(0, 0, 0, 5);
		gbc_errLabel.gridx = 0;
		gbc_errLabel.gridy = 0;
		panel_1.add(errLabel, gbc_errLabel);

		panel_3 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_3.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 1;
		gbc_panel_3.gridy = 0;
		panel_1.add(panel_3, gbc_panel_3);

		confirmButton = new JButton(label(LabelStringKey.PROJPROPDIA_8));
		confirmButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (Objects.nonNull(propSuppiler)) {
					propSuppiler.fireConfirmed();
				}
				dispose();
			}
		});
		panel_3.add(confirmButton);

		cancelButton = new JButton(label(LabelStringKey.PROJPROPDIA_4));
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				propSuppiler.fireCanceled();
				dispose();
			}
		});
		panel_3.add(cancelButton);

		applyButton = new JButton(label(LabelStringKey.PROJPROPDIA_9));
		applyButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (Objects.nonNull(propSuppiler)) {
					propSuppiler.fireApplied();
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

		this.projectProcessorModel = projectProcessorModel;
		this.project = project;

		syncModel();

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
	 * 获取当前的工程处理器模型。
	 * 
	 * @return 当前的工程处理器模型。
	 */
	public SyncKeySetModel<String, ProjectProcessor> getProjectProcessorModel() {
		return projectProcessorModel;
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
	 * 设置当前的工程处理器模型。
	 * 
	 * @param projectProcessorModel
	 *            指定的工程处理器模型。
	 */
	public void setProjectProcessorModel(SyncKeySetModel<String, ProjectProcessor> projectProcessorModel) {
		this.projectProcessorModel = projectProcessorModel;

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
		setTitle(label(LabelStringKey.PROJPROPDIA_1));
		tabbedPane.setTitleAt(0, label(LabelStringKey.PROJPROPDIA_2));
		tabbedPane.setTitleAt(1, label(LabelStringKey.PROJPROPDIA_3));

		confirmButton.setText(label(LabelStringKey.PROJPROPDIA_8));
		cancelButton.setText(label(LabelStringKey.PROJPROPDIA_4));

		label1.setText(label(LabelStringKey.PROJPROPDIA_5));
		label2.setText(label(LabelStringKey.PROJPROPDIA_6));
		label5.setText(label(LabelStringKey.PROJPROPDIA_7));
	}

	// private String byteFormat(long length) {
	// double lengthKIB = NumberUtil.unitTrans(length, DataSize.BYTE,
	// DataSize.KIB).doubleValue();
	// if (lengthKIB < 0.9) {
	// return String.format("%d b", length);
	// }
	// double lengthMIB = NumberUtil.unitTrans(length, DataSize.BYTE,
	// DataSize.MIB).doubleValue();
	// if (lengthMIB < 0.9) {
	// return String.format("%.2f KIB", lengthKIB);
	// }
	// double lengthGIB = NumberUtil.unitTrans(length, DataSize.BYTE,
	// DataSize.GIB).doubleValue();
	// if (lengthGIB < 0.9) {
	// return String.format("%.2f MIB", lengthMIB);
	// }
	// double lengthTIB = NumberUtil.unitTrans(length, DataSize.BYTE,
	// DataSize.TIB).doubleValue();
	// if (lengthGIB < 0.9) {
	// return String.format("%.2f GIB", lengthGIB);
	// }
	// return String.format("%.2f TIB", lengthTIB);
	// }

	private void syncModel() {
		textField1.setText(label(LabelStringKey.FILEPROPDIA_13));
		textField2.setText(label(LabelStringKey.FILEPROPDIA_13));
		textField5.setText(label(LabelStringKey.FILEPROPDIA_13));
		tabbedPane.setSelectedIndex(0);
		tabbedPane.setEnabledAt(1, false);
		fromProjectProcessor.removeAll();
		fromProjectProcessor.repaint();

		if (Objects.isNull(project))
			return;

		project.getLock().readLock().lock();
		try {
			textField1.setText(project.getName());
			textField2.setText(project.getFileTree().getRoot().getName());
			fileSize = project.getFileTree().size();
			textField5.setText(Integer.toString(fileSize));
		} finally {
			project.getLock().readLock().unlock();
		}

		if (Objects.isNull(projectProcessorModel) || Objects.isNull(project))
			return;

		projectProcessorModel.getLock().readLock().lock();
		try {
			ProjectProcessor processor = projectProcessorModel.get(project.getRegisterKey());
			if (Objects.nonNull(processor)
					&& Objects.nonNull((propSuppiler = processor.getProjectPropSuppiler(project)))) {
				Component component = null;
				if (Objects.nonNull(component = propSuppiler.getComponent())) {
					fromProjectProcessor.add(component, BorderLayout.CENTER);
				}
				fromProjectProcessor.repaint();
				tabbedPane.setEnabledAt(1, true);
			}

		} finally {
			projectProcessorModel.getLock().readLock().unlock();
		}

	}
}
