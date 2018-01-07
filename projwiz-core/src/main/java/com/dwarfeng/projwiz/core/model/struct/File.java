package com.dwarfeng.projwiz.core.model.struct;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import com.dwarfeng.dutil.basic.prog.ObverserSet;
import com.dwarfeng.dutil.basic.str.Name;
import com.dwarfeng.dutil.basic.threads.ExternalReadWriteThreadSafe;
import com.dwarfeng.projwiz.core.model.cm.Tree;
import com.dwarfeng.projwiz.core.model.obv.FileObverser;

/**
 * ProjectWizard 的文件类。
 * 
 * <p>
 * 该接口是程序中的文件系统的共同接口。
 * <p>
 * 文件接口的定义需要严格遵守以下的规定
 * 
 * <blockquote> 1. 程序为文件接口提供了最大限度的实现， 也就是 {@link AbstractFile}，使用
 * {@link AbstractFile}扩展方法实现 {@link File}，在绝大部分情况下要比直接实现 {@link File}接口好得多。
 * <p>
 * 2. 文件中的方法 {@link #getFileTree()}
 * 返回的树作为文件的文件的视图，要求其本身不可编辑，这是因为文件的操作不仅仅是直接操作文件树，
 * 比如有的时候，需要通过对外来文件的转换等操作方式来进行文件添加，这不是 {@link Tree#add(Object, Object)}能解决的问题。
 * 所有的文件操作由 {@link File} 接口进行定义。
 * <p>
 * 3. 根元素必须是文件夹，即找到对应的注册键所指定的注册信息 {@link FileProcessor#isFolder(File)} 应该返回
 * <code>true</code>，因此，根元素文件的注册键最好选用预设的注册键"com.dwarfeng.projwiz.plugin.fofp.FolderFileProcessor"，
 * 对于任何文件，它的注册信息对应的 <code>isFolder(File)</code> 永远为 <code>true</code>。
 * <p>
 * 4. 文件接口实现了观察器集合接口，需要维护一个由 {@link FileObverser} 组成的集合， 对于文件的实现，
 * 执行任何方法都应该准确无误地广播相应的观察器， 否则依赖于这些观察器的功能便无法正常工作，侦听器的广播方法已经由文件的抽象实现
 * {@link AbstractFile} 实现，实现文件接口时，只需要在相应的方法中调用这些广播方法即可。
 * <p>
 * 5. 对于第三条，应特别注意的是：如果文件的仓库打开或关闭输入/输出流，那么要通知观察器广播相应的方法。
 * <p>
 * 6. 除了特别说明，否则各个方法中传入的 {@link File} 与 {@link File} 对象都是只读的，这是因为 {@link File} 与
 * {@link File}中很多方法都是连锁的， 如果将这些方法暴露给插件，那么直接更改这些方法很可能导致程序未知的异常。
 * 如果要访问这些文件的写方法的话，可以通过 {@link Toolkit}来实现， {@link Toolkit}可以在调用
 * {@link FileProcessor#newFile()}的时候，由 {@link FileProcessor} 传递到 {@link File}中。
 * <p>
 * 7. 文件中有很多方法是用来指示该文件是否支持某些方法的，比如 {@link File#isSaveSupported()} 就是用来指示文件是否支持
 * {@link File#save()}的。需要注意的是，如果判断一个方法是否受支持的方法返回 <code>true</code>，那么被判断的方法<b>
 * 不能 </b>抛出 {@link UnsupportedOperationException}。
 * <p>
 * 8. 每个文件应该重写 {@link Object#equals(Object)} 方法， 当 {@link File#getUniqueLabel()}
 * 相等时，文件也应该相等， {@link AbstractFile} 已经实现了此项约束， 文件的具体实现除此之外还需要保证不同的文件具有不同的独立标签。
 * <p>
 * 9. 请确保实现文件的具体方法都是线程安全的，必要时使用 {@link File#getLock()} 进行同步。</blockquote>
 * 
 * TODO 完善File的接口说明。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface File extends Name, ExternalReadWriteThreadSafe, ObverserSet<FileObverser> {

	/**
	 * 获取工程的独立标签。
	 * 
	 * @return 工程的独立标签。
	 */
	public String getUniqueLabel();

	/**
	 * 返回该注册类下指定文件是否允许指定键对应的注册类在其中。
	 * <p>
	 * 该方法仅针对于文件夹有效。
	 * 
	 * @param key
	 *            指定的键。
	 * @return 是否允许指定的键对应的注册类在其中。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public boolean acceptIn(String key);

	/**
	 * 丢弃仓库中的某个标签（可选操作）。
	 * <p>
	 * 该操作将会将指定标签与其对应的文件断开映射关系，但不会立即删除文件，而是等待<code>gc</code>来回收。
	 * 
	 * @param label
	 *            指定的标签。
	 * @throws IOException
	 *             IO异常。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 * @throws UnsupportedOperationException
	 *             不支持该操作。
	 */
	public void discardLabel(String label) throws IOException;

	/**
	 * 获取文件的访问时间。
	 * 
	 * <p>
	 * 访问时间小于0，代表文件从未访问。
	 * 
	 * @return 文件的访问时间。
	 */
	public long getAccessTime();

	/**
	 * 获取文件的创建时间。
	 * 
	 * <p>
	 * 创建时间小于0，代表文件从未创建。
	 * 
	 * @return 文件的创建时间。
	 */
	public long getCreateTime();

	/**
	 * 获取仓库中的所有标签。
	 * <p>
	 * 以集合形式返回仓库中的所有标签，集合是只读的。
	 * 
	 * @return 仓库中所有标签组成的集合。
	 */
	public Set<String> getLabels();

	/**
	 * 获取文件的长度。
	 * 
	 * <p>
	 * 返回值小于0代表该文件不支持该方法。
	 * 
	 * @return 文件的长度。
	 */
	public long getLength();

	/**
	 * 获取指定标签下的文件的长度。
	 * <p>
	 * 该操作将会获取指定标签下最新的被提交的文件的长度。
	 * 
	 * @param label
	 *            指定的标签。
	 * @return 指定的标签下对应的最新被提交的文件的长度。
	 * @throws IllegalArgumentException
	 *             仓库中不包含指定的标签。
	 */
	public long getLength(String label);

	/**
	 * 获取文件的编辑时间。
	 * 
	 * <p>
	 * 编辑时间小于0，代表文件从未编辑。
	 * 
	 * @return 文件的编辑时间。
	 */
	public long getModifyTime();

	/**
	 * 返回文件的占用大小。
	 * 
	 * <p>
	 * 占用大小指文件自身的长度和其所包含的所有子文件的占用大小的综合。
	 * 
	 * <p>
	 * 返回值小于0代表该文件不支持该方法。
	 * 
	 * @return 文件的占用大小。
	 */
	public long getOccupiedSize();

	/**
	 * 获得该文档的注册键。
	 * 
	 * @return 该文档的注册键。
	 */
	public String getRegisterKey();

	/**
	 * 返回该文件是否为一个文件夹。
	 * 
	 * @return 该文件是否为一个文件夹。
	 */
	public boolean isFolder();

	/**
	 * 是否支持读取操作。
	 * 
	 * @return 是否支持。
	 */
	public boolean isReadSupported();

	/**
	 * 是否支持写入操作。
	 * 
	 * @return 是否支持。
	 */
	public boolean isWriteSupported();

	/**
	 * 要求仓库开辟一个新的标签（可选操作）。
	 * <p>
	 * 该操作将会使仓库新建一个新的空文件，并且将该文件与指定的标签映射。
	 * 
	 * @param label
	 *            指定的标签。
	 * @throws IllegalArgumentException
	 *             仓库中已经存在指定的标签。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IOException
	 *             IO异常。
	 * @throws UnsupportedOperationException
	 *             不支持该操作。
	 */
	public void newLabel(String label) throws IOException;

	/**
	 * 打开指定的标签对应的输入流（可选操作）。
	 * 
	 * @param label
	 *            指定的标签。
	 * @return 指定的标签对应的输入流。
	 * @throws IllegalArgumentException
	 *             指定的标签不存在。
	 * @throws IOException
	 *             IO异常。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 * @throws UnsupportedOperationException
	 *             不支持该操作。
	 */
	public InputStream openInputStream(String label) throws IOException;

	/**
	 * 打开指定标签对应的输出流（可选操作）。
	 * 
	 * @param labe
	 *            指定的标签。
	 * @return 指定的标签对应的输出流。
	 * @throws IllegalArgumentException
	 *             指定的标签不存在。
	 * @throws IOException
	 *             IO异常。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 * @throws UnsupportedOperationException
	 *             不支持该操作。
	 */
	public OutputStream openOutputStream(String label) throws IOException;

	/**
	 * 清除仓库中的所有内容。
	 * 
	 * @throws IOException
	 *             IO异常。
	 */
	public default void purge() throws IOException {
		for (String label : getLabels()) {
			discardLabel(label);
		}
	}

}
