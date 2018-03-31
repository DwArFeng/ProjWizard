package com.dwarfeng.projwiz.basic4.view;

import javax.swing.JDialog;

import com.dwarfeng.dutil.basic.prog.Buildable;
import com.dwarfeng.projwiz.raefrm.model.struct.FileProcToolkit;

/**
 * 
 * @author DwArFeng
 * @since 0.0.3-alpha
 */
public class NewFolderFileDialog extends JDialog {

	public static class Builder implements Buildable<NewFolderFileDialog> {

		/** 对应的文件处理器的工具包。 */
		protected final FileProcToolkit fileProcToolkit;

		/**
		 * {@inheritDoc}
		 */
		@Override
		public NewFolderFileDialog build() {
			// TODO Auto-generated method stub
			return null;
		}

	}

	/** 对应的文件处理器的工具包。 */
	protected final FileProcToolkit fileProcToolkit;

	/**
	 * @param fileProcToolkit
	 */
	protected NewFolderFileDialog(FileProcToolkit fileProcToolkit) {
		this.fileProcToolkit = fileProcToolkit;
	}

}
