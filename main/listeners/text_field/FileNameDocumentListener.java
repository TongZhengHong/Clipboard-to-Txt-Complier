package main.listeners.text_field;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import main.utility.UiUtil;

public class FileNameDocumentListener implements DocumentListener {
    UiUtil uiUtil;

    public FileNameDocumentListener(UiUtil uiUtil) {
        this.uiUtil = uiUtil;
    }
    
    @Override
	public void insertUpdate(DocumentEvent ev) {
		uiUtil.updateFileNameTextField();
	}

	@Override
	public void removeUpdate(DocumentEvent ev) {
		uiUtil.updateFileNameTextField();
	}

	@Override
	public void changedUpdate(DocumentEvent ev) {
	}
}
