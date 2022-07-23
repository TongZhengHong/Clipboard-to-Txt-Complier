package main;

import java.io.File;

import main.views.FileBrowser;

public class ComplierState {
    public static String previousClipboard = "";

    public static File selectedFile = null;
	public static File currentDirectory = null;
	public static File parentDirectory = null;

	public static boolean isTracking = false;
	public static boolean incrementNumber = true;
	public static boolean showTextFileTop = true;

    public static int fileSortBy = FileBrowser.NAME_ASCENDING;
}
