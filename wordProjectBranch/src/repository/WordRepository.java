package repository;

import model.DBConfig;
import model.Word;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WordRepository {

    public List<Word> findAll() {
        List<Word> list = new ArrayList<>();

        String sql = "SELECT english, meaning FROM WORDS";

        try {
            Connection conn = DBConfig.getConnection();  // ← 여기!
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String eng = rs.getString("english");
                String kor = rs.getString("meaning");

                list.add(new Word(eng, kor, 1));
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
