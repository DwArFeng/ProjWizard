package com.dwarfeng.projwiz.core.view.eum;

import javax.swing.JOptionPane;

/**
 * 对话框中的选项组合类型。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public enum DialogOptionCombo {

	/** 默认的选项组合。 */
	DEFAULT_OPTION(JOptionPane.DEFAULT_OPTION),
	/** 是-否选项组合 */
	YES_NO_OPTION(JOptionPane.YES_NO_OPTION),
	/** 是-否-取消选项组合 */
	YES_NO_CANCEL_OPTION(JOptionPane.YES_NO_CANCEL_OPTION),
	/** 确定-取消选项组合 */
	OK_CANCEL_OPTION(JOptionPane.OK_CANCEL_OPTION),

	;

	private final int value;

	private DialogOptionCombo(int value) {
		this.value = value;
	}

	/**
	 * 获取实例的值。该值与 {@link JOptionPane}的相关字段是一致的。
	 * 
	 * @return 实例的值。
	 */
	public int getValue() {
		return value;
	}

}