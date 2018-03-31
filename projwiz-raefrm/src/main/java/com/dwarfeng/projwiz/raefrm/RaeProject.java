package com.dwarfeng.projwiz.raefrm;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.dwarfeng.dutil.basic.prog.Buildable;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.projwiz.core.model.cm.Tree;
import com.dwarfeng.projwiz.core.model.cm.Tree.Path;
import com.dwarfeng.projwiz.core.model.obv.ProjectObverser;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.ProjectProcessor;
import com.dwarfeng.projwiz.core.util.ModelUtil;
import com.dwarfeng.projwiz.raefrm.model.struct.ProjProcToolkit;

/**
 * Rae框架工程。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public abstract class RaeProject implements Project {

	/**
	 * Rae框架工程的构造器。
	 * 
	 * @author DwArFeng
	 * @since 0.0.3-alpha
	 */
	protected abstract static class RaeProjectBuilder implements Buildable<RaeProject> {

		/** 该工程的处理器类。 */
		protected final Class<? extends ProjectProcessor> processorClass;
		/** 工程的名称。 */
		protected final String name;
		/** 对应的工程处理器的工具包。 */
		protected final ProjProcToolkit projProcToolkit;
		/** 抽象工程的工程树。 */
		protected final Tree<File> fileTree;
		/** Rae工程的文件名称映射。 */
		protected final Map<File, String> fileNameMap;

		/** 工程的观察器集合。 */
		protected Set<ProjectObverser> obversers = Collections.newSetFromMap(new WeakHashMap<>());

		/**
		 * 新实例。
		 * 
		 * @param processorClass
		 *            指定的处理器类。
		 * @param name
		 *            指定的名称。
		 * @param projProcToolkit
		 *            指定的工程文件处理器工具包。
		 * @param fileTree
		 *            指定的文件树。
		 * @param fileNameMap
		 *            指定的文件-名称映射。
		 * @throws NullPointerException
		 *             入口参数为 <code>null</code>。
		 */
		public RaeProjectBuilder(Class<? extends ProjectProcessor> processorClass, String name,
				ProjProcToolkit projProcToolkit, Tree<File> fileTree, Map<File, String> fileNameMap) {
			Objects.requireNonNull(processorClass, "入口参数 processorClass 不能为 null。");
			Objects.requireNonNull(name, "入口参数 name 不能为 null。");
			Objects.requireNonNull(projProcToolkit, "入口参数 projProcToolkit 不能为 null。");
			Objects.requireNonNull(fileTree, "入口参数 fileTree 不能为 null。");
			Objects.requireNonNull(fileNameMap, "入口参数 fileNameMap 不能为 null。");

			this.processorClass = processorClass;
			this.name = name;
			this.projProcToolkit = projProcToolkit;
			this.fileTree = fileTree;
			this.fileNameMap = fileNameMap;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public abstract RaeProject build();

		/**
		 * 设置工程中的观察器集合。
		 * 
		 * @param obversers
		 *            工程中的观察器集合。
		 * @return 构造器自身。
		 */
		public RaeProjectBuilder setObversers(Set<ProjectObverser> obversers) {
			this.obversers = Objects.isNull(obversers) ? Collections.newSetFromMap(new WeakHashMap<>()) : obversers;
			return this;
		}

	}

	/** 工程的同步读写锁。 */
	protected final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	/** 工程的观察器集合。 */
	protected final Set<ProjectObverser> obversers;

	/** 该工程的处理器类。 */
	protected final Class<? extends ProjectProcessor> processorClass;
	/** 工程的名称。 */
	protected final String name;
	/** 抽象工程的工程树。 */
	protected final Tree<File> fileTree;
	/** Rae工程的文件名称映射。 */
	protected final Map<File, String> fileNameMap;

	/** 对应的工程处理器的工具包。 */
	protected final ProjProcToolkit projProcToolkit;

	/**
	 * 新实例。
	 * 
	 * @param processorClass
	 *            该工程的控制器类。
	 * @param name
	 *            该工程的名称。
	 * @param fileTree
	 *            该工程的文件树。
	 * @param fileNameMap
	 *            该工程的文件名称映射。
	 * @param projProcToolkit
	 *            该工程的工程处理器工具包。
	 * @param obversers
	 *            该工程的观察器集合。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected RaeProject(Class<? extends ProjectProcessor> processorClass, String name, Tree<File> fileTree,
			Map<File, String> fileNameMap, ProjProcToolkit projProcToolkit, Set<ProjectObverser> obversers) {
		Objects.requireNonNull(processorClass, "入口参数 processorClass 不能为 null。");
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");
		Objects.requireNonNull(fileTree, "入口参数 fileTree 不能为 null。");
		Objects.requireNonNull(fileNameMap, "入口参数 fileNameMap 不能为 null。");
		Objects.requireNonNull(projProcToolkit, "入口参数 projProcToolkit 不能为 null。");
		Objects.requireNonNull(obversers);

		this.processorClass = processorClass;
		this.name = name;
		this.fileTree = fileTree;
		this.fileNameMap = fileNameMap;
		this.projProcToolkit = projProcToolkit;
		this.obversers = obversers;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File addFile(File parent, File file, String exceptName, AddingSituation situation) {
		throw new UnsupportedOperationException("addFile");
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
	public String getFileName(File file) {
		lock.readLock().lock();
		try {
			return fileNameMap.getOrDefault(file, null);
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Tree<? extends File> getFileTree() {
		lock.readLock().lock();
		try {
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
	public Class<? extends ProjectProcessor> getProcessorClass() {
		return processorClass;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAddFileSupported(AddingSituation situation) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRemoveFileSupported(RemovingSituation situation) {
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
	public File removeFile(File file, RemovingSituation situation) {
		throw new UnsupportedOperationException("removeFile");
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
		throw new UnsupportedOperationException("save");
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
	 * 通知文件被添加。
	 * 
	 * @param path
	 *            指定的文件所在的路径。
	 * @param parent
	 *            指定文件的父节点。
	 * @param file
	 *            指定的文件。
	 * @param situation
	 *            文件的添加情形。
	 */
	protected void fireFileAdded(Tree.Path<File> path, File parent, File file, Project.AddingSituation situation) {
		obversers.forEach(obverser -> {
			try {
				obverser.fireFileAdded(path, parent, file, situation);
			} catch (Exception e) {
				e.printStackTrace();
			}
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
	 * @param situation
	 *            文件的移除情形。
	 */
	protected void fireFileRemoved(Tree.Path<File> path, File parent, File file, Project.RemovingSituation situation) {
		obversers.forEach(obverser -> {
			try {
				obverser.fireFileRemoved(path, parent, file, situation);
			} catch (Exception e) {
				e.printStackTrace();
			}
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
			try {
				obverser.fireFileRenamed(path, file, oldName, newName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * 通知工程被保存。
	 */
	protected void fireSaved() {
		obversers.forEach(obverser -> {
			try {
				obverser.fireSaved();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * 通知观察器该工程通知运行。
	 */
	protected void fireStopped() {
		obversers.forEach(obverser -> {
			try {
				obverser.fireStopped();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

}
