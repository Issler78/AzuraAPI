package br.com.issler.azura_api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record CreateCourseDTO (
        @NotBlank
        @Length(max = 255)
        String title,

        @NotBlank
        String description,

        @NotNull
        Long categoryId
) {}
