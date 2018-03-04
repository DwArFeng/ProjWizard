package com.dwarfeng.projwiz.basic4.tool;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Objects;

import com.dwarfeng.dutil.basic.io.CT;
import com.dwarfeng.dutil.basic.io.FileUtil;
import com.dwarfeng.dutil.develop.cfg.DefaultExconfigModel;
import com.dwarfeng.dutil.develop.cfg.ExconfigModel;
import com.dwarfeng.dutil.develop.cfg.io.PropConfigSaver;
import com.dwarfeng.projwiz.basic4.model.eum.FofpConfigEntry;
import com.dwarfeng.projwiz.basic4.model.eum.MeppConfigEntry;

public class ConfigEntrySaver {

	public static void main(String[] args) throws Exception {
		CT.trace("开始保存配置文件...");

		// ------------------------------------------------------------------------------------------------------------
		CT.trace("保存mepp配置文件...");
		ExconfigModel meppModel = new DefaultExconfigModel();
		meppModel.addAll(Arrays.asList(MeppConfigEntry.values()));

		File meppFile = new File("target" + File.separator + "cfg.core.mepp.properties");
		FileUtil.createFileIfNotExists(meppFile);

		PropConfigSaver meppSaver = new PropConfigSaver(new FileOutputStream(meppFile));
		try {
			meppSaver.countinuousSave(meppModel);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (Objects.nonNull(meppSaver)) {
				meppSaver.close();
			}
		}

		// ------------------------------------------------------------------------------------------------------------
		CT.trace("保存fofp配置文件...");
		ExconfigModel fofpModel = new DefaultExconfigModel();
		fofpModel.addAll(Arrays.asList(FofpConfigEntry.values()));

		File fofpFile = new File("target" + File.separator + "cfg.core.fofp.properties");
		FileUtil.createFileIfNotExists(fofpFile);

		PropConfigSaver fofpSaver = new PropConfigSaver(new FileOutputStream(fofpFile));
		try {
			fofpSaver.countinuousSave(fofpModel);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (Objects.nonNull(fofpSaver)) {
				fofpSaver.close();
			}
		}

		
		// ------------------------------------------------------------------------------------------------------------
		CT.trace("所有文件保存成功!");
	}

}
