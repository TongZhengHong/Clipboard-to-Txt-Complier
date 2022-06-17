package main.utility;

import java.awt.Desktop;
import java.awt.Component;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class FileUtil {

    private static Desktop desktop = Desktop.getDesktop();
    private static JFileChooser fileChooser = new JFileChooser();

    public static String chooseFolder(Component context) {
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnVal = fileChooser.showOpenDialog(context);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            return file.getAbsolutePath();
        }

        return null;
    }

    public static boolean showFileExplorer(File currentDirectory) {
        try {
            desktop.open(currentDirectory);
            return true;

        } catch (IOException e1) {
            System.out.println("Error opening directory: " + currentDirectory.getAbsolutePath());
            return false;
        }
    }

    public static void openFile(File selectedFile) {
        if (selectedFile == null) {
            System.out.println("Please select a file to open!");
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

                } catch (IOException err) {
                    err.printStackTrace();
                }
            } else {
                System.out.println("Only allowed to open text (.txt) files!");
            }
        }
    }

    public enum RenameFileResult {
        FILE_DOESNT_EXIST,
        SUCCESS,
        ERROR,
        CANCEL,
    }

    public static RenameFileResult renameFile(Component context, File selectedFile) {
        if (selectedFile == null || !selectedFile.exists()) {
            System.out.println("Please select a file to rename!");
            return RenameFileResult.FILE_DOESNT_EXIST;
        }

        Object inputDialogResult = JOptionPane.showInputDialog(
                context,
                "New file name: ",
                "Rename File",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                selectedFile.getName());

        // Cancel button is clicked
        if (inputDialogResult == null)
            return RenameFileResult.CANCEL;
        String renamedString = inputDialogResult.toString();

        if (renamedString.isEmpty() || renamedString.equals(selectedFile.getName()))
            return RenameFileResult.CANCEL;
        if (!renamedString.contains(".txt"))
            renamedString += ".txt";

        Path source = Paths.get(selectedFile.getAbsolutePath());
        try {
            Files.move(source, source.resolveSibling(renamedString));
            System.out.println("Successfully renamed file to : " + renamedString);
            return RenameFileResult.SUCCESS;

        } catch (IOException e1) {
            System.out.println("Error renaming file to : " + renamedString);
            return RenameFileResult.ERROR;
        }
    }

    public enum SaveFileResult {
        FILE_EMPTY,
        FILE_EXISTS,
        SUCCESS,
        IO_ERROR,
        FILE_NOT_FOUND
    }

    public static SaveFileResult saveFile(String fileDirectory, String fileName, String content) {
        // Don't save if directory or file name is empty
		if (fileDirectory.isEmpty() || fileName.isEmpty()) {
			System.out.println("Error! Empty file name or directory. Please enter name or folder");
			return SaveFileResult.FILE_EMPTY;
		}

		String fileString = fileDirectory + "\\" + fileName + ".txt";
		File tempFile = new File(fileString);

		// Don't save if current file path exists
		if (tempFile.exists()) {
			System.out.println("Error! File already exists. Please change file name or directory");
			return SaveFileResult.FILE_EXISTS;
		}

		String newLine = System.getProperty("line.separator");
		try {
			// Write contents to new file
            FileWriter fileWriter = new FileWriter(fileString, false);
			BufferedWriter outWriter = new BufferedWriter(fileWriter);
			String[] sentences = content.split("\\n");
			for (String line : sentences) {
				outWriter.write(line);
				outWriter.write(newLine);
			}
			outWriter.close();

			System.out.println("Successfully saved file as " + fileString);

			// Reset current file text
			currentFileTextArea.setText("");

			// Update file explorer to show new file
			updateFileExplorerTree();

			

            return SaveFileResult.SUCCESS;

		} catch (FileNotFoundException e) {
			System.out.println("Error with output folder: " + fileString);
            return SaveFileResult.FILE_NOT_FOUND;

		} catch (IOException e) {
			e.printStackTrace();
            return SaveFileResult.IO_ERROR;
		}
    }
}
