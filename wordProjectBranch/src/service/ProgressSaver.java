package service;

import model.*;
import java.io.*; import java.nio.charset.StandardCharsets;

public class ProgressSaver {
    private final File file;
    public ProgressSaver(File baseDir, String username){ this.file=new File(baseDir, "progress_"+username+".csv"); }
    public void save(UserProfile user, Stats stats, String currentId){
        try(Writer w=new OutputStreamWriter(new FileOutputStream(file,false),StandardCharsets.UTF_8)){
            w.write("total,correct,streak,currentId,langPair,slang\n");
            w.write(stats.total+","+stats.correct+","+stats.streak+","+(currentId==null?"":currentId)+","+user.getPreferredLangPair()+","+(user.isSlangMode()?"1":"0")+"\n");
        }catch(IOException ignored){}
    }
}
