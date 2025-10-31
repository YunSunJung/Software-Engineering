package model;

public class Word implements QuizItem {
    private final String id;       // 스케줄러/통계용 고유 ID
    private final String english;
    private final String meaning;
    private int blankIndex;        // 빈칸 위치 (0부터 시작)
    private int correctCount;
    private int wrongCount;

    public Word(String english, String meaning, int blankIndex) {
        this.english = english;
        this.meaning = meaning;
        this.blankIndex = blankIndex;
        this.correctCount = 0;
        this.wrongCount = 0;
        this.id = english + "#" + blankIndex; // 간단한 고유값
    }

    // ====== 기존 기능 유지 ======

    // 사용자가 입력한 문자와 빈칸 문자가 맞는지 확인
    public boolean checkAnswer(char c) {
        return english.charAt(blankIndex) == c;
    }

    // 정답/오답 카운트 증가
    public void incrementCorrect() { correctCount++; }
    public void incrementWrong()   { wrongCount++; }

    // 문제 표시용 문자열 (예: "apple" + index=2 -> "ap()le")
    public String getDisplayWord() {
        StringBuilder sb = new StringBuilder(english);
        sb.setCharAt(blankIndex, '(');
        sb.insert(blankIndex + 1, ')');
        return sb.toString();
    }

    // ====== QuizItem 인터페이스 구현(새 구조와 호환) ======

    @Override
    public String getId() { return id; }

    @Override
    public boolean isSlang() { return false; } // 단어장 기본 모드는 슬랭 아님

    @Override
    public String getDisplayText() {
        // UI에 “문제 + (뜻)” 형태로 노출
        return getDisplayWord() + " (" + meaning + ")";
    }

    @Override
    public String getAnswer() {
        // AnswerChecker가 문자열 비교하므로 문자 → 문자열로 반환
        return String.valueOf(english.charAt(blankIndex));
    }

    // ====== Getter/Setter ======
    public String getEnglish() { return english; }
    public String getMeaning() { return meaning; }
    public int getBlankIndex() { return blankIndex; }
    public int getCorrectCount() { return correctCount; }
    public int getWrongCount() { return wrongCount; }

    // 필요 시 빈칸 위치를 재설정(난이도 조절/랜덤화 등)
    public void setBlankIndex(int blankIndex) {
        if (blankIndex < 0 || blankIndex >= english.length())
            throw new IllegalArgumentException("blankIndex out of range");
        this.blankIndex = blankIndex;
    }
}
