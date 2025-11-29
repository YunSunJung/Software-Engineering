package scheduler;
import model.QuizItem;
import java.util.List;

public interface QuestionScheduler {
    void init(List<QuizItem> items);
    QuizItem next();
    void feedback(QuizItem item, boolean correct);
}
