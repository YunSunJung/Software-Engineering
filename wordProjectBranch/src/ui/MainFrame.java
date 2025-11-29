package ui;

import ui.theme.Theme;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final StartPanel startPanel = new StartPanel();
    private final QuestionPanel questionPanel = new QuestionPanel();
    private final ChoicePanel choicePanel = new ChoicePanel();
    private final FeedbackPanel feedbackPanel = new FeedbackPanel();
    private final StatsPanel statsPanel = new StatsPanel();
    private final JButton saveExit = new JButton("저장하고 나가기");
    private final JPanel quizPage = new JPanel(new BorderLayout(8,8));

    public MainFrame(){
        super("슬랭귀지"); // ← 변경
        Theme.applyGlobalUI();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 700);
        setLocationRelativeTo(null);

        JPanel startPage = new JPanel(new BorderLayout());
        startPage.add(startPanel, BorderLayout.CENTER);
        setContentPane(startPage);

        JPanel top = new JPanel(new BorderLayout());
        top.add(statsPanel, BorderLayout.WEST);
        top.add(saveExit, BorderLayout.EAST);

        quizPage.add(top, BorderLayout.NORTH);
        quizPage.add(questionPanel, BorderLayout.CENTER);
        quizPage.add(choicePanel, BorderLayout.SOUTH);
    }

    public void showStart(){ setContentPane(new JPanel(new BorderLayout()){{ add(startPanel, BorderLayout.CENTER); }}); revalidate(); repaint(); }
    public void showQuiz(){ setContentPane(quizPage); revalidate(); repaint(); }

    public StartPanel getStartPanel(){ return startPanel; }
    public QuestionPanel getQuestionPanel(){ return questionPanel; }
    public ChoicePanel getChoicePanel(){ return choicePanel; }
    public FeedbackPanel getFeedbackPanel(){ return feedbackPanel; }
    public StatsPanel getStatsPanel(){ return statsPanel; }
    public JButton getSaveExitButton(){ return saveExit; }
}
