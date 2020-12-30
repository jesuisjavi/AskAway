package com.ex.viewModels;

public class SelectTopAnswerForm {
    private int userID;
    private int answerID;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getAnswerID() {
        return answerID;
    }

    public void setAnswerID(int answerID) {
        this.answerID = answerID;
    }

    @Override
    public String toString() {
        return "SelectTopAnswerForm{" +
                "userID=" + userID +
                ", answerID=" + answerID +
                '}';
    }
}
