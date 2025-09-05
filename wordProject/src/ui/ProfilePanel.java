package ui;

import model.User;

import javax.swing.*;
import java.awt.*;

public class ProfilePanel extends JPanel {

    private JLabel usernameLabel;
    private JLabel levelLabel;
    private JLabel expLabel;

    public ProfilePanel() {
        setLayout(new GridLayout(3, 1));

        usernameLabel = new JLabel("사용자: ");
        levelLabel = new JLabel("레벨: ");
        expLabel = new JLabel("경험치: ");

        add(usernameLabel);
        add(levelLabel);
        add(expLabel);
    }

    // 사용자 정보 업데이트
    public void updateProfile(User user) {
        usernameLabel.setText("사용자: " + user.getUsername());
        levelLabel.setText("레벨: " + user.getLevel());
        expLabel.setText("경험치: " + user.getExp());
    }
}