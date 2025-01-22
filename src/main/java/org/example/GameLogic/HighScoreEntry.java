package org.example.GameLogic;

public class HighScoreEntry {

    private final String name;
    private final int score;
    private final int elapsedTime;

    public HighScoreEntry(String name, int score, int elapsedTime) {
        this.name = name;
        this.score = score;
        this.elapsedTime = elapsedTime;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public int getElapsedTime(){
        return elapsedTime;
    }
}
