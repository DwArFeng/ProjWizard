package com.dwarfeng.projwiz.basic4.model.struct;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import com.dwarfeng.dutil.basic.str.Name;
import com.dwarfeng.dutil.develop.cfg.struct.ExconfigEntry;
import com.dwarfeng.dutil.develop.setting.SettingEnumItem;
import com.dwarfeng.projwiz.basic4.model.eum.MeppConfigItem;
import com.dwarfeng.projwiz.basic4.model.eum.ResourceKey;
import com.dwarfeng.projwiz.basic4.util.Constants;
import com.dwarfeng.projwiz.raefrm.model.eum.ProjCoreConfigItem;
import com.dwarfeng.projwiz.raefrm.model.struct.ConstantsProvider;

/**
 * 内存工程处理器常量提供器。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public class MeppConstantsProvider implements ConstantsProvider {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDefaultLoggerI18nPath() {
		return Constants.RESOURCE_MEPP_I18N_LOGGER_PATH;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Name getResourceKey(ResourceKeyType type) {
		switch (type) {
		case CONFIGURATION_CORE:
			return ResourceKey.MEPP_CONFIGURATION_CORE;
		case PERM_DEMAND_SETTING:
			return ResourceKey.MEPP_PERM_DEMAND_SETTING;
		case I18N_LABEL_FILE_SETTING:
			return ResourceKey.MEPP_I18N_LABEL_FILE_SETTING;
		case I18N_LABEL_RESOURCE_SETTING:
			return ResourceKey.MEPP_I18N_LABEL_RESOURCE_SETTING;
		case I18N_LOGGER_FILE_SETTING:
			return ResourceKey.MEPP_I18N_LOGGER_FILE_SETTING;
		case I18N_LOGGER_RESOURCE_SETTING:
			return ResourceKey.MEPP_I18N_LOGGER_RESOURCE_SETTING;
		case IMAGE_MODULE:
			return ResourceKey.MEPP_IMAGE_MODULE;
		default:
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<SettingEnumItem> getSettingEnumItems() {
		Collection<SettingEnumItem> entries = new HashSet<>();
		entries.addAll(Arrays.asList(ProjCoreConfigItem.values()));
		entries.addAll(Arrays.asList(MeppConfigItem.values()));
		return entries;
	}

}
