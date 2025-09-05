package ui;

import model.Stats;

import javax.swing.*;
import java.awt.*;

public class StatsPanel extends JPanel {

    private JLabel totalLabel;
    private JLabel correctLabel;
    private JLabel wrongLabel;
    private JLabel accuracyLabel;

    public StatsPanel() {
        setLayout(new GridLayout(4, 1));
        totalLabel = new JLabel("총 문제: 0");
        correctLabel = new JLabel("정답: 0");
        wrongLabel = new JLabel("오답: 0");
        accuracyLabel = new JLabel("정확도: 0.0%");

        add(totalLabel);
        add(correctLabel);
        add(wrongLabel);
        add(accuracyLabel);
    }

    public void updateStats(Stats stats) {
        totalLabel.setText("총 문제: " + stats.getTotalQuestions());
        correctLabel.setText("정답: " + stats.getCorrectAnswers());
        wrongLabel.setText("오답: " + stats.getWrongAnswers());
        accuracyLabel.setText(String.format("정확도: %.2f%%", stats.getAccuracy()));
    }
}
