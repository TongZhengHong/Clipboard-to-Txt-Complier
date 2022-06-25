package main.listeners.clipboard;

import java.util.List;
import java.util.ArrayList;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

public class ClipboardListener extends Thread implements ClipboardOwner {
	private String previousClipBoard = "";
	private List<ClipboardInterface> listeners = new ArrayList<ClipboardInterface>();
	private Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
	
	public void addClipBoardListener(ClipboardInterface listener) {
		listeners.add(listener);
	}
  
	public void run() {
		Transferable trans = sysClip.getContents(this);
		regainOwnership(trans);
		while (true) {}
	}
  
	public void lostOwnership(Clipboard c, Transferable t) {
		try { 
			//Add delay to prevent exception
			Thread.sleep(300);
			
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
		Transferable contents = sysClip.getContents(this); //EXCEPTION
		processContents(contents);
		regainOwnership(contents);
	}
	
	void processContents(Transferable t) {
		try {
			String data = (String) Toolkit.getDefaultToolkit()
				.getSystemClipboard().getData(DataFlavor.stringFlavor); 
			
			// Only update if clipboard content changes
			if (!data.equals(previousClipBoard)) {
				for(ClipboardInterface listener : listeners){
					listener.onClipboardUpdate(data);
				}
			}
			
			previousClipBoard = data;

		} catch (Exception e) {
			System.out.println("Exception: " + e); 
		}
	}
	  
	void regainOwnership(Transferable t) {
		sysClip.setContents(t, this);
	}
}
