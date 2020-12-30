package com.ex.persistence;

import com.ex.models.Skill;
import com.ex.models.User;
import com.ex.models.UserSkill;
import com.ex.viewModels.Leader;
import com.ex.viewModels.UserSkillVM;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * A repository for skills. Represents possible interactions for Skills table
 */
@Repository
@Transactional
public class SkillRepo {
    private SessionFactory sessionFactory;
    /**
     * Constructor
     */
    @Autowired
    public SkillRepo(SessionFactory sessionFactory){
        System.out.println("Skill Repo created");
        this.sessionFactory = sessionFactory;
    }

    /**
     * Create or Update a skill
     * @param s the skill to create or update
     */
    @Transactional(isolation = Isolation.SERIALIZABLE,propagation = Propagation.REQUIRES_NEW)
    public void save(Skill s) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(s);
    }

    /**
     * Gets a skill by its ID
     * @param id the id of the skill to get
     * @return the skill
     */
    @Transactional(readOnly = true)
    public Skill getById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return (Skill) session.get(Skill.class, id);
    }

    /**
     * Get a skill by its name (names should be unique)
     * @param name the name of the skill
     * @return the skill
     */
    @Transactional(readOnly = true)
    public Skill getByName(String name){
        Session session = sessionFactory.getCurrentSession();
        Query hql = session.createQuery("From Skill where name = :name");
        hql.setString("name",name);

        Skill skill = null;
        List<Skill> result = hql.list();
        for(Skill s: result){
            skill = s;
        }

        return skill;
    }

    @Transactional(readOnly = true)
    public List<Skill> getByQuestion(int questionID){
        Session session = sessionFactory.getCurrentSession();
        String sql = "select s.skill_id, s.name from skills s inner join question_skills qs on s.skill_id = qs.skill_id where qs.question_id = :questionID";
        SQLQuery query = session.createSQLQuery(sql);
        query.addEntity(Skill.class);
        query.setInteger("questionID",questionID);
        return query.list();
    }

    @Transactional(readOnly = true)
    public List<UserSkillVM> getByUser(int userID){
        Session session = sessionFactory.getCurrentSession();
        String sql = "select s.skill_id, s.\"name\",us.points from skills s inner join user_skills us on s.skill_id = us.skill_id where us.user_id = :userID order by s.\"name\" asc";
        SQLQuery query = session.createSQLQuery(sql);
        query.setInteger("userID",userID);
        List<Object[]> result = query.list();
        List<UserSkillVM> skills = new ArrayList<>();
        if(result.size()>0){
            result.forEach((record)->{
                UserSkillVM s = new UserSkillVM();
                s.setSkillID((int) record[0]);
                s.setName((String) record[1]);
                s.setPoints((int) record[2]);
                skills.add(s);
            });
        }
        System.out.println("Number of skills found: "+skills.size());
        return skills;
    }

    /**
     * Get all skills
     * @return all skills
     */
    @Transactional
    public List<Skill> getAll(){
        Session session = sessionFactory.getCurrentSession();
        Query hql = session.createQuery("From Skill");
        return hql.list();
    }

}
