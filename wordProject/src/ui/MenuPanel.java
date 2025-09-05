package ui;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {

    private JButton startButton;
    private JButton loadButton;
    private JButton exitButton;

    public MenuPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);

        startButton = new JButton("새 게임 시작");
        loadButton = new JButton("세션 불러오기");
        exitButton = new JButton("종료");

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(startButton, gbc);

        gbc.gridy = 1;
        add(loadButton, gbc);

        gbc.gridy = 2;
        add(exitButton, gbc);
    }

    // Getter
    public JButton getStartButton() { return startButton; }
    public JButton getLoadButton() { return loadButton; }
    public JButton getExitButton() { return exitButton; }
}
