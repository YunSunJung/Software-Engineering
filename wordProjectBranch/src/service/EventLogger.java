package service;

import java.io.*; import java.nio.charset.StandardCharsets; import java.time.*; import java.time.format.DateTimeFormatter;

public class EventLogger {
    private final File file;
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EventLogger(File baseDir){ this.file = new File(baseDir, "events.csv"); ensure(); }
    private void ensure(){
        if(!file.exists()){
            try(Writer w=new OutputStreamWriter(new FileOutputStream(file),StandardCharsets.UTF_8)){
                w.write("ts,username,itemId,ok\n");
            }catch(IOException ignored){}
        }
    }
    public synchronized void log(String user, String itemId, boolean ok){
        String ts = LocalDateTime.now().format(fmt);
        try(Writer w=new OutputStreamWriter(new FileOutputStream(file,true),StandardCharsets.UTF_8)){
            w.write(ts+","+safe(user)+","+safe(itemId)+","+(ok?"1":"0")+"\n");
        }catch(IOException ignored){}
    }
    private String safe(String s){ return s==null? "": s.replace(","," "); }
    public File getFile(){ return file; }
}
