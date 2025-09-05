package service;

import model.Word;
import java.util.List;

public class WeightAdjuster {

    // 단어별 가중치 계산 (오답 횟수가 많을수록 확률 증가)
    public double calculateWeight(Word word) {
        int wrongCount = word.getWrongCount();
        return 1.0 + wrongCount * 0.5; // 예: 오답 1회마다 0.5 증가
    }

    // 단어 목록 전체 가중치 계산
    public double[] calculateWeights(List<Word> words) {
        double[] weights = new double[words.size()];
        for (int i = 0; i < words.size(); i++) {
            weights[i] = calculateWeight(words.get(i));
        }
        return weights;
    }
}
