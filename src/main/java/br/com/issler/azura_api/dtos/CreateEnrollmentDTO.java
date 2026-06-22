package br.com.issler.azura_api.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateEnrollmentDTO (
        @NotNull
        Long courseId
) {}
