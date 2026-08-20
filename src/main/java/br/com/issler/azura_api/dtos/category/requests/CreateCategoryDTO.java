package br.com.issler.azura_api.dtos.category.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;


@Builder
public record CreateCategoryDTO (
        @NotBlank
        String name
) {}
