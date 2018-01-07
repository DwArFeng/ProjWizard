package com.dwarfeng.projwiz.core.model.cm;

import com.dwarfeng.dutil.basic.threads.ExternalReadWriteThreadSafe;

/**
 * 线程安全的文件选区模型。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface SyncFileSelectionHandler extends FileSelectionHandler, ExternalReadWriteThreadSafe {

}
