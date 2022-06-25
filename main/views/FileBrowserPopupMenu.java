package main.views;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

public class FileBrowserPopupMenu extends JPopupMenu {
    public JMenuItem openMenuItem = new JMenuItem("Open");
    public JMenuItem renameMenuItem = new JMenuItem("Rename");
    public JMenuItem deleteMenuItem = new JMenuItem("Delete");

    public FileBrowserPopupMenu() {
        createPopupMenu();
    }

    private void createPopupMenu() {
        add(openMenuItem);
        add(new JSeparator());
        add(renameMenuItem);
        add(new JSeparator());
        add(deleteMenuItem);
    }
}
