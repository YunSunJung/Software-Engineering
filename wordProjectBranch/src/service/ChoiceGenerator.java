package service;

import model.SentenceItem;
import java.util.*;

public class ChoiceGenerator {
    public List<String> makeChoices(SentenceItem target, List<SentenceItem> pool){
        Set<String> choices = new LinkedHashSet<>();
        choices.add(target.getAnswer());

        List<SentenceItem> cands = new ArrayList<>();
        for(SentenceItem s: pool){
            if(s==target) continue;
            if(s.isSlang()!=target.isSlang()) continue;
            if(!Objects.equals(s.getTags(), target.getTags())) continue;
            cands.add(s);
        }
        cands.sort((a,b)-> Double.compare(sim(b.getAnswer(), target.getAnswer()), sim(a.getAnswer(), target.getAnswer())));
        for(SentenceItem s: cands){ if(choices.size()>=4) break; choices.add(s.getAnswer()); }

        if(choices.size()<4){
            List<SentenceItem> rest = new ArrayList<>(pool);
            Collections.shuffle(rest);
            for(SentenceItem s: rest){ if(choices.size()>=4) break; choices.add(s.getAnswer()); }
        }
        List<String> out = new ArrayList<>(choices);
        Collections.shuffle(out);
        return out.subList(0, Math.min(4,out.size()));
    }

    private double sim(String a, String b){
        if(a==null||b==null) return 0;
        a=a.toLowerCase(); b=b.toLowerCase();
        int common=0; for(char ch: a.toCharArray()) if(b.indexOf(ch)>=0) common++;
        return common/(double)Math.max(1, Math.max(a.length(), b.length()));
    }
}
