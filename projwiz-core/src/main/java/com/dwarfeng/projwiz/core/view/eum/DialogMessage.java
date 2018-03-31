package com.dwarfeng.projwiz.core.view.eum;

import javax.swing.JOptionPane;

/**
 * 对话框中的信息类型。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public enum DialogMessage {
	/** 表示错误的信息类型。 */
	ERROR_MESSAGE(JOptionPane.ERROR_MESSAGE),
	/** 表示信息的信息类型。 */
	INFORMATION_MESSAGE(JOptionPane.INFORMATION_MESSAGE),
	/** 表示警告的信息类型。 */
	WARNING_MESSAGE(JOptionPane.WARNING_MESSAGE),
	/** 表示提问的信息类型。 */
	QUESTION_MESSAGE(JOptionPane.QUESTION_MESSAGE),
	/** 表示正常的信息类型。 */
	PLAIN_MESSAGE(JOptionPane.PLAIN_MESSAGE),

	;

	private final int value;

	private DialogMessage(int value) {
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