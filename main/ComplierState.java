package main;

import java.io.File;

public class ComplierState {
    public static String previousClipboard = "";

    public static File selectedFile = null;
	public static File currentDirectory = null;
	public static File parentDirectory = null;

	public static boolean isTracking = false;
	public static boolean multiLineAutosave = true;
}
