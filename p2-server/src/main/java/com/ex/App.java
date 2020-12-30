package com.ex;

import com.ex.models.*;
import com.ex.persistence.*;
import javassist.ClassPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Component
@EnableWebMvc
public class App {
    private UserRepo userRepo;
    private SkillRepo skillRepo;
    private QuestionRepo questionRepo;
    private CommentRepo commentRepo;
    private AnswerRepo answerRepo;

    public App(){
        System.out.println("Creating app!");
    }

    @Autowired
    public void setUserRepo(UserRepo userRepo){
        this.userRepo = userRepo;
    }

    @Autowired
    public void setSkillRepo(SkillRepo skillRepo){
        this.skillRepo = skillRepo;
    }

    @Autowired
    public void setQuestionRepo(QuestionRepo questionRepo){
        this.questionRepo = questionRepo;
    }

    @Autowired
    public void setCommentRepo(CommentRepo commentRepo){
        this.commentRepo = commentRepo;
    }

    @Autowired
    public void setAnswerRepo(AnswerRepo answerRepo){
        this.answerRepo = answerRepo;
    }

    public static void main(String[] args){
        //Having trouble getting the below code to point into the WEB-INF file. Can anyone figure this out?
        ApplicationContext ac = new ClassPathXmlApplicationContext("WEB-INF/application-context.xml");
        //ApplicationContext ac = new ClassPathXmlApplicationContext("application-context.xml");
        App app = ac.getBean(App.class);

        System.out.println();


        /**
        User u = app.userRepo.getByEmail("testuser@gmail.com");
        Skill s = app.skillRepo.getByName("Java");
        System.out.println(s.toString());
        System.out.println(u.toString());
        Question q = new Question();
        q.setTitle("Another Question");
        q.setDetails("Details on this question! Woohooo!");
        q.setUser(u);
        Set<Skill> skills = q.getSkills();
        System.out.println(skills);
        if(skills == null){
            Set<Skill> newSkills = new HashSet<>();
            newSkills.add(s);
            q.setSkills(newSkills);
        }
        else{
            skills.add(s);
        }
        app.questionRepo.save(q);
         **/

        /**
        Skill s = app.skillRepo.getByName("Java");
        List<Question> questions = app.questionRepo.getAllBySkill(s);
        for(Question q:questions){
            System.out.println(q.toString());
        }**/

        ((AbstractApplicationContext)ac).close();
    }
}
