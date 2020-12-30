package com.ex.web;

import com.ex.models.EmptyJsonResponse;
import com.ex.models.Skill;
import com.ex.models.User;
import com.ex.models.UserSkill;
import com.ex.persistence.SkillRepo;
import com.ex.persistence.UserRepo;
import com.ex.viewModels.Leader;
import com.ex.viewModels.UpdateProfileForm;
import com.ex.viewModels.UserSkillVM;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Controller to handle requests pertaining to users
 */
@Controller
@RequestMapping(
        path={"/users"}
)
@CrossOrigin(origins = "*", allowedHeaders = "*", allowCredentials = "false")
public class UserController {
    private UserRepo userRepo;
    private SkillRepo skillRepo;

    @Autowired
    public UserRepo setUserRepo(UserRepo userRepo){
        return this.userRepo = userRepo;
    }

    @Autowired
    public SkillRepo setSkillRepo(SkillRepo skillRepo){
        return this.skillRepo = skillRepo;
    }

    /**
     * Method to get all users in system. Returns an Array of Json Objects representing the users
     * URL: YOUR_WEBAPPSFOLDER/api/users
     * @return
     * @throws IOException
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<String>> getUsers() throws IOException {
        List<User> users = this.userRepo.getAll();

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(out, users);

        final byte[] data = out.toByteArray();
        return new ResponseEntity(data, HttpStatus.OK);
    }

    /**
     * Gets a list of leaders for each of the skills in the system
     * @return
     * @throws IOException
     */
    @GetMapping(path = "/leaders",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<String>> getLeadersBySkill() throws IOException {
        List<Skill> skills = this.skillRepo.getAll();
        List<Leader> leaders = new ArrayList<Leader>();
        for(Skill s:skills){
            Leader leader = this.userRepo.getLeaderBySkill(s.getSkillID());
            if(leader!=null){
                leaders.add(leader);
            }
        }

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(out, leaders);

        final byte[] data = out.toByteArray();
        return new ResponseEntity(data, HttpStatus.OK);
    }

    /**
     *Return an individual user by the user id
     * URL: YOUR_WEBAPPSFOLDER/api/users/x
     * @param id the id of the user
     * @return
     */
    @GetMapping(path="/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getUserByID(@PathVariable(name="userId", required = true) Integer id) throws IOException {
        //Check to see if user exists
        User user = userRepo.getById(id);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectMapper mapper = new ObjectMapper();
        if(user==null){
            //If user doesn't exists return bad request
            return new ResponseEntity("User not found", HttpStatus.BAD_REQUEST);
        }
        else{
            mapper.writeValue(out, user);
            final byte[] data = out.toByteArray();
            //If user does exist, return user and user skills
            return new ResponseEntity(data,HttpStatus.OK);
        }
    }

    @PostMapping(path="/loadUser", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity getByEmail(@RequestBody String email) throws IOException {
        System.out.println(email);
        try{
            User user = userRepo.getByEmail(email);
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final ObjectMapper mapper = new ObjectMapper();

            mapper.writeValue(out, user);
            final byte[] data = out.toByteArray();
            //If user does exist, return user and user skills
            return new ResponseEntity(data,HttpStatus.OK);
        } catch(NullPointerException e){
            return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK);
        }
    }
    @GetMapping(path = "/userskills/{userId}",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<String>> getSkillsByUser(@PathVariable(name="userId", required = true) Integer id) throws IOException {
        List<UserSkillVM> skills = this.skillRepo.getByUser(id);

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(out, skills);

        final byte[] data = out.toByteArray();
        return new ResponseEntity(data, HttpStatus.OK);
    }



    /**
     * Adds a user to the system or updates it
     * @param form
     * @return
     */
    @PostMapping(value = "/updateUser", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> addUser(@RequestBody UpdateProfileForm form) {
        User u = this.userRepo.getByEmail(form.getEmail());
        if(u!=null){
            u.setGitHubAccount(form.getGitHub());
            u.setFirstName(form.getFirstName());
            u.setLastName(form.getLastName());
            this.userRepo.save(u);
        }
        else{
            User newUser = new User();
            newUser.setGitHubAccount(form.getGitHub());
            newUser.setFirstName(form.getFirstName());
            newUser.setLastName(form.getLastName());
            newUser.setEmail(form.getEmail());
            newUser.setPassword("password");
            this.userRepo.save(newUser);
        }
        return new ResponseEntity("User added",HttpStatus.OK);
    }

    /**
     * Add a skill to the user
     * @param userID the id of the user
     * @param skillID the id of the skill
     * @return
     */
    @PostMapping(value = "/addSkillToUser",consumes=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> addSkillToUser(@RequestParam int userID,@RequestParam int skillID){
        if(this.userRepo.addSkillToUser(userID,skillID)){
            return new ResponseEntity("Test",HttpStatus.OK);
        }
        else{
            return new ResponseEntity("Unknown user or skill",HttpStatus.BAD_REQUEST);
        }
    }
}
