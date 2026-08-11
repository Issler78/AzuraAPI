package br.com.issler.azura_api.dtos;

import br.com.issler.azura_api.enums.EnrollmentStatusTypeEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record EnrollmentResponse(
        UUID id,
        LocalDate enrollmentDate,
        LocalDate completionDate,
        int completionPercentage,
        boolean certificateIssued,
        EnrollmentStatusTypeEnum enrollmentStatus,
        CourseResponse course,
        PaymentResponse payment
) {}
