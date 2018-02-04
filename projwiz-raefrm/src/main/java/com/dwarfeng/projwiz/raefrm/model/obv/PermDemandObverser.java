package com.dwarfeng.projwiz.raefrm.model.obv;

import java.util.Collection;

import com.dwarfeng.dutil.basic.cna.model.obv.MapObverser;
import com.dwarfeng.projwiz.core.model.struct.Toolkit;

/**
 * 权限需求观察器。
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public interface PermDemandObverser extends MapObverser<String, Collection<Toolkit.Method>> {

}
