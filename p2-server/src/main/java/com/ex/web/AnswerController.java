package com.ex.web;

import com.ex.models.*;
import com.ex.persistence.*;
import com.ex.utilities.CheckAuthUtil;
import com.ex.utilities.PointsUtil;
import com.ex.viewModels.AnswerQuestionForm;
import com.ex.viewModels.SelectTopAnswerForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.sql.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Controller that mainly handles requests related to sumbitting answers on questions
 */
@Controller
@RequestMapping(
        path={"/answers"}
)
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AnswerController extends WebController{
    private AnswerRepo answerRepo;
    private UserRepo userRepo;
    private SkillRepo skillRepo;
    private QuestionRepo questionRepo;

    @Autowired
    public AnswerController(AnswerRepo answerRepo, CheckAuthUtil checkAuthUtil,UserRepo userRepo,QuestionRepo questionRepo,SkillRepo skillRepo){
        super(checkAuthUtil);
        this.answerRepo = answerRepo;
        this.userRepo = userRepo;
        this.questionRepo = questionRepo;
        this.skillRepo = skillRepo;
    }

    /**
     * Gets answers by question id
     * @param id the id of the question
     * @return a list of answers
     * @throws IOException
     */
    @GetMapping(path="/{questionID}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getAnswersByQuestionID(@PathVariable(name="questionID", required = true) Integer id) throws IOException {
        List<Answer> answers = answerRepo.getByQuestionId(id);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, answers);
        final byte[] data = out.toByteArray();
        return new ResponseEntity(data,HttpStatus.OK);
    }

    /**
     * Adds an answer to a question
     * @param form the submitted form
     * @return a response
     */
    @PostMapping(value = "/addAnswer", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> addAnswer(@RequestBody AnswerQuestionForm form) {
        System.out.println(form.toString());
        User user = this.userRepo.getById(form.getUserID());
        Question question = this.questionRepo.getById(form.getQuestionID());
        if(user!=null && question!=null){
            Answer a = new Answer();
            a.setTopAnswer(false);
            a.setUser(user);
            a.setQuestion(question);
            a.setDetails(form.getDetails());
            this.answerRepo.save(a);

            for(Skill s: question.getSkills()){
                if(user.getUserSkills().size()>0){
                    for(UserSkill u:user.getUserSkills()){
                        boolean notFound = true;
                        if(u.getSkill().getName().equals(s.getName())){
                            u.setPoints(u.getPoints()+ PointsUtil.AwardPoints(AwardableActions.ANSWERED));
                            this.userRepo.save(user);
                            notFound = false;
                        }

                        if(notFound){
                            this.userRepo.addSkillToUser(user,s,PointsUtil.AwardPoints(AwardableActions.ANSWERED));
                        }
                    }
                }
                else{
                    System.out.println("Adding user skill");
                    this.userRepo.addSkillToUser(user,s,PointsUtil.AwardPoints(AwardableActions.ANSWERED));
                }
            }

            return new ResponseEntity("Answer Added",HttpStatus.OK);
        }
        else{
            return new ResponseEntity("Invalid Question or User", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/selectTop", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> selectTopAnswer(@RequestBody SelectTopAnswerForm form){
        //check for a top answer
        Answer answer = this.answerRepo.getById(form.getAnswerID());
        User user = this.userRepo.getById(form.getUserID());
        Question question = this.questionRepo.getById(answer.getQuestion().getQuestionID());
        if(answer!=null && user!=null && question!=null){
            if(user.equals(question.getUser())){
                System.out.println("User same as poster");
                boolean hasTop = false;
                for(Answer a:question.getAnswers()){
                    if(a.isTopAnswer()){
                        System.out.println("Top answer found");
                        hasTop = true;
                    }
                }

                if(!hasTop){
                    answer.setTopAnswer(true);
                    this.answerRepo.save(answer);
                    //Award points to answerer
                    for(Skill s: this.skillRepo.getByQuestion(question.getQuestionID())){
                        if(user.getUserSkills().size()>0){
                            for(UserSkill u:user.getUserSkills()){
                                boolean notFound = true;
                                if(u.getSkill().getName().equals(s.getName())){
                                    u.setPoints(u.getPoints()+ PointsUtil.AwardPoints(AwardableActions.TOP_ANSWER));
                                    this.userRepo.save(user);
                                    notFound = false;
                                }

                                if(notFound){
                                    this.userRepo.addSkillToUser(user,s,PointsUtil.AwardPoints(AwardableActions.TOP_ANSWER));
                                }
                            }
                        }
                        else{
                            System.out.println("Adding user skill");
                            this.userRepo.addSkillToUser(user,s,PointsUtil.AwardPoints(AwardableActions.TOP_ANSWER));
                        }
                    }
                    return new ResponseEntity("Answer Selected as Top",HttpStatus.OK);
                }else{
                    return new ResponseEntity("Answer Already has top",HttpStatus.OK);
                }
            }
            return new ResponseEntity("Note Original Poster",HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity("Invalid User or Answer",HttpStatus.FORBIDDEN);
    }
}
