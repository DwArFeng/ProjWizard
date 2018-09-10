package com.dwarfeng.projwiz.core.view.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Objects;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

import com.dwarfeng.dutil.basic.cna.model.SyncListModel;
import com.dwarfeng.dutil.basic.cna.model.SyncMapModel;
import com.dwarfeng.dutil.basic.cna.model.SyncReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.SyncSetModel;
import com.dwarfeng.dutil.basic.gui.awt.ImageSize;
import com.dwarfeng.dutil.basic.gui.awt.ImageUtil;
import com.dwarfeng.dutil.basic.gui.swing.JAdjustableBorderPanel;
import com.dwarfeng.dutil.basic.gui.swing.JExconsole;
import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.dutil.develop.i18n.I18nHandler;
import com.dwarfeng.dutil.develop.setting.SyncSettingHandler;
import com.dwarfeng.projwiz.core.model.cm.SyncModuleModel;
import com.dwarfeng.projwiz.core.model.eum.ImageKey;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.model.struct.Editor;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.ProjectFilePair;
import com.dwarfeng.projwiz.core.view.obv.MainFrameVisibleAdapter;
import com.dwarfeng.projwiz.core.view.obv.MainFrameVisibleObverser;
import com.dwarfeng.projwiz.core.view.struct.GuiManager;
import com.dwarfeng.projwiz.core.view.struct.GuiManager.ExecType;
import com.dwarfeng.projwiz.core.view.struct.MainFrameVisibleModel;

/**
 * 主界面。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class MainFrame extends ProjWizFrame {

	private static final long serialVersionUID = -972111665260066583L;

	private final JPanel contentPane;
	private final MfPanel_01 mfPanel_01;
	private final MfMenuBar_01 mfMenuBar_01;
	private final MfPanel_02 mfPanel_02;
	private final JExconsole outConsole;
	private final JExconsole errConsole;
	private final JTabbedPane tabbedPane;
	private final MfDesktopPane_01 desktopPane;

	private final InputStream sysIn;
	private final PrintStream sysOut;
	private final PrintStream sysErr;
	private final JAdjustableBorderPanel adjPanel_01;
	private final JAdjustableBorderPanel adjPanel_02;

	private MainFrameVisibleModel visibleModel;
	private SyncReferenceModel<File> anchorFileModel;
	private SyncReferenceModel<Project> focusProjectModel;
	private SyncSetModel<File> focusFileModel;
	private SyncListModel<Project> holdProjectModel;
	private SyncMapModel<Project, Editor> focusEditorModel;
	private SyncSettingHandler coreSettingHandler;
	private SyncModuleModel moduleModel;

	private final MainFrameVisibleObverser visibleModelObverser = new MainFrameVisibleAdapter() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireEastVisibleChanged(boolean newValue) {
			SwingUtil.invokeInEventQueue(() -> {
				adjPanel_01.setEastVisible(newValue && !visibleModel.isMaximum());
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireMaximumChanged(boolean newValue) {
			SwingUtil.invokeInEventQueue(() -> {
				adjPanel_01.setNorthVisible(visibleModel.isNorthVisible() && !newValue);
				adjPanel_02.setSouthVisible(visibleModel.isSouthVisible() && !newValue);
				adjPanel_01.setEastVisible(visibleModel.isEastVisible() && !newValue);
				adjPanel_01.setWestVisible(visibleModel.isWestVisible() && !newValue);
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireNorthVisibleChanged(boolean newValue) {
			SwingUtil.invokeInEventQueue(() -> {
				adjPanel_01.setNorthVisible(newValue && !visibleModel.isMaximum());
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireSouthVisibleChanged(boolean newValue) {
			SwingUtil.invokeInEventQueue(() -> {
				adjPanel_02.setSouthVisible(newValue && !visibleModel.isMaximum());
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireWestVisibleChanged(boolean newValue) {
			SwingUtil.invokeInEventQueue(() -> {
				adjPanel_01.setWestVisible(newValue && !visibleModel.isMaximum());
			});
		}

	};

	private int frameWidth;
	private int frameHeight;

	/**
	 * 新实例。
	 */
	public MainFrame() {
		this(null, null, null, null, null, null, null, null, null, null, null);
	}

	/**
	 * 
	 * @param guiManager
	 * @param i18nHandler
	 * @param moduleModel
	 * @param editorModel
	 * @param visibleModel
	 * @param anchorFileModel
	 * @param focusProjectModel
	 * @param focusFileModel
	 * @param holdProjectModel
	 * @param focusEditorModel
	 * @param coreSettingHandler
	 */
	public MainFrame(GuiManager guiManager, I18nHandler i18nHandler, SyncModuleModel moduleModel,
			SyncMapModel<ProjectFilePair, Editor> editorModel, MainFrameVisibleModel visibleModel,
			SyncReferenceModel<File> anchorFileModel, SyncReferenceModel<Project> focusProjectModel,
			SyncSetModel<File> focusFileModel, SyncListModel<Project> holdProjectModel,
			SyncMapModel<Project, Editor> focusEditorModel, SyncSettingHandler coreSettingHandler) {
		super(guiManager, i18nHandler);

		sysIn = System.in;
		sysOut = System.out;
		sysErr = System.err;

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				MainFrame.this.guiManager.tryExit(ExecType.CONCURRENT);
			}
		});

		addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				if ((getExtendedState() & JFrame.MAXIMIZED_HORIZ) == 0) {
					frameWidth = e.getComponent().getWidth();
				}
				if ((getExtendedState() & JFrame.MAXIMIZED_VERT) == 0) {
					frameHeight = e.getComponent().getHeight();
				}
			}
		});

		setIconImage(ImageUtil.getInternalImage(ImageKey.ICON, ImageSize.ICON_SUPER_LARGE));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 800, 600);

		mfMenuBar_01 = new MfMenuBar_01(guiManager, i18nHandler, anchorFileModel, focusProjectModel, focusFileModel,
				focusEditorModel, editorModel, coreSettingHandler, visibleModel);
		setJMenuBar(mfMenuBar_01);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		adjPanel_01 = new JAdjustableBorderPanel();
		adjPanel_01.setNorthSeparatorEnabled(false);
		adjPanel_01.setNorthEnabled(true);
		adjPanel_01.setEastEnabled(true);
		adjPanel_01.setSeperatorThickness(5);
		adjPanel_01.setWestEnabled(true);
		contentPane.add(adjPanel_01, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		adjPanel_01.add(panel, BorderLayout.WEST);
		panel.setLayout(new BorderLayout(0, 0));

		mfPanel_01 = new MfPanel_01(guiManager, i18nHandler, focusProjectModel, holdProjectModel, moduleModel);
		panel.add(mfPanel_01, BorderLayout.NORTH);

		mfPanel_02 = new MfPanel_02(guiManager, i18nHandler, anchorFileModel, focusProjectModel, focusFileModel,
				moduleModel);
		panel.add(mfPanel_02, BorderLayout.CENTER);

		JPanel panel_1 = new JPanel();
		adjPanel_01.add(panel_1, BorderLayout.EAST);

		adjPanel_02 = new JAdjustableBorderPanel();
		adjPanel_02.setSeperatorThickness(5);
		adjPanel_02.setSouthEnabled(true);
		adjPanel_01.add(adjPanel_02, BorderLayout.CENTER);

		desktopPane = new MfDesktopPane_01(guiManager, i18nHandler, editorModel, focusProjectModel, focusEditorModel);
		desktopPane.setBackground(new Color(UIManager.getColor("Panel.background").getRGB()));

		adjPanel_02.add(desktopPane, BorderLayout.CENTER);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		adjPanel_02.add(tabbedPane, BorderLayout.SOUTH);

		outConsole = new JExconsole(3000, 0.1, 10, false);
		tabbedPane.addTab(label(LabelStringKey.MAINFRAME_1),
				new ImageIcon(ImageUtil.getInternalImage(ImageKey.OUT_CONSOLE, ImageSize.ICON_SMALL)), outConsole,
				null);

		errConsole = new JExconsole();
		tabbedPane.addTab(label(LabelStringKey.MAINFRAME_2), null, errConsole, null); // TODO
																						// 添加错误流图片。

		System.setIn(outConsole.in);
		System.setOut(outConsole.out);
		System.setErr(errConsole.out);

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new CompoundBorder(new LineBorder(new Color(0, 0, 0)),
				new EtchedBorder(EtchedBorder.LOWERED, null, null)));
		adjPanel_01.add(panel_3, BorderLayout.NORTH);

		if (Objects.nonNull(visibleModel)) {
			visibleModel.addObverser(visibleModelObverser);
		}

		this.visibleModel = visibleModel;
		this.anchorFileModel = anchorFileModel;
		this.focusProjectModel = focusProjectModel;
		this.focusFileModel = focusFileModel;
		this.holdProjectModel = holdProjectModel;
		this.focusEditorModel = focusEditorModel;
		this.coreSettingHandler = coreSettingHandler;
		this.moduleModel = moduleModel;

		syncVisibleModel();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		super.dispose();
		System.setIn(sysIn);
		System.setOut(sysOut);
		System.setErr(sysErr);

		if (Objects.nonNull(visibleModel)) {
			visibleModel.removeObverser(visibleModelObverser);
		}

		mfPanel_01.dispose();
		mfMenuBar_01.dispose();
		mfPanel_02.dispose();
		outConsole.dispose();
		errConsole.dispose();
		desktopPane.dispose();
		super.dispose();
	}

	/**
	 * @return the anchorFileModel
	 */
	public SyncReferenceModel<File> getAnchorFileModel() {
		return anchorFileModel;
	}

	/**
	 * @return the coreSettingHandler
	 */
	public SyncSettingHandler getCoreSettingHandler() {
		return coreSettingHandler;
	}

	/**
	 * 获取东方面板的表现尺寸。
	 * 
	 * @return 东方面板的表现尺寸。
	 */
	public int getEastPreferredValue() {
		return adjPanel_01.getEastPreferredValue();
	}

	/**
	 * @return the focusEditorModel
	 */
	public SyncMapModel<Project, Editor> getFocusEditorModel() {
		return focusEditorModel;
	}

	/**
	 * @return the focusFileModel
	 */
	public SyncSetModel<File> getFocusFileModel() {
		return focusFileModel;
	}

	/**
	 * @return the focusProjectModel
	 */
	public SyncReferenceModel<Project> getFocusProjectModel() {
		return focusProjectModel;
	}

	/**
	 * 获得窗口的高度。
	 * <p>
	 * 此高度是指界面最后一次非最大化的窗口高度。
	 * 
	 * @return 窗口的高度。
	 */
	public int getFrameHeight() {
		return frameHeight;
	}

	/**
	 * 获得窗口的宽度。
	 * <p>
	 * 此高度是指界面最后一次非最大化的窗口宽度。
	 * 
	 * @return 窗口的宽度。
	 */
	public int getFrameWidth() {
		return frameWidth;
	}

	/**
	 * @return the holdProjectModel
	 */
	public SyncListModel<Project> getHoldProjectModel() {
		return holdProjectModel;
	}

	/**
	 * @return the moduleModel
	 */
	public SyncModuleModel getModuleModel() {
		return moduleModel;
	}

	/**
	 * 获取主界面的南方面板的表现尺寸。
	 * 
	 * @return 南方面板的表现尺寸。
	 */
	public int getSouthPreferredValue() {
		return adjPanel_02.getSouthPreferredValue();
	}

	/**
	 * 获取主界面的可见性处理器。
	 * 
	 * @return 主界面的可见性处理器。
	 */
	public MainFrameVisibleModel getVisibleModel() {
		return visibleModel;
	}

	/**
	 * 获取西方面板的表现尺寸。
	 * 
	 * @return 西方面板的表现尺寸。
	 */
	public int getWestPreferredValue() {
		return adjPanel_01.getWestPreferredValue();
	}

	/**
	 * @param anchorFileModel
	 *            the anchorFileModel to set
	 */
	public void setAnchorFileModel(SyncReferenceModel<File> anchorFileModel) {
		// TODO 调用以下类的此方法
		// /ProjWizard/src/projwiz/com/dwarfeng/projwiz/view/gui/MfPanel_01.java
		// /ProjWizard/src/projwiz/com/dwarfeng/projwiz/view/gui/MfPanel_02.java
		// /ProjWizard/src/projwiz/com/dwarfeng/projwiz/view/gui/MfMenu_01.java
		// /ProjWizard/src/projwiz/com/dwarfeng/projwiz/view/gui/MfMenu_02.java
		this.anchorFileModel = anchorFileModel;

	}

	/**
	 * @param coreSettingHandler
	 *            the coreSettingHandler to set
	 */
	public void setCoreSettingHandler(SyncSettingHandler coreSettingHandler) {
		this.coreSettingHandler = coreSettingHandler;
		mfMenuBar_01.setCoreSettingHandler(coreSettingHandler);
	}

	/**
	 * 设置东方面板的表现尺寸。
	 * 
	 * @param eastPreferredValue
	 *            东方面板的表现尺寸。
	 */
	public void setEastPreferredValue(int eastPreferredValue) {
		adjPanel_01.setEastPreferredValue(eastPreferredValue);
	}

	/**
	 * @param focusEditorModel
	 *            the focusEditorModel to set
	 */
	public void setFocusEditorModel(SyncMapModel<Project, Editor> focusEditorModel) {
		// TODO 调用以下类的此方法
		// /ProjWizard/src/projwiz/com/dwarfeng/projwiz/view/gui/MfPanel_01.java
		// /ProjWizard/src/projwiz/com/dwarfeng/projwiz/view/gui/MfPanel_02.java
		// /ProjWizard/src/projwiz/com/dwarfeng/projwiz/view/gui/MfMenu_01.java
		// /ProjWizard/src/projwiz/com/dwarfeng/projwiz/view/gui/MfMenu_02.java
		this.focusEditorModel = focusEditorModel;
	}

	/**
	 * @param focusFileModel
	 *            the focusFileModel to set
	 */
	public void setFocusFileModel(SyncSetModel<File> focusFileModel) {
		// TODO 调用以下类的此方法
		// /ProjWizard/src/projwiz/com/dwarfeng/projwiz/view/gui/MfPanel_01.java
		// /ProjWizard/src/projwiz/com/dwarfeng/projwiz/view/gui/MfPanel_02.java
		// /ProjWizard/src/projwiz/com/dwarfeng/projwiz/view/gui/MfMenu_01.java
		// /ProjWizard/src/projwiz/com/dwarfeng/projwiz/view/gui/MfMenu_02.java
		this.focusFileModel = focusFileModel;
	}

	/**
	 * @param focusProjectModel
	 *            the focusProjectModel to set
	 */
	public void setFocusProjectModel(SyncReferenceModel<Project> focusProjectModel) {
		// TODO 调用以下类的此方法
		// /ProjWizard/src/projwiz/com/dwarfeng/projwiz/view/gui/MfPanel_01.java
		// /ProjWizard/src/projwiz/com/dwarfeng/projwiz/view/gui/MfPanel_02.java
		// /ProjWizard/src/projwiz/com/dwarfeng/projwiz/view/gui/MfMenu_01.java
		// /ProjWizard/src/projwiz/com/dwarfeng/projwiz/view/gui/MfMenu_02.java
		this.focusProjectModel = focusProjectModel;
	}

	/**
	 * @param holdProjectModel
	 *            the holdProjectModel to set
	 */
	public void setHoldProjectModel(SyncListModel<Project> holdProjectModel) {
		// TODO 调用以下类的此方法
		// /ProjWizard/src/projwiz/com/dwarfeng/projwiz/view/gui/MfPanel_01.java
		// /ProjWizard/src/projwiz/com/dwarfeng/projwiz/view/gui/MfPanel_02.java
		// /ProjWizard/src/projwiz/com/dwarfeng/projwiz/view/gui/MfMenu_01.java
		// /ProjWizard/src/projwiz/com/dwarfeng/projwiz/view/gui/MfMenu_02.java
		// /ProjWizard/src/projwiz/com/dwarfeng/projwiz/view/gui/MfDesktopPane_01.java
		this.holdProjectModel = holdProjectModel;
	}

	/**
	 * @param moduleModel
	 *            the moduleModel to set
	 */
	public void setModuleModel(SyncModuleModel moduleModel) {
		this.moduleModel = moduleModel;
		mfPanel_01.setModuleModel(moduleModel);
		mfPanel_02.setModuleModel(moduleModel);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSize(Dimension d) {
		this.frameWidth = d.width;
		this.frameHeight = d.height;
		super.setSize(d);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSize(int width, int height) {
		this.frameWidth = width;
		this.frameHeight = height;
		super.setSize(width, height);
	}

	/**
	 * 设置主界面的南方面板的表现尺寸。
	 * 
	 * @param southPreferredValue
	 *            南方面板的表现尺寸。
	 */
	public void setSouthPreferredValue(int southPreferredValue) {
		adjPanel_02.setSouthPreferredValue(southPreferredValue);
	}

	/**
	 * 设置主界面的可见性模型。
	 * 
	 * @param visibleModel
	 *            指定的可见性模型。
	 */
	public void setVisibleModel(MainFrameVisibleModel visibleModel) {
		if (Objects.nonNull(this.visibleModel)) {
			this.visibleModel.removeObverser(visibleModelObverser);
		}

		if (Objects.nonNull(visibleModel)) {
			visibleModel.addObverser(visibleModelObverser);
		}

		this.visibleModel = visibleModel;

		syncVisibleModel();
	}

	/**
	 * 设置西方面板的表现尺寸。
	 * 
	 * @param westPreferredValue
	 *            西方面板的表现尺寸。
	 */
	public void setWestPreferredValue(int westPreferredValue) {
		adjPanel_01.setWestPreferredValue(westPreferredValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void refreshLabels() {
		tabbedPane.setTitleAt(0, label(LabelStringKey.MAINFRAME_1));
		tabbedPane.setTitleAt(1, label(LabelStringKey.MAINFRAME_2));
	}

	private void syncVisibleModel() {
		adjPanel_01.setNorthVisible(false);
		adjPanel_02.setSouthVisible(false);
		adjPanel_01.setEastVisible(false);
		adjPanel_01.setWestVisible(false);

		if (Objects.isNull(visibleModel)) {
			return;
		}

		adjPanel_01.setNorthVisible(visibleModel.isNorthVisible() && !visibleModel.isMaximum());
		adjPanel_02.setSouthVisible(visibleModel.isSouthVisible() && !visibleModel.isMaximum());
		adjPanel_01.setEastVisible(visibleModel.isEastVisible() && !visibleModel.isMaximum());
		adjPanel_01.setWestVisible(visibleModel.isWestVisible() && !visibleModel.isMaximum());

	}

}
