package com.dwarfeng.projwiz.raefrm;

import java.util.Objects;

import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.dutil.develop.cfg.ConfigUtil;
import com.dwarfeng.dutil.develop.cfg.DefaultExconfigModel;
import com.dwarfeng.dutil.develop.cfg.SyncExconfigModel;
import com.dwarfeng.dutil.develop.i18n.DelegateI18nHandler;
import com.dwarfeng.dutil.develop.i18n.I18nUtil;
import com.dwarfeng.dutil.develop.i18n.SyncI18nHandler;
import com.dwarfeng.projwiz.api.AbstractComponent;
import com.dwarfeng.projwiz.core.model.struct.MetaDataStorage;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;
import com.dwarfeng.projwiz.raefrm.model.struct.ConstantsProvider;

/**
 * Rae框架组件。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public class RaeComponent extends AbstractComponent {

	/** 常量提供器 */
	protected final ConstantsProvider constantsProvider;

	/** 记录器国际化处理器。 */
	protected final SyncI18nHandler loggerI18nHandler = I18nUtil.syncI18nHandler(new DelegateI18nHandler());
	/** 标签国际化处理器。 */
	protected final SyncI18nHandler labelI18nHandler = I18nUtil.syncI18nHandler(new DelegateI18nHandler());
	/** 配置处理器。 */
	protected final SyncExconfigModel configModel = ConfigUtil.syncExconfigModel(new DefaultExconfigModel());

	/** Rae框架中使用的记录器国际化处理器。 */
	protected final SyncI18nHandler raeLoggerI18nHandler = I18nUtil.syncI18nHandler(new DelegateI18nHandler());
	/** Rae框架中使用的标签国际化处理器。 */
	protected final SyncI18nHandler raeLabelI18nHandler = I18nUtil.syncI18nHandler(new DelegateI18nHandler());

	protected RaeComponent(String key, ReferenceModel<? extends Toolkit> toolkitRef, MetaDataStorage metaDataStorage,
			ConstantsProvider constantsProvider) throws ProcessException {
		super(key, toolkitRef, metaDataStorage);

		Objects.requireNonNull(constantsProvider, "入口参数 constantsProvider 不能为 null。");
		this.constantsProvider = constantsProvider;

		initRae();
		initCustom();
	}

	/**
	 * 初始化Rae框架。
	 */
	private void initRae() {
		// TODO Auto-generated method stub

	}

	/**
	 * 初始化自定义配置。
	 */
	private void initCustom() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}
