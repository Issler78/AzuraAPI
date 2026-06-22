package br.com.issler.azura_api.dtos;

import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record UpdateCourseDTO(
        @Length(max = 255)
        String title,

        String description,

        Long categoryId
) {}
