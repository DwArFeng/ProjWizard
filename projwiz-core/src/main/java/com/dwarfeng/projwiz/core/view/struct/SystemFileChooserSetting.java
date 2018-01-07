package com.dwarfeng.projwiz.core.view.struct;

import java.io.File;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.swing.filechooser.FileFilter;

import com.dwarfeng.dutil.basic.prog.Buildable;
import com.dwarfeng.projwiz.core.model.eum.FileChooserDialogType;
import com.dwarfeng.projwiz.core.model.eum.FileSelectionMode;

/**
 * 系统文件选择设置。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public final class SystemFileChooserSetting {

	/**
	 * 文件选择器构造器。
	 * 
	 * @author DwArFeng
	 * @since 0.0.1-alpha
	 */
	public final static class Builder implements Buildable<SystemFileChooserSetting> {

		private boolean acceptAllFileFilterUsed = false;
		private boolean controlButtonsAreShown = true;
		private File currentDirectory = null;
		private FileChooserDialogType dialogType = FileChooserDialogType.OPEN_DIALOG;
		private boolean dragEnabled = false;
		private Set<FileFilter> fileFilters = new HashSet<>();
		private boolean fileHidingEnabled = false;
		private FileSelectionMode fileSelectionMode = FileSelectionMode.FILES_AND_DIRECTORIES;
		private boolean multiSelectionEnabled = false;

		/**
		 * 设置是否接受所所有文件过滤器。
		 * 
		 * @param aFlag
		 *            是否接受。
		 * @return 构造器自身。
		 */
		public Builder acceptAllFileFilterUsed(boolean aFlag) {
			acceptAllFileFilterUsed = aFlag;
			return this;
		}

		/**
		 * 添加一个文件过滤器。
		 * 
		 * @param fileFilter
		 *            指定的文件过滤器。
		 * @return 构造器自身。
		 * @throws NullPointerException
		 *             入口参数为 <code>null</code>。
		 */
		public Builder addFileFilters(FileFilter fileFilter) {
			Objects.requireNonNull(fileFilter, "入口参数 fileFilter 不能为 null。");
			fileFilters.add(fileFilter);
			return this;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SystemFileChooserSetting build() {
			return new SystemFileChooserSetting(acceptAllFileFilterUsed, controlButtonsAreShown, currentDirectory,
					dialogType, dragEnabled, fileFilters, fileHidingEnabled, fileSelectionMode, multiSelectionEnabled);
		}

		/**
		 * 清除所有的文件过滤器。
		 * 
		 * @return 构造器自身。
		 */
		public Builder clearFileFilters() {
			fileFilters.clear();
			return this;
		}

		/**
		 * 设置是否显示控制按钮。
		 * 
		 * @param aFlag
		 *            是否显示。
		 * @return 构造器自身。
		 */
		public Builder controlButtonsAreShown(boolean aFlag) {
			controlButtonsAreShown = aFlag;
			return this;
		}

		/**
		 * 设置当前的目录。
		 * 
		 * @param file
		 *            当前的目录。
		 * @return 构造器自身。
		 */
		public Builder currentDirectory(File file) {
			currentDirectory = file;
			return this;
		}

		/**
		 * 设置当前的对话框类型。
		 * 
		 * @param dialogType
		 *            对话框类型。
		 * @return 构造器自身。
		 * @throws NullPointerException
		 *             入口参数为 <code>null</code>。
		 */
		public Builder dialogType(FileChooserDialogType dialogType) {
			Objects.requireNonNull(dialogType, "入口参数 dialogType 不能为 null。");
			this.dialogType = dialogType;
			return this;
		}

		/**
		 * 设置是否允许拖拽。
		 * 
		 * @param aFlag
		 *            是否允许。
		 * @return 构造器自身。
		 */
		public Builder dragEnabled(boolean aFlag) {
			dragEnabled = aFlag;
			return this;
		}

		/**
		 * 设置是否显示隐藏文件。
		 * 
		 * @param aFlag
		 *            是否显示。
		 * @return 构造器自身。
		 */
		public Builder fileHidingEnabled(boolean aFlag) {
			fileHidingEnabled = aFlag;
			return this;
		}

		/**
		 * 设置文件的选择模式。
		 * 
		 * @param mode
		 *            文件的选择模式。
		 * @return 构造器自身。
		 * @throws NullPointerException
		 *             入口参数为 <code>null</code>。
		 */
		public Builder fileSelectionMode(FileSelectionMode mode) {
			Objects.requireNonNull(mode, "入口参数 mode 不能为 null。");
			fileSelectionMode = mode;
			return this;
		}

		/**
		 * 设置是否可以选择多个文件。
		 * 
		 * @param aFlag
		 *            是否可以选择多个文件。
		 * @return 构造器自身。
		 */
		public Builder mutiSelectionEnabled(boolean aFlag) {
			multiSelectionEnabled = aFlag;
			return this;
		}

		/**
		 * 移除一个文件过滤器。
		 * 
		 * @param fileFilter
		 *            指定的文件过滤器。
		 * @return 构造器自身。
		 */
		public Builder removeFileFilters(FileFilter fileFilter) {
			fileFilters.remove(fileFilter);
			return this;
		}

	}

	private final boolean acceptAllFileFilterUsed;
	private final boolean controlButtonsAreShown;
	private final File currentDirectory;
	private final FileChooserDialogType dialogType;
	private final boolean dragEnabled;
	private final Set<FileFilter> fileFilters;
	private final boolean fileHidingEnabled;
	private final FileSelectionMode fileSelectionMode;
	private final boolean multiSelectionEnabled;

	private SystemFileChooserSetting(boolean acceptAllFileFilterUsed, boolean controlButtonsAreShown,
			File currentDirectory, FileChooserDialogType dialogType, boolean dragEnabled, Set<FileFilter> fileFilters,
			boolean fileHidingEnabled, FileSelectionMode fileSelectionMode, boolean mutiSelectionEnabled) {
		this.acceptAllFileFilterUsed = acceptAllFileFilterUsed;
		this.controlButtonsAreShown = controlButtonsAreShown;
		this.currentDirectory = currentDirectory;
		this.dialogType = dialogType;
		this.dragEnabled = dragEnabled;
		this.fileFilters = fileFilters;
		this.fileHidingEnabled = fileHidingEnabled;
		this.fileSelectionMode = fileSelectionMode;
		this.multiSelectionEnabled = mutiSelectionEnabled;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof SystemFileChooserSetting)) {
			return false;
		}
		SystemFileChooserSetting other = (SystemFileChooserSetting) obj;
		if (acceptAllFileFilterUsed != other.acceptAllFileFilterUsed) {
			return false;
		}
		if (controlButtonsAreShown != other.controlButtonsAreShown) {
			return false;
		}
		if (currentDirectory == null) {
			if (other.currentDirectory != null) {
				return false;
			}
		} else if (!currentDirectory.equals(other.currentDirectory)) {
			return false;
		}
		if (dialogType != other.dialogType) {
			return false;
		}
		if (dragEnabled != other.dragEnabled) {
			return false;
		}
		if (fileFilters == null) {
			if (other.fileFilters != null) {
				return false;
			}
		} else if (!fileFilters.equals(other.fileFilters)) {
			return false;
		}
		if (fileHidingEnabled != other.fileHidingEnabled) {
			return false;
		}
		if (fileSelectionMode != other.fileSelectionMode) {
			return false;
		}
		if (multiSelectionEnabled != other.multiSelectionEnabled) {
			return false;
		}
		return true;
	}

	/**
	 * 获取当前的目录。
	 * 
	 * @return 当前的目录。
	 */
	public File getCurrentDirectory() {
		return currentDirectory;
	}

	/**
	 * 获取对话框的类型。
	 * 
	 * @return 对话框的类型。
	 */
	public FileChooserDialogType getDialogType() {
		return dialogType;
	}

	/**
	 * 获取文件选择器集合。
	 * 
	 * @return 文件选择器集合。
	 */
	public Set<FileFilter> getFileFilters() {
		return fileFilters;
	}

	/**
	 * 获取文件的选择模式。
	 * 
	 * @return 文件的选择模式。
	 */
	public FileSelectionMode getFileSelectionMode() {
		return fileSelectionMode;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (acceptAllFileFilterUsed ? 1231 : 1237);
		result = prime * result + (controlButtonsAreShown ? 1231 : 1237);
		result = prime * result + ((currentDirectory == null) ? 0 : currentDirectory.hashCode());
		result = prime * result + ((dialogType == null) ? 0 : dialogType.hashCode());
		result = prime * result + (dragEnabled ? 1231 : 1237);
		result = prime * result + ((fileFilters == null) ? 0 : fileFilters.hashCode());
		result = prime * result + (fileHidingEnabled ? 1231 : 1237);
		result = prime * result + ((fileSelectionMode == null) ? 0 : fileSelectionMode.hashCode());
		result = prime * result + (multiSelectionEnabled ? 1231 : 1237);
		return result;
	}

	/**
	 * 获取所有文件过滤器是否被启用。
	 * 
	 * @return 所有文件过滤器是否被启用。
	 */
	public boolean isAcceptAllFileFilterUsed() {
		return acceptAllFileFilterUsed;
	}

	/**
	 * 获取控制按钮是否被选用。
	 * 
	 * @return 控制按钮是否被选用。
	 */
	public boolean isControlButtonsAreShown() {
		return controlButtonsAreShown;
	}

	/**
	 * 获取拖拽是否被启用。
	 * 
	 * @return 拖拽是否被启用。
	 */
	public boolean isDragEnabled() {
		return dragEnabled;
	}

	/**
	 * 获取拖拽是否允许。
	 * 
	 * @return 拖拽是否允许。
	 */
	public boolean isFileHidingEnabled() {
		return fileHidingEnabled;
	}

	/**
	 * 获取多重选择是否允许。
	 * 
	 * @return 多重选择是否允许。
	 */
	public boolean isMultiSelectionEnabled() {
		return multiSelectionEnabled;
	}

}
