package ui.theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UIUtil {
    public static JPanel cardPanel(LayoutManager lm){
        JPanel p=new JPanel(lm){
            @Override protected void paintComponent(Graphics g){
                super.paintComponent(g);
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0,0,0,20));
                g2.fillRoundRect(6,8,getWidth()-12,getHeight()-12,24,24);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0,2,getWidth()-12,getHeight()-12,24,24);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(18,24,18,24));
        return p;
    }

    public static JButton primaryButton(String text){
        JButton b=new JButton(text){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0,0,Theme.PRIMARY,0,getHeight(),Theme.PRIMARY_DARK));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),16,16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setForeground(Color.WHITE);
        b.setBorder(new EmptyBorder(10,16,10,16));
        return b;
    }
}
