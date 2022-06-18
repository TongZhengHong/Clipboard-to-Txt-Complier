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

import main.MainWindow;

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

    public static void showFileExplorer(File currentDirectory) {
        if (currentDirectory == null || !currentDirectory.exists())
            return;
        try {
            desktop.open(currentDirectory);
        } catch (IOException e1) {
            System.out.println("Error opening directory: " + currentDirectory.getAbsolutePath());
            MainWindow.consoleLog("Error opening directory: " + currentDirectory.getAbsolutePath());
        }
    }

    public static void open(File selectedFile) {
        if (selectedFile == null || !selectedFile.exists()) {
            System.out.println("Please select a file to open!");
            MainWindow.consoleLog("Please select a file to open!");
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
                    MainWindow.consoleLog("Opening: " + fileName);

                } catch (IOException err) {
                    err.printStackTrace();
                    MainWindow.consoleLog("Error occured while opening: " + fileName);
                }
            } else {
                System.out.println("Only allowed to open text (.txt) files!");
                MainWindow.consoleLog("Only allowed to open text (.txt) files!");
            }
        }
    }

    public static boolean rename(Component context, File selectedFile) {
        if (selectedFile == null || !selectedFile.exists()) {
            System.out.println("Please select a file to rename!");
            MainWindow.consoleLog("Please select a file to rename!");
            return false;
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
            return false;

        String renamedString = inputDialogResult.toString();

        if (renamedString.isEmpty() || renamedString.equals(selectedFile.getName()))
            return false;

        if (!renamedString.contains(".txt"))
            renamedString += ".txt";

        Path source = Paths.get(selectedFile.getAbsolutePath());
        try {
            Files.move(source, source.resolveSibling(renamedString));
            System.out.println("Successfully renamed file to : " + renamedString);
            MainWindow.consoleLog("Successfully renamed file to : " + renamedString);
            return true;

        } catch (IOException e1) {
            System.out.println("Error renaming file to : " + renamedString);
            MainWindow.consoleLog("Error renaming file to : " + renamedString);
        }

        return false;
    }

    public static boolean save(String fileDirectory, String fileName, String currentText) {
        // Don't save if directory or file name is empty
		if (fileDirectory.isEmpty() || fileName.isEmpty()) {
			System.out.println("Error! Empty file name or directory. Please enter name or folder");
            MainWindow.consoleLog("Error! Empty file name or directory. Please enter name or folder");
			return false;
		}

        fileName = fileName.trim();
		String fileString = fileDirectory + "\\" + fileName + ".txt";
		File tempFile = new File(fileString);

		// Don't save if current file path exists
		if (tempFile.exists()) {
			System.out.println("Error! File already exists. Please change file name or directory");
            MainWindow.consoleLog("Error! File already exists. Please change file name or directory");
			return false;
		}

		String newLine = System.getProperty("line.separator");
		try {
			// Write contents to new file
            FileWriter fileWriter = new FileWriter(fileString, false);
			BufferedWriter outWriter = new BufferedWriter(fileWriter);
			String[] sentences = currentText.split("\\n");
			for (String line : sentences) {
				outWriter.write(line);
				outWriter.write(newLine);
			}
			outWriter.close();

			System.out.println("Successfully saved file as " + fileString);
            MainWindow.consoleLog("Successfully saved file as " + fileString);
            return true;

		} catch (FileNotFoundException e) {
			System.out.println("Error with output folder: " + fileString);
            MainWindow.consoleLog("Error with output folder: " + fileString);

		} catch (IOException e) {
			e.printStackTrace();
            MainWindow.consoleLog("Error writing to file: " + fileString);
		}

        return false;
    }
}
