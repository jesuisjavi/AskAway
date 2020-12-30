package com.ex.viewModels;

public class PostCommentForm {
    private int userID;
    private int questionID;
    private int answerID;
    private String details;

    public PostCommentForm() {
    }

    public PostCommentForm(int userID, int questionID, int answerID, String details) {
        this.userID = userID;
        this.questionID = questionID;
        this.answerID = answerID;
        this.details = details;
    }

    public PostCommentForm(int userID, int questionID, String details) {
        this.userID = userID;
        this.questionID = questionID;
        this.details = details;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public int getAnswerID() {
        return answerID;
    }

    public void setAnswerID(int answerID) {
        this.answerID = answerID;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "PostCommentForm{" +
                "userID=" + userID +
                ", questionID=" + questionID +
                ", answerID=" + answerID +
                ", details='" + details + '\'' +
                '}';
    }
}
