package com.dwarfeng.projwiz.core.model.cm;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.dwarfeng.dutil.basic.cna.model.KeySetModel;
import com.dwarfeng.dutil.basic.cna.model.MapKeySetModel;
import com.dwarfeng.dutil.basic.cna.model.obv.SetObverser;
import com.dwarfeng.projwiz.core.model.obv.LoggerObverser;
import com.dwarfeng.projwiz.core.model.struct.Logger;
import com.dwarfeng.projwiz.core.model.struct.LoggerInfo;

/**
 * 代理记录器处理器。
 * <p>
 * 通过代理一个 <code>KeySetModel</code> 来实现的记录器处理器。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public final class DelegateLoggerHandler implements LoggerHandler {

	private class DelegateIterator implements Iterator<LoggerInfo> {

		private final Iterator<LoggerInfo> delegate;
		private LoggerInfo loggerInfo;

		public DelegateIterator(Iterator<LoggerInfo> delegate) {
			this.delegate = delegate;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return delegate.hasNext();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public LoggerInfo next() {
			loggerInfo = delegate.next();
			return loggerInfo;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			delegate.remove();
			unuseOne(loggerInfo == null ? null : loggerInfo.getKey());
		}

	}

	private final KeySetModel<String, LoggerInfo> delegate;

	private final Map<String, Logger> usingMap = new LinkedHashMap<>();

	/**
	 * 生成一个默认的代理记录器处理器。
	 */
	public DelegateLoggerHandler() {
		this(new MapKeySetModel<>());
	}

	/**
	 * 生成一个具有指定代理的记录器处理器。
	 * 
	 * @param delegate
	 *            指定的代理。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public DelegateLoggerHandler(KeySetModel<String, LoggerInfo> delegate) {
		Objects.requireNonNull(delegate, "入口参数 delegate 不能为 null。");
		this.delegate = delegate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean add(LoggerInfo e) {
		return delegate.add(e);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAll(Collection<? extends LoggerInfo> c) {
		return delegate.addAll(c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addObverser(SetObverser<LoggerInfo> obverser) {
		return delegate.addObverser(obverser);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		delegate.clear();
		unuseAll();
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
	public void debug(String message) {
		Objects.requireNonNull(message, "入口参数 message 不能为 null。");
		usingMap.values().forEach(logger -> {
			logger.debug(message);
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		return delegate.equals(o);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(String message, Throwable t) {
		Objects.requireNonNull(message, "入口参数 message 不能为 null。");
		Objects.requireNonNull(t, "入口参数 t 不能为 null。");
		usingMap.values().forEach(logger -> {
			logger.error(message, t);
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fatal(String message, Throwable t) {
		Objects.requireNonNull(message, "入口参数 message 不能为 null。");
		Objects.requireNonNull(t, "入口参数 t 不能为 null。");
		usingMap.values().forEach(logger -> {
			logger.fatal(message, t);
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LoggerInfo get(String key) {
		return delegate.get(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<SetObverser<LoggerInfo>> getObversers() {
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
	public void info(String message) {
		Objects.requireNonNull(message, "入口参数 message 不能为 null。");
		usingMap.values().forEach(logger -> {
			logger.info(message);
		});
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
	public Iterator<LoggerInfo> iterator() {
		return new DelegateIterator(delegate.iterator());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(Object o) {
		if (delegate.remove(o)) {
			LoggerInfo loggerInfo = (LoggerInfo) o;
			unuse(loggerInfo == null ? null : loggerInfo.getKey());
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		Objects.requireNonNull(c, "入口参数 c 不能为 null。");
		return batchRemove(c, true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAllKey(Collection<?> c) {
		Objects.requireNonNull(c, "入口参数 c 不能为 null。");
		return batchRemoveKey(c, true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeKey(Object key) {
		if (delegate.removeKey(key)) {
			unuseOne((String) key);
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeObverser(SetObverser<LoggerInfo> obverser) {
		return delegate.removeObverser(obverser);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		Objects.requireNonNull(c, "入口参数 c 不能为 null。");
		return batchRemove(c, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean retainAllKey(Collection<?> c) {
		Objects.requireNonNull(c, "入口参数 c 不能为 null。");
		return batchRemoveKey(c, false);
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
	 * {@inheritDoc}
	 */
	@Override
	public void trace(String message) {
		Objects.requireNonNull(message, "入口参数 message 不能为 null。");
		usingMap.values().forEach(logger -> {
			logger.trace(message);
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean unuse(String... keys) {
		Objects.requireNonNull(keys, "入口参数 keys 不能为 null。");

		boolean aFlag = false;
		for (String key : keys) {
			if (useOne(key)) {
				aFlag = true;
			}
		}
		return aFlag;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean unuseAll() {
		boolean aFlag = false;
		for (LoggerInfo loggerInfo : this) {
			unuseOne(loggerInfo == null ? null : loggerInfo.getKey());
		}
		return aFlag;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean use(String... keys) {
		Objects.requireNonNull(keys, "入口参数 keys 不能为 null。");

		boolean aFlag = false;
		for (String key : keys) {
			if (useOne(key))
				aFlag = true;
		}
		return aFlag;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean useAll() {
		boolean aFlag = false;
		for (LoggerInfo loggerInfo : this) {
			useOne(loggerInfo == null ? null : loggerInfo.getKey());
		}
		return aFlag;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Logger> usedLoggers() {
		return Collections.unmodifiableCollection(usingMap.values());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(String message) {
		Objects.requireNonNull(message, "入口参数 message 不能为 null。");
		usingMap.values().forEach(logger -> {
			logger.warn(message);
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(String message, Throwable t) {
		Objects.requireNonNull(message, "入口参数 message 不能为 null。");
		Objects.requireNonNull(t, "入口参数 t 不能为 null。");
		usingMap.values().forEach(logger -> {
			logger.warn(message, t);
		});
	}

	/**
	 * 通知观察器指定的记录器停止使用。
	 * 
	 * @param key
	 *            指定的记录器对应的键。
	 * @param logger
	 *            指定的记录器。
	 */
	protected void fireLoggerUnused(String key, Logger logger) {
		for (SetObverser<LoggerInfo> obverser : delegate.getObversers()) {
			if (Objects.nonNull(obverser) && obverser instanceof LoggerObverser) {
				try {
					((LoggerObverser) obverser).fireLoggerUnused(key, logger);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 通知观察器指定的记录器被使用。
	 * 
	 * @param key
	 *            指定的记录器对应的键。
	 * @param logger
	 *            指定的记录器。
	 */
	protected void fireLoggerUsed(String key, Logger logger) {
		for (SetObverser<LoggerInfo> obverser : delegate.getObversers()) {
			if (Objects.nonNull(obverser) && obverser instanceof LoggerObverser) {
				try {
					((LoggerObverser) obverser).fireLoggerUsed(key, logger);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private boolean batchRemove(Collection<?> c, boolean aFlag) {
		boolean result = false;

		for (LoggerInfo loggerInfo : delegate) {

			if (c.contains(loggerInfo) == aFlag) {
				remove(loggerInfo);
				result = true;
			}
		}

		return result;
	}

	private boolean batchRemoveKey(Collection<?> c, boolean aFlag) {
		boolean result = false;

		for (LoggerInfo loggerInfo : delegate) {

			if (c.contains(loggerInfo == null ? null : loggerInfo.getKey()) == aFlag) {
				removeKey(loggerInfo);
				result = true;
			}
		}

		return result;
	}

	private boolean unuseOne(String key) {
		if (!usingMap.containsKey(key))
			return false;
		Logger logger = usingMap.remove(key);
		fireLoggerUnused(key, logger);
		return true;
	}

	private boolean useOne(String key) {
		if (!containsKey(key))
			return false;
		LoggerInfo loggerInfo = get(key);
		if (Objects.isNull(loggerInfo))
			return false;
		try {
			Logger logger = loggerInfo.newLogger();
			usingMap.put(key, logger);
			fireLoggerUsed(key, logger);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
