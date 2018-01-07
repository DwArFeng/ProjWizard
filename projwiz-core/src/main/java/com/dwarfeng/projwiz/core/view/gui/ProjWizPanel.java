package com.dwarfeng.projwiz.core.view.gui;

import java.util.Objects;

import javax.swing.JPanel;

import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.dutil.basic.threads.ThreadUtil;
import com.dwarfeng.dutil.develop.i18n.I18n;
import com.dwarfeng.dutil.develop.i18n.I18nHandler;
import com.dwarfeng.dutil.develop.i18n.obv.I18nAdapter;
import com.dwarfeng.dutil.develop.i18n.obv.I18nObverser;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.util.Constants;
import com.dwarfeng.projwiz.core.util.I18nUtil;
import com.dwarfeng.projwiz.core.view.struct.GuiManager;

/**
 * 程序中的抽象面板。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public abstract class ProjWizPanel extends JPanel {

	private static final long serialVersionUID = -7933775901963480055L;

	/** 抽象对话框中的 GUI 管理器。 */
	protected final GuiManager guiManager;
	/** 抽象对话框中的国际化处理器。 */
	protected I18nHandler i18nHandler;

	private final I18nObverser i18nObverser = new I18nAdapter() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireCurrentLocaleChanged(java.util.Locale oldLocale, java.util.Locale newLocale,
				com.dwarfeng.dutil.develop.i18n.I18n newI18n) {
			SwingUtil.invokeInEventQueue(() -> {
				ThreadUtil.readLockIfSync(i18nHandler);
				try {
					refreshLabels();
				} finally {
					ThreadUtil.readUnlockIfSync(i18nHandler);
				}
			});
		};
	};

	/**
	 * 新实例。
	 */
	public ProjWizPanel() {
		this(null, null);
	}

	/**
	 * 新实例。
	 * 
	 * @param guiManager
	 *            程序管理器。
	 * @param i18n
	 *            国际化接口。
	 */
	public ProjWizPanel(GuiManager guiManager, I18nHandler i18nHandler) {
		this.guiManager = guiManager == null ? Constants.DEFAULT_GUIMANAGER : guiManager;
		this.i18nHandler = i18nHandler;

		if (Objects.nonNull(i18nHandler)) {
			i18nHandler.addObverser(i18nObverser);
		}

	}

	/**
	 * 释放资源。
	 */
	public void dispose() {
		if (Objects.nonNull(i18nHandler))
			i18nHandler.removeObverser(i18nObverser);
	}

	/**
	 * 获取国际化处理器。
	 * 
	 * @return the i18nHandler
	 */
	public I18nHandler getI18nHandler() {
		return i18nHandler;
	}

	/**
	 * 设置国际化处理器。
	 * 
	 * @param i18nHandler
	 *            the i18nHandler to set
	 */
	public void setI18nHandler(I18nHandler i18nHandler) {
		if (Objects.nonNull(this.i18nHandler)) {
			this.i18nHandler.removeObverser(i18nObverser);
		}

		if (Objects.nonNull(i18nHandler)) {
			i18nHandler.addObverser(i18nObverser);
		}

		this.i18nHandler = i18nHandler;

		if (Objects.nonNull(this.i18nHandler)) {
			this.i18nHandler.addObverser(i18nObverser);
			ThreadUtil.readLockIfSync(this.i18nHandler);
			try {
				refreshLabels();
			} finally {
				ThreadUtil.readUnlockIfSync(this.i18nHandler);
			}
		} else {
			refreshLabels();
		}
	}

	/**
	 * 获取格式化标签文本。
	 * 
	 * @param labelStringKey
	 *            指定的标签文本键。
	 * @param args
	 *            参数。
	 * @return 格式化标签文本。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	protected String formatLabel(LabelStringKey labelStringKey, Object... args) {
		Objects.requireNonNull(labelStringKey, "入口参数 labelStringKey 不能为 null。");
		Objects.requireNonNull(args, "入口参数 args 不能为 null。");
		return String.format(I18nUtil.getLabelString(i18n(), labelStringKey.getName()), args);
	}

	/**
	 * 获取标签文本。
	 * 
	 * @param labelStringKey
	 *            指定的标签文本键。
	 * @return 标签文本。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	protected String label(LabelStringKey labelStringKey) {
		Objects.requireNonNull(labelStringKey, "入口参数 labelStringKey 不能为 null。");
		return I18nUtil.getLabelString(i18n(), labelStringKey.getName());
	}

	/**
	 * 刷新标签。
	 */
	protected abstract void refreshLabels();

	private I18n i18n() {
		return Objects.isNull(i18nHandler) ? null : i18nHandler.getCurrentI18n();
	}

}
