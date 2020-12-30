package com.ex.persistence;

import com.ex.models.Question;
import com.ex.models.Skill;
import com.ex.models.User;
import org.hibernate.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * A Repository of Questions. Represents possible interactions with Questions table
 */
@Repository
@Transactional
public class QuestionRepo {
    private SessionFactory sessionFactory;
    /**
     * Constructor
     */
    @Autowired
    public QuestionRepo(SessionFactory sessionFactory){
        System.out.println("Question Repo created");
        this.sessionFactory = sessionFactory;
    }

    /**
     * Saves or Updates the Question object
     * @param q the user to create/save
     */
    @Transactional(isolation = Isolation.SERIALIZABLE,propagation = Propagation.REQUIRES_NEW)
    public void save(Question q) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(q);
    }

    /**
     * Get a question by its id
     * @param id the id of the question
     * @return the question or null if not found
     */
    @Transactional
    public Question getById(int id){
        Session session = sessionFactory.getCurrentSession();
        Question q = (Question) session.get(Question.class, id);
        if(q!=null){
            Hibernate.initialize(q.getSkills());
            Hibernate.initialize(q.getComments());
            Hibernate.initialize(q.getAnswers());
            System.out.println(q.toString());
            System.out.println(q.getSkills());
            System.out.println(q.getComments());
            System.out.println(q.getAnswers());
        }
        return q;
    }

    /**
     * Get all questions by user id
     * @param userID the id of the user
     * @return questions for this user or null if none exist
     */
    @Transactional
    public List<Question> getAllByUserId(int userID){
        Session session = sessionFactory.getCurrentSession();
        Query hql = session.createQuery("From Question where user_id = :userID");
        hql.setInteger("userID",userID);
        return hql.list();
    }

    /**
     * Get all questions
     * @return all questions
     */
    @Transactional
    public List<Question> getAll(){
        Session session = sessionFactory.getCurrentSession();
        Query hql = session.createQuery("From Question");
        return hql.list();
    }

    @Transactional
    public List<Question> getRecent(){
        Session session = sessionFactory.getCurrentSession();
        String sql = "select * from question q order by q.date_posted desc limit 20";
        SQLQuery query = session.createSQLQuery(sql);
        query.addEntity(Question.class);
        List<Question> questions = query.list();
        return questions;
    }

    /**
     * Get all questions that have a skill. Primary or otherwise
     * @param skill the skill to search for
     */
    @Transactional
    public List<Question> getAllBySkill(Skill skill){
        Session session = sessionFactory.getCurrentSession();
        String hql = "From Question q" +
                " JOIN q.skills s " +
                " WHERE s.name = :name ";
        Query query = session.createQuery(hql);
        query.setString("name",skill.getName());
        return query.list();
    }

    /**
     * Get all questions that have a skill. Primary or otherwise
     * @param name the name of the skill
     */
    @Transactional
    public List<Question> getAllBySkill(String name){
        Session session = sessionFactory.getCurrentSession();
        List<Question> result = new ArrayList<>();
        String hql = "Select q from Question q" +
                " left join q.skills s " +
                " where s.name = :name ";
        Query query = session.createQuery(hql);
        query.setString("name",name);
        result = query.list();
        return result;
    }
}
