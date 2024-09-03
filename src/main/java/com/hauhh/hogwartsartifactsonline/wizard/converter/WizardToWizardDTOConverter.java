package com.hauhh.hogwartsartifactsonline.wizard.converter;

import com.hauhh.hogwartsartifactsonline.wizard.Wizard;
import com.hauhh.hogwartsartifactsonline.wizard.WizardDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WizardToWizardDTOConverter implements Converter<Wizard, WizardDTO> {

    @Override
    public WizardDTO convert(Wizard source) {
        return WizardDTO.builder()
                .wizardID(source.getWizardID())
                .name(source.getName())
                .numberOfArtifacts(source.getNumberOfArtifacts())
                .build();
    }
}
