package com.hauhh.wizard;

import com.hauhh.model.Artifact;
import com.hauhh.repository.ArtifactRepository;
import com.hauhh.model.Wizard;
import com.hauhh.repository.WizardRepository;
import com.hauhh.service.WizardService;
import com.hauhh.exception.ObjectNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WizardServiceTest {

    @InjectMocks
    private WizardService wizardService;

    @Mock
    private WizardRepository wizardRepository;

    @Mock
    private ArtifactRepository artifactRepository;

    private Wizard firstWizard;

    private Wizard secondWizard;

    private List<Wizard> wizards;

    private List<Artifact> artifacts;

    @BeforeEach
    void setUp() {

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
    void testGetAllWizardSuccess() {
        //Given
        given(wizardRepository.findAll()).willReturn(this.wizards);
        //Then
        List<Wizard> allWizard = wizardService.getAllWizard();
        //When
        assertThat(allWizard.size()).isEqualTo(this.wizards.size());

        verify(wizardRepository, times(1)).findAll();
    }

    @Test
    void testGetWizardByIDSuccess() {
        //Given
        given(wizardRepository.findById(1)).willReturn(Optional.of(firstWizard));
        //When
        Wizard wizardByID = wizardService.getWizardByID(1);
        //Then

        assertThat(wizardByID.getWizardID()).isEqualTo(firstWizard.getWizardID());
        assertThat(wizardByID.getName()).isEqualTo(firstWizard.getName());
        assertThat(wizardByID.getNumberOfArtifacts()).isEqualTo(firstWizard.getNumberOfArtifacts());

        verify(wizardRepository, times(1)).findById(1);
    }

    @Test
    void testGetWizardByIDNotFoundException() {
        //Given
        given(wizardRepository.findById(1)).willReturn(Optional.empty());
        //When
        Throwable throwable = catchThrowable(() -> {
            wizardService.getWizardByID(1);
        });
        //Then
        assertThat(throwable).isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void testSaveWizardSuccess() {
        //Given
        given(wizardRepository.save(firstWizard)).willReturn(firstWizard);
        //When
        Wizard wizard = wizardService.saveWizard(firstWizard);
        //Then
        assertThat(wizard.getWizardID()).isEqualTo(firstWizard.getWizardID());
        assertThat(wizard.getName()).isEqualTo(firstWizard.getName());
        assertThat(wizard.getNumberOfArtifacts()).isEqualTo(firstWizard.getNumberOfArtifacts());

        verify(wizardRepository, times(1)).save(firstWizard);
    }

    @Test
    void testUpdateWizardSuccess() {
        //Given
        Wizard existingWizard = Wizard.builder()
                .wizardID(1)
                .name("Harry Potter")
                .artifacts(this.artifacts)
                .build();

        Wizard updateWizard = Wizard.builder()
                .wizardID(1)
                .name("Harry Maguire")
                .artifacts(this.artifacts)
                .build();

        given(wizardRepository.findById(1)).willReturn(Optional.of(existingWizard));
        given(wizardRepository.save(existingWizard)).willReturn(updateWizard);
        //When
        Wizard updateWizardService = wizardService.updateWizard(1, existingWizard);
        //Then
        assertThat(updateWizardService.getWizardID()).isEqualTo(updateWizard.getWizardID());
        assertThat(updateWizardService.getName()).isEqualTo(updateWizard.getName());
        assertThat(updateWizardService.getNumberOfArtifacts()).isEqualTo(updateWizard.getNumberOfArtifacts());

        verify(wizardRepository, times(1)).findById(1);
        verify(wizardRepository, times(1)).save(existingWizard);
    }

    @Test
    void testUpdateWizardNotFound() {
        //Given
        given(wizardRepository.findById(1)).willReturn(Optional.empty());
        //When
        Throwable throwable = catchThrowable(() -> {
            wizardService.updateWizard(1, Wizard.builder()
                    .wizardID(1)
                    .name("Harry Maguire")
                    .artifacts(this.artifacts)
                    .build());
        });
        //Then
        assertThat(throwable).isInstanceOf(ObjectNotFoundException.class);

        verify(wizardRepository, times(1)).findById(1);
    }

    @Test
    void testDeleteWizardSuccess() {
        //Given
        given(wizardRepository.findById(1)).willReturn(Optional.of(secondWizard));
        doNothing().when(wizardRepository).deleteById(1);
        //When
        wizardService.deleteWizard(1);
        //Then
        verify(wizardRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteWizardNotFound() {
        //Given
        given(wizardRepository.findById(1)).willReturn(Optional.empty());
        //When
        Throwable throwable = catchThrowable(() -> {
            wizardService.deleteWizard(1);
        });
        //Then
        assertThat(throwable).isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void testAssignArtifactToWizardSuccess() {
        //Given
        Wizard wizard = Wizard.builder()
                .wizardID(1)
                .name("Harry Potter")
                .artifacts(new ArrayList<>())
                .build();

        Artifact artifact = Artifact.builder()
                .artifactID("1250808601744904190")
                .name("Invisibility Cloak 2")
                .description("A better version of Invisibility Clock")
                .imageUrl("...")
                .build();

        given(wizardRepository.findById(1)).willReturn(Optional.of(wizard));

        given(artifactRepository.findById("1250808601744904190")).willReturn(Optional.of(artifact));
        //Then
        this.wizardService.assignArtifactToWizard(1, "1250808601744904190");
        //When
        assertThat(artifact.getArtifactID()).isEqualTo("1250808601744904190");
        assertThat(artifact.getOwner().getWizardID()).isEqualTo(1);
        assertThat(artifact.getOwner().getName()).isEqualTo("Harry Potter");

        Assertions.assertThat(wizard.getArtifacts()).contains(artifact);
    }


    @Test
    void testAssignArtifactToWizardNotFound() {
        given(wizardRepository.findById(1)).willReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> {
            wizardService.assignArtifactToWizard(1, "1250808601744904190");
        });

        assertThat(throwable).isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void testAssignArtifactToWizardArtifactNotFound() {
        given(wizardRepository.findById(1)).willReturn(Optional.of(Wizard.builder()
                .wizardID(1)
                .name("Harry Potter")
                .artifacts(new ArrayList<>())
                .build()));
        given(artifactRepository.findById("1250808601744904190")).willReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> {
            wizardService.assignArtifactToWizard(1, "1250808601744904190");
        });

        assertThat(throwable).isInstanceOf(ObjectNotFoundException.class);
    }
}