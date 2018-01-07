package com.dwarfeng.projwiz.core.model.struct;

import java.util.Objects;

/**
 * 独立标签生成器。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public final class PrefixUniqueLabelGenerator implements UniqueLabelGenerator {

	private final String prefix;
	private long count = 1;

	/**
	 * 新实例。
	 * 
	 * @param prefix
	 *            指定的前缀。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public PrefixUniqueLabelGenerator(String prefix) {
		Objects.requireNonNull(prefix, "入口参数 prefix 不能为 null。");
		this.prefix = prefix;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof PrefixUniqueLabelGenerator)) {
			return false;
		}
		PrefixUniqueLabelGenerator other = (PrefixUniqueLabelGenerator) obj;
		if (count != other.count) {
			return false;
		}
		if (prefix == null) {
			if (other.prefix != null) {
				return false;
			}
		} else if (!prefix.equals(other.prefix)) {
			return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (count ^ (count >>> 32));
		result = prime * result + ((prefix == null) ? 0 : prefix.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String nextUniqueLabel() {
		return String.format("%s - %d", prefix, count++);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "ClassUniqueLabelGenerator [prefix=" + prefix + ", count=" + count + "]";
	}

}
