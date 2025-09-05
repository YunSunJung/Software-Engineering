package service;


import model.Session;
import model.Word;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ReportGenerator {

    // 세션 결과를 파일로 보고서 생성
    public void generateReport(Session session, String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write("사용자: " + session.getUser().getUsername() + "\n");
            bw.write("레벨: " + session.getUser().getLevel() + "\n");
            bw.write("경험치: " + session.getUser().getExp() + "\n");
            bw.write("총 문제 수: " + session.getQuizWords().size() + "\n\n");

            List<Word> words = session.getQuizWords();
            for (Word w : words) {
                bw.write(String.format(
                        "단어: %s, 뜻: %s, 정답횟수: %d, 오답횟수: %d\n",
                        w.getEnglish(),
                        w.getMeaning(),
                        w.getCorrectCount(),
                        w.getWrongCount()
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}