package com.ex.web;

import com.ex.models.Skill;
import com.ex.models.User;
import com.ex.models.UserSkill;
import com.ex.persistence.SkillRepo;
import com.ex.persistence.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Controller to handle requests pertaining to leaderboards
 */
@Controller
@RequestMapping(
        path={"/leaderboards"}
)
@CrossOrigin(origins = "*", allowedHeaders = "*", allowCredentials = "false")
public class LeaderboardsController {
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
     * Method to get the leaderboard. Returns a Map where the key is the skill and the value are the top5 users with that skill
     * URL: YOUR_WEBAPPSFOLDER/api/users
     * @return
     * @throws IOException
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String, List<User>>> getLeaderboard() throws IOException {
        List<User> users = this.userRepo.getAll();
        List<Skill> skills = skillRepo.getAll();
        Map<String, Map<User, Integer>> board = new HashMap<>();

        Map<User, Integer> userSkills;
        List<User> skillUser;

        for (Skill skill : skills){
            userSkills = new HashMap<>();

            for (User user : users) {
                for (UserSkill userSkill : user.getUserSkills()) {
                    if (userSkill.getSkill().equals(skill)) {
                        userSkills.put(user, userSkill.getPoints());
                        break;
                    }
                }
            }
            skillUser = new ArrayList<>();
            skillUser.addAll(userSkills.keySet());
            Map<User, Integer> finalUserSkills = userSkills;
            Collections.sort(skillUser, Comparator.comparing(user -> finalUserSkills.get(user)));
            Collections.reverse(skillUser);

            board.put(skill.getName(), new HashMap<>());
            for (int i = 0; i < 5; i++){
                board.get(skill.getName()).put(skillUser.get(i), userSkills.get(skillUser.get(i)));
            }
        }

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(out, board);

        final byte[] data = out.toByteArray();
        return new ResponseEntity(data, HttpStatus.OK);
    }
}
