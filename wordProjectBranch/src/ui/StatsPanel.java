package ui;

import model.Stats;
import ui.theme.UIUtil;

import javax.swing.*;
import java.awt.*;

public class StatsPanel extends JPanel {
    private final JLabel total=new JLabel("0"), acc=new JLabel("0%"), streak=new JLabel("0");

    public StatsPanel(){
        setLayout(new BorderLayout());
        JPanel card=UIUtil.cardPanel(new GridLayout(1,0,8,8));
        card.add(unit("문항", total));
        card.add(unit("정확도", acc));
        card.add(unit("연속정답", streak));
        add(card, BorderLayout.CENTER);
    }
    private JPanel unit(String name, JLabel value){
        JPanel p=new JPanel(new GridLayout(0,1)); p.setOpaque(false);
        JLabel n=new JLabel(name, SwingConstants.CENTER); n.setFont(new Font("Dialog",Font.PLAIN,12));
        value.setHorizontalAlignment(SwingConstants.CENTER); value.setFont(new Font("Dialog",Font.BOLD,18));
        p.add(n); p.add(value); return p;
    }
    public void update(Stats s){
        total.setText(String.valueOf(s.total));
        acc.setText(String.format("%.1f%%", s.accuracy()));
        streak.setText(String.valueOf(s.streak));
    }
}
