package service;

import model.User;

public class LevelManager {

    // 경험치 추가 및 레벨업 처리
    public void addExp(User user, int exp) {
        user.addExp(exp);
    }

    // 레벨업 여부 확인
    public boolean checkLevelUp(User user) {
        int currentLevel = user.getLevel();
        int exp = user.getExp();
        int requiredExp = currentLevel * 10; // 예: 레벨 * 10 필요 경험치
        return exp >= requiredExp;
    }
}
