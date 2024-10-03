package com.hauhh.converter.wizard;

import com.hauhh.model.Wizard;
import com.hauhh.dto.WizardDTO;
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
