package com.dwarfeng.projwiz.raefrm;

import java.awt.Component;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Objects;

import javax.swing.JPanel;

import com.dwarfeng.dutil.basic.prog.Buildable;
import com.dwarfeng.dutil.basic.str.Name;
import com.dwarfeng.dutil.develop.i18n.I18n;
import com.dwarfeng.dutil.develop.i18n.obv.I18nAdapter;
import com.dwarfeng.dutil.develop.i18n.obv.I18nObverser;
import com.dwarfeng.dutil.develop.resource.Resource;
import com.dwarfeng.dutil.develop.resource.ResourceHandler;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.PropUI;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;
import com.dwarfeng.projwiz.raefrm.model.struct.ProjProcToolkit;

/**
 * Rae框架下的工程属性用户接口。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public abstract class RaeProjectPropUI<P extends Project> extends JPanel implements PropUI {

	private static final long serialVersionUID = -13145783626707281L;

	/**
	 * Rae框架工程属性用户接口的构造器。
	 * 
	 * @author DwArFeng
	 * @since 0.0.3-alpha
	 */
	public abstract static class RaeProjectPropUIBuilder<P extends Project> implements Buildable<RaeProjectPropUI<P>> {

		/** 对应的工程处理器的工具包。 */
		protected final ProjProcToolkit projProcToolkit;
		/** 工程属性用户接口的目标工程。 */
		protected final P project;

		/**
		 * 新实例。
		 * 
		 * @param projProcToolkit
		 *            指定的工程文件处理器工具包。
		 * @param project
		 *            指定的工程。
		 * @throws NullPointerException
		 *             入口参数为 <code>null</code>。
		 */
		protected RaeProjectPropUIBuilder(ProjProcToolkit projProcToolkit, P project) {
			Objects.requireNonNull(projProcToolkit, "入口参数 projProcToolkit 不能为 null。");
			Objects.requireNonNull(project, "入口参数 project 不能为 null。");

			this.projProcToolkit = projProcToolkit;
			this.project = project;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public abstract RaeProjectPropUI<P> build();

		/**
		 * 获取用户接口的目标文件。
		 * 
		 * @return 用户接口的目标文件。
		 */
		public P getProject() {
			return project;
		}

		/**
		 * 获取用户接口的工程处理器工具箱。
		 * 
		 * @return 用户接口的工程处理器工具箱。
		 */
		public ProjProcToolkit getProjProcToolkit() {
			return projProcToolkit;
		}

	}

	/** 对应的工程处理器的工具包。 */
	protected final ProjProcToolkit projProcToolkit;
	/** 工程属性用户接口的目标工程。 */
	protected final P project;

	/** 国际化观察器。 */
	protected final I18nObverser i18nObverser = new I18nAdapter() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireCurrentLocaleChanged(Locale oldLocale, Locale newLocale, I18n newI18n) {
			refreshLabels();
		}

	};

	/**
	 * 新实例。
	 * 
	 * @param projProcToolkit
	 *            指定的工程文件处理器工具包。
	 * @param project
	 *            指定的工程。
	 * @param file
	 *            指定的文件。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected RaeProjectPropUI(ProjProcToolkit projProcToolkit, P project) {
		Objects.requireNonNull(projProcToolkit, "入口参数 projProcToolkit 不能为 null。");
		Objects.requireNonNull(project, "入口参数 project 不能为 null。");

		this.projProcToolkit = projProcToolkit;
		this.project = project;

		this.projProcToolkit.getLabelI18nHandler().addObverser(i18nObverser);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract void fireApplied();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract void fireCanceled();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract void fireConfirmed();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component getComponent() {
		return this;
	}

	/**
	 * 获取工程属性用户接口的目标工程。
	 * 
	 * @return 工程属性用户接口的目标工程。
	 */
	public P getProject() {
		return project;
	}

	/**
	 * 向记录器中输出一条调试。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void debug(Name name) {
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");

		getProjwizToolkit().debug(projProcToolkit.getLoggerI18nHandler().getStringOrDefault(name,
				com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL));
	}

	/**
	 * 释放系统资源。
	 */
	protected void dispose() {
		this.projProcToolkit.getLabelI18nHandler().removeObverser(i18nObverser);
	}

	/**
	 * 向记录器中输出一条错误。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @param e
	 *            指定的可抛出对象。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void error(Name name, Throwable e) {
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");

		getProjwizToolkit().error(projProcToolkit.getLoggerI18nHandler().getStringOrDefault(name,
				com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL), e);
	}

	/**
	 * 向记录器中输出一条致命错误。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @param e
	 *            指定的可抛出对象。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void fatal(Name name, Throwable e) {
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");

		getProjwizToolkit().fatal(projProcToolkit.getLoggerI18nHandler().getStringOrDefault(name,
				com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL), e);
	}

	/**
	 * 向记录器中格式化输出一条调试。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @param args
	 *            参数。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void formatDebug(Name name, Object... args) {
		Objects.requireNonNull(name, "入口参数 loggerStringKey 不能为 null。");
		Objects.requireNonNull(args, "入口参数 args 不能为 null。");

		getProjwizToolkit().debug(String.format(projProcToolkit.getLoggerI18nHandler().getStringOrDefault(name,
				com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL), args));
	}

	/**
	 * 向记录器中格式化输出一条错误。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @param e
	 *            指定的可抛出对象。
	 * @param args
	 *            参数。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void formatError(Name name, Throwable e, Object... args) {
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");
		Objects.requireNonNull(e, "入口参数 e 不能为 null。");
		Objects.requireNonNull(args, "入口参数 args 不能为 null。");

		getProjwizToolkit().error(String.format(projProcToolkit.getLoggerI18nHandler().getStringOrDefault(name,
				com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL), args), e);
	}

	/**
	 * 向记录器中格式化输出一条致命错误。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @param e
	 *            指定的可抛出对象。
	 * @param args
	 *            参数。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void formatFatal(Name name, Throwable e, Object... args) {
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");
		Objects.requireNonNull(e, "入口参数 e 不能为 null。");
		Objects.requireNonNull(args, "入口参数 args 不能为 null。");

		getProjwizToolkit().fatal(String.format(projProcToolkit.getLoggerI18nHandler().getStringOrDefault(name,
				com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL), args), e);
	}

	/**
	 * 向记录器中格式化输出一条信息。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @param args
	 *            参数。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void formatInfo(Name name, Object... args) {
		Objects.requireNonNull(name, "入口参数 loggerStringKey 不能为 null。");
		Objects.requireNonNull(args, "入口参数 args 不能为 null。");

		getProjwizToolkit().info(String.format(projProcToolkit.getLoggerI18nHandler().getStringOrDefault(name,
				com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL), args));
	}

	/**
	 * 返回指定标签键对应的标签格式化文本。
	 * 
	 * @param name
	 *            指定的标签键。
	 * @param args
	 *            参数。
	 * @return 指定标签键对应的格式化标签文本。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	protected String formatLabel(Name name, Object... args) {
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");
		Objects.requireNonNull(args, "入口参数 args 不能为 null。");

		return String.format(projProcToolkit.getLabelI18nHandler().getStringOrDefault(name,
				com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL), args);
	}

	/**
	 * 向记录器中格式化输出一条显示。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @param args
	 *            参数。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void formatTrace(Name name, Object... args) {
		Objects.requireNonNull(name, "入口参数 loggerStringKey 不能为 null。");
		Objects.requireNonNull(args, "入口参数 args 不能为 null。");

		getProjwizToolkit().trace(String.format(projProcToolkit.getLoggerI18nHandler().getStringOrDefault(name,
				com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL), args));
	}

	/**
	 * 向记录器中格式化输出一条警告。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @param args
	 *            参数。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void formatWarn(Name name, Object... args) {
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");
		Objects.requireNonNull(args, "入口参数 args 不能为 null。");

		getProjwizToolkit().warn(String.format(projProcToolkit.getLoggerI18nHandler().getStringOrDefault(name,
				com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL), args));
	}

	/**
	 * 向记录器中格式化输出一条警告。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @param e
	 *            指定的可抛出对象。
	 * @param args
	 *            参数。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void formatWarn(Name name, Throwable e, Object... args) {
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");
		Objects.requireNonNull(e, "入口参数 e 不能为 null。");
		Objects.requireNonNull(args, "入口参数 args 不能为 null。");

		getProjwizToolkit().warn(String.format(projProcToolkit.getLoggerI18nHandler().getStringOrDefault(name,
				com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL), args), e);
	}

	/**
	 * 获取组件的工具包。
	 * 
	 * @return 组件的工具包。
	 */
	protected final Toolkit getProjwizToolkit() {
		return projProcToolkit.getToolkit();
	}

	/**
	 * 向记录器中输出一条信息。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void info(Name name) {
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");

		getProjwizToolkit().info(projProcToolkit.getLoggerI18nHandler().getStringOrDefault(name,
				com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL));
	}

	/**
	 * 返回此组件的工具包是否不满足指定的权限键对应的所有需求。
	 * 
	 * @param permKey
	 *            指定的权限键。
	 * @return 此组件的工具包是否不满足指定的权限键对应的所有需求。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected boolean isNotPermKeyAvaliable(Name permKey) {
		Objects.requireNonNull(permKey, "入口参数 permKey 不能为 null。");
		return projProcToolkit.getPermDemandModel().isNotPermKeyAvaliable(permKey, getProjwizToolkit());
	}

	/**
	 * 返回此组件的工具包是否能满足指定的权限键对应的所有需求。
	 * 
	 * @param permKey
	 *            含有指定的权限键的名称对象。
	 * @return 此组件的工具包是否能满足指定的权限键对应的所有需求。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected boolean isPermKeyAvailable(Name permKey) {
		Objects.requireNonNull(permKey, "入口参数 permKey 不能为 null。");
		return projProcToolkit.getPermDemandModel().isPermKeyAvailable(permKey, getProjwizToolkit());
	}

	/**
	 * 返回指定标签键对应的标签文本。
	 * 
	 * @param name
	 *            指定的标签键。
	 * @return 指定的标签键对应的标签文本。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	protected String label(Name name) {
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");

		return projProcToolkit.getLabelI18nHandler().getStringOrDefault(name,
				com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL);
	}

	/**
	 * 打开指定资源键对应的资源。
	 * 
	 * <p>
	 * 该方法通过 Toolkit 获取只读配置处理器，随后获取指定资源键对应的资源，在资源自动重置之后，打开输入流。
	 * 
	 * @param resourceKey
	 *            指定的资源键对应的名称接口。
	 * @return 指定资源对应的资源键。
	 * @throws IOException
	 *             IO异常。
	 */
	protected InputStream openResource(Name resourceKey) throws IOException {
		return openResource(resourceKey, true);
	}

	/**
	 * 打开指定资源键对应的资源。
	 * 
	 * <p>
	 * 该方法通过 Toolkit 获取只读配置处理器，随后获取指定资源键对应的资源，打开输入流。
	 * 
	 * @param resourceKey
	 *            指定的资源键对应的名称接口。
	 * @param autoReset
	 *            是否自动重置。
	 * @return 指定资源对应的资源键。
	 * @throws IOException
	 *             IO异常。
	 */
	protected InputStream openResource(Name resourceKey, boolean autoReset) throws IOException {
		Objects.requireNonNull(resourceKey, "入口参数 resourceKey 不能为 null。");

		ResourceHandler cfgHandlerReadOnly = getProjwizToolkit().getCfgHandlerReadOnly();
		if (!cfgHandlerReadOnly.containsKey(resourceKey)) {
			throw new IOException(String.format("不存在指定的资源: %s", resourceKey.getName()));
		}

		Resource resource = cfgHandlerReadOnly.get(resourceKey.getName());
		if (autoReset) {
			resource.autoReset();
		}
		return resource.openInputStream();
	}

	/**
	 * 刷新标签。
	 */
	protected abstract void refreshLabels();

	/**
	 * 向记录器中输出一条显示。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void trace(Name name) {
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");

		getProjwizToolkit().trace(projProcToolkit.getLoggerI18nHandler().getStringOrDefault(name,
				com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL));
	}

	/**
	 * 向记录器中输出一条信息。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void warn(Name name) {
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");

		getProjwizToolkit().warn(projProcToolkit.getLoggerI18nHandler().getStringOrDefault(name,
				com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL));
	}

	/**
	 * 向记录器中输出一条警告。
	 * 
	 * @param name
	 *            指定的文本键。
	 * @param e
	 *            指定的可抛出对象。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	protected void warn(Name name, Throwable e) {
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");

		getProjwizToolkit().warn(projProcToolkit.getLoggerI18nHandler().getStringOrDefault(name,
				com.dwarfeng.projwiz.core.util.Constants.MISSING_LABEL), e);
	}
}
