package main.listeners.file_browser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import main.ComplierState;
import main.misc.ActionCommands;
import main.utility.FileUtil;
import main.utility.UiUtil;

public class FilePopupMenuListener implements ActionListener {
    UiUtil uiUtil;

    public FilePopupMenuListener(UiUtil uiUtil) {
        this.uiUtil = uiUtil;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals(ActionCommands.OPEN_FILE)) {
			FileUtil.open(ComplierState.selectedFile, false);
            
        } else if (command.equals(ActionCommands.OPEN_NOTEPAD)) {
			FileUtil.open(ComplierState.selectedFile, true);

        } else if (command.equals(ActionCommands.RENAME_FILE)) {
			uiUtil.renameFile(ComplierState.selectedFile);

        } else if (command.equals(ActionCommands.DELETE_FILE)) {
            uiUtil.deleteFile(ComplierState.selectedFile);
        }
    }
}
