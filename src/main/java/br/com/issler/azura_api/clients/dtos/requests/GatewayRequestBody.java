package br.com.issler.azura_api.clients.dtos.requests;

import br.com.issler.azura_api.enums.PaymentMethodType;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record GatewayRequestBody(
        BigDecimal amount,
        String payerCpf,
        PaymentMethodType paymentMethod
) {}
