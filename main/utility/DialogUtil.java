package main.utility;

import java.awt.Component;
import java.io.File;

import javax.swing.JOptionPane;

public class DialogUtil {
    
    public static boolean showConfirmSaveDialog(Component context) {
        int confirmDialogResult = JOptionPane.showConfirmDialog(
                context,
                "File name already exists! Would you like to override the existing file?",
                "Save File",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        return confirmDialogResult == JOptionPane.YES_OPTION;
    }

    public static boolean showConfirmRenameDialog(Component context) {
        int confirmDialogResult = JOptionPane.showConfirmDialog(
                context,
                "File name already exists! Would you like to override the existing file?",
                "Rename File",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        return confirmDialogResult == JOptionPane.YES_OPTION;
    }

    public static boolean showConfirmDeleteDialog(Component context) {
        int confirmDialogResult = JOptionPane.showConfirmDialog(
                context,
                "Are you sure you would like to delete this file?",
                "Delete File",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        return confirmDialogResult == JOptionPane.YES_OPTION;
    }

    public static String showRenameDialog(Component context, File originalFile) {
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
}
