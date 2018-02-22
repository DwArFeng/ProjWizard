package com.dwarfeng.projwiz.raefrm;

import java.awt.Component;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.projwiz.core.model.obv.EditorObverser;
import com.dwarfeng.projwiz.core.model.struct.Editor;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.raefrm.model.struct.FileProcToolkit;

/**
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public abstract class RaeEditor implements Editor {

	/** 侦听器集合。 */
	protected final Set<EditorObverser> obversers;
	/** 同步读写锁。 */
	protected final ReadWriteLock lock = new ReentrantReadWriteLock();
	/** 正在编辑的工程。 */
	protected final Project editProject;
	/** 正在编辑的文件。 */
	protected final File editFile;

	/** 对应的文件处理器的工具包。 */
	protected final FileProcToolkit fileProcToolkit;

	/**
	 * 新实例。
	 * 
	 * @param editProject
	 *            正在编辑的工程。
	 * @param editFile
	 *            正在编辑的文件。
	 * @param fileProcToolkit
	 *            对应的工程文件处理器的工具包。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	public RaeEditor(Project editProject, File editFile, FileProcToolkit fileProcToolkit) {
		this(Collections.newSetFromMap(new WeakHashMap<>()), editProject, editFile, fileProcToolkit);
	}

	/**
	 * 新实例。
	 * 
	 * @param obversers
	 *            指定的观察器集合。
	 * @param editProject
	 *            正在编辑的工程。
	 * @param editFile
	 *            正在编辑的文件。
	 * @param fileProcToolkit
	 *            对应的工程文件处理器的工具包。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected RaeEditor(Set<EditorObverser> obversers, Project editProject, File editFile,
			FileProcToolkit fileProcToolkit) {
		Objects.requireNonNull(obversers, "入口参数 obversers 不能为 null。");
		Objects.requireNonNull(editProject, "入口参数 editProject 不能为 null。");
		Objects.requireNonNull(editFile, "入口参数 editFile 不能为 null。");
		Objects.requireNonNull(fileProcToolkit, "入口参数 fileProcToolkit 不能为 null。");

		this.obversers = obversers;
		this.editFile = editFile;
		this.editProject = editProject;
		this.fileProcToolkit = fileProcToolkit;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addObverser(EditorObverser obverser) {
		lock.writeLock().lock();
		try {
			return obversers.add(obverser);
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clearObverser() {
		lock.writeLock().lock();
		try {
			obversers.clear();
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File getEditFile() {
		return editFile;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component getEditorView() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Project getEditProject() {
		return editProject;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReadWriteLock getLock() {
		return lock;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<EditorObverser> getObversers() {
		return Collections.unmodifiableSet(obversers);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTitle() {
		return String.format("%s_%s", editProject.getName(), editFile.getName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSaveSupported() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isStopSuggest() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeObverser(EditorObverser obverser) {
		lock.writeLock().lock();
		try {
			return obversers.remove(obverser);
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save() throws ProcessException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stop() {
		lock.readLock().lock();
		try {
			fireStopped();
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.getClass().toString() + " [lock=" + lock + ", obversers=" + obversers + ", editFile=" + editFile
				+ ", editProject=" + editProject + "]";
	}

	/**
	 * 通知观察器编辑器进行保存。
	 */
	protected void fireSaved() {
		obversers.forEach(obverser -> {
			obverser.fireSaved();
		});
	}

	/**
	 * 通知观察器该编辑器退出编辑。
	 */
	protected void fireStopped() {
		obversers.forEach(obverser -> {
			obverser.fireStopped();
		});
	}

	/**
	 * 通知观察器建议保存标记改变。
	 * 
	 * @param newValue
	 *            新的值。
	 */
	protected void fireStopSuggestChanged(boolean newValue) {
		obversers.forEach(obverser -> {
			obverser.fireStopSuggestChanged(newValue);
		});
	}

	/**
	 * 通知观察器标题改变了。
	 * 
	 * @param oldTitle
	 *            旧的标题。
	 * @param newTitle
	 *            新的标题。
	 */
	protected void fireTitleChanged(String oldTitle, String newTitle) {
		obversers.forEach(obverser -> {
			obverser.fireTitleChanged(oldTitle, newTitle);
		});
	}

}
