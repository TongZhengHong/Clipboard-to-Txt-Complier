package main.listeners.clipboard;

public interface ClipboardInterface {
	/**
	 * MyCustomListener callback when clipboard changes. 
	 * Update clipboard text area with new change and autosave if needed
	 * @param data
	 */
	void onClipboardUpdate(String data);

	/**
	 * Callback to show dialog to inform user that clipboard listener has crashed
	 * and give suggestions regarding the potential issues.
	 */
	void onClipboardListenerCrash();

	/**
	 * Callback to show dialog if clipboard listener crash and unable to restart
	 */
	void onClipboardListenerDied();
}
