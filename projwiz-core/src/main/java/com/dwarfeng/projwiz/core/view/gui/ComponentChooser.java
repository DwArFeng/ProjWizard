package com.dwarfeng.projwiz.core.view.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.dwarfeng.dutil.basic.cna.CollectionUtil;
import com.dwarfeng.dutil.basic.cna.model.DefaultReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
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
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.model.struct.Component;
import com.dwarfeng.projwiz.core.model.struct.ComponentComparator;
import com.dwarfeng.projwiz.core.view.eum.ChooseOption;
import com.dwarfeng.projwiz.core.view.struct.GuiManager;
import com.dwarfeng.projwiz.core.view.struct.JListMouseListener4Selection;
import com.dwarfeng.projwiz.core.view.struct.JListMouseMotionListener4Selection;

public final class ComponentChooser extends ProjWizChooser {

	private final JTextArea textArea;
	private final JList<Component> componentList;

	private SyncComponentModel componentModel;

	private final ReferenceModel<Component> selectedComponent = new DefaultReferenceModel<>();
	private final List<Component> selectedComponents = new ArrayList<>();
	private final ListSelectionModel componentListSelectionModel = new DefaultListSelectionModel();

	private final MuaListModel<Component> componentListModel = new MuaListModel<>();
	private final ListCellRenderer<Object> componentListRenderer = new DefaultListCellRenderer() {

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
				if (Objects.isNull(componentFilter) || componentFilter.accept(element)) {
					CollectionUtil.insertByOrder(componentListModel, element, new ComponentComparator());
				}
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireCleared() {
			SwingUtil.invokeInEventQueue(() -> {
				componentListModel.clear();
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireRemoved(Component element) {
			SwingUtil.invokeInEventQueue(() -> {
				componentListModel.remove(element);
			});
		}
	};
	private Filter<Component> componentFilter;
	private Component currentComponent;
	private boolean controlButtonsAreShown;
	private boolean multiSelectionEnabled;

	/**
	 * 新实例。
	 */
	public ComponentChooser() {
		this(null, null, null);
	}

	/**
	 * 
	 * @param guiManager
	 * @param i18nHandler
	 * @param componentModel
	 */
	public ComponentChooser(GuiManager guiManager, I18nHandler i18nHandler, SyncComponentModel componentModel) {
		super(guiManager, i18nHandler, LabelStringKey.COMPCHOOSER_1);

		setPreferredSize(new Dimension(450, 300));
		setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
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

		componentList = new JList<>();
		componentList.setVisibleRowCount(0);
		componentList.setDoubleBuffered(true);
		componentList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		componentList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					currentComponent = componentList.getSelectedValue();
					if (Objects.nonNull(currentComponent)) {
						textArea.setText(currentComponent.getDescription());
					} else {
						textArea.setText(null);
					}
					componentList.requestFocus();
				}
			}
		});
		componentList.setModel(componentListModel);
		componentList.setSelectionModel(componentListSelectionModel);
		componentList.setCellRenderer(componentListRenderer);
		componentList.addMouseMotionListener(new JListMouseMotionListener4Selection(componentList));
		componentList.addMouseListener(new JListMouseListener4Selection(componentList));
		componentList.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && Objects.nonNull(componentList.getSelectedValue())) {
					disposeCurrentDialog(ChooseOption.APPROVE_OPTION);
				}
			}
		});
		componentList.requestFocus();
		scrollPane_1.setViewportView(componentList);

		JPanel panel_1 = new JPanel();
		FlowLayout fl_panel_1 = (FlowLayout) panel_1.getLayout();
		fl_panel_1.setAlignment(FlowLayout.RIGHT);
		add(panel_1, BorderLayout.SOUTH);

		panel_1.add(button_approve);
		panel_1.add(button_cancel);

		if (Objects.nonNull(componentModel)) {
			componentModel.addObverser(listObverser);
		}

		this.componentModel = componentModel;

		syncInit();
	}

	@Override
	public void dispose() {
		if (Objects.nonNull(componentModel)) {
			componentModel.removeObverser(listObverser);
		}

		// 同步选择组件
		syncSelectedComponent();

		super.dispose();
	}

	/**
	 * 获取该面板中当前的文件过滤器。
	 * 
	 * @return 该面板中当前的文件过滤器。
	 */
	public Filter<Component> getComponentFilter() {
		return componentFilter;
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
	 * 获取选择的组件。
	 * 
	 * @return 选择的组件。
	 */
	public Component getSelectedComponent() {
		return selectedComponent.get();
	}

	/**
	 * 获取选择的组件。
	 * 
	 * @return 选择的组件。
	 */
	public Component[] getSelectedComponents() {
		return selectedComponents.toArray(new Component[0]);
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
	 * 获取是否可以选择多个组件。
	 * 
	 * @return 是否可以选择多个组件。
	 */
	public boolean isMultiSelectionEnabled() {
		return multiSelectionEnabled;
	}

	/**
	 * 设置该面板中当前的文件过滤器。
	 * 
	 * @param componnetFilter
	 *            面板中当前的文件过滤器。
	 */
	public void setComponentFilter(Filter<Component> componnetFilter) {
		this.componentFilter = componnetFilter;
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
	 * 设置控制按钮是否显示。
	 * 
	 * @param aFlag
	 *            是否显示。
	 */
	public void setControlButtonsAreShown(boolean controlButtonsAreShown) {
		this.controlButtonsAreShown = controlButtonsAreShown;
	}

	/**
	 * 设置是否可以选择多个组件。
	 * 
	 * @param multiSelectionEnabled
	 *            是否可以选择多个组件。
	 */
	public void setMultiSelectionEnabled(boolean multiSelectionEnabled) {
		this.multiSelectionEnabled = multiSelectionEnabled;
		if (multiSelectionEnabled) {
			componentList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		} else {
			componentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
	}

	private void syncComponentModel() {
		componentListModel.clear();
		textArea.setText(null);

		if (Objects.isNull(this.componentModel)) {
			return;
		}

		Comparator<Component> c = new ComponentComparator();

		this.componentModel.getLock().readLock().lock();
		try {
			this.componentModel.forEach(component -> {
				if (Objects.isNull(componentFilter) || componentFilter.accept(component)) {
					CollectionUtil.insertByOrder(componentListModel, component, c);
				}
			});
		} finally {
			this.componentModel.getLock().readLock().unlock();
		}

		if (componentListModel.size() > 0) {
			componentList.getSelectionModel().setSelectionInterval(0, 0);
			componentList.getSelectionModel().setAnchorSelectionIndex(0);
			componentList.getSelectionModel().setLeadSelectionIndex(0);
		}

	}

	private void syncFiliter() {
		componentListModel.clear();
		textArea.setText(null);

		if (Objects.isNull(this.componentModel)) {
			return;
		}

		Comparator<Component> c = new ComponentComparator();

		this.componentModel.getLock().readLock().lock();
		try {
			this.componentModel.forEach(component -> {
				if (Objects.isNull(componentFilter) || componentFilter.accept(component)) {
					CollectionUtil.insertByOrder(componentListModel, component, c);
				}
			});
		} finally {
			this.componentModel.getLock().readLock().unlock();
		}

		if (componentListModel.size() > 0) {
			componentList.getSelectionModel().setSelectionInterval(0, 0);
			componentList.getSelectionModel().setAnchorSelectionIndex(0);
			componentList.getSelectionModel().setLeadSelectionIndex(0);
		}

	}

	private void syncInit() {
		componentListModel.clear();
		textArea.setText(null);

		if (Objects.isNull(this.componentModel)) {
			return;
		}

		Comparator<Component> c = new ComponentComparator();

		this.componentModel.getLock().readLock().lock();
		try {
			this.componentModel.forEach(component -> {
				if (Objects.isNull(componentFilter) || componentFilter.accept(component)) {
					CollectionUtil.insertByOrder(componentListModel, component, c);
				}
			});
		} finally {
			this.componentModel.getLock().readLock().unlock();
		}

		if (componentListModel.size() > 0) {
			componentList.getSelectionModel().setSelectionInterval(0, 0);
			componentList.getSelectionModel().setAnchorSelectionIndex(0);
			componentList.getSelectionModel().setLeadSelectionIndex(0);
		}
	}

	private void syncSelectedComponent() {
		selectedComponents.clear();
		selectedComponent.set(null);

		switch (chooseOption) {
		case APPROVE_OPTION:
			for (int i = 0; i < componentListModel.size(); i++) {
				if (componentListSelectionModel.isSelectedIndex(i)) {
					selectedComponents.add(componentListModel.get(i));
				}
			}
			if (!componentModel.isEmpty() && componentListSelectionModel.getMinSelectionIndex() >= 0) {
				selectedComponent.set(componentListModel.get(componentListSelectionModel.getAnchorSelectionIndex()));
			}
			break;
		default:
			break;
		}
	}

}
