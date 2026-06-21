package br.com.issler.azura_api.dtos;

import lombok.Builder;

@Builder
public record TokenResponseDTO(String token, long expires_in) {
}
