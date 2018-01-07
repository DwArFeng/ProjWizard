package com.dwarfeng.projwiz.core.model.cm;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import com.dwarfeng.dutil.basic.cna.model.MapKeySetModel;
import com.dwarfeng.dutil.basic.cna.model.obv.SetAdapter;
import com.dwarfeng.dutil.basic.cna.model.obv.SetObverser;
import com.dwarfeng.dutil.basic.gui.swing.SwingUtil;
import com.dwarfeng.projwiz.core.view.struct.WindowSuppiler;

/**
 * 默认的外部窗口模型。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public final class MapExternalWindowModel extends MapKeySetModel<String, WindowSuppiler> {

	private final class WindowInspector extends WindowAdapter {

		private final WindowSuppiler source;

		public WindowInspector(WindowSuppiler source) {
			this.source = source;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void windowClosed(WindowEvent e) {
			if (contains(source)) {
				remove(source);
			}
		}

	}

	private final Map<WindowSuppiler, WindowInspector> windowInspectorRefs = new HashMap<>();
	private final SetObverser<WindowSuppiler> externalWindowObverser = new SetAdapter<WindowSuppiler>() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireAdded(WindowSuppiler element) {
			WindowInspector inspector = new WindowInspector(element);
			windowInspectorRefs.put(element, inspector);
			element.getWindow().addWindowListener(inspector);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireRemoved(WindowSuppiler element) {
			if (!element.isDispose()) {
				SwingUtil.invokeInEventQueue(() -> {
					element.getWindow().dispose();
				});
			}
			windowInspectorRefs.remove(element);
		}

	};

	/**
	 * 新实例。
	 */
	public MapExternalWindowModel() {
		this(new HashMap<>(), Collections.newSetFromMap(new WeakHashMap<>()));
	}

	/**
	 * 生成一个拥有指定的映射，指定的侦听器集合的映射键值集合模型。
	 * 
	 * @param map
	 *            指定的映射。
	 * @param obversers
	 *            指定的侦听器集合。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public MapExternalWindowModel(Map<String, WindowSuppiler> map, Set<SetObverser<WindowSuppiler>> obversers) {
		super(map, obversers);
		addObverser(externalWindowObverser);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		for (Iterator<WindowSuppiler> it = iterator(); it.hasNext();) {
			it.next();
			it.remove();
		}
	}

}
