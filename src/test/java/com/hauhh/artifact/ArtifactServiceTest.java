package com.hauhh.artifact;

import com.hauhh.model.Artifact;
import com.hauhh.repository.ArtifactRepository;
import com.hauhh.service.ArtifactService;
import com.hauhh.exception.ObjectNotFoundException;
import com.hauhh.utils.IDUtil;
import com.hauhh.model.Wizard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test For Artifact Service")
class ArtifactServiceTest {

    @Mock //Using this annotation to real object we want to simulate, don't call the real artifact
    private ArtifactRepository artifactRepository;

    @Mock
    private IDUtil idUtil;

    @InjectMocks //Using this annotation to inject the Mock in to the class Repo -> Service
    private ArtifactService artifactService;

    private Wizard wizard;

    private Artifact artifact;

    private List<Artifact> artifacts;

    @BeforeEach
    void setUp() {
        wizard = Wizard.builder()
                .wizardID(2)
                .name("Harry Potter")
                .build();

        artifact = Artifact.builder()
                .artifactID("1250808601744904192")
                .name("Invisibility Cloak")
                .description("An invisibility cloak is used to make the wearer invisible.")
                .imageUrl("ImageURL")
                .owner(wizard)
                .build();

        Artifact a1 = Artifact.builder()
                .artifactID("1250808601744904192")
                .name("Invisibility Cloak")
                .description("An invisibility cloak is used to make the wearer invisible.")
                .imageUrl("ImageURL")
                .build();

        Artifact a2 = Artifact.builder()
                .artifactID("1250808601744904191")
                .name("Deluminator")
                .description("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter.")
                .imageUrl("ImageURL")
                .build();

        this.artifacts = new ArrayList<>();
        this.artifacts.add(a1);
        this.artifacts.add(a2);
    }

    @Test
    @DisplayName("Test Find Artifact with VALID artifactID (GET)")
    void testFindByIdSuccess() {
        //1. Given:
        given(artifactRepository.findById("1250808601744904192"))
                .willReturn(Optional.of(artifact));//Defines the behavior of mock object.
        //2. When:
        Artifact returnArtifact = artifactService.findById("1250808601744904192");
        //3. Then:
        assertThat(returnArtifact.getArtifactID()).isEqualTo(artifact.getArtifactID());
        assertThat(returnArtifact.getName()).isEqualTo(artifact.getName());
        assertThat(returnArtifact.getDescription()).isEqualTo(artifact.getDescription());
        assertThat(returnArtifact.getImageUrl()).isEqualTo(artifact.getImageUrl());

        verify(artifactRepository, times(1)).findById("1250808601744904192");
    }


    @Test
    @DisplayName("Test find Artifact with INVALID artifactID (GET)")
    void testFindByIdNotFound() {
        //Given
        given(artifactRepository.findById(Mockito.any(String.class))).willReturn(Optional.empty());
        //When
        Throwable throwable = catchThrowable(() -> {
            Artifact returnArtifact = artifactService.findById("1250808601744904192");
        });
        //Then
        assertThat(throwable)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find artifact with ID: 1250808601744904192");

        verify(artifactRepository, times(1)).findById("1250808601744904192");

    }


    @Test
    @DisplayName("Test Find All Artifact (GET)")
    void testFindAllSuccess() {
        //Given
        given(artifactRepository.findAll()).willReturn(this.artifacts);
        //When
        List<Artifact> listArtifact = artifactService.findAll();
        //Then
        assertThat(listArtifact.size()).isEqualTo(this.artifacts.size());

        verify(artifactRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test Add Artifact With VALID INPUT (POST)")
    void testSaveSuccess() {
        //Given
        Artifact newArtifact = Artifact.builder()
                .name("Artifact Name")
                .description("Artifact Description")
                .imageUrl("ImageURL")
                .build();

        given(idUtil.nextId()).willReturn(123456L);
        given(artifactRepository.save(newArtifact)).willReturn(newArtifact);
        //When
        Artifact serviceArtifact = artifactService.addArtifact(newArtifact);
        //Then
        assertThat(serviceArtifact.getArtifactID()).isEqualTo("123456");
        assertThat(serviceArtifact.getName()).isEqualTo(newArtifact.getName());
        assertThat(serviceArtifact.getDescription()).isEqualTo(newArtifact.getDescription());
        assertThat(serviceArtifact.getImageUrl()).isEqualTo(newArtifact.getImageUrl());

        verify(artifactRepository, times(1)).save(newArtifact);

    }

    @Test
    @DisplayName("Test Update Artifact With VALID INPUT (PUT)")
    void testUpdateSuccess() {
        //Given
        Artifact oldArtifact = Artifact.builder()
                .artifactID("1250808601744904192")
                .name("Invisibility Cloak")
                .description("An invisibility cloak is used to make the wearer invisible.")
                .imageUrl("ImageURL")
                .build();

        Artifact updateArtifact = Artifact.builder()
                .artifactID("1250808601744904192")
                .name("Invisibility Cloak 2")
                .description("New Description")
                .imageUrl("ImageURL2")
                .build();

        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(oldArtifact));
        given(artifactRepository.save(oldArtifact)).willReturn(oldArtifact);
        //When
        Artifact updateArtifactInService = artifactService.updateArtifact("1250808601744904192", updateArtifact);
        //Then
        assertThat(updateArtifactInService.getArtifactID()).isEqualTo("1250808601744904192");
        assertThat(updateArtifactInService.getName()).isEqualTo("Invisibility Cloak 2");
        assertThat(updateArtifactInService.getDescription()).isEqualTo("New Description");
        assertThat(updateArtifactInService.getImageUrl()).isEqualTo("ImageURL2");

        verify(artifactRepository, times(1)).findById("1250808601744904192");
        verify(artifactRepository, times(1)).save(oldArtifact);
    }

    @Test
    @DisplayName("Test Update Artifact With INVALID INPUT (PUT)")
    void testUpdateNotFound() {
        //Given
        Artifact updateArtifact = Artifact.builder()
                .artifactID("1250808601744904192")
                .name("Invisibility Cloak 2")
                .description("New Description")
                .imageUrl("ImageURL2")
                .build();

        given(artifactRepository.findById("1231231212313")).willReturn(Optional.empty());
        //When
        assertThrows(ObjectNotFoundException.class, () -> {
            artifactService.updateArtifact("1231231212313", updateArtifact);
        });
        //Then
        verify(artifactRepository, times(1)).findById("1231231212313");
    }

    @Test
    @DisplayName("Test Delete Artifact with VALID artifactID (DELETE)")
    void testDeleteSuccess() {
        //Given
        Artifact artifact = Artifact.builder()
                .artifactID("1250808601744904192")
                .name("Invisibility Cloak")
                .description("An invisibility cloak is used to make the wearer invisible.")
                .imageUrl("ImageURL")
                .build();
        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(artifact));
        doNothing().when(artifactRepository).deleteById("1250808601744904192");
        //When
        artifactService.deleteArtifact("1250808601744904192");
        //Then
        verify(artifactRepository, times(1)).deleteById("1250808601744904192");
    }

    @Test
    @DisplayName("Test Delete Artifact with artifactID (DELETE)")
    void testDeleteNotFound() {
        //Given
        Artifact artifact = Artifact.builder()
                .artifactID("1250808601744904192")
                .name("Invisibility Cloak")
                .description("An invisibility cloak is used to make the wearer invisible.")
                .imageUrl("ImageURL")
                .build();
        //When
        given(artifactRepository.findById("1231231212313")).willReturn(Optional.empty());
        //Then
        assertThrows(ObjectNotFoundException.class, () -> {
            artifactService.deleteArtifact("1231231212313");
        });

        verify(artifactRepository, times(1)).findById("1231231212313");
    }
}