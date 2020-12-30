package com.ex.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.*;

/**
 * A model representing an individual skill. These skills are used for tags on questions.
 */
@Entity
@Table(name = "skills")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "skillID")
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_id")
    private int skillID;

    @Column(name = "name")
    private String name;

    /**
    @OneToMany(mappedBy = "pk.skill", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<UserSkill> users = new HashSet<>();

    public Set<UserSkill> getUserSkills() {
        return users;
    }

    public void setUserSkills(Set<UserSkill> users) {
        this.users = users;
    }**/

    public int getSkillID() {
        return skillID;
    }

    public void setSkillID(int skillID) {
        this.skillID = skillID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Skill{" +
                "skillID=" + skillID +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(!(obj instanceof Skill))
            return false;

        Skill s = (Skill)obj;
        if(this.getSkillID()!=s.getSkillID())
            return false;
        if(this.getName()!=s.getName())
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(skillID,name);
    }
}
