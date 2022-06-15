package main;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultCaret;
import javax.swing.text.PlainDocument;

public class MainWindow extends JFrame implements ActionListener,
    DocumentListener, ItemListener, MyCustomListeners {
        
    private Desktop desktop;
	private JFileChooser fc;
	private Preferences userPreferences;

	public static File selectedFile = null;
	public static File currentDirectory = null;
	public static File parentDirectory = null;

	public static boolean isTracking = false;
	public static boolean multiLineAutosave = true;

	// Panels
	private JPanel topPanel = new JPanel();
	private JPanel centerPanel = new JPanel();
	private JPanel bottomPanel = new JPanel();

	// Top section
	private JButton startStopButton = new JButton("Start");
	private JLabel statusLabel = new JLabel("Status: Idle");
	private JButton fileChooserButton = new JButton("Choose folder");

	// Center section
	private JButton backButton = new JButton("Back");
	private JButton openButton = new JButton("Open");
	private JButton renameButton = new JButton("Rename");
	private JButton showExplorerButton = new JButton("Show in Explorer");

	private FileExplorerTree fileExplorerTree;
	private JTextArea clipBoardTextArea = new JTextArea();
	private JTextArea currentFileTextArea = new JTextArea();

	// Bottom section
	private JCheckBox autoSaveCheckBox = new JCheckBox("Auto save file if clipboard has multiple lines");
	private JButton saveButton = new JButton("Save Manually");
	private static JTextArea console = new JTextArea(4, 50);

	// Text Fields
	private JTextField leadingZerosTextField = new JTextField();
	private JTextField numberTextField = new JTextField();
	private JTextField outputFolderTextField = new JTextField();
	private JTextField leadingTextField = new JTextField();
	private JTextField trailingTextField = new JTextField();
	private JTextField fileNameTextField = new JTextField();

	public static void consoleLog(String text) {
		String currentText = console.getText();
		if (currentText.isEmpty())
			currentText += text;
		else
			currentText += "\n" + text;
		console.setText(currentText);
	}

	public MainWindow(String title) {
        setTitle(title);
		ImageIcon icon = new ImageIcon(getClass().getResource("../images/clipboard.png"));
		setIconImage(icon.getImage());
        
		desktop = Desktop.getDesktop();
		userPreferences = Preferences.userNodeForPackage(ClipboardToTxt.class);

		fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		BoardListener b = new BoardListener();
		b.addClipBoardListener(this);
		b.start();

		retrievePrefs();
		setupListeners();
		createLayout();

		// Show the full file name for after pref retrieval
		updateFileNameTextField();
	}
    // Draws GUI layout of the program
	private void createLayout() {
		setSize(640, 480);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(new WindowListener() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("Closing window, saving constants...");
				userPreferences.put("Output Folder", outputFolderTextField.getText());
				userPreferences.put("Leading", leadingTextField.getText());
				userPreferences.put("Number", numberTextField.getText());
				userPreferences.put("Trailing", trailingTextField.getText());
				userPreferences.put("Zeros", leadingZerosTextField.getText());
				userPreferences.putBoolean("Autosave", multiLineAutosave);
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

            @Override
            public void windowOpened(WindowEvent e) {
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
		});

		// ==========================================================================================
		// TOP Panel
		// ==========================================================================================
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

		// ==========================================================================================
		// CENTER Panel
		// ==========================================================================================
		JPanel fileExplorerPanel = new JPanel();
		JPanel fileControlPanel = new JPanel();

		JPanel clipboardPanel = new JPanel();
		JPanel currentFilePanel = new JPanel();

		fileControlPanel.setLayout(new GridLayout(1, 3));
		fileControlPanel.setMaximumSize(new Dimension(
				fileControlPanel.getMaximumSize().width,
				fileControlPanel.getMinimumSize().height));

		fileExplorerPanel.setLayout(new BoxLayout(fileExplorerPanel, BoxLayout.Y_AXIS));
		clipboardPanel.setLayout(new BoxLayout(clipboardPanel, BoxLayout.Y_AXIS));
		currentFilePanel.setLayout(new BoxLayout(currentFilePanel, BoxLayout.Y_AXIS));

		fileExplorerPanel.setBorder(BorderFactory.createTitledBorder("Current Folder"));
		clipboardPanel.setBorder(BorderFactory.createTitledBorder("Clipboard"));
		currentFilePanel.setBorder(BorderFactory.createTitledBorder("Current Text File"));

		fileControlPanel.add(backButton);
		fileControlPanel.add(renameButton);
		fileControlPanel.add(openButton);

		fileExplorerPanel.add(fileControlPanel);
		fileExplorerPanel.add(fileExplorerTree);
		fileExplorerPanel.add(showExplorerButton);

		JPanel tempPanel = new JPanel();
		tempPanel.setLayout(new GridLayout(1, 1));
		tempPanel.add(showExplorerButton);

		tempPanel.setMaximumSize(new Dimension(
				tempPanel.getMaximumSize().width,
				tempPanel.getMinimumSize().height));
		fileExplorerPanel.add(tempPanel);
		
		JScrollPane clipboardScrollPane = new JScrollPane(clipBoardTextArea);
		clipboardPanel.add(clipboardScrollPane);
		
		JScrollPane currentFileScrollPane = new JScrollPane(currentFileTextArea);
		currentFilePanel.add(currentFileScrollPane);

		centerPanel.setLayout(new GridLayout(1, 3));
		centerPanel.add(fileExplorerPanel);
		centerPanel.add(clipboardPanel);
		centerPanel.add(currentFilePanel);
		centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

		// ==========================================================================================
		// BOTTOM Panel
		// ==========================================================================================
		JPanel bottomRow = new JPanel();
		JLabel fileNameLabel = new JLabel("File Name: ");

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
		bottomPanel.add(bottomRow);
		bottomPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		bottomPanel.add(consoleScroll);
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

		add(topPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
		setLocationRelativeTo(null);
		pack();
		setMinimumSize(getSize());
		setVisible(true);
	}

	// Attachs the corresponding listeners to the ui elements
	private void setupListeners() {
		// Updates file explorer tree whenever output folder text field changes
		outputFolderTextField.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent ev) {
				updateFileExplorerTree();
			}

			public void removeUpdate(DocumentEvent ev) {
				updateFileExplorerTree();
			}

			public void changedUpdate(DocumentEvent ev) {
			}
		});

		startStopButton.setActionCommand("Start");
		startStopButton.addActionListener(this);

		fileChooserButton.setActionCommand("Choose File");
		fileChooserButton.addActionListener(this);

		backButton.setActionCommand("Back");
		backButton.addActionListener(this);

		openButton.setActionCommand("Open File");
		openButton.addActionListener(this);

		renameButton.setActionCommand("Rename File");
		renameButton.addActionListener(this);

		showExplorerButton.setActionCommand("Show Explorer");
		showExplorerButton.addActionListener(this);

		saveButton.setActionCommand("Save");
		saveButton.addActionListener(this);

		if (currentDirectory == null)
			fileExplorerTree = new FileExplorerTree();
		else
			fileExplorerTree = new FileExplorerTree(currentDirectory.getAbsolutePath());
		fileExplorerTree.addTreeListener(this);

		leadingTextField.getDocument().addDocumentListener(this);
		numberTextField.getDocument().addDocumentListener(this);
		trailingTextField.getDocument().addDocumentListener(this);
		leadingZerosTextField.getDocument().addDocumentListener(this);

		PlainDocument numberDoc = (PlainDocument) numberTextField.getDocument();
		PlainDocument zerosDoc = (PlainDocument) leadingZerosTextField.getDocument();
		numberDoc.setDocumentFilter(new IntegerFilter(false));
		zerosDoc.setDocumentFilter(new IntegerFilter(true));

		clipBoardTextArea.setEditable(false);

		autoSaveCheckBox.setSelected(multiLineAutosave);
		autoSaveCheckBox.addItemListener(this);

		console.setEditable(false);
		DefaultCaret consoleCaret = (DefaultCaret) console.getCaret();
		consoleCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	}

	// ==========================================================================================
	// RETRIEVE PREFERENCES FUNCTION
	//
	// Gets all preference variables to display
	// Checks if output file stored is valid before updating
	// currentDir, parentDir and FileExplorerTree
	// ==========================================================================================
	private void retrievePrefs() {
		multiLineAutosave = userPreferences.getBoolean("Autosave", false);

		String output = userPreferences.get("Output Folder", "");
		String leading = userPreferences.get("Leading", "");
		String number = userPreferences.get("Number", "");
		String trailing = userPreferences.get("Trailing", "");
		String zeros = userPreferences.get("Zeros", "");

		leadingTextField.setText(leading);
		numberTextField.setText(number);
		trailingTextField.setText(trailing);
		leadingZerosTextField.setText(zeros);

		File temp = new File(output);
		if (temp.exists()) {
			currentDirectory = temp;
			parentDirectory = temp.getParentFile();
			outputFolderTextField.setText(output);
		}
	}

	public void itemStateChanged(ItemEvent e) {
		Object source = e.getItemSelectable();
		if (source == autoSaveCheckBox) {
			multiLineAutosave = e.getStateChange() == 1;
		}
	}

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("Start")) {
			isTracking = true;
			statusLabel.setText("Status: Tracking");

			startStopButton.setText("Stop");
			startStopButton.setActionCommand("Stop");

			System.out.println("Start tracking clipboard changes...");
			consoleLog("Start tracking clipboard changes...");

		} else if (command.equals("Stop")) {
			isTracking = false;
			statusLabel.setText("Status: Idle");

			startStopButton.setText("Start");
			startStopButton.setActionCommand("Start");

			System.out.println("Stop tracking clipboard changes...");
			consoleLog("Stop tracking clipboard changes...");

		} else if (command.equals("Choose File")) {
			int returnVal = fc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				outputFolderTextField.setText(file.getPath());
			}

		} else if (command.equals("Back")) {
			if (parentDirectory == null || !parentDirectory.exists())
				return;
			outputFolderTextField.setText(parentDirectory.getAbsolutePath());

		} else if (command.equals("Open File")) {
			if (selectedFile == null) {
				System.out.println("Please select a file to open!");
				consoleLog("Please select a file to open!");
				return;
			}

			String fileName = selectedFile.getAbsolutePath();
			int dotIndex = fileName.lastIndexOf('.');
			String fileType = (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);

			if (desktop.isSupported(Desktop.Action.OPEN)) {
				if (fileType.equals("txt")) {
					try {
						desktop.open(selectedFile);
						System.out.println("Opening: " + fileName);
						consoleLog("Opening: " + fileName);

					} catch (IOException err) {
						err.printStackTrace();
					}
				} else {
					System.out.println("Only allowed to open text (.txt) files!");
					consoleLog("Only allowed to open text (.txt) files!");
				}
			}

		} else if (command.equals("Rename File")) {
			if (selectedFile == null || !selectedFile.exists()) {
				System.out.println("Please select a file to rename!");
				consoleLog("Please select a file to rename!");
				return;
			}

			Object inputDialogResult = JOptionPane.showInputDialog(
					this,
					"New file name: ",
					"Rename File",
					JOptionPane.PLAIN_MESSAGE,
					null,
					null,
					selectedFile.getName());

			// Cancel button is clicked
			if (inputDialogResult == null)
				return;
			String renamedString = inputDialogResult.toString();

			if (renamedString.isEmpty() ||
					renamedString.equals(selectedFile.getName()))
				return;
			if (!renamedString.contains(".txt"))
				renamedString += ".txt";

			Path source = Paths.get(selectedFile.getAbsolutePath());
			try {
				Files.move(source, source.resolveSibling(renamedString));
				System.out.println("Successfully renamed file to : " + renamedString);
				consoleLog("Successfully renamed file to : " + renamedString);

				// Update File Explorer Tree to reflect renamed file
				updateFileExplorerTree();

			} catch (IOException e1) {
				System.out.println("Error renaming file to : " + renamedString);
				consoleLog("Error renaming file to : " + renamedString);
			}

		} else if (command.equals("Show Explorer")) {
			if (currentDirectory == null || !currentDirectory.exists())
				return;
			try {
				desktop.open(currentDirectory);
			} catch (IOException e1) {
				System.out.println("Error opening directory: " + currentDirectory.getAbsolutePath());
				consoleLog("Error opening directory: " + currentDirectory.getAbsolutePath());
			}

		} else if (command.equals("Save")) {
			saveFile();
		}
	}

	// DocumentListener methods for file name related text fields
	public void insertUpdate(DocumentEvent ev) {
		updateFileNameTextField();
	}

	public void removeUpdate(DocumentEvent ev) {
		updateFileNameTextField();
	}

	public void changedUpdate(DocumentEvent ev) {
	}

	// ==========================================================================================
	// UPDATE FILE EXPLORER TREE FUNCTION
	//
	// Gets output folder from text field and update file explorer
	// if directory path is valid
	// ==========================================================================================
	private void updateFileExplorerTree() {
		String tempPath = outputFolderTextField.getText();
		File tempFile = new File(tempPath);

		if (tempFile.exists() && tempFile.isDirectory()) {
			selectedFile = null;
			currentDirectory = tempFile;
			parentDirectory = tempFile.getParentFile();

			fileExplorerTree.buildTreeFromPath(tempPath);
			userPreferences.put("Output Folder", tempPath);
		}
	}

	// ==========================================================================================
	// UPDATE FILE NAME TEXT FIELD FUNCTION
	//
	// Gets file number and pad with leading zeros to form file name with
	// leading and trailing components
	// ==========================================================================================
	private void updateFileNameTextField() {
		String number = numberTextField.getText().trim();
		String limit = leadingZerosTextField.getText().trim();

		if (!limit.equals("") && !limit.equals("0")) {
			try {
				int limitInt = Integer.valueOf(limit);
				int numberInt = Integer.valueOf(number);
				limit = String.valueOf(limitInt);

				String newFormat = "%0" + limit + "d";
				number = String.format(newFormat, numberInt);

			} catch (NumberFormatException e) {
				// Ignore
			}
		}

		String leading = leadingTextField.getText();
		String trailing = trailingTextField.getText();
		if (number.isEmpty()) {
			fileNameTextField.setText(leading + " " + trailing);
		} else {
			fileNameTextField.setText(leading + " " + number + " " + trailing);
		}
	}

	// ==========================================================================================
	// SAVE FILE FUNCTION
	//
	// Checks if file directory and name not empty and file does not exists
	// before writing contents to new file. After which, reset current text,
	// update file explorer to show new file and increment file number
	// ==========================================================================================
	private void saveFile() {
		String fileDirectory = outputFolderTextField.getText();
		String fileName = fileNameTextField.getText();

		// Don't save if directory or file name is empty
		if (fileDirectory.isEmpty() || fileName.isEmpty()) {
			System.out.println("Error! Empty file name or directory. Please enter name or folder");
			consoleLog("Error! Empty file name or directory. Please enter name or folder");
			return;
		}

		String fileString = fileDirectory + "\\" + fileName + ".txt";
		File tempFile = new File(fileString);

		// Don't save if current file path exists
		if (tempFile.exists()) {
			System.out.println("Error! File already exists. Please change file name or directory");
			consoleLog("Error! File already exists. Please change file name or directory");
			return;
		}

		String currentText = currentFileTextArea.getText();
		String newLine = System.getProperty("line.separator");
		BufferedWriter outWriter;

		try {
			// Write contents to new file
			outWriter = new BufferedWriter(new FileWriter(fileString, false));
			String[] sentences = currentText.split("\\n");
			for (String line : sentences) {
				outWriter.write(line);
				outWriter.write(newLine);
			}
			outWriter.close();

			System.out.println("Successfully saved file as " + fileString);
			consoleLog("Successfully saved file as " + fileString);

			// Reset current file text
			currentFileTextArea.setText("");

			// Update file explorer to show new file
			updateFileExplorerTree();

			// Increment file number if number is given
			if (!numberTextField.getText().isEmpty()) {
				try {
					int number = Integer.valueOf(numberTextField.getText());
					number++;
					numberTextField.setText(Integer.toString(number));
					updateFileNameTextField();

				} catch (NumberFormatException error) {
					error.printStackTrace();
					consoleLog("Error incrementing file number");
				}
			}

		} catch (FileNotFoundException e) {
			System.out.println("Error with output folder: " + fileString);
			consoleLog("Error with output folder: " + fileString);

		} catch (IOException e) {
			e.printStackTrace();
			consoleLog("Error writing to file: " + fileString);
		}
	}

	// MyCustomListener callback when clipboard changes
	// Update clipboard text area with new change and autosave if neede
	public void onClipBoardUpdate(String clipboardText) {
		String currentText = currentFileTextArea.getText();

		if (currentText.isEmpty())
			currentText += clipboardText;
		else
			currentText += "\n" + clipboardText;

		currentFileTextArea.setText(currentText);
		clipBoardTextArea.setText(clipboardText);

		// Save file if clipboard has multiple lines
		if (multiLineAutosave && clipboardText.contains("\n")) {
			saveFile();
			return;
		}
	}

	// Updates outputFolderTextField which will update file explorer tree
	public void onTreeFolderClick(String path) {
		outputFolderTextField.setText(path);
	}
}
