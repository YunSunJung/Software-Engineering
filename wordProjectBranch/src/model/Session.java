package model;
import scheduler.QuestionScheduler;
public class Session {
    private QuizItem current; private final QuestionScheduler scheduler;
    public Session(QuestionScheduler scheduler){ this.scheduler=scheduler; }
    public QuizItem next(){ current=scheduler.next(); return current; }
    public QuizItem current(){ return current; } public QuestionScheduler scheduler(){ return scheduler; }
}
