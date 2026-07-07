package br.com.issler.azura_api.dtos;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CourseResponse(
        Long id,
        String title,
        String description,
        BigDecimal price,
        CategoryResponse category
) {}
