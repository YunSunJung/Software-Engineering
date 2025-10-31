package ui;

import javax.swing.*;
import java.awt.*;

public class LoginDialog extends JDialog {
    private final JTextField user = new JTextField(16);
    private final JPasswordField pass = new JPasswordField(16);
    private final JButton ok=new JButton("로그인"), cancel=new JButton("취소");
    private boolean confirmed=false;

    public LoginDialog(JFrame owner){
        super(owner,"로그인",true);
        setLayout(new GridLayout(0,2,6,6));
        add(new JLabel("사용자명:")); add(user);
        add(new JLabel("비밀번호:")); add(pass);
        add(ok); add(cancel);
        pack(); setLocationRelativeTo(owner);
        getRootPane().setDefaultButton(ok);
        ok.addActionListener(e->{ confirmed=true; setVisible(false); });
        cancel.addActionListener(e->{ confirmed=false; setVisible(false); });
    }
    public boolean isConfirmed(){ return confirmed; }
    public String getUsername(){ return user.getText(); }
    public String getPassword(){ return new String(pass.getPassword()); }
}
