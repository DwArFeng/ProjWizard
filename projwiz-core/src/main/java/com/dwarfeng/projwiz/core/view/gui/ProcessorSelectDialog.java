package com.dwarfeng.projwiz.core.view.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.AbstractAction;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.dwarfeng.dutil.basic.cna.CollectionUtil;
import com.dwarfeng.dutil.basic.cna.model.SyncKeySetModel;
import com.dwarfeng.dutil.basic.cna.model.obv.SetAdapter;
import com.dwarfeng.dutil.basic.cna.model.obv.SetObverser;
import com.dwarfeng.dutil.basic.gui.awt.CommonIconLib;
import com.dwarfeng.dutil.basic.gui.awt.ImageSize;
import com.dwarfeng.dutil.basic.gui.awt.ImageUtil;
import com.dwarfeng.dutil.basic.gui.swing.MuaListModel;
import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.dutil.develop.i18n.I18nHandler;
import com.dwarfeng.projwiz.core.model.eum.DialogOption;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.model.struct.Processor;
import com.dwarfeng.projwiz.core.model.struct.ProcessorKeyComparator;
import com.dwarfeng.projwiz.core.view.struct.GuiManager;
import com.dwarfeng.projwiz.core.view.struct.JListMouseListener4Selection;
import com.dwarfeng.projwiz.core.view.struct.JListMouseMotionListener4Selection;
import com.dwarfeng.projwiz.core.view.struct.WindowSuppiler;

/**
 * 工程处理器选择面板。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public final class ProcessorSelectDialog<T extends Processor> extends ProjWizDialog implements WindowSuppiler {

	private static final long serialVersionUID = -3909625518055979076L;

	/**
	 * 处理器过滤器。
	 * 
	 * @author DwArFeng
	 * @since 0.0.1-alpha
	 */
	public static interface ProcessorFilter<T extends Processor> {

		/**
		 * 询问该过滤器是否接受指定的处理器。
		 * 
		 * @param processor
		 *            指定的处理器。
		 * @return 是否接受。
		 */
		public boolean accept(T processor);
	}

	private final JTextArea textArea;
	private final JList<T> list;
	private final JButton okButton;
	private final JButton cancelButton;

	private final Lock disposeLock = new ReentrantLock();
	private final Condition disposeCondition = disposeLock.newCondition();

	private SyncKeySetModel<String, T> processorModel;
	private ProcessorFilter<T> processorFilter;

	private final MuaListModel<T> listModel = new MuaListModel<>();
	private final ListCellRenderer<Object> listRenderer = new DefaultListCellRenderer() {

		private static final long serialVersionUID = -7069222384518497810L;

		@Override
		public java.awt.Component getListCellRendererComponent(javax.swing.JList<?> list, Object value, int index,
				boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			setHorizontalAlignment(JLabel.CENTER);
			setHorizontalTextPosition(JLabel.CENTER);
			setVerticalTextPosition(JLabel.BOTTOM);

			setText(null);
			if (Objects.nonNull(((Processor) value).getIcon())) {
				setIcon(new ImageIcon(ImageUtil.scaleImage(((Processor) value).getIcon(), ImageSize.ICON_MEDIUM)));
			} else {
				setIcon(new ImageIcon(ImageUtil.getInternalImage(CommonIconLib.UNKNOWN_BLUE, ImageSize.ICON_MEDIUM)));
			}
			setToolTipText(((Processor) value).getName());
			return this;
		};
	};
	private final SetObverser<T> listObverser = new SetAdapter<T>() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireAdded(T element) {
			SwingUtil.invokeInEventQueue(() -> {
				if (Objects.isNull(processorFilter) || processorFilter.accept(element)) {
					CollectionUtil.insertByOrder(listModel, element, new ProcessorKeyComparator());
				}
			});
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.dwarfeng.dutil.basic.cna.model.obv.SetAdapter#fireCleared()
		 */
		@Override
		public void fireCleared() {
			SwingUtil.invokeInEventQueue(() -> {
				listModel.clear();
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireRemoved(T element) {
			SwingUtil.invokeInEventQueue(() -> {
				listModel.remove(element);
			});
		}
	};

	private T currentProcessor;
	private boolean disposeFlag = false;
	private DialogOption option = DialogOption.CLOSED;

	/**
	 * 新实例。
	 */
	public ProcessorSelectDialog() {
		this(null, null, null, null, null);
	}

	/**
	 * 新实例。
	 * 
	 * @param processorModel
	 *            指定的工程处理器模型。
	 */
	public ProcessorSelectDialog(GuiManager guiManager, I18nHandler i18nHandler, Window owner,
			SyncKeySetModel<String, T> processorModel, ProcessorFilter<T> processorFilter) {
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
		setTitle(label(LabelStringKey.PROCSELDIA_1));
		setResizable(false);

		getContentPane().setPreferredSize(new Dimension(450, 300));
		getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 5));

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);
		scrollPane.setDoubleBuffered(true);
		scrollPane.setPreferredSize(new Dimension(40, 128));

		textArea = new JTextArea();
		textArea.setFocusTraversalKeysEnabled(false);
		textArea.setEditable(false);
		textArea.setDoubleBuffered(true);
		textArea.setPreferredSize(new Dimension(10, 10));
		scrollPane.setViewportView(textArea);

		JScrollPane scrollPane_1 = new JScrollPane();
		panel.add(scrollPane_1, BorderLayout.SOUTH);
		scrollPane_1.setPreferredSize(new Dimension(40, 80));
		scrollPane_1.setDoubleBuffered(true);
		scrollPane_1.getViewport().setBackground(new Color(UIManager.getColor("List.background").getRGB()));

		list = new JList<>();
		list.setVisibleRowCount(0);
		list.setDoubleBuffered(true);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					currentProcessor = list.getSelectedValue();
					if (Objects.nonNull(currentProcessor)) {
						textArea.setText(currentProcessor.getDescription());
					} else {
						textArea.setText(null);
					}
					list.requestFocus();
				}
			}
		});
		list.setModel(listModel);
		list.setCellRenderer(listRenderer);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addMouseMotionListener(new JListMouseMotionListener4Selection(list));
		list.addMouseListener(new JListMouseListener4Selection(list));
		list.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && Objects.nonNull(list.getSelectedValue())) {
					option = DialogOption.OK_YES;
					dispose();
				}
			}
		});
		list.requestFocus();
		scrollPane_1.setViewportView(list);

		JPanel panel_1 = new JPanel();
		FlowLayout fl_panel_1 = (FlowLayout) panel_1.getLayout();
		fl_panel_1.setAlignment(FlowLayout.RIGHT);
		getContentPane().add(panel_1, BorderLayout.SOUTH);

		okButton = new JButton(label(LabelStringKey.PROCSELDIA_2));
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				option = DialogOption.OK_YES;
				dispose();
			}
		});
		panel_1.add(okButton);

		cancelButton = new JButton(label(LabelStringKey.PROCSELDIA_3));
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				option = DialogOption.CANCEL;
				dispose();
			}
		});

		contentPane.registerKeyboardAction(new AbstractAction() {

			private static final long serialVersionUID = 694312027933589369L;

			@Override
			public void actionPerformed(ActionEvent e) {
				option = DialogOption.CANCEL;
				dispose();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		panel_1.add(cancelButton);

		getRootPane().setDefaultButton(okButton);

		pack();

		if (Objects.nonNull(processorModel)) {
			processorModel.addObverser(listObverser);
		}

		this.processorModel = processorModel;
		this.processorFilter = processorFilter;

		syncInit();
	}

	@Override
	public void dispose() {
		if (Objects.nonNull(processorModel)) {
			processorModel.removeObverser(listObverser);
		}
		super.dispose();
	}

	/**
	 * 获取该面板中当前的处理器。
	 * 
	 * @return 该面板中当前的处理器。
	 */
	public T getCurrentProcessor() {
		return currentProcessor;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getKey() {
		return this.getClass().toString();
	}

	/**
	 * 获取对话框的选项。
	 * 
	 * @return 对话框的选项。
	 */
	public DialogOption getOption() {
		return option;
	}

	/**
	 * 获取该面板中当前的文件过滤器。
	 * 
	 * @return 该面板中当前的文件过滤器。
	 */
	public ProcessorFilter<T> getProcessorFilter() {
		return processorFilter;
	}

	/**
	 * 获取该面板的工程处理器模型。
	 * 
	 * @return 该免肝的工程处理器模型。
	 */
	public SyncKeySetModel<String, T> getProcessorModel() {
		return processorModel;
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
	 * 设置该面板中当前的文件过滤器。
	 * 
	 * @param processorFilter
	 *            面板中当前的文件过滤器。
	 */
	public void setProcessorFilter(ProcessorFilter<T> processorFilter) {
		this.processorFilter = processorFilter;
		syncProcessorFiliter();
	}

	/**
	 * 设置该面板的工程处理器模型。
	 * 
	 * @param processorModel
	 */
	public void setProcessorModel(SyncKeySetModel<String, T> processorModel) {
		if (Objects.nonNull(this.processorModel)) {
			this.processorModel.removeObverser(listObverser);
		}

		if (Objects.nonNull(processorModel)) {
			processorModel.addObverser(listObverser);
		}

		this.processorModel = processorModel;

		syncProcessorModel();
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
		setTitle(label(LabelStringKey.PROCSELDIA_1));
		okButton.setText(label(LabelStringKey.PROCSELDIA_2));
		cancelButton.setText(label(LabelStringKey.PROCSELDIA_3));
	}

	private void syncProcessorFiliter() {
		listModel.clear();
		textArea.setText(null);

		if (Objects.isNull(this.processorModel)) {
			return;
		}

		Comparator<Processor> c = new ProcessorKeyComparator();

		this.processorModel.getLock().readLock().lock();
		try {
			this.processorModel.forEach(processor -> {
				if (Objects.isNull(processorFilter) || processorFilter.accept(processor)) {
					CollectionUtil.insertByOrder(listModel, processor, c);
				}
			});
		} finally {
			this.processorModel.getLock().readLock().unlock();
		}

		if (listModel.size() > 0) {
			list.getSelectionModel().setSelectionInterval(0, 0);
			list.getSelectionModel().setAnchorSelectionIndex(0);
			list.getSelectionModel().setLeadSelectionIndex(0);
		}

	}

	private void syncProcessorModel() {
		listModel.clear();
		textArea.setText(null);

		if (Objects.isNull(this.processorModel)) {
			return;
		}

		Comparator<Processor> c = new ProcessorKeyComparator();

		this.processorModel.getLock().readLock().lock();
		try {
			this.processorModel.forEach(processor -> {
				if (Objects.isNull(processorFilter) || processorFilter.accept(processor)) {
					CollectionUtil.insertByOrder(listModel, processor, c);
				}
			});
		} finally {
			this.processorModel.getLock().readLock().unlock();
		}

		if (listModel.size() > 0) {
			list.getSelectionModel().setSelectionInterval(0, 0);
			list.getSelectionModel().setAnchorSelectionIndex(0);
			list.getSelectionModel().setLeadSelectionIndex(0);
		}

	}

	private void syncInit() {
		listModel.clear();
		textArea.setText(null);

		if (Objects.isNull(this.processorModel)) {
			return;
		}

		Comparator<Processor> c = new ProcessorKeyComparator();

		this.processorModel.getLock().readLock().lock();
		try {
			this.processorModel.forEach(processor -> {
				if (Objects.isNull(processorFilter) || processorFilter.accept(processor)) {
					CollectionUtil.insertByOrder(listModel, processor, c);
				}
			});
		} finally {
			this.processorModel.getLock().readLock().unlock();
		}

		if (listModel.size() > 0) {
			list.getSelectionModel().setSelectionInterval(0, 0);
			list.getSelectionModel().setAnchorSelectionIndex(0);
			list.getSelectionModel().setLeadSelectionIndex(0);
		}
	}

}
