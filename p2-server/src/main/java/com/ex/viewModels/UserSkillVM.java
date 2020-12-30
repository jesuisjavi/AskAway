package com.ex.viewModels;

public class UserSkillVM {
    private int skillID;
    private String name;
    private int points;

    public UserSkillVM(int skillID, String name, int points) {
        this.skillID = skillID;
        this.name = name;
        this.points = points;
    }

    public UserSkillVM() {

    }

    public int getSkillID() {
        return skillID;
    }

    public void setSkillID(int skillID) {
        this.skillID = skillID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
