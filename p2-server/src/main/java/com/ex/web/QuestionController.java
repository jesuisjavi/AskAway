package com.ex.web;

import com.ex.models.*;
import com.ex.persistence.AnswerRepo;
import com.ex.persistence.QuestionRepo;
import com.ex.persistence.SkillRepo;
import com.ex.persistence.UserRepo;
import com.ex.searchengine.SearchEngine;
import com.ex.utilities.CheckAuthUtil;
import com.ex.utilities.PointsUtil;
import com.ex.viewModels.PostQuestionForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * Controller to handle requests for questions
 * - Will retutn any question with its comments, answers, and comments on answers
 * - Will return a list of all questions (JUST THE QUESTIONS)
 *
 */
@Controller
@RequestMapping(
        path={"/questions"}
)
@CrossOrigin(origins = "*", allowedHeaders = "*", allowCredentials = "false")
public class QuestionController extends WebController{
    private QuestionRepo questionRepo;
    private UserRepo userRepo;
    private AnswerRepo answerRepo;
    private SkillRepo skillRepo;
    private SearchEngine searchEngine;

    @Autowired
    public QuestionController(QuestionRepo questionRepo, UserRepo userRepo, AnswerRepo answerRepo, SkillRepo skillRepo, CheckAuthUtil checkAuthUtil){
        super(checkAuthUtil);
        this.userRepo = userRepo;
        this.questionRepo = questionRepo;
        this.answerRepo = answerRepo;
        this.skillRepo = skillRepo;
        try {
            searchEngine = SearchEngine.getInstance();
            PopulateSEMaps();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void PopulateSEMaps(){
        List<Question> questions = questionRepo.getAll();
        for (Question question : questions){
            searchEngine.AddDocument(question.getQuestionID(), question.getDetails());
        }
    }

    /**
     * Method to get all questions in system
     * URL: YOUR_WEBAPPSFOLDER/api/questions
     * @return
     * @throws IOException
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<String>> getQuestions() throws IOException {
        List<Question> questions = this.questionRepo.getAll();

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(out, questions);

        final byte[] data = out.toByteArray();
        return new ResponseEntity(data, HttpStatus.OK);
    }

    @GetMapping(path="/recent",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<String>> getRecentQuestions() throws IOException {
        List<Question> questions = this.questionRepo.getRecent();

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(out, questions);

        final byte[] data = out.toByteArray();
        return new ResponseEntity(data, HttpStatus.OK);
    }


    /**
     * Method to get all questions in system
     * URL: YOUR_WEBAPPSFOLDER/api/query
     * @return
     * @throws IOException
     */
    @GetMapping(path="/query", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<String>> query(@RequestParam String query) throws IOException, URISyntaxException {
        /*
        List<Question> result = this.questionRepo.getAll();
        Iterator<Question> i = result.iterator();
        while(i.hasNext()){
            Question q = i.next();
            if(!q.getTitle().toUpperCase().contains(query.toUpperCase())){
                System.out.println("removing question titled "+q.getTitle());
                i.remove();
            }
        }*/

        List<Integer> docs = searchEngine.Query(query);
        List<Question> questions = this.questionRepo.getAll();

        List<Question> result = questions.stream().filter(question -> docs.contains(question.getQuestionID())).collect(Collectors.toList());
        System.out.println(result);

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(out, result);

        final byte[] data = out.toByteArray();
        return new ResponseEntity(data, HttpStatus.OK);
    }

    /**
     *Return an question with all of its details
     * URL: YOUR_WEBAPPSFOLDER/api/questions/x
     * @param id the id of the question
     * @return
     */
    @GetMapping(path="/{questionID}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getQuestionByID(@PathVariable(name="questionID", required = true) Integer id) throws IOException {
        //Check to see if question exists
        Question question = questionRepo.getById(id);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectMapper mapper = new ObjectMapper();
        if(question==null){
            return new ResponseEntity("Question not found", HttpStatus.BAD_REQUEST);
        }
        else{
            mapper.writeValue(out, question);
            final byte[] data = out.toByteArray();
            return new ResponseEntity(data,HttpStatus.OK);
        }
    }

    @PostMapping(value = "/addQuestion", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> addQuestion(@RequestBody PostQuestionForm postQuestionForm) {
        System.out.println(postQuestionForm);
        User user = this.userRepo.getById(postQuestionForm.getUserID());
        if(user!=null){
            //Save Question
            Question question = new Question();
            question.setUser(user);
            question.setTitle(postQuestionForm.getTitle());
            question.setDetails(postQuestionForm.getDetails());
            Set<Skill> skills = new HashSet<Skill>();
            for(String s:postQuestionForm.getTags()){
                skills.add(this.skillRepo.getByName(s));
            }
            question.setSkills(skills);
            this.questionRepo.save(question);
            //searchEngine.AddDocument(question.getQuestionID(), question.getDetails());

            //Add points to the question skills
            for(String s: postQuestionForm.getTags()){
                System.out.println("Tag on form: "+s);
            }
            for(String s: postQuestionForm.getTags()){
                if(user.getUserSkills().size()>0){
                    for(UserSkill u:user.getUserSkills()){
                        boolean notFound = true;
                        if(u.getSkill().getName().equals(s)){
                            u.setPoints(u.getPoints()+ PointsUtil.AwardPoints(AwardableActions.ADDED));
                            this.userRepo.save(user);
                            notFound = false;
                        }

                        if(notFound){
                            System.out.println(s);
                            Skill skill = this.skillRepo.getByName(s);
                            System.out.println("Adding user skill");
                            this.userRepo.addSkillToUser(user,skill,PointsUtil.AwardPoints(AwardableActions.ADDED));
                        }
                    }
                }
                else{
                    System.out.println(s);
                    Skill skill = this.skillRepo.getByName(s);
                    System.out.println("Adding user skill");
                    this.userRepo.addSkillToUser(user,skill,PointsUtil.AwardPoints(AwardableActions.ADDED));
                }
            }
            return new ResponseEntity("User added",HttpStatus.OK);
        }
        else{
            return new ResponseEntity("Invalid Question", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/selectTop")
    @ResponseBody
    public ResponseEntity<String> selectTopAnswer(@RequestParam String email,@RequestParam int questionID,@RequestParam int answerID){
        if(this.checkAuthUtil.checkEmail(email)){
            User user = this.userRepo.getByEmail(email);
            //get the response and answer objects
            Question question = this.questionRepo.getById(questionID);
            Answer answer = this.answerRepo.getById(answerID);
            //If user logged in is the question poster
            if(question.getUser().equals(user)){
                boolean hasTop = false;
                boolean isAnswer = false;
                for(Answer a:question.getAnswers()){
                    //if an answer has already been selected as top
                    if(a.isTopAnswer()){
                        hasTop = true;
                    }
                    if(a.equals(answer)){
                        isAnswer = true;
                    }
                }
                //Check to see if top answer has already been selected, if not...
                if(!hasTop && isAnswer){
                    //... mark this answer as top
                    answer.setTopAnswer(true);
                    question.setDateResolved(new java.sql.Date(System.currentTimeMillis()));
                    this.questionRepo.save(question);
                    this.answerRepo.save(answer);
                    return new ResponseEntity<>("Top answer selected",HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>("Unable to select top answer",HttpStatus.FORBIDDEN);
    }
}
