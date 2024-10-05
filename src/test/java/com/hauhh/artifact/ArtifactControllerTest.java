package com.hauhh.artifact;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hauhh.dto.ArtifactDTO;
import com.hauhh.model.Artifact;
import com.hauhh.service.ArtifactService;
import com.hauhh.common.StatusCode;
import com.hauhh.exception.ObjectNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)//Turn off Spring Security
@DisplayName("Unit Test for Artifact Controller")
class ArtifactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ArtifactService artifactService;

    private List<Artifact> artifacts;

    @Value("${api.endpoint.url}")
    private String baseArtifactURL;

    @BeforeEach
    void setUp() {

        this.baseArtifactURL += "/artifacts";

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
    @DisplayName("Test Find Artifact With VALID artifactID (GET)")
    void testFindArtifactByIDSuccess() throws Exception {
        //Given
        given(this.artifactService.findById("1250808601744904191")).willReturn(this.artifacts.get(0));
        //When and then
        this.mockMvc.perform(get(this.baseArtifactURL + "/1250808601744904191")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.artifactID").value("1250808601744904191"))
                .andExpect(jsonPath("$.data.name").value("Deluminator"))
                .andExpect(jsonPath("$.data.description").value("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user."))
                .andExpect(jsonPath("$.data.imageUrl").value("imageUrl"));
    }


    @Test
    @DisplayName("Test Find Artifact With INVALID artifactID (GET)")
    void testFindArtifactByIDNotFound() throws Exception {
        //Given
        given(this.artifactService.findById("1250808601744904191")).willThrow(new ObjectNotFoundException("artifact", "1250808601744904191"));
        //When and Then
        this.mockMvc.perform(get(this.baseArtifactURL + "/1250808601744904191")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with ID: 1250808601744904191"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("Test Find All Artifact (GET)")
    void testFindAllArtifactSuccess() throws Exception {
        //Given
        given(this.artifactService.findAll()).willReturn(this.artifacts);
        //When Then
        this.mockMvc.perform(get(this.baseArtifactURL).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.artifacts.size())))
                .andExpect(jsonPath("$.data[0].artifactID").value("1250808601744904191"))
                .andExpect(jsonPath("$.data[0].name").value("Deluminator"))
                .andExpect(jsonPath("$.data[1].artifactID").value("1250808601744904192"))
                .andExpect(jsonPath("$.data[1].name").value("Invisibility Cloak"));
    }

    @Test
    @DisplayName("Test Save Artifact With VALID INPUT (POST)")
    void testSaveArtifactSuccess() throws Exception {
        //Given
        ArtifactDTO artifactDTO = ArtifactDTO.builder()
                .name("New Artifact Name")
                .description("New Artifact Description")
                .imageUrl("imageUrl")
                .build();

        String value = this.objectMapper.writeValueAsString(artifactDTO);

        Artifact saveArtifact = Artifact.builder()
                .artifactID("1250808601744904197")
                .name("New Artifact Name")
                .description("New Artifact Description")
                .imageUrl("imageUrl")
                .build();

        given(this.artifactService.addArtifact(any(Artifact.class))).willReturn(saveArtifact);
        //When Then
        this.mockMvc.perform(post(this.baseArtifactURL)
                        .content(value)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.artifactID").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(saveArtifact.getName()))
                .andExpect(jsonPath("$.data.description").value(saveArtifact.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(saveArtifact.getImageUrl()));
    }

    @Test
    @DisplayName("Test Update Artifact With VALID INPUT (PUT)")
    void testUpdateArtifactSuccess() throws Exception {
        //Given
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
        //When Then
        this.mockMvc.perform(put(this.baseArtifactURL + "/1250808601744904192")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.artifactID").value("1250808601744904192"))
                .andExpect(jsonPath("$.data.name").value(updatedArtifact.getName()))
                .andExpect(jsonPath("$.data.description").value(updatedArtifact.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(updatedArtifact.getImageUrl()));
    }

    @Test
    @DisplayName("Test Update Artifact With INVALID INPUT (PUT)")
    void testUpdateArtifactErrorWithNonExistentArtifactID() throws Exception {
        //Given
        ArtifactDTO artifactDTO = ArtifactDTO.builder()
                .name("Invisibility Cloak")
                .description("New Description")
                .imageUrl("ImageURL")
                .build();

        String requestBody = this.objectMapper.writeValueAsString(artifactDTO);

        given(this.artifactService.updateArtifact(eq("1250808601744904192"), any(Artifact.class)))
                .willThrow(new ObjectNotFoundException("artifact", "1250808601744904192"));
        //When Then
        this.mockMvc.perform(put(baseArtifactURL + "/1250808601744904192")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with ID: 1250808601744904192"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("Test Delete Artifact With VALID artifactID (DELETE)")
    void testDeleteArtifactSuccess() throws Exception {
        //Given
        doNothing().when(this.artifactService).deleteArtifact("1250808601744904192");

        //When then
        this.mockMvc.perform(delete(baseArtifactURL + "/1250808601744904192")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("Test Delete Artifact With INVALID artifactID (DELETE)")
    void testDeleteArtifactErrorWithNonExistentArtifactID() throws Exception {
        //Given
        doThrow(new ObjectNotFoundException("artifact", "1250808601744904191")).when(this.artifactService).deleteArtifact("1250808601744904191");
        //When then
        this.mockMvc.perform(delete(this.baseArtifactURL + "/1250808601744904191")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with ID: 1250808601744904191"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

}