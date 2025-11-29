package scheduler;

import model.QuizItem;
import java.util.*;

public class WrongBiasScheduler implements QuestionScheduler {
    private final Random rnd = new Random();
    private final Map<String, Integer> wrong = new HashMap<>();
    private List<QuizItem> pool = new ArrayList<>();

    @Override public void init(List<QuizItem> items) { pool = new ArrayList<>(items); }

    @Override public QuizItem next() {
        if (pool.isEmpty()) return null;
        pool.sort((a,b) -> Integer.compare(wrong.getOrDefault(b.getId(),0), wrong.getOrDefault(a.getId(),0)));
        return pool.get(rnd.nextInt(Math.min(5, pool.size())));
    }

    @Override public void feedback(QuizItem item, boolean ok) {
        if (!ok) wrong.merge(item.getId(), 1, Integer::sum);
        else wrong.put(item.getId(), Math.max(0, wrong.getOrDefault(item.getId(),0) - 1));
    }
}
