package com.ex.web;

import com.ex.models.*;
import com.ex.persistence.*;
import com.ex.utilities.CheckAuthUtil;
import com.ex.viewModels.PostCommentForm;
import com.ex.viewModels.PostQuestionForm;
import com.ex.viewModels.QuestionComment;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Controller that mainly handles requests related to adding comments on both questions and answers
 */
@Controller
@RequestMapping(
        path={"/comments"}
)
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CommentController extends WebController{
    private QuestionRepo questionRepo;
    private UserRepo userRepo;
    private AnswerRepo answerRepo;
    private SkillRepo skillRepo;
    private CommentRepo commentRepo;

    @Autowired
    public CommentController(QuestionRepo questionRepo, UserRepo userRepo, AnswerRepo answerRepo, SkillRepo skillRepo, CheckAuthUtil checkAuthUtil, CommentRepo commentRepo){
        super(checkAuthUtil);
        this.userRepo = userRepo;
        this.questionRepo = questionRepo;
        this.answerRepo = answerRepo;
        this.skillRepo = skillRepo;
        this.commentRepo = commentRepo;
    }

    /**
     * Gets all comments on a particular question
     * @param id the id of the question
     * @return a response
     * @throws IOException
     */
    @GetMapping(path="/questionComments/{questionID}",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<String>> getCommentsOnQuestion(@PathVariable(name="questionID", required = true) Integer id) throws IOException {
        System.out.println(id);
        List<Comment> comments = this.commentRepo.getByQuestionID(id);
        List<QuestionComment> result = new ArrayList<>();
        for(Comment c: comments){
            QuestionComment qc = new QuestionComment();
            qc.setDetails(c.getDetails());
            qc.setFirstName(c.getUser().getFirstName());
            qc.setLastName(c.getUser().getLastName());
            result.add(qc);
        }


        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(out, result);

        final byte[] data = out.toByteArray();
        return new ResponseEntity(data, HttpStatus.OK);
    }

    /**
     * Gets all comments on a particular answer
     * @param id the id of the answer
     * @return response
     * @throws IOException
     */
    @GetMapping(path="/answerComments/{answerID}",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<String>> getCommentsOnAnswer(@PathVariable(name="answerID", required = true) Integer id) throws IOException {
        List<Comment> comments = this.commentRepo.getByAnswerID(id);

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(out, comments);

        final byte[] data = out.toByteArray();
        return new ResponseEntity(data, HttpStatus.OK);
    }

    /**
     * Adds a comment
     * @param postCommentForm the form
     * @return a response
     */
    @PostMapping(value = "/addComment", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> addComment(@RequestBody PostCommentForm postCommentForm) {
        System.out.println(postCommentForm.toString());
        User user = this.userRepo.getById(postCommentForm.getUserID());
        Question question = this.questionRepo.getById(postCommentForm.getQuestionID());
        Answer answer = this.answerRepo.getById(postCommentForm.getAnswerID());
        if(user!=null && question!=null){
            Comment comment = new Comment();
            comment.setUser(user);
            comment.setQuestion(question);
            comment.setAnswer(answer);
            comment.setDetails(postCommentForm.getDetails());
            this.commentRepo.save(comment);
            return new ResponseEntity("Comment Added",HttpStatus.OK);
        }
        else{
            return new ResponseEntity("Invalid Question or User", HttpStatus.BAD_REQUEST);
        }
    }
}
