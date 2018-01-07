package com.dwarfeng.projwiz.api;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.projwiz.core.model.cm.MapTree;
import com.dwarfeng.projwiz.core.model.cm.Tree;
import com.dwarfeng.projwiz.core.model.cm.Tree.Path;
import com.dwarfeng.projwiz.core.model.obv.ProjectObverser;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.util.ModelUtil;

/**
 * 抽象工程。
 * 
 * <p>
 * 工程的抽象实现。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public abstract class AbstractProject implements Project {

	/** 工程的同步读写锁。 */
	protected final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	/** 工程的观察器集合。 */
	protected final Set<ProjectObverser> obversers = Collections.newSetFromMap(new WeakHashMap<>());

	/** 工程的独立标签 */
	protected final String uniqueLabel;
	/** 工程的注册键。 */
	protected final String registerKey;
	/** 工程的名称。 */
	protected final String name;
	/** 抽象工程的工程树。 */
	protected final Tree<File> fileTree;

	/**
	 * 新实例。
	 * 
	 * @param registerKey
	 *            指定的注册键。
	 * @param name
	 *            指定的名称。
	 * @param uniqueLabel
	 *            独立标签。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public AbstractProject(String uniqueLabel, String registerKey, String name) {
		this(uniqueLabel, registerKey, name, new MapTree<>());
	}

	/**
	 * 新实例。
	 * 
	 * @param registerKey
	 *            指定的注册键。
	 * @param name
	 *            指定的名称。
	 * @param uniqueLabel
	 *            独立标签。
	 * @param fileTree
	 *            工程的文件树。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public AbstractProject(String uniqueLabel, String registerKey, String name, Tree<File> fileTree) {
		Objects.requireNonNull(uniqueLabel, "入口参数 uniqueLabel 不能为 null。");
		Objects.requireNonNull(registerKey, "入口参数 registerKey 不能为 null。");
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");
		Objects.requireNonNull(fileTree, "入口参数 fileTree 不能为 null。");

		this.uniqueLabel = uniqueLabel;
		this.registerKey = registerKey;
		this.name = name;
		this.fileTree = fileTree;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File addFileByCopy(File parent, File file) {
		throw new UnsupportedOperationException("addFileByCopy");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File addFileByMove(File parent, File file) {
		throw new UnsupportedOperationException("addFileByMove");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File addFileByNew(File parent, File file) {
		throw new UnsupportedOperationException("addFileByNew");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addObverser(ProjectObverser obverser) {
		lock.writeLock().lock();
		try {
			return obversers.add(obverser);
		} finally {
			lock.writeLock().unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dwarfeng.dutil.basic.prog.ObverserSet#clearObverser()
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Project))
			return false;
		Project other = (Project) obj;
		if (uniqueLabel == null) {
			if (other.getUniqueLabel() != null)
				return false;
		} else if (!uniqueLabel.equals(other.getUniqueLabel()))
			return false;
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Tree<? extends File> getFileTree() {
		lock.readLock().lock();
		try {
			// TODO 以后使用只读树。
			return ModelUtil.unmodifiableTree(fileTree);
		} finally {
			lock.readLock().unlock();
		}
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
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<ProjectObverser> getObversers() {
		lock.readLock().lock();
		try {
			return Collections.unmodifiableSet(obversers);
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getRegisterKey() {
		return registerKey;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUniqueLabel() {
		return uniqueLabel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uniqueLabel == null) ? 0 : uniqueLabel.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAddFileByCopySupported() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAddFileByMoveSupported() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAddFileByNewSupported() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRemoveFileByDeleteSupported() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRemoveFileByMoveSupported() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRenameFileSupported() {
		return false;
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
	public File removeFileByDelete(File file) {
		throw new UnsupportedOperationException("removeFileByDelete");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File removeFileByMove(File file) {
		throw new UnsupportedOperationException("removeFileByMove");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeObverser(ProjectObverser obverser) {
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
	public File renameFile(File file, String newName) {
		throw new UnsupportedOperationException("renameFile");
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
		lock.writeLock().lock();
		try {
			fireStopped();
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * 通知指定的文件以复制的方式被添加。
	 * 
	 * @param path
	 *            指定的文件所在的路径。
	 * @param parent
	 *            指定文件的父节点。
	 * @param file
	 *            指定的文件。
	 */
	protected void fireFileAddedByCopy(Tree.Path<File> path, File parent, File file) {
		obversers.forEach(obverser -> {
			obverser.fireFileAddedByCopy(path, parent, file);
		});
	}

	/**
	 * 通知指定的文件以移动的方式被添加。
	 * 
	 * @param path
	 *            指定的文件所在的路径。
	 * @param parent
	 *            指定文件的父节点。
	 * @param file
	 *            指定的文件。
	 */
	protected void fireFileAddedByMove(Tree.Path<File> path, File parent, File file) {
		obversers.forEach(obverser -> {
			obverser.fireFileAddedByMove(path, parent, file);
		});
	}

	/**
	 * 通知指定的文件以新建的方式被添加。
	 * 
	 * @param path
	 *            指定的文件所在的路径。
	 * @param parent
	 *            指定文件的父节点。
	 * @param file
	 *            指定的文件。
	 */
	protected void fireFileAddedByNew(Tree.Path<File> path, File parent, File file) {
		obversers.forEach(obverser -> {
			obverser.fireFileAddedByNew(path, parent, file);
		});
	}

	/**
	 * 通知指定的文件以删除的方式被移除。
	 * 
	 * @param path
	 *            指定的文件所在的路径。
	 * @param parent
	 *            指定的文件的父节点。
	 * @param file
	 *            指定的文件。
	 */
	protected void fireFileRemovedByDelete(Tree.Path<File> path, File parent, File file) {
		obversers.forEach(obverser -> {
			obverser.fireFileRemovedByDelete(path, parent, file);
		});
	}

	/**
	 * 通知指定的文件以删除的方式被移除。
	 * 
	 * @param path
	 *            指定的文件所在的路径。
	 * @param parent
	 *            指定的文件的父节点。
	 * @param file
	 *            指定的文件。
	 */
	protected void fireFileRemovedByMove(Tree.Path<File> path, File parent, File file) {
		obversers.forEach(obverser -> {
			obverser.fireFileRemovedByMove(path, parent, file);
		});
	}

	/**
	 * 通知指定的文件被重命名。
	 * 
	 * @param path
	 *            指定的文件所在的路径。
	 * @param file
	 *            指定的文件。
	 * @param oldName
	 *            指定文件的旧名字。
	 * @param newName
	 *            指定文件的新名字。
	 */
	protected void fireFileRenamed(Path<File> path, File file, String oldName, String newName) {
		obversers.forEach(obverser -> {
			obverser.fireFileRenamed(path, file, oldName, newName);
		});
	}

	/**
	 * 通知工程被保存。
	 */
	protected void fireSaved() {
		obversers.forEach(obverser -> {
			obverser.fireSaved();
		});
	}

	/**
	 * 通知观察器该工程通知运行。
	 */
	protected void fireStopped() {
		obversers.forEach(obverser -> {
			obverser.fireStopped();
		});
	}

}