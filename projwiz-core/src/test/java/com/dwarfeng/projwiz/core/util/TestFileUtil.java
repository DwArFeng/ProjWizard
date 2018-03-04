package com.dwarfeng.projwiz.core.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestFileUtil {

	@Test
	public void testIsFileName() {
		assertTrue(ProjectFileUtil.isValidFileName("123"));
		assertTrue(ProjectFileUtil.isValidFileName("a2"));
		assertTrue(ProjectFileUtil.isValidFileName("2ad"));
		assertTrue(ProjectFileUtil.isValidFileName("dwarfeng"));
		assertTrue(ProjectFileUtil.isValidFileName("中国智造 惠及全球"));
		assertFalse(ProjectFileUtil.isValidFileName("abc$def"));
		assertFalse(ProjectFileUtil.isValidFileName("abc#def"));
		assertFalse(ProjectFileUtil.isValidFileName("abc@def"));
		assertFalse(ProjectFileUtil.isValidFileName("abc.def"));
		assertFalse(ProjectFileUtil.isValidFileName("abc,def"));
		assertFalse(ProjectFileUtil.isValidFileName("中国智造，惠及全球"));

	}

}
