package com.ex.persistence;

import com.ex.models.Comment;
import com.ex.models.Question;
import com.ex.models.User;
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
 * A repository of comments. Represents the possible interactions that can be performed with the comment's table
 */
@Repository
public class CommentRepo {
    private SessionFactory sessionFactory;
    /**
     * Constructor
     * @param sessionFactory
     */
    @Autowired
    public CommentRepo(SessionFactory sessionFactory){
        System.out.println("Comment Repo created");
        this.sessionFactory = sessionFactory;
    }

    /**
     * Saves or Updates the Comment object
     * @param c the comment to create/save
     */
    @Transactional(isolation = Isolation.SERIALIZABLE,propagation = Propagation.REQUIRES_NEW)
    public void save(Comment c) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(c);
    }

    /**
     * Gets a comment by id
     * @param id the id of the comment
     * @return the comment or null
     */
    @Transactional
    public Comment getById(int id){
        Session session = sessionFactory.getCurrentSession();
        Comment c = (Comment) session.get(Comment.class,id);
        return c;
    }

    /**
     * Get all comments on a question
     * @param id the id of the question
     * @return a list of comments
     */
    @Transactional
    public List<Comment> getByQuestionID(int id){
        Session session = sessionFactory.getCurrentSession();
        Query hql = session.createQuery("From Comment where question_id = :id and answer_id = null");
        hql.setInteger("id",id);
        return hql.list();
    }

    /**
     * Get all comments on an answer
     * @param id the id of the answer
     * @return a list of comments
     */
    @Transactional
    public List<Comment> getByAnswerID(int id){
        Session session = sessionFactory.getCurrentSession();
        Query hql = session.createQuery("From Comment where answer_id = :id");
        hql.setInteger("id",id);
        return hql.list();
    }


    /**
     * Deletes a comment by comment id
     * @param id the id of the comment to delete
     */
    @Transactional
    public void delete(int id){
        Session session = sessionFactory.getCurrentSession();
        Comment comment = (Comment) session.load(Comment.class,id);
        session.delete(comment);
    }
}
