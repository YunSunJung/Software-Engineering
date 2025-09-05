package service;

import model.Session;
import model.Word;

import java.io.*;
import java.util.List;

public class ProgressSaver {

    // 세션 저장 (단순 CSV 형태 예시)
    public void saveSession(Session session, String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(session.getUser().getUsername() + "," + session.getUser().getLevel() + "," + session.getUser().getExp());
            bw.newLine();
            List<Word> words = session.getQuizWords();
            for (Word w : words) {
                bw.write(w.getEnglish() + "," + w.getMeaning() + "," + w.getBlankIndex() + "," + w.getCorrectCount() + "," + w.getWrongCount());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 세션 불러오기 (간단히 복원)
    public Session loadSession(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String userLine = br.readLine();
            if (userLine == null) return null;
            String[] userTokens = userLine.split(",");
            String username = userTokens[0];
            int level = Integer.parseInt(userTokens[1]);
            int exp = Integer.parseInt(userTokens[2]);

            List<Word> words = new java.util.ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                String english = tokens[0];
                String meaning = tokens[1];
                int blankIndex = Integer.parseInt(tokens[2]);
                int correctCount = Integer.parseInt(tokens[3]);
                int wrongCount = Integer.parseInt(tokens[4]);
                Word w = new Word(english, meaning, blankIndex);
                for (int i = 0; i < correctCount; i++) w.incrementCorrect();
                for (int i = 0; i < wrongCount; i++) w.incrementWrong();
                words.add(w);
            }
            model.User user = new model.User(username);
            user.addExp(exp); // 기존 경험치 반영
            Session session = new Session(user, words);
            return session;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
