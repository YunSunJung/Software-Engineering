package scheduler;

import model.QuizItem;
import java.util.*;

public class SM2Scheduler implements QuestionScheduler {
    private static class Card {
        QuizItem item; int rep = 0; double interval = 1; double ease = 2.5; long due = 0;
    }
    private final List<Card> cards = new ArrayList<>();
    private final Random rnd = new Random();

    @Override public void init(List<QuizItem> items) {
        cards.clear(); long now = System.currentTimeMillis();
        for (QuizItem q : items) { Card c = new Card(); c.item = q; c.due = now; cards.add(c); }
    }

    @Override public QuizItem next() {
        long now = System.currentTimeMillis();
        List<Card> dueCards = new ArrayList<>();
        for (Card c : cards) if (c.due <= now) dueCards.add(c);
        if (dueCards.isEmpty()) dueCards.addAll(cards);
        return dueCards.get(rnd.nextInt(dueCards.size())).item;
    }

    @Override public void feedback(QuizItem item, boolean ok) {
        Card c = null; for (Card cc : cards) if (cc.item == item) { c = cc; break; } if (c == null) return;
        double q = ok ? 4.0 : 2.0;
        c.ease = Math.max(1.3, c.ease + (0.1 - (5 - q) * (0.08 + (5 - q) * 0.02)));
        if (!ok) { c.rep = 0; c.interval = 1; }
        else {
            c.rep++;
            if (c.rep == 1) c.interval = 1;
            else if (c.rep == 2) c.interval = 6;
            else c.interval = Math.ceil(c.interval * c.ease);
        }
        long dayMs = 24L * 60 * 60 * 1000;
        c.due = System.currentTimeMillis() + (long) (c.interval * dayMs);
    }
}
