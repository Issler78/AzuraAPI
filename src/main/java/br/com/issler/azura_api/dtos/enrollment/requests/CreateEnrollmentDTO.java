package br.com.issler.azura_api.dtos.enrollment.requests;

import br.com.issler.azura_api.enums.PaymentMethodType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateEnrollmentDTO (
        @NotNull(message = "shouldn't be null")
        Long courseId,

        @NotNull(message = "shouldn't be null")
        PaymentMethodType paymentMethod
) {}
