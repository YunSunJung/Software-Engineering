package ui;

import ui.theme.UIUtil;
import javax.swing.*;
import java.awt.*;

public class FeedbackPanel extends JPanel {
    private final JLabel msg = new JLabel(" ");

    public FeedbackPanel(){
        setLayout(new BorderLayout());
        JPanel card=UIUtil.cardPanel(new BorderLayout());
        msg.setHorizontalAlignment(SwingConstants.CENTER);
        msg.setFont(new Font("Dialog",Font.BOLD,16));
        card.add(msg, BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);
    }
    public void showFeedback(boolean ok, String answer){ msg.setText(ok? "✅ 정답!" : "❌ 오답, 정답: " + answer); }
    public void clear(){ msg.setText(" "); }
}
