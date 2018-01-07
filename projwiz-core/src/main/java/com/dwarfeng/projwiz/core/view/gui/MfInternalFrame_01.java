package com.dwarfeng.projwiz.core.view.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Objects;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.dutil.develop.i18n.I18nHandler;
import com.dwarfeng.projwiz.core.model.obv.EditorAdapter;
import com.dwarfeng.projwiz.core.model.obv.EditorObverser;
import com.dwarfeng.projwiz.core.model.struct.Editor;
import com.dwarfeng.projwiz.core.view.struct.GuiManager;
import com.dwarfeng.projwiz.core.view.struct.GuiManager.ExecType;

/**
 * 内部窗口。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
final class MfInternalFrame_01 extends ProjWizInternalFrame {

	private static final long serialVersionUID = -8089391120583342791L;

	private static Component createDefaultComponent() {
		JLabel label = new JLabel();
		label.setText("请添加内容");
		label.setPreferredSize(new Dimension(300, 100));
		label.setHorizontalAlignment(JLabel.CENTER);
		return label;
	}

	private Editor editor;

	private final EditorObverser editorObverser = new EditorAdapter() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireTitleChanged(String oldTitle, String newTitle) {
			SwingUtil.invokeInEventQueue(() -> {
				setTitle(newTitle);
			});
		}

	};

	/**
	 * 新实例。
	 */
	public MfInternalFrame_01() {
		this(null, null, null);
	}

	/**
	 * 
	 * @param guiManager
	 * @param i18nHandler
	 * @param editor
	 */
	public MfInternalFrame_01(GuiManager guiManager, I18nHandler i18nHandler, Editor editor) {
		super(guiManager, i18nHandler);

		addInternalFrameListener(new InternalFrameAdapter() {

			@Override
			public void internalFrameClosing(InternalFrameEvent e) {
				if (Objects.isNull(editor))
					return;

				MfInternalFrame_01.this.guiManager.tryStopCertainEditor(editor, ExecType.CONCURRENT);
			}
		});

		setResizable(true);
		setMaximizable(true);
		setIconifiable(true);
		setClosable(true);
		setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);

		getContentPane().setLayout(new BorderLayout());

		if (Objects.nonNull(editor)) {
			editor.addObverser(editorObverser);
		}

		this.editor = editor;
		syncEditor();
	}

	@Override
	protected void refreshLabels() {
		// TODO Auto-generated method stub

	}

	/**
	 * 获得内部窗口中的编辑器。
	 * 
	 * @return 内部窗口中的编辑器。
	 */
	public Editor getEditor() {
		return editor;
	}

	/**
	 * 设置内部窗口中的编辑器。
	 * 
	 * @param editor
	 *            内部窗口中的编辑器。
	 */
	public void setEditor(Editor editor) {
		if (Objects.nonNull(this.editor)) {
			this.editor.removeObverser(editorObverser);
		}

		if (Objects.nonNull(editor)) {
			editor.addObverser(editorObverser);
		}

		this.editor = editor;
		syncEditor();
	}

	private void syncEditor() {
		setTitle(null);
		getContentPane().removeAll();
		repaint();

		if (Objects.isNull(editor)) {
			return;
		}

		editor.getLock().readLock().lock();
		try {
			Component component = Objects.isNull(editor.getEditorView()) ? createDefaultComponent()
					: editor.getEditorView();
			getContentPane().add(component, BorderLayout.CENTER);
			setTitle(editor.getTitle());
			pack();
		} finally {
			editor.getLock().readLock().unlock();
		}

	}

}
