package com.hauhh.artifact;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hauhh.common.StatusCode;
import com.hauhh.dto.ArtifactDTO;
import com.hauhh.exception.ObjectNotFoundException;
import com.hauhh.model.Artifact;
import com.hauhh.service.ArtifactService;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@Tag("Integration")
@AutoConfigureMockMvc
@DisplayName("Integration JWT for Artifact Controller")
public class ArtifactControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ArtifactService artifactService;

    @Value("${api.endpoint.url}")
    private String baseUrl;

    private String token;

    private List<Artifact> artifacts;

    @BeforeEach
    void setUp() throws Exception {
        ResultActions resultActions = this.mockMvc
                .perform(post(this.baseUrl + "/auth/login")
                        .with(httpBasic("HauHH", "123")));

        MvcResult mvcResult = resultActions.andDo(print()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        JSONObject json = new JSONObject(content);

        this.token = "Bearer " + json.getJSONObject("data").getString("token");

        this.artifacts = new ArrayList<>();

        Artifact a1 = Artifact.builder()
                .artifactID("1250808601744904191")
                .name("Deluminator")
                .description("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.")
                .imageUrl("imageUrl")
                .build();

        Artifact a2 = Artifact.builder()
                .artifactID("1250808601744904192")
                .name("Invisibility Cloak")
                .description("An invisibility cloak is used to make the wearer invisible.")
                .imageUrl("imageUrl")
                .build();

        Artifact a3 = Artifact.builder()
                .artifactID("1250808601744904193")
                .name("Elder Wand")
                .description("The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.")
                .imageUrl("imageUrl")
                .build();

        Artifact a4 = Artifact.builder()
                .artifactID("1250808601744904194")
                .name("The Marauder's Map")
                .description("A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.")
                .imageUrl("imageUrl")
                .build();

        Artifact a5 = Artifact.builder()
                .artifactID("1250808601744904195")
                .name("The Sword Of Gryffindor")
                .description("A goblin-made sword adorned with large rubies on the pommel. It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts.")
                .imageUrl("imageUrl")
                .build();

        Artifact a6 = Artifact.builder()
                .artifactID("1250808601744904196")
                .name("Resurrection Stone")
                .description("The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them.")
                .imageUrl("imageUrl")
                .build();

        this.artifacts.addAll(List.of(a1, a2, a3, a4, a5, a6));
    }

    @Test
    @DisplayName("Test Find All Artifact (GET)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
// Reset H2 database before calling this test case.
    public void testFindAllSuccess() throws Exception {

        given(this.artifactService.findAll()).willReturn(artifacts);

        this.mockMvc.perform(get(this.baseUrl + "/artifacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
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
    @DisplayName("Test Find Artifact With VALID artifactID (GET)")
    public void testFindByIDSuccess() throws Exception {
        Artifact artifact = Artifact.builder()
                .artifactID("1250808601744904191")
                .name("Name")
                .description("Description")
                .imageUrl("ImageUrl")
                .build();

        given(this.artifactService.findById("1250808601744904191")).willReturn(artifact);

        this.mockMvc.perform(get(this.baseUrl + "/artifacts/1250808601744904191")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.artifactID").value("1250808601744904191"))
                .andExpect(jsonPath("$.data.name").value("Name"))
                .andExpect(jsonPath("$.data.description").value("Description"))
                .andExpect(jsonPath("$.data.imageUrl").value("ImageUrl"));
    }

    @Test
    @DisplayName("Test Find Artifact With INVALID artifactID (GET)")
    public void testFindByIDError() throws Exception {

        given(this.artifactService.findById("125080860174490491"))
                .willThrow(new ObjectNotFoundException("artifact", "125080860174490491"));

        this.mockMvc.perform(get(this.baseUrl + "/artifacts/125080860174490491")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with ID: 125080860174490491"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("Test Add Artifact With VALID INPUT (POST)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testAddArtifactSuccess() throws Exception {
        ArtifactDTO artifactDTO = ArtifactDTO.builder()
                .name("Remembrall")
                .description("Description")
                .imageUrl("ImageUrl")
                .build();

        String requestBody = this.objectMapper.writeValueAsString(artifactDTO);

        Artifact artifact = Artifact.builder()
                .artifactID("1250808601744904198")
                .name("Remembrall")
                .description("Description")
                .imageUrl("ImageUrl")
                .build();

        given(this.artifactService.addArtifact(any(Artifact.class))).willReturn(artifact);

        //Test #1
        this.mockMvc.perform(post(this.baseUrl + "/artifacts")
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.artifactID").value("1250808601744904198"))
                .andExpect(jsonPath("$.data.name").value("Remembrall"))
                .andExpect(jsonPath("$.data.description").value("Description"))
                .andExpect(jsonPath("$.data.imageUrl").value("ImageUrl"));

        //Test #2
        given(this.artifactService.findAll()).willReturn(List.of(artifact));

        this.mockMvc.perform(get(this.baseUrl + "/artifacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.data[0].artifactID").value("1250808601744904198"))
                .andExpect(jsonPath("$.data[0].description").value("Description"))
                .andExpect(jsonPath("$.data[0].imageUrl").value("ImageUrl"));
    }

    @Test
    @DisplayName("Test Add Artifact With INVALID INPUT (POST)")
    public void testAddArtifactError() throws Exception {
        ArtifactDTO artifactDTO = ArtifactDTO.builder()
                .name("")
                .description("")
                .imageUrl("")
                .build();

        String requestBody = this.objectMapper.writeValueAsString(artifactDTO);

        this.mockMvc.perform(post(this.baseUrl + "/artifacts")
                        .content(requestBody)
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
                .andExpect(jsonPath("$.data", Matchers.hasSize(0)));
    }

    @Test
    @DisplayName("Test Update Artifact with VALID input (PUT) ")
    public void testUpdateArtifactSuccess() throws Exception {
        ArtifactDTO artifactDTO = ArtifactDTO.builder()
                .name("Invisibility Cloak")
                .description("New Description")
                .imageUrl("ImageURL")
                .build();

        String requestBody = this.objectMapper.writeValueAsString(artifactDTO);

        Artifact updatedArtifact = Artifact.builder()
                .artifactID("1250808601744904192")
                .name("Invisibility Cloak")
                .description("New Description")
                .imageUrl("ImageURL")
                .build();


        given(this.artifactService.updateArtifact(eq("1250808601744904192"), any(Artifact.class)))
                .willReturn(updatedArtifact);

        this.mockMvc.perform(put(this.baseUrl + "/artifacts/1250808601744904192")
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.artifactID").value("1250808601744904192"))
                .andExpect(jsonPath("$.data.name").value(updatedArtifact.getName()))
                .andExpect(jsonPath("$.data.description").value(updatedArtifact.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(updatedArtifact.getImageUrl()));
    }

    @Test
    @DisplayName("Test Update Artifact with INVALID input (PUT)")
    public void testUpdateArtifactError() throws Exception {
        ArtifactDTO artifactDTO = ArtifactDTO.builder()
                .name("Invisibility Cloak")
                .description("New Description")
                .imageUrl("ImageURL")
                .build();

        String requestBody = this.objectMapper.writeValueAsString(artifactDTO);

        given(this.artifactService.updateArtifact(eq("1250808601744904192"), any(Artifact.class)))
                .willThrow(new ObjectNotFoundException("artifact", "1250808601744904192"));

        this.mockMvc.perform(put(this.baseUrl + "/artifacts/1250808601744904192")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with ID: 1250808601744904192"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("Test Delete Artifact With VALID artifactID (DELETE)")
    public void testDeleteArtifactSuccess() throws Exception {
        doNothing().when(this.artifactService).deleteArtifact("1250808601744904192");

        this.mockMvc.perform(delete(this.baseUrl + "/artifacts/1250808601744904192")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MediaType.APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("Test Delete Artifact with INVALID ArtifactID (DELETE)")
    public void testDeleteArtifactError() throws Exception {
        doThrow(new ObjectNotFoundException("artifact", "1250808601744904192")).when(this.artifactService).deleteArtifact("1250808601744904192");

        this.mockMvc.perform(delete(this.baseUrl + "/artifacts/1250808601744904192")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MediaType.APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with ID: 1250808601744904192"))
                .andExpect(jsonPath("$.data").isEmpty());
    }


}
