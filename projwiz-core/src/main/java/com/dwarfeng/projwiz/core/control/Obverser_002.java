package com.dwarfeng.projwiz.core.control;

import java.util.Locale;

import com.dwarfeng.dutil.develop.backgr.AbstractTask;
import com.dwarfeng.dutil.develop.cfg.ConfigFirmProps;
import com.dwarfeng.dutil.develop.cfg.ConfigKey;
import com.dwarfeng.dutil.develop.cfg.obv.ExconfigObverser;
import com.dwarfeng.dutil.develop.cfg.struct.ValueParser;
import com.dwarfeng.projwiz.core.model.eum.CoreConfiguration;
import com.dwarfeng.projwiz.core.model.struct.Toolkit.BackgroundType;

final class CoreConfigObverserImpl extends ProjWizObverser implements ExconfigObverser {

	public CoreConfigObverserImpl(ProjWizard projWizard) {
		super(projWizard);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fireCurrentValueChanged(ConfigKey configKey, String oldValue, String newValue, String validValue) {
		projWizard.getToolkit().submitTask(new AbstractTask() {

			@Override
			protected void todo() throws Exception {
				if (configKey.equals(CoreConfiguration.I18N_LOGGER.getConfigKey())) {
					projWizard.getToolkit().getLoggerI18nHandler()
							.setCurrentLocale(projWizard.getToolkit().getCoreConfigModel()
									.getParsedValue(CoreConfiguration.I18N_LOGGER.getConfigKey(), Locale.class));
				}
				if (configKey.equals(CoreConfiguration.I18N_LABEL.getConfigKey())) {
					projWizard.getToolkit().getLabelI18nHandler()
							.setCurrentLocale(projWizard.getToolkit().getCoreConfigModel()
									.getParsedValue(CoreConfiguration.I18N_LABEL.getConfigKey(), Locale.class));
				}
			}
		}, BackgroundType.CONCURRENT);

	}

	@Override
	public void fireConfigKeyCleared() {
	}

	@Override
	public void fireConfigFirmPropsChanged(ConfigKey configKey, ConfigFirmProps oldValue, ConfigFirmProps newValue) {
	}

	@Override
	public void fireConfigKeyRemoved(ConfigKey configKey, ConfigFirmProps configFirmProps, ValueParser valueParser,
			String currentValue) {
	}

	@Override
	public void fireConfigKeyAdded(ConfigKey configKey, ConfigFirmProps configFirmProps, ValueParser valueParser,
			String currentValue) {
	}

	@Override
	public void fireValueParserChanged(ConfigKey configKey, ValueParser oldValue, ValueParser newValue) {
	}

}