package com.dwarfeng.projwiz.core.model.struct;

import java.awt.Image;

import com.dwarfeng.dutil.basic.prog.WithKey;
import com.dwarfeng.dutil.basic.str.Tag;
import com.dwarfeng.dutil.basic.threads.ExternalReadWriteThreadSafe;
import com.dwarfeng.projwiz.core.model.eum.IconVariability;

/**
 * 组件接口。
 * 
 * <p>
 * 组件是 ProjWizard的重要组成部分，通过加载不同的组件，ProjWizard可以实现各种功能。
 * 
 * <p>
 * 组件的实现需要遵循以下的规则： <br>
 * <blockquote> 1. </blockquote>
 * 
 * TODO 完善Component的接口说明。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface Component extends WithKey<String>, ExternalReadWriteThreadSafe, Tag {

	/**
	 * 当结束组件时进行的调度。
	 */
	public void dispose();

	/**
	 * 获取该组件的图标。
	 * 
	 * @return 该组件的图标。
	 */
	public Image getIcon();

	/**
	 * 获取该组件的图标的可变性。
	 * 
	 * @return 该组件的图标的可变性。
	 */
	public IconVariability getIconVarialibity();

}
