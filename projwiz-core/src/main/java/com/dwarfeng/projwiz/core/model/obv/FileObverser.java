package com.dwarfeng.projwiz.core.model.obv;

import com.dwarfeng.dutil.basic.prog.Obverser;
import com.dwarfeng.projwiz.core.model.struct.FileProcessor;

public interface FileObverser extends Obverser {

	/**
	 * 通知访问时间发生改变。
	 * 
	 * @param oldValue
	 *            旧的访问时间。
	 * @param newValue
	 *            新的访问时间。
	 */
	public void fireAccessTimeChanged(long oldValue, long newValue);

	/**
	 * 通知创建时间发生改变。
	 * 
	 * @param oldValue
	 *            旧的创建时间。
	 * @param newValue
	 *            新的创建时间。
	 */
	public void fireCreateTimeChanged(long oldValue, long newValue);

	/**
	 * 通知指定的文件的指定标签所在的输入流被关闭。
	 * 
	 * @param label
	 *            指定的标签。
	 */
	public void fireInputClosed(String label);

	/**
	 * 通知指定的文件的指定标签所在的输入流被打开。
	 * 
	 * @param label
	 *            指定的标签。
	 */
	public void fireInputOpened(String label);

	/**
	 * 通知指定的标签被添加。
	 * 
	 * @param label
	 *            指定的标签。
	 */
	public void fireLabelAdded(String label);

	/**
	 * 通知指定的标签被移除。
	 * 
	 * @param label
	 *            指定的标签。
	 */
	public void fireLabelRemoved(String label);

	/**
	 * 通知文件的长度发生改变。
	 * 
	 * @param oldValue
	 *            旧的长度。
	 * @param newValue
	 *            新的长度。
	 */
	public void fireLengthChanged(long oldValue, long newValue);

	/**
	 * 通知编辑时间发生改变。
	 * 
	 * @param oldValue
	 *            旧的编辑时间。
	 * @param newValue
	 *            新的编辑时间。
	 */
	public void fireModifyTimeChanged(long oldValue, long newValue);

	/**
	 * 通知文件的占用大小发生了改变。
	 * 
	 * @param oldValue
	 *            旧的占用大小。
	 * @param newValue
	 *            新的占用大小。
	 */
	public void fireOccupiedSizeChanged(long oldValue, long newValue);

	/**
	 * 通知是定的文件的指定标签所在的输出流被关闭。
	 * 
	 * @param label
	 *            指定的标签。
	 */
	public void fireOutputClosed(String label);

	/**
	 * 通知指定的文件的指定标签所在的输出流被打开。
	 * 
	 * @param label
	 *            指定的标签。
	 */
	public void fireOutputOpened(String label);

	/**
	 * 通知文件的处理器类发生了改变。
	 * 
	 * @param oldValue
	 *            旧的处理器类。
	 * @param newValue
	 *            新的处理器类。
	 */
	public void fireProcessorClassChanged(Class<? extends FileProcessor> oldValue,
			Class<? extends FileProcessor> newValue);

}
