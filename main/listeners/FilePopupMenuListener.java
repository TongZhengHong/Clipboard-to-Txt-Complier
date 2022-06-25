package main.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import main.utility.UiUtil;
import main.views.FileBrowserPopupMenu;

public class FilePopupMenuListener implements ActionListener {
    FileBrowserPopupMenu popupMenu;
    UiUtil uiUtil;

    public FilePopupMenuListener(FileBrowserPopupMenu popupMenu) {
        this.popupMenu = popupMenu;

        attachListeners();
    }

    private void attachListeners() {
        popupMenu.openMenuItem.setActionCommand("open");
        popupMenu.openMenuItem.addActionListener(this);

        popupMenu.renameMenuItem.setActionCommand("rename");
        popupMenu.renameMenuItem.addActionListener(this);

        popupMenu.deleteMenuItem.setActionCommand("delete");
        popupMenu.deleteMenuItem.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command == "open") {

        } else if (command == "rename") {

        } else if (command == "delete") {

        }
    }
}
