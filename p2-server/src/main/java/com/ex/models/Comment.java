package com.ex.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;

/**
 * A model representing a comment. A comment is associated with a question and can be further associated with an answer if
 * the comment is on a specific answer
 */
@Entity
@Table(name = "comment")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "commentID")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private int commentID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",referencedColumnName = "user_id")
    @JsonManagedReference
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id",referencedColumnName = "question_id")
    @JsonManagedReference
    private Question question;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "answer_id",referencedColumnName = "answer_id")
    @JsonManagedReference
    private Answer answer;

    @Column(name = "details")
    private String details;

    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentID=" + commentID +
                ", user=" + user +
                ", question=" + question +
                ", answer=" + answer +
                ", details='" + details + '\'' +
                '}';
    }
}
