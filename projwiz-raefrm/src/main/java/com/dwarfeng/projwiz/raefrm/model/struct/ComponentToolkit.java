package com.dwarfeng.projwiz.raefrm.model.struct;

import com.dwarfeng.dutil.develop.cfg.SyncExconfigModel;
import com.dwarfeng.dutil.develop.i18n.SyncI18nHandler;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;
import com.dwarfeng.projwiz.raefrm.ConstantsProvider;
import com.dwarfeng.projwiz.raefrm.model.cm.SyncPermDemandModel;

/**
 * 组件工具包。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public interface ComponentToolkit {

	/**
	 * 获取组件中的工具包。
	 * 
	 * @return 组件中的工具包。
	 */
	public Toolkit getToolkit();

	/**
	 * 获取组件中的常量提供器。
	 * 
	 * @return 组件中的常量提供器。
	 */
	public ConstantsProvider getConstantsProvider();

	/**
	 * 获取组件中的记录器国际化处理器。
	 * 
	 * @return 组件中的记录器国际化处理器。
	 */
	public SyncI18nHandler getLoggerI18nHandler();

	/**
	 * 获取组件中的标签国际化处理器。
	 * 
	 * @return 组件中的标签国际化处理器。
	 */
	public SyncI18nHandler getLabelI18nHandler();

	/**
	 * 获取组件中的配置处理器。
	 * 
	 * @return 组件中的配置处理器。
	 */
	public SyncExconfigModel getConfigModel();

	/**
	 * 获取组件中的权限需求模型。
	 * 
	 * @return 组件中的权限需求模型。
	 */
	public SyncPermDemandModel getPermDemandModel();
}
