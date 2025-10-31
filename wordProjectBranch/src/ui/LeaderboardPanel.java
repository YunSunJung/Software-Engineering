package ui;

import service.ScoreService;
import ui.theme.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/** TOP5 랭킹을 뱃지/그라데이션/글로우로 화려하게 표현 */
public class LeaderboardPanel extends JPanel {
    public LeaderboardPanel(){
        setLayout(new BorderLayout(8,8));
        setOpaque(false);
        JLabel title=new JLabel("🏆 TOP 5 맞힌 단어 랭킹");
        title.setFont(new Font("Malgun Gothic",Font.BOLD,20));
        title.setForeground(Theme.TEXT_SECONDARY);
        add(title, BorderLayout.NORTH);
        add(new JPanel(), BorderLayout.CENTER);
    }

    public void setEntries(List<ScoreService.Entry> entries){
        JPanel list=new JPanel(new GridLayout(Math.max(1,entries.size()),1,10,10)){
            @Override protected void paintComponent(Graphics g){
                super.paintComponent(g);
                // 배경에 은은한 패턴
                Graphics2D g2=(Graphics2D)g.create();
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.06f));
                for(int y=0;y<getHeight();y+=40){
                    g2.setColor(new Color(80,120,255));
                    g2.fillRect(0,y,getWidth(),20);
                }
                g2.dispose();
            }
        };
        list.setOpaque(false);

        int rank=1;
        for(ScoreService.Entry e: entries){
            list.add(row(rank++, e.username, e.correct));
        }
        if(entries.isEmpty()){
            JLabel empty=new JLabel("기록이 없습니다. 학습을 시작해보세요!", SwingConstants.CENTER);
            empty.setFont(new Font("Dialog",Font.PLAIN,14));
            empty.setForeground(new Color(110,120,135));
            list.setLayout(new GridLayout(1,1));
            list.add(empty);
        }
        remove(1);
        add(list, BorderLayout.CENTER);
        revalidate(); repaint();
    }

    private JComponent row(int rank,String user,int score){
        Color badgeStart = rank==1? new Color(255,215,0)  : rank==2? new Color(192,192,192) :
                           rank==3? new Color(205,127,50) : new Color(120,130,145);
        Color badgeEnd   = rank<=3? badgeStart.darker() : new Color(90,98,114);

        JPanel p=new JPanel(new BorderLayout(12,0)){
            @Override protected void paintComponent(Graphics g){
                super.paintComponent(g);
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 카드 배경 + 글로우
                int w=getWidth(), h=getHeight();
                g2.setPaint(new GradientPaint(0,0,new Color(255,255,255),0,h,new Color(244,247,252)));
                g2.fillRoundRect(0,0,w,h,18,18);

                if(rank==1){
                    g2.setPaint(new RadialGradientPaint(new Point(w/4,h/2), w/2f,
                            new float[]{0f,1f}, new Color[]{new Color(255,230,120,80), new Color(255,230,120,0)}));
                    g2.fillRoundRect(0,0,w,h,18,18);
                }
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(12,14,12,14));

        // 왼쪽: 랭크 뱃지
        JLabel badge = new JLabel(rank<=3? (rank==1?"👑 1":"★ "+rank) : "#"+rank);
        badge.setFont(new Font("Malgun Gothic", Font.BOLD, 18));
        badge.setForeground(Color.WHITE);
        JPanel badgeBox = new JPanel(new GridBagLayout()){
            @Override protected void paintComponent(Graphics g){
                super.paintComponent(g);
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0,0,badgeStart,0,getHeight(),badgeEnd));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),14,14);
                g2.dispose();
            }
        };
        badgeBox.setOpaque(false);
        badgeBox.add(badge);
        badgeBox.setPreferredSize(new Dimension(86,36));

        // 가운데: 이름 + 진행바(점수 시각화)
        JLabel name = new JLabel(user);
        name.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
        JProgressBar bar = new JProgressBar(0, Math.max(1, score));
        bar.setValue(score);
        bar.setStringPainted(true);
        bar.setString(score + " 개");
        bar.setForeground(new Color(57,119,255));
        bar.setBackground(new Color(230,235,245));
        JPanel center = new JPanel(new GridLayout(2,1,4,6));
        center.setOpaque(false);
        center.add(name);
        center.add(bar);

        // 오른쪽: 포인트 아이콘
        JLabel pts = new JLabel("✨");
        pts.setFont(new Font("Dialog", Font.PLAIN, 20));

        p.add(badgeBox, BorderLayout.WEST);
        p.add(center, BorderLayout.CENTER);
        p.add(pts, BorderLayout.EAST);
        return p;
    }
}
