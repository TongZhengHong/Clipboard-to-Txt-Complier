package main;

import java.io.File;

public class ComplierState {
    public String previousClipboard;

    public File selectedFile;
	public File currentDirectory;
	public File parentDirectory;

	public boolean isTracking;
	public boolean multiLineAutosave;

    public ComplierState() {
        previousClipboard = "";
        
        selectedFile = null;
        currentDirectory = null;
        parentDirectory = null;

        isTracking = false;
        multiLineAutosave = true;
    }
}
