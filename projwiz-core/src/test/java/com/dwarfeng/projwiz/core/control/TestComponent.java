package com.dwarfeng.projwiz.core.control;

import java.awt.Image;
import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.projwiz.core.model.eum.IconVariability;
import com.dwarfeng.projwiz.core.model.struct.Component;
import com.dwarfeng.projwiz.core.model.struct.MetaDataStorage;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;

/**
 * 测试用组件。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public final class TestComponent implements Component {

	/** 该组件的键值。 */
	protected final String key;
	/** 该组件使用的工具包引用。 */
	protected final ReferenceModel<? extends Toolkit> toolkitRef;
	/** 该组件使用的元数据仓库。 */
	protected final MetaDataStorage metaDataStorage;

	/** 该组件的同步读写锁。 */
	protected final ReadWriteLock lock = new ReentrantReadWriteLock();

	/**
	 * 
	 * @param key
	 * @param toolkitRef
	 * @param metaDataStorage
	 * @return
	 * @throws ProcessException
	 */
	public static TestComponent newInstance(String key, ReferenceModel<Toolkit> toolkitRef,
			MetaDataStorage metaDataStorage) throws ProcessException {
		return new TestComponent(key, toolkitRef, metaDataStorage);
	}

	/**
	 * 新实例。
	 * 
	 * @param key
	 *            指定的键。
	 * @param toolkitRef
	 *            指定的工具包引用。
	 * @param metaDataStorage
	 *            指定的元数据仓库。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	protected TestComponent(String key, ReferenceModel<? extends Toolkit> toolkitRef, MetaDataStorage metaDataStorage) {
		Objects.requireNonNull(key, "入口参数 key 不能为 null。");
		Objects.requireNonNull(toolkitRef, "入口参数 toolkitRef 不能为 null。");
		Objects.requireNonNull(metaDataStorage, "入口参数 metaDataStorage 不能为 null。");

		this.key = key;
		this.toolkitRef = toolkitRef;
		this.metaDataStorage = metaDataStorage;
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
		// 不实现。
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDescription() {
		// 不实现。
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		// 不实现。
	}

	@Override
	public Image getIcon() {
		// 不实现。
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IconVariability getIconVarialibity() {
		// 不实现。
		return null;
	}

}
