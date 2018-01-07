package com.dwarfeng.projwiz.core.model.cm;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import com.dwarfeng.dutil.basic.cna.model.KeySetModel;
import com.dwarfeng.dutil.basic.cna.model.MapKeySetModel;
import com.dwarfeng.dutil.basic.cna.model.obv.SetObverser;
import com.dwarfeng.dutil.develop.resource.Resource;
import com.dwarfeng.projwiz.core.model.obv.ProcessorConfigObverser;
import com.dwarfeng.projwiz.core.model.struct.ProcessorConfigInfo;

/**
 * 代理集合处理器配置处理器。
 * <p>
 * 通过代理一个 <code>KeySetModel</code>来实现的处理器配置处理器，并且在此基础上增加了处理器配置处理器的实现。
 * 
 * @author DwArFeng
 * @since 0.1.1-beta
 */
public final class DelegateProcessorConfigHandler implements ProcessorConfigHandler {

	private final KeySetModel<String, ProcessorConfigInfo> delegate;

	private File direction;

	/**
	 * 生成一个默认的处理器配置处理器。
	 */
	public DelegateProcessorConfigHandler() {
		this(new MapKeySetModel<>(), null);
	}

	/**
	 * 生成一个具有指定的代理和初始的目录的处理器配置处理器。
	 * 
	 * @param delegate
	 *            指定的代理。
	 * @param initialDirection
	 *            指定的初始目录。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public DelegateProcessorConfigHandler(KeySetModel<String, ProcessorConfigInfo> delegate, File initialDirection) {
		Objects.requireNonNull(delegate, "入口参数 delegate 不能为 null。");
		this.delegate = delegate;
		this.direction = Objects.isNull(initialDirection) ? new File("") : initialDirection;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean add(ProcessorConfigInfo e) {
		return delegate.add(e);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAll(Collection<? extends ProcessorConfigInfo> c) {
		return delegate.addAll(c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addObverser(SetObverser<ProcessorConfigInfo> obverser) {
		return delegate.addObverser(obverser);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		delegate.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clearObverser() {
		delegate.clearObverser();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(Object o) {
		return delegate.contains(o);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		return delegate.containsAll(c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsAllKey(Collection<?> c) {
		return delegate.containsAllKey(c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsKey(Object key) {
		return delegate.containsKey(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		return delegate.equals(o);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProcessorConfigInfo get(String key) {
		return delegate.get(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File getDirection() {
		return direction;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<SetObverser<ProcessorConfigInfo>> getObversers() {
		return delegate.getObversers();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return delegate.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<ProcessorConfigInfo> iterator() {
		return delegate.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Resource newResource(String key) {
		ProcessorConfigInfo info = delegate.get(key);
		return info.newResource(direction);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(Object o) {
		return delegate.remove(o);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		return delegate.removeAll(c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAllKey(Collection<?> c) {
		return delegate.removeAllKey(c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeKey(Object key) {
		return delegate.removeKey(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeObverser(SetObverser<ProcessorConfigInfo> obverser) {
		return delegate.removeObverser(obverser);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		return delegate.retainAll(c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean retainAllKey(Collection<?> c) {
		return delegate.retainAllKey(c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean setDirection(File direction) {
		File oldValue = direction;
		File newValue = Objects.isNull(direction) ? new File("") : direction;
		this.direction = newValue;

		fireDirectionChanged(oldValue, newValue);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return delegate.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return delegate.toArray();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T[] toArray(T[] a) {
		return delegate.toArray(a);
	}

	/**
	 * 通知处理器的目录发生了改变。
	 * 
	 * @param oldValue
	 *            旧的目录。
	 * @param newValue
	 *            新的目录。
	 */
	private void fireDirectionChanged(File oldValue, File newValue) {
		for (SetObverser<ProcessorConfigInfo> obverser : delegate.getObversers()) {
			if (Objects.nonNull(obverser) && obverser instanceof ProcessorConfigObverser) {
				((ProcessorConfigObverser) obverser).fireDirectionChanged(oldValue, newValue);
			}
		}
	}

}
