package com.dwarfeng.projwiz.core.view.gui;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.dwarfeng.dutil.basic.cna.ArrayUtil;
import com.dwarfeng.dutil.basic.cna.model.SyncKeySetModel;
import com.dwarfeng.dutil.basic.cna.model.SyncReferenceModel;
import com.dwarfeng.dutil.basic.cna.model.SyncSetModel;
import com.dwarfeng.dutil.basic.cna.model.obv.ReferenceAdapter;
import com.dwarfeng.dutil.basic.cna.model.obv.ReferenceObverser;
import com.dwarfeng.dutil.basic.cna.model.obv.SetAdapter;
import com.dwarfeng.dutil.basic.cna.model.obv.SetObverser;
import com.dwarfeng.dutil.basic.gui.awt.CommonIconLib;
import com.dwarfeng.dutil.basic.gui.awt.ImageSize;
import com.dwarfeng.dutil.basic.gui.awt.ImageUtil;
import com.dwarfeng.dutil.basic.gui.swing.JMenuItemAction;
import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.dutil.develop.backgr.AbstractTask;
import com.dwarfeng.dutil.develop.backgr.Task;
import com.dwarfeng.dutil.develop.i18n.I18nHandler;
import com.dwarfeng.projwiz.core.model.cm.Tree.Path;
import com.dwarfeng.projwiz.core.model.eum.ImageKey;
import com.dwarfeng.projwiz.core.model.eum.LabelStringKey;
import com.dwarfeng.projwiz.core.model.obv.ProjectAdapter;
import com.dwarfeng.projwiz.core.model.obv.ProjectObverser;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.FileProcessor;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.ProjectProcessor;
import com.dwarfeng.projwiz.core.util.ModelUtil;
import com.dwarfeng.projwiz.core.util.SwingTreeUtil;
import com.dwarfeng.projwiz.core.view.struct.GuiManager;
import com.dwarfeng.projwiz.core.view.struct.GuiManager.ExecType;

final class MfPanel_02 extends ProjWizPanel {

	private static final long serialVersionUID = 9177857181581910358L;
	private static final String DF_ANCHORFILEMODEL_FIRECLEARED = "A";
	private static final String DF_ANCHORFILEMODEL_FIRECHANGED = "B";
	private static final String DF_FOCUSPROJECTMODEL_FIRECLEARED = "C";
	private static final String DF_FOCUSPROJECTMODEL_FIRECHANGED = "D";
	private static final String DF_FOCUSFILEMODEL_FIREREMOVED = "E";
	private static final String DF_FOCUSFILEMODEL_FIRECLEARED = "F";
	private static final String DF_FOCUSFILEMODEL_FIREADDED = "G";

	private final JTree tree;
	private final JPopupMenu popup;
	private final JMenuItem mi_01;
	private final JMenuItem mi_02;
	private final JMenuItem mi_03;
	private final JMenuItem mi_04;
	private final JMenuItem mi_05;
	private final JMenuItem mi_06;
	private final JMenuItem mi_07;
	private final JMenuItem mi_08;

	// private SyncKeySetModel<String, ProjectProcessor> projectProcessorModel;
	private SyncKeySetModel<String, FileProcessor> fileProcessorModel;
	private SyncReferenceModel<File> anchorFileModel;
	private SyncReferenceModel<Project> focusProjectModel;
	private SyncSetModel<File> focusFileModel;
	private Project project;

	// 将文件与名称存放在映射中，避免在Renderer中由于使用 File.getName()导致同步锁死锁。
	private final Map<File, String> fileNameMap = new HashMap<>();
	private final DefaultTreeModel treeModel = new DefaultTreeModel(null);

	private final TreeCellRenderer treeRenderer = new DefaultTreeCellRenderer() {

		private static final long serialVersionUID = -6630645225494609239L;

		@Override
		public java.awt.Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
				boolean leaf, int row, boolean hasFocus) {
			// 如果调用value，则该方法会调用value.toString()，会导致已知的死锁。
			super.getTreeCellRendererComponent(tree, "value as file", sel, expanded, leaf, row, hasFocus);
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			File file = (File) node.getUserObject();
			if (Objects.isNull(file))
				return this;
			// 用注释中的文本会使文件上锁，进而导致已知的死锁。
			// setText(file.getName());
			setText(fileNameMap.get(file));
			Image image = null;
			if (Objects.nonNull(fileProcessorModel)) {
				FileProcessor processor = fileProcessorModel.get(file.getRegisterKey());
				if (Objects.nonNull(processor)) {
					image = processor.getFileIcon(ModelUtil.unmodifiableFile(file));
				}
			}
			if (Objects.isNull(image)) {
				setIcon(new ImageIcon(ImageUtil.getInternalImage(CommonIconLib.UNKNOWN_BLUE, ImageSize.ICON_SMALL)));
			} else {
				setIcon(new ImageIcon(ImageUtil.scaleImage(image, ImageSize.ICON_SMALL)));
			}
			return this;
		};
	};

	private final ReferenceObverser<File> anchorFileObverser = new ReferenceAdapter<File>() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireCleared() {
			SwingUtil.invokeInEventQueue(() -> {
				if (checkDuplexingForecast(new Object[] { DF_ANCHORFILEMODEL_FIRECLEARED })) {
					return;
				}

				adjustFlag = true;
				try {
					// NOT COMPLETED YET

				} finally {
					adjustFlag = false;
				}

			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireSet(File oldValue, File newValue) {
			SwingUtil.invokeInEventQueue(() -> {
				if (checkDuplexingForecast(new Object[] { DF_ANCHORFILEMODEL_FIRECHANGED, oldValue, newValue })) {
					return;
				}

				if (Objects.equals(oldValue, newValue)) {
					return;
				}

				adjustFlag = true;
				try {
					// if (Objects.isNull(newFile)) {
					// tree.setAnchorSelectionPath(null);
					// } else {
					// tree.setAnchorSelectionPath(SwingTreeUtil.findTreePath(project.getFileTree().getPath(newFile),
					// (DefaultMutableTreeNode) treeModel.getRoot()));
					// }
					//
					// openedAndFocusModel.getLock().readLock().lock();
					// try {
					// for (File focusFile :
					// openedAndFocusModel.getFocusFiles()) {
					// tree.getSelectionModel()
					// .addSelectionPath(SwingTreeUtil.findTreePath(
					// openedAndFocusModel.getFocusProject().getFileTree().getPath(focusFile),
					// (DefaultMutableTreeNode) treeModel.getRoot()));
					// }
					// } finally {
					// openedAndFocusModel.getLock().readLock().unlock();
					// }
				} finally {
					adjustFlag = false;
				}

			});
		}

	};
	private final SetObverser<File> focusFileObverser = new SetAdapter<File>() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireAdded(File element) {
			SwingUtil.invokeInEventQueue(() -> {
				fireFocusFileAdded_Tree(element);
				fireFocusFileAdded_Popup(element);
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireCleared() {
			SwingUtil.invokeInEventQueue(() -> {
				fireFocusFileCleared_Tree();
				fireFocusFileCleared_Popup();
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireRemoved(File element) {
			SwingUtil.invokeInEventQueue(() -> {
				fireFocusFileRemoved_Tree(element);
				fireFocusFileRemoved_Popup(element);
			});
		}

		private void fireFocusFileAdded_Popup(File file) {
			focusFileFlag = true;
			checkPopup();
		}

		private void fireFocusFileAdded_Tree(File file) {
			if (checkDuplexingForecast(new Object[] { DF_FOCUSFILEMODEL_FIREADDED, file })) {
				return;
			}

			adjustFlag = true;
			try {
				tree.getSelectionModel().addSelectionPath(SwingTreeUtil.findTreePath(
						project.getFileTree().getPath(file), (DefaultMutableTreeNode) treeModel.getRoot()));
			} finally {
				adjustFlag = false;
			}
		}

		private void fireFocusFileCleared_Popup() {
			focusFileFlag = false;
			checkPopup();
		}

		private void fireFocusFileCleared_Tree() {
			if (checkDuplexingForecast(new Object[] { DF_FOCUSFILEMODEL_FIRECLEARED })) {
				return;
			}

			adjustFlag = true;
			try {
				tree.getSelectionModel().clearSelection();
			} finally {
				adjustFlag = false;
			}
		}

		private void fireFocusFileRemoved_Popup(File file) {
			if (tree.getSelectionModel().getSelectionPaths().length == 0) {
				focusFileFlag = false;
				checkPopup();
			}
		}

		private void fireFocusFileRemoved_Tree(File file) {
			if (checkDuplexingForecast(new Object[] { DF_FOCUSFILEMODEL_FIREREMOVED, file })) {
				return;
			}

			adjustFlag = true;
			try {
				tree.getSelectionModel().removeSelectionPath(SwingTreeUtil.findTreePath(
						project.getFileTree().getPath(file), (DefaultMutableTreeNode) treeModel.getRoot()));
			} finally {
				adjustFlag = false;
			}
		}

	};
	private final ReferenceObverser<Project> focusProjectObverser = new ReferenceAdapter<Project>() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireCleared() {
			final AtomicReference<TreeNode> rootRef = new AtomicReference<TreeNode>(null);
			final AtomicBoolean returnFlagRef = new AtomicBoolean(false);

			final Task task = new AbstractTask() {

				@Override
				protected void todo() throws Exception {
					if (checkDuplexingForecast(new Object[] { DF_FOCUSPROJECTMODEL_FIRECLEARED })) {
						returnFlagRef.set(true);
						return;
					}

					if (Objects.nonNull(project)) {
						project.removeObverser(projectObverser);
					}

					fileNameMap.clear();

					project = null;
					rootRef.set(null);
				}
			};

			guiManager.submitTask(task, ExecType.CONCURRENT);
			try {
				task.awaitFinish();
			} catch (InterruptedException ignore) {
				// 抛异常也要遵守基本法
			}

			SwingUtil.invokeInEventQueue(() -> {
				fireFocusProjectChanged_Tree(null, returnFlagRef.get(), rootRef.get());
				fireFocusProjectChanged_Popup(null);
			});
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireSet(Project oldValue, Project newValue) {
			final AtomicReference<TreeNode> rootRef = new AtomicReference<TreeNode>(null);
			final AtomicBoolean returnFlagRef = new AtomicBoolean(false);

			final Task task = new AbstractTask() {

				@Override
				protected void todo() throws Exception {
					if (checkDuplexingForecast(new Object[] { DF_FOCUSPROJECTMODEL_FIRECHANGED, oldValue, newValue })) {
						returnFlagRef.set(true);
						return;
					}

					if (!Objects.equals(newValue, project)) {
						if (Objects.nonNull(project)) {
							project.removeObverser(projectObverser);
						}

						fileNameMap.clear();

						project = newValue;

						if (Objects.nonNull(project)) {
							project.getLock().writeLock().lock();
							try {
								project.addObverser(projectObverser);
								project.getFileTree().forEach(file -> {
									fileNameMap.put(file, file.getName());
								});
								rootRef.set(SwingTreeUtil.newTreeNodeFromTree(project.getFileTree()));
							} finally {
								project.getLock().writeLock().unlock();
							}
						} else {
							rootRef.set(null);
						}

					}

				}
			};

			guiManager.submitTask(task, ExecType.CONCURRENT);
			try {
				task.awaitFinish();
			} catch (InterruptedException ignore) {
				// 抛异常也要遵守基本法
			}

			SwingUtil.invokeInEventQueue(() -> {
				fireFocusProjectChanged_Tree(newValue, returnFlagRef.get(), rootRef.get());
				fireFocusProjectChanged_Popup(newValue);
			});
		}

		private void fireFocusProjectChanged_Popup(Project newProject) {
			focusProjectFlag = Objects.nonNull(newProject);
			checkPopup();
		}

		private void fireFocusProjectChanged_Tree(Project newProject, boolean returnFlag, TreeNode root) {
			if (returnFlag) {
				return;
			}

			adjustFlag = true;
			try {
				treeModel.setRoot(root);
			} finally {
				adjustFlag = false;
			}
		}

	};

	private final ProjectObverser projectObverser = new ProjectAdapter() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireFileAddedByCopy(Path<File> path, File parent, File file) {
			fireAdded(path, parent, file);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireFileAddedByMove(Path<File> path, File parent, File file) {
			fireAdded(path, parent, file);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireFileAddedByNew(Path<File> path, File parent, File file) {
			fireAdded(path, parent, file);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireFileRemovedByDelete(Path<File> path, File parent, File file) {
			fireRemoved(path, parent, file);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireFileRemovedByMove(Path<File> path, File parent, File file) {
			fireRemoved(path, parent, file);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireFileRenamed(Path<File> path, File file, String oldName, String newName) {
			SwingUtil.invokeInEventQueue(() -> {
				adjustFlag = true;
				try {
					fileNameMap.put(file, newName);

					TreePath parentTreePath = SwingTreeUtil.findTreePath(path,
							(DefaultMutableTreeNode) treeModel.getRoot());

					TreePath[] selectedPaths = tree.getSelectionModel().getSelectionPaths();
					TreePath anchorPath = tree.getAnchorSelectionPath();

					DefaultMutableTreeNode node = (DefaultMutableTreeNode) parentTreePath.getLastPathComponent();
					DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();

					parentNode.remove(node);
					SwingTreeUtil.insertTreeNodeByOrder(parentNode, node);

					treeModel.reload(parentNode);

					tree.getSelectionModel().setSelectionPaths(selectedPaths);
					tree.setAnchorSelectionPath(anchorPath);
				} finally {
					adjustFlag = false;
				}

			});
		}

		private void fireAdded(Path<File> path, File parent, File file) {
			SwingUtil.invokeInEventQueue(() -> {
				adjustFlag = true;
				try {
					fileNameMap.put(file, file.getName());

					TreePath parentTreePath = SwingTreeUtil.findTreePath(project.getFileTree().getPath(parent),
							(DefaultMutableTreeNode) treeModel.getRoot());

					DefaultMutableTreeNode node = (DefaultMutableTreeNode) parentTreePath.getLastPathComponent();
					SwingTreeUtil.insertTreeNodeByOrder(node, new DefaultMutableTreeNode(file));

					treeModel.reload(node);
				} finally {
					adjustFlag = false;
				}

			});
		}

		private void fireRemoved(Path<File> path, File parent, File file) {
			SwingUtil.invokeInEventQueue(() -> {
				adjustFlag = true;
				try {
					fileNameMap.remove(file);

					TreePath parentTreePath = SwingTreeUtil.findTreePath(project.getFileTree().getPath(parent),
							(DefaultMutableTreeNode) treeModel.getRoot());

					DefaultMutableTreeNode node = (DefaultMutableTreeNode) parentTreePath.getLastPathComponent();
					for (int i = 0; i < node.getChildCount(); i++) {
						if (Objects.equals(((DefaultMutableTreeNode) node.getChildAt(i)).getUserObject(), file)) {
							node.remove(i);
							break;
						}
					}

					treeModel.reload(node);
				} finally {
					adjustFlag = false;
				}

			});
		}

	};

	private final MouseListener popupMouseListener = new MouseAdapter() {

		// boolean mask = false;
		//
		// @Override
		// public void mousePressed(MouseEvent e) {
		// mask = true;
		// }
		//
		// @Override
		// public void mouseReleased(MouseEvent e) {
		// if (e.isPopupTrigger() && mask && focusProjectFlag) {
		// showMenu(e);
		// }
		// mask = false;
		// }

		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.isPopupTrigger() && focusProjectFlag) {
				showMenu(e);
			}
		}

		private void showMenu(MouseEvent e) {
			popup.show(e.getComponent(), e.getX(), e.getY());
		}
	};
	/** 双工通信预测 */
	private final Queue<Object[]> duplexingForecast = new ArrayDeque<>();

	private boolean adjustFlag = false;

	private boolean focusProjectFlag = false;
	private boolean focusFileFlag = false;

	/**
	 * 新实例。
	 */
	public MfPanel_02() {
		this(null, null, null, null, null, null, null);
	}

	/**
	 * 
	 * @param guiManager
	 * @param i18nHandler
	 * @param anchorFileModel
	 * @param focusProjectModel
	 * @param focusFileModel
	 * @param projectProcessorModel
	 * @param fileProcessorModel
	 */
	public MfPanel_02(GuiManager guiManager, I18nHandler i18nHandler, SyncReferenceModel<File> anchorFileModel,
			SyncReferenceModel<Project> focusProjectModel, SyncSetModel<File> focusFileModel,
			SyncKeySetModel<String, ProjectProcessor> projectProcessorModel,
			SyncKeySetModel<String, FileProcessor> fileProcessorModel) {
		super(guiManager, i18nHandler);

		setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);

		tree = new JTree();
		tree.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					if (Objects.isNull(MfPanel_02.this.anchorFileModel))
						return;
					if (Objects.isNull(MfPanel_02.this.anchorFileModel.get()))
						return;
					if (Objects.isNull(project))
						return;

					MfPanel_02.this.guiManager.openAnchorFile(ExecType.CONCURRENT);
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					TreePath path = tree.getClosestPathForLocation(e.getX(), e.getY());
					if (Objects.nonNull(path) && !tree.getPathBounds(path).contains(e.getX(), e.getY())) {
						path = null;
					}

					if (Objects.isNull(path)) {
						if (tree.getSelectionModel().getSelectionPaths().length > 0) {
							tree.getSelectionModel().setSelectionPath(null);
						}
						return;
					}

					if (!ArrayUtil.contains(tree.getSelectionModel().getSelectionPaths(), path)) {
						tree.getSelectionModel().setSelectionPath(path);
					}
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					TreePath path = tree.getClosestPathForLocation(e.getX(), e.getY());
					if (Objects.nonNull(path) && !tree.getPathBounds(path).contains(e.getX(), e.getY())) {
						path = null;
					}

					if (Objects.isNull(path)) {
						if (tree.getSelectionModel().getSelectionPaths().length > 0) {
							tree.getSelectionModel().setSelectionPath(null);
						}
						return;
					}

					if (!ArrayUtil.contains(tree.getSelectionModel().getSelectionPaths(), path)) {
						tree.getSelectionModel().setSelectionPath(path);
					}
				}
			}

		});
		tree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				if (Objects.isNull(MfPanel_02.this.anchorFileModel))
					return;

				if (adjustFlag)
					return;

				File newAnchorFile = Objects.isNull(e.getNewLeadSelectionPath()) ? null
						: getFileFromPath(e.getNewLeadSelectionPath());
				File oldAnchorFile = MfPanel_02.this.anchorFileModel.get();
				duplexingForecast.offer(new Object[] { DF_ANCHORFILEMODEL_FIRECHANGED, oldAnchorFile, newAnchorFile });

				MfPanel_02.this.guiManager.setAnchorFile(newAnchorFile, ExecType.FIFO);

				for (TreePath treePath : e.getPaths()) {
					if (e.isAddedPath(treePath)) {
						File focusFile2Add = getFileFromPath(treePath);
						duplexingForecast.offer(new Object[] { DF_FOCUSFILEMODEL_FIREADDED, focusFile2Add });
						MfPanel_02.this.guiManager.addFocusFile(focusFile2Add, ExecType.FIFO);
					} else {
						File focusFile2Remove = getFileFromPath(treePath);
						duplexingForecast.offer(new Object[] { DF_FOCUSFILEMODEL_FIREREMOVED, focusFile2Remove });
						MfPanel_02.this.guiManager.removeFocusFile(focusFile2Remove, ExecType.FIFO);
					}
				}

			}
		});
		tree.setRootVisible(false);
		tree.setModel(treeModel);
		tree.setCellRenderer(treeRenderer);

		tree.registerKeyboardAction(e -> {
			tree.getSelectionModel().clearSelection();

		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JTree.WHEN_FOCUSED);
		tree.registerKeyboardAction(e -> {
			if (Objects.isNull(this.anchorFileModel))
				return;
			if (Objects.isNull(this.anchorFileModel.get()))
				return;
			MfPanel_02.this.guiManager.renameAnchorFile(ExecType.CONCURRENT);
		}, KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), JTree.WHEN_FOCUSED);
		tree.registerKeyboardAction(e -> {
			if (Objects.isNull(this.focusFileModel))
				return;
			if (this.focusFileModel.size() == 0)
				return;
			MfPanel_02.this.guiManager.openFocusFile(ExecType.CONCURRENT);
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JTree.WHEN_FOCUSED);

		scrollPane.setViewportView(tree);

		popup = new JPopupMenu();
		mi_01 = popup.add(new JMenuItemAction.Builder().name(label(LabelStringKey.MFPANEL_2_1)).mnemonic('N')
				.keyStorke(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK))
				.icon(new ImageIcon(ImageUtil.getInternalImage(ImageKey.NEW_FILE, ImageSize.ICON_SMALL)))
				.listener(e -> {
					MfPanel_02.this.guiManager.newFile(ExecType.CONCURRENT);
				}).build());
		popup.addSeparator();

		mi_02 = popup.add(new JMenuItemAction.Builder().name(label(LabelStringKey.MFPANEL_2_2)).mnemonic('O')
				.keyStorke(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK))
				.icon(new ImageIcon(ImageUtil.getInternalImage(ImageKey.OPEN_FILE, ImageSize.ICON_SMALL)))
				.listener(e -> {
					MfPanel_02.this.guiManager.openFocusFile(ExecType.CONCURRENT);
				}).build());
		mi_03 = popup.add(new JMenuItemAction.Builder().name(label(LabelStringKey.MFPANEL_2_3)).mnemonic('H')
				.keyStorke(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK))
				.icon(new ImageIcon(ImageUtil.getInternalImage(ImageKey.OPEN_FILE, ImageSize.ICON_SMALL))).build());
		popup.addSeparator();

		mi_04 = popup.add(new JMenuItemAction.Builder().name(label(LabelStringKey.MFPANEL_2_4)).mnemonic('C')
				.keyStorke(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK))
				.icon(new ImageIcon(ImageUtil.getInternalImage(ImageKey.COPY_FILE, ImageSize.ICON_SMALL))).build());
		mi_05 = popup.add(new JMenuItemAction.Builder().name(label(LabelStringKey.MFPANEL_2_5)).mnemonic('V')
				.keyStorke(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.ALT_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK))
				.build());
		mi_06 = popup.add(new JMenuItemAction.Builder().name(label(LabelStringKey.MFPANEL_2_6)).mnemonic('D')
				.keyStorke(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0))
				.icon(new ImageIcon(ImageUtil.getInternalImage(ImageKey.DELETE_FILE, ImageSize.ICON_SMALL)))
				.listener(e -> {
					MfPanel_02.this.guiManager.deleteFocusFile(ExecType.CONCURRENT);
				}).build());
		mi_07 = popup.add(new JMenuItemAction.Builder().name(label(LabelStringKey.MFPANEL_2_7)).mnemonic('D')
				.keyStorke(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0))
				.icon(new ImageIcon(ImageUtil.getInternalImage(ImageKey.DELETE_FILE, ImageSize.ICON_SMALL)))
				.listener(e -> {
					MfPanel_02.this.guiManager.deleteFocusFile(ExecType.CONCURRENT);
				}).build());
		popup.addSeparator();

		mi_08 = popup.add(new JMenuItemAction.Builder().name(label(LabelStringKey.MFPANEL_2_8)).mnemonic('P')
				.keyStorke(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.ALT_DOWN_MASK)).listener(e -> {
					MfPanel_02.this.guiManager.showAnchorFilePropertiesDialog(ExecType.CONCURRENT);
				}).build());
		tree.addMouseListener(popupMouseListener);

		checkPopup();

		if (Objects.nonNull(anchorFileModel)) {
			anchorFileModel.addObverser(anchorFileObverser);
		}
		if (Objects.nonNull(focusProjectModel)) {
			focusProjectModel.addObverser(focusProjectObverser);
		}
		if (Objects.nonNull(focusFileModel)) {
			focusFileModel.addObverser(focusFileObverser);
		}

		// this.projectProcessorModel = projectProcessorModel;
		this.fileProcessorModel = fileProcessorModel;
		this.anchorFileModel = anchorFileModel;
		this.focusProjectModel = focusProjectModel;
		this.focusFileModel = focusFileModel;

		syncModel();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		super.dispose();

		adjustFlag = true;
		try {
			if (Objects.nonNull(this.anchorFileModel)) {
				this.anchorFileModel.removeObverser(anchorFileObverser);
			}
			if (Objects.nonNull(this.focusProjectModel)) {
				this.focusProjectModel.removeObverser(focusProjectObverser);
			}
			if (Objects.nonNull(this.focusFileModel)) {
				this.focusFileModel.removeObverser(focusFileObverser);
			}

			if (Objects.nonNull(this.project)) {
				project.removeObverser(projectObverser);
			}

			treeModel.setRoot(null);
			tree.getSelectionModel().clearSelection();
			fileNameMap.clear();
		} finally {
			adjustFlag = false;
		}
	}

	/**
	 * @return the anchorFileModel
	 */
	public SyncReferenceModel<File> getAnchorFileModel() {
		return anchorFileModel;
	}

	/**
	 * @return the fileProcessorModel
	 */
	public SyncKeySetModel<String, FileProcessor> getFileProcessorModel() {
		return fileProcessorModel;
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
	 * @param anchorFileModel
	 *            the anchorFileModel to set
	 */
	public void setAnchorFileModel(SyncReferenceModel<File> anchorFileModel) {
		if (Objects.nonNull(this.anchorFileModel)) {
			this.anchorFileModel.removeObverser(anchorFileObverser);
		}

		if (Objects.nonNull(anchorFileModel)) {
			anchorFileModel.addObverser(anchorFileObverser);
		}

		this.anchorFileModel = anchorFileModel;
		syncModel();
	}

	/**
	 * @param fileProcessorModel
	 *            the fileProcessorModel to set
	 */
	public void setFileProcessorModel(SyncKeySetModel<String, FileProcessor> fileProcessorModel) {
		this.fileProcessorModel = fileProcessorModel;
		adjustFlag = true;
		try {
			tree.repaint();
		} finally {
			adjustFlag = false;
		}
	}

	/**
	 * @param focusFileModel
	 *            the focusFileModel to set
	 */
	public void setFocusFileModel(SyncSetModel<File> focusFileModel) {
		if (Objects.nonNull(this.focusFileModel)) {
			this.focusFileModel.removeObverser(focusFileObverser);
		}

		if (Objects.nonNull(focusFileModel)) {
			focusFileModel.addObverser(focusFileObverser);
		}

		this.focusFileModel = focusFileModel;
		syncModel();
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
		syncModel();
	}

	// /**
	// * @param projectProcessorModel
	// * the projectProcessorModel to set
	// */
	// public void setProjectProcessorModel(SyncKeySetModel<String,
	// ProjectProcessor> projectProcessorModel) {
	// this.projectProcessorModel = projectProcessorModel;
	// tree.repaint();
	// }

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void refreshLabels() {
		mi_01.setText(label(LabelStringKey.MFPANEL_2_1));
		mi_02.setText(label(LabelStringKey.MFPANEL_2_2));
		mi_03.setText(label(LabelStringKey.MFPANEL_2_3));
		mi_04.setText(label(LabelStringKey.MFPANEL_2_4));
		mi_05.setText(label(LabelStringKey.MFPANEL_2_5));
		mi_06.setText(label(LabelStringKey.MFPANEL_2_6));
		mi_07.setText(label(LabelStringKey.MFPANEL_2_7));

	}

	private boolean checkDuplexingForecast(Object[] objs) {
		if (duplexingForecast.isEmpty()) {
			return false;
		}

		if (Arrays.equals(duplexingForecast.peek(), objs)) {
			duplexingForecast.poll();
			return true;
		} else {
			syncModel();
			duplexingForecast.clear();
			return false;
		}
	}

	private void checkPopup() {
		mi_01.setEnabled(focusProjectFlag);
		mi_02.setEnabled(focusProjectFlag && focusFileFlag);
		mi_03.setEnabled(focusProjectFlag && focusFileFlag);
		mi_04.setEnabled(focusProjectFlag && focusFileFlag);
		mi_05.setEnabled(focusProjectFlag && focusFileFlag);
		mi_06.setEnabled(focusProjectFlag && focusFileFlag);
		mi_07.setEnabled(focusProjectFlag && focusFileFlag);
		mi_08.setEnabled(focusProjectFlag && focusFileFlag);
	}

	private File getFileFromPath(TreePath treePath) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
		return (File) node.getUserObject();
	}

	private void syncModel() {
		treeModel.setRoot(null);
		tree.getSelectionModel().clearSelection();
		tree.setAnchorSelectionPath(null);
		fileNameMap.clear();

		if (Objects.isNull(anchorFileModel)) {
			return;
		}

		if (Objects.isNull(focusProjectModel)) {
			return;
		}

		if (Objects.isNull(focusFileModel)) {
			return;
		}

		this.anchorFileModel.getLock().readLock().lock();
		this.focusProjectModel.getLock().readLock().lock();
		this.focusFileModel.getLock().readLock().lock();
		adjustFlag = true;
		try {
			if (Objects.nonNull(this.project)) {
				this.project.removeObverser(projectObverser);
			}

			this.project = focusProjectModel.get();

			if (Objects.nonNull(project)) {

				this.project.addObverser(projectObverser);
				this.project.getLock().readLock().lock();

				try {
					project.getFileTree().forEach(file -> {
						fileNameMap.put(file, file.getName());
					});
					treeModel.setRoot(SwingTreeUtil.newTreeNodeFromTree(this.project.getFileTree()));
				} finally {
					this.project.getLock().readLock().unlock();
				}

				for (File focusFile : focusFileModel) {
					tree.getSelectionModel().addSelectionPath(SwingTreeUtil.findTreePath(
							project.getFileTree().getPath(focusFile), (DefaultMutableTreeNode) treeModel.getRoot()));
				}
			}

		} finally {
			this.focusFileModel.getLock().readLock().unlock();
			this.focusProjectModel.getLock().readLock().unlock();
			this.anchorFileModel.getLock().readLock().unlock();
			adjustFlag = false;
		}

	}
}
