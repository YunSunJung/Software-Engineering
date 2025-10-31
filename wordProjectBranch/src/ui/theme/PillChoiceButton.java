package ui.theme;

import javax.swing.*;
import java.awt.*;

public class PillChoiceButton extends JToggleButton {
    public PillChoiceButton(String text){
        super(text);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setMargin(new Insets(10,16,10,16));
        setOpaque(false);
    }
    @Override protected void paintComponent(Graphics g){
        Color bg = isSelected()? Theme.PRIMARY : new Color(236,241,247);
        Color fg = isSelected()? Color.WHITE : Theme.TEXT_PRIMARY;
        Graphics2D g2=(Graphics2D)g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(bg);
        g2.fillRoundRect(0,0,getWidth(),getHeight(),20,20);
        g2.dispose();
        setForeground(fg);
        super.paintComponent(g);
    }
}
