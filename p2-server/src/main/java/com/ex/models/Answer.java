package com.ex.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;
import java.util.Set;

/**
 * A model representing an answer to a question.
 */

@Entity
@Table(name="answer")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "answerID")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id",updatable = false)
    private int answerID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="question_id", referencedColumnName = "question_id")
    private Question question;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(name = "details")
    private String details;

    @Column(name = "top_answer")
    private boolean isTopAnswer;

    @Column(name = "date_posted")
    @CreationTimestamp
    private Date datePosted;

    @OneToMany(mappedBy = "answer",cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<Comment> comments;

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public int getAnswerID() {
        return answerID;
    }

    public void setAnswerID(int answerID) {
        this.answerID = answerID;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isTopAnswer() {
        return isTopAnswer;
    }

    public void setTopAnswer(boolean topAnswer) {
        isTopAnswer = topAnswer;
    }

    public Date getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "answerID=" + answerID +
                ", question=" + question +
                ", user=" + user +
                ", details='" + details + '\'' +
                ", isTopAnswer=" + isTopAnswer +
                ", datePosted=" + datePosted +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(!(obj instanceof Answer))
            return false;

        Answer u = (Answer) obj;
        if(this.getAnswerID() != u.getAnswerID())
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(answerID);
    }
}
