package main.listeners;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import main.MainWindow;
import main.utility.PreferenceUtil;

public class WindowCloseListener implements WindowListener {
    MainWindow window;

    public WindowCloseListener(MainWindow window) {
        this.window = window;
    }
    
	@Override
	public void windowClosing(WindowEvent e) {
		PreferenceUtil.saveAllPreferences(window);
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}
}
