package main.utility;

import java.io.File;

import javax.swing.text.BadLocationException;

import main.ComplierState;
import main.MainWindow;

public class UiUtil {
    MainWindow mainWindow;
    ComplierState state;

    public UiUtil(MainWindow mainWindow, ComplierState state) {
        this.mainWindow = mainWindow;
        this.state = state;
    }

    /**
     * Called after file is saved.
     * Increment number in <code>numberTextField</code>
     */
    public void incrementNumberTextField() {
        if (!mainWindow.numberTextField.getText().isEmpty()) {
            try {
                int number = Integer.valueOf(mainWindow.numberTextField.getText());
                number++;
                mainWindow.numberTextField.setText(Integer.toString(number));
                updateFileNameTextField();

            } catch (NumberFormatException error) {
                error.printStackTrace();
                MainWindow.consoleLog("Error incrementing file number");
            }
        }
    }

    /**
     * Gets file number and pad with leading zeros and combine with leading 
     * and trailing to show file name in <code>fileNameTextField</code>
     */
    public void updateFileNameTextField() {
        String number = mainWindow.numberTextField.getText().trim();
		String limit = mainWindow.leadingZerosTextField.getText().trim();

		if (!limit.equals("") && !limit.equals("0")) {
			try {
				int limitInt = Integer.valueOf(limit);
				int numberInt = Integer.valueOf(number);
				limit = String.valueOf(limitInt);

				String newFormat = "%0" + limit + "d";
				number = String.format(newFormat, numberInt);

			} catch (NumberFormatException e) {
				e.printStackTrace();
                MainWindow.consoleLog("Error updating file name!");
			}
		}

		String leading = mainWindow.leadingTextField.getText();
		String trailing = mainWindow.trailingTextField.getText();
		if (number.isEmpty()) {
			mainWindow.fileNameTextField.setText(leading + " " + trailing);
		} else {
			mainWindow.fileNameTextField.setText(leading + " " + number + " " + trailing);
		}
    }
    
    /**
     * Update file browser with file directory from 
     * <code>outputFolderTextField</code>. Checks if directory path 
     * exists before executing refresh and updating state file variables
     */
    public void updateFileBrowser() {
		String tempPath = mainWindow.outputFolderTextField.getText();
		File tempFile = new File(tempPath);

		if (tempFile.exists() && tempFile.isDirectory()) {
			state.selectedFile = null;
			state.currentDirectory = tempFile;
			state.parentDirectory = tempFile.getParentFile();

			mainWindow.fileBrowser.buildTreeFromPath(tempPath);
		}
	}

    public void handleClipboardUpdate(String clipboardText) {
        // Do not handle clipboard changes if NOT tracking
        if (!state.isTracking) return;

        String currentText = mainWindow.currentFileTextArea.getText();

		if (currentText.isEmpty())
			currentText += clipboardText;
		else
			currentText += "\n" + clipboardText;

        mainWindow.currentFileTextArea.setText(currentText);
        mainWindow.clipBoardTextArea.setText(clipboardText);

        // Update cursor position to start of last line to show textArea contents
        int lastLineNumber = mainWindow.currentFileTextArea.getLineCount() - 1;
        try {
            int lineStartCaretPosition = 
                mainWindow.currentFileTextArea.getLineStartOffset(lastLineNumber);
            mainWindow.currentFileTextArea.setCaretPosition(lineStartCaretPosition);

        } catch (BadLocationException e) {
            e.printStackTrace();
        }

		// Save file if clipboard has multiple lines
		if (state.multiLineAutosave && clipboardText.contains("\n")) {
			saveFile();
		}
    }

    public void renameFile() {
        File file = state.selectedFile;
        boolean renameSuccess = FileUtil.rename(mainWindow, file);
        if (renameSuccess) updateFileBrowser();
    }

    public void saveFile() {
        String fileDirectory = mainWindow.outputFolderTextField.getText();
		String fileName = mainWindow.fileNameTextField.getText();
        String currentText = mainWindow.currentFileTextArea.getText();

        boolean saveSuccess = FileUtil.save(fileDirectory, fileName, currentText);
        if (saveSuccess) {
            // Reset current file text
            mainWindow.currentFileTextArea.setText("");

            // Update file browser to show new file
            updateFileBrowser();

            incrementNumberTextField();
        }
    }
}
