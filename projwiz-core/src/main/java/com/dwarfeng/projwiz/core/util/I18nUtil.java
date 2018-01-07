package com.dwarfeng.projwiz.core.util;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

import com.dwarfeng.dutil.develop.i18n.I18n;
import com.dwarfeng.dutil.develop.i18n.PropertiesI18n;

/**
 * 国际化工具包。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class I18nUtil {

	private static final I18n I18N_LOGGER;
	private static final I18n I18N_LABEL;

	static {
		Properties loggerProperties = new Properties();
		Properties labelProperties = new Properties();
		try {
			loggerProperties.load(I18nUtil.class.getResourceAsStream(Constants.RESOURCE_I18N_LOGGER_PATH));
			labelProperties.load(I18nUtil.class.getResourceAsStream(Constants.RESOURCE_I18N_LABEL_PATH));
		} catch (IOException e) {
			// 由于该资源在包内，在不破坏包结构的情况下不可能出现异常。
			e.printStackTrace();
		}
		I18N_LOGGER = new PropertiesI18n(loggerProperties);
		I18N_LABEL = new PropertiesI18n(labelProperties);
	}

	/**
	 * 获取指定键对应的记录器文本。
	 * <p>
	 * 当指定的国际化接口为 <code>null</code>的时候，则使用默认的国际化接口。
	 * 
	 * @param i18n
	 *            指定的国际化接口。
	 * @param key
	 *            指定的记录器文本键。
	 * @return 指定的键对应的记录器的文本。
	 */
	public final static String getLoggerString(I18n i18n, String key) {
		if (Objects.isNull(i18n)) {
			return I18N_LOGGER.getStringOrDefault(key, Constants.MISSING_LABEL);
		} else {
			return i18n.getStringOrDefault(key, Constants.MISSING_LABEL);
		}
	}

	/**
	 * 获取指定键对应的标签文本。
	 * <p>
	 * 当指定的国际化接口为 <code>null</code>的时候，则使用默认的国际化接口。
	 * 
	 * @param i18n
	 *            指定的国际化接口。
	 * @param key
	 *            指定的标签文本键。
	 * @return 指定的键对应的记录器的文本。
	 */
	public final static String getLabelString(I18n i18n, String key) {
		if (Objects.isNull(i18n)) {
			return I18N_LABEL.getStringOrDefault(key, Constants.MISSING_LABEL);
		} else {
			return i18n.getStringOrDefault(key, Constants.MISSING_LABEL);
		}
	}

	// 禁止外部实例化
	private I18nUtil() {
	}

}
