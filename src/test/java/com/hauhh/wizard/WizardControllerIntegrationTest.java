package com.hauhh.wizard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hauhh.common.StatusCode;
import com.hauhh.dto.WizardDTO;
import com.hauhh.exception.ObjectNotFoundException;
import com.hauhh.model.Artifact;
import com.hauhh.model.Wizard;
import com.hauhh.service.WizardService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@Tag("Integration")
@AutoConfigureMockMvc
@DisplayName("Integration JWT for Wizard Controller")
public class WizardControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WizardService wizardService;

    private Wizard firstWizard;

    private Wizard secondWizard;

    private List<Wizard> wizards;

    private List<Artifact> artifacts;

    @Value("${api.endpoint.url}")
    private String baseWizardURL;

    private String token;

    @BeforeEach
    void setUp() throws Exception {
        Artifact firstArtifact = Artifact.builder()
                .artifactID("1250808601744904192")
                .name("Invisibility Cloak")
                .description("An invisibility cloak is used to make the wearer invisible.")
                .imageUrl("ImageURL")
                .build();

        Artifact secondArtifact = Artifact.builder()
                .artifactID("1250808601744904191")
                .name("Deluminator")
                .description("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter.")
                .imageUrl("ImageURL")
                .build();

        Artifact thirdArtifact = Artifact.builder()
                .artifactID("1250808601744904193")
                .name("Elder Wand")
                .description("The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.")
                .imageUrl("imageUrl")
                .build();

        Artifact fourArtifact = Artifact.builder()
                .artifactID("1250808601744904194")
                .name("The Marauder's Map")
                .description("A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.")
                .imageUrl("imageUrl")
                .build();

        Artifact fiveArtifact = Artifact.builder()
                .artifactID("1250808601744904195")
                .name("The Sword Of Gryffindor")
                .description("A goblin-made sword adorned with large rubies on the pommel. It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts.")
                .imageUrl("imageUrl")
                .build();

        this.artifacts = new ArrayList<>();
        this.artifacts.add(firstArtifact);
        this.artifacts.add(secondArtifact);
        this.artifacts.add(thirdArtifact);
        this.artifacts.add(fourArtifact);
        this.artifacts.add(fiveArtifact);

        firstWizard = Wizard.builder()
                .wizardID(1)
                .name("Harry Potter")
                .artifacts(new ArrayList<>())
                .build();

        firstWizard.addArtifact(firstArtifact);
        firstWizard.addArtifact(thirdArtifact);
        firstWizard.addArtifact(fourArtifact);

        secondWizard = Wizard.builder()
                .wizardID(2)
                .name("Draco Malfoy")
                .artifacts(new ArrayList<>())
                .build();

        secondWizard.addArtifact(secondArtifact);
        secondWizard.addArtifact(fiveArtifact);

        this.wizards = new ArrayList<>();
        this.wizards.add(firstWizard);
        this.wizards.add(secondWizard);

        ResultActions resultActions = this.mockMvc
                .perform(post(this.baseWizardURL + "/auth/login")
                        .with(httpBasic("HauHH", "123")));

        MvcResult mvcResult = resultActions.andDo(print()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        JSONObject jsonObject = new JSONObject(content);

        this.token = "Bearer " + jsonObject.getJSONObject("data").getString("token");
    }


    @Test
    @DisplayName("Test Find All Wizard (GET)")
    public void testFindAllWizard() throws Exception {
        given(this.wizardService.getAllWizard()).willReturn(wizards);

        this.mockMvc.perform(get(this.baseWizardURL + "/wizard")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(2)));
    }


    @Test
    @DisplayName("Test Find Wizard By wizardID With VALID wizardID (GET)")
    public void testFindByIDWizard() throws Exception {

        given(this.wizardService.getWizardByID(1)).willReturn(firstWizard);

        this.mockMvc.perform(get(this.baseWizardURL + "/wizard/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.wizardID").value(1))
                .andExpect(jsonPath("$.data.name").value("Harry Potter"))
                .andExpect(jsonPath("$.data.numberOfArtifacts").value(3));
    }


    @Test
    @DisplayName("Test Find Wizard By wizardID With INVALID wizardID (GET)")
    public void testFindByIDWizardInvalid() throws Exception {
        given(this.wizardService.getWizardByID(2)).willThrow(new ObjectNotFoundException("wizard", 2));

        this.mockMvc.perform(get(this.baseWizardURL + "/wizard/2")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with ID: 2"));
    }


    @Test
    @DisplayName("Test Save Wizard With VALID Input (POST)")
    public void testSaveWizard() throws Exception {
        WizardDTO wizardDTO = WizardDTO.builder()
                .name("HaiHau")
                .build();

        String requestBody = this.objectMapper.writeValueAsString(wizardDTO);

        Wizard wizard = Wizard.builder()
                .wizardID(3)
                .name("HaiHau")
                .artifacts(new ArrayList<>())
                .build();

        given(this.wizardService.saveWizard(any(Wizard.class))).willReturn(wizard);


        this.mockMvc.perform(post(this.baseWizardURL + "/wizard")
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.wizardID").value(3))
                .andExpect(jsonPath("$.data.name").value("HaiHau"));
    }

    @Test
    @DisplayName("Test Save Wizard With INVALID Input (POST)")
    public void testSaveWizardError() throws Exception {
        WizardDTO wizardDTO = WizardDTO.builder()
                .name("")
                .build();

        String requestBody = this.objectMapper.writeValueAsString(wizardDTO);

        this.mockMvc.perform(post(this.baseWizardURL + "/wizard")
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details."))
                .andExpect(jsonPath("$.data.name").value("Name is required"));
    }

    @Test
    @DisplayName("Test Update Wizard With VALID Input (PUT)")
    public void testUpdateWizardSuccess() throws Exception {
        WizardDTO wizardDTO = WizardDTO.builder()
                .name("HaiHau")
                .build();

        String requestBody = this.objectMapper.writeValueAsString(wizardDTO);

        Wizard wizard = Wizard.builder()
                .wizardID(3)
                .name("HaiHau")
                .artifacts(artifacts)
                .build();

        given(this.wizardService.updateWizard(eq(3), any(Wizard.class))).willReturn(wizard);

        this.mockMvc.perform(put(this.baseWizardURL + "/wizard/3")
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.wizardID").value(3))
                .andExpect(jsonPath("$.data.name").value("HaiHau"))
                .andExpect(jsonPath("$.data.numberOfArtifacts").value(5));
    }

    @Test
    @DisplayName("Test Update Wizard With INVALID Input (PUT)")
    public void testUpdateWizardError() throws Exception {
        WizardDTO wizardDTO = WizardDTO.builder()
                .name("")
                .build();

        String requestBody = this.objectMapper.writeValueAsString(wizardDTO);

        this.mockMvc.perform(put(this.baseWizardURL + "/wizard/3")
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details."))
                .andExpect(jsonPath("$.data.name").value("Name is required"));
        ;
    }

    @Test
    @DisplayName("Test Delete Wizard With VALID wizardID (DELETE)")
    public void testDeleteWizardSuccess() throws Exception {
        doNothing().when(this.wizardService).deleteWizard(eq(1));

        this.mockMvc.perform(delete(this.baseWizardURL + "/wizard/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"));
    }

    @Test
    @DisplayName("Test Delete Wizard With INVALID wizardID (DELETE)")
    public void testDeleteWizardError() throws Exception {
        doThrow(new ObjectNotFoundException("wizard", 1)).when(this.wizardService).deleteWizard(eq(1));

        this.mockMvc.perform(delete(this.baseWizardURL + "/wizard/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with ID: 1"));
    }
}
