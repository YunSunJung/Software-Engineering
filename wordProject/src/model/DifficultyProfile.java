package model;

public class DifficultyProfile {
    private Word word;
    private double weight; // 출제 확률 가중치

    public DifficultyProfile(Word word, double weight) {
        this.word = word;
        this.weight = weight;
    }

    // 가중치 계산 예시: 오답 많으면 높아짐
    public void updateWeight() {
        int wrongCount = word.getWrongCount();
        weight = 1.0 + wrongCount * 0.5; // 예: 오답 1회마다 0.5 증가
    }

    // Getter/Setter
    public Word getWord() { return word; }
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
}
