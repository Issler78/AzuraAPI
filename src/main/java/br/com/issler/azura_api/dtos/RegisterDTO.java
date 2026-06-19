package br.com.issler.azura_api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
public record RegisterDTO(
        @NotBlank
        @Size(max = 150)
        String name,

        @NotBlank
        @Size(max = 255)
        String email,

        @NotBlank
        @Size(max = 255)
        String password,

        @NotBlank
        @Size(min = 11, max = 11)
        String cpf
) {}
