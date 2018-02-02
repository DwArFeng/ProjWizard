package com.dwarfeng.projwiz.api;

import java.awt.Image;
import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.dutil.basic.gui.awt.ImageUtil;
import com.dwarfeng.projwiz.api.model.eum.ImageKey;
import com.dwarfeng.projwiz.api.util.Constants;
import com.dwarfeng.projwiz.core.model.eum.IconVariability;
import com.dwarfeng.projwiz.core.model.struct.Component;
import com.dwarfeng.projwiz.core.model.struct.MetaDataStorage;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;

/**
 * 抽象组件接口。
 * 
 * <p>
 * 组件接口的抽象实现。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public abstract class AbstractComponent implements Component {

	/** 该组件的键值。 */
	protected final String key;
	/** 该组件使用的工具包引用。 */
	protected final ReferenceModel<? extends Toolkit> toolkitRef;
	/** 该组件使用的元数据仓库。 */
	protected final MetaDataStorage metaDataStorage;

	/** 该组件的同步读写锁。 */
	protected final ReadWriteLock lock = new ReentrantReadWriteLock();

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
	protected AbstractComponent(String key, ReferenceModel<? extends Toolkit> toolkitRef,
			MetaDataStorage metaDataStorage) {
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
	public void dispose() {
		return;
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
	public Image getIcon() {
		return ImageUtil.getInternalImage(ImageKey.COMPONENT, Constants.IMAGE_LOAD_FAILED, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IconVariability getIconVarialibity() {
		return IconVariability.FIX;
	}

}
