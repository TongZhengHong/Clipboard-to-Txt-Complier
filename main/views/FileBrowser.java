package main.views;
import java.io.*;

import java.util.List;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileSystemView;

import main.listeners.FileBrowserMouseListener;
import main.listeners.MyCustomListeners;

public class FileBrowser extends JScrollPane implements TreeSelectionListener {
	private JTree tree;
	private FileSystemView fileSystemView;
    private FileBrowserPopupMenu fileBrowserPopupMenu = new FileBrowserPopupMenu();
	
	private List<MyCustomListeners> listeners = new ArrayList<MyCustomListeners>();
	
	public void addTreeListener(MyCustomListeners listener) {
		listeners.add(listener);
	}

    public FileBrowser(String path) {
		fileSystemView = FileSystemView.getFileSystemView();
        buildTreeFromPath(path);
    }
	
	public void buildTreeFromPath(String path) {
        if (path == null || path.isEmpty()) return;
		File currentDir = new File(path);
        if (!currentDir.exists() || !currentDir.isDirectory()) return;

		File[] root = { currentDir };
		buildTree(root, true);
	}
	
	private void buildTree(File[] roots, boolean populateFolder) {		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode();
		DefaultTreeModel treeModel = new DefaultTreeModel(root);
		
		// show the file system roots.
		for (File fileSystemRoot : roots) {
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileSystemRoot);
			root.add(node);

			File[] files = fileSystemView.getFiles(fileSystemRoot, true);

            List<File> filesList = new ArrayList<File>();
            List<File> foldersList = new ArrayList<File>();
			for (File file : files) {
				if (file.isDirectory()) {
                    foldersList.add(file);
				} else if (file.isFile()) {
                    filesList.add(file);
				}
			}
            
            //Show folders first before files
            for (File folder : foldersList) {
                node.add(new DefaultMutableTreeNode(folder));
            }

            for (File file : filesList) {
                node.add(new DefaultMutableTreeNode(file));
            }
		}

		tree = new JTree(treeModel);
		tree.setRootVisible(false);
		tree.setVisibleRowCount(15);
		
        FileBrowserMouseListener popupListener = 
            new FileBrowserMouseListener(tree, fileBrowserPopupMenu);
        tree.addMouseListener(popupListener);
		tree.addTreeSelectionListener(this);
		tree.setCellRenderer(new FileItemRenderer());
		tree.expandRow(0);
		
		this.setViewportView(tree);
	}
	
    @Override
	public void valueChanged(TreeSelectionEvent tse){
		DefaultMutableTreeNode node =
			(DefaultMutableTreeNode) tse.getPath().getLastPathComponent();
			
		File file = (File) node.getUserObject();
        // Update ui to show that folder as root
        for(MyCustomListeners listener : listeners){
            if (file.isDirectory()) {
                listener.onFileBrowserFolderClick(file.getAbsolutePath());
            } else if (file.isFile()) {
                listener.onFileBrowserItemClick(file);
            }
        }
	}
}
