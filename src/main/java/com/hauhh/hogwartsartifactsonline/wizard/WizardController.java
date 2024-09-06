package com.hauhh.hogwartsartifactsonline.wizard;

import com.hauhh.hogwartsartifactsonline.system.Result;
import com.hauhh.hogwartsartifactsonline.system.StatusCode;
import com.hauhh.hogwartsartifactsonline.wizard.converter.WizardDTOToWizardConverter;
import com.hauhh.hogwartsartifactsonline.wizard.converter.WizardToWizardDTOConverter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.endpoint.url}/wizard")
public class WizardController {

    private final WizardService wizardService;
    private final WizardToWizardDTOConverter entityToDTOConverter;
    private final WizardDTOToWizardConverter dtoToEntityConverter;

    @GetMapping
    public Result<List<WizardDTO>> getAllWizard() {
        return Result.<List<WizardDTO>>builder()
                .flag(true)
                .code(StatusCode.SUCCESS)
                .message("Find All Success")
                .data(this.wizardService.getAllWizard().stream()
                        .map(entityToDTOConverter::convert)
                        .toList())
                .build();
    }

    @GetMapping("/{wizardID}")
    public Result<WizardDTO> getWizardByID(@PathVariable Integer wizardID) {
        return Result.<WizardDTO>builder()
                .flag(true)
                .code(StatusCode.SUCCESS)
                .message("Find One Success")
                .data(this.entityToDTOConverter.convert(
                        this.wizardService.getWizardByID(
                                wizardID
                        )
                ))
                .build();
    }

    @PostMapping
    public Result<WizardDTO> createWizard(@Valid @RequestBody WizardDTO wizardDTO) {
        return Result.<WizardDTO>builder()
                .flag(true)
                .code(StatusCode.SUCCESS)
                .message("Add Success")
                .data(this.entityToDTOConverter.convert(
                        this.wizardService.saveWizard(
                                this.dtoToEntityConverter.convert(
                                        wizardDTO
                                )
                        )
                ))
                .build();
    }

    @PutMapping("/{wizardID}")
    public Result<WizardDTO> updateWizard(@PathVariable Integer wizardID, @Valid @RequestBody WizardDTO wizardDTO) {
        return Result.<WizardDTO>builder()
                .flag(true)
                .code(StatusCode.SUCCESS)
                .message("Update Success")
                .data(this.entityToDTOConverter.convert(
                        this.wizardService.updateWizard(wizardID,
                                this.dtoToEntityConverter.convert(
                                        wizardDTO
                                ))
                ))
                .build();
    }

    @DeleteMapping("/{wizardID}")
    public Result<Void> deleteWizard(@PathVariable Integer wizardID) {
        this.wizardService.deleteWizard(wizardID);
        return Result.<Void>builder()
                .flag(true)
                .code(StatusCode.SUCCESS)
                .message("Delete Success")
                .data(null)
                .build();
    }

    @PutMapping("/{wizardID}/artifacts/{artifactID}")
    public Result<Void> assignArtifactToWizard(@PathVariable Integer wizardID, @PathVariable String artifactID) {
        this.wizardService.assignArtifactToWizard(wizardID, artifactID);
        return Result.<Void>builder()
                .flag(true)
                .code(StatusCode.SUCCESS)
                .message("Artifact Assignment Success")
                .data(null)
                .build();
    }
}
