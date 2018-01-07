package com.dwarfeng.projwiz.core.view.obv;

/**
 * 主界面可见性观察器适配器。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public abstract class MainFrameVisibleAdapter implements MainFrameVisibleObverser {

	@Override
	public void fireNorthVisibleChanged(boolean newValue) {
	}

	@Override
	public void fireSouthVisibleChanged(boolean newValue) {
	}

	@Override
	public void fireEastVisibleChanged(boolean newValue) {
	}

	@Override
	public void fireWestVisibleChanged(boolean newValue) {
	}

	@Override
	public void fireMaximumChanged(boolean newValue) {
	}

}
