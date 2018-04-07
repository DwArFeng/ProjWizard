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
import com.dwarfeng.projwiz.raefrm.model.eum.PermDemandKey;
import com.dwarfeng.projwiz.raefrm.model.eum.ProjCoreConfigEntry;
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
		Objects.requireNonNull(situation, "入口参数 situation 不能为 null。");

		lock.readLock().lock();
		try {
			switch (situation) {
			case BY_COPY:
				return projProcToolkit.getCoreConfigModel().getParsedValue(
						ProjCoreConfigEntry.RAE_PROJECT_SUPPORTED_ADDING_BYCOPY.getConfigKey(), Boolean.class);
			case BY_MOVE:
				return projProcToolkit.getCoreConfigModel().getParsedValue(
						ProjCoreConfigEntry.RAE_PROJECT_SUPPORTED_ADDING_BYMOVE.getConfigKey(), Boolean.class);
			case BY_NEW:
				return projProcToolkit.getCoreConfigModel().getParsedValue(
						ProjCoreConfigEntry.RAE_PROJECT_SUPPORTED_ADDING_BYNEW.getConfigKey(), Boolean.class);
			case OTHER:
				return projProcToolkit.getCoreConfigModel().getParsedValue(
						ProjCoreConfigEntry.RAE_PROJECT_SUPPORTED_ADDING_BYOTHER.getConfigKey(), Boolean.class);
			default:
				return false;
			}
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRemoveFileSupported(RemovingSituation situation) {
		Objects.requireNonNull(situation, "入口参数 situation 不能为 null。");

		lock.readLock().lock();
		try {
			switch (situation) {
			case BY_DELETE:
				return projProcToolkit.getCoreConfigModel().getParsedValue(
						ProjCoreConfigEntry.RAE_PROJECT_SUPPORTED_REMOVING_BYDELETE.getConfigKey(), Boolean.class);
			case BY_MOVE:
				return projProcToolkit.getCoreConfigModel().getParsedValue(
						ProjCoreConfigEntry.RAE_PROJECT_SUPPORTED_REMOVING_BYMOVE.getConfigKey(), Boolean.class);
			case OTHER:
				return projProcToolkit.getCoreConfigModel().getParsedValue(
						ProjCoreConfigEntry.RAE_PROJECT_SUPPORTED_REMOVING_BYOTHER.getConfigKey(), Boolean.class);
			default:
				return false;
			}
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRenameFileSupported() {
		lock.readLock().lock();
		try {
			return projProcToolkit.getCoreConfigModel().getParsedValue(
					ProjCoreConfigEntry.RAE_PROJECT_SUPPORTED_RENAME_FILE.getConfigKey(), Boolean.class);
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSaveSupported() {
		lock.readLock().lock();
		try {
			return projProcToolkit.getCoreConfigModel()
					.getParsedValue(ProjCoreConfigEntry.RAE_PROJECT_SUPPORTED_SAVE.getConfigKey(), Boolean.class);
		} finally {
			lock.readLock().unlock();
		}
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
	 * <p>
	 * 该方法首先检查 {@link #isAddFileSupported(AddingSituation)} ,
	 * 确认该工程处理器是否允许新建工程，如果不允许，则直接抛出
	 * {@link UnsupportedOperationException}；否则，调用相应子方法，返回要求的结果。
	 */
	@Override
	public File addFile(File parent, File file, String exceptName, AddingSituation situation) {
		Objects.requireNonNull(parent, "入口参数 parent 不能为 null。");
		Objects.requireNonNull(file, "入口参数 file 不能为 null。");
		Objects.requireNonNull(exceptName, "入口参数 exceptName 不能为 null。");
		Objects.requireNonNull(situation, "入口参数 situation 不能为 null。");

		lock.writeLock().lock();
		try {
			if (!isAddFileSupported(situation)) {
				throw new UnsupportedOperationException("addFile");
			} else {
				switch (situation) {
				case BY_COPY:
					projProcToolkit.requirePermKeyAvailable(PermDemandKey.RAE_PROJECT_ADDFILE_BYCOPY);
					return addFileByCopy_Sub(parent, file, exceptName);
				case BY_MOVE:
					projProcToolkit.requirePermKeyAvailable(PermDemandKey.RAE_PROJECT_ADDFILE_BYMOVE);
					return addFileByMove_Sub(parent, file, exceptName);
				case BY_NEW:
					projProcToolkit.requirePermKeyAvailable(PermDemandKey.RAE_PROJECT_ADDFILE_BYNEW);
					return addFileByNew_Sub(parent, file, exceptName);
				case OTHER:
					projProcToolkit.requirePermKeyAvailable(PermDemandKey.RAE_PROJECT_ADDFILE_BYOTHER);
					return addFileByOther_Sub(parent, file, exceptName);
				default:
					throw new IllegalArgumentException("非法的参数 situation: " + situation);
				}
			}
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * 该方法首先检查 {@link #removeFile(File, RemovingSituation)} ,
	 * 确认该工程处理器是否允许新建工程，如果不允许，则直接抛出
	 * {@link UnsupportedOperationException}；否则，调用相应子方法，返回要求的结果。
	 */
	@Override
	public File removeFile(File file, RemovingSituation situation) {
		Objects.requireNonNull(file, "入口参数 file 不能为 null。");
		Objects.requireNonNull(situation, "入口参数 situation 不能为 null。");

		lock.writeLock().lock();
		try {
			if (!isRemoveFileSupported(situation)) {
				throw new UnsupportedOperationException("removeFile");
			} else {
				switch (situation) {
				case BY_DELETE:
					projProcToolkit.requirePermKeyAvailable(PermDemandKey.RAE_PROJECT_REMOVEFILE_BYDELETE);
					return removeFileByDelete_Sub(file);
				case BY_MOVE:
					projProcToolkit.requirePermKeyAvailable(PermDemandKey.RAE_PROJECT_REMOVEFILE_BYMOVE);
					return removeFileByMove_Sub(file);
				case OTHER:
					projProcToolkit.requirePermKeyAvailable(PermDemandKey.RAE_PROJECT_ADDFILE_BYOTHER);
					return removeFileByOther_Sub(file);
				default:
					throw new IllegalArgumentException("非法的参数 situation: " + situation);
				}
			}
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * 该方法首先检查 {@link #isRenameFileSupported()} , 确认该工程处理器是否允许新建工程，如果不允许，则直接抛出
	 * {@link UnsupportedOperationException}；否则，调用
	 * {@link #renameFile_Sub(File, String)}，返回要求的结果。
	 */
	@Override
	public File renameFile(File file, String newName) {
		Objects.requireNonNull(file, "入口参数 file 不能为 null。");
		Objects.requireNonNull(newName, "入口参数 newName 不能为 null。");

		lock.writeLock().lock();
		try {
			if (!isRenameFileSupported()) {
				throw new UnsupportedOperationException("renameFile");
			} else {
				return renameFile_Sub(file, newName);
			}
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * 该方法首先检查 {@link #isSaveSupported()} , 确认该工程处理器是否允许新建工程，如果不允许，则直接抛出
	 * {@link UnsupportedOperationException}；否则，调用 {@link #save_Sub()}，返回要求的结果。
	 */
	@Override
	public void save() throws ProcessException {
		lock.writeLock().lock();
		try {
			if (!isSaveSupported()) {
				throw new UnsupportedOperationException("save");
			} else {
				save_Sub();
			}
		} finally {
			lock.writeLock().unlock();
		}
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
	 * 通过其它方式添加文件的子方法。
	 * <p>
	 * 该方法被 {@link #addFile(File, File, String, AddingSituation)} 调用。
	 * 
	 * @param parent
	 *            指定文件的父节点。
	 * @param file
	 *            指定的文件。
	 * @param exceptName
	 *            该文件的期望名称。
	 * @return 实际被添加进工程中的文件，如果失败，则为 <code>null</code>。
	 * @throws UnsupportedOperationException
	 *             不支持的操作。
	 */
	protected abstract File addFileByOther_Sub(File parent, File file, String exceptName)
			throws UnsupportedOperationException;

	/**
	 * 通过新建方式添加文件的子方法。
	 * <p>
	 * 该方法被 {@link #addFile(File, File, String, AddingSituation)} 调用。
	 * 
	 * @param parent
	 *            指定文件的父节点。
	 * @param file
	 *            指定的文件。
	 * @param exceptName
	 *            该文件的期望名称。
	 * @return 实际被添加进工程中的文件，如果失败，则为 <code>null</code>。
	 * @throws UnsupportedOperationException
	 *             不支持的操作。
	 */
	protected abstract File addFileByNew_Sub(File parent, File file, String exceptName)
			throws UnsupportedOperationException;

	/**
	 * 通过移动方式添加文件的子方法。
	 * <p>
	 * 该方法被 {@link #addFile(File, File, String, AddingSituation)} 调用。
	 * 
	 * @param parent
	 *            指定文件的父节点。
	 * @param file
	 *            指定的文件。
	 * @param exceptName
	 *            该文件的期望名称。
	 * @return 实际被添加进工程中的文件，如果失败，则为 <code>null</code>。
	 * @throws UnsupportedOperationException
	 *             不支持的操作。
	 */
	protected abstract File addFileByMove_Sub(File parent, File file, String exceptName)
			throws UnsupportedOperationException;

	/**
	 * 通过复制方式添加文件的子方法。
	 * <p>
	 * 该方法被 {@link #addFile(File, File, String, AddingSituation)} 调用。
	 * 
	 * @param parent
	 *            指定文件的父节点。
	 * @param file
	 *            指定的文件。
	 * @param exceptName
	 *            该文件的期望名称。
	 * @return 实际被添加进工程中的文件，如果失败，则为 <code>null</code>。
	 * @throws UnsupportedOperationException
	 *             不支持的操作。
	 */
	protected abstract File addFileByCopy_Sub(File parent, File file, String exceptName)
			throws UnsupportedOperationException;

	/**
	 * 通过其它方式移除文件的子方法。
	 * <p>
	 * 该方法被 {@link #removeFile(File, RemovingSituation)} 调用。
	 * 
	 * @param file
	 *            指定的文件。
	 * @return 被移除的文件 ,如果失败，则为 <code>null</code>。。
	 * @throws UnsupportedOperationException
	 *             不支持该操作。
	 */
	protected abstract File removeFileByOther_Sub(File file) throws UnsupportedOperationException;

	/**
	 * 通过移动方式移除文件的子方法。
	 * <p>
	 * 该方法被 {@link #removeFile(File, RemovingSituation)} 调用。
	 * 
	 * @param file
	 *            指定的文件。
	 * @return 被移除的文件 ,如果失败，则为 <code>null</code>。。
	 * @throws UnsupportedOperationException
	 *             不支持该操作。
	 */
	protected abstract File removeFileByMove_Sub(File file) throws UnsupportedOperationException;

	/**
	 * 通过删除方式移除文件的子方法。
	 * <p>
	 * 该方法被 {@link #removeFile(File, RemovingSituation)} 调用。
	 * 
	 * @param file
	 *            指定的文件。
	 * @return 被移除的文件 ,如果失败，则为 <code>null</code>。。
	 * @throws UnsupportedOperationException
	 *             不支持该操作。
	 */
	protected abstract File removeFileByDelete_Sub(File file) throws UnsupportedOperationException;

	/**
	 * 重命名文件的子方法。
	 * <p>
	 * 该方法被 {@link #renameFile(File, String)} 调用。
	 * 
	 * @param file
	 *            指定的文件。
	 * @param newName
	 *            指定的新名称。
	 * @return 实际被重命名的文件，如果失败，则为 <code>null</code>。
	 * @throws UnsupportedOperationException
	 *             不支持该操作。
	 */
	protected abstract File renameFile_Sub(File file, String newName) throws UnsupportedOperationException;

	/**
	 * 即时保存的子方法。
	 * <p>
	 * 该方法被 {@link #save_Sub()} 调用。
	 * 
	 * @throws ProcessException
	 *             过程失败。
	 * @throws UnsupportedOperationException
	 *             不支持该操作。
	 */
	protected abstract void save_Sub() throws ProcessException, UnsupportedOperationException;

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
