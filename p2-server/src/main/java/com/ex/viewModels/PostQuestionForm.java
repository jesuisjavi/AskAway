package com.ex.viewModels;

import java.util.Arrays;

public class PostQuestionForm {
    private int userID;
    private String title;
    private String details;
    private String[] tags;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "PostQuestionForm{" +
                "userID=" + userID +
                ", title='" + title + '\'' +
                ", details='" + details + '\'' +
                ", tags=" + Arrays.toString(tags) +
                '}';
    }
}
