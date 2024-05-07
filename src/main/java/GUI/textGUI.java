package GUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class textGUI {
    public static JTextArea textArea;

    private JFrame frame;
    private JTextField findField;
    private int lastIndex = -1;
    private JLabel positionLabel;

    public textGUI() {
        frame = new JFrame("Text Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textArea = new JTextArea(10, 30);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton findButton = new JButton("Find");
        findButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                findText();
            }
        });
        buttonPanel.add(findButton);

        frame.add(buttonPanel, BorderLayout.NORTH);

        frame.pack();
        frame.setVisible(true);
    }

    private void findText() {
        JDialog dialog = new JDialog(frame, "Find", true);
        dialog.setLayout(new FlowLayout());

        findField = new JTextField(15);
        dialog.add(findField);

        positionLabel = new JLabel("Position: -");
        dialog.add(positionLabel);

        JButton findNextButton = new JButton("Find Next");
        findNextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                findNext();
            }
        });
        dialog.add(findNextButton);

        JButton findPrevButton = new JButton("Find Previous");
        findPrevButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                findPrevious();
            }
        });
        dialog.add(findPrevButton);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        dialog.add(closeButton);

        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void findNext() {
        String findText = findField.getText();
        if (findText != null && !findText.isEmpty()) {
            String content = textGUI.textArea.getText();
            int index = content.indexOf(findText, lastIndex + 1);
            if (index != -1) {
                textGUI.textArea.requestFocusInWindow();
                textGUI.textArea.select(index, index + findText.length());
                lastIndex = index;
                positionLabel.setText("Position: " + lastIndex);
            } else {
                JOptionPane.showMessageDialog(frame, "No more occurrences found");
                lastIndex = -1;
                textGUI.textArea.requestFocusInWindow();
            }
        }
    }

    private void findPrevious() {
        String findText = findField.getText();
        if (findText != null && !findText.isEmpty()) {
            String content = textGUI.textArea.getText();
            int index = content.lastIndexOf(findText, lastIndex - 1);
            if (index != -1) {
                textGUI.textArea.requestFocusInWindow();
                textGUI.textArea.select(index, index + findText.length());
                lastIndex = index;
                positionLabel.setText("Position: " + lastIndex);
            } else {
                JOptionPane.showMessageDialog(frame, "No more occurrences found");
                lastIndex = textGUI.textArea.getText().length();
                textGUI.textArea.requestFocusInWindow();
            }
        }
    }

    public static void main(String[] args) {
        new textGUI();
    }
}


