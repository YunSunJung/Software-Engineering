package service;

import java.io.*; import java.nio.charset.StandardCharsets; import java.util.*;

public class ScoreService {
    public static class Entry { public final String username; public final int correct; public Entry(String u,int c){username=u;correct=c;} }
    private final File scoresFile;

    public ScoreService(File baseDir){ this.scoresFile=new File(baseDir,"scores.csv"); ensure(); }
    private void ensure(){
        if(!scoresFile.exists()){
            try(Writer w=new OutputStreamWriter(new FileOutputStream(scoresFile),StandardCharsets.UTF_8)){
                w.write("username,correct\n");
            }catch(IOException ignored){}
        }
    }
    public synchronized void addCorrect(String u,int d){
        Map<String,Integer> m=readAll(); m.put(u,Math.max(0,m.getOrDefault(u,0)+Math.max(0,d))); saveAll(m);
    }
    public synchronized List<Entry> topN(int n){
        Map<String,Integer> m=readAll(); List<Entry> list=new ArrayList<>();
        for(Map.Entry<String,Integer> e:m.entrySet()) list.add(new Entry(e.getKey(),e.getValue()));
        list.sort((a,b)->Integer.compare(b.correct,a.correct));
        return list.size()>n?list.subList(0,n):list;
    }
    private Map<String,Integer> readAll(){
        Map<String,Integer> m=new HashMap<>();
        try(BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(scoresFile),StandardCharsets.UTF_8))){
            String line; boolean h=true;
            while((line=br.readLine())!=null){
                if(h){h=false;continue;}
                String[] p=line.split(",",-1);
                if(p.length>=2) try{ m.put(p[0].trim(), Integer.parseInt(p[1].trim())); }catch(Exception ignored){}
            }
        }catch(Exception ignored){}
        return m;
    }
    private void saveAll(Map<String,Integer> m){
        try(Writer w=new OutputStreamWriter(new FileOutputStream(scoresFile,false),StandardCharsets.UTF_8)){
            w.write("username,correct\n");
            for(Map.Entry<String,Integer> e:m.entrySet()) w.write(e.getKey()+","+e.getValue()+"\n");
        }catch(IOException ignored){}
    }
}
