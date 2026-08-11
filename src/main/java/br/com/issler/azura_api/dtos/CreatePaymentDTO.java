package br.com.issler.azura_api.dtos;

import br.com.issler.azura_api.database.models.EnrollmentEntity;
import br.com.issler.azura_api.enums.PaymentMethodType;
import br.com.issler.azura_api.enums.PaymentStatusType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record CreatePaymentDTO(
        @NotNull
        BigDecimal amount,

        @NotNull
        PaymentMethodType paymentMethod,

        @NotNull
        EnrollmentEntity enrollment
) {}
