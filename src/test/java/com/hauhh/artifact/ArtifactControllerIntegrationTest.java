package com.hauhh.artifact;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hauhh.common.StatusCode;
import com.hauhh.model.Artifact;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@Tag("Integration")
@AutoConfigureMockMvc
@DisplayName("Integration Test for Artifact API endpoints")
public class ArtifactControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${api.endpoint.url}")
    private String baseUrl;

    private String token;

    @BeforeEach
    void setUp() throws Exception {
        ResultActions resultActions = this.mockMvc
                .perform(post(this.baseUrl + "/auth/login")
                        .with(httpBasic("HauHH", "123")));

        MvcResult mvcResult = resultActions.andDo(print()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        JSONObject json = new JSONObject(content);

        this.token = "Bearer " + json.getJSONObject("data").getString("token");
    }

    @Test
    @DisplayName("Check findAllArtifacts (GET)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
// Reset H2 database before calling this test case.
    public void testFindAllSuccess() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/artifacts")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag")
                        .value(true))
                .andExpect(jsonPath("$.code")
                        .value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message")
                        .value("Find All Success"))
                .andExpect(jsonPath("$.data",
                        Matchers.hasSize(6)));
    }

    @Test
    @DisplayName("Check findByID (GET)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void testFindByIDSuccess() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/artifacts/1250808601744904191")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.artifactID").value("1250808601744904191"));
    }

    @Test
    @DisplayName("Check findByID with non-existent ID (GET)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void testFindByIDError() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/artifacts/125080860174490491")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with ID: 125080860174490491"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("Check addArtifact with valid input (POST)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testAddArtifactSuccess() throws Exception {
        Artifact a = new Artifact();
        a.setName("Remembrall");
        a.setDescription("A Remembrall was a magical large marble-sized glass ball that contained smoke which turned red when its owner or user had forgotten something." +
                " It turned clear once whatever was forgotten was remembered.");
        a.setImageUrl("ImageUrl");

        String json = this.objectMapper.writeValueAsString(a);

        //Test #1
        this.mockMvc.perform(post(this.baseUrl + "/artifacts")
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag")
                        .value(true))
                .andExpect(jsonPath("$.code")
                        .value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message")
                        .value("Add Success"))
                .andExpect(jsonPath("$.data.artifactID")
                        .isNotEmpty())
                .andExpect(jsonPath("$.data.name")
                        .value("Remembrall"))
                .andExpect(jsonPath("$.data.description")
                        .value("A Remembrall was a magical large marble-sized glass ball that contained smoke which turned red when its owner or user had forgotten something." +
                                " It turned clear once whatever was forgotten was remembered."))
                .andExpect(jsonPath("$.data.imageUrl")
                        .value("ImageUrl"));

        //Test #2
        this.mockMvc.perform(get(this.baseUrl + "/artifacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag")
                        .value(true))
                .andExpect(jsonPath("$.code")
                        .value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message")
                        .value("Find All Success"))
                .andExpect(jsonPath("$.data",
                        Matchers.hasSize(7)));
    }

    @Test
    @DisplayName("Check addArtifact with invalid input (POST)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void testAddArtifactError() throws Exception {
        Artifact a = Artifact.builder()
                .name("")
                .description("")
                .imageUrl("")
                .build();

        String json = this.objectMapper.writeValueAsString(a);


        this.mockMvc.perform(post(this.baseUrl + "/artifacts")
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details."))
                .andExpect(jsonPath("$.data.name").value("Name is required"))
                .andExpect(jsonPath("$.data.description").value("Description is required"))
                .andExpect(jsonPath("$.data.imageUrl").value("ImageURL is required"));

        this.mockMvc.perform(get(this.baseUrl + "/artifacts")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(6)));

    }

}
