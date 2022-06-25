package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileSystemView;

import main.utility.PreferenceUtil;
import main.utility.UiUtil;
import main.views.FileBrowser;

public class MainWindow extends JFrame implements WindowListener {

	// Top section
	public JButton startStopButton = new JButton("Start");
	public JLabel statusLabel = new JLabel("Status: Idle");
	public JButton fileChooserButton = new JButton("Choose folder");

	// Center section
	public JButton backButton = new JButton("Back");
	public JButton refreshButton = new JButton("Refresh");
	public JButton openButton = new JButton("Open");
	public JButton renameButton = new JButton("Rename");
	public JButton showExplorerButton = new JButton("Show in Explorer");
	public JButton duplicateClipboardButton = new JButton("Duplicate Clipboard");

	public FileBrowser fileBrowser;
	public JTextArea fileViewerTextArea = new JTextArea();
	public JTextArea currentFileTextArea = new JTextArea();

	// Bottom section
	public JTextArea clipboardTextArea = new JTextArea(2, 80);
	public JCheckBox autoSaveCheckBox = new JCheckBox("Auto save file if clipboard has multiple lines");
	public JButton saveButton = new JButton("Save Manually");
	public static JTextArea console = new JTextArea(4, 80);

	// Text Fields
	public JTextField leadingZerosTextField = new JTextField();
	public JTextField numberTextField = new JTextField();
	public JTextField outputFolderTextField = new JTextField();
	public JTextField leadingTextField = new JTextField();
	public JTextField trailingTextField = new JTextField();
	public JTextField fileNameTextField = new JTextField();

	private ComplierState state;

	public MainWindow(String title, ComplierState state) {
		setTitle(title);
		this.state = state;

		ImageIcon icon = new ImageIcon(getClass().getResource("../images/clipboard.png"));
		setIconImage(icon.getImage());

		FileSystemView sys = FileSystemView.getFileSystemView();
		File[] root = sys.getRoots();
		fileBrowser = new FileBrowser(root[0].getAbsolutePath());
		state.currentDirectory = root[0];

		createLayout();
	}

	/**
	 * Logs new [text] in bottom console section
	 * 
	 * @param text
	 */
	public static void consoleLog(String text) {
		String currentText = console.getText();

		if (currentText.isEmpty())
			currentText += text;
		else
			currentText += "\n" + text;

		console.setText(currentText);
		UiUtil.bringCursorToStart(console);
	}

	// Draws GUI layout of the program
	private void createLayout() {
		setSize(640, 480);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(this);

		JPanel topPanel = createTopSection();
		JPanel centerPanel = createCenterSection();
		JPanel bottomPanel = createBottomSection();

		add(topPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
		setLocationRelativeTo(null);
		pack();
		setMinimumSize(getSize());
		setVisible(true);
	}

	// ==========================================================================================
	// TOP Panel
	// ==========================================================================================
	private JPanel createTopSection() {
		JPanel topPanel = new JPanel();

		JLabel leadingZerosLabel = new JLabel("Leading Zeros: ");
		JPanel statusPanel = new JPanel();
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		statusPanel.add(startStopButton);
		statusPanel.add(Box.createRigidArea(new Dimension(15, 0)));
		statusPanel.add(statusLabel);
		statusPanel.add(Box.createHorizontalGlue());
		statusPanel.add(leadingZerosLabel);
		leadingZerosTextField.setColumns(2);
		leadingZerosTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		leadingZerosTextField.setMaximumSize(new Dimension(
				leadingZerosTextField.getMinimumSize().width,
				leadingZerosTextField.getMaximumSize().height));
		statusPanel.add(leadingZerosTextField);

		JPanel outputFolderPanel = new JPanel();
		outputFolderPanel.setLayout(new BoxLayout(outputFolderPanel, BoxLayout.X_AXIS));
		JLabel outputFolderLabel = new JLabel("Output Folder: ");
		outputFolderPanel.add(outputFolderLabel);
		outputFolderPanel.add(outputFolderTextField);
		outputFolderPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		outputFolderPanel.add(fileChooserButton);

		JPanel textFileNamePanel = new JPanel();
		textFileNamePanel.setLayout(new BoxLayout(textFileNamePanel, BoxLayout.X_AXIS));
		JLabel leadingLabel = new JLabel("Leading: ");
		textFileNamePanel.add(leadingLabel);
		textFileNamePanel.add(leadingTextField);
		textFileNamePanel.add(Box.createRigidArea(new Dimension(10, 0)));
		JLabel numberLabel = new JLabel("Start No: ");
		textFileNamePanel.add(numberLabel);
		textFileNamePanel.add(numberTextField);
		textFileNamePanel.add(Box.createRigidArea(new Dimension(10, 0)));
		JLabel trailingLabel = new JLabel("Trailing: ");
		textFileNamePanel.add(trailingLabel);
		textFileNamePanel.add(trailingTextField);

		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.add(statusPanel);
		topPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		topPanel.add(outputFolderPanel);
		topPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		topPanel.add(textFileNamePanel);
		topPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

		return topPanel;
	}

	// ==========================================================================================
	// CENTER Panel
	// ==========================================================================================
	private JPanel createCenterSection() {
		JPanel centerPanel = new JPanel();

		JPanel fileBrowserPanel = new JPanel();
		JPanel fileControlPanel = new JPanel();

		JPanel clipboardPanel = new JPanel();
		JPanel currentFilePanel = new JPanel();

		fileControlPanel.setLayout(new GridLayout(2, 2));
		fileControlPanel.setMaximumSize(new Dimension(
				fileControlPanel.getMaximumSize().width,
				fileControlPanel.getMinimumSize().height));

		fileBrowserPanel.setLayout(new BoxLayout(fileBrowserPanel, BoxLayout.Y_AXIS));
		clipboardPanel.setLayout(new BoxLayout(clipboardPanel, BoxLayout.Y_AXIS));
		currentFilePanel.setLayout(new BoxLayout(currentFilePanel, BoxLayout.Y_AXIS));

		fileBrowserPanel.setBorder(BorderFactory.createTitledBorder("Current Folder"));
		clipboardPanel.setBorder(BorderFactory.createTitledBorder("File Viewer"));
		currentFilePanel.setBorder(BorderFactory.createTitledBorder("Current Text File"));

		fileControlPanel.add(backButton);
		fileControlPanel.add(refreshButton);
		fileControlPanel.add(renameButton);
		fileControlPanel.add(openButton);

		fileBrowserPanel.add(fileControlPanel);
		fileBrowserPanel.add(fileBrowser);
		fileBrowserPanel.add(showExplorerButton);

		JPanel tempPanel = new JPanel();
		tempPanel.setLayout(new GridLayout(1, 1));
		tempPanel.add(showExplorerButton);

		tempPanel.setMaximumSize(new Dimension(
				tempPanel.getMaximumSize().width,
				tempPanel.getMinimumSize().height));
		fileBrowserPanel.add(tempPanel);

		JScrollPane fileViewerScrollPane = new JScrollPane(fileViewerTextArea);
		clipboardPanel.add(fileViewerScrollPane);

		JPanel duplicateButtonPanel = new JPanel();
		duplicateButtonPanel.setLayout(new GridLayout(1, 1));
		duplicateButtonPanel.add(duplicateClipboardButton);
		duplicateButtonPanel.setMaximumSize(new Dimension(
				duplicateButtonPanel.getMaximumSize().width,
				duplicateButtonPanel.getMinimumSize().height));

		JScrollPane currentFileScrollPane = new JScrollPane(currentFileTextArea);
		currentFilePanel.add(currentFileScrollPane);
		currentFilePanel.add(duplicateButtonPanel);

		centerPanel.setLayout(new GridLayout(1, 3));
		centerPanel.add(fileBrowserPanel);
		centerPanel.add(clipboardPanel);
		centerPanel.add(currentFilePanel);
		centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

		return centerPanel;
	}

	// ==========================================================================================
	// BOTTOM Panel
	// ==========================================================================================
	private JPanel createBottomSection() {
		JPanel bottomPanel = new JPanel();

		JPanel bottomRow = new JPanel();
		JLabel fileNameLabel = new JLabel("File Name: ");

		JScrollPane clipboardScroll = new JScrollPane();
		clipboardScroll.add(clipboardTextArea);
		clipboardScroll.setViewportView(clipboardTextArea);

		bottomRow.setLayout(new BoxLayout(bottomRow, BoxLayout.X_AXIS));
		bottomRow.add(autoSaveCheckBox);
		bottomRow.add(Box.createRigidArea(new Dimension(15, 0)));
		bottomRow.add(fileNameLabel);
		bottomRow.add(fileNameTextField);
		bottomRow.add(Box.createRigidArea(new Dimension(15, 0)));
		bottomRow.add(saveButton);

		JScrollPane consoleScroll = new JScrollPane();
		consoleScroll.add(console);
		consoleScroll.setViewportView(console);

		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
		bottomPanel.add(clipboardScroll);
		bottomPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		bottomPanel.add(bottomRow);
		bottomPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		bottomPanel.add(consoleScroll);
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

		return bottomPanel;
	}

	@Override
	public void windowClosing(WindowEvent e) {
		PreferenceUtil.saveAllPreferences(this, state);
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}
}
