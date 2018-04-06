package com.dwarfeng.projwiz.core.view.struct;

import com.dwarfeng.dutil.basic.prog.Buildable;
import com.dwarfeng.dutil.basic.prog.Filter;
import com.dwarfeng.projwiz.core.model.struct.Module;
import com.dwarfeng.projwiz.core.view.eum.ChooserDialogType;

/**
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public final class ModuleChooserSetting {

	/**
	 * 组件选择设置构造器。
	 * 
	 * @author DwArFeng
	 * @since 0.0.1-alpha
	 */
	public final static class Builder implements Buildable<ModuleChooserSetting> {

		private boolean controlButtonsAreShown;
		private ChooserDialogType chooserDialogType;
		private Filter<Module> moduleFilter;
		private boolean multiSelectionEnabled;

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ModuleChooserSetting build() {
			return new ModuleChooserSetting(controlButtonsAreShown, chooserDialogType, moduleFilter,
					multiSelectionEnabled);
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
			this.chooserDialogType = chooserDialogType;
			return this;
		}

		/**
		 * 获取当前的组件过滤器。
		 * 
		 * @param moduleFilter
		 *            指定的组件过滤器。
		 */
		public Builder setModuleFilter(Filter<Module> moduleFilter) {
			this.moduleFilter = moduleFilter;
			return this;
		}

		/**
		 * 设置是否显示控制按钮。
		 * 
		 * @param aFlag
		 *            是否显示。
		 * @return 构造器自身。
		 */
		public Builder setControlButtonsAreShown(boolean controlButtonsAreShown) {
			this.controlButtonsAreShown = controlButtonsAreShown;
			return this;
		}

		/**
		 * 设置是否可以选择多个组件。
		 * 
		 * @param aFlag
		 *            是否可以选择多个组件。
		 * @return 构造器自身。
		 */
		public Builder setMultiSelectionEnabled(boolean multiSelectionEnabled) {
			this.multiSelectionEnabled = multiSelectionEnabled;
			return this;
		}

	}

	private final boolean controlButtonsAreShown;
	private final ChooserDialogType chooserDialogType;
	private final Filter<Module> moduleFilter;
	private final boolean multiSelectionEnabled;

	private ModuleChooserSetting(boolean controlButtonsAreShown, ChooserDialogType chooserDialogType,
			Filter<Module> moduleFilter, boolean multiSelectionEnabled) {
		this.controlButtonsAreShown = controlButtonsAreShown;
		this.chooserDialogType = chooserDialogType;
		this.moduleFilter = moduleFilter;
		this.multiSelectionEnabled = multiSelectionEnabled;
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
		ModuleChooserSetting other = (ModuleChooserSetting) obj;
		if (chooserDialogType != other.chooserDialogType)
			return false;
		if (moduleFilter == null) {
			if (other.moduleFilter != null)
				return false;
		} else if (!moduleFilter.equals(other.moduleFilter))
			return false;
		if (controlButtonsAreShown != other.controlButtonsAreShown)
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
	 * 获取组件过滤器。
	 * 
	 * @return 组件过滤器。
	 */
	public Filter<Module> getModuleFilter() {
		return moduleFilter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chooserDialogType == null) ? 0 : chooserDialogType.hashCode());
		result = prime * result + ((moduleFilter == null) ? 0 : moduleFilter.hashCode());
		result = prime * result + (controlButtonsAreShown ? 1231 : 1237);
		result = prime * result + (multiSelectionEnabled ? 1231 : 1237);
		return result;
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
	 * 获取是否可以选择多个组件。
	 * 
	 * @return 是否可以选择多个组件。
	 */
	public boolean isMultiSelectionEnabled() {
		return multiSelectionEnabled;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "ModuleChooserSetting [controlButtonsAreShown=" + controlButtonsAreShown + ", chooserDialogType="
				+ chooserDialogType + ", moduleFilter=" + moduleFilter + ", multiSelectionEnabled="
				+ multiSelectionEnabled + "]";
	}

}
