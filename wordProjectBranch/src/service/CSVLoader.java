package service;

import model.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CSVLoader {
    private static List<String> parseCSVLine(String line){
        List<String> out = new ArrayList<>(); if(line==null) return out;
        StringBuilder sb = new StringBuilder(); boolean q=false;
        for(int i=0;i<line.length();i++){
            char c=line.charAt(i);
            if(c=='\"'){
                if(q && i+1<line.length() && line.charAt(i+1)=='\"'){ sb.append('\"'); i++; }
                else q=!q;
            } else if(c==',' && !q){ out.add(sb.toString()); sb.setLength(0); }
            else sb.append(c);
        }
        out.add(sb.toString()); return out;
    }
    private static String unq(String s){
        if(s==null) return ""; s=s.trim();
        if(s.length()>=2 && s.charAt(0)=='\"' && s.charAt(s.length()-1)=='\"') s=s.substring(1,s.length()-1);
        return s;
    }

    public List<SentenceItem> loadSentences(String name){
        InputStream is = getClass().getClassLoader().getResourceAsStream(name);
        if(is==null){ System.err.println("missing "+name); return Collections.emptyList(); }
        return loadSentences(is);
    }

    public List<SentenceItem> loadSentences(InputStream is){
        List<SentenceItem> list = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){
            String header = br.readLine(); if(header==null) return list;
            String line;
            while((line=br.readLine())!=null){
                List<String> p = parseCSVLine(line); if(p.size()<9) continue;
                String sid=unq(p.get(0));
                String lpRaw=unq(p.get(1)).toUpperCase().replace("-","_");
                if(lpRaw.equals("KO_ZH")) lpRaw="KO_CN";
                if(lpRaw.equals("KO_JA")) lpRaw="KO_JP";
                LangPair pair; try{ pair=LangPair.valueOf(lpRaw);}catch(Exception e){ continue; }
                String srcKo=unq(p.get(2)), ans=unq(p.get(3));
                String en=unq(p.get(4)), ja=unq(p.get(5)), cn=unq(p.get(6));
                boolean slang="true".equalsIgnoreCase(p.get(7).trim());
                String tags=unq(p.get(8));
                if(srcKo.isEmpty()||ans.isEmpty()) continue;
                list.add(SentenceItem.of(sid,pair,srcKo,ans,en,ja,cn,slang,tags));
            }
        }catch(Exception e){ System.err.println("loadSentences error: "+e.getMessage()); }
        return list;
    }
}
