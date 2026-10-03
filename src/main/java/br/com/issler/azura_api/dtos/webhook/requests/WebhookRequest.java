package br.com.issler.azura_api.dtos.webhook.requests;

import br.com.issler.azura_api.enums.PaymentStatusType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record WebhookRequest(
        @NotNull(message = "shouldn't be null")
        UUID gatewayPaymentId,

        LocalDateTime paidAt,

        @NotNull(message = "shouldn't be null")
        PaymentStatusType status
) {}
