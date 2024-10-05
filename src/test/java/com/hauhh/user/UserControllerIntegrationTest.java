package com.hauhh.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hauhh.common.StatusCode;
import com.hauhh.model.User;
import com.hauhh.service.UserService;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@Tag("Integration")
@AutoConfigureMockMvc
@DisplayName("Integration JWT for User Controller ")
public class UserControllerIntegrationTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private User firstUser;

    private User secondUser;

    private List<User> users;

    @Value("${api.endpoint.url}")
    private String baseURL;

    private String token;

    @BeforeEach
    void setUp() throws Exception {
        firstUser = User.builder()
                .userID(1)
                .username("Game")
                .password("pw")
                .roles("ADMIN")
                .enabled(true)
                .build();

        secondUser = User.builder()
                .userID(2)
                .username("Ngoc")
                .password("pw")
                .roles("USER")
                .enabled(true)
                .build();

        this.users = new ArrayList<>();
        this.users.add(firstUser);
        this.users.add(secondUser);


        ResultActions resultActions = this.mockMvc
                .perform(post(this.baseURL + "/auth/login")
                        .with(httpBasic("HauHH", "123")));

        MvcResult mvcResult = resultActions.andDo(print()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        JSONObject jsonObject = new JSONObject(content);

        this.token = "Bearer " + jsonObject.getJSONObject("data").getString("token");
    }


}
