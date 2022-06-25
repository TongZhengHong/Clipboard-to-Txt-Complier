package main.views;

import java.awt.event.MouseListener;

import java.io.File;

import java.util.List;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.filechooser.FileSystemView;

public class FileBrowser extends JScrollPane {
	private JTree tree;
	private FileSystemView fileSystemView = FileSystemView.getFileSystemView();

    private MouseListener mouseListener;

    public FileBrowser(String path) {
        buildTreeFromPath(path);
    }
	
	public void buildTreeFromPath(String path) {
        if (path == null || path.isEmpty()) return;

		File currentDir = new File(path);
        if (!currentDir.exists() || !currentDir.isDirectory()) return;

		File[] root = { currentDir };
		buildTree(root);
	}
	
	private void buildTree(File[] roots) {		
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
