package com.dwarfeng.projwiz.core.model.obv;

/**
 * 编辑器适配器。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public abstract class EditorAdapter implements EditorObverser {

	@Override
	public void fireSaved() {
	}

	@Override
	public void fireStopped() {
	}

	@Override
	public void fireStopSuggestChanged(boolean newValue) {
	}

	@Override
	public void fireTitleChanged(String oldTitle, String newTitle) {
	}

}
