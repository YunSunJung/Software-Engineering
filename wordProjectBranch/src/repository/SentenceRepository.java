package repository;

import model.DBConfig;
import model.LangPair;
import model.SentenceItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SentenceRepository {

    public List<SentenceItem> findBySlangMode(boolean isSlang) {
        List<SentenceItem> list = new ArrayList<>();

        String sql =
                "SELECT SID, SRC_KO, ANSWER_KO, " +
                "TRANS_EN, TRANS_JA, TRANS_CN, " +
                "LANG_PAIR, IS_SLANG, TAGS " +
                "FROM SENTENCES WHERE IS_SLANG = ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, isSlang ? 1 : 0);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String sid       = rs.getString("SID");
                String srcKo     = rs.getString("SRC_KO");
                String answerKo  = rs.getString("ANSWER_KO");

                String en        = rs.getString("TRANS_EN");
                String ja        = rs.getString("TRANS_JA");
                String cn        = rs.getString("TRANS_CN");

                String pairStr   = rs.getString("LANG_PAIR");
                LangPair pair    = LangPair.valueOf(pairStr); // KO_EN, KO_JP, KO_CN

                boolean slang    = rs.getInt("IS_SLANG") == 1;

                String tags      = rs.getString("TAGS");

                SentenceItem item =
                        new SentenceItem(sid, pair, srcKo, answerKo, en, ja, cn, slang, tags);

                list.add(item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
