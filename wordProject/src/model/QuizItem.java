package model;

public interface QuizItem {
    String getId();
    boolean isSlang();
    String getDisplayText(); // 질문에 표시될 텍스트(문장/단어)
    String getAnswer();      // 정답 토큰
}
