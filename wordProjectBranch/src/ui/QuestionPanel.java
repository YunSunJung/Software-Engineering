package ui;

import model.*;
import ui.theme.UIUtil;
import ui.theme.Theme;

import javax.swing.*;
import java.awt.*;

public class QuestionPanel extends JPanel {
    private final JTextArea koArea=new JTextArea(3,60);
    private final JTextArea trArea=new JTextArea(3,60);

    public QuestionPanel(){
        setLayout(new BorderLayout(10,10));
        setBackground(Theme.BG);

        JPanel card=UIUtil.cardPanel(new GridLayout(0,1,8,8));
        JLabel head=new JLabel("문제");
        head.setFont(new Font("Malgun Gothic",Font.BOLD,18));
        head.setForeground(Theme.TEXT_SECONDARY);

        koArea.setFont(FontUtil.pickKR(Font.BOLD,28));
        koArea.setLineWrap(true); koArea.setWrapStyleWord(true); koArea.setEditable(false);

        trArea.setFont(new Font("Dialog",Font.PLAIN,18));
        trArea.setLineWrap(true); trArea.setWrapStyleWord(true); trArea.setEditable(false);

        card.add(head); card.add(new JScrollPane(koArea)); card.add(new JScrollPane(trArea));
        add(card, BorderLayout.CENTER);
    }

    public void setItem(QuizItem item){
        if(item==null){ koArea.setText("문항 없음"); trArea.setText(""); return; }
        if(item instanceof SentenceItem){
            SentenceItem s=(SentenceItem)item;
            switch (s.getLangPair()){
                case KO_JP: trArea.setFont(FontUtil.pickJP(Font.PLAIN,18)); break;
                case KO_CN: trArea.setFont(FontUtil.pickCN(Font.PLAIN,18)); break;
                default:    trArea.setFont(new Font("Dialog",Font.PLAIN,18));
            }
            koArea.setText(s.getMaskedKo());
            trArea.setText(s.getLearnerTranslation());
        } else {
            koArea.setText(item.getDisplayText());
            trArea.setText("");
        }
    }
}
