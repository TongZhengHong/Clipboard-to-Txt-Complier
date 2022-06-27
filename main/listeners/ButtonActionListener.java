package main.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import main.ComplierState;
import main.MainWindow;
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
		if (command.equals("Start")) {
			ComplierState.isTracking = true;
			mainWindow.statusLabel.setText("Status: Tracking");

			mainWindow.startStopButton.setText("Stop");
			mainWindow.startStopButton.setActionCommand("Stop");

			System.out.println("Start tracking clipboard changes...");
			MainWindow.consoleLog("Start tracking clipboard changes...");

		} else if (command.equals("Stop")) {
			ComplierState.isTracking = false;
			mainWindow.statusLabel.setText("Status: Idle");

			mainWindow.startStopButton.setText("Start");
			mainWindow.startStopButton.setActionCommand("Start");

			System.out.println("Stop tracking clipboard changes...");
			MainWindow.consoleLog("Stop tracking clipboard changes...");

		} else if (command.equals("Choose Folder")) {
			String filePath = FileUtil.chooseFolder(mainWindow);
			if (filePath != null)
				mainWindow.outputFolderTextField.setText(filePath);

		} else if (command.equals("Back")) {
			if (ComplierState.parentDirectory == null || !ComplierState.parentDirectory.exists())
				return;
			mainWindow.outputFolderTextField.setText(ComplierState.parentDirectory.getAbsolutePath());

		} else if (command.equals("Refresh")) {
			uiUtil.updateFileBrowser();

		} else if (command.equals("Show Explorer")) {
			FileUtil.showFileExplorer(ComplierState.currentDirectory);

		} else if (command.equals("Sort by Name")) {
			// FileUtil.open(ComplierState.selectedFile);

		} else if (command.equals("Sort by Date")) {
			// uiUtil.renameFile();

		} else if (command.equals("Save File")) {
			uiUtil.saveFile();

		} else if (command.equals("Duplicate")) {
			uiUtil.duplicateClipboard();
		}
	}
}
