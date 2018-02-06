package com.dwarfeng.projwiz.raefrm;

import java.awt.Image;

import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.dutil.develop.cfg.SyncExconfigModel;
import com.dwarfeng.dutil.develop.i18n.SyncI18nHandler;
import com.dwarfeng.projwiz.core.model.eum.IconVariability;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.MetaDataStorage;
import com.dwarfeng.projwiz.core.model.struct.Project;
import com.dwarfeng.projwiz.core.model.struct.ProjectProcessor;
import com.dwarfeng.projwiz.core.model.struct.PropSuppiler;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;
import com.dwarfeng.projwiz.raefrm.model.cm.SyncPermDemandModel;
import com.dwarfeng.projwiz.raefrm.model.struct.ProjProcToolkit;

/**
 * Rae框架工程处理器。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public abstract class RaeProjectProcessor extends RaeComponent implements ProjectProcessor {

	/**
	 * 工程处理器工具包的内部实现。
	 * 
	 * @author DwArFeng
	 * @since 0.0.3-alpha
	 */
	protected final class ProjProcToolkitImpl implements ProjProcToolkit {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getKey() {
			return key;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Toolkit getToolkit() {
			return toolkitRef.get();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ConstantsProvider getConstantsProvider() {
			return constantsProvider;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SyncI18nHandler getLoggerI18nHandler() {
			return loggerI18nHandler;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SyncI18nHandler getLabelI18nHandler() {
			return labelI18nHandler;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SyncExconfigModel getConfigModel() {
			return configModel;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SyncPermDemandModel getPermDemandModel() {
			return permDemandModel;
		}

	}

	protected RaeProjectProcessor(String key, ReferenceModel<? extends Toolkit> toolkitRef,
			MetaDataStorage metaDataStorage, ConstantsProvider constantsProvider) throws ProcessException {
		super(key, toolkitRef, metaDataStorage, constantsProvider);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PropSuppiler getFilePropSuppiler(File file) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Image getProjectIcon(Project project) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IconVariability getProjectIconFixType(Project project) {
		return IconVariability.FIX;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PropSuppiler getProjectPropSuppiler(Project project) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Image getProjectThumb(Project project) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IconVariability getProjectThumbFixType(Project project) {
		return IconVariability.FIX;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNewProjectSupported() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Project newProject() throws ProcessException {
		throw new UnsupportedOperationException("newProject");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOpenProjectSupported() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Project openProject() throws ProcessException {
		throw new UnsupportedOperationException("openProject");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSaveProjectSupported() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Project saveProject(Project project) throws ProcessException {
		throw new UnsupportedOperationException("saveProject");
	}

}
