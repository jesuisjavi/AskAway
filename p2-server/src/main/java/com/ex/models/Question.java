package com.ex.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

/**
 * A question posted by a user. Questions can be tagged with skills. Only one primary skill should exist at a time.
 */
@Entity
@Table(name = "question")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "questionID")
public class Question{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="question_id")
    private int questionID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",referencedColumnName = "user_id")
    private User user;

    @Column(name = "title")
    private String title;

    @Column(name = "details")
    private String details;

    @Column(name = "date_posted")
    @CreationTimestamp
    private Date datePosted;

    @Column(name = "date_resolved")
    private Date dateResolved;

    @ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinTable(name="question_skills",
            joinColumns = @JoinColumn(name = "question_id", referencedColumnName = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id",referencedColumnName = "skill_id")
    )
    @JsonManagedReference
    private Set<Skill> skills;

    @OneToMany(mappedBy = "question",cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<Comment> comments;

    @OneToMany(mappedBy = "question",cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<Answer> answers;

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Date getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

    public Date getDateResolved() {
        return dateResolved;
    }

    public void setDateResolved(Date dateResolved) {
        this.dateResolved = dateResolved;
    }

    @Override
    public String toString() {
        return "Question{" +
                "questionID=" + questionID +
                ", user=" + user +
                ", details='" + details + '\'' +
                ", datePosted=" + datePosted +
                ", dateResolved=" + dateResolved +
                '}';
    }
}
