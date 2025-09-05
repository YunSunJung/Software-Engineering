package com.wordmaster;

import model.*;
import service.*;
import ui.*;

import javax.swing.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // CSV 읽기 (resources를 Source Folder로 등록했고 파일명만 넘깁니다)
        CSVLoader loader = new CSVLoader();
        List<Word> words = loader.loadWords("words.csv");

        // 사용자/세션/통계
        User user = new User("Player1");
        final Session[] sessionRef = new Session[1];
        final Stats[] statsRef = new Stats[1];
        sessionRef[0] = new Session(user, words);
        statsRef[0] = new Stats();

        // 서비스
        AnswerChecker answerChecker = new AnswerChecker();
        AccuracyTracker accuracyTracker = new AccuracyTracker();
        WrongWordTracker wrongWordTracker = new WrongWordTracker();

        // UI
        MainFrame frame = new MainFrame();
        StartPanel start = frame.getStartPanel();
        QuestionPanel qPanel = frame.getQuestionPanel();
        AnswerPanel aPanel = frame.getAnswerPanel();
        FeedbackPanel fPanel = frame.getFeedbackPanel();
        StatsPanel sPanel = frame.getStatsPanel();
        ProfilePanel pPanel = frame.getProfilePanel();

        // 현재 문제를 익명리스너에서 바꾸기 위해 배열로 보관
        final Word[] current = new Word[1];

        // "새 게임 시작" 버튼
        start.getNewGameButton().addActionListener(e -> {
            sessionRef[0] = new Session(user, words); // 새 세션
            statsRef[0]   = new Stats();              // 통계 리셋
            current[0]    = sessionRef[0].getNextWord();

            if (current[0] != null) qPanel.setWord(current[0]);
            aPanel.clearInput();
            sPanel.updateStats(statsRef[0]);
            pPanel.updateProfile(user);

            frame.showQuiz(); // 문제+입력창 카드로 전환
            SwingUtilities.invokeLater(() -> aPanel.getAnswerField().requestFocusInWindow());
        });

        // "제출" 버튼
        aPanel.getSubmitButton().addActionListener(e -> {
            if (current[0] == null) return;

            char input = aPanel.getInputChar();
            boolean ok = answerChecker.checkAnswer(current[0], input);
            fPanel.showFeedback(ok,
                String.valueOf(current[0].getEnglish().charAt(current[0].getBlankIndex())));
            accuracyTracker.updateStats(statsRef[0], ok);
            if (!ok) wrongWordTracker.recordWrongWord(current[0]);

            sPanel.updateStats(statsRef[0]);
            pPanel.updateProfile(user);

            Word next = sessionRef[0].getNextWord();
            if (next != null) {
                current[0] = next;
                qPanel.setWord(current[0]);
                aPanel.clearInput();
                aPanel.getAnswerField().requestFocusInWindow();
            } else {
                JOptionPane.showMessageDialog(frame, "모든 문제가 완료되었습니다!");
                frame.showStart(); // 시작 화면으로 복귀
            }
        });

        // 처음에는 시작 화면
        frame.showStart();
        //push 2.0
        //push 2.2
    }
}

