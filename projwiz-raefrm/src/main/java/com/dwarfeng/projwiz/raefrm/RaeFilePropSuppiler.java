package com.dwarfeng.projwiz.raefrm;

import java.awt.Component;
import java.util.Objects;

import javax.swing.JPanel;

import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.PropSuppiler;

/**
 * Rae框架下的文件属性提供器。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public abstract class RaeFilePropSuppiler<T extends File> extends JPanel implements PropSuppiler {

	/** 属性提供器的目标文件。 */
	protected final T file;

	/**
	 * 新实例。
	 * 
	 * @param file
	 *            指定的目标文件。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected RaeFilePropSuppiler(T file) {
		Objects.requireNonNull(file, "入口参数 file 不能为 null。");

		this.file = file;
	}

	/**
	 * 获取属性提供器的目标文件。
	 * 
	 * @return 属性提供器的目标文件。
	 */
	public T getFile() {
		return file;
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
