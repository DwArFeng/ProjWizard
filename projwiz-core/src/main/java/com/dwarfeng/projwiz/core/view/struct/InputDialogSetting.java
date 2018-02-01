package com.dwarfeng.projwiz.core.view.struct;

import java.util.Arrays;

import javax.swing.Icon;
import javax.swing.UIManager;

import com.dwarfeng.dutil.basic.prog.Buildable;
import com.dwarfeng.projwiz.core.model.eum.DialogMessage;

public final class InputDialogSetting {

	/**
	 * 输入对话框设置构造器。
	 * 
	 * @author DwArFeng
	 * @since 0.0.3-alpha
	 */
	public final static class Builder implements Buildable<InputDialogSetting> {

		private DialogMessage dialogMessage = DialogMessage.QUESTION_MESSAGE;
		private Icon icon;
		private Object initialSelectionValue;
		private Object message;
		private Object[] selectionValues;
		private String title = UIManager.getString("OptionPane.inputDialogTitle");

		public Builder() {
		}

		/**
		 * 设置输入对话框中的信息类型。
		 * 
		 * @param dialogMessage
		 *            指定的信息类型。
		 * @return 构造器自身。
		 */
		public Builder setDialogMessage(DialogMessage dialogMessage) {
			this.dialogMessage = dialogMessage;
			return this;
		}

		/**
		 * 设置输入对话框中的图标。
		 * 
		 * @param icon
		 *            指定的图标。
		 * @return 构造器自身。
		 */
		public Builder setIcon(Icon icon) {
			this.icon = icon;
			return this;
		}

		/**
		 * 设置输入对话框的初始对戏。
		 * 
		 * @param initialSelectionValue
		 *            指定的初始对象。
		 * @return 构造器自身。
		 */
		public Builder setInitialSelectionValue(Object initialSelectionValue) {
			this.initialSelectionValue = initialSelectionValue;
			return this;
		}

		/**
		 * 设置输入对话框显示的信息。
		 * 
		 * @param message
		 *            指定的信息。
		 * @return 构造器自身。
		 */
		public Builder setMessage(Object message) {
			this.message = message;
			return this;
		}

		/**
		 * 设置输入对话框的可选对象。
		 * 
		 * @param selectionValues
		 *            指定的可选对象组成的数组。
		 * @return 构造器自身。
		 */
		public Builder setSelectionValues(Object[] selectionValues) {
			this.selectionValues = selectionValues;
			return this;
		}

		/**
		 * 设置输入对话框的标题。
		 * 
		 * @param title
		 *            指定的标题。
		 * @return 构造器自身。
		 */
		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public InputDialogSetting build() {
			return new InputDialogSetting(message, title, dialogMessage, icon, selectionValues, initialSelectionValue);
		}

	}

	private final DialogMessage dialogMessage;
	private final Icon icon;
	private final Object initialSelectionValue;
	private final Object message;
	private final Object[] selectionValues;
	private final String title;

	private InputDialogSetting(Object message, String title, DialogMessage dialogMessage, Icon icon,
			Object[] selectionValues, Object initialSelectionValue) {
		this.message = message;
		this.title = title;
		this.dialogMessage = dialogMessage;
		this.icon = icon;
		this.selectionValues = selectionValues;
		this.initialSelectionValue = initialSelectionValue;
	}

	/**
	 * 获取输入对话框的信息类型。
	 * 
	 * @return 输入对话框的信息类型。
	 */
	public DialogMessage getDialogMessage() {
		return dialogMessage;
	}

	/**
	 * 获取输入对话框的图标。
	 * 
	 * @return 输入对话框的图标。
	 */
	public Icon getIcon() {
		return icon;
	}

	/**
	 * 获取输入对话框中的初始选择对象。
	 * 
	 * @return 输入对话框中的初始选择对象。
	 */
	public Object getInitialSelectionValue() {
		return initialSelectionValue;
	}

	/**
	 * 获取输入对话框显示的信息。
	 * 
	 * @return 输入对话框显示的信息。
	 */
	public Object getMessage() {
		return message;
	}

	/**
	 * 获取输入对话框中可选的输入对象。
	 * 
	 * @return 输入对话框中可选的输入对象。
	 */
	public Object[] getSelectionValues() {
		return selectionValues;
	}

	/**
	 * 获取输入对话框的标题。
	 * 
	 * @return 输入对话框的标题。
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dialogMessage == null) ? 0 : dialogMessage.hashCode());
		result = prime * result + ((icon == null) ? 0 : icon.hashCode());
		result = prime * result + ((initialSelectionValue == null) ? 0 : initialSelectionValue.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + Arrays.hashCode(selectionValues);
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InputDialogSetting other = (InputDialogSetting) obj;
		if (dialogMessage != other.dialogMessage)
			return false;
		if (icon == null) {
			if (other.icon != null)
				return false;
		} else if (!icon.equals(other.icon))
			return false;
		if (initialSelectionValue == null) {
			if (other.initialSelectionValue != null)
				return false;
		} else if (!initialSelectionValue.equals(other.initialSelectionValue))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (!Arrays.equals(selectionValues, other.selectionValues))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "InputDialogSetting [dialogMessage=" + dialogMessage + ", icon=" + icon + ", initialSelectionValue="
				+ initialSelectionValue + ", message=" + message + ", selectionValues="
				+ Arrays.toString(selectionValues) + ", title=" + title + "]";
	}

}
