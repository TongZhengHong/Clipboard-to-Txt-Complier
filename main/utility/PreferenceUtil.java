package main.utility;

import java.io.File;
import java.util.prefs.Preferences;

import main.ComplierState;
import main.MainWindow;
import main.views.FileBrowser;

public class PreferenceUtil {
    final static String outputFolderKey = "Output Folder";
    final static String leadingKey = "Leading";
    final static String numberKey = "Number";
    final static String trailingKey = "Trailing";
    final static String zeroesKey = "Zeroes";
    final static String fileNameKey = "File Name";
    final static String autosaveKey = "Autosave";

    /**
     * Saves all preferences from textfields and autosave checkbox
     * 
     * @param mainWindow Get values from textfields to save
     */
    public static void saveAllPreferences(MainWindow mainWindow, ComplierState state) {
		Preferences prefs = Preferences.userNodeForPackage(PreferenceUtil.class);

        System.out.println("Closing window, saving constants...");
        prefs.put(outputFolderKey, mainWindow.outputFolderTextField.getText());
        prefs.put(leadingKey, mainWindow.leadingTextField.getText());
        prefs.put(numberKey, mainWindow.numberTextField.getText());
        prefs.put(trailingKey, mainWindow.trailingTextField.getText());
        prefs.put(zeroesKey, mainWindow.leadingZerosTextField.getText());
        prefs.put(fileNameKey, mainWindow.fileNameTextField.getText());
        prefs.putBoolean(autosaveKey, state.multiLineAutosave);
    }

    /**
     * Gets all preference variables to display
     * Checks if output file stored is valid before updating
     * currentDir, parentDir and FileExplorerTree
     * 
     * @param mainWindow Load values to textfields
     * @param state Load previous autosave and folder information
     */
    public static void loadPreferences(MainWindow mainWindow, ComplierState state) {
        Preferences prefs = Preferences.userNodeForPackage(PreferenceUtil.class);
        
        boolean autosave = prefs.getBoolean(autosaveKey, false);
		mainWindow.autoSaveCheckBox.setSelected(autosave);
        state.multiLineAutosave = autosave;

		String output = prefs.get(outputFolderKey, "");
		String leading = prefs.get(leadingKey, "");
		String number = prefs.get(numberKey, "");
		String trailing = prefs.get(trailingKey, "");
        String fileName = prefs.get(fileNameKey, "");
		String zeros = prefs.get(zeroesKey, "");

		mainWindow.leadingTextField.setText(leading);
		mainWindow.numberTextField.setText(number);
		mainWindow.trailingTextField.setText(trailing);
		mainWindow.fileNameTextField.setText(fileName);
		mainWindow.leadingZerosTextField.setText(zeros);

		File outputFile = new File(output);
		if (outputFile.exists()) {
			state.currentDirectory = outputFile;
			state.parentDirectory = outputFile.getParentFile();
			mainWindow.outputFolderTextField.setText(output);

            //Set starting directory of fileBrowser to previously store folder
            mainWindow.fileBrowser = new FileBrowser(outputFile.getAbsolutePath());
		}
    }
}
