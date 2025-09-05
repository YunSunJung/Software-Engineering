package service;

import model.Word;
import java.util.List;
import java.util.Random;

public class QuestionGenerator {

    private Random random = new Random();

    // 단어 목록에서 랜덤으로 단어 선택
    public Word generateNextWord(List<Word> words) {
        if (words == null || words.isEmpty()) return null;
        int index = random.nextInt(words.size());
        return words.get(index);
    }

    // 가중치 기반 랜덤 선택 (DifficultyProfile과 연동 가능)
    public Word generateWeightedWord(List<Word> words, List<Double> weights) {
        if (words == null || words.isEmpty() || weights == null || weights.isEmpty()) return null;

        double totalWeight = 0.0;
        for (double w : weights) totalWeight += w;

        double r = random.nextDouble() * totalWeight;
        double cumulative = 0.0;
        for (int i = 0; i < words.size(); i++) {
            cumulative += weights.get(i);
            if (r <= cumulative) return words.get(i);
        }
        return words.get(words.size() - 1);
    }
}
