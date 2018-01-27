package com.dwarfeng.projwiz.core.model.struct;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.dwarfeng.dutil.basic.prog.Filter;

public class TempMetaDataStorage implements MetaDataStorage {

	@Override
	public boolean create(String fileName) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(String fileName) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public InputStream openInputStream(String fileName) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OutputStream openOutputStream(String fileName) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] listFileName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] listFileName(Filter<String> filter) {
		// TODO Auto-generated method stub
		return null;
	}

}
