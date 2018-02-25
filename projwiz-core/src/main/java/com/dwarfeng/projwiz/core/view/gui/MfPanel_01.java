package com.dwarfeng.projwiz.core.view.gui;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.ListCellRenderer;

import com.dwarfeng.dutil.basic.cna.model.SyncListModel;
import com.dwarfeng.dutil.basic.cna.model.SyncReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.obv.ListAdapter;
import com.dwarfeng.dutil.basic.cna.model.obv.ListObverser;
import com.dwarfeng.dutil.basic.cna.model.obv.ReferenceAdapter;
import com.dwarfeng.dutil.basic.cna.model.obv.ReferenceObverser;
import com.dwarfeng.dutil.basic.gui.awt.CommonIconLib;
import com.dwarfeng.dutil.basic.gui.awt.ImageSize;
import com.dwarfeng.dutil.basic.gui.awt.ImageUtil;
import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.dutil.develop.i18n.I18nHandler;
import com.dwarfeng.projwiz.core.model.cm.SyncComponentModel;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.ProjectProcessor;
import com.dwarfeng.projwiz.core.view.struct.GuiManager;
import com.dwarfeng.projwiz.core.view.struct.GuiManager.ExecType;

class MfPanel_01 extends ProjWizPanel {

	private static final long serialVersionUID = -7697300338984335955L;
	private static final String DF_HOLDPROJECTMODEL_FIREADDED = "A";
	private static final String DF_HOLDPROJECTMODEL_FIREREMOVED = "B";
	private static final String DF_HOLDPROJECTMODEL_FIRECHANGED = "C";
	private static final String DF_HOLDPROJECTMODEL_FIRECLEARED = "D";
	private static final String DF_FOCUSPROJECTMODEL_FIRECHANGED = "E";
	private static final String DF_FOCUSPROJECTMODEL_FIRECLEARED = "F";

	private SyncComponentModel componentModel;
	private SyncReferenceModel<Project> focusProjectModel;
	private SyncListModel<Project> holdProjectModel;
	private final JComboBox<Project> comboBox;

	private final DefaultComboBoxModel<Project> comboModel = new DefaultComboBoxModel<>();

	private final ListCellRenderer<Object> comboRenderer = new DefaultListCellRenderer() {

		private static final long serialVersionUID = 6053676608992603277L;

		@Override
		public java.awt.Component getListCellRendererComponent(javax.swing.JList<?> list, Object value, int index,
				boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			Project project = (Project) value;
			if (Objects.isNull(project))
				return this;
			setText(project.getName());
			Image image = null;
			if (Objects.nonNull(componentModel)) {
				ProjectProcessor processor = componentModel.get(project.getProcessorClass());
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

	private final ListObverser<Project> holdProjectObverser = new ListAdapter<Project>() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireAdded(int index, Project element) {
			SwingUtil.invokeInEventQueue(() -> {
				if (checkDuplexingForecast(new Object[] { DF_HOLDPROJECTMODEL_FIREADDED, index, element })) {
					return;
				}

				adjustFlagLock.lock();
				try {
					adjustFlag = true;
					comboModel.insertElementAt(element, index);
				} finally {
					adjustFlag = false;
					adjustFlagLock.unlock();
				}
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireChanged(int index, Project oldElement, Project newElement) {
			SwingUtil.invokeInEventQueue(() -> {
				if (checkDuplexingForecast(
						new Object[] { DF_HOLDPROJECTMODEL_FIRECHANGED, index, oldElement, newElement })) {
					return;
				}

				adjustFlagLock.lock();
				try {
					adjustFlag = true;
					comboModel.removeElementAt(index);
					comboModel.insertElementAt(newElement, index);
				} finally {
					adjustFlag = false;
					adjustFlagLock.unlock();
				}
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireCleared() {
			SwingUtil.invokeInEventQueue(() -> {
				if (checkDuplexingForecast(new Object[] { DF_HOLDPROJECTMODEL_FIRECLEARED })) {
					return;
				}

				adjustFlagLock.lock();
				try {
					adjustFlag = true;
					comboModel.removeAllElements();
					focusProject = null;
				} finally {
					adjustFlag = false;
					adjustFlagLock.unlock();
				}
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireRemoved(int index, Project element) {
			SwingUtil.invokeInEventQueue(() -> {
				if (checkDuplexingForecast(new Object[] { DF_HOLDPROJECTMODEL_FIREREMOVED, index, element })) {
					return;
				}

				adjustFlagLock.lock();
				try {
					adjustFlag = true;
					comboModel.removeElementAt(index);
					if (Objects.equals(focusProject, element)) {
						focusProject = null;
					}
				} finally {
					adjustFlag = false;
					adjustFlagLock.unlock();
				}
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
				if (checkDuplexingForecast(new Object[] { DF_FOCUSPROJECTMODEL_FIRECLEARED })) {
					return;
				}

				adjustFlagLock.lock();
				try {
					adjustFlag = true;
					comboModel.setSelectedItem(null);
					focusProject = null;
				} finally {
					adjustFlag = false;
					adjustFlagLock.unlock();
				}
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireSet(Project oldValue, Project newValue) {
			SwingUtil.invokeInEventQueue(() -> {
				if (checkDuplexingForecast(new Object[] { DF_FOCUSPROJECTMODEL_FIRECHANGED, oldValue, newValue })) {
					return;
				}

				adjustFlagLock.lock();
				try {
					adjustFlag = true;
					if (!Objects.equals(newValue, focusProject)) {
						if (!Objects.equals(newValue, comboModel.getSelectedItem())) {
							comboModel.setSelectedItem(newValue);
						}
						focusProject = newValue;
					}
				} finally {
					adjustFlag = false;
					adjustFlagLock.unlock();
				}
			});
		}

	};

	/** 双工通信预测 */
	private final Queue<Object[]> duplexingForecast = new ArrayDeque<>();

	private boolean adjustFlag = false;
	private Lock adjustFlagLock = new ReentrantLock();
	private Project focusProject = null;

	/**
	 * 新实例。
	 */
	public MfPanel_01() {
		this(null, null, null, null, null);
	}

	/**
	 * 
	 * @param guiManager
	 * @param i18nHandler
	 * @param focusProjectModel
	 * @param holdProjectModel
	 * @param componentModel
	 */
	public MfPanel_01(GuiManager guiManager, I18nHandler i18nHandler, SyncReferenceModel<Project> focusProjectModel,
			SyncListModel<Project> holdProjectModel, SyncComponentModel componentModel) {
		super(guiManager, i18nHandler);

		setLayout(new BorderLayout(0, 0));

		comboBox = new JComboBox<>();
		comboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				adjustFlagLock.lock();
				try {
					if (adjustFlag || Objects.equals(focusProject, comboBox.getSelectedItem()))
						return;
				} finally {
					adjustFlagLock.unlock();
				}

				if (Objects.nonNull(MfPanel_01.this.focusProjectModel)) {
					Project oldFocusProject = MfPanel_01.this.focusProject;
					Project newFocusProject = (Project) comboBox.getSelectedItem();
					duplexingForecast
							.offer(new Object[] { DF_FOCUSPROJECTMODEL_FIRECHANGED, oldFocusProject, newFocusProject });

					focusProject = newFocusProject;
					MfPanel_01.this.guiManager.setFocusProject(focusProject, ExecType.CONCURRENT);
				}
			}
		});
		comboBox.setModel(comboModel);
		comboBox.setRenderer(comboRenderer);
		add(comboBox, BorderLayout.CENTER);

		if (Objects.nonNull(focusProjectModel)) {
			focusProjectModel.addObverser(focusProjectObverser);
		}
		if (Objects.nonNull(holdProjectModel)) {
			holdProjectModel.addObverser(holdProjectObverser);
		}

		this.focusProjectModel = focusProjectModel;
		this.holdProjectModel = holdProjectModel;
		this.componentModel = componentModel;

		syncProjectModel();
		syncComponentModel();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		super.dispose();

		adjustFlagLock.lock();
		try {
			adjustFlag = true;

			if (Objects.nonNull(this.focusProjectModel)) {
				this.focusProjectModel.removeObverser(focusProjectObverser);
			}
			if (Objects.nonNull(this.holdProjectModel)) {
				this.holdProjectModel.removeObverser(holdProjectObverser);
			}

			comboModel.removeAllElements();
			focusProject = null;

		} finally {
			adjustFlag = false;
			adjustFlagLock.unlock();
		}

	}

	/**
	 * 获取该面板的组件处理器模型。
	 * 
	 * @return the componentModel 该面板的组件处理器模型。
	 */
	public SyncComponentModel getComponentModel() {
		return componentModel;
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
	 * 设置该面板的组件处理器模型。
	 * 
	 * @param projectProcessorModel
	 *            该面板的组件处理器模型。
	 */
	public void setComponentModel(SyncComponentModel componentModel) {
		this.componentModel = componentModel;
		syncComponentModel();
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
	 * {@inheritDoc}
	 */
	@Override
	protected void refreshLabels() {
	}

	private boolean checkDuplexingForecast(Object[] objs) {
		if (duplexingForecast.isEmpty()) {
			return false;
		}

		if (Arrays.equals(duplexingForecast.peek(), objs)) {
			duplexingForecast.poll();
			return true;
		} else {
			syncProjectModel();
			duplexingForecast.clear();
			return false;
		}
	}

	private void syncComponentModel() {
		adjustFlagLock.lock();
		try {
			adjustFlag = true;
			comboBox.repaint();
		} finally {
			adjustFlag = false;
			adjustFlagLock.unlock();
		}
	}

	/**
	 * 当该方法与 {@link #syncHoldProjectModel()}一起调用时，先调用
	 * {@link #syncHoldProjectModel()}。
	 */
	private void syncFocusProjectModel() {
		comboBox.setSelectedItem(null);
		focusProject = null;

		if (Objects.isNull(holdProjectModel)) {
			return;
		}

		if (Objects.isNull(focusProjectModel)) {
			return;
		}

		adjustFlagLock.lock();
		try {
			adjustFlag = true;
			this.focusProjectModel.getLock().readLock().lock();
			try {
				comboBox.setSelectedItem(focusProjectModel.get());
				focusProject = focusProjectModel.get();
			} finally {
				this.focusProjectModel.getLock().readLock().unlock();
			}
		} finally {
			adjustFlag = false;
			adjustFlagLock.unlock();
		}

	}

	/**
	 * 当该方法与 {@link #syncFocusProjectModel()}一起调用时，先调用
	 * {@link #syncHoldProjectModel()}。
	 */
	private void syncHoldProjectModel() {
		comboModel.removeAllElements();

		if (Objects.isNull(holdProjectModel)) {
			return;
		}

		adjustFlagLock.lock();
		try {
			adjustFlag = true;
			this.holdProjectModel.getLock().readLock().lock();
			try {
				this.holdProjectModel.forEach(project -> {
					comboModel.addElement(project);
				});
			} finally {
				this.holdProjectModel.getLock().readLock().unlock();
			}
		} finally {
			adjustFlag = false;
			adjustFlagLock.unlock();
		}

	}

	/**
	 * 需要同时调用 {@link #syncFocusProjectModel()} 和 {@link #syncHoldProjectModel()}
	 * 时，应该优先调用此方法。
	 */
	private void syncProjectModel() {
		comboModel.removeAllElements();
		focusProject = null;

		if (Objects.isNull(holdProjectModel)) {
			return;
		}

		adjustFlagLock.lock();
		try {
			adjustFlag = true;

			this.holdProjectModel.getLock().readLock().lock();
			try {
				this.holdProjectModel.forEach(project -> {
					comboModel.addElement(project);
				});
			} finally {
				this.holdProjectModel.getLock().readLock().unlock();
			}

			if (Objects.isNull(focusProjectModel)) {
				return;
			}

			this.focusProjectModel.getLock().readLock().lock();
			try {
				comboBox.setSelectedItem(focusProjectModel.get());
				focusProject = focusProjectModel.get();
			} finally {
				this.focusProjectModel.getLock().readLock().unlock();
			}

		} finally {
			adjustFlag = false;
			adjustFlagLock.unlock();
		}
	};

}
