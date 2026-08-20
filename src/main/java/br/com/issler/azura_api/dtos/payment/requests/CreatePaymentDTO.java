package br.com.issler.azura_api.dtos.payment.requests;

import br.com.issler.azura_api.database.models.EnrollmentEntity;
import br.com.issler.azura_api.enums.PaymentMethodType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CreatePaymentDTO(
        @NotNull
        BigDecimal amount,

        @NotNull
        PaymentMethodType paymentMethod,

        @NotNull
        EnrollmentEntity enrollment
) {}
