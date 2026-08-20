package br.com.issler.azura_api.dtos.category.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CategoryResponse(
        Long id,
        String name
) {}
