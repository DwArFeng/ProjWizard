package com.dwarfeng.projwiz.core.view.struct;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.dwarfeng.projwiz.core.view.obv.MainFrameVisibleObverser;

/**
 * 主界面可见性处理器的默认实现。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public final class DefaultMainFrameVisibleModel implements MainFrameVisibleModel {

	private final Set<MainFrameVisibleObverser> obversers;

	private final ReadWriteLock lock = new ReentrantReadWriteLock();

	private boolean northVisible;
	private boolean southVisible;
	private boolean eastVisible;
	private boolean westVisible;

	private boolean maximum;

	public DefaultMainFrameVisibleModel() {
		this(Collections.newSetFromMap(new WeakHashMap<>()), true, true, true, true, false);
	}

	public DefaultMainFrameVisibleModel(Set<MainFrameVisibleObverser> obversers, boolean northVisible,
			boolean southVisible, boolean eastVisbile, boolean westVisible, boolean maximum) {
		this.obversers = obversers;
		this.northVisible = northVisible;
		this.southVisible = southVisible;
		this.eastVisible = eastVisbile;
		this.westVisible = westVisible;
		this.maximum = maximum;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addObverser(MainFrameVisibleObverser obverser) {
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
	public Set<MainFrameVisibleObverser> getObversers() {
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
	public boolean isEastVisible() {
		lock.readLock().lock();
		try {
			return eastVisible;
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isMaximum() {
		lock.readLock().lock();
		try {
			return maximum;
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNorthVisible() {
		lock.readLock().lock();
		try {
			return northVisible;
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSouthVisible() {
		lock.readLock().lock();
		try {
			return southVisible;
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWestVisible() {
		lock.readLock().lock();
		try {
			return westVisible;
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeObverser(MainFrameVisibleObverser obverser) {
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
	public void setEastVisible(boolean aFlag) {
		lock.writeLock().lock();
		try {
			eastVisible = aFlag;
			fireEastVisibleChanged(aFlag);
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMaximum(boolean aFlag) {
		lock.writeLock().lock();
		try {
			maximum = aFlag;
			fireMaximumChanged(aFlag);
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setNorthVisible(boolean aFlag) {
		lock.writeLock().lock();
		try {
			northVisible = aFlag;
			fireNorthVisibleChanged(aFlag);
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSouthVisible(boolean aFlag) {
		lock.writeLock().lock();
		try {
			southVisible = aFlag;
			fireSouthVisibleChanged(aFlag);
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setWestVisible(boolean aFlag) {
		lock.writeLock().lock();
		try {
			westVisible = aFlag;
			fireWestVisibleChanged(aFlag);
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * 通知观察器东方面板的可见性发生了改变。
	 * 
	 * @param newValue
	 *            新值。
	 */
	private void fireEastVisibleChanged(boolean newValue) {
		obversers.forEach(obverser -> {
			obverser.fireEastVisibleChanged(newValue);
		});
	}

	/**
	 * 通知观察器主界面的最大化状态发生了改变。
	 * 
	 * @param newValue
	 *            新值。
	 */
	private void fireMaximumChanged(boolean newValue) {
		obversers.forEach(obverser -> {
			obverser.fireMaximumChanged(newValue);
		});
	}

	/**
	 * 通知观察器北方面板的可见性发生了改变。
	 * 
	 * @param newValue
	 *            新值。
	 */
	private void fireNorthVisibleChanged(boolean newValue) {
		obversers.forEach(obverser -> {
			obverser.fireNorthVisibleChanged(newValue);
		});
	}

	/**
	 * 通知观察器南方面板的可见性发生了改变。
	 * 
	 * @param newValue
	 *            新值。
	 */
	private void fireSouthVisibleChanged(boolean newValue) {
		obversers.forEach(obverser -> {
			obverser.fireSouthVisibleChanged(newValue);
		});
	}

	/**
	 * 通知观察器西方面板的可见性发生了改变。
	 * 
	 * @param newValue
	 *            新值。
	 */
	private void fireWestVisibleChanged(boolean newValue) {
		obversers.forEach(obverser -> {
			obverser.fireWestVisibleChanged(newValue);
		});
	}

	@Override
	public ReadWriteLock getLock() {
		// TODO Auto-generated method stub
		return null;
	}

}
