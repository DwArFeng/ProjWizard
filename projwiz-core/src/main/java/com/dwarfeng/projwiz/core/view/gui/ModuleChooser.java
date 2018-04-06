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
import com.dwarfeng.projwiz.core.model.cm.SyncModuleModel;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.model.struct.Module;
import com.dwarfeng.projwiz.core.model.struct.ModuleComparator;
import com.dwarfeng.projwiz.core.view.eum.ChooseOption;
import com.dwarfeng.projwiz.core.view.struct.GuiManager;
import com.dwarfeng.projwiz.core.view.struct.JListMouseListener4Selection;
import com.dwarfeng.projwiz.core.view.struct.JListMouseMotionListener4Selection;

public final class ModuleChooser extends ProjWizChooser {

	private final JTextArea textArea;
	private final JList<Module> moduleList;

	private SyncModuleModel moduleModel;

	private final ReferenceModel<Module> selectedModule = new DefaultReferenceModel<>();
	private final List<Module> selectedModules = new ArrayList<>();
	private final ListSelectionModel moduleListSelectionModel = new DefaultListSelectionModel();

	private final MuaListModel<Module> moduleListModel = new MuaListModel<>();
	private final ListCellRenderer<Object> moduleListRenderer = new DefaultListCellRenderer() {

		@Override
		public java.awt.Component getListCellRendererComponent(javax.swing.JList<?> list, Object value, int index,
				boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			setHorizontalAlignment(JLabel.CENTER);
			setHorizontalTextPosition(JLabel.CENTER);
			setVerticalTextPosition(JLabel.BOTTOM);

			setText(null);
			if (Objects.nonNull(((Module) value).getIcon())) {
				setIcon(new ImageIcon(ImageUtil.scaleImage(((Module) value).getIcon(), ImageSize.ICON_MEDIUM)));
			} else {
				setIcon(new ImageIcon(ImageUtil.getInternalImage(CommonIconLib.UNKNOWN_BLUE, ImageSize.ICON_MEDIUM)));
			}
			setToolTipText(((Module) value).getName());
			return this;
		};
	};
	private final SetObverser<Module> listObverser = new SetAdapter<Module>() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireAdded(Module element) {
			SwingUtil.invokeInEventQueue(() -> {
				if (Objects.isNull(moduleFilter) || moduleFilter.accept(element)) {
					CollectionUtil.insertByOrder(moduleListModel, element, new ModuleComparator());
				}
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireCleared() {
			SwingUtil.invokeInEventQueue(() -> {
				moduleListModel.clear();
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireRemoved(Module element) {
			SwingUtil.invokeInEventQueue(() -> {
				moduleListModel.remove(element);
			});
		}
	};
	private Filter<Module> moduleFilter;
	private Module currentModule;
	private boolean controlButtonsAreShown;
	private boolean multiSelectionEnabled;

	/**
	 * 新实例。
	 */
	public ModuleChooser() {
		this(null, null, null);
	}

	/**
	 * 
	 * @param guiManager
	 * @param i18nHandler
	 * @param moduleModel
	 */
	public ModuleChooser(GuiManager guiManager, I18nHandler i18nHandler, SyncModuleModel moduleModel) {
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

		moduleList = new JList<>();
		moduleList.setVisibleRowCount(0);
		moduleList.setDoubleBuffered(true);
		moduleList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		moduleList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					currentModule = moduleList.getSelectedValue();
					if (Objects.nonNull(currentModule)) {
						textArea.setText(currentModule.getDescription());
					} else {
						textArea.setText(null);
					}
					moduleList.requestFocus();
				}
			}
		});
		moduleList.setModel(moduleListModel);
		moduleList.setSelectionModel(moduleListSelectionModel);
		moduleList.setCellRenderer(moduleListRenderer);
		moduleList.addMouseMotionListener(new JListMouseMotionListener4Selection(moduleList));
		moduleList.addMouseListener(new JListMouseListener4Selection(moduleList));
		moduleList.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && Objects.nonNull(moduleList.getSelectedValue())) {
					disposeCurrentDialog(ChooseOption.APPROVE_OPTION);
				}
			}
		});
		moduleList.requestFocus();
		scrollPane_1.setViewportView(moduleList);

		JPanel panel_1 = new JPanel();
		FlowLayout fl_panel_1 = (FlowLayout) panel_1.getLayout();
		fl_panel_1.setAlignment(FlowLayout.RIGHT);
		add(panel_1, BorderLayout.SOUTH);

		panel_1.add(button_approve);
		panel_1.add(button_cancel);

		if (Objects.nonNull(moduleModel)) {
			moduleModel.addObverser(listObverser);
		}

		this.moduleModel = moduleModel;

		syncInit();
	}

	@Override
	public void dispose() {
		if (Objects.nonNull(moduleModel)) {
			moduleModel.removeObverser(listObverser);
		}

		// 同步选择组件
		syncSelectedModule();

		super.dispose();
	}

	/**
	 * 获取该面板中当前的文件过滤器。
	 * 
	 * @return 该面板中当前的文件过滤器。
	 */
	public Filter<Module> getModuleFilter() {
		return moduleFilter;
	}

	/**
	 * 获取该面板的组件处理器模型。
	 * 
	 * @return 该免肝的组件处理器模型。
	 */
	public SyncModuleModel getModuleModel() {
		return moduleModel;
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
	public <T extends Module> T getCurrentModule(Class<T> clas) {
		Objects.requireNonNull(clas, "入口参数 clas 不能为 null。");
		return clas.cast(currentModule);
	}

	/**
	 * 获取选择的组件。
	 * 
	 * @return 选择的组件。
	 */
	public Module getSelectedModule() {
		return selectedModule.get();
	}

	/**
	 * 获取选择的组件。
	 * 
	 * @return 选择的组件。
	 */
	public Module[] getSelectedModules() {
		return selectedModules.toArray(new Module[0]);
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
	public void setModuleFilter(Filter<Module> componnetFilter) {
		this.moduleFilter = componnetFilter;
		syncFiliter();
	}

	/**
	 * 设置该面板的组件处理器模型。
	 * 
	 * @param moduleModel
	 *            指定的组件处理器模型。
	 */
	public void setModuleModel(SyncModuleModel moduleModel) {
		if (Objects.nonNull(this.moduleModel)) {
			this.moduleModel.removeObverser(listObverser);
		}

		if (Objects.nonNull(moduleModel)) {
			moduleModel.addObverser(listObverser);
		}

		this.moduleModel = moduleModel;

		syncModuleModel();
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
			moduleList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		} else {
			moduleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
	}

	private void syncModuleModel() {
		moduleListModel.clear();
		textArea.setText(null);

		if (Objects.isNull(this.moduleModel)) {
			return;
		}

		Comparator<Module> c = new ModuleComparator();

		this.moduleModel.getLock().readLock().lock();
		try {
			this.moduleModel.forEach(module -> {
				if (Objects.isNull(moduleFilter) || moduleFilter.accept(module)) {
					CollectionUtil.insertByOrder(moduleListModel, module, c);
				}
			});
		} finally {
			this.moduleModel.getLock().readLock().unlock();
		}

		if (moduleListModel.size() > 0) {
			moduleList.getSelectionModel().setSelectionInterval(0, 0);
			moduleList.getSelectionModel().setAnchorSelectionIndex(0);
			moduleList.getSelectionModel().setLeadSelectionIndex(0);
		}

	}

	private void syncFiliter() {
		moduleListModel.clear();
		textArea.setText(null);

		if (Objects.isNull(this.moduleModel)) {
			return;
		}

		Comparator<Module> c = new ModuleComparator();

		this.moduleModel.getLock().readLock().lock();
		try {
			this.moduleModel.forEach(module -> {
				if (Objects.isNull(moduleFilter) || moduleFilter.accept(module)) {
					CollectionUtil.insertByOrder(moduleListModel, module, c);
				}
			});
		} finally {
			this.moduleModel.getLock().readLock().unlock();
		}

		if (moduleListModel.size() > 0) {
			moduleList.getSelectionModel().setSelectionInterval(0, 0);
			moduleList.getSelectionModel().setAnchorSelectionIndex(0);
			moduleList.getSelectionModel().setLeadSelectionIndex(0);
		}

	}

	private void syncInit() {
		moduleListModel.clear();
		textArea.setText(null);

		if (Objects.isNull(this.moduleModel)) {
			return;
		}

		Comparator<Module> c = new ModuleComparator();

		this.moduleModel.getLock().readLock().lock();
		try {
			this.moduleModel.forEach(module -> {
				if (Objects.isNull(moduleFilter) || moduleFilter.accept(module)) {
					CollectionUtil.insertByOrder(moduleListModel, module, c);
				}
			});
		} finally {
			this.moduleModel.getLock().readLock().unlock();
		}

		if (moduleListModel.size() > 0) {
			moduleList.getSelectionModel().setSelectionInterval(0, 0);
			moduleList.getSelectionModel().setAnchorSelectionIndex(0);
			moduleList.getSelectionModel().setLeadSelectionIndex(0);
		}
	}

	private void syncSelectedModule() {
		selectedModules.clear();
		selectedModule.set(null);

		switch (chooseOption) {
		case APPROVE_OPTION:
			for (int i = 0; i < moduleListModel.size(); i++) {
				if (moduleListSelectionModel.isSelectedIndex(i)) {
					selectedModules.add(moduleListModel.get(i));
				}
			}
			if (!moduleModel.isEmpty() && moduleListSelectionModel.getMinSelectionIndex() >= 0) {
				selectedModule.set(moduleListModel.get(moduleListSelectionModel.getAnchorSelectionIndex()));
			}
			break;
		default:
			break;
		}
	}

}
