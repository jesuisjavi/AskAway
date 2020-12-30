package com.ex.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;

/**
 * Represents the join table for users and skills
 * Necessary since extra columns exist in the join table that will need to be user
 */
@Entity
@Table(name = "user_skills")
@AssociationOverrides({
        @AssociationOverride(name = "pk.user",joinColumns = @JoinColumn(name = "user_id")),
        @AssociationOverride(name = "pk.skill",joinColumns = @JoinColumn(name = "skill_id"))
})
public class UserSkill {
    @EmbeddedId
    private UserSkillId pk = new UserSkillId();

    @Column(name = "points")
    private int points;

    @Column(name = "is_favorite")
    private boolean isFavorite;

    @JsonBackReference
    public UserSkillId getPk() {
        return pk;
    }

    public void setPk(UserSkillId pk) {
        this.pk = pk;
    }

    /**
     * Get user (derived by code from mkyong)
     * @return
     */
    @Transient
    public User getUser(){
        return getPk().getUser();
    }

    public void setUser(User user){
        getPk().setUser(user);
    }

    /**
     * Get skill (derived by code from mkyong)
     * @return
     */
    @Transient
    public Skill getSkill(){
        return getPk().getSkill();
    }

    public void setSkill(Skill skill){
        getPk().setSkill(skill);
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public String toString() {
        return "UserSkill{" +
                "pk=" + pk +
                ", user=" + pk.getUser().toString() +
                ", skill=" + pk.getSkill().toString() +
                ", points=" + points +
                ", isFavorite=" + isFavorite +
                '}';
    }
}
