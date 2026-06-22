package br.com.issler.azura_api.dtos;

import lombok.Builder;

@Builder
public record UpdateCategoryDTO(
        String name
) {}
