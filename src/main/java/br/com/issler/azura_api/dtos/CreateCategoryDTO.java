package br.com.issler.azura_api.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CreateCategoryDTO {
    @NotBlank
    private String name;
}
