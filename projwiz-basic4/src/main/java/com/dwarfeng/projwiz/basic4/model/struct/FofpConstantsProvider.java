package com.dwarfeng.projwiz.basic4.model.struct;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import com.dwarfeng.dutil.basic.str.Name;
import com.dwarfeng.dutil.develop.setting.SettingEnumItem;
import com.dwarfeng.projwiz.basic4.model.eum.FofpConfigItem;
import com.dwarfeng.projwiz.basic4.model.eum.ResourceKey;
import com.dwarfeng.projwiz.basic4.util.Constants;
import com.dwarfeng.projwiz.raefrm.model.eum.FileCoreConfigItem;
import com.dwarfeng.projwiz.raefrm.model.struct.ConstantsProvider;

/**
 * 内存工程处理器常量提供器。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public class FofpConstantsProvider implements ConstantsProvider {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDefaultLoggerI18nPath() {
		return Constants.RESOURCE_FOFP_I18N_LOGGER_PATH;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Name getResourceKey(ResourceKeyType type) {
		switch (type) {
		case CONFIGURATION_CORE:
			return ResourceKey.FOFP_CONFIGURATION_CORE;
		case PERM_DEMAND_SETTING:
			return ResourceKey.FOFP_PERM_DEMAND_SETTING;
		case I18N_LABEL_FILE_SETTING:
			return ResourceKey.FOFP_I18N_LABEL_FILE_SETTING;
		case I18N_LABEL_RESOURCE_SETTING:
			return ResourceKey.FOFP_I18N_LABEL_RESOURCE_SETTING;
		case I18N_LOGGER_FILE_SETTING:
			return ResourceKey.FOFP_I18N_LOGGER_FILE_SETTING;
		case I18N_LOGGER_RESOURCE_SETTING:
			return ResourceKey.FOFP_I18N_LOGGER_RESOURCE_SETTING;
		case IMAGE_MODULE:
			return ResourceKey.FOFP_IMAGE_MODULE;
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
		entries.addAll(Arrays.asList(FileCoreConfigItem.values()));
		entries.addAll(Arrays.asList(FofpConfigItem.values()));
		return entries;
	}

}
