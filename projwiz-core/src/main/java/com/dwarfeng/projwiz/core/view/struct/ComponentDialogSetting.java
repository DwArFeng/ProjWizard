package com.dwarfeng.projwiz.core.view.struct;

import java.awt.Image;
import java.awt.Window;

import javax.swing.JDialog;

import com.dwarfeng.dutil.basic.prog.Buildable;

/**
 * 组件对话框设置。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public final class ComponentDialogSetting {

	/**
	 * 组件对话框设置构造器。
	 * 
	 * @author DwArFeng
	 * @since 0.0.3-alpha
	 */
	public static final class Builder implements Buildable<ComponentDialogSetting> {

		private String title;
		private Image iconImage;
		private boolean resizeable = false;
		private boolean undecorated = false;
		private Window.Type type;

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ComponentDialogSetting build() {
			return new ComponentDialogSetting(title, iconImage, resizeable, undecorated, type);
		}

		/**
		 * 设置组件对话框的图标图片。
		 * 
		 * @param iconImage
		 *            指定的图标图片。
		 * @return 构造器自身。
		 */
		public Builder setIconImage(Image iconImage) {
			this.iconImage = iconImage;
			return this;
		}

		/**
		 * 设置组件对话框是否允许调整大小。
		 * 
		 * @param resizeable
		 *            是否允许调整大小。
		 * @return 构造器自身。
		 */
		public Builder setResizeable(boolean resizeable) {
			this.resizeable = resizeable;
			return this;
		}

		/**
		 * 设置组件对话框的标题。
		 * 
		 * @param title
		 *            组件对话框的标题。
		 * @return 构造器自身。
		 */
		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		/**
		 * 设置组件对话框的类型。
		 * 
		 * @param type
		 *            组件对话框的类型。
		 * @return 构造器自身。
		 */
		public Builder setType(Window.Type type) {
			this.type = type;
			return this;
		}

		/**
		 * 设置组件对话框是否不包含边框。
		 * 
		 * @param undecorated
		 *            是否不包含边框。
		 * @return 构造器自身。
		 */
		public Builder setUndecorated(boolean undecorated) {
			this.undecorated = undecorated;
			return this;
		}

	}

	private final String title;
	private final Image iconImage;
	private final boolean resizeable;
	private final boolean undecorated;
	private final Window.Type type;

	private ComponentDialogSetting(String title, Image iconImage, boolean resizeable, boolean undecorated,
			Window.Type type) {
		this.title = title;
		this.iconImage = iconImage;
		this.resizeable = resizeable;
		this.undecorated = undecorated;
		this.type = type;
	}

	/**
	 * 获取组件对话框的图标图片。
	 * 
	 * @return 组件对话框的图标图片。
	 */
	public Image getIconImage() {
		return iconImage;
	}

	/**
	 * 获取组件对话框的标题。
	 * 
	 * @return 组件对话框的标题。
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 获取组件对话框的类型。
	 * 
	 * @return the type 组件对话框的类型。
	 */
	public JDialog.Type getType() {
		return type;
	}

	/**
	 * 获取组件对话框是否允许调整大小。
	 * 
	 * @return 组件对话框是否允许调整大小。
	 */
	public boolean isResizeable() {
		return resizeable;
	}

	/**
	 * 获取组件对话框是否不包含边框。
	 * 
	 * @return 对话框组件是否不包含边框。
	 */
	public boolean isUndecorated() {
		return undecorated;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((iconImage == null) ? 0 : iconImage.hashCode());
		result = prime * result + (resizeable ? 1231 : 1237);
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + (undecorated ? 1231 : 1237);
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
		ComponentDialogSetting other = (ComponentDialogSetting) obj;
		if (iconImage == null) {
			if (other.iconImage != null)
				return false;
		} else if (!iconImage.equals(other.iconImage))
			return false;
		if (resizeable != other.resizeable)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (type != other.type)
			return false;
		if (undecorated != other.undecorated)
			return false;
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "ComponentDialogSetting [title=" + title + ", iconImage=" + iconImage + ", resizeable=" + resizeable
				+ ", undecorated=" + undecorated + ", type=" + type + "]";
	}

}
