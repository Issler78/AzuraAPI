package br.com.issler.azura_api.dtos;

import lombok.Builder;

@Builder
public record CourseResponse(
        Long id,
        String title,
        String description,
        CategoryResponse category
) {}
