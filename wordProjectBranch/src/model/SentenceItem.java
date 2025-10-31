package model;

public class SentenceItem implements QuizItem {
    private final String sid;
    private final LangPair langPair;
    private final String srcKo, answerKo;
    private final String transEn, transJa, transCn;
    private final boolean slang;
    private final String tags;

    public SentenceItem(String sid, LangPair pair, String srcKo, String answerKo,
                        String en, String ja, String cn, boolean slang, String tags) {
        this.sid = sid; this.langPair = pair; this.srcKo = srcKo; this.answerKo = answerKo;
        this.transEn = en; this.transJa = ja; this.transCn = cn; this.slang = slang; this.tags = tags;
    }
    public static SentenceItem of(String sid, LangPair pair, String srcKo, String answerKo,
                                  String en, String ja, String cn, boolean slang, String tags) {
        return new SentenceItem(sid, pair, srcKo, answerKo, en, ja, cn, slang, tags);
    }

    public String getMaskedKo() { return srcKo.replace(answerKo, "(   )"); }
    public String getLearnerTranslation() {
        switch (langPair) {
            case KO_JP: return transJa != null ? transJa : "";
            case KO_CN: return transCn != null ? transCn : "";
            default:    return transEn != null ? transEn : "";
        }
    }

    @Override public String getId() { return sid; }
    @Override public String getDisplayText() { return getMaskedKo() + "\n\n" + getLearnerTranslation(); }
    @Override public String getAnswer() { return answerKo; }
    @Override public boolean isSlang() { return slang; }

    public LangPair getLangPair() { return langPair; }
    public String getTags() { return tags; }
}
