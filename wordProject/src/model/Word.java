package model;

public class Word {
    private String english;
    private String meaning;
    private int blankIndex; // 빈칸 위치 (0부터 시작)
    private int correctCount;
    private int wrongCount;

    public Word(String english, String meaning, int blankIndex) {
        this.english = english;
        this.meaning = meaning;
        this.blankIndex = blankIndex;
        this.correctCount = 0;
        this.wrongCount = 0;
    }

    // 사용자가 입력한 문자와 빈칸 문자가 맞는지 확인
    public boolean checkAnswer(char c) {
        return english.charAt(blankIndex) == c;
    }

    // 정답/오답 카운트 증가
    public void incrementCorrect() {
        correctCount++;
    }

    public void incrementWrong() {
        wrongCount++;
    }

    // 문제 표시용 문자열 (예: ap()le)
    public String getDisplayWord() {
        StringBuilder sb = new StringBuilder(english);
        sb.setCharAt(blankIndex, '(');
        sb.insert(blankIndex + 1, ')');
        return sb.toString();
    }

    // Getter 메서드
    public String getEnglish() { return english; }
    public String getMeaning() { return meaning; }
    public int getBlankIndex() { return blankIndex; }
    public int getCorrectCount() { return correctCount; }
    public int getWrongCount() { return wrongCount; }
}
