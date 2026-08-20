package br.com.issler.azura_api.dtos.auth.responses;

import lombok.Builder;

@Builder
public record TokenResponse(String token, long expires_in) {
}
