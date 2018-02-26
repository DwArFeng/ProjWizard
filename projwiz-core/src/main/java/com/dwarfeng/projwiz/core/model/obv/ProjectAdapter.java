package com.dwarfeng.projwiz.core.model.obv;

import com.dwarfeng.projwiz.core.model.cm.Tree.Path;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.Project.AddingSituation;
import com.dwarfeng.projwiz.core.model.struct.Project.RemovingSituation;

/**
 * 工程观察器适配器。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public abstract class ProjectAdapter implements ProjectObverser {

	@Override
	public void fireFileAdded(Path<File> path, File parent, File file, AddingSituation situation) {
	}

	@Override
	public void fireFileRemoved(Path<File> path, File parent, File file, RemovingSituation situation) {
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
