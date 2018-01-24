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
import com.dwarfeng.dutil.basic.cna.model.obv.SetAdapter;
import com.dwarfeng.dutil.basic.cna.model.obv.SetObverser;
import com.dwarfeng.dutil.basic.gui.awt.CommonIconLib;
import com.dwarfeng.dutil.basic.gui.awt.ImageSize;
import com.dwarfeng.dutil.basic.gui.awt.ImageUtil;
import com.dwarfeng.dutil.basic.gui.swing.MuaListModel;
import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.dutil.basic.prog.Filter;
import com.dwarfeng.dutil.develop.i18n.I18nHandler;
import com.dwarfeng.projwiz.core.model.cm.SyncComponentModel;
import com.dwarfeng.projwiz.core.model.eum.DialogOption;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.model.struct.Component;
import com.dwarfeng.projwiz.core.model.struct.ComponentKeyComparator;
import com.dwarfeng.projwiz.core.view.struct.GuiManager;
import com.dwarfeng.projwiz.core.view.struct.JListMouseListener4Selection;
import com.dwarfeng.projwiz.core.view.struct.JListMouseMotionListener4Selection;
import com.dwarfeng.projwiz.core.view.struct.WindowSuppiler;

/**
 * 组件处理器选择面板。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public final class ComponentSelectDialog extends ProjWizDialog implements WindowSuppiler {

	private static final long serialVersionUID = 1481629325183704700L;

	private final JTextArea textArea;
	private final JList<Component> list;
	private final JButton okButton;
	private final JButton cancelButton;

	private final Lock disposeLock = new ReentrantLock();
	private final Condition disposeCondition = disposeLock.newCondition();

	private SyncComponentModel componentModel;
	private Filter<Component> filter;

	private final MuaListModel<Component> listModel = new MuaListModel<>();
	private final ListCellRenderer<Object> listRenderer = new DefaultListCellRenderer() {

		private static final long serialVersionUID = 3104667765050182488L;

		@Override
		public java.awt.Component getListCellRendererComponent(javax.swing.JList<?> list, Object value, int index,
				boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			setHorizontalAlignment(JLabel.CENTER);
			setHorizontalTextPosition(JLabel.CENTER);
			setVerticalTextPosition(JLabel.BOTTOM);

			setText(null);
			if (Objects.nonNull(((Component) value).getIcon())) {
				setIcon(new ImageIcon(ImageUtil.scaleImage(((Component) value).getIcon(), ImageSize.ICON_MEDIUM)));
			} else {
				setIcon(new ImageIcon(ImageUtil.getInternalImage(CommonIconLib.UNKNOWN_BLUE, ImageSize.ICON_MEDIUM)));
			}
			setToolTipText(((Component) value).getName());
			return this;
		};
	};
	private final SetObverser<Component> listObverser = new SetAdapter<Component>() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireAdded(Component element) {
			SwingUtil.invokeInEventQueue(() -> {
				if (Objects.isNull(filter) || filter.accept(element)) {
					CollectionUtil.insertByOrder(listModel, element, new ComponentKeyComparator());
				}
			});
		}

		/**
		 * {@inheritDoc}
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
		public void fireRemoved(Component element) {
			SwingUtil.invokeInEventQueue(() -> {
				listModel.remove(element);
			});
		}
	};

	private Component currentComponent;
	private boolean disposeFlag = false;
	private DialogOption option = DialogOption.CLOSED;

	/**
	 * 新实例。
	 */
	public ComponentSelectDialog() {
		this(null, null, null, null, null);
	}

	/**
	 * 新实例。
	 * 
	 * @param guiManager
	 * @param i18nHandler
	 * @param owner
	 * @param componentModel
	 * @param filter
	 */
	public ComponentSelectDialog(GuiManager guiManager, I18nHandler i18nHandler, Window owner,
			SyncComponentModel componentModel, Filter<Component> filter) {
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
					currentComponent = list.getSelectedValue();
					if (Objects.nonNull(currentComponent)) {
						textArea.setText(currentComponent.getDescription());
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

			private static final long serialVersionUID = 435777217375395794L;

			@Override
			public void actionPerformed(ActionEvent e) {
				option = DialogOption.CANCEL;
				dispose();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		panel_1.add(cancelButton);

		getRootPane().setDefaultButton(okButton);

		pack();

		if (Objects.nonNull(componentModel)) {
			componentModel.addObverser(listObverser);
		}

		this.componentModel = componentModel;
		this.filter = filter;

		syncInit();
	}

	@Override
	public void dispose() {
		if (Objects.nonNull(componentModel)) {
			componentModel.removeObverser(listObverser);
		}
		super.dispose();
	}

	/**
	 * 获取该面板中当前的处理器。
	 * 
	 * @return 该面板中当前的处理器。
	 */
	public Component getCurrentComponent() {
		return currentComponent;
	}

	/**
	 * 以指定的类获取该面板中当前的处理器。
	 * 
	 * @param clas
	 *            指定的类。
	 * @return 转化成指定类的面板中的当前处理器。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	public <T extends Component> T getCurrentComponent(Class<T> clas) {
		Objects.requireNonNull(clas, "入口参数 clas 不能为 null。");
		return clas.cast(currentComponent);
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
	public Filter<Component> getFilter() {
		return filter;
	}

	/**
	 * 获取该面板的组件处理器模型。
	 * 
	 * @return 该免肝的组件处理器模型。
	 */
	public SyncComponentModel getComponentModel() {
		return componentModel;
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
	 * @param filter
	 *            面板中当前的文件过滤器。
	 */
	public void setFilter(Filter<Component> filter) {
		this.filter = filter;
		syncFiliter();
	}

	/**
	 * 设置该面板的组件处理器模型。
	 * 
	 * @param componentModel
	 *            指定的组件处理器模型。
	 */
	public void setComponentModel(SyncComponentModel componentModel) {
		if (Objects.nonNull(this.componentModel)) {
			this.componentModel.removeObverser(listObverser);
		}

		if (Objects.nonNull(componentModel)) {
			componentModel.addObverser(listObverser);
		}

		this.componentModel = componentModel;

		syncComponentModel();
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

	private void syncFiliter() {
		listModel.clear();
		textArea.setText(null);

		if (Objects.isNull(this.componentModel)) {
			return;
		}

		Comparator<Component> c = new ComponentKeyComparator();

		this.componentModel.getLock().readLock().lock();
		try {
			this.componentModel.forEach(component -> {
				if (Objects.isNull(filter) || filter.accept(component)) {
					CollectionUtil.insertByOrder(listModel, component, c);
				}
			});
		} finally {
			this.componentModel.getLock().readLock().unlock();
		}

		if (listModel.size() > 0) {
			list.getSelectionModel().setSelectionInterval(0, 0);
			list.getSelectionModel().setAnchorSelectionIndex(0);
			list.getSelectionModel().setLeadSelectionIndex(0);
		}

	}

	private void syncComponentModel() {
		listModel.clear();
		textArea.setText(null);

		if (Objects.isNull(this.componentModel)) {
			return;
		}

		Comparator<Component> c = new ComponentKeyComparator();

		this.componentModel.getLock().readLock().lock();
		try {
			this.componentModel.forEach(component -> {
				if (Objects.isNull(filter) || filter.accept(component)) {
					CollectionUtil.insertByOrder(listModel, component, c);
				}
			});
		} finally {
			this.componentModel.getLock().readLock().unlock();
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

		if (Objects.isNull(this.componentModel)) {
			return;
		}

		Comparator<Component> c = new ComponentKeyComparator();

		this.componentModel.getLock().readLock().lock();
		try {
			this.componentModel.forEach(component -> {
				if (Objects.isNull(filter) || filter.accept(component)) {
					CollectionUtil.insertByOrder(listModel, component, c);
				}
			});
		} finally {
			this.componentModel.getLock().readLock().unlock();
		}

		if (listModel.size() > 0) {
			list.getSelectionModel().setSelectionInterval(0, 0);
			list.getSelectionModel().setAnchorSelectionIndex(0);
			list.getSelectionModel().setLeadSelectionIndex(0);
		}
	}

}
