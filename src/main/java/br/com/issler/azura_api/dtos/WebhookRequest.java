package br.com.issler.azura_api.dtos;

import br.com.issler.azura_api.enums.PaymentStatusType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record WebhookRequest(
        @NotNull(message = "shouldn't be null")
        UUID gatewayPaymentId,

        @NotNull(message = "shouldn't be null")
        LocalDateTime paidAt, // on a real project, paidAt can be null (because, sometimes payment not is accepted). For study purposes, it always has a value

        @NotNull(message = "shouldn't be null")
        PaymentStatusType status
) {}
