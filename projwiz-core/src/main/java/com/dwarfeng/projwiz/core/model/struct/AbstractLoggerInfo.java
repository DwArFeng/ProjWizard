package com.dwarfeng.projwiz.core.model.struct;

import java.util.Objects;

/**
 * 抽象记录器信息。
 * <p>
 * 记录器信息的抽象实现。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public abstract class AbstractLoggerInfo implements LoggerInfo {

	protected final String key;

	/**
	 * 生成一个指定键值的新的抽象记录器模型。
	 * 
	 * @param key
	 *            指定的键值。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public AbstractLoggerInfo(String key) {
		Objects.requireNonNull(key, "入口参数 key 不能为 null。");
		this.key = key;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dwarfeng.dutil.basic.prog.WithKey#getKey()
	 */
	@Override
	public String getKey() {
		return key;
	}

}
