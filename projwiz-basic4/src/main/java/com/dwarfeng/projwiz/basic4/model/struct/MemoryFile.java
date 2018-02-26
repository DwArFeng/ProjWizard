package com.dwarfeng.projwiz.basic4.model.struct;

import java.util.Set;

import com.dwarfeng.dutil.basic.str.Name;
import com.dwarfeng.projwiz.core.model.obv.FileObverser;
import com.dwarfeng.projwiz.core.model.struct.FileProcessor;
import com.dwarfeng.projwiz.raefrm.RaeFile;
import com.dwarfeng.projwiz.raefrm.model.struct.ProjProcToolkit;

/**
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public class MemoryFile extends RaeFile {

	public MemoryFile(boolean isFolder, ProjProcToolkit projprocToolkit, Name fileType,
			Class<? extends FileProcessor> processorClass, long accessTime, long createTime, long modifyTime,
			Set<FileObverser> obversers) {
		super(isFolder, projprocToolkit, fileType, processorClass, accessTime, createTime, modifyTime, obversers);
		// TODO Auto-generated constructor stub
	}

}
