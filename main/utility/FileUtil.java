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

import main.MainWindow;

public class FileUtil {
    private static Desktop desktop = Desktop.getDesktop();
    private static JFileChooser fileChooser = new JFileChooser();

    public static boolean isTxtFile(File file) {
        String fileName = file.getAbsolutePath();
        int dotIndex = fileName.lastIndexOf('.');
        String fileType = (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
        return fileType.equals("txt");
    }

    public static String readTextFile(File selectedFile) {
        if (!isTxtFile(selectedFile))
            return "";

        Path selectedFilePath = Paths.get(selectedFile.getAbsolutePath());
        try {
            String content = new String(Files.readAllBytes(selectedFilePath));
            return content;

        } catch (IOException e) {
            e.printStackTrace();
            MainWindow.consoleLog("Error reading file: " + selectedFile);
            return "";
        }
    }

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

    public static void open(File selectedFile, boolean openNotepadPlusPlus) {
        if (selectedFile == null || !selectedFile.exists()) {
            System.out.println("Please select a file to open!");
            MainWindow.consoleLog("Please select a file to open!");
            return;
        }

        String fileName = selectedFile.getAbsolutePath();
        if (!isTxtFile(selectedFile)) {
            System.out.println("Only allowed to open text (.txt) files!");
            MainWindow.consoleLog("Only allowed to open text (.txt) files!");
            return;
        }

        if (openNotepadPlusPlus) {
            try {
                fileName = '"' + fileName + '"';
                ProcessBuilder processBuilder = new ProcessBuilder();
                String command = "start notepad++ " + fileName;
                processBuilder.command("CMD", "/C", command);
                processBuilder.start();

                System.out.println("Opening in Notepad++: " + fileName);
                MainWindow.consoleLog("Opening in Notepad++: " + fileName);
    
            } catch (Exception e) {
                e.printStackTrace();
                MainWindow.consoleLog("Error occured while opening: " + fileName);
                MainWindow.consoleLog("Does your system have notepad++ installed?");
            }

        } else if (desktop.isSupported(Desktop.Action.OPEN)) {
            try {
                desktop.open(selectedFile);
                System.out.println("Opening: " + fileName);
                MainWindow.consoleLog("Opening: " + fileName);

            } catch (IOException err) {
                err.printStackTrace();
                MainWindow.consoleLog("Error occured while opening: " + fileName);
            }
        }
    }

    public static boolean rename(File selectedFile, String renamedString) {
        Path source = Paths.get(selectedFile.getAbsolutePath());
        try {
            Files.move(source, source.resolveSibling(renamedString));
            System.out.println("Successfully renamed file to: " + renamedString);
            MainWindow.consoleLog("Successfully renamed file to: " + renamedString);
            return true;

        } catch (IOException e) {
            System.out.println("Error renaming file to: " + renamedString);
            MainWindow.consoleLog("Error renaming file to: " + renamedString);
        }

        return false;
    }

    public static boolean save(String filePathString, String currentText) {
        String newLine = System.getProperty("line.separator");
        try {
            // Write contents to new file
            FileWriter fileWriter = new FileWriter(filePathString, false);
            BufferedWriter outWriter = new BufferedWriter(fileWriter);
            String[] sentences = currentText.split("\\n");
            for (String line : sentences) {
                outWriter.write(line);
                outWriter.write(newLine);
            }
            outWriter.close();

            System.out.println("Successfully saved file as: " + filePathString);
            MainWindow.consoleLog("Successfully saved file as: " + filePathString);
            return true;

        } catch (FileNotFoundException e) {
            System.out.println("Error with output folder: " + filePathString);
            MainWindow.consoleLog("Error with output folder: " + filePathString);

        } catch (IOException e) {
            e.printStackTrace();
            MainWindow.consoleLog("Error writing to file: " + filePathString);
        }

        return false;
    }
}
