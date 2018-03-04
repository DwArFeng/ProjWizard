package com.dwarfeng.projwiz.raefrm;

import java.awt.Component;
import java.util.Objects;

import javax.swing.JPanel;

import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.PropSuppiler;

/**
 * Rae框架下的工程属性提供器。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public abstract class RaeProjectPropSuppiler<T extends Project> extends JPanel implements PropSuppiler {

	/** 属性提供器的目标工程。 */
	protected final T project;

	/**
	 * 新实例。
	 * 
	 * @param project
	 *            指定的目标工程。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected RaeProjectPropSuppiler(T project) {
		Objects.requireNonNull(project, "入口参数 project 不能为 null。");

		this.project = project;
	}

	/**
	 * 获取属性提供器的目标工程。
	 * 
	 * @return 属性提供器的目标工程。
	 */
	public T getFile() {
		return project;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component getComponent() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract void fireApplied();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract void fireConfirmed();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract void fireCanceled();

}
