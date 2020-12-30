package com.ex.viewModels;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

public class Leader {
    private int userID;
    private String firstName;
    private String lastName;
    private int skillID;
    private String skillName;
    private int points;

    public Leader(){}

    public Leader(int userID, String firstName, String lastName, int skillID, String skillName, int points) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.skillID = skillID;
        this.skillName = skillName;
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getSkillID() {
        return skillID;
    }

    public void setSkillID(int skillID) {
        this.skillID = skillID;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    @Override
    public String toString() {
        return "Leader{" +
                "userID=" + userID +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", skillID=" + skillID +
                ", skillName='" + skillName + '\'' +
                ", points=" + points +
                '}';
    }
}
