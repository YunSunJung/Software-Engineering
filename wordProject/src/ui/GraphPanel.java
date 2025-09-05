package ui;

import model.Stats;

import javax.swing.*;
import java.awt.*;

public class GraphPanel extends JPanel {

    private Stats stats;

    public GraphPanel() {
        setPreferredSize(new Dimension(400, 300));
    }

    public void setStats(Stats stats) {
        this.stats = stats;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (stats == null) return;

        int total = stats.getTotalQuestions();
        if (total == 0) return;

        int correct = stats.getCorrectAnswers();
        int wrong = stats.getWrongAnswers();

        int width = getWidth();
        int height = getHeight();
        int barWidth = width / 4;

        // 정답 바
        g.setColor(Color.GREEN);
        int correctHeight = (int) ((double) correct / total * height);
        g.fillRect(width / 4 - barWidth / 2, height - correctHeight, barWidth, correctHeight);

        // 오답 바
        g.setColor(Color.RED);
        int wrongHeight = (int) ((double) wrong / total * height);
        g.fillRect(3 * width / 4 - barWidth / 2, height - wrongHeight, barWidth, wrongHeight);

        // 레이블
        g.setColor(Color.BLACK);
        g.drawString("정답", width / 4 - barWidth / 2, height - correctHeight - 5);
        g.drawString("오답", 3 * width / 4 - barWidth / 2, height - wrongHeight - 5);
    }
}
