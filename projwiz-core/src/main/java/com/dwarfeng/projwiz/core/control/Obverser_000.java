package com.dwarfeng.projwiz.core.control;

import java.util.Objects;

import com.dwarfeng.dutil.basic.prog.Obverser;

/**
 * 抽象 ProjWizard 用观察器。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
abstract class ProjWizObverser implements Obverser {

	/** ProjWizard实例。 */
	protected final ProjWizard projWizard;

	/**
	 * 新实例。
	 * 
	 * @param projWizard
	 *            指定的ProjWizard实例。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public ProjWizObverser(ProjWizard projWizard) {
		Objects.requireNonNull(projWizard, "入口参数 projWizard 不能为 null。");
		this.projWizard = projWizard;
	}
}