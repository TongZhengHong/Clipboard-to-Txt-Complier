package main.listeners.clipboard;

import main.MainWindow;

import java.util.List;
import java.util.ArrayList;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

public class ClipboardListener extends Thread implements ClipboardOwner {
	private final int DELAY = 300; // in milliseconds

	private String previousClipBoard = "";
	private List<ClipboardInterface> listeners = new ArrayList<ClipboardInterface>();
	
	public void addClipBoardListener(ClipboardInterface listener) {
		listeners.add(listener);
	}
  
	public void run() {
		try {
			Thread.sleep(DELAY);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		regainClipboardOwnership(clipboard);

		while (true) {}
	}
  
	public void lostOwnership(Clipboard c, Transferable t) {
		try {
			// Pause to wait for clipboard object
			Thread.sleep(DELAY); 

			// Get current system clipboard instance
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

			// Get clipboard string data
			String clipboardString = (String) clipboard.getData(DataFlavor.stringFlavor);

			// Process new clipboard content
			// Only update if clipboard content changes
			if (!clipboardString.equals(previousClipBoard)) {
				for(ClipboardInterface listener : listeners) {
					listener.onClipboardUpdate(clipboardString);
				}
			}
			previousClipBoard = clipboardString;

			// Assign the new content to clipboard
			regainClipboardOwnership(clipboard);
			
		} catch (Exception e) {
			MainWindow.consoleLog("Clipboard Listener has crashed, restarting listener.");
			MainWindow.consoleLog("Please PAUSE in between copy operations!");
			System.out.println(e);

			// Show dialog to inform user to clipboard listener has crashed
			for(ClipboardInterface listener : listeners)
				listener.onClipboardListenerCrash();
			
			restartClipboardListener();
		}
	}

	private void restartClipboardListener() {
		try {
			// Pause before reassign clipboard (restart listener)
			Thread.sleep(DELAY); 

			// Assign the new content to clipboard
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			regainClipboardOwnership(clipboard);

		} catch (Exception deadException) {
			MainWindow.consoleLog("Unable to restart clipboard listener." + 
				" Please restart application and try again!");
			System.out.println(deadException);

			// Show dialog to prompt user to restart program
			for(ClipboardInterface listener : listeners)
				listener.onClipboardListenerDied();
		}
	}

	private void regainClipboardOwnership(Clipboard clipboard) {
		Transferable trans = clipboard.getContents(this);
		clipboard.setContents(trans, this);
	}
}
