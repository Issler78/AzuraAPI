package br.com.issler.azura_api.dtos;

import br.com.issler.azura_api.enums.PaymentMethodType;
import br.com.issler.azura_api.enums.PaymentStatusType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record GatewayPaymentResponse(
        @NotNull
        UUID gatewayPaymentId,

        @NotNull
        BigDecimal amount,

        @NotNull
        PaymentStatusType status,

        @NotNull
        PaymentMethodType paymentMethod,

        LocalDateTime paidAt,

        String qrCode
) {}
