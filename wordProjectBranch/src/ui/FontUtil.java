package ui;

import java.awt.*;
import java.util.LinkedHashSet;

public class FontUtil {
    private static final String[] JP = {"Noto Sans CJK JP","Noto Sans JP","Yu Gothic UI","Yu Gothic","Meiryo","MS PGothic","MS Gothic","Dialog"};
    private static final String[] CN = {"Noto Sans CJK SC","Noto Sans SC","Microsoft YaHei","SimSun","PingFang SC","Source Han Sans SC","Dialog"};
    private static final String[] KR = {"Malgun Gothic","Noto Sans CJK KR","Noto Sans KR","Dialog"};

    public static Font pickJP(int style,int size){ return pick(JP,style,size); }
    public static Font pickCN(int style,int size){ return pick(CN,style,size); }
    public static Font pickKR(int style,int size){ return pick(KR,style,size); }

    private static Font pick(String[] names,int style,int size){
        LinkedHashSet<String> set=new LinkedHashSet<>();
        for(String f: GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames())
            set.add(f);
        for(String n: names) if(set.contains(n)) return new Font(n,style,size);
        return new Font("Dialog",style,size);
    }
}
