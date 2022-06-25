package main.listeners.text_field;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import main.utility.UiUtil;

public class OutputFolderDocumentListener implements DocumentListener {
    UiUtil uiUtil;

    public OutputFolderDocumentListener(UiUtil uiUtil) {
        this.uiUtil = uiUtil;
    }

    @Override
    public void insertUpdate(DocumentEvent ev) {
        uiUtil.updateFileBrowser();
    }

    @Override
    public void removeUpdate(DocumentEvent ev) {
        uiUtil.updateFileBrowser();
    }

    @Override
    public void changedUpdate(DocumentEvent ev) {
    }
}
