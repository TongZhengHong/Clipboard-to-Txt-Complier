package main.listeners;

import java.io.File;

public interface MyCustomListeners {
	/**
	 * MyCustomListener callback when clipboard changes. 
	 * Update clipboard text area with new change and autosave if needed
	 * @param data
	 */
	void onClipboardUpdate(String data);

	/**
	 * Updates outputFolderTextField which will update file browser
	 * when folder is clicked in fileBrowser
	 * @param path
	 */
	void onFileBrowserItemClick(File selectedFile);

	/**
	 * Updates outputFolderTextField which will update file browser
	 * when folder is clicked in fileBrowser
	 * @param path
	 */
	void onFileBrowserFolderClick(String path);
}
