package ui;

import service.AnalyticsService;
import ui.theme.UIUtil;
import ui.theme.Theme;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

/** 라인차트 기반 학습 분석 패널 */
public class AnalyticsPanel extends JPanel {
    private final AnalyticsService svc;
    private final int days = 10; // 최근 10일

    public AnalyticsPanel(AnalyticsService svc){
        this.svc = svc;
        setLayout(new BorderLayout(8,8));
        setOpaque(false);

        JPanel card = UIUtil.cardPanel(new BorderLayout(8,8));
        JLabel title = new JLabel("최근 학습 정확도(일별)");
        title.setFont(new Font("Malgun Gothic", Font.BOLD, 18));
        title.setForeground(Theme.TEXT_SECONDARY);
        card.add(title, BorderLayout.NORTH);
        card.add(new LineChart(svc, days), BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);
    }

    /** 간단한 라인차트 컴포넌트(축/눈금/그리드/라벨/툴팁) */
    private static class LineChart extends JComponent {
        private final AnalyticsService svc;
        private final int days;
        private final int padLeft = 56, padRight = 24, padTop = 28, padBottom = 48;
        private final DateTimeFormatter df = DateTimeFormatter.ofPattern("MM/dd");

        LineChart(AnalyticsService svc, int days){ this.svc = svc; this.days = days; setOpaque(false); setToolTipText(""); }

        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();
            int cw = Math.max(1, w - padLeft - padRight);
            int ch = Math.max(1, h - padTop - padBottom);
            int x0 = padLeft, y0 = h - padBottom;

            // 배경
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, w, h, 12, 12);

            // 데이터
            LinkedHashMap<LocalDate, Double> series = svc.accuracySeries(days);
            int n = series.size(); // days와 동일(빈 날짜 0% 포함)
            double[] vals = new double[n];
            LocalDate[] daysArr = new LocalDate[n];
            int i = 0; for (Map.Entry<LocalDate, Double> e : series.entrySet()) { daysArr[i] = e.getKey(); vals[i] = e.getValue(); i++; }

            // 축/그리드
            g2.setColor(new Color(230, 235, 243));
            g2.fillRect(x0, y0 - ch, cw, ch); // plot 영역 베이스(아주 옅은 배경)

            g2.setColor(new Color(210, 216, 228));
            for (int yTick = 0; yTick <= 100; yTick += 20) {
                int y = y0 - (int) Math.round(ch * (yTick / 100.0));
                g2.drawLine(x0, y, x0 + cw, y);
            }

            // y축 라벨
            g2.setColor(new Color(120, 130, 145));
            g2.setFont(getFont().deriveFont(Font.PLAIN, 12f));
            for (int yTick = 0; yTick <= 100; yTick += 20) {
                int y = y0 - (int) Math.round(ch * (yTick / 100.0));
                String t = String.valueOf(yTick);
                int tw = g2.getFontMetrics().stringWidth(t);
                g2.drawString(t, x0 - tw - 8, y + 4);
            }
            g2.drawString("정확도 %", x0 - 44, y0 - ch - 10);

            // x축 눈금/라벨
            int step = Math.max(1, n / 5); // 라벨 너무 많지 않게
            g2.setColor(new Color(120, 130, 145));
            for (int idx = 0; idx < n; idx += step) {
                int x = x0 + (int) Math.round(cw * (idx / (double) Math.max(1, n - 1)));
                String label = df.format(daysArr[idx]);
                int tw = g2.getFontMetrics().stringWidth(label);
                g2.drawString(label, x - tw / 2, y0 + 20);
            }

            // 라인/포인트
            Stroke line = new BasicStroke(2.2f);
            g2.setColor(Theme.PRIMARY);
            g2.setStroke(line);

            int prevX = -1, prevY = -1;
            for (int idx = 0; idx < n; idx++) {
                int x = x0 + (int) Math.round(cw * (idx / (double) Math.max(1, n - 1)));
                int y = y0 - (int) Math.round(ch * (vals[idx] / 100.0));
                if (prevX >= 0) g2.drawLine(prevX, prevY, x, y);
                prevX = x; prevY = y;
            }

            // 포인트(동그라미)
            g2.setColor(new Color(57,119,255,200));
            for (int idx = 0; idx < n; idx++) {
                int x = x0 + (int) Math.round(cw * (idx / (double) Math.max(1, n - 1)));
                int y = y0 - (int) Math.round(ch * (vals[idx] / 100.0));
                g2.fillOval(x - 4, y - 4, 8, 8);
            }

            g2.dispose();
        }

        // 마우스 위치에 툴팁(날짜/정확도) 표시
        @Override public String getToolTipText(java.awt.event.MouseEvent event) {
            LinkedHashMap<LocalDate, Double> series = svc.accuracySeries(days);
            if (series.isEmpty()) return null;
            int w = getWidth(), h = getHeight();
            int cw = Math.max(1, w - padLeft - padRight);
            int x0 = padLeft;

            int n = series.size();
            LocalDate[] daysArr = series.keySet().toArray(new LocalDate[0]);
            Double[] vals = series.values().toArray(new Double[0]);

            // x좌표를 index로 역추정
            int mx = event.getX();
            double t = (mx - x0) / (double) Math.max(1, cw);
            int idx = (int) Math.round(t * (n - 1));
            idx = Math.max(0, Math.min(n - 1, idx));

            return daysArr[idx].format(df) + "  •  " + String.format("%.1f%%", vals[idx]);
        }
    }
}
