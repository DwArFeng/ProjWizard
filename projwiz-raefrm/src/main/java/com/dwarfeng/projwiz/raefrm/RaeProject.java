package com.dwarfeng.projwiz.raefrm;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
import com.dwarfeng.projwiz.core.model.struct.Toolkit;
import com.dwarfeng.projwiz.core.util.ModelUtil;
import com.dwarfeng.projwiz.raefrm.model.struct.ProjProcToolkit;

/**
 * Rae框架文件工程。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public abstract class RaeProject implements Project {

	/** 工程的同步读写锁。 */
	protected final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	/** 工程的观察器集合。 */
	protected final Set<ProjectObverser> obversers = Collections.newSetFromMap(new WeakHashMap<>());

	/** 对应的工程处理器的工具包。 */
	protected final ProjProcToolkit projprocToolkit;
	/** 工程的名称。 */
	protected final String name;
	/** 抽象工程的工程树。 */
	protected final Tree<File> fileTree;

	/**
	 * 新实例。
	 * 
	 * @param projprocToolkit
	 *            对应的工程处理器的工具包。
	 * @param name
	 *            工程的名称。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public RaeProject(ProjProcToolkit projprocToolkit, String name) {
		this(projprocToolkit, name, new MapTree<>());
	}

	/**
	 * 新实例。
	 * 
	 * @param projprocToolkit
	 *            对应的工程处理器的工具包。
	 * @param name
	 *            工程的名称。
	 * @param fileTree
	 *            已有的工程树。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public RaeProject(ProjProcToolkit projprocToolkit, String name, Tree<File> fileTree) {
		Objects.requireNonNull(projprocToolkit, "入口参数 projprocToolkit 不能为 null。");
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");
		Objects.requireNonNull(fileTree, "入口参数 fileTree 不能为 null。");

		this.projprocToolkit = projprocToolkit;
		this.name = name;
		this.fileTree = fileTree;
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
	public String getName() {
		return name;
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
	public String getRegisterKey() {
		return projprocToolkit.getKey();
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
	public File addFile(File parent, File file, AddingSituation situation) {
		throw new UnsupportedOperationException("addFile");
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
	public File removeFile(File file, RemovingSituation situation) {
		throw new UnsupportedOperationException("removeFile");
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
	public File renameFile(File file, String newName) {
		throw new UnsupportedOperationException("renameFile");
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
	public void save() throws ProcessException {
		throw new UnsupportedOperationException("save");
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

	/**
	 * 获取组件的工具包。
	 * 
	 * @return 组件的工具包。
	 */
	protected final Toolkit getToolkit() {
		return projprocToolkit.getToolkit();
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
