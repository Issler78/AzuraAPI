package br.com.issler.azura_api.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;


@Builder
public record CreateCategoryDTO (
        @NotBlank
        String name
) {}
