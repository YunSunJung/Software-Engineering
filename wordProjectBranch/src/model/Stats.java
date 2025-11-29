package model;

import java.util.HashMap;
import java.util.Map;

public class Stats {
    public int total = 0, correct = 0, streak = 0;
    private final Map<String, Integer> wrongCounts = new HashMap<>();

    public void record(QuizItem item, boolean ok) {
        total++;
        if (ok) { correct++; streak++; }
        else { streak = 0; wrongCounts.merge(item.getId(), 1, Integer::sum); }
    }
    public int getWrongCount(String id) { return wrongCounts.getOrDefault(id, 0); }
    public double accuracy() { return total == 0 ? 0.0 : (correct * 100.0 / total); }
}
