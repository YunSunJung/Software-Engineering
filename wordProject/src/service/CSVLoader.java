package service;

import model.Word;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CSVLoader {

    public List<Word> loadWords(InputStream is) {
        List<Word> wordList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            boolean firstLine = true; // 헤더 건너뛰기
            while ((line = br.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; }
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String english = parts[0].trim();
                    String meaning = parts[1].trim();
                    int difficulty = Integer.parseInt(parts[2].trim());
                    wordList.add(new Word(english, meaning, difficulty));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wordList;
    }

    // 클래스패스에서 읽기 (Main에서 loader.loadWords("words.csv")로 사용)
    public List<Word> loadWords(String filename) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(filename);
        if (is == null) {
            System.err.println("파일을 찾을 수 없습니다: " + filename);
            return new ArrayList<>();
        }
        return loadWords(is);
    }
}
