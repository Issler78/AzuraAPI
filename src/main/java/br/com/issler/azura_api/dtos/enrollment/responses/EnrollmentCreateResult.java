package br.com.issler.azura_api.dtos.enrollment.responses;

import br.com.issler.azura_api.database.models.EnrollmentEntity;
import br.com.issler.azura_api.dtos.payment.responses.PaymentResponse;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record EnrollmentCreateResult(
        @NotNull
        EnrollmentEntity enrollment,

        @NotNull
        PaymentResponse paymentResponse
) {}
