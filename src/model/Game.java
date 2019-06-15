package model;

import java.util.ArrayList;

public class Game {

    private String name;
    private String path;
    private ArrayList<Pair<String,Integer>> ranks;

    public String getPath() {
        return path;
    }

    public Game(String name, String path, ArrayList<Pair<String,Integer>> ranks) {
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

    public ArrayList<Pair<String, Integer>> getRanks() {
        return ranks;
    }

    public void setRanks(ArrayList<Pair<String, Integer>> ranks) {
        this.ranks = ranks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
