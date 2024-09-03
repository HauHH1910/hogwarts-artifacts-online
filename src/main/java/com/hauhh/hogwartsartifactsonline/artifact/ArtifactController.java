package com.hauhh.hogwartsartifactsonline.artifact;

import com.hauhh.hogwartsartifactsonline.artifact.conveter.ArtifactDTOTOArtifactConverter;
import com.hauhh.hogwartsartifactsonline.artifact.conveter.ArtifactToArtifactDTOConverter;
import com.hauhh.hogwartsartifactsonline.system.Result;
import com.hauhh.hogwartsartifactsonline.system.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/artifacts")
public class ArtifactController {

    private final ArtifactService artifactService;
    private final ArtifactToArtifactDTOConverter entityTOdto;
    private final ArtifactDTOTOArtifactConverter dtoToEntity;

    @GetMapping("/{artifactID}")
    public Result<ArtifactDTO> findArtifactByID(@PathVariable String artifactID) {
        return Result.<ArtifactDTO>builder()
                .flag(true)
                .code(StatusCode.SUCCESS)
                .message("Find One Success")
                .data(entityTOdto.convert(artifactService.findById(artifactID)))
                .build();
    }

    @GetMapping
    public Result<List<ArtifactDTO>> findAllArtifacts() {
        return Result.<List<ArtifactDTO>>builder()
                .flag(true)
                .code(StatusCode.SUCCESS)
                .message("Find All Success")
                .data(artifactService.findAll().stream()
                        .map(entityTOdto::convert).toList())
                .build();
    }

    @PostMapping
    public Result<ArtifactDTO> addArtifact(@Valid @RequestBody ArtifactDTO artifactDTO) {
        return Result.<ArtifactDTO>builder()
                .flag(true)
                .code(StatusCode.SUCCESS)
                .message("Add Success")
                .data(this.entityTOdto.convert(
                        this.artifactService.addArtifact(
                                Objects.requireNonNull(dtoToEntity.convert(artifactDTO))
                        )))
                .build();
    }

    @PutMapping("/{artifactID}")
    public Result<ArtifactDTO> updateArtifact(@PathVariable String artifactID, @Valid @RequestBody ArtifactDTO artifactDTO) {
        return Result.<ArtifactDTO>builder()
                .flag(true)
                .code(StatusCode.SUCCESS)
                .message("Update Success")
                .data(this.entityTOdto.convert(
                        this.artifactService.updateArtifact(
                                artifactID, this.dtoToEntity.convert(artifactDTO)
                        )))
                .build();
    }

    @DeleteMapping("/{artifactID}")
    public Result<Void> deleteArtifact(@PathVariable String artifactID) {
        this.artifactService.deleteArtifact(artifactID);
        return Result.<Void>builder()
                .flag(true)
                .code(StatusCode.SUCCESS)
                .message("Delete Success")
                .build();
    }

}
