package com.dwarfeng.projwiz.basic4.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import com.dwarfeng.dutil.develop.i18n.I18nHandler;
import com.dwarfeng.projwiz.basic4.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.view.eum.DialogOption;
import com.dwarfeng.projwiz.raefrm.RaeDialog;

public class FofpNewFileDialog extends RaeDialog {

	private final JPanel contentPane;
	private final JLabel label2;
	private final JTextArea textArea;
	private final JButton okButton;
	private final JButton cancelButton;
	private DialogOption option = DialogOption.CLOSED;

	/**
	 * 新实例。
	 */
	public FofpNewFileDialog() {
		this(null);
	}

	/**
	 * 新实例。
	 * 
	 * @param i18nHandler
	 *            指定的国际化处理器。
	 */
	public FofpNewFileDialog(I18nHandler i18nHandler) {
		super(i18nHandler);

		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle(label(LabelStringKey.FOFP_PROCESSOR_NEWFILE_0));
		setPreferredSize(new Dimension(450, 300));

		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 54, 50, 0 };
		gridBagLayout.rowHeights = new int[] { -3, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		panel.setLayout(gridBagLayout);
		contentPane.add(panel, BorderLayout.CENTER);

		label2 = new JLabel(label(LabelStringKey.FOFP_PROCESSOR_NEWFILE_1));
		GridBagConstraints gbc_label2 = new GridBagConstraints();
		gbc_label2.anchor = GridBagConstraints.WEST;
		gbc_label2.insets = new Insets(0, 0, 5, 5);
		gbc_label2.gridx = 0;
		gbc_label2.gridy = 0;
		panel.add(label2, gbc_label2);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		panel.add(scrollPane, gbc_scrollPane);

		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);

		JPanel panel_1 = new JPanel();
		FlowLayout fl_panel_1 = (FlowLayout) panel_1.getLayout();
		fl_panel_1.setAlignment(FlowLayout.RIGHT);
		getContentPane().add(panel_1, BorderLayout.SOUTH);

		okButton = new JButton(label(LabelStringKey.FOFP_PROCESSOR_NEWFILE_2));
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				option = DialogOption.OK_YES;
				dispose();
			}
		});
		panel_1.add(okButton);

		cancelButton = new JButton(label(LabelStringKey.FOFP_PROCESSOR_NEWFILE_3));
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				option = DialogOption.CANCEL;
				dispose();
			}
		});

		contentPane.registerKeyboardAction(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				option = DialogOption.CANCEL;
				dispose();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		panel_1.add(cancelButton);

		getRootPane().setDefaultButton(okButton);

		pack();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void refreshLabels() {
		setTitle(label(LabelStringKey.FOFP_PROCESSOR_NEWFILE_0));
		label2.setText(label(LabelStringKey.FOFP_PROCESSOR_NEWFILE_1));
		okButton.setText(label(LabelStringKey.FOFP_PROCESSOR_NEWFILE_2));
		cancelButton.setText(label(LabelStringKey.FOFP_PROCESSOR_NEWFILE_3));
	}

	/**
	 * 获取文件的描述。
	 * 
	 * @return 文件的描述。
	 */
	public String getFileDescription() {
		return textArea.getText();
	}

	/**
	 * 设置文件的描述。
	 * 
	 * @param value
	 *            文件的描述。
	 */
	public void setFileDescription(String value) {
		this.textArea.setText(value);
	}

	/**
	 * 返回对话框选择的选项。
	 * 
	 * @return the option 对话框选择的选项。
	 */
	public DialogOption getOption() {
		return option;
	}

}
