package main;
import java.awt.Toolkit;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

class IntegerFilter extends DocumentFilter {
    private boolean setLimit;

    public IntegerFilter(boolean limit) {
        this.setLimit = limit;
    }

    private boolean test(String text) {
        if (text.isEmpty()) return true;
        try {
            int number = Integer.parseInt(text);
            if (setLimit && (number < 0 || number > 10)) {
                System.out.println("Please enter number from 0 to 10!");
                MainWindow.consoleLog("Please enter number from 0 to 10!");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a number");
            MainWindow.consoleLog("Invalid input! Please enter a number");
            return false;
        }
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string,
            AttributeSet attr) throws BadLocationException {

        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.insert(offset, string);

        if (test(sb.toString())) {
            super.insertString(fb, offset, string, attr);
        } else {
            // warn the user and don't allow the insert
            Toolkit.getDefaultToolkit().beep();
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text,
            AttributeSet attrs) throws BadLocationException {

        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.replace(offset, offset + length, text);

        if (test(sb.toString())) {
            super.replace(fb, offset, length, text, attrs);
        } else {
            // warn the user and don't allow the insert
            Toolkit.getDefaultToolkit().beep();
        }

    }

    @Override
    public void remove(FilterBypass fb, int offset, int length)
            throws BadLocationException {
        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.delete(offset, offset + length);

        if (test(sb.toString())) {
            super.remove(fb, offset, length);
        } else {
            // warn the user and don't allow the insert
            Toolkit.getDefaultToolkit().beep();
        }
    }
}