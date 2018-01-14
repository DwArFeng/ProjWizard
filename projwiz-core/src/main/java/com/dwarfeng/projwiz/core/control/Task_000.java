package com.dwarfeng.projwiz.core.control;

import java.util.Objects;

import com.dwarfeng.dutil.develop.backgr.AbstractTask;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.model.eum.LoggerStringKey;
import com.dwarfeng.projwiz.core.util.Constants;

/**
 * 抽象 Note Wizard 用任务。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
abstract class ProjWizTask extends AbstractTask {

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
	public ProjWizTask(ProjWizard projWizard) {
		Objects.requireNonNull(projWizard, "入口参数 projWizard 不能为 null。");
		this.projWizard = projWizard;
	}

	/**
	 * 向记录器中输入一条信息。
	 * 
	 * @param loggerStringKey
	 *            指定的文本键。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void info(LoggerStringKey loggerStringKey) {
		Objects.requireNonNull(loggerStringKey, "入口参数 loggerStringKey 不能为 null。");

		projWizard.getToolkit().info(projWizard.getToolkit().getLoggerI18nHandler().getStringOrDefault(loggerStringKey,
				Constants.MISSING_LABEL));
	}

	/**
	 * 向记录器中格式化输入一条信息。
	 * 
	 * @param loggerStringKey
	 *            指定的文本键。
	 * @param args
	 *            参数。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void formatInfo(LoggerStringKey loggerStringKey, Object... args) {
		Objects.requireNonNull(loggerStringKey, "入口参数 loggerStringKey 不能为 null。");
		Objects.requireNonNull(args, "入口参数 args 不能为 null。");

		projWizard.getToolkit().info(String.format(projWizard.getToolkit().getLoggerI18nHandler()
				.getStringOrDefault(loggerStringKey, Constants.MISSING_LABEL), args));
	}

	/**
	 * 向记录器中输入一条警告。
	 * 
	 * @param loggerStringKey
	 *            指定的文本键。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void warn(LoggerStringKey loggerStringKey) {
		Objects.requireNonNull(loggerStringKey, "入口参数 loggerStringKey 不能为 null。");

		projWizard.getToolkit().warn(projWizard.getToolkit().getLoggerI18nHandler().getStringOrDefault(loggerStringKey,
				Constants.MISSING_LABEL));
	}

	/**
	 * 向记录器中格式化输入一条警告。
	 * 
	 * @param loggerStringKey
	 *            指定的文本键。
	 * @param args
	 *            参数。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void formatWarn(LoggerStringKey loggerStringKey, Object... args) {
		Objects.requireNonNull(loggerStringKey, "入口参数 loggerStringKey 不能为 null。");
		Objects.requireNonNull(args, "入口参数 args 不能为 null。");

		projWizard.getToolkit().warn(String.format(projWizard.getToolkit().getLoggerI18nHandler()
				.getStringOrDefault(loggerStringKey, Constants.MISSING_LABEL), args));
	}

	/**
	 * 向记录器中输入一条警告。
	 * 
	 * @param loggerStringKey
	 *            指定的文本键。
	 * @param e
	 *            指定的可抛出对象。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void warn(LoggerStringKey loggerStringKey, Throwable e) {
		Objects.requireNonNull(loggerStringKey, "入口参数 loggerStringKey 不能为 null。");
		Objects.requireNonNull(e, "入口参数 e 不能为 null。");

		projWizard.getToolkit().warn(projWizard.getToolkit().getLoggerI18nHandler().getStringOrDefault(loggerStringKey,
				Constants.MISSING_LABEL), e);
	}

	/**
	 * 向记录器中格式化输入一条警告。
	 * 
	 * @param loggerStringKey
	 *            指定的文本键。
	 * @param e
	 *            指定的可抛出对象。
	 * @param args
	 *            参数。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void formatWarn(LoggerStringKey loggerStringKey, Throwable e, Object... args) {
		Objects.requireNonNull(loggerStringKey, "入口参数 loggerStringKey 不能为 null。");
		Objects.requireNonNull(e, "入口参数 e 不能为 null。");
		Objects.requireNonNull(args, "入口参数 args 不能为 null。");

		projWizard.getToolkit().warn(String.format(projWizard.getToolkit().getLoggerI18nHandler()
				.getStringOrDefault(loggerStringKey, Constants.MISSING_LABEL), args), e);
	}

	/**
	 * 获取标签文本。
	 * 
	 * @param labelStringKey
	 *            指定的标签文本键。
	 * @return 标签文本。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	protected String label(LabelStringKey labelStringKey) {
		Objects.requireNonNull(labelStringKey, "入口参数 labelStringKey 不能为 null。");

		return projWizard.getToolkit().getLabelI18nHandler().getStringOrDefault(labelStringKey,
				Constants.MISSING_LABEL);
	}

	/**
	 * 获取格式化标签文本。
	 * 
	 * @param labelStringKey
	 *            指定的标签文本键。
	 * @param args
	 *            参数。
	 * @return 格式化标签文本。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	protected String formatLabel(LabelStringKey labelStringKey, Object... args) {
		Objects.requireNonNull(labelStringKey, "入口参数 labelStringKey 不能为 null。");
		Objects.requireNonNull(args, "入口参数 args 不能为 null。");

		return String.format(projWizard.getToolkit().getLabelI18nHandler().getStringOrDefault(labelStringKey,
				Constants.MISSING_LABEL), args);
	}

}
