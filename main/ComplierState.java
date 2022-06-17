package main;

import java.io.File;

public class ComplierState {
    public File selectedFile;
	public File currentDirectory;
	public File parentDirectory;

	public boolean isTracking;
	public boolean multiLineAutosave;

    public ComplierState() {
        selectedFile = null;
        currentDirectory = null;
        parentDirectory = null;

        isTracking = false;
        multiLineAutosave = true;
    }
}
