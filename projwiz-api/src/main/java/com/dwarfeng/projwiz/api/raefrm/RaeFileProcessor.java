package com.dwarfeng.projwiz.api.raefrm;

import java.awt.Image;
import java.util.concurrent.locks.ReadWriteLock;

import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.projwiz.api.AbstractFileProcessor;
import com.dwarfeng.projwiz.core.model.eum.IconVariability;
import com.dwarfeng.projwiz.core.model.struct.Editor;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.MetaDataStorage;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.PropSuppiler;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;

/**
 * Rae框架文件处理器。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public abstract class RaeFileProcessor extends AbstractFileProcessor {

	public RaeFileProcessor(String key, ReferenceModel<? extends Toolkit> toolkitRef, MetaDataStorage metaDataStorage) {
		super(key, toolkitRef, metaDataStorage);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canOpenFile(File file) {
		// TODO Auto-generated method stub
		return super.canOpenFile(file);
	}

	@Override
	public Image getFileIcon(File file) {
		// TODO Auto-generated method stub
		return super.getFileIcon(file);
	}

	@Override
	public IconVariability getFileIconVariability(File file) {
		// TODO Auto-generated method stub
		return super.getFileIconVariability(file);
	}

	@Override
	public IconVariability getFileThumbVariability(File file) {
		// TODO Auto-generated method stub
		return super.getFileThumbVariability(file);
	}

	@Override
	public Image getIcon() {
		// TODO Auto-generated method stub
		return super.getIcon();
	}

	@Override
	public Image getThumb(File file) {
		// TODO Auto-generated method stub
		return super.getThumb(file);
	}

	@Override
	public boolean isNewFileSupported() {
		// TODO Auto-generated method stub
		return super.isNewFileSupported();
	}

	@Override
	public File newFile() throws ProcessException {
		// TODO Auto-generated method stub
		return super.newFile();
	}

	@Override
	public PropSuppiler getPropSuppiler(File file) {
		// TODO Auto-generated method stub
		return super.getPropSuppiler(file);
	}

	@Override
	public boolean isNewEditorSupported() {
		// TODO Auto-generated method stub
		return super.isNewEditorSupported();
	}

	@Override
	public Editor newEditor(Project editProject, File editFile) throws ProcessException {
		// TODO Auto-generated method stub
		return super.newEditor(editProject, editFile);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return super.getKey();
	}

	@Override
	public ReadWriteLock getLock() {
		// TODO Auto-generated method stub
		return super.getLock();
	}

	@Override
	public IconVariability getIconVarialibity() {
		// TODO Auto-generated method stub
		return super.getIconVarialibity();
	}

}
