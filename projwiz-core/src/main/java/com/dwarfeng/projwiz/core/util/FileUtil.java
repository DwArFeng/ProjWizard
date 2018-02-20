package com.dwarfeng.projwiz.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

import com.dwarfeng.projwiz.core.model.cm.Tree.Path;
import com.dwarfeng.projwiz.core.model.struct.File;
import com.dwarfeng.projwiz.core.model.struct.Project;

/**
 * 有关于文件的工具包。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class FileUtil {

	/**
	 * 文件名称比较器。
	 * 
	 * @author DwArFeng
	 * @since 0.0.1-alpha
	 */
	private static final class DefaultFileComparator implements Comparator<File> {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compare(File o1, File o2) {
			Objects.requireNonNull(o1, "入口参数 o1 不能为 null。");
			Objects.requireNonNull(o2, "入口参数 o2 不能为 null。");

			if (o1.isFolder() && !o2.isFolder()) {
				return -1;
			} else if (!o1.isFolder() && o2.isFolder()) {
				return 1;
			}

			int result = 0;

			if ((result = o1.getComponentKey().compareTo(o2.getComponentKey())) != 0) {
				return result;
			}

			return o1.getName().compareTo(o2.getName());
		}

	}

	private static final DefaultFileComparator FILE_COMPARAOTR = new DefaultFileComparator();

	private final static String MULTI_STD_PATH_REGEX = "^([^;^\\*^%^&^@^!^#^\\^^\\\\^/^"
			+ "+^:^$^\\r^\\n]+:([^;^\\*^%^&^@^!^#^\\^^\\\\^/^+^:^$^\\r^\\n]+/)*[^;^\\*^%^&"
			+ "^@^!^#^\\^^\\\\^/^+^:^$^\\r^\\n]+;)*[^;^\\*^%^&^@^!^#^\\^^\\\\^/^+^:^$^\\r"
			+ "^\\n]+:([^;^\\*^%^&^@^!^#^\\^^\\\\^/^+^:^$^\\r^\\n]+/)*[^;^\\*^%^&^@^!^#^"
			+ "\\^^\\\\^/^+^:^$^\\r^\\n]+;*$";
	private final static String SINGLE_STD_PATH_REGEX = "^[^;^\\*^%^&^@^!^#^\\^^\\\\^/^"
			+ "+^:^$^\\r^\\n]+:([^;^\\*^%^&^@^!^#^\\^^\\\\^/^+^:^$^\\r^\\n]+/)*[^;^\\*^%^&"
			+ "^@^!^#^\\^^\\\\^/^+^:^$^\\r^\\n]+$";
	private final static String STD_PATH_PROJECT_DIM = ":";
	private final static String STD_PATH_FOLDER_DIM = "/";
	private final static String STD_PATH_FILE_DIM = ";";

	/**
	 * 返回指定的文件是否拥有指定名称的子文件。
	 * 
	 * @param project
	 *            文件所在的工程。
	 * @param parent
	 *            指定的文件。
	 * @param name
	 *            指定的名称。
	 * @return 该文件是否拥有指定名称的子文件。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public static boolean containsFile(Project project, File parent, String name) {
		Objects.requireNonNull(name, "入口参数 name 不能为 null。");

		parent.getLock().readLock().lock();
		try {
			if (!parent.isFolder())
				return false;
			for (File file : project.getFileTree().getChilds(parent)) {
				if (file.getName().equals(name))
					return true;
			}
			return false;
		} finally {
			parent.getLock().readLock().unlock();
		}
	}

	/**
	 * 获取文件比较器。
	 * 
	 * @return 文件比较器。
	 */
	public static Comparator<File> defaultFileComparator() {
		return FILE_COMPARAOTR;
	}

	/**
	 * 寻找指定的文件属于哪个工程。
	 * <p>
	 * 如果一个文件存在于多个工程之中，则只返回第一个包含该工程的文件。
	 * 
	 * @param projects
	 *            指定的文件可能存在的工程集合。
	 * @param file
	 *            指定的文件。
	 * @return 指定的文件属于哪个工程，如果都不属于，则返回 <code>null</code>。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 */
	public static final Project findProjectWithFile(Collection<Project> projects, File file) {
		Objects.requireNonNull(projects, "入口参数 projects 不能为 null。");
		Objects.requireNonNull(file, "入口参数 file 不能为 null。");

		for (Project project : projects) {
			project.getLock().readLock().lock();
			try {
				if (project.getFileTree().contains(file)) {
					return project;
				}
			} finally {
				project.getLock().readLock().unlock();
			}
		}

		return null;
	}

	/**
	 * 通过指定的标准路径获得对应的文件。
	 * 
	 * @param projects
	 *            文件可能处在的工程组成的集合。
	 * @param stdPath
	 *            指定的标准路径。
	 * @return 指定的标准路径对应的文件。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IllegalArgumentException
	 *             指定的字符串不是标准路径，或者无法在工程集合中找到对应的文件。
	 */
	public static final File getFile(Collection<? extends Project> projects, String stdPath) {
		Objects.requireNonNull(projects, "入口参数 projects 不能为 null。");
		Objects.requireNonNull(stdPath, "入口参数 stdPath 不能为 null。");

		if (!isSingleStdPath(stdPath)) {
			if (!isStdPath(stdPath)) {
				throw new IllegalArgumentException("指定的 stdPath 不是路径。");
			}
			throw new IllegalArgumentException("指定的 stdPath 不是单个文件路径。");
		}

		StringTokenizer projectSt = new StringTokenizer(stdPath, STD_PATH_PROJECT_DIM);
		String projectName = projectSt.nextToken();

		Project aimProject = null;
		for (Project project : projects) {
			if (Objects.equals(projectName, project.getName())) {
				aimProject = project;
				break;
			}
		}

		if (Objects.isNull(aimProject)) {
			throw new IllegalArgumentException("未能在指定的工程集合中找到路径指示的工程。");
		}

		return getFile(aimProject, stdPath);
	}

	/**
	 * 通过指定的标准路径获得对应的文件。
	 * 
	 * @param project
	 *            文件所在的工程。
	 * @param stdPath
	 *            指定的标准路径。
	 * @return 指定的标准路径对应的文件，如果没有在工程中找到指定的文件，则返回 <code>null</code>。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 * @throws IllegalArgumentException
	 *             stdPath不符合标准要求。
	 */
	public static final File getFile(Project project, String stdPath) {
		Objects.requireNonNull(project, "入口参数 project 不能为 null。");
		Objects.requireNonNull(stdPath, "入口参数 stdPath 不能为 null。");

		if (!isSingleStdPath(stdPath)) {
			if (!isStdPath(stdPath)) {
				throw new IllegalArgumentException("指定的 stdPath 不是路径。");
			}
			throw new IllegalArgumentException("指定的 stdPath 不是单个文件路径。");
		}

		StringTokenizer projectSt = new StringTokenizer(stdPath, STD_PATH_PROJECT_DIM);
		String projectName = projectSt.nextToken();
		String filesName = projectSt.nextToken();

		if (!Objects.equals(projectName, project.getName())) {
			throw new IllegalArgumentException("入口工程与 stdPath 中的工程不一致。");
		}

		Collection<? extends File> avaFiles = Arrays.asList(project.getFileTree().getRoot());
		File aimFile = null;
		StringTokenizer fileSt = new StringTokenizer(filesName, STD_PATH_FOLDER_DIM);
		cti: while (fileSt.hasMoreTokens()) {
			String fileName = fileSt.nextToken();
			for (File file : avaFiles) {
				if (Objects.equals(fileName, file.getName())) {
					avaFiles = project.getFileTree().getChilds(file);
					aimFile = file;
					continue cti;
				}
			}
			throw new IllegalArgumentException("未能在工程中找到指定的文件。");
		}

		return aimFile;
	}

	/**
	 * 通过指定的标准路径获得对应的文件数组。
	 * 
	 * @param projects
	 *            文件可能处在的工程组成的集合。
	 * @param stdPath
	 *            指定的标准路径。
	 * @return 通过指定的标准路径获得对应的文件数组。
	 * @throws NullPointerException
	 *             指定的入口参数为 <code> null </code>。
	 * @throws IllegalArgumentException
	 *             stdPath不符合标准要求。
	 */
	public static final File[] getFiles(Collection<? extends Project> projects, String stdPath) {
		Objects.requireNonNull(projects, "入口参数 projects 不能为 null。");
		Objects.requireNonNull(stdPath, "入口参数 stdPath 不能为 null。");

		if (!isStdPath(stdPath)) {
			throw new IllegalArgumentException("指定的 stdPath 不是路径。");
		}

		StringTokenizer pathSt = new StringTokenizer(stdPath, STD_PATH_FILE_DIM);
		List<File> fileList = new ArrayList<>();

		while (pathSt.hasMoreTokens()) {
			String path = pathSt.nextToken();
			fileList.add(getFile(projects, path));
		}

		return fileList.toArray(new File[0]);
	}

	/**
	 * 获取指定文件的标准路径。
	 * 
	 * @param projects
	 *            指定的文件可能处在的工程组成的集合。
	 * @param file
	 *            指定的文件。
	 * @return 指定文件的标准路径。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IllegalArgumentException
	 *             指定的文件不在工程列表中。
	 */
	public static final String getStdPath(Collection<Project> projects, File file)
			throws NullPointerException, IllegalArgumentException {
		Objects.requireNonNull(projects, "入口参数 projects 不能为 null。");
		Objects.requireNonNull(file, "入口参数 file 不能为 null。");

		Project project = findProjectWithFile(projects, file);
		if (Objects.isNull(project)) {
			throw new IllegalArgumentException("指定的文件不在指定的工程中。");
		}

		return getStdPath(project, file);
	}

	/**
	 * 获取指定文件列表的组合标准路径。
	 * <p>
	 * 组合标准路径指多个文件的标准路径的组合，其中不同的路径用 <code>;</code>分隔。
	 * 
	 * @param projects
	 *            文件可能处在的所有工程的集合。
	 * @param files
	 *            指定的文件列表。
	 * @return 指定文件列表的组合标准路径。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IllegalArgumentException
	 *             至少一个文件列表中的文件不属于指定的工程。
	 */
	public static final String getStdPath(Collection<Project> projects, List<File> files)
			throws NullPointerException, IllegalArgumentException {
		Objects.requireNonNull(projects, "入口参数 projects 不能为 null。");
		Objects.requireNonNull(files, "入口参数 files 不能为 null。");

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < files.size(); i++) {
			sb.append(getStdPath(projects, files.get(i)));

			// 如果不是最后一个，则需要在文本的最后加上分号。
			if (i < files.size() - 1) {
				sb.append(STD_PATH_FILE_DIM);
			}
		}

		return sb.toString();
	}

	/**
	 * 获取指定工程下指定文件的标准路径。
	 * 
	 * @param project
	 *            指定的工程。
	 * @param file
	 *            指定的文件。
	 * @return 指定工程下指定文件的标准路径。
	 * @throws NullPointerException
	 *             入口参数为 <code>null</code>。
	 * @throws IllegalArgumentException
	 *             指定的文件不属于指定的工程。
	 */
	public static final String getStdPath(Project project, File file)
			throws NullPointerException, IllegalArgumentException {
		Objects.requireNonNull(project, "入口参数 project 不能为 null。");
		Objects.requireNonNull(file, "入口参数 file 不能为 null。");

		Path<? extends File> filePath = project.getFileTree().getPath(file);

		if (Objects.isNull(filePath)) {
			throw new IllegalArgumentException("指定的文件不在指定的工程中。");
		}

		StringBuilder sb = new StringBuilder();
		sb.append(project.getName());
		sb.append(STD_PATH_PROJECT_DIM);

		for (File pathFile : filePath) {
			sb.append(pathFile.getName());
			if (!Objects.equals(pathFile, filePath.get(filePath.depth() - 1))) {
				sb.append(STD_PATH_FOLDER_DIM);
			}
		}

		return sb.toString();
	}

	/**
	 * 返回一个字符串是否是标准路径。
	 * 
	 * @param string
	 *            指定的字符串。
	 * @return 指定的字符串是否是标准路径。
	 */
	public static final boolean isStdPath(String string) {
		if (Objects.isNull(string)) {
			return false;
		}

		return string.matches(MULTI_STD_PATH_REGEX);
	}

	/**
	 * 返回一个字符串是否是单个文件的标准路径。
	 * 
	 * @param string
	 *            指定的字符串。
	 * @return 指定的字符串是否是单个文件的标准路径。
	 */
	public static final boolean isSingleStdPath(String string) {
		if (Objects.isNull(string)) {
			return false;
		}

		return string.matches(SINGLE_STD_PATH_REGEX);
	}

	// 禁止外部实例化
	private FileUtil() {
	}

}
