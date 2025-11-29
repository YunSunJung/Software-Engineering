package service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

/** 이벤트 로그에서 최근 N일 정확도(%)를 일자별로 계산해 라인차트용 시계열로 반환 */
public class AnalyticsService {
    private final File eventsFile;
    public AnalyticsService(File baseDir){ this.eventsFile = new File(baseDir, "events.csv"); }

    /** 최근 days일의 정확도. 누락된 날짜는 0%로 채움(라인이 끊기지 않도록) */
    public LinkedHashMap<LocalDate, Double> accuracySeries(int days){
        LinkedHashMap<LocalDate, Double> series = new LinkedHashMap<>();
        if (days <= 0) return series;

        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(days - 1);
        // 빈 시계열(0%)로 먼저 초기화
        for (LocalDate d = start; !d.isAfter(today); d = d.plusDays(1))
            series.put(d, 0.0);

        if (!eventsFile.exists()) return series;

        // 집계 버킷: 일자 -> [총 시도, 정답 수]
        Map<LocalDate, int[]> bucket = new HashMap<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(eventsFile), StandardCharsets.UTF_8))) {
            String line; boolean header = true;
            while ((line = br.readLine()) != null) {
                if (header) { header = false; continue; }
                String[] p = line.split(",", -1);
                if (p.length < 4) continue;
                // p[0] = "yyyy-MM-dd HH:mm:ss"
                LocalDate day;
                try { day = LocalDate.parse(p[0].substring(0, 10)); }
                catch (Exception ignore) { continue; }
                if (day.isBefore(start) || day.isAfter(today)) continue;

                int[] arr = bucket.computeIfAbsent(day, k -> new int[2]);
                arr[0]++;                     // total
                if ("1".equals(p[3])) arr[1]++; // correct
            }
        } catch (IOException ignore) { /* 파일 없거나 읽기 실패 시 0% 유지 */ }

        // 정확도 갱신
        for (Map.Entry<LocalDate, int[]> e : bucket.entrySet()) {
            int total = e.getValue()[0], ok = e.getValue()[1];
            double acc = total == 0 ? 0.0 : (ok * 100.0 / total);
            series.put(e.getKey(), acc);
        }
        return series;
    }
}
