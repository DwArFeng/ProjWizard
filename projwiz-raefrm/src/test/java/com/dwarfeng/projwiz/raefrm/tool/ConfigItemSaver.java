package com.dwarfeng.projwiz.raefrm.tool;

import java.io.File;
import java.io.FileOutputStream;

import com.dwarfeng.dutil.basic.io.CT;
import com.dwarfeng.dutil.basic.io.FileUtil;
import com.dwarfeng.dutil.develop.setting.DefaultSettingHandler;
import com.dwarfeng.dutil.develop.setting.SettingHandler;
import com.dwarfeng.dutil.develop.setting.SettingUtil;
import com.dwarfeng.dutil.develop.setting.io.PropSettingValueSaver;
import com.dwarfeng.projwiz.raefrm.model.eum.FileCoreConfigItem;
import com.dwarfeng.projwiz.raefrm.model.eum.ProjCoreConfigItem;

public class ConfigItemSaver {

	public static void main(String[] args) throws Exception {
		CT.trace("开始保存配置文件...");

		// ------------------------------------------------------------------------------------------------------------
		CT.trace("保存ProjProcCCE配置文件...");
		SettingHandler meppModel = new DefaultSettingHandler();
		SettingUtil.putEnumItems(ProjCoreConfigItem.class, meppModel);

		File meppFile = new File("target" + File.separator + "cfg.core-projproc.properties");
		FileUtil.createFileIfNotExists(meppFile);

		try (PropSettingValueSaver saver = new PropSettingValueSaver(new FileOutputStream(meppFile), true)) {
			saver.countinuousSave(meppModel);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// ------------------------------------------------------------------------------------------------------------
		CT.trace("保存FileProcCCE配置文件...");
		SettingHandler fofpModel = new DefaultSettingHandler();
		SettingUtil.putEnumItems(FileCoreConfigItem.class, meppModel);

		File fofpFile = new File("target" + File.separator + "cfg.core-fileproc.properties");
		FileUtil.createFileIfNotExists(fofpFile);

		try (PropSettingValueSaver saver = new PropSettingValueSaver(new FileOutputStream(fofpFile), true)) {
			saver.countinuousSave(fofpModel);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// ------------------------------------------------------------------------------------------------------------
		CT.trace("所有文件保存成功!");
	}

}
