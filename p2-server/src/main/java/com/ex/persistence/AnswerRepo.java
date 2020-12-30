package com.ex.persistence;

import com.ex.models.Answer;
import com.ex.models.Comment;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * A repository of Answers. Represents possible interactions that can be performed with the Answer's table
 */

@Repository
@Transactional
public class AnswerRepo {
    private SessionFactory sessionFactory;
    /**
     * Constructor
     * @param sessionFactory
     */
    @Autowired
    public AnswerRepo(SessionFactory sessionFactory){
        System.out.println("Answer Repo created");
        this.sessionFactory = sessionFactory;
    }

    /**
     * Saves or Updates the Comment object
     * @param a the comment to create/save
     */
    @Transactional(isolation = Isolation.SERIALIZABLE,propagation = Propagation.REQUIRES_NEW)
    public void save(Answer a) {
        System.out.println(a.toString());
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(a);
    }

    /**
     * Get an answer by its id
     * @param id the id of the answer
     * @return the answer or null
     */
    @Transactional
    public Answer getById(int id){
        Session session = sessionFactory.getCurrentSession();
        Answer a = (Answer) session.get(Answer.class,id);
        if(a!=null){
            Hibernate.initialize(a.getComments());
        }
        return a;
    }

    @Transactional
    public List<Answer> getByQuestionId(int id){
        Session session = sessionFactory.getCurrentSession();
        Query hql = session.createQuery("From Answer where question_id = :id");
        hql.setInteger("id",id);
        return hql.list();
    }
}
