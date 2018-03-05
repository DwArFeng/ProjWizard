package com.dwarfeng.projwiz.basic4.view;

import java.awt.BorderLayout;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.dwarfeng.dutil.basic.cna.model.DefaultReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.projwiz.basic4.model.obv.MeppFileAdapter;
import com.dwarfeng.projwiz.basic4.model.obv.MeppFileObverser;
import com.dwarfeng.projwiz.basic4.model.struct.MeppFile;
import com.dwarfeng.projwiz.basic4.model.struct.MeppProject;
import com.dwarfeng.projwiz.raefrm.RaeFilePropUI;
import com.dwarfeng.projwiz.raefrm.model.struct.ProjProcToolkit;

/**
 * 内存工程处理器文件属性用户接口。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public class MeppFilePropUI extends RaeFilePropUI<MeppProject, MeppFile> {

	/**
	 * 构造器。
	 * 
	 * @author DwArFeng
	 * @since 0.0.3-alpha
	 */
	public static class Builder extends RaeFilePropUIBuilder<MeppProject, MeppFile> {

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
		public Builder(ProjProcToolkit projProcToolkit, MeppProject project, MeppFile file) {
			super(projProcToolkit, project, file);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public MeppFilePropUI build() {
			final ReferenceModel<MeppFilePropUI> projectRef = new DefaultReferenceModel<>();
			try {
				SwingUtil.invokeAndWaitInEventQueue(() -> {
					projectRef.set(new MeppFilePropUI(projProcToolkit, project, file));
				});
			} catch (InvocationTargetException | InterruptedException ignore) {
				// 抛异常也要按照基本法。
			}
			return projectRef.get();
		}

	}

	/** 界面中的表格。 */
	protected final JTable table;
	/** 指示是否可读的单选开关。 */
	protected JRadioButton rsRadioButton;
	/** 指示是否可写的单选开关。 */
	protected JRadioButton wsRadioButton;

	/** 表格所使用的模型。 */
	protected final DefaultTableModel tableModel = new DefaultTableModel() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		};

	};

	/** 表格所使用的渲染器。 */
	protected DefaultTableCellRenderer tableCellRenderer = new DefaultTableCellRenderer() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			// TODO Auto-generated method stub
			return this;
		};
	};

	/** 文件的侦听器。 */
	protected final MeppFileObverser fileObverser = new MeppFileAdapter() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireLabelAdded(String label) {
			SwingUtil.invokeInEventQueue(() -> {
				// TODO Auto-generated method stub
				super.fireLabelAdded(label);
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireLabelRemoved(String label) {
			SwingUtil.invokeInEventQueue(() -> {
				// TODO Auto-generated method stub
				super.fireLabelAdded(label);
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireReadSupportedChanged(boolean newValue) {
			SwingUtil.invokeInEventQueue(() -> {
				// TODO Auto-generated method stub
				super.fireReadSupportedChanged(newValue);
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireWriteSupportedChanged(boolean newValue) {
			SwingUtil.invokeInEventQueue(() -> {
				// TODO Auto-generated method stub
				super.fireWriteSupportedChanged(newValue);
			});
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
	protected MeppFilePropUI(ProjProcToolkit projProcToolkit, MeppProject project, MeppFile file) {
		super(projProcToolkit, project, file);

		setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
		table.setFillsViewportHeight(true);
		table.getTableHeader().setReorderingAllowed(false);

		tableModel.setColumnIdentifiers(new String[] { "Label", "BufferCapa" });

		table.setModel(tableModel);

		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(tableCellRenderer);
		}

		table.setRowHeight(tableCellRenderer.getFontMetrics(getFont()).getHeight());
		table.repaint();
		scrollPane.setViewportView(table);

		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);

		rsRadioButton = new JRadioButton("New radio button");
		panel.add(rsRadioButton);

		wsRadioButton = new JRadioButton("New radio button");
		panel.add(wsRadioButton);

		syncFile();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fireApplied() {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fireCanceled() {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fireConfirmed() {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void refreshLabels() {
		// TODO Auto-generated method stub

	}

	private void syncFile() {
		tableModel.setRowCount(0);

		file.getLock().readLock().lock();
		try {
			// TODO Auto-generated method stub
		} finally {
			file.getLock().readLock().unlock();
		}
	}

}
