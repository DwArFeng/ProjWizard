package com.dwarfeng.projwiz.basic4.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import com.dwarfeng.dutil.basic.cna.model.DefaultReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.projwiz.basic4.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.basic4.model.struct.MeppProject;
import com.dwarfeng.projwiz.raefrm.RaeProjectPropUI;
import com.dwarfeng.projwiz.raefrm.model.struct.ProjProcToolkit;

/**
 * 内存工程处理器工程属性用户接口。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public class MeppProjectPropUI extends RaeProjectPropUI<MeppProject> {

	private static final long serialVersionUID = -1365080258775148295L;

	/**
	 * 构造器。
	 * 
	 * @author DwArFeng
	 * @since 0.0.3-alpha
	 */
	public static class Builder extends RaeProjectPropUIBuilder<MeppProject> {

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
		public Builder(ProjProcToolkit projProcToolkit, MeppProject project) {
			super(projProcToolkit, project);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public MeppProjectPropUI build() {
			final ReferenceModel<MeppProjectPropUI> projectRef = new DefaultReferenceModel<>();
			try {
				SwingUtil.invokeAndWaitInEventQueue(() -> {
					projectRef.set(new MeppProjectPropUI(projProcToolkit, project));
				});
			} catch (InvocationTargetException | InterruptedException ignore) {
				// 抛异常也要按照基本法。
			}
			return projectRef.get();
		}

	}

	private final JLabel label1;
	private final JSpinner spinner;

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
	protected MeppProjectPropUI(ProjProcToolkit projProcToolkit, MeppProject project) {
		super(projProcToolkit, project);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 17, 117, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 21, 30, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		label1 = new JLabel(this.projProcToolkit.label(LabelStringKey.MEPP_PROJECT_PROPUI_0));
		GridBagConstraints gbc_label1 = new GridBagConstraints();
		gbc_label1.fill = GridBagConstraints.HORIZONTAL;
		gbc_label1.insets = new Insets(0, 0, 5, 5);
		gbc_label1.gridx = 0;
		gbc_label1.gridy = 1;
		add(label1, gbc_label1);

		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
		GridBagConstraints gbc_spinner = new GridBagConstraints();
		gbc_spinner.insets = new Insets(0, 0, 5, 5);
		gbc_spinner.fill = GridBagConstraints.BOTH;
		gbc_spinner.gridx = 1;
		gbc_spinner.gridy = 1;
		add(spinner, gbc_spinner);

		syncProject();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fireApplied() {
		project.setDefaultBuffCapa((int) spinner.getValue());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fireCanceled() {
		dispose();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fireConfirmed() {
		project.setDefaultBuffCapa((int) spinner.getValue());
		dispose();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void refreshLabels() {
		label1.setText(projProcToolkit.label(LabelStringKey.MEPP_PROJECT_PROPUI_0));
	}

	private void syncProject() {
		spinner.setValue(0);

		if (Objects.isNull(project)) {
			return;
		}

		project.getLock().readLock().lock();
		try {
			spinner.setValue(project.getDefaultBuffCapa());
		} finally {
			project.getLock().readLock().unlock();
		}
	}

}
