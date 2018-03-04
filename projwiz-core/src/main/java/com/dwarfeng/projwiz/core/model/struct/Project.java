package com.dwarfeng.projwiz.core.model.struct;

import com.dwarfeng.dutil.basic.prog.ObverserSet;
import com.dwarfeng.dutil.basic.prog.ProcessException;
import com.dwarfeng.dutil.basic.str.Name;
import com.dwarfeng.dutil.basic.threads.ExternalReadWriteThreadSafe;
import com.dwarfeng.projwiz.core.model.cm.Tree;
import com.dwarfeng.projwiz.core.model.obv.ProjectObverser;

/**
 * 工程接口。
 * 
 * <p>
 * 工程接口的定义需要严格遵守以下的规定
 * 
 * <blockquote> 1. 程序为工程接口提供了最大限度的实现， 也就是 {@link AbstractProject}，使用
 * {@link AbstractProject}扩展方法实现 {@link Project}，在绝大部分情况下要比直接实现
 * {@link Project}接口好得多。
 * <p>
 * 2. 工程中的方法 {@link #getFileTree()}
 * 返回的树作为工程的文件的视图，要求其本身不可编辑，这是因为文件的操作不仅仅是直接操作工程树，
 * 比如有的时候，需要通过对外来文件的转换等操作方式来进行文件添加，这不是 {@link Tree#add(Object, Object)}能解决的问题。
 * 所有的文件操作由 {@link Project} 接口进行定义。
 * <p>
 * 3. 根元素必须是文件夹，即找到对应的注册键所指定的注册信息 {@link FileProcessor#isFolder(File)} 应该返回
 * <code>true</code>，因此，根元素文件的注册键最好选用预设的注册键"com.dwarfeng.projwiz.plugin.fofp.FolderFileProcessor"，
 * 对于任何文件，它的注册信息对应的 <code>isFolder(File)</code> 永远为 <code>true</code>。
 * <p>
 * 4. 工程接口实现了观察器集合接口，需要维护一个由 {@link ProjectObverser} 组成的集合， 对于工程的实现，
 * 执行任何方法都应该准确无误地广播相应的观察器， 否则依赖于这些观察器的功能便无法正常工作，侦听器的广播方法已经由工程的抽象实现
 * {@link AbstractProject} 实现，实现工程接口时，只需要在相应的方法中调用这些广播方法即可。
 * <p>
 * 5. 对于第三条，应特别注意的是：如果文件的仓库打开或关闭输入/输出流，那么要通知观察器广播相应的方法。
 * <p>
 * 6. 除了特别说明，否则各个方法中传入的 {@link Project} 与 {@link File} 对象都是只读的，这是因为
 * {@link Project} 与 {@link File}中很多方法都是连锁的，
 * 如果将这些方法暴露给插件，那么直接更改这些方法很可能导致程序未知的异常。 如果要访问这些文件的写方法的话，可以通过 {@link Toolkit}来实现，
 * {@link Toolkit}可以在调用 {@link ProjectProcessor#newProject()}的时候，由
 * {@link ProjectProcessor} 传递到 {@link Project}中。
 * <p>
 * 7. 工程中有很多方法是用来指示该工程是否支持某些方法的，比如 {@link Project#isSaveSupported()}
 * 就是用来指示工程是否支持 {@link Project#save()}的。需要注意的是，如果判断一个方法是否受支持的方法返回
 * <code>true</code>，那么被判断的方法<b> 不能 </b>抛出
 * {@link UnsupportedOperationException}。
 * <p>
 * 8. 每个工程应该重写 {@link Object#equals(Object)} 方法， 当
 * {@link Project#getUniqueLabel()} 相等时，工程也应该相等， {@link AbstractProject}
 * 已经实现了此项约束， 工程的具体实现除此之外还需要保证不同的工程具有不同的独立标签。
 * <p>
 * 9. 请确保实现工程的具体方法都是线程安全的，必要时使用 {@link Project#getLock()} 进行同步。</blockquote>
 * 
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface Project extends ObverserSet<ProjectObverser>, ExternalReadWriteThreadSafe, Name {

	/**
	 * 添加文件的情形。
	 * 
	 * @author DwArFeng
	 * @since 0.0.3-alpha
	 */
	public enum AddingSituation {
		/** 通过复制的方式添加文件。 */
		BY_COPY,
		/** 通过移动的方式添加文件。 */
		BY_MOVE,
		/** 通过新建的方式添加文件。 */
		BY_NEW,
		/** 其它方式。 */
		OTHER,
	}

	/**
	 * 移除文件的情形。
	 * 
	 * @author DwArFeng
	 * @since 0.0.3-alpha
	 */
	public enum RemovingSituation {
		/** 通过删除的方式移除文件。 */
		BY_DELETE,
		/** 通过移动的方式移除文件。 */
		BY_MOVE,
		/** 其它方式。 */
		OTHER,
	}

	/**
	 * 向工程中添加指定的文件（可选操作）。
	 * 
	 * @param parent
	 *            指定文件的父节点。
	 * @param file
	 *            指定的文件。
	 * @param exceptName
	 *            该文件的期望名称。
	 * @param situation
	 *            添加文件时的情景。
	 * @return 实际被添加进工程中的文件，如果失败，则为 <code>null</code>。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IllegalArgumentException
	 *             参数异常。
	 * @throws UnsupportedOperationException
	 *             不支持的操作。
	 */
	public File addFile(File parent, File file, String exceptName, AddingSituation situation);

	/**
	 * 获取工程中指定文件的名称。
	 * 
	 * <p>
	 * 如果工程中不存在指定的文件，则返回 <code>null</code>.
	 * 
	 * @param file
	 *            指定的文件。
	 * @return 指定的文件对应的名称。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public String getFileName(File file);

	/**
	 * 获取工程接口的文件树。
	 * <p>
	 * 返回的树是不可编辑的。
	 * 
	 * @return 工程接口的文件树。
	 */
	public Tree<? extends File> getFileTree();

	/**
	 * 获取管理该工程的工程处理器的类。
	 * 
	 * @return 管理该工程的处理器的类。
	 */
	public Class<? extends ProjectProcessor> getProcessorClass();

	/**
	 * 询问该工程是否支持添加文件。
	 * 
	 * @param situation
	 *            添加文件时的情景。
	 * @return 是否支持添加文件。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	public boolean isAddFileSupported(AddingSituation situation);

	/**
	 * 询问该工程是否支持移除文件。
	 * 
	 * @param situation
	 *            移除文件时的情景。
	 * @return 是否支持移除文件。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	public boolean isRemoveFileSupported(RemovingSituation situation);

	/**
	 * 询问该工程是否支持重命名文件。
	 * 
	 * @return 是否支持重命名文件。
	 */
	public boolean isRenameFileSupported();

	/**
	 * 返回工程是否支持保存操作。
	 * 
	 * @return 是否支持。
	 */
	public boolean isSaveSupported();

	/**
	 * 是否建议停止运行。
	 * 
	 * 执行该方法时，运行器可以根据情况决定是否建议退出运行； 在有些情况下，如果不建议退出运行，则程序不会主动停止运行。
	 * （经典的情形时询问用户是否保存，如果用户点击取消，则返回 <code>false</code>
	 * ，即不建议退出运行——在某些情况下，该工程则不会退出，而是允许用户继续运行）。
	 * 
	 * @return 是否建议停止运行。
	 */
	public boolean isStopSuggest();

	/**
	 * 从工程中移除文件（可选操作）。
	 * 
	 * @param file
	 *            指定的文件。
	 * @param situation
	 *            移除文件时的情景。
	 * @return 被移除的文件。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 */
	public File removeFile(File file, RemovingSituation situation);

	/**
	 * 重命名文件（可选操作）。
	 * 
	 * @param file
	 *            指定的文件。
	 * @param newName
	 *            指定的新名称。
	 * @return 实际被重命名的文件，如果失败，则为 <code>null</code>。
	 * @throws UnsupportedOperationException
	 *             不支持该操作。
	 */
	public File renameFile(File file, String newName);

	/**
	 * 存储工程（可选操作）。
	 * <p>
	 * 快速的持久化该工程。
	 * 
	 * @return 是否存储成功。
	 * @throws ProcessException
	 *             过程失败。
	 * @throws UnsupportedOperationException
	 *             不支持该操作。
	 */
	public void save() throws ProcessException;

	/**
	 * 停止运行。
	 * <p>
	 * 该方法需要运行器立即停止运行。
	 */
	public void stop();

}
