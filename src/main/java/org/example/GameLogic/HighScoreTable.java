package org.example.GameLogic;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HighScoreTable {

    HighScoreEntry highScoreEntry;
    private final List<HighScoreEntry> highScores = new ArrayList<>();
    private final String filePath;

    public HighScoreTable(String filePath){
        this.filePath = filePath;
        loadHighScores();
    }

    public void addHighScore(String playerName, int score){
        highScores.add(new HighScoreEntry(playerName, score));
        highScores.sort((a,b) -> Integer.compare(b.getScore(), a.getScore()));
        if (highScores.size() > 10) {
            highScores.removeLast();
        }
        saveHighScores();
    }

    public List<HighScoreEntry> getHighScores() {
        return highScores;
    }

    private void saveHighScores() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (HighScoreEntry entry : highScores){
                writer.println(entry.getName() + "," + entry.getScore());
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadHighScores() {
        File file = new File(filePath);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    highScores.add(new HighScoreEntry(parts[0], Integer.parseInt(parts[1])));
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
