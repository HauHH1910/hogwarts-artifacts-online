package com.hauhh.controller;

import com.hauhh.dto.ArtifactDTO;
import com.hauhh.service.ArtifactService;
import com.hauhh.converter.artifact.ArtifactDTOToArtifactConverter;
import com.hauhh.converter.artifact.ArtifactToArtifactDTOConverter;
import com.hauhh.common.Result;
import com.hauhh.common.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.endpoint.url}/artifacts")
public class ArtifactController {

    private final ArtifactService artifactService;
    private final ArtifactToArtifactDTOConverter entityTOdto;
    private final ArtifactDTOToArtifactConverter dtoToEntity;

    @GetMapping("/{artifactID}")
    public Result<ArtifactDTO> findArtifactByID(@PathVariable String artifactID) {
        return Result.<ArtifactDTO>builder()
                .flag(true)
                .code(StatusCode.SUCCESS)
                .message("Find One Success")
                .data(this.entityTOdto.convert(
                        this.artifactService.findById(artifactID)
                ))
                .build();
    }

    @GetMapping
    public Result<List<ArtifactDTO>> findAllArtifacts() {
        return Result.<List<ArtifactDTO>>builder()
                .flag(true)
                .code(StatusCode.SUCCESS)
                .message("Find All Success")
                .data(this.artifactService.findAll().stream()
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
                                Objects.requireNonNull(
                                        dtoToEntity.convert(
                                                artifactDTO
                                        )
                                )
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
                                artifactID, this.dtoToEntity.convert(
                                        artifactDTO
                                )
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
