package service;

import model.*;
import java.io.*; import java.nio.charset.StandardCharsets; import java.security.MessageDigest; import java.util.*;

public class AuthService {
    private final File baseDir; private final File usersFile;

    public AuthService(){
        this.baseDir = new File(System.getProperty("user.home"), ".smsv");
        if(!baseDir.exists()) baseDir.mkdirs();
        this.usersFile = new File(baseDir, "users.csv");
        if(!usersFile.exists()){
            try(Writer w=new OutputStreamWriter(new FileOutputStream(usersFile),StandardCharsets.UTF_8)){
                w.write("username,passwordHash,langPair\n");
            }catch(IOException ignored){}
        }
    }

    public boolean signup(String u, String p, LangPair lp){
        if(u==null||u.trim().isEmpty()||p==null||p.isEmpty()) return false;
        Map<String,String[]> m = readAll(); if(m.containsKey(u)) return false;
        String hash=sha256(p);
        try(Writer w=new OutputStreamWriter(new FileOutputStream(usersFile,true),StandardCharsets.UTF_8)){
            w.write(u+","+hash+","+lp.name()+"\n"); return true;
        }catch(IOException e){ return false; }
    }

    public UserProfile login(String u, String p){
        Map<String,String[]> m = readAll(); if(!m.containsKey(u)) return null;
        String[] rec = m.get(u); if(!sha256(p).equals(rec[1])) return null;
        UserProfile up = new UserProfile(u);
        String norm = rec[2]!=null?rec[2].trim().toUpperCase().replace("-","_"):"KO_EN";
        if(norm.equals("KO_ZH")) norm="KO_CN";
        if(norm.equals("KO_JA")) norm="KO_JP";
        try{ up.setPreferredLangPair(LangPair.valueOf(norm)); }catch(Exception ignored){}
        return up;
    }

    private Map<String,String[]> readAll(){
        Map<String,String[]> map=new HashMap<>();
        try(BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(usersFile),StandardCharsets.UTF_8))){
            String line; boolean head=true;
            while((line=br.readLine())!=null){
                if(head){ head=false; continue; }
                String[] p=line.split(",",-1);
                if(p.length>=3) map.put(p[0],p);
            }
        }catch(Exception ignored){}
        return map;
    }

    private static String sha256(String s){
        try{
            MessageDigest md=MessageDigest.getInstance("SHA-256");
            byte[] b=md.digest(s.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb=new StringBuilder();
            for(byte x:b) sb.append(String.format("%02x",x));
            return sb.toString();
        }catch(Exception e){ return ""; }
    }

    public File getBaseDir(){ return baseDir; }
}
