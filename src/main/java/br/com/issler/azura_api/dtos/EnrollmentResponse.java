package br.com.issler.azura_api.dtos;

import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record EnrollmentResponse(
        UUID id,
        LocalDate enrollmentDate,
        CourseResponse course
) {}
