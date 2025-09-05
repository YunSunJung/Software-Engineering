package service;

import model.Word;
import java.util.ArrayList;
import java.util.List;

public class WrongWordTracker {
    private List<Word> wrongWords;

    public WrongWordTracker() {
        wrongWords = new ArrayList<>();
    }

    // 오답 단어 기록
    public void recordWrongWord(Word word) {
        if (!wrongWords.contains(word)) {
            wrongWords.add(word);
        }
    }

    // 오답 단어 목록 반환
    public List<Word> getWrongWords() {
        return wrongWords;
    }

    // 초기화
    public void clear() {
        wrongWords.clear();
    }
}