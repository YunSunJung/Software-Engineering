package com.wordmaster;

import model.*;
import scheduler.*;
import service.*;
import ui.*;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            AuthService auth = new AuthService();
            CSVLoader loader = new CSVLoader();
            List<SentenceItem> sentences = loader.loadSentences("sentences.csv");
            ScoreService scoreService = new ScoreService(auth.getBaseDir());
            EventLogger eventLogger = new EventLogger(auth.getBaseDir());
            AnalyticsService analyticsService = new AnalyticsService(auth.getBaseDir());

            MainFrame frame = new MainFrame();
            StartPanel start = frame.getStartPanel();
            QuestionPanel qPanel = frame.getQuestionPanel();
            ChoicePanel choice = frame.getChoicePanel();
            FeedbackPanel fPanel = frame.getFeedbackPanel();
            StatsPanel sPanel = frame.getStatsPanel();
            JButton saveExit = frame.getSaveExitButton();

            // 랭킹 초기 표시
            start.getLeaderboardPanel().setEntries(scoreService.topN(5));

            final UserProfile[] user = new UserProfile[1];
            final Stats stats = new Stats();

            SignupDialog signup = new SignupDialog(frame);
            LoginDialog login = new LoginDialog(frame);

            // 이전 리스너 정리
            Runnable clear = () -> {
                for (ActionListener al : choice.getSubmitButton().getActionListeners())
                    choice.getSubmitButton().removeActionListener(al);
                for (ActionListener al : saveExit.getActionListeners())
                    saveExit.removeActionListener(al);
            };

            // 회원가입
            start.getSignupBtn().addActionListener(e -> {
                signup.setVisible(true);
                if (!signup.isConfirmed()) return;
                boolean ok = auth.signup(signup.getUsername(), signup.getPassword(), signup.getLang());
                JOptionPane.showMessageDialog(frame, ok ? "가입 성공" : "가입 실패");
                start.getLeaderboardPanel().setEntries(scoreService.topN(5));
            });

            // 로그인
            start.getLoginBtn().addActionListener(e -> {
                login.setVisible(true);
                if (!login.isConfirmed()) return;
                UserProfile up = auth.login(login.getUsername(), login.getPassword());
                if (up == null) {
                    JOptionPane.showMessageDialog(frame, "로그인 실패");
                    return;
                }
                user[0] = up;
                start.setSelectedLang(up.getPreferredLangPair());
                start.setSlangMode(up.isSlangMode());
                start.setGreeting(up.getUsername());
                JOptionPane.showMessageDialog(frame, "로그인: " + up.getUsername());
                start.getLeaderboardPanel().setEntries(scoreService.topN(5));
            });

            // 분석 보기
            start.getAnalyticsBtn().addActionListener(e -> {
                JFrame win = new JFrame("학습 분석");
                win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                win.setSize(640, 360);
                win.setLocationRelativeTo(frame);
                win.setContentPane(new AnalyticsPanel(analyticsService));
                win.setVisible(true);
            });

            // 학습 시작
            start.getStartBtn().addActionListener(e -> {
                if (user[0] == null) {
                    JOptionPane.showMessageDialog(frame, "먼저 로그인하세요.");
                    return;
                }
                user[0].setPreferredLangPair(start.getSelectedLang());
                user[0].setSlangMode(start.isSlangMode());
                clear.run();

                // 풀 구성(슬랭/일반 분리)
                List<SentenceItem> pool = new ArrayList<>();
                boolean slang = user[0].isSlangMode();
                LangPair pair = user[0].getPreferredLangPair();
                for (SentenceItem s : sentences) {
                    if (s.getLangPair() != pair) continue;
                    if (slang && !s.isSlang()) continue;
                    if (!slang && s.isSlang()) continue;
                    pool.add(s);
                }
                if (pool.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "해당 설정에 맞는 문항이 없습니다.");
                    return;
                }

                QuestionScheduler qs = new SM2Scheduler();
                List<QuizItem> items = new ArrayList<>(pool);
                qs.init(items);

                ChoiceGenerator cg = new ChoiceGenerator();
                final QuizItem[] current = new QuizItem[1];

                fPanel.clear();
                sPanel.update(stats);

                current[0] = qs.next();
                qPanel.setItem(current[0]);
                if (current[0] instanceof SentenceItem)
                    choice.setChoices(cg.makeChoices((SentenceItem) current[0], pool));

                choice.getSubmitButton().addActionListener(ev -> {
                    if (current[0] == null) return;
                    String pick = choice.getSelected();
                    if (pick == null) {
                        JOptionPane.showMessageDialog(frame, "선지를 선택하세요.");
                        return;
                    }
                    boolean ok = current[0].getAnswer().equals(pick);
                    stats.record(current[0], ok);
                    if (ok && user[0] != null) scoreService.addCorrect(user[0].getUsername(), 1);
                    eventLogger.log(user[0].getUsername(), current[0].getId(), ok);
                    qs.feedback(current[0], ok);

                    fPanel.showFeedback(ok, current[0].getAnswer());
                    sPanel.update(stats);

                    current[0] = qs.next();
                    if (current[0] == null) {
                        JOptionPane.showMessageDialog(frame, "학습 종료");
                        start.getLeaderboardPanel().setEntries(scoreService.topN(5));
                        return;
                    }
                    qPanel.setItem(current[0]);
                    if (current[0] instanceof SentenceItem)
                        choice.setChoices(cg.makeChoices((SentenceItem) current[0], pool));
                });

                saveExit.addActionListener(ev -> {
                    if (user[0] == null) return;
                    new ProgressSaver(auth.getBaseDir(), user[0].getUsername())
                            .save(user[0], stats, (current[0] != null) ? current[0].getId() : null);
                    clear.run();
                    fPanel.clear();
                    qPanel.setItem(null);
                    stats.total = stats.correct = stats.streak = 0;
                    sPanel.update(stats);
                    JOptionPane.showMessageDialog(frame, "저장되었습니다. 초기화면으로 돌아갑니다.");
                    start.getLeaderboardPanel().setEntries(scoreService.topN(5));
                    frame.showStart();
                });

                frame.showQuiz();
            });

            frame.setVisible(true);
            frame.showStart();
        });
    }
}
