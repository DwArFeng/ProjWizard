package com.dwarfeng.projwiz.raefrm;

import java.awt.Component;
import java.util.Locale;
import java.util.Objects;

import javax.swing.JPanel;

import com.dwarfeng.dutil.basic.prog.Buildable;
import com.dwarfeng.dutil.develop.i18n.I18n;
import com.dwarfeng.dutil.develop.i18n.obv.I18nAdapter;
import com.dwarfeng.dutil.develop.i18n.obv.I18nObverser;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.PropUI;
import com.dwarfeng.projwiz.raefrm.model.struct.ProjProcToolkit;

/**
 * Rae框架下的工程属性用户接口。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public abstract class RaeProjectPropUI<P extends Project> extends JPanel implements PropUI {

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

	}

	private static final long serialVersionUID = -5564486679199555L;

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
	 * 释放系统资源。
	 */
	protected void dispose() {
		projProcToolkit.getLabelI18nHandler().removeObverser(i18nObverser);
	}

	/**
	 * 刷新标签。
	 */
	protected abstract void refreshLabels();
}
