package com.dwarfeng.projwiz.core.view.gui;

import java.beans.PropertyVetoException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Objects;
import java.util.Queue;

import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import com.dwarfeng.dutil.basic.cna.model.SyncMapModel;
import com.dwarfeng.dutil.basic.cna.model.SyncReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.obv.MapAdapter;
import com.dwarfeng.dutil.basic.cna.model.obv.MapObverser;
import com.dwarfeng.dutil.basic.cna.model.obv.ReferenceAdapter;
import com.dwarfeng.dutil.basic.cna.model.obv.ReferenceObverser;
import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.dutil.develop.i18n.I18nHandler;
import com.dwarfeng.projwiz.core.model.struct.Editor;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.ProjectFilePair;
import com.dwarfeng.projwiz.core.view.struct.GuiManager;
import com.dwarfeng.projwiz.core.view.struct.GuiManager.ExecType;

final class MfDesktopPane_01 extends ProjWizDesktopPane {

	private static final long serialVersionUID = 6805251038140134948L;
	private static final String DF_FOCUSEDITORMODEL_FIREPUT = "A";
	private static final String DF_FOCUSEDITORMODEL_FIRECHANGED = "B";
	private static final String DF_FOCUSEDITORMODEL_FIREREMOVED = "C";

	private SyncMapModel<ProjectFilePair, Editor> editorModel;
	private SyncReferenceModel<Project> focusProjectModel;
	private SyncMapModel<Project, Editor> focusEditorModel;

	private MapObverser<ProjectFilePair, Editor> editorObverser = new MapAdapter<ProjectFilePair, Editor>() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireChanged(ProjectFilePair key, Editor oldValue, Editor newValue) {
			SwingUtil.invokeInEventQueue(() -> {
				if (Objects.equals(oldValue, newValue))
					return;

				bk: for (JInternalFrame internalFrame : getAllFrames()) {
					MfInternalFrame_01 frame = (MfInternalFrame_01) internalFrame;
					if (Objects.equals(newValue, frame.getEditor())) {
						frame.removeInternalFrameListener(internalFrameListener);
						frame.dispose();
						remove(frame);
						break bk;
					}
				}

				Project focusProject = Objects.isNull(focusProjectModel) ? null : focusProjectModel.get();

				MfInternalFrame_01 frame = new MfInternalFrame_01(guiManager, i18nHandler, newValue);
				frame.addInternalFrameListener(internalFrameListener);
				frame.setSize(100, 100);

				boolean aFlag = Objects.equals(focusProject, newValue.getEditProject())
						&& Objects.nonNull(focusProject);

				if (aFlag) {
					frame.setVisible(true);
				} else {
					frame.setVisible(false);
				}

				add(frame);
				frame.pack();
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireCleared() {
			SwingUtil.invokeInEventQueue(() -> {
				disposeAllInternalFrame();
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void firePut(ProjectFilePair key, Editor value) {
			SwingUtil.invokeInEventQueue(() -> {
				Project focusProject = Objects.isNull(focusProjectModel) ? null : focusProjectModel.get();

				MfInternalFrame_01 frame = new MfInternalFrame_01(guiManager, i18nHandler, value);
				frame.addInternalFrameListener(internalFrameListener);
				frame.setSize(100, 100);

				boolean aFlag = Objects.equals(focusProject, value.getEditProject()) && Objects.nonNull(focusProject);

				if (aFlag) {
					frame.setVisible(true);
				} else {
					frame.setVisible(false);
				}

				add(frame);
				frame.pack();

			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireRemoved(ProjectFilePair key, Editor value) {
			SwingUtil.invokeInEventQueue(() -> {
				for (JInternalFrame internalFrame : getAllFrames()) {
					MfInternalFrame_01 frame = (MfInternalFrame_01) internalFrame;
					if (Objects.equals(value, frame.getEditor())) {
						frame.removeInternalFrameListener(internalFrameListener);
						frame.dispose();
						remove(frame);
					}
				}
			});
		}

	};

	private final MapObverser<Project, Editor> focusEditorObverser = new MapAdapter<Project, Editor>() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireChanged(Project key, Editor oldValue, Editor newValue) {
			SwingUtil.invokeInEventQueue(() -> {
				if (checkDuplexingForecast(new Object[] { DF_FOCUSEDITORMODEL_FIRECHANGED, key, oldValue, newValue })) {
					return;
				}

				Project focusProject = Objects.isNull(focusProjectModel) ? null : focusProjectModel.get();
				if (!Objects.equals(focusProject, key))
					return;
				if (Objects.equals(oldValue, newValue))
					return;

				adjustFlag = true;
				try {
					if (Objects.isNull(newValue)) {
						if (Objects.nonNull(getSelectedFrame())) {
							try {
								getSelectedFrame().setSelected(false);
							} catch (PropertyVetoException e) {
								e.printStackTrace();
							}
						}
					} else {
						for (JInternalFrame internalFrame : getAllFrames()) {
							MfInternalFrame_01 frame = (MfInternalFrame_01) internalFrame;
							if (Objects.equals(newValue, frame.getEditor()) && Objects.nonNull(newValue)) {
								try {
									frame.setSelected(true);
								} catch (PropertyVetoException e) {
									e.printStackTrace();
								}
								return;
							}
						}
					}
				} finally {
					adjustFlag = false;
				}

			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireCleared() {
			SwingUtil.invokeInEventQueue(() -> {
				if (checkDuplexingForecast(new Object[] { "fireFocusEditorCleared" })) {
					return;
				}

				if (Objects.isNull(getSelectedFrame()))
					return;

				try {
					getSelectedFrame().setSelected(false);
				} catch (PropertyVetoException e) {
					e.printStackTrace();
				}
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void firePut(Project key, Editor value) {
			SwingUtil.invokeInEventQueue(() -> {
				if (checkDuplexingForecast(new Object[] { DF_FOCUSEDITORMODEL_FIREPUT, key, value })) {
					return;
				}

				Project focusProject = Objects.isNull(focusProjectModel) ? null : focusProjectModel.get();
				if (!Objects.equals(focusProject, key))
					return;

				adjustFlag = true;
				try {
					if (Objects.isNull(value)) {
						if (Objects.nonNull(getSelectedFrame())) {
							try {
								getSelectedFrame().setSelected(false);
							} catch (PropertyVetoException e) {
								e.printStackTrace();
							}
						}
					} else {
						for (JInternalFrame internalFrame : getAllFrames()) {
							MfInternalFrame_01 frame = (MfInternalFrame_01) internalFrame;
							if (Objects.equals(key, frame.getEditor()) && Objects.nonNull(key)) {
								try {
									frame.setSelected(true);
								} catch (PropertyVetoException e) {
									e.printStackTrace();
								}
								return;
							}
						}
					}
				} finally {
					adjustFlag = false;
				}

			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireRemoved(Project key, Editor value) {
			SwingUtil.invokeInEventQueue(() -> {
				if (checkDuplexingForecast(new Object[] { DF_FOCUSEDITORMODEL_FIREREMOVED, key, value })) {
					return;
				}

				Project focusProject = Objects.isNull(focusProjectModel) ? null : focusProjectModel.get();

				if (!Objects.equals(key, focusProject))
					return;
				if (Objects.isNull(getSelectedFrame()))
					return;

				try {
					getSelectedFrame().setSelected(false);
				} catch (PropertyVetoException e) {
					e.printStackTrace();
				}
			});
		}

	};

	private final ReferenceObverser<Project> focusProjectObverser = new ReferenceAdapter<Project>() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireCleared() {
			SwingUtil.invokeInEventQueue(() -> {
				hideAllInternalFrame();
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireSet(Project oldValue, Project newValue) {
			SwingUtil.invokeInEventQueue(() -> {
				if (Objects.equals(oldValue, newValue)) {
					return;
				}

				hideAllInternalFrame();

				if (Objects.isNull(newValue)) {
					return;
				}

				if (Objects.isNull(focusEditorModel)) {
					return;
				}

				Object focusFrame = null;
				focusEditorModel.getLock().readLock().lock();
				try {
					focusFrame = focusEditorModel.get(newValue);
				} finally {
					focusEditorModel.getLock().readLock().unlock();
				}

				for (JInternalFrame internalFrame : getAllFrames()) {
					MfInternalFrame_01 frame = (MfInternalFrame_01) internalFrame;
					if (Objects.equals(newValue, frame.getEditor().getEditProject())) {
						frame.setVisible(true);
					}
					if (Objects.equals(focusFrame, frame) && Objects.nonNull(focusFrame)) {
						try {
							frame.setSelected(true);
						} catch (PropertyVetoException e) {
							e.printStackTrace();
						}
					}
				}
			});
		}

	};

	private final InternalFrameListener internalFrameListener = new InternalFrameAdapter() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void internalFrameActivated(InternalFrameEvent e) {
			if (adjustFlag)
				return;

			if (Objects.isNull(focusEditorModel)) {
				return;
			}

			MfInternalFrame_01 frame = (MfInternalFrame_01) e.getInternalFrame();
			Project focusProject = Objects.isNull(focusProjectModel) ? null : focusProjectModel.get();
			Editor focusEditor = Objects.isNull(frame) ? null : frame.getEditor();

			if (Objects.isNull(frame) || Objects.isNull(focusProject))
				return;

			if (Objects.isNull(focusProject)) {
				duplexingForecast.offer(new Object[] { DF_FOCUSEDITORMODEL_FIREPUT, focusProject, focusEditor });
			} else {
				Editor oldEditor = focusEditorModel.get(focusProject);
				duplexingForecast
						.offer(new Object[] { DF_FOCUSEDITORMODEL_FIRECHANGED, focusProject, oldEditor, focusEditor });
			}

			guiManager.putFocusEditor(focusProject, focusEditor, ExecType.CONCURRENT);

		}

	};

	/** 双工通信预测 */
	private final Queue<Object[]> duplexingForecast = new ArrayDeque<>();

	private boolean adjustFlag = false;

	/**
	 * 新实例。
	 */
	public MfDesktopPane_01() {
		this(null, null, null, null, null);
	}

	/**
	 * 
	 * @param guiManager
	 * @param i18nHandler
	 * @param editorModel
	 * @param focusProjectModel
	 * @param focusEditorModel
	 */
	public MfDesktopPane_01(GuiManager guiManager, I18nHandler i18nHandler,
			SyncMapModel<ProjectFilePair, Editor> editorModel, SyncReferenceModel<Project> focusProjectModel,
			SyncMapModel<Project, Editor> focusEditorModel) {
		super(guiManager, i18nHandler);

		if (Objects.nonNull(editorModel)) {
			editorModel.addObverser(editorObverser);
		}
		if (Objects.nonNull(focusEditorModel)) {
			focusEditorModel.addObverser(focusEditorObverser);
		}
		if (Objects.nonNull(focusProjectModel)) {
			focusProjectModel.addObverser(focusProjectObverser);
		}

		this.editorModel = editorModel;
		this.focusEditorModel = focusEditorModel;
		this.focusProjectModel = focusProjectModel;

		syncEditorModel();
		syncFocusModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		if (Objects.nonNull(this.editorModel)) {
			this.editorModel.removeObverser(editorObverser);
		}
		if (Objects.nonNull(this.focusEditorModel)) {
			this.focusEditorModel.removeObverser(focusEditorObverser);
		}
		if (Objects.nonNull(this.focusProjectModel)) {
			this.focusProjectModel.removeObverser(focusProjectObverser);
		}

		super.dispose();
	}

	/**
	 * 获取面板中的编辑器模型。
	 * 
	 * @return 面板中的编辑器模型。
	 */
	public SyncMapModel<ProjectFilePair, Editor> getEditorModel() {
		return editorModel;
	}

	/**
	 * @return the focusEditorModel
	 */
	public SyncMapModel<Project, Editor> getFocusEditorModel() {
		return focusEditorModel;
	}

	/**
	 * @return the focusProjectModel
	 */
	public SyncReferenceModel<Project> getFocusProjectModel() {
		return focusProjectModel;
	}

	/**
	 * 设置面板中的编辑器模型。
	 * 
	 * @param editorModel
	 *            面板中的编辑器模型。
	 */
	public void setEditorModel(SyncMapModel<ProjectFilePair, Editor> editorModel) {
		if (Objects.nonNull(this.editorModel)) {
			this.editorModel.removeObverser(editorObverser);
		}

		if (Objects.nonNull(editorModel)) {
			editorModel.addObverser(editorObverser);
		}

		this.editorModel = editorModel;
		syncEditorModel();
	}

	/**
	 * @param focusEditorModel
	 *            the focusEditorModel to set
	 */
	public void setFocusEditorModel(SyncMapModel<Project, Editor> focusEditorModel) {
		if (Objects.nonNull(this.focusEditorModel)) {
			this.focusEditorModel.removeObverser(focusEditorObverser);
		}

		if (Objects.nonNull(focusEditorModel)) {
			focusEditorModel.addObverser(focusEditorObverser);
		}

		this.focusEditorModel = focusEditorModel;
		syncFocusModel();
	}

	/**
	 * @param focusProjectModel
	 *            the focusProjectModel to set
	 */
	public void setFocusProjectModel(SyncReferenceModel<Project> focusProjectModel) {
		if (Objects.nonNull(this.focusProjectModel)) {
			this.focusProjectModel.removeObverser(focusProjectObverser);
		}

		if (Objects.nonNull(focusProjectModel)) {
			focusProjectModel.addObverser(focusProjectObverser);
		}

		this.focusProjectModel = focusProjectModel;
		syncFocusModel();
	}

	@Override
	protected void refreshLabels() {
		// TODO Auto-generated method stub

	}

	private boolean checkDuplexingForecast(Object[] objs) {
		if (duplexingForecast.isEmpty()) {
			return false;
		}

		if (Arrays.equals(duplexingForecast.peek(), objs)) {
			duplexingForecast.poll();
			return true;
		} else {
			syncFocusModel();
			duplexingForecast.clear();
			return false;
		}
	}

	/**
	 * 关闭所有的内部窗口。
	 */
	private void disposeAllInternalFrame() {
		for (JInternalFrame internalFrame : getAllFrames()) {
			MfInternalFrame_01 frame = (MfInternalFrame_01) internalFrame;
			frame.removeInternalFrameListener(internalFrameListener);
			frame.dispose();
		}
	}

	/**
	 * 隐藏所有的内部窗口。
	 */
	private void hideAllInternalFrame() {
		for (JInternalFrame frame : getAllFrames()) {
			frame.setVisible(false);
		}
	}

	private void syncEditorModel() {
		disposeAllInternalFrame();

		if (Objects.isNull(editorModel) || Objects.isNull(focusProjectModel)) {
			return;
		}

		this.editorModel.getLock().readLock().lock();
		this.focusProjectModel.getLock().readLock().lock();
		try {
			Project focusProject = Objects.isNull(focusProjectModel) ? null : focusProjectModel.get();

			for (Editor editor : editorModel.values()) {
				MfInternalFrame_01 frame = new MfInternalFrame_01(guiManager, i18nHandler, editor);
				frame.addInternalFrameListener(internalFrameListener);

				if (Objects.equals(focusProject, editor.getEditProject()) && Objects.nonNull(focusProject)) {
					frame.setVisible(true);
				} else {
					frame.setVisible(false);
				}

				add(frame);
				frame.pack();
			}

		} finally {
			this.focusProjectModel.getLock().readLock().unlock();
			this.editorModel.getLock().readLock().unlock();
		}

	}

	private void syncFocusModel() {
		hideAllInternalFrame();

		if (Objects.isNull(focusEditorModel)) {
			return;
		}

		if (Objects.isNull(focusProjectModel)) {
			return;
		}

		this.focusEditorModel.getLock().readLock().lock();
		this.focusProjectModel.getLock().readLock().lock();
		adjustFlag = true;
		try {
			Project focusProject = Objects.isNull(focusProjectModel) ? null : focusProjectModel.get();
			Editor focusEditor = Objects.isNull(focusEditorModel) ? null : focusEditorModel.get(focusProject);

			for (JInternalFrame internalFrame : getAllFrames()) {
				MfInternalFrame_01 frame = (MfInternalFrame_01) internalFrame;
				if (Objects.equals(focusProject, frame.getEditor().getEditProject())) {
					frame.setVisible(true);
					if (Objects.equals(focusEditor, frame.getEditor()) && Objects.nonNull(focusEditor)) {
						try {
							frame.setSelected(true);
						} catch (PropertyVetoException e) {
							e.printStackTrace();
						}
					}
				}
			}

		} finally {
			this.focusProjectModel.getLock().readLock().unlock();
			this.focusEditorModel.getLock().readLock().unlock();
			adjustFlag = false;
		}

	}

}
