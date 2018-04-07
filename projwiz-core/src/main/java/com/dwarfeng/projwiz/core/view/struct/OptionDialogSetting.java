package com.dwarfeng.projwiz.core.view.struct;

import java.util.Arrays;

import javax.swing.Icon;

import com.dwarfeng.dutil.basic.prog.Buildable;
import com.dwarfeng.projwiz.core.view.eum.DialogMessage;
import com.dwarfeng.projwiz.core.view.eum.DialogOptionCombo;

/**
 * 选项对话框设置。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public final class OptionDialogSetting {

	/**
	 * 选项对话框设置构造器。
	 * 
	 * @author DwArFeng
	 * @since 0.0.3-alpha
	 */
	public static final class Builder implements Buildable<OptionDialogSetting> {

		private Object message;
		private String title;
		private DialogOptionCombo dialogOptionCombo = DialogOptionCombo.YES_NO_OPTION;
		private DialogMessage dialogMessage = DialogMessage.QUESTION_MESSAGE;
		private Icon icon;
		private Object[] options;
		private Object initialValue;

		/**
		 * 新实例。
		 */
		public Builder() {
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public OptionDialogSetting build() {
			return new OptionDialogSetting(message, title, dialogOptionCombo, dialogMessage, icon, options,
					initialValue);
		}

		/**
		 * 设置选线对话框中的信息类型。
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
		 * 设置选线对话框的选项组合类型。
		 * 
		 * @param dialogOptionCombo
		 *            指定的选项组合类型。
		 * @return 构造器自身。
		 */
		public Builder setDialogOptionCombo(DialogOptionCombo dialogOptionCombo) {
			this.dialogOptionCombo = dialogOptionCombo;
			return this;
		}

		/**
		 * 设置选线对话框中的图标。
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
		 * 设置选线对话框的初始值。
		 * 
		 * @param initialValue
		 *            指定的初始值。
		 * @return 构造器自身。
		 */
		public Builder setInitialValue(Object initialValue) {
			this.initialValue = initialValue;
			return this;
		}

		/**
		 * 设置选线对话框显示的信息。
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
		 * 设置选线对话框的选项。
		 * 
		 * @param options
		 *            指定的选项组成的数组。
		 * @return 构造器自身。
		 */
		public Builder setOptions(Object[] options) {
			this.options = options;
			return this;
		}

		/**
		 * 设置选线对话框的标题。
		 * 
		 * @param title
		 *            指定的标题。
		 * @return 构造器自身。
		 */
		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

	}

	private final Object message;
	private final String title;
	private final DialogOptionCombo dialogOptionCombo;
	private final DialogMessage dialogMessage;
	private final Icon icon;
	private final Object[] options;
	private final Object initialValue;

	private OptionDialogSetting(Object message, String title, DialogOptionCombo dialogOptionCombo,
			DialogMessage dialogMessage, Icon icon, Object[] options, Object initialValue) {
		this.message = message;
		this.title = title;
		this.dialogOptionCombo = dialogOptionCombo;
		this.dialogMessage = dialogMessage;
		this.icon = icon;
		this.options = options;
		this.initialValue = initialValue;
	}

	/**
	 * 获取选项对话框的信息类型。
	 * 
	 * @return 选项对话框的信息类型。
	 */
	public DialogMessage getDialogMessage() {
		return dialogMessage;
	}

	/**
	 * 获取选项对话框中的选项组合类型。
	 * 
	 * @return 选项对话框中的选项组合类型。
	 */
	public DialogOptionCombo getDialogOptionCombo() {
		return dialogOptionCombo;
	}

	/**
	 * 获取选项对话框的图标。
	 * 
	 * @return 选项对话框的图标。
	 */
	public Icon getIcon() {
		return icon;
	}

	/**
	 * 获取选项对话框的初始值。
	 * 
	 * @return 选项对话框的初始值。
	 */
	public Object getInitialValue() {
		return initialValue;
	}

	/**
	 * 获取选项对话框显示的信息。
	 * 
	 * @return 选项对话框显示的信息。
	 */
	public Object getMessage() {
		return message;
	}

	/**
	 * 获取选项对话框的选项。
	 * 
	 * @return 选项对话框的选项。
	 */
	public Object[] getOptions() {
		return options;
	}

	/**
	 * 获取选项对话框显示的标题。
	 * 
	 * @return 选项对话框显示的标题。
	 */
	public String getTitle() {
		return title;
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
		OptionDialogSetting other = (OptionDialogSetting) obj;
		if (dialogMessage != other.dialogMessage)
			return false;
		if (dialogOptionCombo != other.dialogOptionCombo)
			return false;
		if (icon == null) {
			if (other.icon != null)
				return false;
		} else if (!icon.equals(other.icon))
			return false;
		if (initialValue == null) {
			if (other.initialValue != null)
				return false;
		} else if (!initialValue.equals(other.initialValue))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (!Arrays.equals(options, other.options))
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dialogMessage == null) ? 0 : dialogMessage.hashCode());
		result = prime * result + ((dialogOptionCombo == null) ? 0 : dialogOptionCombo.hashCode());
		result = prime * result + ((icon == null) ? 0 : icon.hashCode());
		result = prime * result + ((initialValue == null) ? 0 : initialValue.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + Arrays.hashCode(options);
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "OptionDialogSetting [message=" + message + ", title=" + title + ", dialogOptionCombo="
				+ dialogOptionCombo + ", dialogMessage=" + dialogMessage + ", icon=" + icon + ", options="
				+ Arrays.toString(options) + ", initialValue=" + initialValue + "]";
	}

}
