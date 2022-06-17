package main.views;
import java.io.*;

import java.util.List;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileSystemView;

import main.MainWindow;
import main.listeners.MyCustomListeners;

public class FileBrowser extends JScrollPane implements TreeSelectionListener {
	private JTree tree;
	private FileSystemView fileSystemView;
	
	private List<MyCustomListeners> listeners = new ArrayList<MyCustomListeners>();
	
	public void addTreeListener(MyCustomListeners listener) {
		listeners.add(listener);
	}
	
	public FileBrowser() {
		fileSystemView = FileSystemView.getFileSystemView();
		File[] root = fileSystemView.getRoots();
        MainWindow.currentDirectory = root[0];
		buildTree(root, false);
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

            boolean hasDirectory = false;
            List<File> justFiles = new ArrayList<File>();
			for (File file : files) {
				if (file.isDirectory()) {
                    hasDirectory = true;
					node.add(new DefaultMutableTreeNode(file));
					
				} else if (file.isFile()) {
                    justFiles.add(file);
				}
			}

            // Show files after folders are added
            if (!hasDirectory) {
                for (File file : justFiles) {
                    node.add(new DefaultMutableTreeNode(file));
                }
            }
		}

		tree = new JTree(treeModel);
		tree.setRootVisible(false);
		tree.setVisibleRowCount(15);
		
		tree.addTreeSelectionListener(this);
		tree.setCellRenderer(new FileItemRenderer());
		tree.expandRow(0);
		
		this.setViewportView(tree);
	}
	
	private void showChildren(final DefaultMutableTreeNode node) {
        tree.setEnabled(false);

        SwingWorker<Void, File> worker = new SwingWorker<Void, File>() {
            @Override
            public Void doInBackground() {
                File file = (File) node.getUserObject();
                if (file.isDirectory()) {
                    File[] files = fileSystemView.getFiles(file, true); //!!
                    if (node.isLeaf()) {
                        for (File child : files) {
							publish(child);
                        }
                    }
                }
                return null;
            }

            @Override
            protected void process(List<File> chunks) {
                for (File child : chunks) {
                    node.add(new DefaultMutableTreeNode(child));
                }
            }

            @Override
            protected void done() {
                tree.setEnabled(true);
            }
        };
		
        worker.execute();
    }
	
	public void valueChanged(TreeSelectionEvent tse){
		DefaultMutableTreeNode node =
			(DefaultMutableTreeNode) tse.getPath().getLastPathComponent();
			
		File file = (File) node.getUserObject();
		if (file.isDirectory()) {
			showChildren(node);
			
            // Update ui to show that folder as root
			for(MyCustomListeners listener : listeners){
				listener.onTreeFolderClick(file.getAbsolutePath());
			}
            
			MainWindow.selectedFile = null;
            MainWindow.currentDirectory = file;
            MainWindow.parentDirectory = file.getParentFile();
			
		} else if (file.isFile()) {
			MainWindow.selectedFile = file;
		}
	}
}
