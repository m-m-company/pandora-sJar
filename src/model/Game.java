package model;

import java.util.Map;

public class Game {

    private String name;
    private String path;
    private Map<String,Integer> ranks;

    public String getPath() {
        return path;
    }

    public Game(String name, String path, Map<String, Integer> ranks) {
        this.name = name;
        this.path = path;
        this.ranks = ranks;
    }
    public Game(String name, String path) {
        this.name = name;
        this.path = path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, Integer> getRanks() {
        return ranks;
    }

    public void setRanks(Map<String, Integer> ranks) {
        this.ranks = ranks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
