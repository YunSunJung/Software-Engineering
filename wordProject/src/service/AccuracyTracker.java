package service;

import model.Stats;

public class AccuracyTracker {

    // Stats 객체에 결과 반영
    public void updateStats(Stats stats, boolean isCorrect) {
        stats.recordResult(isCorrect);
    }

    // 정확도 가져오기
    public double getAccuracy(Stats stats) {
        return stats.getAccuracy();
    }
}
