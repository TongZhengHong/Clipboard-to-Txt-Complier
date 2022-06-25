package main.listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPopupMenu;
import javax.swing.JTree;

public class FileBrowserMouseListener implements MouseListener {
    private JTree fileBrowserTree;
    private JPopupMenu fileBrowserPopupMenu;

    public FileBrowserMouseListener(JTree fileBrowserTree, JPopupMenu fileBrowserPopupMenu) {
        this.fileBrowserTree = fileBrowserTree;
        this.fileBrowserPopupMenu = fileBrowserPopupMenu;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Right click for popup menu
        if (e.isPopupTrigger()) {
            int row = fileBrowserTree.getClosestRowForLocation(e.getX(), e.getY());
            fileBrowserTree.setSelectionRow(row);
            fileBrowserPopupMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
