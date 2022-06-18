package main.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultCaret;
import javax.swing.text.PlainDocument;

import main.MainWindow;
import main.utility.FileUtil;
import main.utility.UiUtil;
import main.ComplierState;
import main.IntegerFilter;

public class ListenerController implements ActionListener,
DocumentListener, ItemListener, MyCustomListeners {
    MainWindow mainWindow;
    ComplierState state;
    UiUtil uiUtil;

    public ListenerController(MainWindow mainWindow, ComplierState state, UiUtil uiUtil) {
        this.mainWindow = mainWindow;
        this.state = state;
        this.uiUtil = uiUtil;

        setupCustomListeners();
        setupListeners();
    }

    private void setupCustomListeners() {
        mainWindow.fileBrowser.addTreeListener(this);

        ClipboardListener board = new ClipboardListener();
		board.addClipBoardListener(this);
		board.start();
    }

    private void setupListeners() {
        // Updates file explorer tree whenever output folder text field changes
		mainWindow.outputFolderTextField.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent ev) {
				uiUtil.updateFileBrowser();
			}

			public void removeUpdate(DocumentEvent ev) {
				uiUtil.updateFileBrowser();
			}

			public void changedUpdate(DocumentEvent ev) {
			}
		});

		mainWindow.startStopButton.setActionCommand("Start");
		mainWindow.startStopButton.addActionListener(this);

		mainWindow.fileChooserButton.setActionCommand("Choose Folder");
		mainWindow.fileChooserButton.addActionListener(this);

		mainWindow.backButton.setActionCommand("Back");
		mainWindow.backButton.addActionListener(this);

		mainWindow.openButton.setActionCommand("Open File");
		mainWindow.openButton.addActionListener(this);

		mainWindow.renameButton.setActionCommand("Rename File");
		mainWindow.renameButton.addActionListener(this);

		mainWindow.showExplorerButton.setActionCommand("Show Explorer");
		mainWindow.showExplorerButton.addActionListener(this);

		mainWindow.saveButton.setActionCommand("Save");
		mainWindow.saveButton.addActionListener(this);

        mainWindow.leadingTextField.getDocument().addDocumentListener(this);
		mainWindow.numberTextField.getDocument().addDocumentListener(this);
		mainWindow.trailingTextField.getDocument().addDocumentListener(this);
		mainWindow.leadingZerosTextField.getDocument().addDocumentListener(this);

		PlainDocument numberDoc = (PlainDocument) mainWindow.numberTextField.getDocument();
		PlainDocument zerosDoc = (PlainDocument) mainWindow.leadingZerosTextField.getDocument();
		numberDoc.setDocumentFilter(new IntegerFilter(false));
		zerosDoc.setDocumentFilter(new IntegerFilter(true));

		mainWindow.autoSaveCheckBox.addItemListener(this);

		mainWindow.clipBoardTextArea.setEditable(false);
		MainWindow.console.setEditable(false);
        
		DefaultCaret consoleCaret = (DefaultCaret) MainWindow.console.getCaret();
		consoleCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
		Object source = e.getItemSelectable();
		if (source == mainWindow.autoSaveCheckBox) {
			state.multiLineAutosave = e.getStateChange() == 1;
		}
	}

    @Override
    public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("Start")) {
			state.isTracking = true;
			mainWindow.statusLabel.setText("Status: Tracking");

			mainWindow.startStopButton.setText("Stop");
			mainWindow.startStopButton.setActionCommand("Stop");

			System.out.println("Start tracking clipboard changes...");
			MainWindow.consoleLog("Start tracking clipboard changes...");

		} else if (command.equals("Stop")) {
			state.isTracking = false;
			mainWindow.statusLabel.setText("Status: Idle");

			mainWindow.startStopButton.setText("Start");
			mainWindow.startStopButton.setActionCommand("Start");

			System.out.println("Stop tracking clipboard changes...");
			MainWindow.consoleLog("Stop tracking clipboard changes...");

		} else if (command.equals("Choose Folder")) {
			String filePath = FileUtil.chooseFolder(mainWindow);
            mainWindow.outputFolderTextField.setText(filePath);

		} else if (command.equals("Back")) {
			if (state.parentDirectory == null || !state.parentDirectory.exists())
				return;
			mainWindow.outputFolderTextField.setText(state.parentDirectory.getAbsolutePath());

		} else if (command.equals("Show Explorer")) {
			FileUtil.showFileExplorer(state.currentDirectory);

		} else if (command.equals("Open File")) {
			FileUtil.open(state.selectedFile);

		} else if (command.equals("Rename File")) {
			uiUtil.renameFile();

		} else if (command.equals("Save")) {
			uiUtil.saveFile();
		}
	}

    @Override
	// DocumentListener methods for file name related text fields
	public void insertUpdate(DocumentEvent ev) {
		uiUtil.updateFileNameTextField();
	}

    @Override
	public void removeUpdate(DocumentEvent ev) {
		uiUtil.updateFileNameTextField();
	}

    @Override
	public void changedUpdate(DocumentEvent ev) {
	}

    @Override
    public void onClipboardUpdate(String data) {
        uiUtil.handleClipboardUpdate(data);
    }

    @Override
    public void onFileBrowserItemClick(File selectedFile) {
        state.selectedFile = selectedFile;
    }

    // Updates outputFolderTextField which will update file explorer tree
    @Override
    public void onFileBrowserFolderClick(String path) {
        mainWindow.outputFolderTextField.setText(path);
    }
}
