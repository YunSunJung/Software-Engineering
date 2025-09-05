package ui;

import model.Word;

import javax.swing.*;
import java.awt.*;

public class QuestionPanel extends JPanel {
    private JLabel wordLabel;

    public QuestionPanel() {
        setLayout(new BorderLayout());
        wordLabel = new JLabel("문제가 여기에 표시됩니다", SwingConstants.CENTER);
        wordLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 36)); // 한글 글꼴
        add(wordLabel, BorderLayout.CENTER);
    }

    public void setWord(Word word) {
        if (word != null) {
            // 오른쪽 괄호 안에 '뜻'을 그대로 보여줍니다.
            wordLabel.setText(word.getDisplayWord() + "  (" + word.getMeaning() + ")");
        } else {
            wordLabel.setText("문제가 없습니다");
        }
    }

    public JLabel getWordLabel() { return wordLabel; }
}
