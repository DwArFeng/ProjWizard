package com.dwarfeng.projwiz.core.model.obv;

/**
 * 文件观察器适配器。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public abstract class FileAdapter implements FileObverser {

	@Override
	public void fireInputClosed(String label) {
	}

	@Override
	public void fireInputOpened(String label) {
	}

	@Override
	public void fireOutputClosed(String label) {
	}

	@Override
	public void fireOutputOpened(String label) {
	}

	@Override
	public void fireLabelAdded(String label) {
	}

	@Override
	public void fireLabelRemoved(String label) {
	}

	@Override
	public void fireModifyTimeChanged(long oldValue, long newValue) {
	}

	@Override
	public void fireCreateTimeChanged(long oldValue, long newValue) {
	}

	@Override
	public void fireAccessTimeChanged(long oldValue, long newValue) {
	}

	@Override
	public void fireReadSupportedChanged(boolean newValue) {
	}

	@Override
	public void fireWriteSupportedChanged(boolean newValue) {
	}

	@Override
	public void fireLengthChanged(long oldValue, long newValue) {
	}

	@Override
	public void fireOccupiedSizeChanged(long oldValue, long newValue) {
	}

}
