package main.utility;

import java.awt.Component;

import java.io.File;

import javax.swing.JOptionPane;
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

        boolean isNumberEmpty = number.isEmpty();
        boolean isLimitEmpty = limit.equals("");
        boolean isLimitZero = limit.equals("0");

        if (!isNumberEmpty && !isLimitEmpty && !isLimitZero) {
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

    private void handleClipboardUpdate(String clipboardText, boolean checkAutoSave) {
        // Do not handle clipboard changes if NOT tracking
        if (!state.isTracking)
            return;

        state.previousClipboard = clipboardText;

        String currentText = mainWindow.currentFileTextArea.getText();

        if (currentText.isEmpty())
            currentText += clipboardText;
        else
            currentText += "\n" + clipboardText;

        mainWindow.currentFileTextArea.setText(currentText);
        mainWindow.clipboardTextArea.setText(clipboardText);

        // Update cursor position to start of last line to show textArea contents
        int lastLineNumber = mainWindow.currentFileTextArea.getLineCount() - 1;
        try {
            int lineStartCaretPosition = mainWindow.currentFileTextArea.getLineStartOffset(lastLineNumber);
            mainWindow.currentFileTextArea.setCaretPosition(lineStartCaretPosition);

        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        // Save file if clipboard has multiple lines
        if (checkAutoSave && state.multiLineAutosave) {
            if (clipboardText.contains("\n"))
                saveFile();
        }
    }

    public void handleClipboardUpdate(String clipboardText) {
        handleClipboardUpdate(clipboardText, true);
    }

    public void duplicateClipboard() {
        String previousClipboard = mainWindow.clipboardTextArea.getText();

        if (previousClipboard.trim().isEmpty()) {
            System.out.println("Previous clipboard is empty!");
            MainWindow.consoleLog("Previous clipboard is empty!");
        } else
            // Do not check autosave, duplicate previous clipboard even with newline
            handleClipboardUpdate(state.previousClipboard, false);
    }

    private boolean showConfirmSaveDialog(Component context) {
        int confirmDialogResult = JOptionPane.showConfirmDialog(
                context,
                "File name already exists! Would you like to override the existing file?",
                "Save File",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        return confirmDialogResult == JOptionPane.YES_OPTION;
    }

    private String showRenameDialog(Component context, File originalFile) {

        Object inputDialogResult = JOptionPane.showInputDialog(
                context,
                "New file name: ",
                "Rename File",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                originalFile.getName());

        // Cancel button is clicked
        if (inputDialogResult == null)
            return null;

        String renamedString = inputDialogResult.toString();

        if (renamedString.isEmpty() || renamedString.equals(originalFile.getName()))
            return null;

        if (!renamedString.contains(".txt"))
            renamedString += ".txt";

        return renamedString;
    }

    /**
     * Solicits new file name from user via InputDialog. Update
     * <code>fileBrowser</code> only file renamed successfully to
     * show newly updated file name.
     * 
     * <p>
     * Only allows text (.txt) files to be renamed.
     * </p>
     */
    public void renameFile() {
        File file = state.selectedFile;

        if (file == null || !file.exists()) {
            System.out.println("Please select a file to rename!");
            MainWindow.consoleLog("Please select a file to rename!");
            return;
        }

        if (!FileUtil.isTxtFile(file)) {
            System.out.println("Only allowed to rename text (.txt) files!");
            MainWindow.consoleLog("Only allowed to rename text (.txt) files!");
            return;
        }

        String renamedFileName = showRenameDialog(mainWindow, file);
        // User cancel, empty file name or same file name
        if (renamedFileName == null)
            return;

        boolean renameSuccess = FileUtil.rename(file, renamedFileName);
        if (renameSuccess)
            updateFileBrowser();
    }

    /**
     * <p>
     * Save text in <code>currentFileTextArea</code> to valid current directory
     * and under user input file name. If file name already exists in current
     * directory, confirm with user to override file or abort save operation.
     * </p>
     * 
     * <p>
     * Once file is saved successfully, reset <code>currentFileTextArea</code>
     * to empty, refresh file browser to show newly added file and increment
     * <code>numberTextField</code>.
     * </p>
     */
    public void saveFile() {
        String fileDirectory = mainWindow.outputFolderTextField.getText();
        String fileName = mainWindow.fileNameTextField.getText().trim();
        String currentText = mainWindow.currentFileTextArea.getText();

        // Don't save if directory or file name is empty
        if (fileDirectory.isEmpty() || fileName.isEmpty()) {
            System.out.println("Error! Empty file name or directory. Please enter name or folder");
            MainWindow.consoleLog("Error! Empty file name or directory. Please enter name or folder");
            return;
        }

        String filePathString = fileDirectory + "\\" + fileName + ".txt";
        File tempFile = new File(filePathString);

        // Ask user to override if there is an existing file
        if (tempFile.exists()) {
            boolean confirmSave = showConfirmSaveDialog(mainWindow);

            // User chooses NOT to override file, abort saving file
            if (!confirmSave) {
                System.out.println("Abort overriding existing file!");
                MainWindow.consoleLog("Abort overriding existing file!");
                return;
            }
        }

        boolean saveSuccess = FileUtil.save(filePathString, currentText);
        if (saveSuccess) {
            // Reset current file text
            mainWindow.currentFileTextArea.setText("");

            // Update file browser to show new file
            updateFileBrowser();

            incrementNumberTextField();
        }
    }
}
