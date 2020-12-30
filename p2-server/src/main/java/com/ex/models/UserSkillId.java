package com.ex.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Represents the composite key of a User skill
 * Necessary because our join table has extra columns related to both keys
 */
@Embeddable
public class UserSkillId implements Serializable {
    @ManyToOne
    @JsonManagedReference
    private User user;

    @ManyToOne
    @JsonManagedReference
    private Skill skill;

    public UserSkillId(){}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }
}
