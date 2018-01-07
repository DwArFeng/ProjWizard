package com.dwarfeng.projwiz.core.view.struct;

import com.dwarfeng.dutil.basic.prog.ObverserSet;
import com.dwarfeng.dutil.basic.threads.ExternalReadWriteThreadSafe;
import com.dwarfeng.projwiz.core.view.obv.MainFrameVisibleObverser;

/**
 * 主界面可见性模型。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface MainFrameVisibleModel extends ObverserSet<MainFrameVisibleObverser>, ExternalReadWriteThreadSafe {

	/**
	 * 获取北方面板是否可见。
	 * 
	 * @return 北方面板是否可见。
	 */
	public boolean isNorthVisible();

	/**
	 * 获取南方面板是否可见。
	 * 
	 * @return 南方面板是否可见。
	 */
	public boolean isSouthVisible();

	/**
	 * 获取东方面板是否可见。
	 * 
	 * @return 东方面板是否可见。
	 */
	public boolean isEastVisible();

	/**
	 * 获取西方面板是否可见。
	 * 
	 * @return 西方面板是否可见。
	 */
	public boolean isWestVisible();

	/**
	 * 设置北方面板是否可见。
	 * 
	 * @param aFlag
	 *            北方面板是否可见。
	 */
	public void setNorthVisible(boolean aFlag);

	/**
	 * 设置南方面板是否可见。
	 * 
	 * @param aFlag
	 *            南方面板是否可见。
	 */
	public void setSouthVisible(boolean aFlag);

	/**
	 * 设置东方面板是否可见。
	 * 
	 * @param aFlag
	 *            东方面板是否可见。
	 */
	public void setEastVisible(boolean aFlag);

	/**
	 * 设置西方面板是否可见。
	 * 
	 * @param aFlag
	 *            西方面板是否可见。
	 */
	public void setWestVisible(boolean aFlag);

	/**
	 * 获取主界面是否最大化。
	 * 
	 * @return 主界面是否最大化。
	 */
	public boolean isMaximum();

	/**
	 * 设置主界面是否最大化。
	 * 
	 * @param aFlag
	 *            是否最大化。
	 */
	public void setMaximum(boolean aFlag);

}
