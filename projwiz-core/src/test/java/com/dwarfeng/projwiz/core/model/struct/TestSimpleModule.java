package com.dwarfeng.projwiz.core.model.struct;

import java.awt.Image;
import java.util.concurrent.locks.ReadWriteLock;

import com.dwarfeng.projwiz.core.model.eum.IconVariability;
import com.dwarfeng.projwiz.core.model.struct.Module;

/**
 * 测试用简单组件。
 * 
 * <p>
 * 作为测试用的，不实现任何方法的组件。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public class TestSimpleModule implements Module {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReadWriteLock getLock() {
		// 不实现。
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		// 不实现。
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDescription() {
		// 不实现。
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		// 不实现。
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Image getIcon() {
		// 不实现。
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IconVariability getIconVarialibity() {
		// 不实现。
		return null;
	}

}
