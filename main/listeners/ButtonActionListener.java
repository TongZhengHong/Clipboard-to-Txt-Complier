package main.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import main.ComplierState;
import main.MainWindow;
import main.misc.ActionCommands;
import main.utility.FileUtil;
import main.utility.UiUtil;

public class ButtonActionListener implements ActionListener {
    MainWindow mainWindow;
	UiUtil uiUtil;

    public ButtonActionListener(MainWindow mainWindow, UiUtil uiUtil) {
		this.mainWindow = mainWindow;
		this.uiUtil = uiUtil;
    }

    @Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals(ActionCommands.START)) {
			ComplierState.isTracking = true;
			mainWindow.statusLabel.setText("Status: Tracking");

			mainWindow.startStopButton.setText("Stop");
			mainWindow.startStopButton.setActionCommand(ActionCommands.STOP);

			System.out.println("Start tracking clipboard changes...");
			MainWindow.consoleLog("Start tracking clipboard changes...");

		} else if (command.equals(ActionCommands.STOP)) {
			ComplierState.isTracking = false;
			mainWindow.statusLabel.setText("Status: Idle");

			mainWindow.startStopButton.setText("Start");
			mainWindow.startStopButton.setActionCommand(ActionCommands.START);

			System.out.println("Stop tracking clipboard changes...");
			MainWindow.consoleLog("Stop tracking clipboard changes...");

		} else if (command.equals(ActionCommands.CHOOSE_FOLDER)) {
			String filePath = FileUtil.chooseFolder(mainWindow, ComplierState.currentDirectory);
			if (filePath != null)
				mainWindow.outputFolderTextField.setText(filePath);

		} else if (command.equals(ActionCommands.BACK)) {
			if (ComplierState.parentDirectory != null && ComplierState.parentDirectory.exists())
				mainWindow.outputFolderTextField.setText(ComplierState.parentDirectory.getAbsolutePath());

		} else if (command.equals(ActionCommands.REFRESH)) {
			uiUtil.updateFileBrowser();

		} else if (command.equals(ActionCommands.SHOW_EXPLORER)) {
			FileUtil.showFileExplorer(ComplierState.currentDirectory);

		} else if (command.equals(ActionCommands.SORT_BY_NAME)) {
			uiUtil.toggleSortByName();
			uiUtil.updateFileBrowser();

		} else if (command.equals(ActionCommands.SORT_BY_DATE)) {
			uiUtil.toggleSortByDate();
			uiUtil.updateFileBrowser();

		} else if (command.equals(ActionCommands.SAVE_FILE)) {
			uiUtil.saveFile();

		} else if (command.equals(ActionCommands.DUPLICATE)) {
			uiUtil.duplicateClipboard();

		} else if (command.equals(ActionCommands.SHOW_TOP)) {
			uiUtil.toggleTopBottomFileViewer(true);

		} else if (command.equals(ActionCommands.SHOW_BOTTOM)) {
			uiUtil.toggleTopBottomFileViewer(false);
		}
	}
}
