package br.com.issler.azura_api.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LoginDTO(
        @NotBlank
        String email,

        @NotBlank
        String password
) {}
