package ui;

import javax.swing.*;
import java.awt.*;

public class StartPanel extends JPanel {
    private JButton newGameButton;

    public StartPanel() {
        setLayout(new BorderLayout());
        JLabel title = new JLabel("Word Master Game", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.CENTER);

        newGameButton = new JButton("새 게임 시작");
        add(newGameButton, BorderLayout.SOUTH);
    }

    public JButton getNewGameButton() {
        return newGameButton;
    }
}
