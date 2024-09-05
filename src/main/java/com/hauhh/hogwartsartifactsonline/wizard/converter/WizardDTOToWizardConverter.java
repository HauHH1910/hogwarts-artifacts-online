package com.hauhh.hogwartsartifactsonline.wizard.converter;

import com.hauhh.hogwartsartifactsonline.wizard.Wizard;
import com.hauhh.hogwartsartifactsonline.wizard.WizardDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WizardDTOToWizardConverter implements Converter<WizardDTO, Wizard> {

    @Override
    public Wizard convert(WizardDTO source) {
        return Wizard.builder()
                .wizardID(source.wizardID())
                .name(source.name())
                .build();
    }
}
