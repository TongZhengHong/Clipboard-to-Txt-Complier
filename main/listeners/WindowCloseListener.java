package main.listeners;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import main.ComplierState;
import main.MainWindow;
import main.utility.PreferenceUtil;

public class WindowCloseListener implements WindowListener {
    MainWindow window;
    ComplierState state;

    public WindowCloseListener(MainWindow window, ComplierState state) {
        this.window = window;
        this.state = state;
    }
    
	@Override
	public void windowClosing(WindowEvent e) {
		PreferenceUtil.saveAllPreferences(window, state);
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
