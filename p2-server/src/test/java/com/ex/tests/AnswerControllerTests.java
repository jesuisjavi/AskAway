package com.ex.tests;

import com.ex.viewModels.AnswerQuestionForm;
import com.ex.viewModels.PostQuestionForm;
import com.ex.viewModels.SelectTopAnswerForm;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/application-context.xml"})
@WebAppConfiguration
public class AnswerControllerTests {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void shouldProvideCommentsController() {
        ServletContext servletContext = wac.getServletContext();

        Assert.assertNotNull(servletContext);
        Assert.assertTrue(servletContext instanceof MockServletContext);
        for(String s:wac.getBeanDefinitionNames()){
            System.out.println(s);
        }
        Assert.assertNotNull(wac.getBean("answerController"));
    }

    @Test
    public void shouldReturnProperContentTypeForGettingAnswersOnQuestion() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/answers/1"))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();

        Assert.assertEquals("application/json",
                mvcResult.getResponse().getContentType());
    }

    @Test
    public void shouldAddANewAnswer() throws Exception {
        AnswerQuestionForm form = new AnswerQuestionForm();
        form.setDetails("DETAILS OF THIS ANSWER");
        form.setUserID(5);
        form.setQuestionID(2);

        Gson gson = new Gson();
        String json = gson.toJson(form);
        System.out.println(json);

        MvcResult mvcResult = this.mockMvc.perform(post("/answers/addAnswer")
                .contentType(MediaType.APPLICATION_JSON).content(json)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Assert.assertEquals(HttpStatus.OK.value(),
                mvcResult.getResponse().getStatus());
    }

    @Test
    public void shouldSelectTopAnswer() throws Exception {
        SelectTopAnswerForm form = new SelectTopAnswerForm();
        form.setUserID(5);
        form.setAnswerID(2);

        Gson gson = new Gson();
        String json = gson.toJson(form);
        System.out.println(json);

        MvcResult mvcResult = this.mockMvc.perform(post("/answers/selectTop")
                .contentType(MediaType.APPLICATION_JSON).content(json)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Assert.assertEquals(HttpStatus.OK.value(),
                mvcResult.getResponse().getStatus());
    }
}
