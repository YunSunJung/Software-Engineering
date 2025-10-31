package model;
public class Word implements QuizItem {
    private final String id; private final String english; private final String meaning; private int blankIndex;
    public Word(String english, String meaning, int blankIndex){ this.english=english; this.meaning=meaning; this.blankIndex=Math.max(0, Math.min(blankIndex, Math.max(0, english.length()-1))); this.id=english+"#"+this.blankIndex; }
    public boolean checkAnswer(char c){ return english.charAt(blankIndex)==c; }
    public String getDisplayWord(){ if(english.isEmpty()) return english; StringBuilder sb=new StringBuilder(english); sb.setCharAt(blankIndex,'('); sb.insert(blankIndex+1,')'); return sb.toString(); }
    @Override public String getId(){ return id; } @Override public boolean isSlang(){ return false; }
    @Override public String getDisplayText(){ return getDisplayWord() + " (" + meaning + ")"; }
    @Override public String getAnswer(){ return String.valueOf(english.charAt(blankIndex)); }
}
