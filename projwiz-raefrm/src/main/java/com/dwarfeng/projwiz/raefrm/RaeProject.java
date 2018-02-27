package com.dwarfeng.projwiz.raefrm;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.dwarfeng.dutil.basic.prog.Buildable;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.dutil.basic.str.Name;
import com.dwarfeng.dutil.develop.resource.Resource;
import com.dwarfeng.dutil.develop.resource.ResourceHandler;
import com.dwarfeng.projwiz.core.model.cm.MapTree;
import com.dwarfeng.projwiz.core.model.cm.Tree;
import com.dwarfeng.projwiz.core.model.cm.Tree.Path;
import com.dwarfeng.projwiz.core.model.obv.ProjectObverser;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.ProjectProcessor;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;
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
		protected final ProjProcToolkit projprocToolkit;

		/** 抽象工程的工程树。 */
		protected Tree<File> fileTree = new MapTree<>();
		/** Rae工程的文件名称映射。 */
		protected Map<File, String> fileNameMap = new HashMap<>();

		/** 工程的观察器集合。 */
		protected Set<ProjectObverser> obversers = Collections.newSetFromMap(new WeakHashMap<>());

		/**
		 * 新实例。
		 * 
		 * @param processorClass
		 *            指定的处理器类。
		 * @param name
		 *            指定的名称。
		 * @param projprocToolkit
		 *            指定的工程文件处理器工具包。
		 * @throws NullPointerException
		 *             入口参数为 <code>null</code>。
		 */
		protected RaeProjectBuilder(Class<? extends ProjectProcessor> processorClass, String name,
				ProjProcToolkit projprocToolkit) {
			Objects.requireNonNull(processorClass, "入口参数 processorClass 不能为 null。");
			Objects.requireNonNull(name, "入口参数 name 不能为 null。");
			Objects.requireNonNull(projprocToolkit, "入口参数 projprocToolkit 不能为 null。");

			this.processorClass = processorClass;
			this.name = name;
			this.projprocToolkit = projprocToolkit;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public abstract RaeProject build();

		/**
		 * 获取工程的文件-名称映射。
		 * 
		 * @return 工程的文件-名称映射。
		 */
		public Map<File, String> getFileNameMap() {
			return fileNameMap;
		}

		/**
		 * 获取工程的文件树。
		 * 
		 * @return 工程的文件树。
		 */
		public Tree<File> getFileTree() {
			return fileTree;
		}

		/**
		 * 获取工程的名称。
		 * 
		 * @return 工程的名称。
		 */
		public String getName() {
			return name;
		}

		/**
		 * 获取工程的观察器集合。
		 * 
		 * @return 工程的观察器集合。
		 */
		public Set<ProjectObverser> getObversers() {
			return obversers;
		}

		/**
		 * 获取工程的处理器类。
		 * 
		 * @return 工程的处理器类。
		 */
		public Class<? extends ProjectProcessor> getProcessorClass() {
			return processorClass;
		}

		/**
		 * 获取工程的工程文件处理器工具包。
		 * 
		 * @return 工程的工程文件处理器工具包。
		 */
		public ProjProcToolkit getProjprocToolkit() {
			return projprocToolkit;
		}

		/**
		 * 设置工程中的文件-名称映射。
		 * 
		 * @param fileNameMap
		 *            指定的文件-名称映射。
		 * @return 构造器自身。
		 */
		public RaeProjectBuilder setFileNameMap(Map<File, String> fileNameMap) {
			this.fileNameMap = Objects.isNull(fileNameMap) ? new HashMap<>() : fileNameMap;
			return this;
		}

		/**
		 * 设置工程中的文件树。
		 * 
		 * @param fileTree
		 *            指定的文件树。
		 * @return 构造器自身。
		 */
		public RaeProjectBuilder setFileTree(Tree<File> fileTree) {
			this.fileTree = Objects.isNull(fileTree) ? new MapTree<>() : fileTree;
			return this;
		}

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
	protected final ProjProcToolkit projprocToolkit;

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
	 * @param projprocToolkit
	 *            该工程的工程处理器工具包。
	 * @param obversers
	 *            该工程的观察器集合。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected RaeProject(Class<? extends ProjectProcessor> processorClass, String name, Tree<File> fileTree,
			Map<File, String> fileNameMap, ProjProcToolkit projprocToolkit, Set<ProjectObverser> obversers) {
		Objects.requireNonNull(processorClass, "入口参数 processorClass 不能为 null。");
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");
		Objects.requireNonNull(fileTree, "入口参数 fileTree 不能为 null。");
		Objects.requireNonNull(fileNameMap, "入口参数 fileNameMap 不能为 null。");
		Objects.requireNonNull(projprocToolkit, "入口参数 projprocToolkit 不能为 null。");
		Objects.requireNonNull(obversers);

		this.processorClass = processorClass;
		this.name = name;
		this.fileTree = fileTree;
		this.fileNameMap = fileNameMap;
		this.projprocToolkit = projprocToolkit;
		this.obversers = obversers;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File addFile(File parent, File file, AddingSituation situation) {
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

	/**
	 * 向记录器中格式化输出一条信息。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @param args
	 *            参数。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void formatInfo(Name name, Object... args) {
		Objects.requireNonNull(name, "入口参数 loggerStringKey 不能为 null。");
		Objects.requireNonNull(args, "入口参数 args 不能为 null。");

		getToolkit().info(String.format(projprocToolkit.getLoggerI18nHandler().getStringOrDefault(name,
				com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL), args));
	}

	/**
	 * 向记录器中格式化输出一条警告。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @param args
	 *            参数。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void formatWarn(Name name, Object... args) {
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");
		Objects.requireNonNull(args, "入口参数 args 不能为 null。");

		getToolkit().warn(String.format(projprocToolkit.getLoggerI18nHandler().getStringOrDefault(name,
				com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL), args));
	}

	/**
	 * 向记录器中格式化输出一条警告。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @param e
	 *            指定的可抛出对象。
	 * @param args
	 *            参数。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void formatWarn(Name name, Throwable e, Object... args) {
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");
		Objects.requireNonNull(e, "入口参数 e 不能为 null。");
		Objects.requireNonNull(args, "入口参数 args 不能为 null。");

		getToolkit().warn(String.format(projprocToolkit.getLoggerI18nHandler().getStringOrDefault(name,
				com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL), args), e);
	}

	/**
	 * 获取组件的工具包。
	 * 
	 * @return 组件的工具包。
	 */
	protected final Toolkit getToolkit() {
		return projprocToolkit.getToolkit();
	}

	/**
	 * 向记录器中输出一条信息。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void info(Name name) {
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");

		getToolkit().info(projprocToolkit.getLoggerI18nHandler().getStringOrDefault(name,
				com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL));
	}

	/**
	 * 返回此组件的工具包是否不满足指定的权限键对应的所有需求。
	 * 
	 * @param permKey
	 *            指定的权限键。
	 * @return 此组件的工具包是否不满足指定的权限键对应的所有需求。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected boolean isNotPermKeyAvaliable(Name permKey) {
		Objects.requireNonNull(permKey, "入口参数 permKey 不能为 null。");
		return projprocToolkit.getPermDemandModel().isNotPermKeyAvaliable(permKey, getToolkit());
	}

	/**
	 * 返回此组件的工具包是否能满足指定的权限键对应的所有需求。
	 * 
	 * @param permKey
	 *            含有指定的权限键的名称对象。
	 * @return 此组件的工具包是否能满足指定的权限键对应的所有需求。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected boolean isPermKeyAvailable(Name permKey) {
		Objects.requireNonNull(permKey, "入口参数 permKey 不能为 null。");
		return projprocToolkit.getPermDemandModel().isPermKeyAvailable(permKey, getToolkit());
	}

	/**
	 * 打开指定资源键对应的资源。
	 * 
	 * <p>
	 * 该方法通过 Toolkit 获取只读配置处理器，随后获取指定资源键对应的资源，在资源自动重置之后，打开输入流。
	 * 
	 * @param resourceKey
	 *            指定的资源键对应的名称接口。
	 * @return 指定资源对应的资源键。
	 * @throws IOException
	 *             IO异常。
	 */
	protected InputStream openResource(Name resourceKey) throws IOException {
		return openResource(resourceKey, true);
	}

	/**
	 * 打开指定资源键对应的资源。
	 * 
	 * <p>
	 * 该方法通过 Toolkit 获取只读配置处理器，随后获取指定资源键对应的资源，打开输入流。
	 * 
	 * @param resourceKey
	 *            指定的资源键对应的名称接口。
	 * @param autoReset
	 *            是否自动重置。
	 * @return 指定资源对应的资源键。
	 * @throws IOException
	 *             IO异常。
	 */
	protected InputStream openResource(Name resourceKey, boolean autoReset) throws IOException {
		Objects.requireNonNull(resourceKey, "入口参数 resourceKey 不能为 null。");

		ResourceHandler cfgHandlerReadOnly = getToolkit().getCfgHandlerReadOnly();
		if (!cfgHandlerReadOnly.containsKey(resourceKey)) {
			throw new IOException(String.format("不存在指定的资源: %s", resourceKey.getName()));
		}

		Resource resource = cfgHandlerReadOnly.get(resourceKey.getName());
		if (autoReset) {
			resource.autoReset();
		}
		return resource.openInputStream();
	}

	/**
	 * 向记录器中输出一条信息。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void warn(Name name) {
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");

		getToolkit().warn(projprocToolkit.getLoggerI18nHandler().getStringOrDefault(name,
				com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL));
	}

	/**
	 * 向记录器中输出一条警告。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @param e
	 *            指定的可抛出对象。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void warn(Name name, Throwable e) {
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");

		getToolkit().warn(projprocToolkit.getLoggerI18nHandler().getStringOrDefault(name,
				com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL), e);
	}

}
