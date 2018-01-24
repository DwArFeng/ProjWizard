package com.dwarfeng.projwiz.core.model.struct;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.dwarfeng.dutil.basic.prog.Filter;

/**
 * 元数据仓库。
 * 
 * <p>
 * 组件的元数据存放的仓库。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface MetaDataStorage {

	/**
	 * 创建指定名称的文件。
	 * 
	 * @param fileName
	 *            指定的名称。
	 * @return 是否创建陈宫。
	 * @throws IOException
	 *             IO异常。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public boolean create(String fileName) throws IOException;

	/**
	 * 删除指定名称的文件。
	 * 
	 * @param fileName
	 *            指定的文件名称。
	 * @return 文件是否被删除。
	 * @throws IOException
	 *             IO异常。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public boolean delete(String fileName) throws IOException;

	/**
	 * 打开指定名称的文件的输入流。
	 * 
	 * @param fileName
	 *            指定的文件名称。
	 * @return 指定名称的文件的输入流。
	 * @throws IOException
	 *             IO异常。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public InputStream openInputStream(String fileName) throws IOException;

	/**
	 * 打开指定名称的文件的输出流。
	 * 
	 * @param fileName
	 *            指定的文件名称。
	 * @return 指定名称的文件的输出流。
	 * @throws IOException
	 *             IO异常。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public OutputStream openOutputStream(String fileName) throws IOException;

	/**
	 * 列出仓库中所有文件的名称。
	 * 
	 * @return 所有文件的名称组成的数组。
	 */
	public String[] listFileName();

	/**
	 * 列出仓库中满足指定过过滤器的文件名称。
	 * 
	 * @param filter
	 *            指定的过滤器。
	 * @return 满足指定过滤器的所有的文件组成的数组。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public String[] listFileName(Filter<String> filter);

}
