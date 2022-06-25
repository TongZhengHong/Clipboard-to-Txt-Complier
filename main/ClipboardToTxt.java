package main;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import main.listeners.ListenerController;
import main.utility.PreferenceUtil;
import main.utility.UiUtil;

public class ClipboardToTxt {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
		} catch (ClassNotFoundException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final MainWindow mainWindow = new MainWindow("Clipboard To Txt");
		
				PreferenceUtil.loadPreferences(mainWindow);
		
				UiUtil uiUtil = new UiUtil(mainWindow);
				new ListenerController(mainWindow, uiUtil);
            }
        });
	}
}
