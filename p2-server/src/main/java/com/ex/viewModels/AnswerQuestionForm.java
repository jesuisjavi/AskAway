package com.ex.viewModels;

public class AnswerQuestionForm {
    private int questionID;
    private int userID;
    private String details;

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "AnswerQuestionForm{" +
                "questionID=" + questionID +
                ", userID=" + userID +
                ", details='" + details + '\'' +
                '}';
    }
}
