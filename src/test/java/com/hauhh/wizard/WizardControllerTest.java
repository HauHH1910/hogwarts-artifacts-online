package com.hauhh.wizard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hauhh.dto.WizardDTO;
import com.hauhh.model.Artifact;
import com.hauhh.model.Wizard;
import com.hauhh.service.WizardService;
import com.hauhh.common.StatusCode;
import com.hauhh.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)//Turn off Spring Security
class WizardControllerTest {

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

    @BeforeEach
    void setUp() {

        this.baseWizardURL += "/wizard";


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
    }

    @Test
    void testGetAllSuccess() throws Exception {
        //Given
        given(wizardService.getAllWizard()).willReturn(this.wizards);
        //Then When
        this.mockMvc.perform(get(this.baseWizardURL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data[0].wizardID").value(1))
                .andExpect(jsonPath("$.data[0].name").value("Harry Potter"))
                .andExpect(jsonPath("$.data[0].numberOfArtifacts").value(3))
                .andExpect(jsonPath("$.data[1].wizardID").value(2))
                .andExpect(jsonPath("$.data[1].name").value("Draco Malfoy"))
                .andExpect(jsonPath("$.data[1].numberOfArtifacts").value(2));
    }

    @Test
    void testGetWizardByIDSuccess() throws Exception {
        //Given
        given(wizardService.getWizardByID(1)).willReturn(this.firstWizard);
        //When then
        this.mockMvc.perform(get(this.baseWizardURL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.wizardID").value(1))
                .andExpect(jsonPath("$.data.name").value("Harry Potter"))
                .andExpect(jsonPath("$.data.numberOfArtifacts").value(3));
    }

    @Test
    void testGetWizardByIDNotFoundException() throws Exception {
        //Given
        given(wizardService.getWizardByID(1)).willThrow(new ObjectNotFoundException("wizard", 1));
        //When then
        this.mockMvc.perform(get(this.baseWizardURL + "/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with ID: 1"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testSaveWizardSuccess() throws Exception {
        //Given
        WizardDTO wizardDTO = WizardDTO.builder()
                .wizardID(null)
                .name("Draco Malfoy")
                .numberOfArtifacts(0)
                .build();

        String requestBody = this.objectMapper.writeValueAsString(wizardDTO);

        Wizard wizard = Wizard.builder()
                .wizardID(1)
                .name("Draco Malfoy")
                .artifacts(new ArrayList<>())
                .build();

        given(wizardService.saveWizard(Mockito.any(Wizard.class))).willReturn(wizard);
        //Then When
        this.mockMvc.perform(post(this.baseWizardURL)
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.wizardID").value(1))
                .andExpect(jsonPath("$.data.name").value("Draco Malfoy"))
                .andExpect(jsonPath("$.data.numberOfArtifacts").value(0));
    }

    @Test
    void testUpdateWizardSuccess() throws Exception {
        //Given
        Wizard existingWizard = Wizard.builder()
                .wizardID(1)
                .name("Draco Malfoy")
                .artifacts(new ArrayList<>())
                .build();

        String requestBody = this.objectMapper.writeValueAsString(existingWizard);

        Wizard updateWizard = Wizard.builder()
                .wizardID(1)
                .name("Dracula")
                .artifacts(new ArrayList<>())
                .build();

        given(wizardService.updateWizard(eq(1), Mockito.any(Wizard.class))).willReturn(updateWizard);
        //Then When
        this.mockMvc.perform(put(this.baseWizardURL + "/1")
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.wizardID").value(1))
                .andExpect(jsonPath("$.data.name").value("Dracula"))
                .andExpect(jsonPath("$.data.numberOfArtifacts").value(0));
    }

    @Test
    void testUpdateWizardNotFound() throws Exception {
        //Given
        WizardDTO wizardDTO = WizardDTO.builder()
                .wizardID(10)
                .name("Draco Malfoy")
                .numberOfArtifacts(3)
                .build();

        String requestBody = this.objectMapper.writeValueAsString(wizardDTO);

        given(wizardService.updateWizard(eq(1), Mockito.any(Wizard.class))).willThrow(new ObjectNotFoundException("wizard", 1));
        //When then
        this.mockMvc.perform(put(this.baseWizardURL + "/1")
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with ID: 1"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteWizardSuccess() throws Exception {
        //Given
        doNothing().when(wizardService).deleteWizard(1);
        //Then when
        this.mockMvc.perform(delete(this.baseWizardURL + "/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteWizardNotFound() throws Exception {
        //Given
        doThrow(new ObjectNotFoundException("wizard", 1)).when(wizardService).deleteWizard(1);
        //When then
        this.mockMvc.perform(delete(this.baseWizardURL + "/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with ID: 1"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAssignArtifactToWizardSuccess() throws Exception {
        //Given
        doNothing().when(this.wizardService).assignArtifactToWizard(1, "1250808601744904190");
        //When then
        this.mockMvc.perform(put(this.baseWizardURL + "/1/artifacts/1250808601744904190")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Artifact Assignment Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAssignArtifactToWizardNotFound() throws Exception {
        //Given
        doThrow(new ObjectNotFoundException("wizard", 1)).when(this.wizardService).assignArtifactToWizard(1, "1250808601744904190");
        //When then
        this.mockMvc.perform(put(this.baseWizardURL + "/1/artifacts/1250808601744904190")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with ID: 1"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAssignArtifactToWizardArtifactNotFound() throws Exception {
        //Given
        doThrow(new ObjectNotFoundException("artifact", "1250808601744904190")).when(this.wizardService).assignArtifactToWizard(1, "1250808601744904190");
        //When then
        this.mockMvc.perform(put(this.baseWizardURL + "/1/artifacts/1250808601744904190")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with ID: 1250808601744904190"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}