package com.dwarfeng.projwiz.core.model.struct;

import java.util.Objects;

import com.dwarfeng.dutil.basic.cna.model.MapModel;
import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.projwiz.core.model.cm.ModuleModel;

/**
 * 组件模型和组件-工具包引用模型对。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public final class CotoPair {

	private final ModuleModel moduleModel;
	private final MapModel<Class<? extends Module>, ReferenceModel<Toolkit>> moduleToolkitModel;

	/**
	 * 新实例。
	 * 
	 * @param moduleModel
	 *            指定的组件模型。
	 * @param moduleToolkitModel
	 *            指定的组件-工具包引用模型。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	public CotoPair(ModuleModel moduleModel,
			MapModel<Class<? extends Module>, ReferenceModel<Toolkit>> moduleToolkitModel) {
		Objects.requireNonNull(moduleModel, "入口参数 moduleModel 不能为 null。");
		Objects.requireNonNull(moduleToolkitModel, "入口参数 moduleToolkitModel 不能为 null。");

		this.moduleModel = moduleModel;
		this.moduleToolkitModel = moduleToolkitModel;
	}

	/**
	 * @return the moduleModel.
	 */
	public ModuleModel getModuleModel() {
		return moduleModel;
	}

	/**
	 * @return the moduleToolkitModel.
	 */
	public MapModel<Class<? extends Module>, ReferenceModel<Toolkit>> getModuleToolkitModel() {
		return moduleToolkitModel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((moduleToolkitModel == null) ? 0 : moduleToolkitModel.hashCode());
		result = prime * result + ((moduleModel == null) ? 0 : moduleModel.hashCode());
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
		CotoPair other = (CotoPair) obj;
		if (moduleToolkitModel == null) {
			if (other.moduleToolkitModel != null)
				return false;
		} else if (!moduleToolkitModel.equals(other.moduleToolkitModel))
			return false;
		if (moduleModel == null) {
			if (other.moduleModel != null)
				return false;
		} else if (!moduleModel.equals(other.moduleModel))
			return false;
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "CotoPair [moduleModel=" + moduleModel + ", moduleToolkitModel=" + moduleToolkitModel + "]";
	}

}
