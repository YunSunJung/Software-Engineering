package model;

public class User {
    private String username;
    private int level;
    private int exp;

    public User(String username) {
        this.username = username;
        this.level = 1;
        this.exp = 0;
    }

    // 경험치 추가
    public void addExp(int exp) {
        this.exp += exp;
        checkLevelUp();
    }

    // 레벨업 확인
    private void checkLevelUp() {
        int requiredExp = level * 10; // 예: 레벨*10 경험치 필요
        while (this.exp >= requiredExp) {
            this.exp -= requiredExp;
            level++;
            requiredExp = level * 10;
            System.out.println(username + "님 레벨업! 현재 레벨: " + level);
        }
    }

    // Getter 메서드
    public String getUsername() { return username; }
    public int getLevel() { return level; }
    public int getExp() { return exp; }
}