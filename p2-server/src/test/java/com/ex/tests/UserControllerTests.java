package com.ex.tests;

import com.ex.models.User;
import com.ex.viewModels.PostQuestionForm;
import com.ex.viewModels.UpdateProfileForm;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/application-context.xml"})
@WebAppConfiguration
public class UserControllerTests {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void shouldProvideUserController() {
        ServletContext servletContext = wac.getServletContext();

        Assert.assertNotNull(servletContext);
        Assert.assertTrue(servletContext instanceof MockServletContext);
        Assert.assertNotNull(wac.getBean("userController"));
    }

    @Test
    public void shouldReturnProperContentTypeForGettingUsers() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/users"))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();

        Assert.assertEquals("application/json",
                mvcResult.getResponse().getContentType());
    }

    @Test
    public void shouldReturnProperContentTypeForGettingAUser() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/users/5"))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();

        Assert.assertEquals("application/json",
                mvcResult.getResponse().getContentType());
    }

    @Test
    public void shouldReturnProperContentTypeForGettingLeaders() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/users/leaders"))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();

        Assert.assertEquals("application/json",
                mvcResult.getResponse().getContentType());
    }

    @Test
    public void shouldReturnProperContentTypeForGettingUserSkills() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/users/userskills/5"))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();

        Assert.assertEquals("application/json",
                mvcResult.getResponse().getContentType());
    }

    @Test
    public void shouldReturnUserFromPost() throws Exception {
        String email = "cassieccolvin@gmail.com";

        MvcResult mvcResult = this.mockMvc.perform(post("/users/loadUser")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", email))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();

        Assert.assertEquals(HttpStatus.OK.value(),
                mvcResult.getResponse().getStatus());
    }

    @Test
    public void shouldAddUserToDBAndReturnOK() throws Exception {
        // Grabbed a quick example string generator soley for the sake of testing.
        //https://www.baeldung.com/java-random-string
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        System.out.println(generatedString);
        UpdateProfileForm form = new UpdateProfileForm();
        form.setEmail(generatedString+"@gmail.com");
        form.setFirstName("TEST");
        form.setLastName("TEST");
        form.setGitHub(generatedString);

        Gson gson = new Gson();
        String json = gson.toJson(form);
        System.out.println(json);

        MvcResult mvcResult = this.mockMvc.perform(post("/users/updateUser")
                .contentType(MediaType.APPLICATION_JSON).content(json)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Assert.assertEquals(HttpStatus.OK.value(),
                mvcResult.getResponse().getStatus());
    }

    @Test
    public void shouldUpdateUserToDBAndReturnOK() throws Exception {
        // Grabbed a quick example string generator soley for the sake of testing.
        //https://www.baeldung.com/java-random-string
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        System.out.println(generatedString);
        UpdateProfileForm form = new UpdateProfileForm();
        form.setEmail("testuser@gmail.com");
        form.setFirstName("TEST");
        form.setLastName("TEST");
        form.setGitHub(generatedString);

        Gson gson = new Gson();
        String json = gson.toJson(form);
        System.out.println(json);

        MvcResult mvcResult = this.mockMvc.perform(post("/users/updateUser")
                .contentType(MediaType.APPLICATION_JSON).content(json)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Assert.assertEquals(HttpStatus.OK.value(),
                mvcResult.getResponse().getStatus());
    }

    @Test
    public void shouldTryToAddExistingSkillToUser() throws Exception {
        String userID = "5";
        String skillID = "5";

        MvcResult mvcResult = this.mockMvc.perform(post("/users/addSkillToUser")
                .contentType(MediaType.APPLICATION_JSON)
                .param("userID", userID)
                .param("skillID",skillID)
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(),
                mvcResult.getResponse().getStatus());
    }
}
