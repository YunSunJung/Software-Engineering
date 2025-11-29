package ui;

import model.LangPair;
import javax.swing.*;
import java.awt.*;

public class SignupDialog extends JDialog {
    private final JTextField user = new JTextField(16);
    private final JPasswordField pass = new JPasswordField(16);
    private final JComboBox<LangPair> lang = new JComboBox<>(new LangPair[]{LangPair.KO_EN, LangPair.KO_JP, LangPair.KO_CN});
    private final JButton ok=new JButton("가입"), cancel=new JButton("취소");
    private boolean confirmed=false;

    public SignupDialog(JFrame owner){
        super(owner,"회원가입",true);
        setLayout(new GridLayout(0,2,6,6));
        add(new JLabel("사용자명:")); add(user);
        add(new JLabel("비밀번호:")); add(pass);
        add(new JLabel("언어쌍:")); add(lang);
        add(ok); add(cancel);
        pack(); setLocationRelativeTo(owner);
        getRootPane().setDefaultButton(ok);
        ok.addActionListener(e->{ confirmed=true; setVisible(false); });
        cancel.addActionListener(e->{ confirmed=false; setVisible(false); });
    }
    public boolean isConfirmed(){ return confirmed; }
    public String getUsername(){ return user.getText(); }
    public String getPassword(){ return new String(pass.getPassword()); }
    public LangPair getLang(){ return (LangPair)lang.getSelectedItem(); }
}
