package service;

import java.util.Timer;
import java.util.TimerTask;

public class TimerService {
    private Timer timer;
    private boolean timeUp;

    public TimerService() {
        timer = new Timer();
        timeUp = false;
    }

    // 타이머 시작
    public void startTimer(int seconds) {
        timeUp = false;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timeUp = true;
            }
        }, seconds * 1000);
    }

    // 타이머 중지
    public void stopTimer() {
        timer.cancel();
        timer = new Timer();
        timeUp = false;
    }

    // 시간 초과 여부
    public boolean isTimeUp() {
        return timeUp;
    }
}
