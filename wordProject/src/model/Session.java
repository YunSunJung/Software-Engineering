package model;

import java.util.List;

public class Session {
    private User user;
    private List<Word> quizWords;
    private int currentIndex;

    public Session(User user, List<Word> quizWords) {
        this.user = user;
        this.quizWords = quizWords;
        this.currentIndex = 0;
    }
    
    public void reset() {
        this.currentIndex = 0;
        // 필요시 단어 섞기 등 초기화 코드 추가
    }

    // 다음 단어 가져오기
    public Word getNextWord() {
        if (hasNext()) {
            return quizWords.get(currentIndex++);
        }
        return null;
    }

    // 더 풀 단어가 있는지 확인
    public boolean hasNext() {
        return currentIndex < quizWords.size();
    }

    // Getter
    public User getUser() { return user; }
    public int getCurrentIndex() { return currentIndex; }
    public List<Word> getQuizWords() { return quizWords; }

    // Setter
    public void setQuizWords(List<Word> quizWords) {
        this.quizWords = quizWords;
        this.currentIndex = 0;
    }
}
