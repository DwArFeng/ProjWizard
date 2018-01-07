package com.dwarfeng.projwiz.core.model.obv;

import com.dwarfeng.projwiz.core.model.cm.Tree.Path;
import com.dwarfeng.projwiz.core.model.struct.File;

/**
 * 工程观察器适配器。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public abstract class ProjectAdapter implements ProjectObverser {
	
	

	@Override
	public void fireFileAddedByCopy(Path<File> path, File parent, File file) {
	}

	@Override
	public void fireFileAddedByMove(Path<File> path, File parent, File file) {
	}

	@Override
	public void fireFileAddedByNew(Path<File> path, File parent, File file) {
	}

	@Override
	public void fireFileRemovedByDelete(Path<File> path, File parent, File file) {
	}

	@Override
	public void fireFileRemovedByMove(Path<File> path, File parent, File file) {
	}

	@Override
	public void fireFileRenamed(Path<File> path, File file, String oldName, String newName) {
	}

	@Override
	public void fireSaved() {
	}

	@Override
	public void fireStopped() {
	}

}
