package com.dwarfeng.projwiz.core.model.struct;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 临时仓库。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface TempStorage {

	/**
	 * 打开指定标签对应的输入流。
	 * <p>
	 * 如果指定的标签不存在，则返回 <code>null</code>。
	 * 
	 * @param label
	 *            指定的标签。
	 * @return 指定的标签对应的输入流。
	 * @throws IOException
	 *             IO异常。
	 */
	public InputStream openInputStream(String label) throws IOException;

	/**
	 * 打开指定的标签对应的输出流。
	 * <p>
	 * 如果指定的标签不存在，则返回 <code>null</code>。
	 * 
	 * @param label
	 *            指定的标签。
	 * @return 指定的标签对应的输出流。
	 * @throws IOException
	 *             IO异常。
	 */
	public OutputStream openOutputStream(String label) throws IOException;

	/**
	 * 释放指定的标签。
	 * 
	 * @param key
	 *            指定的标签。
	 */
	public void disposeLabel(String key) throws IOException;

	/**
	 * 申请一个新的临时文件。
	 * 
	 * @return 新的临时文件的标签。
	 * @throws IOException
	 *             IO异常。
	 */
	public String requireNewLabel() throws IOException;

}
