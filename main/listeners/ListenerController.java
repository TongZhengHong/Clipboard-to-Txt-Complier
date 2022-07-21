package main.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.text.PlainDocument;

import main.MainWindow;
import main.listeners.clipboard.ClipboardInterface;
import main.listeners.clipboard.ClipboardListener;
import main.listeners.file_browser.FileBrowserMouseListener;
import main.listeners.file_browser.FilePopupMenuListener;
import main.listeners.text_field.FileNameDocumentListener;
import main.listeners.text_field.OutputFolderDocumentListener;
import main.misc.IntegerFilter;
import main.utility.DialogUtil;
import main.utility.UiUtil;
import main.ComplierState;

public class ListenerController implements ClipboardInterface {
	MainWindow mainWindow;
	UiUtil uiUtil;

	public ListenerController(MainWindow mainWindow, UiUtil uiUtil) {
		this.mainWindow = mainWindow;
		this.uiUtil = uiUtil;

		// Start listening to clipboard changes
		ClipboardListener board = new ClipboardListener();
		board.addClipBoardListener(this);
		board.start();

		setupListeners();
	}

	private void setupListeners() {
		mainWindow.addWindowListener(new WindowCloseListener(mainWindow));

		buttonListeners();

		textFieldListeners();

		fileBrowserListeners();

		checkBoxListeners();
	}

	@Override
	public void onClipboardUpdate(String data) {
		uiUtil.handleClipboardUpdate(data);
	}

	@Override
	public void onClipboardListenerCrash() {
		DialogUtil.showClipboardListenerCrashDialog(mainWindow);
	}

	@Override
	public void onClipboardListenerDied() {
		DialogUtil.showClipboardListenerDiedDialog(mainWindow);
	}

	private void buttonListeners() {
		ButtonActionListener buttonActionListener = new ButtonActionListener(mainWindow, uiUtil);

		mainWindow.startStopButton.setMnemonic(KeyEvent.VK_S);
		mainWindow.startStopButton.setActionCommand("Start");
		mainWindow.startStopButton.addActionListener(buttonActionListener);

		mainWindow.fileChooserButton.setMnemonic(KeyEvent.VK_C);
		mainWindow.fileChooserButton.setActionCommand("Choose Folder");
		mainWindow.fileChooserButton.addActionListener(buttonActionListener);

		mainWindow.backButton.setMnemonic(KeyEvent.VK_B);
		mainWindow.backButton.setActionCommand("Back");
		mainWindow.backButton.addActionListener(buttonActionListener);

		mainWindow.refreshButton.setMnemonic(KeyEvent.VK_R);
		mainWindow.refreshButton.setActionCommand("Refresh");
		mainWindow.refreshButton.addActionListener(buttonActionListener);

		mainWindow.sortByNameButton.setActionCommand("Sort by Name");
		mainWindow.sortByNameButton.addActionListener(buttonActionListener);

		mainWindow.sortByDateButton.setActionCommand("Sort by Date");
		mainWindow.sortByDateButton.addActionListener(buttonActionListener);

		mainWindow.showExplorerButton.setMnemonic(KeyEvent.VK_E);
		mainWindow.showExplorerButton.setActionCommand("Show Explorer");
		mainWindow.showExplorerButton.addActionListener(buttonActionListener);

		mainWindow.duplicateClipboardButton.setMnemonic(KeyEvent.VK_D);
		mainWindow.duplicateClipboardButton.setActionCommand("Duplicate");
		mainWindow.duplicateClipboardButton.addActionListener(buttonActionListener);

		mainWindow.saveButton.setToolTipText("Save file (Ctrl+S)");
		mainWindow.saveButton.setActionCommand("Save File");
		mainWindow.saveButton.addActionListener(buttonActionListener);

		// Add accelerator for save button
		KeyStroke keySave = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK);
		Action performSave = new AbstractAction("Save") {
			public void actionPerformed(ActionEvent e) {
				uiUtil.saveFile();
			}
		};
		mainWindow.saveButton.getActionMap().put("performSave", performSave);
		mainWindow.saveButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keySave, "performSave");
	}

	private void textFieldListeners() {
		FileNameDocumentListener fileNameDocumentListener = new FileNameDocumentListener(uiUtil);
		OutputFolderDocumentListener outputFolderDocumentListener = new OutputFolderDocumentListener(uiUtil);

		// Combines leading, number, trailing and leading zeroes text fields to form file name
		mainWindow.leadingTextField.getDocument().addDocumentListener(fileNameDocumentListener);
		mainWindow.numberTextField.getDocument().addDocumentListener(fileNameDocumentListener);
		mainWindow.trailingTextField.getDocument().addDocumentListener(fileNameDocumentListener);
		mainWindow.leadingZerosTextField.getDocument().addDocumentListener(fileNameDocumentListener);

		// Updates file explorer tree whenever output folder text field changes
		mainWindow.outputFolderTextField.getDocument().addDocumentListener(outputFolderDocumentListener);

		// Add IntegerFilter to ensure only numbers can be entered
		PlainDocument numberDoc = (PlainDocument) mainWindow.numberTextField.getDocument();
		PlainDocument zerosDoc = (PlainDocument) mainWindow.leadingZerosTextField.getDocument();
		numberDoc.setDocumentFilter(new IntegerFilter(false));
		zerosDoc.setDocumentFilter(new IntegerFilter(true));

		// Set editable field for text fields
		mainWindow.fileViewerTextArea.setEditable(false);
		mainWindow.clipboardTextArea.setEditable(false);
		MainWindow.console.setEditable(false);
	}

	private void fileBrowserListeners() {
		// Detect single & double clicks and right clicks on fileBrowser
		mainWindow.fileBrowser.addTreeMouseListener(new FileBrowserMouseListener(
				mainWindow.fileBrowser,
				mainWindow.fileBrowserPopupMenu,
				uiUtil));

		FilePopupMenuListener popupMenuListener = new FilePopupMenuListener(uiUtil);

		mainWindow.fileBrowserPopupMenu.openMenuItem.setActionCommand("open");
		mainWindow.fileBrowserPopupMenu.openMenuItem.addActionListener(popupMenuListener);

		mainWindow.fileBrowserPopupMenu.openNotepadMenuItem.setActionCommand("open notepad");
		mainWindow.fileBrowserPopupMenu.openNotepadMenuItem.addActionListener(popupMenuListener);

		mainWindow.fileBrowserPopupMenu.renameMenuItem.setActionCommand("rename");
		mainWindow.fileBrowserPopupMenu.renameMenuItem.addActionListener(popupMenuListener);

		mainWindow.fileBrowserPopupMenu.deleteMenuItem.setActionCommand("delete");
		mainWindow.fileBrowserPopupMenu.deleteMenuItem.addActionListener(popupMenuListener);
	}

	private void checkBoxListeners() {
		mainWindow.incrementCheckBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				Object source = e.getItemSelectable();
				if (source == mainWindow.incrementCheckBox) {
					ComplierState.incrementNumber = e.getStateChange() == 1;
				}
			}
		});
	}
}
