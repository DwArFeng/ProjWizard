package com.dwarfeng.projwiz.core.view.struct;

import java.awt.Window;

import com.dwarfeng.dutil.basic.prog.WithKey;

/**
 * 窗口提供器。
 * 
 * <p>
 * 该提供器能提供一个窗口。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface WindowSuppiler extends WithKey<String> {

	/**
	 * 获取提供器提供的窗口。
	 * 
	 * @return 提供器提供的窗口。
	 */
	public Window getWindow();

	/**
	 * 该提供器是否已经被释放。
	 * 
	 * @return 是否已经被释放。
	 */
	public boolean isDispose();

}
