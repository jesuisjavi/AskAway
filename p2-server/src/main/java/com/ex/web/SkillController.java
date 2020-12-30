package com.ex.web;

import com.ex.models.Skill;
import com.ex.models.User;
import com.ex.persistence.SkillRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Controller handling requests related to skills
 */
@Repository
@RequestMapping(
        path={"/skills"}
)
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SkillController {
    private SkillRepo skillRepo;

    @Autowired
    public SkillRepo setSkillRepo(SkillRepo skillRepo){
        return this.skillRepo = skillRepo;
    }

    /**
     * Gets a list of all skills in the system
     * @return a Response body containing the skills
     * @throws IOException
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<String>> getSkills() throws IOException {
        List<Skill> skills = this.skillRepo.getAll();

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(out, skills);

        final byte[] data = out.toByteArray();
        return new ResponseEntity(data, HttpStatus.OK);
    }

    /**
     * Get a particular skill by ID
     * @param skillID the id of the skill
     * @return a response
     * @throws IOException
     */
    @GetMapping(path="/{skillID}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<String>> getSkill(@PathVariable(name="skillID", required = true) Integer skillID) throws IOException{
        Skill skill = this.skillRepo.getById(skillID);

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(out, skill);

        final byte[] data = out.toByteArray();
        return new ResponseEntity(data, HttpStatus.OK);
    }

    /**
     * Add a skill to the system
     * @param skill the skill to add
     * @param result whether or not the binding was a success
     * @param model the binding
     * @return a response
     */
    @PostMapping(value = "/addSkill")
    @ResponseBody
    public ResponseEntity<String> addSkill(@ModelAttribute("skill")Skill skill,
                                          BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            return new ResponseEntity("Invalid Skill", HttpStatus.BAD_REQUEST);
        }
        this.skillRepo.save(skill);
        return new ResponseEntity("Skill added",HttpStatus.OK);
    }
}
