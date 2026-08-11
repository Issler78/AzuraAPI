package br.com.issler.azura_api.dtos;

import br.com.issler.azura_api.enums.PaymentMethodType;
import br.com.issler.azura_api.enums.PaymentStatusType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PaymentResponse(
        UUID gatewayPaymentId,
        BigDecimal amount,
        PaymentStatusType status,
        PaymentMethodType paymentMethod,
        LocalDateTime paidAt,
        String qrCode
) {}
