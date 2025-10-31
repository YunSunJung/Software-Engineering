package ui.theme;

import javax.swing.*;
import java.awt.*;

public class Theme {
    public static final Color BG = new Color(245,247,250);
    public static final Color SURFACE = Color.WHITE;
    public static final Color TEXT_PRIMARY = new Color(30,31,34);
    public static final Color TEXT_SECONDARY = new Color(96,105,119);
    public static final Color PRIMARY = new Color(57,119,255);
    public static final Color PRIMARY_DARK = new Color(32,84,230);

    public static void applyGlobalUI(){
        Font base = new Font("Malgun Gothic", Font.PLAIN, 14);
        UIManager.put("defaultFont", base);
        UIManager.put("Panel.background", BG);
        System.setProperty("awt.useSystemAAFontSettings","on");
        System.setProperty("swing.aatext","true");
    }
}
