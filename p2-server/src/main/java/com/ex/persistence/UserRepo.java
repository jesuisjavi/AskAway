package com.ex.persistence;

import com.ex.models.Skill;
import com.ex.models.User;
import com.ex.models.UserSkill;
import com.ex.viewModels.Leader;
import org.hibernate.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A repository of users. Represents possible transactions with User table
 */
@Repository
@Transactional
public class UserRepo {
    private SessionFactory sessionFactory;
    /**
     * Constructor
     */
    @Autowired
    public UserRepo(SessionFactory sessionFactory){
        System.out.println("User Repo created");
        this.sessionFactory = sessionFactory;
    }

    /**
     * Saves or Updates the User object
     * @param s the user to create/save
     */
    @Transactional(isolation = Isolation.SERIALIZABLE,propagation = Propagation.REQUIRES_NEW)
    public void save(User s) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(s);
    }

    /**
     * Gets a user by the userID
     * @param id the userID of the desired user
     * @return the User or null if not found
     */
    @Transactional(readOnly = true)
    public User getById(int id) {
        Session session = sessionFactory.getCurrentSession();
        User u = (User) session.get(User.class, id);
        if(u!=null){
            Hibernate.initialize(u.getUserSkills());
        }
        return u;
    }

    /**
     * Returns the leader of a skill
     * @param skillID
     * @return
     */
    @Transactional(readOnly = true)
    public Leader getLeaderBySkill(int skillID){
        Session session = sessionFactory.getCurrentSession();
        String sql = "select u.user_id, u.first_name, u.last_name, us.points, us.skill_id, s.name from \"user\" u inner join user_skills us on u.user_id = us.user_id inner join skills s on us.skill_id = s.skill_id where us.skill_id = :skillID and us.points = (select max (points) from user_skills where skill_id = :skillID)";
        SQLQuery query = session.createSQLQuery(sql);
        query.setInteger("skillID",skillID);
        List<Object[]> result = query.list();
        Leader leader = new Leader();
        if(result.size()>0){
            result.forEach((record)->{
                leader.setUserID((int) record[0]);
                leader.setFirstName((String) record[1]);
                leader.setLastName((String) record[2]);
                leader.setPoints((int) record[3]);
                leader.setSkillID((int) record[4]);
                leader.setSkillName((String) record[5]);
            });
        }
        if(leader.getUserID()<=0){
            return null;
        }
        return leader;
    }

    /**
     * Gets a user by its email field. Also initializes user skills
     * @param email the email of the user
     * @return the User or null if not found
     */
    @Transactional(readOnly = true)
    public User getByEmail(String email){
        Session session = sessionFactory.getCurrentSession();
        Query hql = session.createQuery("From User where email = :email");
        hql.setString("email",email);

        User user = null;
        List<User> result = hql.list();
        for(User u: result){
            user = (User) session.load(User.class,u.getUserID());
        }
        if(user!=null){
            Hibernate.initialize(user.getUserSkills());
        }
        return user;
    }

    /**
     * Get all users in table
     * @return a list of users
     */
    @Transactional
    public List<User> getAll(){
        Session session = sessionFactory.getCurrentSession();
        Query hql = session.createQuery("From User");
        return hql.list();
    }

    /**
     * Delete a user
     * @param userID the id of the user to delete
     */
    @Transactional
    public void delete(int userID){
        Session session = sessionFactory.getCurrentSession();
        User user = (User) session.load(User.class,userID);
        session.delete(user);
    }

    /**
     * Adds a skill to the user
     * @param u the user to add a skill to
     * @param s what skill should be added
     */
    @Transactional
    public boolean addSkillToUser(User u, Skill s){
        Session session = sessionFactory.getCurrentSession();

        User user = (User) session.get(User.class,u.getUserID());
        Skill skill = (Skill) session.get(Skill.class, s.getSkillID());
        if(user!=null && skill!=null){
            //check to see if already exists
            Set<UserSkill> skills = user.getUserSkills();
            for(UserSkill userSkill:skills){
                if(userSkill.getPk().getSkill().equals(skill)){
                    return false;
                }
            }

            UserSkill userSkill = new UserSkill();
            userSkill.setFavorite(false);
            userSkill.setPoints(0);
            userSkill.setUser(user);
            userSkill.setSkill(skill);

            user.getUserSkills().add(userSkill);

            session.saveOrUpdate(user);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean addSkillToUser(User u, Skill s,int points){
        Session session = sessionFactory.getCurrentSession();

        User user = (User) session.get(User.class,u.getUserID());
        Skill skill = (Skill) session.get(Skill.class, s.getSkillID());
        if(user!=null && skill!=null){
            //check to see if already exists
            Set<UserSkill> skills = user.getUserSkills();
            for(UserSkill userSkill:skills){
                if(userSkill.getPk().getSkill().equals(skill)){
                    return false;
                }
            }

            UserSkill userSkill = new UserSkill();
            userSkill.setFavorite(false);
            userSkill.setPoints(points);
            userSkill.setUser(user);
            userSkill.setSkill(skill);

            user.getUserSkills().add(userSkill);

            session.saveOrUpdate(user);
            return true;
        }
        return false;
    }

    /**
     * Adds a skill to the user
     * @param u the user to add a skill to
     * @param s what skill should be added
     */
    @Transactional
    public boolean addSkillToUser(int u, int s){
        Session session = sessionFactory.getCurrentSession();

        User user = (User) session.get(User.class,u);
        Skill skill = (Skill) session.get(Skill.class, s);
        if(user!=null && skill!=null){
            //check to see if already exists
            Set<UserSkill> skills = user.getUserSkills();
            for(UserSkill userSkill:skills){
                if(userSkill.getPk().getSkill().equals(skill)){
                    return false;
                }
            }

            UserSkill userSkill = new UserSkill();
            userSkill.setFavorite(false);
            userSkill.setPoints(0);
            userSkill.setUser(user);
            userSkill.setSkill(skill);

            user.getUserSkills().add(userSkill);

            session.saveOrUpdate(user);
            return true;
        }
        return false;
    }

    /**
     * Removes a skill from a user
     * @param u the user to remove a skill from
     * @param s the skill to remove
     */
    @Transactional
    public void removeSkillFromUser(User u, Skill s){
        Session session = sessionFactory.getCurrentSession();
        User user = (User) session.get(User.class,u.getUserID());
        Skill skill = (Skill) session.get(Skill.class, s.getSkillID());

        Set<UserSkill> userSkills = user.getUserSkills();
        Iterator<UserSkill> i = userSkills.iterator();
        while (i.hasNext()){
            UserSkill us = i.next();
            System.out.println(us.getPk().getUser().equals(user));
            System.out.println(us.getPk().getSkill().equals(skill));
            if(us.getPk().getUser().equals(user) && us.getPk().getSkill().equals(skill)){
                userSkills.remove(us);
            }
        }
        session.save(user);

    }

}
