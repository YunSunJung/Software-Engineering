package ui;

import ui.theme.PillChoiceButton;
import ui.theme.UIUtil;
import ui.theme.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class ChoicePanel extends JPanel {
    private final JButton submitBtn = UIUtil.primaryButton("정답 제출");
    private final ButtonGroup group = new ButtonGroup();
    private final List<PillChoiceButton> btns = new ArrayList<>(4);

    public ChoicePanel(){
        setLayout(new BorderLayout(8,8));
        setBackground(Theme.BG);
        setBorder(BorderFactory.createEmptyBorder(0,8,8,8));

        JPanel card=UIUtil.cardPanel(new BorderLayout(8,8));
        JPanel grid=new JPanel(new GridLayout(2,2,10,10));
        grid.setOpaque(false);

        for(int i=0;i<4;i++){
            PillChoiceButton b=new PillChoiceButton("선지 "+(i+1));
            b.setHorizontalAlignment(SwingConstants.CENTER);
            btns.add(b); group.add(b); grid.add(b);
        }

        // 1~4 단축키, Enter 제출
        registerKeyboardAction(e->submitBtn.doClick(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),
                WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        for(int i=0;i<4;i++){
            final int idx=i;
            registerKeyboardAction(e->btns.get(idx).setSelected(true),
                    KeyStroke.getKeyStroke(KeyEvent.VK_1+i,0),
                    WHEN_IN_FOCUSED_WINDOW);
        }

        card.add(grid, BorderLayout.CENTER);
        JPanel south=new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.setOpaque(false); south.add(submitBtn);
        card.add(south, BorderLayout.SOUTH);
        add(card, BorderLayout.CENTER);
    }

    public void setChoices(java.util.List<String> choices){
        group.clearSelection();
        for(int i=0;i<btns.size();i++){
            String t=(choices!=null && i<choices.size())? choices.get(i):"";
            PillChoiceButton b=btns.get(i);
            b.setText(t); b.setSelected(false); b.setVisible(t!=null && !t.isEmpty());
        }
        revalidate(); repaint();
    }

    public String getSelected(){
        for(PillChoiceButton b: btns) if(b.isSelected()) return b.getText();
        return null;
    }
    public JButton getSubmitButton(){ return submitBtn; }
}
