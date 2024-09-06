package com.hauhh.hogwartsartifactsonline.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hauhh.hogwartsartifactsonline.system.StatusCode;
import com.hauhh.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import com.hauhh.hogwartsartifactsonline.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    private User user1;

    private List<User> users;

    @Value("${api.endpoint.url}")
    private String baseURL;

    @BeforeEach
    void setUp() {
        this.baseURL += "/users";

        user = User.builder()
                .userID(1)
                .username("Game")
                .password("pw")
                .roles("user")
                .enabled(true)
                .build();

        user1 = User.builder()
                .userID(2)
                .username("Ngoc")
                .password("pw")
                .roles("manager")
                .enabled(true)
                .build();

        this.users = new ArrayList<>();
        this.users.add(user);
        this.users.add(user1);
    }

    @Test
    void testGetAllUsersSuccess() throws Exception {
        //Given
        given(userService.getAllUsers()).willReturn(this.users);
        //When then
        this.mockMvc.perform(get(this.baseURL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data[0].userID").value(1))
                .andExpect(jsonPath("$.data[0].username").value("Game"))
                .andExpect(jsonPath("$.data[0].enabled").value(true))
                .andExpect(jsonPath("$.data[0].roles").value("user"))
                .andExpect(jsonPath("$.data[1].userID").value(2))
                .andExpect(jsonPath("$.data[1].username").value("Ngoc"))
                .andExpect(jsonPath("$.data[1].enabled").value(true))
                .andExpect(jsonPath("$.data[1].roles").value("manager"));

    }

    @Test
    void testGetUserByIDSuccess() throws Exception {
        //Given
        given(userService.getUserById(1)).willReturn(user);
        //When
        this.mockMvc.perform(get(this.baseURL + "/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.userID").value(1))
                .andExpect(jsonPath("$.data.username").value("Game"))
                .andExpect(jsonPath("$.data.enabled").value(true))
                .andExpect(jsonPath("$.data.roles").value("user"));
    }

    @Test
    void testGetUserByIDNotFound() throws Exception {
        //Given
        given(userService.getUserById(1)).willThrow(new ObjectNotFoundException("user", 1));
        //Then when
        this.mockMvc.perform(get(this.baseURL + "/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with ID: 1"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testCreateUserSuccess() throws Exception {
        //Given
        UserDTO userDTO = UserDTO.builder()
                .userID(null)
                .username("Lily")
                .enabled(true)
                .roles("user")
                .build();

        String requestBody = this.objectMapper.writeValueAsString(userDTO);

        User user = User.builder()
                .userID(1)
                .username("Lily")
                .password("pw")
                .enabled(true)
                .roles("user")
                .build();

        given(userService.createUser(any(User.class))).willReturn(user);
        //Then When
        this.mockMvc.perform(post(this.baseURL)
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.userID").value(1))
                .andExpect(jsonPath("$.data.username").value("Lily"))
                .andExpect(jsonPath("$.data.enabled").value(true))
                .andExpect(jsonPath("$.data.roles").value("user"));
    }

    @Test
    void testUpdateUserSuccess() throws Exception {
        //Given
        UserDTO userDTO = UserDTO.builder()
                .userID(null)
                .username("john-update")
                .enabled(true)
                .roles("admin user")
                .build();

        User user = User.builder()
                .userID(1)
                .username("john-update")
                .password("pw")
                .enabled(true)
                .roles("admin user")
                .build();

        String requestBody = this.objectMapper.writeValueAsString(userDTO);

        given(userService.updateUser(eq(1), any(User.class))).willReturn(user);

        //When then
        this.mockMvc.perform(put(this.baseURL + "/1")
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.userID").value(1))
                .andExpect(jsonPath("$.data.username").value("john-update"))
                .andExpect(jsonPath("$.data.enabled").value(true))
                .andExpect(jsonPath("$.data.roles").value("admin user"));
    }

    @Test
    void testUpdateUserNotFound() throws Exception {
        //Given
        String requestBody = this.objectMapper
                .writeValueAsString(
                        UserDTO.builder()
                                .userID(null)
                                .username("Hau")
                                .enabled(true)
                                .roles("user")
                                .build()
                );
        given(userService.updateUser(eq(1), any(User.class))).willThrow(new ObjectNotFoundException("user", 1));
        //When then
        this.mockMvc.perform(put(this.baseURL + "/1")
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with ID: 1"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteUserSuccess() throws Exception {
        //Given
        doNothing().when(userService).deleteUser(eq(1));
        //When then
        this.mockMvc.perform(delete(this.baseURL + "/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteUserNotFound() throws Exception{
        //Given
        doThrow(new ObjectNotFoundException("user", 1)).when(userService).deleteUser(1);
        //When
        this.mockMvc.perform(delete(this.baseURL + "/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("Could not find user with ID: 1"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}