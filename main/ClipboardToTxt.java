package main;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ClipboardToTxt {

	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
		} catch (ClassNotFoundException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}

		new ClipboardToTxt();
	}

	
}
