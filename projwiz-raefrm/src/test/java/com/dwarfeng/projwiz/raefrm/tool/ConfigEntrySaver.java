package com.dwarfeng.projwiz.raefrm.tool;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;

import com.dwarfeng.dutil.basic.io.CT;
import com.dwarfeng.dutil.basic.io.FileUtil;
import com.dwarfeng.dutil.develop.cfg.DefaultExconfigModel;
import com.dwarfeng.dutil.develop.cfg.ExconfigModel;
import com.dwarfeng.dutil.develop.cfg.io.PropConfigSaver;
import com.dwarfeng.projwiz.raefrm.model.eum.FileCoreConfigEntry;
import com.dwarfeng.projwiz.raefrm.model.eum.ProjCoreConfigEntry;

public class ConfigEntrySaver {

	public static void main(String[] args) throws Exception {
		CT.trace("开始保存配置文件...");

		// ------------------------------------------------------------------------------------------------------------
		CT.trace("保存ProjProcCCE配置文件...");
		ExconfigModel meppModel = new DefaultExconfigModel();
		meppModel.addAll(Arrays.asList(ProjCoreConfigEntry.values()));

		File meppFile = new File("target" + File.separator + "cfg.core-projproc.properties");
		FileUtil.createFileIfNotExists(meppFile);

		try (PropConfigSaver projProcCCESaver = new PropConfigSaver(new FileOutputStream(meppFile))) {
			projProcCCESaver.countinuousSave(meppModel);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// ------------------------------------------------------------------------------------------------------------
		CT.trace("保存FileProcCCE配置文件...");
		ExconfigModel fofpModel = new DefaultExconfigModel();
		fofpModel.addAll(Arrays.asList(FileCoreConfigEntry.values()));

		File fofpFile = new File("target" + File.separator + "cfg.core-fileproc.properties");
		FileUtil.createFileIfNotExists(fofpFile);

		try (PropConfigSaver fileProcCCESaver = new PropConfigSaver(new FileOutputStream(fofpFile))) {
			fileProcCCESaver.countinuousSave(fofpModel);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// ------------------------------------------------------------------------------------------------------------
		CT.trace("所有文件保存成功!");
	}

}
