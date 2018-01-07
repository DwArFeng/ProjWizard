package com.dwarfeng.projwiz.core.model.struct;

/**
 * 工程-文件对。
 * 
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public final class ProjectFilePair {

	private final Project project;
	private final File file;

	/**
	 * 新实例。
	 * 
	 * @param project
	 *            指定的工程。
	 * @param file
	 *            指定的文件。
	 */
	public ProjectFilePair(Project project, File file) {
		this.project = project;
		this.file = file;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ProjectFilePair)) {
			return false;
		}
		ProjectFilePair other = (ProjectFilePair) obj;
		if (file == null) {
			if (other.file != null) {
				return false;
			}
		} else if (!file.equals(other.file)) {
			return false;
		}
		if (project == null) {
			if (other.project != null) {
				return false;
			}
		} else if (!project.equals(other.project)) {
			return false;
		}
		return true;
	}

	/**
	 * 获取对象中的文件。
	 * 
	 * @return 对象中的文件。
	 */
	public File getFile() {
		return file;
	}

	/**
	 * 获取对象中的工程。
	 * 
	 * @return 对象中的工程。
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		result = prime * result + ((project == null) ? 0 : project.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "ProjectFilePair [project=" + project + ", file=" + file + "]";
	}

}