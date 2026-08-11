package br.com.issler.azura_api.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CourseResponse(
        Long id,
        String title,
        String description,
        BigDecimal price,
        CategoryResponse category
) {}
