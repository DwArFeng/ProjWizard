package com.dwarfeng.projwiz.core.control;

import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.projwiz.core.model.struct.MetaDataStorage;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;

public class TestModule3 extends TestModule {

	/**
	 * 
	 * @param key
	 * @param toolkitRef
	 * @param metaDataStorage
	 * @return
	 * @throws ProcessException
	 */
	public static TestModule3 newInstance(ReferenceModel<Toolkit> toolkitRef, MetaDataStorage metaDataStorage)
			throws ProcessException {
		return new TestModule3(toolkitRef, metaDataStorage);
	}

	protected TestModule3(ReferenceModel<? extends Toolkit> toolkitRef, MetaDataStorage metaDataStorage) {
		super(toolkitRef, metaDataStorage);
		// TODO Auto-generated constructor stub
	}

}
