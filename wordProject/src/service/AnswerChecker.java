package service;

import model.Word;

public class AnswerChecker {

    // 입력 문자와 단어 빈칸 확인
    public boolean checkAnswer(Word word, char input) {
        boolean isCorrect = word.checkAnswer(input);
        if (isCorrect) {
            word.incrementCorrect();
        } else {
            word.incrementWrong();
        }
        return isCorrect;
    }
}
