package com.dwarfeng.projwiz.core.view.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog.ModalityType;
import java.awt.HeadlessException;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.dwarfeng.dutil.develop.i18n.I18nHandler;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.util.Constants;
import com.dwarfeng.projwiz.core.view.struct.GuiManager;

/**
 * 
 * @author DwArFeng
 * @since 0.0.2-alpha
 */
public final class SystemFileChooser extends JFileChooser {

	private static final class FileChooserDialog extends ProjWizDialog {

		private static final long serialVersionUID = 4656058564541571419L;

		/**
		 * 新实例。
		 */
		@SuppressWarnings("unused")
		public FileChooserDialog() {
			this(null, null, null);
		}

		/**
		 * 新实例。
		 * 
		 * @param guiManager
		 *            指定的 GUI 管理器。
		 * @param i18nHandler
		 *            指定的国际化处理器。
		 * @param owner
		 *            对话框指定的所有者。
		 */
		public FileChooserDialog(GuiManager guiManager, I18nHandler i18nHandler, Window owner) {
			super(guiManager, i18nHandler, owner);
			setTitle(label(LabelStringKey.SYSFC_1));
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void refreshLabels() {
			setTitle(label(LabelStringKey.SYSFC_1));
		}

	}

	private static final long serialVersionUID = -2933074003696988762L;

	/** 抽象对话框中的 GUI 管理器。 */
	protected final GuiManager guiManager;
	/** 抽象对话框中的国际化处理器。 */
	protected I18nHandler i18nHandler;

	/** 当前正在引用的对话框。 */
	private FileChooserDialog currentDialog = null;

	/**
	 * 新实例。
	 */
	public SystemFileChooser() {
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
	public SystemFileChooser(GuiManager guiManager, I18nHandler i18nHandler) {
		super();

		this.guiManager = guiManager == null ? Constants.DEFAULT_GUIMANAGER : guiManager;
		this.i18nHandler = i18nHandler;
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
		this.i18nHandler = i18nHandler;
		if (Objects.nonNull(currentDialog)) {
			currentDialog.setI18nHandler(i18nHandler);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected JDialog createDialog(Component parent) throws HeadlessException {
		FileChooserDialog dialog;
		Window window = SwingUtilities.getWindowAncestor(parent);

		dialog = new FileChooserDialog(guiManager, i18nHandler, window);
		dialog.setModalityType(ModalityType.APPLICATION_MODAL);
		this.currentDialog = dialog;
		dialog.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosed(WindowEvent e) {
				currentDialog = null;
			}
		});
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setComponentOrientation(this.getComponentOrientation());

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
}
