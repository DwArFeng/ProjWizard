package com.dwarfeng.projwiz.core.view.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog.ModalityType;
import java.awt.HeadlessException;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.dutil.basic.threads.ThreadUtil;
import com.dwarfeng.dutil.develop.i18n.I18nHandler;
import com.dwarfeng.dutil.develop.i18n.obv.I18nAdapter;
import com.dwarfeng.dutil.develop.i18n.obv.I18nObverser;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.util.Constants;
import com.dwarfeng.projwiz.core.view.eum.ChooseOption;
import com.dwarfeng.projwiz.core.view.eum.ChooserDialogType;
import com.dwarfeng.projwiz.core.view.struct.GuiManager;

/**
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public abstract class ProjWizChooser extends JPanel {

	private static final long serialVersionUID = -275491218229890061L;

	private class ChooserDialog extends ProjWizDialog {

		private static final long serialVersionUID = -5478092279641558608L;

		// bean 要求具有无参数构造器。
		@SuppressWarnings("unused")
		public ChooserDialog() {
			this(null, null, null);
		}

		public ChooserDialog(GuiManager guiManager, I18nHandler i18nHandler, Window owner) {
			super(guiManager, i18nHandler, owner);
			setTitle(label(titleLabel));
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void dispose() {
			ProjWizChooser.this.dispose();
			super.dispose();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void refreshLabels() {
			setTitle(label(titleLabel));
		}

	}

	/** 抽象对话框中的 GUI 管理器。 */
	protected final GuiManager guiManager;
	/** 抽象对话框中的国际化处理器。 */
	protected I18nHandler i18nHandler;
	/** 接受按钮。 */
	protected final JButton button_approve;
	/** 取消按钮。 */
	protected final JButton button_cancel;
	/** 标题标签。 */
	protected final LabelStringKey titleLabel;

	/** 当前的对话框。 */
	protected ProjWizDialog currentDialog = null;
	/** 选择器的选择选项。 */
	protected ChooseOption chooseOption = ChooseOption.ERROR_OPTION;
	/** 选择器的对话框类型。 */
	protected ChooserDialogType chooserDialogType = ChooserDialogType.CONFIRM_DIALOG;

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

	/** 额外的防止意外释放的保障。 */
	protected boolean disposeFlag = false;

	public ProjWizChooser() {
		this(null, null, null);
	}

	/**
	 * 
	 * @param guiManager
	 * @param i18nHandler
	 * @param titleLabel
	 */
	public ProjWizChooser(GuiManager guiManager, I18nHandler i18nHandler, LabelStringKey titleLabel) {
		this.guiManager = guiManager == null ? Constants.DEFAULT_GUIMANAGER : guiManager;
		this.i18nHandler = i18nHandler;

		if (Objects.nonNull(i18nHandler)) {
			i18nHandler.addObverser(i18nObverser);
		}

		registerKeyboardAction(e -> {
			disposeCurrentDialog(ChooseOption.CANCEL_OPTION);
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

		registerKeyboardAction(e -> {
			disposeCurrentDialog(ChooseOption.CANCEL_OPTION);
		}, KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK),
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

		button_approve = new JButton();
		setApproveButtonText();
		button_approve.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				disposeCurrentDialog(ChooseOption.APPROVE_OPTION);
			}
		});

		button_cancel = new JButton();
		button_cancel.setText(label(LabelStringKey.PROJWIZCHOOSER_5));
		button_cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				disposeCurrentDialog(ChooseOption.CANCEL_OPTION);
			}
		});

		this.titleLabel = Objects.isNull(titleLabel) ? LabelStringKey.PROJWIZCHOOSER_4 : titleLabel;
	}

	/**
	 * 释放资源。
	 */
	public void dispose() {
		if (Objects.nonNull(i18nHandler))
			i18nHandler.removeObverser(i18nObverser);
		// CT.trace(chooseOption);
	}

	/**
	 * 获取选择器的选择选项。
	 * 
	 * @return 选择器的选择选项。
	 */
	public ChooseOption getChooseOption() {
		return chooseOption;
	}

	/**
	 * 返回该选择器的对话框类型。
	 * 
	 * @return 该选择器的对话框类型。
	 */
	public ChooserDialogType getChooserDialogType() {
		return chooserDialogType;
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
	 * 设置该选择器的对话框类型。
	 * 
	 * <p>
	 * 如果入口参数为 <code>null</code>，则将对话框类型设置为
	 * {@link ChooserDialogType#CONFIRM_DIALOG}。
	 * 
	 * @param chooserDialogType
	 *            指定的选择器对话框类型。
	 */
	public void setChooserDialogType(ChooserDialogType chooserDialogType) {
		this.chooserDialogType = Objects.isNull(chooserDialogType) ? ChooserDialogType.CONFIRM_DIALOG
				: chooserDialogType;
		setApproveButtonText();
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

	public ChooseOption showDialog(Component parent) throws HeadlessException {
		if (currentDialog != null) {
			// Prevent to show second instance of dialog if the previous one
			// still exists
			return ChooseOption.ERROR_OPTION;
		}

		currentDialog = createDialog(parent);
		chooseOption = ChooseOption.ERROR_OPTION;

		currentDialog.setVisible(true);

		// Remove all components from dialog. The MetalFileChooserUI.installUI()
		// method (and other LAFs)
		// registers AWT listener for dialogs and produces memory leaks. It
		// happens when
		// installUI invoked after the showDialog method.
		currentDialog.getContentPane().removeAll();

		// 额外保障对话框的意外释放。
		try {
			if (!disposeFlag) {
				throw new IllegalStateException("选择器对话框发生了意料之外的释放！");
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
			currentDialog.dispose();
		}

		currentDialog = null;
		return chooseOption;
	}

	/**
	 * 释放该选择器当前的对话框，并将选择选项设置为指定值。
	 * 
	 * @param chooseOption
	 *            指定的的选择选项。
	 * @throws IllegalStateException
	 *             当选择器还未生成对话框便调用该方法或其它未知情形时。
	 */
	protected void disposeCurrentDialog(ChooseOption chooseOption) throws IllegalStateException {
		Objects.requireNonNull(chooseOption, "入口参数 chooseOption 不能为 null。");

		if (Objects.isNull(currentDialog)) {
			chooseOption = ChooseOption.ERROR_OPTION;
			throw new IllegalStateException("确认按钮的侦听器似乎不该在这个时候触发");
		}

		this.chooseOption = chooseOption;
		disposeFlag = true;
		currentDialog.dispose();
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
	protected void refreshLabels() {
		setApproveButtonText();
		button_cancel.setText(label(LabelStringKey.PROJWIZCHOOSER_5));
	}

	private ProjWizDialog createDialog(Component parent) throws HeadlessException {
		ChooserDialog dialog;
		Window window = Objects.isNull(parent) ? null : SwingUtilities.getWindowAncestor(parent);

		dialog = new ChooserDialog(guiManager, i18nHandler, window);
		dialog.setModalityType(ModalityType.APPLICATION_MODAL);
		this.currentDialog = dialog;
		dialog.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosed(WindowEvent e) {
				currentDialog = null;
			}

			@Override
			public void windowClosing(WindowEvent e) {
				disposeCurrentDialog(ChooseOption.CANCEL_OPTION);
			}
		});
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.setComponentOrientation(this.getComponentOrientation());
		dialog.getRootPane().setDefaultButton(button_approve);

		Container contentPane = dialog.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(this, BorderLayout.CENTER);

		if (JDialog.isDefaultLookAndFeelDecorated()) {
			boolean supportsWindowDecorations = UIManager.getLookAndFeel().getSupportsWindowDecorations();
			if (supportsWindowDecorations) {
				dialog.getRootPane().setWindowDecorationStyle(JRootPane.FILE_CHOOSER_DIALOG);
			}
		}
		dialog.pack();
		dialog.setLocationRelativeTo(parent);

		return dialog;
	}

	private void setApproveButtonText() {
		switch (chooserDialogType) {
		case OPEN_DIALOG:
			button_approve.setText(label(LabelStringKey.PROJWIZCHOOSER_2));
			break;
		case SAVE_DIALOG:
			button_approve.setText(label(LabelStringKey.PROJWIZCHOOSER_3));
			break;
		case CONFIRM_DIALOG:
			button_approve.setText(label(LabelStringKey.PROJWIZCHOOSER_1));
			break;
		default:
			button_approve.setText(label(LabelStringKey.PROJWIZCHOOSER_1));
			break;
		}
	}

}
