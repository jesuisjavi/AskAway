package com.ex.tests;

import com.ex.models.Question;
import com.ex.viewModels.PostQuestionForm;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/application-context.xml"})
@WebAppConfiguration
public class QuestionControllerTests {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void shouldProvideQuestionController() {
        ServletContext servletContext = wac.getServletContext();

        Assert.assertNotNull(servletContext);
        Assert.assertTrue(servletContext instanceof MockServletContext);
        for(String s:wac.getBeanDefinitionNames()){
            System.out.println(s);
        }
        Assert.assertNotNull(wac.getBean("questionController"));
    }

    @Test
    public void shouldReturnProperContentTypeForGettingQuestions() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/questions"))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();

        Assert.assertEquals("application/json",
                mvcResult.getResponse().getContentType());
    }

    @Test
    public void shouldReturnProperContentTypeForGettingAQuestion() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/questions/1"))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();

        Assert.assertEquals("application/json",
                mvcResult.getResponse().getContentType());
    }

    @Test
    public void shouldReturnProperContentTypeForGettingRecentQuestions() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/questions/recent"))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();

        Assert.assertEquals("application/json",
                mvcResult.getResponse().getContentType());
    }

    @Test
    public void shouldAddANewQuestion() throws Exception {
        PostQuestionForm form = new PostQuestionForm();
        form.setDetails("DETAILS OF THIS QUESTION");
        form.setTitle("TEST QUESTION DELETE ME");
        form.setUserID(29);
        form.setTags(new String[]{"CSS"});

        Gson gson = new Gson();
        String json = gson.toJson(form);
        System.out.println(json);

        MvcResult mvcResult = this.mockMvc.perform(post("/questions/addQuestion")
                .contentType(MediaType.APPLICATION_JSON).content(json)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Assert.assertEquals(HttpStatus.OK.value(),
                mvcResult.getResponse().getStatus());
    }

    @Test
    public void shouldDenySelectTopAnswerToIncorrectUser() throws Exception {
        String email = "notRightUser@gmail.com";
        String questionID = "1";
        String answerID = "1";

        MvcResult mvcResult = this.mockMvc.perform(post("/questions/selectTop")
                .contentType(MediaType.APPLICATION_JSON)
                .param("email", email)
                .param("questionID",questionID)
                .param("answerID",answerID)
        )
                .andDo(print())
                .andExpect(status().isForbidden())
                .andReturn();

        Assert.assertEquals(HttpStatus.FORBIDDEN.value(),
                mvcResult.getResponse().getStatus());
    }

    @Test
    public void shouldDenySelectTopAnswerToUserWhoIsNotPoster() throws Exception {
        String email = "testuser2@gmail.com@gmail.com";
        String questionID = "1";
        String answerID = "1";

        MvcResult mvcResult = this.mockMvc.perform(post("/questions/selectTop")
                .contentType(MediaType.APPLICATION_JSON)
                .param("email", email)
                .param("questionID",questionID)
                .param("answerID",answerID)
        )
                .andDo(print())
                .andExpect(status().isForbidden())
                .andReturn();

        Assert.assertEquals(HttpStatus.FORBIDDEN.value(),
                mvcResult.getResponse().getStatus());
    }
}
