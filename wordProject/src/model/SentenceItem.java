package model;

import java.util.HashSet;
import java.util.Set;

public class SentenceItem implements QuizItem {
    private final String sid;
    public LangPair langPair;
    public String srcText;     // 빈칸 포함 원문 (예: "오늘 ㄹㅇ (   ) 였음.")
    public int blankIndex = -1; // 선택적으로 사용
    public String answer;      // 정답 토큰
    public String transKo, transJa, transEn, transZh;
    public boolean isSlang;
    public Set<String> tags = new HashSet<>();

    public SentenceItem(String sid) { this.sid = sid; }

    @Override public String getId() { return sid; }
    @Override public boolean isSlang() { return isSlang; }
    @Override public String getDisplayText() { return srcText; }
    @Override public String getAnswer() { return answer; }
}
