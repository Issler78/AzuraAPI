package br.com.issler.azura_api.dtos;

import lombok.Builder;

@Builder
public record CategoryResponse(
        Long id,
        String name
) {}
