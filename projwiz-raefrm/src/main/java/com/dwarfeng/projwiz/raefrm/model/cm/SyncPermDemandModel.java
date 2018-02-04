package com.dwarfeng.projwiz.raefrm.model.cm;

import com.dwarfeng.dutil.basic.threads.ExternalReadWriteThreadSafe;

/**
 * 线程安全的权限需求模型。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public interface SyncPermDemandModel extends PermDemandModel, ExternalReadWriteThreadSafe {

}
