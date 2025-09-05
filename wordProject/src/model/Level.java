package model;

public class Level {
    private int levelNumber;
    private int requiredExp;

    public Level(int levelNumber) {
        this.levelNumber = levelNumber;
        this.requiredExp = calculateRequiredExp(levelNumber);
    }

    // 레벨에 따른 요구 경험치 계산
    private int calculateRequiredExp(int levelNumber) {
        return levelNumber * 10; // 예: 레벨 * 10 경험치
    }

    // Getter
    public int getLevelNumber() { return levelNumber; }
    public int getRequiredExp() { return requiredExp; }

    // 레벨업
    public void levelUp() {
        levelNumber++;
        requiredExp = calculateRequiredExp(levelNumber);
    }
}
