package com.dwarfeng.projwiz.core.control;

import com.dwarfeng.dutil.basic.cna.model.ReferenceModel;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.projwiz.core.model.struct.MetaDataStorage;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;

public class TestComonent1 extends TestComponent {

	/**
	 * 
	 * @param key
	 * @param toolkitRef
	 * @param metaDataStorage
	 * @return
	 * @throws ProcessException
	 */
	public static TestComonent1 newInstance(ReferenceModel<Toolkit> toolkitRef, MetaDataStorage metaDataStorage)
			throws ProcessException {
		return new TestComonent1(toolkitRef, metaDataStorage);
	}

	protected TestComonent1(ReferenceModel<? extends Toolkit> toolkitRef, MetaDataStorage metaDataStorage) {
		super(toolkitRef, metaDataStorage);
		// TODO Auto-generated constructor stub
	}

}