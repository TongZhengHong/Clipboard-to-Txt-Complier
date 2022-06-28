package main.views;

import java.awt.event.MouseListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import main.ComplierState;
import main.misc.FileNameComparator;

import javax.swing.filechooser.FileSystemView;

public class FileBrowser extends JScrollPane {
    public final static int NAME_ASCENDING = 0;
    public final static int NAME_DESCENDING = 1;
    public final static int DATE_ASCENDING = 2;
    public final static int DATE_DESCENDING = 3;

	private JTree tree;
	private FileSystemView fileSystemView = FileSystemView.getFileSystemView();

    private MouseListener mouseListener;

    public FileBrowser(String path) {
        buildTreeFromPath(path);
    }

    public void buildTreeFromPath(String path) {
        buildTreeFromPath(path, ComplierState.fileSortBy);
    }
	
	public void buildTreeFromPath(String path, int sortBy) {
        if (path == null || path.isEmpty()) return;

		File currentDir = new File(path);
        if (!currentDir.exists() || !currentDir.isDirectory()) return;

		File[] root = { currentDir };
		buildTree(root, sortBy);
	}
	
	private void buildTree(File[] roots, int sortBy) {		
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

            if (sortBy == NAME_ASCENDING) {
                Collections.sort(filesList, new FileNameComparator(true));
                Collections.sort(foldersList, new FileNameComparator(true));

            } else if (sortBy == NAME_DESCENDING) {
                Collections.sort(filesList, new FileNameComparator(false));
                Collections.sort(foldersList, new FileNameComparator(false));

            } else if (sortBy == DATE_ASCENDING) {
                Collections.sort(
                    filesList, 
                    (first, second) -> Long.compare(first.lastModified(), second.lastModified()));
                Collections.sort(
                    foldersList, 
                    (first, second) -> Long.compare(first.lastModified(), second.lastModified()));

            } else if (sortBy == DATE_DESCENDING) {
                Collections.sort(
                    filesList, 
                    (first, second) -> Long.compare(second.lastModified(), first.lastModified()));
                Collections.sort(
                    foldersList, 
                    (first, second) -> Long.compare(second.lastModified(), first.lastModified()));
            }

            if (sortBy == NAME_ASCENDING || sortBy == DATE_ASCENDING) {
                for (File file : foldersList) 
                    node.add(new DefaultMutableTreeNode(file));
                for (File file : filesList) 
                    node.add(new DefaultMutableTreeNode(file));
            } else {
                for (File file : filesList) 
                    node.add(new DefaultMutableTreeNode(file));
                for (File file : foldersList) 
                    node.add(new DefaultMutableTreeNode(file));
            }
		}

		tree = new JTree(treeModel);
		tree.setRootVisible(false);
		// tree.setVisibleRowCount(15);
		tree.setCellRenderer(new FileItemRenderer());
		tree.expandRow(0);

        // Re-initialise previous mouse listener
        if (mouseListener != null) {
            tree.addMouseListener(mouseListener);
        }
		
		this.setViewportView(tree);
	}

    public void addTreeMouseListener(MouseListener listener) {
        mouseListener = listener;
        tree.addMouseListener(listener);
    }

    public JTree getTree() {
        return tree;
    }
}
