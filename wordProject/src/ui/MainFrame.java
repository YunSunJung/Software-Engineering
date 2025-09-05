package ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private StartPanel startPanel;
    private JPanel quizCard;          // <- 퀴즈 화면(복합 패널)

    private QuestionPanel questionPanel;
    private AnswerPanel answerPanel;
    private FeedbackPanel feedbackPanel;
    private StatsPanel statsPanel;
    private ProfilePanel profilePanel;

    public MainFrame() {
        setTitle("Word Master");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 패널 생성
        startPanel = new StartPanel();
        questionPanel = new QuestionPanel();
        answerPanel = new AnswerPanel();
        feedbackPanel = new FeedbackPanel();
        statsPanel = new StatsPanel();
        profilePanel = new ProfilePanel();

        // ▶ QUIZ 카드(한 화면에 합치기)
        quizCard = new JPanel(new BorderLayout(0, 10));
        quizCard.add(feedbackPanel, BorderLayout.NORTH);   // 위: 정답/오답 피드백
        quizCard.add(questionPanel, BorderLayout.CENTER);  // 가운데: 문제
        quizCard.add(answerPanel, BorderLayout.SOUTH);     // 아래: 입력창+제출 버튼
        // 필요하면 오른쪽에 통계 붙이기:
        // quizCard.add(statsPanel, BorderLayout.EAST);

        // 카드 등록
        mainPanel.add(startPanel, "START");
        mainPanel.add(quizCard,   "QUIZ");

        setContentPane(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // 화면 전환 유틸
    public void showStart() { cardLayout.show(mainPanel, "START"); }
    public void showQuiz()  { 
        cardLayout.show(mainPanel, "QUIZ"); 
        getRootPane().setDefaultButton(answerPanel.getSubmitButton());
        // 입력창에 포커스 주기
        SwingUtilities.invokeLater(() -> answerPanel.getAnswerField().requestFocusInWindow());
    }

    // 필요시 getter
    public StartPanel getStartPanel() { return startPanel; }
    public QuestionPanel getQuestionPanel() { return questionPanel; }
    public AnswerPanel getAnswerPanel() { return answerPanel; }
    public FeedbackPanel getFeedbackPanel() { return feedbackPanel; }
    public StatsPanel getStatsPanel() { return statsPanel; }
    public ProfilePanel getProfilePanel() { return profilePanel; }
}
