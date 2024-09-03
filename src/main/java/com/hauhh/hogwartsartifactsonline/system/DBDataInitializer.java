package com.hauhh.hogwartsartifactsonline.system;

import com.hauhh.hogwartsartifactsonline.artifact.Artifact;
import com.hauhh.hogwartsartifactsonline.artifact.ArtifactRepository;
import com.hauhh.hogwartsartifactsonline.wizard.Wizard;
import com.hauhh.hogwartsartifactsonline.wizard.WizardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DBDataInitializer implements CommandLineRunner {

    private final ArtifactRepository artifactRepository;
    private final WizardRepository wizardRepository;


    @Override
    public void run(String... args) throws Exception {
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

        Wizard w1 = Wizard.builder()
                .wizardID(1)
                .name("Harry Potter")
                .artifacts(new ArrayList<>())
                .build();

        w1.addArtifact(a1);
        w1.addArtifact(a2);

        Wizard w2 = Wizard.builder()
                .wizardID(2)
                .name("Hermione Granger")
                .artifacts(new ArrayList<>())
                .build();

        w2.addArtifact(a3);
        w2.addArtifact(a4);

        Wizard w3 = Wizard.builder()
                .wizardID(3)
                .name("Ron Weasley")
                .artifacts(new ArrayList<>())
                .build();

        w3.addArtifact(a5);

        /**
         * This line meaning that when saving 1 Wizard then all the Artifact that associated with Wizard will be save as well
         * -> Only need to save the Wizard
         **/
        wizardRepository.save(w1);
        wizardRepository.save(w2);
        wizardRepository.save(w3);

        artifactRepository.save(a6);

    }
}
