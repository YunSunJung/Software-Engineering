package model;

public class HistoryRecord {
    private Word word;
    private int correctCount;
    private int wrongCount;

    public HistoryRecord(Word word) {
        this.word = word;
        this.correctCount = 0;
        this.wrongCount = 0;
    }

    // 정답/오답 기록
    public void recordCorrect() {
        correctCount++;
    }

    public void recordWrong() {
        wrongCount++;
    }

    // Getter
    public Word getWord() { return word; }
    public int getCorrectCount() { return correctCount; }
    public int getWrongCount() { return wrongCount; }
}
