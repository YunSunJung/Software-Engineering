package ui;

import javax.swing.*;
import java.awt.*;

public class FeedbackPanel extends JPanel {
    private JLabel feedbackLabel;

    public FeedbackPanel() {
        setLayout(new BorderLayout());
        feedbackLabel = new JLabel("정답/오답 피드백", SwingConstants.CENTER);
        feedbackLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 28)); // 한글 글꼴
        add(feedbackLabel, BorderLayout.CENTER);
    }

    public void showFeedback(boolean isCorrect, String correctAnswer) {
        if (isCorrect) {
            feedbackLabel.setText("정답!");
            feedbackLabel.setForeground(new Color(0, 160, 0));
        } else {
            feedbackLabel.setText("오답! 정답: " + correctAnswer);
            feedbackLabel.setForeground(Color.RED);
        }
    }

    public void clearFeedback() {
        feedbackLabel.setText("정답/오답 피드백");
        feedbackLabel.setForeground(Color.DARK_GRAY);
    }
}
