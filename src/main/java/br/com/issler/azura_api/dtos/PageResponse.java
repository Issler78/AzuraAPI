package br.com.issler.azura_api.dtos;

import lombok.Builder;

import java.util.List;

@Builder
public record PageResponse<T>(
    List<T> content,
    int page,
    int size,
    int elementsInPage,
    int totalPages,
    long totalElements,
    boolean firstPage,
    boolean lastPage,
    boolean hasPreviousPage,
    boolean hasNextPage
) {}
