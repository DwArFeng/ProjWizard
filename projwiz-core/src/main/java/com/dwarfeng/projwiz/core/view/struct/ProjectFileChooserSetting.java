package com.dwarfeng.projwiz.core.view.struct;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.swing.filechooser.FileFilter;

import com.dwarfeng.dutil.basic.prog.Buildable;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.view.eum.ChooserDialogType;
import com.dwarfeng.projwiz.core.view.eum.FileSelectionMode;
import com.dwarfeng.projwiz.core.view.gui.ProjectFileChooser;

/**
 * 文件选择设置。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public final class ProjectFileChooserSetting {

	/**
	 * 文件选择设置构造器。
	 * 
	 * @author DwArFeng
	 * @since 0.0.1-alpha
	 */
	public final static class Builder implements Buildable<ProjectFileChooserSetting> {

		private boolean acceptAllFileFilterUsed = false;
		private boolean controlButtonsAreShown = true;
		private File currentDirectory = null;
		private ChooserDialogType chooserDialogType = ChooserDialogType.OPEN_DIALOG;
		private boolean dragEnabled = false;
		private Set<ProjectFileChooser.FileFilter> fileFilters = new HashSet<>();
		private boolean fileHidingEnabled = false;
		private FileSelectionMode fileSelectionMode = FileSelectionMode.FILES_AND_DIRECTORIES;
		private boolean multiSelectionEnabled = false;

		/**
		 * 添加一个文件过滤器。
		 * 
		 * @param fileFilter
		 *            指定的文件过滤器。
		 * @return 构造器自身。
		 * @throws NullPointerException
		 *             入口参数为 <code>null</code>。
		 */
		public Builder addFileFilters(ProjectFileChooser.FileFilter fileFilter) {
			Objects.requireNonNull(fileFilter, "入口参数 fileFilter 不能为 null。");
			fileFilters.add(fileFilter);
			return this;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ProjectFileChooserSetting build() {
			return new ProjectFileChooserSetting(acceptAllFileFilterUsed, controlButtonsAreShown, currentDirectory,
					chooserDialogType, dragEnabled, fileFilters, fileHidingEnabled, fileSelectionMode,
					multiSelectionEnabled);
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

		/**
		 * 设置是否接受所所有文件过滤器。
		 * 
		 * @param aFlag
		 *            是否接受。
		 * @return 构造器自身。
		 */
		public Builder setAcceptAllFileFilterUsed(boolean aFlag) {
			acceptAllFileFilterUsed = aFlag;
			return this;
		}

		/**
		 * 设置当前的对话框类型。
		 * 
		 * @param chooserDialogType
		 *            对话框类型。
		 * @return 构造器自身。
		 * @throws NullPointerException
		 *             入口参数为 <code>null</code>。
		 */
		public Builder setChooserDialogType(ChooserDialogType chooserDialogType) {
			Objects.requireNonNull(chooserDialogType, "入口参数 chooserDialogType 不能为 null。");
			this.chooserDialogType = chooserDialogType;
			return this;
		}

		/**
		 * 设置是否显示控制按钮。
		 * 
		 * @param aFlag
		 *            是否显示。
		 * @return 构造器自身。
		 */
		public Builder setControlButtonsAreShown(boolean aFlag) {
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
		public Builder setCurrentDirectory(File file) {
			currentDirectory = file;
			return this;
		}

		/**
		 * 设置是否允许拖拽。
		 * 
		 * @param aFlag
		 *            是否允许。
		 * @return 构造器自身。
		 */
		public Builder setDragEnabled(boolean aFlag) {
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
		public Builder setFileHidingEnabled(boolean aFlag) {
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
		public Builder setFileSelectionMode(FileSelectionMode mode) {
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
		public Builder setMutiSelectionEnabled(boolean aFlag) {
			multiSelectionEnabled = aFlag;
			return this;
		}

	}

	private final boolean acceptAllFileFilterUsed;
	private final boolean controlButtonsAreShown;
	private final File currentDirectory;
	private final ChooserDialogType chooserDialogType;
	private final boolean dragEnabled;
	private final Set<ProjectFileChooser.FileFilter> fileFilters;
	private final boolean fileHidingEnabled;
	private final FileSelectionMode fileSelectionMode;
	private final boolean multiSelectionEnabled;

	private ProjectFileChooserSetting(boolean acceptAllFileFilterUsed, boolean controlButtonsAreShown,
			File currentDirectory, ChooserDialogType chooserDialogType, boolean dragEnabled,
			Set<ProjectFileChooser.FileFilter> fileFilters, boolean fileHidingEnabled,
			FileSelectionMode fileSelectionMode, boolean mutiSelectionEnabled) {
		this.acceptAllFileFilterUsed = acceptAllFileFilterUsed;
		this.controlButtonsAreShown = controlButtonsAreShown;
		this.currentDirectory = currentDirectory;
		this.chooserDialogType = chooserDialogType;
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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ProjectFileChooserSetting))
			return false;
		ProjectFileChooserSetting other = (ProjectFileChooserSetting) obj;
		if (acceptAllFileFilterUsed != other.acceptAllFileFilterUsed)
			return false;
		if (chooserDialogType != other.chooserDialogType)
			return false;
		if (controlButtonsAreShown != other.controlButtonsAreShown)
			return false;
		if (currentDirectory == null) {
			if (other.currentDirectory != null)
				return false;
		} else if (!currentDirectory.equals(other.currentDirectory))
			return false;
		if (dragEnabled != other.dragEnabled)
			return false;
		if (fileFilters == null) {
			if (other.fileFilters != null)
				return false;
		} else if (!fileFilters.equals(other.fileFilters))
			return false;
		if (fileHidingEnabled != other.fileHidingEnabled)
			return false;
		if (fileSelectionMode != other.fileSelectionMode)
			return false;
		if (multiSelectionEnabled != other.multiSelectionEnabled)
			return false;
		return true;
	}

	/**
	 * 获取对话框的类型。
	 * 
	 * @return 对话框的类型。
	 */
	public ChooserDialogType getChooserDialogType() {
		return chooserDialogType;
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
	 * 获取文件选择器集合。
	 * 
	 * @return 文件选择器集合。
	 */
	public Set<ProjectFileChooser.FileFilter> getFileFilters() {
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
		result = prime * result + ((chooserDialogType == null) ? 0 : chooserDialogType.hashCode());
		result = prime * result + (controlButtonsAreShown ? 1231 : 1237);
		result = prime * result + ((currentDirectory == null) ? 0 : currentDirectory.hashCode());
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
	 * 获取控制按钮是否显示。
	 * 
	 * @return 控制按钮是否显示。
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "ProjectFileChooserSetting [acceptAllFileFilterUsed=" + acceptAllFileFilterUsed
				+ ", controlButtonsAreShown=" + controlButtonsAreShown + ", currentDirectory=" + currentDirectory
				+ ", chooserDialogType=" + chooserDialogType + ", dragEnabled=" + dragEnabled + ", fileFilters="
				+ fileFilters + ", fileHidingEnabled=" + fileHidingEnabled + ", fileSelectionMode=" + fileSelectionMode
				+ ", multiSelectionEnabled=" + multiSelectionEnabled + "]";
	}

}
