package model;

public class UserProfile {
    private final String username;
    private String passwordHash;
    private LangPair preferredLangPair = LangPair.KO_EN;
    private boolean slangMode = false;

    public UserProfile(String username) { this.username = username; }

    public String getUsername() { return username; }
    public void setPasswordHash(String h) { this.passwordHash = h; }
    public String getPasswordHash() { return passwordHash; }

    public LangPair getPreferredLangPair() { return preferredLangPair; }
    public void setPreferredLangPair(LangPair lp) { this.preferredLangPair = lp; }

    public boolean isSlangMode() { return slangMode; }
    public void setSlangMode(boolean b) { this.slangMode = b; }
}
