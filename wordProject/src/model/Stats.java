package model;

public class Stats {
    private int totalQuestions;
    private int correctAnswers;
    private int wrongAnswers;

    public Stats() {
        this.totalQuestions = 0;
        this.correctAnswers = 0;
        this.wrongAnswers = 0;
    }

    // 결과 기록
    public void recordResult(boolean isCorrect) {
        totalQuestions++;
        if (isCorrect) {
            correctAnswers++;
        } else {
            wrongAnswers++;
        }
    }

    // 정확도 계산
    public double getAccuracy() {
        if (totalQuestions == 0) return 0.0;
        return (double) correctAnswers / totalQuestions * 100;
    }

    // Getter
    public int getTotalQuestions() { return totalQuestions; }
    public int getCorrectAnswers() { return correctAnswers; }
    public int getWrongAnswers() { return wrongAnswers; }
}