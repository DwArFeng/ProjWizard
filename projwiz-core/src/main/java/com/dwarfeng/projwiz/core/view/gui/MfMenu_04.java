package com.dwarfeng.projwiz.core.view.gui;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Objects;
import java.util.Queue;

import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.dutil.develop.i18n.I18nHandler;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.view.obv.MainFrameVisibleAdapter;
import com.dwarfeng.projwiz.core.view.obv.MainFrameVisibleObverser;
import com.dwarfeng.projwiz.core.view.struct.GuiManager;
import com.dwarfeng.projwiz.core.view.struct.MainFrameVisibleModel;

public class MfMenu_04 extends ProjWizMenu {

	private static final long serialVersionUID = -6027825334082354066L;

	private final JRadioButtonMenuItem mi_01;
	private final JRadioButtonMenuItem mi_02;
	private final JRadioButtonMenuItem mi_03;
	private final JRadioButtonMenuItem mi_04;
	private final JRadioButtonMenuItem mi_05;
	private final JSeparator sp_01;

	private MainFrameVisibleModel mainFrameVisibleModel;

	private MainFrameVisibleObverser mainFrameVisibleObverser = new MainFrameVisibleAdapter() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireNorthVisibleChanged(boolean newValue) {
			SwingUtil.invokeInEventQueue(() -> {
				if (checkDuplexingForecast(new Object[] { "fireNorthVisibleChanged", newValue })) {
					return;
				}

				if (Objects.equals(newValue, mi_01.isSelected())) {
					return;
				}

				mi_01.setSelected(newValue);
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireSouthVisibleChanged(boolean newValue) {
			SwingUtil.invokeInEventQueue(() -> {
				if (checkDuplexingForecast(new Object[] { "fireSouthVisibleChanged", newValue })) {
					return;
				}

				if (Objects.equals(newValue, mi_04.isSelected())) {
					return;
				}

				mi_04.setSelected(newValue);
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireEastVisibleChanged(boolean newValue) {
			SwingUtil.invokeInEventQueue(() -> {
				if (checkDuplexingForecast(new Object[] { "fireEastVisibleChanged", newValue })) {
					return;
				}

				if (Objects.equals(newValue, mi_03.isSelected())) {
					return;
				}

				mi_03.setSelected(newValue);
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireWestVisibleChanged(boolean newValue) {
			SwingUtil.invokeInEventQueue(() -> {
				if (checkDuplexingForecast(new Object[] { "fireWestVisibleChanged", newValue })) {
					return;
				}

				if (Objects.equals(newValue, mi_02.isSelected())) {
					return;
				}

				mi_02.setSelected(newValue);
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireMaximumChanged(boolean newValue) {
			SwingUtil.invokeInEventQueue(() -> {
				if (checkDuplexingForecast(new Object[] { "fireMaximumChanged", newValue })) {
					return;
				}

				if (Objects.equals(newValue, mi_05.isSelected())) {
					return;
				}

				mi_05.setSelected(newValue);
			});
		}

	};

	/** 双工通信预测 */
	private final Queue<Object[]> duplexingForecast = new ArrayDeque<>();

	/**
	 * 新实例。
	 */
	public MfMenu_04() {
		this(null, null, null);
	}

	/**
	 * 新实例。
	 * 
	 * @param mainFrameVisibleModel
	 * 
	 * @param obversers
	 *            观察器集合。
	 * @param i18n
	 *            国际化接口。
	 */
	public MfMenu_04(GuiManager guiManager, I18nHandler i18nHandler, MainFrameVisibleModel mainFrameVisibleModel) {
		super(guiManager, i18nHandler);

		setText(label(LabelStringKey.MFMENU_4_1));
		setMnemonic('V');

		// 工具栏
		mi_01 = new JRadioButtonMenuItem();
		mi_01.setText(label(LabelStringKey.MFMENU_4_2));
		mi_01.setMnemonic('T');
		mi_01.addActionListener(e -> {
			boolean aFlag = mi_01.isSelected();
			duplexingForecast.offer(new Object[] { "fireNorthVisibleChanged", aFlag });
			mainFrameVisibleModel.setNorthVisible(aFlag);
		});
		add(mi_01);

		// 文件树
		mi_02 = new JRadioButtonMenuItem();
		mi_02.setText(label(LabelStringKey.MFMENU_4_3));
		mi_02.setMnemonic('F');
		mi_02.addActionListener(e -> {
			boolean aFlag = mi_02.isSelected();
			duplexingForecast.offer(new Object[] { "fireWestVisibleChanged", aFlag });
			mainFrameVisibleModel.setWestVisible(aFlag);
		});
		add(mi_02);

		// 提醒
		mi_03 = new JRadioButtonMenuItem();
		mi_03.setText(label(LabelStringKey.MFMENU_4_4));
		mi_03.setMnemonic('N');
		mi_03.addActionListener(e -> {
			boolean aFlag = mi_03.isSelected();
			duplexingForecast.offer(new Object[] { "fireEastVisibleChanged", aFlag });
			mainFrameVisibleModel.setEastVisible(aFlag);
		});
		add(mi_03);

		// 信息
		mi_04 = new JRadioButtonMenuItem();
		mi_04.setText(label(LabelStringKey.MFMENU_4_5));
		mi_04.setMnemonic('I');
		mi_04.addActionListener(e -> {
			boolean aFlag = mi_04.isSelected();
			duplexingForecast.offer(new Object[] { "fireSouthVisibleChanged", aFlag });
			mainFrameVisibleModel.setSouthVisible(aFlag);
		});
		add(mi_04);

		sp_01 = new JSeparator();
		add(sp_01);

		// 最大化
		mi_05 = new JRadioButtonMenuItem();
		mi_05.setText(label(LabelStringKey.MFMENU_4_6));
		mi_05.setMnemonic('M');
		mi_05.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_DOWN_MASK));
		mi_05.addActionListener(e -> {
			boolean aFlag = mi_05.isSelected();
			duplexingForecast.offer(new Object[] { "fireMaximumChanged", aFlag });
			mainFrameVisibleModel.setMaximum(aFlag);
		});
		add(mi_05);

		if (Objects.nonNull(mainFrameVisibleModel)) {
			mainFrameVisibleModel.addObverser(mainFrameVisibleObverser);
		}

		this.mainFrameVisibleModel = mainFrameVisibleModel;

		syncMainFrameVisibleModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		super.dispose();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void refreshLabels() {
		setText(label(LabelStringKey.MFMENU_4_1));
		mi_01.setText(label(LabelStringKey.MFMENU_4_2));
		mi_02.setText(label(LabelStringKey.MFMENU_4_3));
		mi_03.setText(label(LabelStringKey.MFMENU_4_4));
		mi_04.setText(label(LabelStringKey.MFMENU_4_5));
		mi_05.setText(label(LabelStringKey.MFMENU_4_6));

		if (Objects.nonNull(mainFrameVisibleModel)) {
			mainFrameVisibleModel.removeObverser(mainFrameVisibleObverser);
		}

	}

	/**
	 * 获取菜单的主界面可见性处理器。
	 * 
	 * @return 主界面可见性处理器。
	 */
	public MainFrameVisibleModel getMainFrameVisibleModel() {
		return mainFrameVisibleModel;
	}

	/**
	 * 设置菜单的主界面可见性处理器。
	 * 
	 * @param handler
	 *            指定的主界面可见性处理器。
	 */
	public void setMainFrameVisibleModel(MainFrameVisibleModel handler) {
		if (Objects.nonNull(this.mainFrameVisibleModel)) {
			this.mainFrameVisibleModel.removeObverser(mainFrameVisibleObverser);
		}

		if (Objects.nonNull(handler)) {
			handler.addObverser(mainFrameVisibleObverser);
		}

		this.mainFrameVisibleModel = handler;

		syncMainFrameVisibleModel();
	}

	private void syncMainFrameVisibleModel() {
		mi_01.setSelected(false);
		mi_02.setSelected(false);
		mi_03.setSelected(false);
		mi_04.setSelected(false);
		mi_05.setSelected(false);

		if (Objects.isNull(mainFrameVisibleModel)) {
			return;
		}

		mi_01.setSelected(mainFrameVisibleModel.isNorthVisible());
		mi_02.setSelected(mainFrameVisibleModel.isWestVisible());
		mi_03.setSelected(mainFrameVisibleModel.isEastVisible());
		mi_04.setSelected(mainFrameVisibleModel.isSouthVisible());
		mi_05.setSelected(mainFrameVisibleModel.isMaximum());
	}

	private boolean checkDuplexingForecast(Object[] objs) {
		if (duplexingForecast.isEmpty()) {
			return false;
		}

		if (Arrays.equals(duplexingForecast.peek(), objs)) {
			duplexingForecast.poll();
			return true;
		} else {
			syncMainFrameVisibleModel();
			duplexingForecast.clear();
			return false;
		}
	}

}
