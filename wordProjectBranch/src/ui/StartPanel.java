package ui;

import model.LangPair;
import ui.theme.Theme;
import ui.theme.UIUtil;

import javax.swing.*;
import java.awt.*;

public class StartPanel extends JPanel {
    private final JButton signupBtn = UIUtil.primaryButton("회원가입");
    private final JButton loginBtn  = UIUtil.primaryButton("로그인");
    private final JButton startBtn  = UIUtil.primaryButton("새 학습 시작");
    private final JButton analyticsBtn = UIUtil.primaryButton("분석 보기");

    private final JComboBox<LangPair> langCombo =
            new JComboBox<>(new LangPair[]{LangPair.KO_EN, LangPair.KO_JP, LangPair.KO_CN});
    private final JCheckBox slangToggle = new JCheckBox("슬랭 모드");

    private final LeaderboardPanel leaderboard = new LeaderboardPanel();

    // ★ 로그인 인사말
    private final JLabel greeting = new JLabel(" ", SwingConstants.LEFT);

    public StartPanel(){
        setLayout(new BorderLayout(12,12));
        setBackground(Theme.BG);
        setBorder(BorderFactory.createEmptyBorder(16,16,16,16));

        // 타이틀
        JLabel title=new JLabel("슬랭귀지");
        title.setFont(new Font("Malgun Gothic",Font.BOLD,28));
        add(title, BorderLayout.NORTH);

        JPanel main=new JPanel(new GridLayout(1,2,14,14));
        main.setOpaque(false);

        // 좌측 카드
        JPanel left=UIUtil.cardPanel(new GridLayout(0,1,10,10));

        // 1) 학습 언어쌍
        JPanel r1=new JPanel(new FlowLayout(FlowLayout.LEFT));
        r1.setOpaque(false);
        r1.add(new JLabel("학습 언어쌍: KO → "));
        r1.add(langCombo);

        // 2) (요청) 인사말 – 언어 선택 “밑쪽”
        JPanel r1_5=new JPanel(new FlowLayout(FlowLayout.LEFT));
        r1_5.setOpaque(false);
        greeting.setFont(new Font("Malgun Gothic", Font.PLAIN, 15));
        greeting.setForeground(new Color(70, 78, 92));
        r1_5.add(greeting);

        // 3) 슬랭 토글
        JPanel r2=new JPanel(new FlowLayout(FlowLayout.LEFT));
        r2.setOpaque(false);
        r2.add(slangToggle);

        // 4) 버튼들
        JPanel r3=new JPanel(new FlowLayout(FlowLayout.LEFT,8,0));
        r3.setOpaque(false);
        r3.add(signupBtn); r3.add(loginBtn); r3.add(startBtn); r3.add(analyticsBtn);

        left.add(r1);
        left.add(r1_5);     // ← 인사말 위치
        left.add(r2);
        left.add(r3);

        // 우측 카드(랭킹)
        JPanel right=UIUtil.cardPanel(new BorderLayout());
        right.add(leaderboard, BorderLayout.CENTER);

        main.add(left);
        main.add(right);
        add(main, BorderLayout.CENTER);
    }

    // === 외부 접근자 ===
    public JButton getSignupBtn(){ return signupBtn; }
    public JButton getLoginBtn(){ return loginBtn; }
    public JButton getStartBtn(){ return startBtn; }
    public JButton getAnalyticsBtn(){ return analyticsBtn; }

    public LangPair getSelectedLang(){ return (LangPair)langCombo.getSelectedItem(); }
    public boolean isSlangMode(){ return slangToggle.isSelected(); }
    public void setSelectedLang(LangPair lp){ langCombo.setSelectedItem(lp); }
    public void setSlangMode(boolean b){ slangToggle.setSelected(b); }

    public LeaderboardPanel getLeaderboardPanel(){ return leaderboard; }

    // ★ 로그인 인사말 갱신용
    public void setGreeting(String username){
        if (username == null || username.isEmpty()) greeting.setText(" ");
        else greeting.setText(username + "님 안녕하세요!");
    }
}
