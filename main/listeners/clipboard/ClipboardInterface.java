package main.listeners.clipboard;

public interface ClipboardInterface {
	/**
	 * MyCustomListener callback when clipboard changes. 
	 * Update clipboard text area with new change and autosave if needed
	 * @param data
	 */
	void onClipboardUpdate(String data);
}
