package com.dwarfeng.projwiz.api;

import java.awt.Image;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.dwarfeng.dutil.basic.gui.awt.CommonIconLib;
import com.dwarfeng.dutil.basic.gui.awt.ImageSize;
import com.dwarfeng.dutil.basic.gui.awt.ImageUtil;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.dutil.develop.resource.Resource;
import com.dwarfeng.projwiz.core.model.obv.ProcessorObverser;
import com.dwarfeng.projwiz.core.model.struct.Processor;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;
import com.dwarfeng.projwiz.core.util.Constants;

/**
 * 抽象处理器。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public abstract class AbstractProcessor implements Processor {

	/** 观察器集合。 */
	protected final Set<ProcessorObverser> obversers = Collections.newSetFromMap(new WeakHashMap<>());;

	/** 同步读写锁。 */
	protected final ReadWriteLock lock = new ReentrantReadWriteLock();
	/** 键值 */
	protected final String key;
	/** 指向配置的资源。 */
	protected final Resource configResource;
	/** 工具包。 */
	protected Toolkit toolkit;

	/**
	 * 新实例。
	 * 
	 * @param key
	 *            指定的键值。
	 * @param configResource
	 *            指向配置的资源。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public AbstractProcessor(String key, Resource configResource) {
		Objects.requireNonNull(key, "入口参数 key 不能为 null。");
		Objects.requireNonNull(configResource, "入口参数 configResource 不能为 null。");

		this.key = key;
		this.configResource = configResource;

		toolkit = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addObverser(ProcessorObverser obverser) {
		return obversers.add(obverser);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clearObverser() {
		obversers.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Resource getConfigResource() {
		return configResource;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDescription() {
		return key;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Image getIcon() {
		return ImageUtil.getInternalImage(CommonIconLib.UNKNOWN_BLUE, ImageSize.ICON_SUPER_LARGE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getKey() {
		return key;
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
		return key;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<ProcessorObverser> getObversers() {
		return Collections.unmodifiableSet(obversers);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Toolkit getToolkit() {
		lock.readLock().lock();
		try {
			return Objects.isNull(toolkit) ? Constants.NON_PERMISSION_TOOLKIT : toolkit;
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void loadConfig() throws ProcessException {
		return;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeObverser(ProcessorObverser obverser) {
		return obversers.remove(obverser);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveConfig() throws ProcessException {
		return;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * 该方法会首先触发 {@link #doToolkitRemove()}，表示当前的toolkit已经移除，
	 * 随后，会将处理器中的工具包设置为指定的工具包， 最后调用 {@link #doToolkitSet()}方法。
	 * 
	 */
	@Override
	public boolean setToolkit(Toolkit toolkit) {
		lock.writeLock().lock();
		try {
			Toolkit oldValue = Objects.isNull(this.toolkit) ? Constants.NON_PERMISSION_TOOLKIT : this.toolkit;
			Toolkit newValue = Objects.isNull(toolkit) ? Constants.NON_PERMISSION_TOOLKIT : toolkit;

			this.toolkit = newValue;
			fireToolkitChanged(oldValue, newValue);
			return true;
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * 通知处理器中的观察器发生了改变。
	 * 
	 * @param oldValue
	 *            旧观察器。
	 * @param newValue
	 *            新观察器。
	 */
	protected void fireToolkitChanged(Toolkit oldValue, Toolkit newValue) {
		obversers.forEach(obverser -> {
			obverser.fireToolkitChanged(oldValue, newValue);
		});
	}

}
