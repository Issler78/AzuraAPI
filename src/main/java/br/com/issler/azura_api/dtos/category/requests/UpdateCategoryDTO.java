package br.com.issler.azura_api.dtos.category.requests;

import lombok.Builder;

@Builder
public record UpdateCategoryDTO(
        String name
) {}
