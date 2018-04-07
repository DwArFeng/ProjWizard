package com.dwarfeng.projwiz.core.view.struct;

import javax.swing.Icon;
import javax.swing.UIManager;

import com.dwarfeng.dutil.basic.prog.Buildable;
import com.dwarfeng.projwiz.core.view.eum.DialogMessage;
import com.dwarfeng.projwiz.core.view.eum.DialogOptionCombo;

/**
 * 确认对话框设置。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public final class ConfirmDialogSetting {

	/**
	 * 确认对话框设置构造器。
	 * 
	 * @author DwArFeng
	 * @since 0.0.3-alpha
	 */
	public final static class Builder implements Buildable<ConfirmDialogSetting> {

		private DialogMessage dialogMessage = DialogMessage.QUESTION_MESSAGE;
		private DialogOptionCombo dialogOptionCombo = DialogOptionCombo.YES_NO_CANCEL_OPTION;
		private Icon icon = null;
		private Object message = null;
		private String title = UIManager.getString("OptionPane.titleText");

		/**
		 * 新实例。
		 */
		public Builder() {
		}

		/**
		 * 设置确认对话框中的信息类型。
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
		 * 设置确认对话框的选项组合类型。
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
		 * 设置确认对话框中的图标。
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
		 * 设置确认对话框显示的信息。
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
		 * 设置确认对话框的标题。
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
		public ConfirmDialogSetting build() {
			return new ConfirmDialogSetting(message, title, dialogOptionCombo, dialogMessage, icon);
		}

	}

	private final DialogMessage dialogMessage;
	private final DialogOptionCombo dialogOptionCombo;
	private final Icon icon;
	private final Object message;
	private final String title;

	private ConfirmDialogSetting(Object message, String title, DialogOptionCombo dialogOptionCombo,
			DialogMessage dialogMessage, Icon icon) {
		this.message = message;
		this.title = title;
		this.dialogOptionCombo = dialogOptionCombo;
		this.dialogMessage = dialogMessage;
		this.icon = icon;
	}

	/**
	 * 获取确认对话框的信息类型。
	 * 
	 * @return 确认对话框的信息类型。
	 */
	public DialogMessage getDialogMessage() {
		return dialogMessage;
	}

	/**
	 * 获取确认对话框中的选项组合类型。
	 * 
	 * @return 确认对话框中的选项组合类型。
	 */
	public DialogOptionCombo getDialogOptionCombo() {
		return dialogOptionCombo;
	}

	/**
	 * 获取确认对话框的图标。
	 * 
	 * @return 确认对话框的图标。
	 */
	public Icon getIcon() {
		return icon;
	}

	/**
	 * 获取确认对话框显示的信息。
	 * 
	 * @return 确认对话框显示的信息。
	 */
	public Object getMessage() {
		return message;
	}

	/**
	 * 获取确认对话框显示的标题。
	 * 
	 * @return 确认对话框显示的标题。
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
		result = prime * result + ((dialogOptionCombo == null) ? 0 : dialogOptionCombo.hashCode());
		result = prime * result + ((icon == null) ? 0 : icon.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
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
		ConfirmDialogSetting other = (ConfirmDialogSetting) obj;
		if (dialogMessage != other.dialogMessage)
			return false;
		if (dialogOptionCombo != other.dialogOptionCombo)
			return false;
		if (icon == null) {
			if (other.icon != null)
				return false;
		} else if (!icon.equals(other.icon))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
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
		return "ConfirmDialogSetting [dialogMessage=" + dialogMessage + ", dialogOptionCombo=" + dialogOptionCombo
				+ ", icon=" + icon + ", message=" + message + ", title=" + title + "]";
	}

}
