package br.com.issler.azura_api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCourseDTO {
    @NotBlank
    @Length(max = 255)
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private Long categoryId;
}
