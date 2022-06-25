package main.listeners.file_browser;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import main.ComplierState;
import main.utility.FileUtil;
import main.utility.UiUtil;
import main.views.FileBrowser;

public class FileBrowserMouseListener implements MouseListener {
    private FileBrowser fileBrowser;
    private JPopupMenu fileBrowserPopupMenu;

    private UiUtil uiUtil;

    public FileBrowserMouseListener(FileBrowser fileBrowser,
            JPopupMenu fileBrowserPopupMenu, UiUtil uiUtil) {
        this.fileBrowser = fileBrowser;
        this.fileBrowserPopupMenu = fileBrowserPopupMenu;
        this.uiUtil = uiUtil;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JTree fileBrowserTree = fileBrowser.getTree();

        int selectedRow = fileBrowserTree.getRowForLocation(e.getX(), e.getY());
        if (selectedRow == -1) return;
        fileBrowserTree.setSelectionRow(selectedRow);

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) fileBrowserTree.getLastSelectedPathComponent();
        File file = (File) node.getUserObject();

        if (e.getClickCount() == 1) {
            // Single click detected
            if (file.isDirectory()) {
                uiUtil.handleFileBrowserFolderClick(file.getAbsolutePath());

            } else if (file.isFile()) {
                uiUtil.handleFileBrowserFileClick(file);
            }

        } else if (e.getClickCount() == 2) {
            // Double click detected: Open text file
			FileUtil.open(ComplierState.selectedFile);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Right click for popup menu
        if (e.isPopupTrigger()) {
            JTree fileBrowserTree = fileBrowser.getTree();
            
            int selectedRow = fileBrowserTree.getRowForLocation(e.getX(), e.getY());
            if (selectedRow == -1) return;

            fileBrowserTree.setSelectionRow(selectedRow);

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) fileBrowserTree.getLastSelectedPathComponent();
            ComplierState.selectedFile = (File) node.getUserObject();

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
