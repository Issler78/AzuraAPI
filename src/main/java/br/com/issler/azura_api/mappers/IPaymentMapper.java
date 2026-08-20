package br.com.issler.azura_api.mappers;

import br.com.issler.azura_api.clients.dtos.responses.GatewayPaymentResponse;
import br.com.issler.azura_api.dtos.payment.responses.PaymentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface IPaymentMapper {
    @Mapping(target = "gatewayPaymentId", source = "gatewayPaymentId")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "paymentMethod", source = "paymentMethod")
    @Mapping(target = "paidAt", source = "paidAt")
    @Mapping(target = "qrCode", source = "qrCode")
    PaymentResponse toPaymentResponse(GatewayPaymentResponse gatewayPaymentResponse);
}
