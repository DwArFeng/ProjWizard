package com.dwarfeng.projwiz.core.model.struct;

import java.util.Objects;

import com.dwarfeng.dutil.basic.cna.model.MapModel;
import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.projwiz.core.model.cm.ComponentModel;

/**
 * 组件模型和组件-工具包引用模型对。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public final class CotoPair {

	private final ComponentModel componentModel;
	private final MapModel<String, ReferenceModel<Toolkit>> cmpoentToolkitModel;

	/**
	 * 新实例。
	 * 
	 * @param componentModel
	 *            指定的组件模型。
	 * @param cmpoentToolkitModel
	 *            指定的组件-工具包引用模型。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	public CotoPair(ComponentModel componentModel, MapModel<String, ReferenceModel<Toolkit>> cmpoentToolkitModel) {
		Objects.requireNonNull(componentModel, "入口参数 componentModel 不能为 null。");
		Objects.requireNonNull(cmpoentToolkitModel, "入口参数 cmpoentToolkitModel 不能为 null。");

		this.componentModel = componentModel;
		this.cmpoentToolkitModel = cmpoentToolkitModel;
	}

	/**
	 * @return the componentModel.
	 */
	public ComponentModel getComponentModel() {
		return componentModel;
	}

	/**
	 * @return the cmpoentToolkitModel.
	 */
	public MapModel<String, ReferenceModel<Toolkit>> getCmpoentToolkitModel() {
		return cmpoentToolkitModel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cmpoentToolkitModel == null) ? 0 : cmpoentToolkitModel.hashCode());
		result = prime * result + ((componentModel == null) ? 0 : componentModel.hashCode());
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
		if (cmpoentToolkitModel == null) {
			if (other.cmpoentToolkitModel != null)
				return false;
		} else if (!cmpoentToolkitModel.equals(other.cmpoentToolkitModel))
			return false;
		if (componentModel == null) {
			if (other.componentModel != null)
				return false;
		} else if (!componentModel.equals(other.componentModel))
			return false;
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "CotoPair [componentModel=" + componentModel + ", cmpoentToolkitModel=" + cmpoentToolkitModel + "]";
	}

}
