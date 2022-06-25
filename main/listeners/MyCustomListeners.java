package main.listeners;

public interface MyCustomListeners {
	/**
	 * MyCustomListener callback when clipboard changes. 
	 * Update clipboard text area with new change and autosave if needed
	 * @param data
	 */
	void onClipboardUpdate(String data);
}
