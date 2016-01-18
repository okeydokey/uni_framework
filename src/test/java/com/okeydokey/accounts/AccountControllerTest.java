package com.okeydokey.accounts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.okeydokey.UniFrameworkApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author okeydokey
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = UniFrameworkApplication.class)
@WebAppConfiguration
@Transactional // auto rollback
public class AccountControllerTest {
    @Autowired
    WebApplicationContext wac;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired AccountService service;

    MockMvc mockMvc;

    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
    @Test
    public void createAccount() throws Exception {
        AccountDto.Create createDto = new AccountDto.Create();
        createDto.setUsername("okeydokey");
        createDto.setPassword("password");

        ResultActions result = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)    // header setting
                .content(objectMapper.writeValueAsString(createDto)));

        result.andDo(print());
        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.username", is("okeydokey")));

        // UserDuplicated Test
        result = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)    // header setting
                .content(objectMapper.writeValueAsString(createDto)));

        result.andDo(print());
        result.andExpect(status().isBadRequest());
        result.andExpect(jsonPath("$.code", is("duplicated.username.exception")));
    }

    @Test
    public void createAccount_BadRequest() throws Exception {
        AccountDto.Create createDto = new AccountDto.Create();
        createDto.setUsername(" ");
        createDto.setPassword("1234");

        ResultActions result = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)    // header setting
                .content(objectMapper.writeValueAsString(createDto)));

        result.andDo(print());
        result.andExpect(status().isBadRequest());
        result.andExpect(jsonPath("$.code", is("bad.request")));
    }

    @Test
    public void getAccounts() throws Exception {
        AccountDto.Create createDto = new AccountDto.Create();
        createDto.setUsername("okeydokey");
        createDto.setPassword("password");
        service.createAccount(createDto);

        ResultActions result = mockMvc.perform(get("/accounts"));

        result.andDo(print());
        result.andExpect(status().isOk());
    }
}