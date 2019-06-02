package model;

import java.util.Map;

public class Game {

    private String name;
    private String imagePath;
    private Map<String,Integer> ranks;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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
