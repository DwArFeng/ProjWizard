package com.dwarfeng.projwiz.basic4.impl;

import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.projwiz.basic4.model.struct.FofpConstantsProvider;
import com.dwarfeng.projwiz.core.model.struct.MetaDataStorage;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;
import com.dwarfeng.projwiz.raefrm.RaeFileProcessor;

/**
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public class FolderFileProcessor extends RaeFileProcessor {

	/**
	 * 
	 * @param key
	 * @param toolkitRef
	 * @param metaDataStorage
	 * @return
	 * @throws ProcessException
	 */
	public static FolderFileProcessor newInstance(ReferenceModel<Toolkit> toolkitRef, MetaDataStorage metaDataStorage)
			throws ProcessException {
		return new FolderFileProcessor(toolkitRef, metaDataStorage);
	}

	/**
	 * 新实例。
	 * 
	 * @param toolkitRef
	 *            指定的工具包引用。
	 * @param metaDataStorage
	 *            指定的元数据仓库。
	 * @throws ProcessException
	 *             过程异常。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	protected FolderFileProcessor(ReferenceModel<? extends Toolkit> toolkitRef, MetaDataStorage metaDataStorage)
			throws ProcessException {
		super(toolkitRef, metaDataStorage, new FofpConstantsProvider());
		// TODO Auto-generated constructor stub
	}

}
