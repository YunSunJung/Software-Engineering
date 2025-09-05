package service;

import model.QuizResult;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class LogSaver {

    private String logFilePath;

    public LogSaver(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    // 한 문제 결과를 로그 파일에 기록
    public void logAnswer(QuizResult result) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(logFilePath, true))) {
            String logLine = String.format(
                    "%s, Word: %s, Answer: %c, Correct: %s",
                    LocalDateTime.now(),
                    result.getWord().getEnglish(),
                    result.getAnswer(),
                    result.isCorrect()
            );
            bw.write(logLine);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
