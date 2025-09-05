package model;

public class QuizResult {
    private Word word;
    private boolean isCorrect;
    private char answer;

    public QuizResult(Word word, boolean isCorrect, char answer) {
        this.word = word;
        this.isCorrect = isCorrect;
        this.answer = answer;
    }

    // Getter
    public Word getWord() { return word; }
    public boolean isCorrect() { return isCorrect; }
    public char getAnswer() { return answer; }
}