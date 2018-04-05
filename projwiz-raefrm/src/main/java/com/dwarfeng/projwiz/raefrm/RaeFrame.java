package com.dwarfeng.projwiz.raefrm;

import java.util.Objects;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.dutil.basic.threads.ThreadUtil;
import com.dwarfeng.dutil.develop.i18n.I18nHandler;
import com.dwarfeng.dutil.develop.i18n.obv.I18nAdapter;
import com.dwarfeng.dutil.develop.i18n.obv.I18nObverser;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.util.Constants;

/**
 * Rae框架窗口。
 * <p>
 * 该窗口在 {@link JDialog} 的基础上自动托管了国际化接口。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public abstract class RaeFrame extends JFrame {

	private static final long serialVersionUID = 8082185557618935547L;

	/** 窗口的内容面板 */
	protected final JPanel contentPane;

	/** 国际化处理器。 */
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
	public RaeFrame() {
		this(null);
	}

	/**
	 * 新实例。
	 * 
	 * @param i18n
	 *            国际化接口。
	 */
	public RaeFrame(I18nHandler i18nHandler) {
		this.i18nHandler = i18nHandler;

		this.contentPane = new JPanel();
		setContentPane(contentPane);

		if (Objects.nonNull(i18nHandler)) {
			i18nHandler.addObverser(i18nObverser);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Window#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		if (Objects.nonNull(i18nHandler))
			i18nHandler.removeObverser(i18nObverser);
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

		return String.format(Objects.isNull(i18nHandler) ? Constants.MISSING_LABEL
				: i18nHandler.getStringOrDefault(labelStringKey, Constants.MISSING_LABEL), args);
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

		return Objects.isNull(i18nHandler) ? Constants.MISSING_LABEL
				: i18nHandler.getStringOrDefault(labelStringKey, Constants.MISSING_LABEL);
	}

	/**
	 * 刷新标签。
	 */
	protected abstract void refreshLabels();

}
