package com.dwarfeng.projwiz.core.control;

import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.projwiz.core.model.struct.Module;
import com.dwarfeng.projwiz.core.model.struct.MetaDataStorage;
import com.dwarfeng.projwiz.core.model.struct.TestSimpleModule;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;

/**
 * 测试用组件。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public abstract class TestModule extends TestSimpleModule implements Module {

	/** 该组件使用的工具包引用。 */
	protected final ReferenceModel<? extends Toolkit> toolkitRef;
	/** 该组件使用的元数据仓库。 */
	protected final MetaDataStorage metaDataStorage;

	/** 该组件的同步读写锁。 */
	protected final ReadWriteLock lock = new ReentrantReadWriteLock();

	/**
	 * 新实例。
	 * 
	 * @param toolkitRef
	 *            指定的工具包引用。
	 * @param metaDataStorage
	 *            指定的元数据仓库。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	protected TestModule(ReferenceModel<? extends Toolkit> toolkitRef, MetaDataStorage metaDataStorage) {
		Objects.requireNonNull(toolkitRef, "入口参数 toolkitRef 不能为 null。");
		Objects.requireNonNull(metaDataStorage, "入口参数 metaDataStorage 不能为 null。");

		this.toolkitRef = toolkitRef;
		this.metaDataStorage = metaDataStorage;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReadWriteLock getLock() {
		return lock;
	}

}
