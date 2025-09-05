package ui;

import javax.swing.*;
import java.awt.*;

public class AnswerPanel extends JPanel {

    private JTextField answerField;
    private JButton submitButton;

    public AnswerPanel() {
        setLayout(new FlowLayout());

        answerField = new JTextField(5);
        answerField.setFont(new Font("Arial", Font.PLAIN, 24));
        submitButton = new JButton("제출");

        add(new JLabel("답 입력:"));
        add(answerField);
        add(submitButton);
        
        answerField.addActionListener(e -> submitButton.doClick());
    }

    // Getter
    public JTextField getAnswerField() { return answerField; }
    public JButton getSubmitButton() { return submitButton; }

    // 입력값 가져오기
    public char getInputChar() {
        String text = answerField.getText();
        if (text != null && text.length() > 0) {
            return text.charAt(0);
        }
        return '\0';
    }

    // 입력 필드 초기화
    public void clearInput() {
        answerField.setText("");
    }
}
