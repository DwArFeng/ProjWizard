package com.dwarfeng.projwiz.api;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.util.Arrays;
import java.util.EnumMap;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.dwarfeng.dutil.basic.gui.awt.ImageSize;
import com.dwarfeng.dutil.basic.gui.awt.ImageUtil;
import com.dwarfeng.dutil.basic.gui.swing.MuaListModel;
import com.dwarfeng.projwiz.api.model.eum.ImageKey;

public class Test_ImageKey extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	private final EnumMap<ImageKey, Icon> imageMap = new EnumMap<>(ImageKey.class);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					Test_ImageKey frame = new Test_ImageKey();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Test_ImageKey() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);

		JList<ImageKey> list = new JList<>();
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setVisibleRowCount(0);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(list);

		list.setCellRenderer(new DefaultListCellRenderer() {

			private static final long serialVersionUID = 1L;

			/**
			 * {@inheritDoc}
			 */
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (imageMap.containsKey(value)) {
					setIcon(imageMap.get(value));
				} else {
					Icon icon = new ImageIcon(ImageUtil.getInternalImage((ImageKey) value, ImageUtil.getDefaultImage(),
							ImageSize.ICON_LARGE));
					imageMap.put((ImageKey) value, icon);
					setIcon(icon);
				}

				setVerticalTextPosition(SwingConstants.BOTTOM);
				setHorizontalTextPosition(SwingConstants.CENTER);
				setHorizontalAlignment(SwingConstants.CENTER);
				return this;
			}
		});

		MuaListModel<ImageKey> listModel = new MuaListModel<>();
		listModel.addAll(Arrays.asList(ImageKey.values()));

		list.setModel(listModel);
	}

}
