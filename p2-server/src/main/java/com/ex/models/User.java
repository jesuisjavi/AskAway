package com.ex.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.*;

/**
 * A model representing a user of the system
 */
@Entity
@Table(name = "\"user\"")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userID")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userID;

    @Column(name = "email",unique = true)
    private String email; //Verify against email

    @Column(name = "password") //remove use 3rd party only
    private String password; //we should probably implement some sort of hashing here

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "github_email")
    private String gitHubAccount;

    //orphanRemoval true allows any skills to be deleted when the user object is saved.
    @OneToMany(mappedBy = "pk.user",fetch = FetchType.EAGER,cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonBackReference
    private Set<UserSkill> skills = new HashSet<UserSkill>();

    public Set<UserSkill> getUserSkills() {
        return skills;
    }

    public void setUserSkills(Set<UserSkill> skills) {
        this.skills = skills;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getGitHubAccount() {
        return gitHubAccount;
    }

    public void setGitHubAccount(String gitHubAccount) {
        this.gitHubAccount = gitHubAccount;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gitHubAccount='" + gitHubAccount + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(!(obj instanceof User))
            return false;

        User u = (User) obj;
        if(this.getUserID() != u.getUserID())
            return false;
        if(!this.getEmail().equals(u.getEmail()))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userID, email);
    }
}
