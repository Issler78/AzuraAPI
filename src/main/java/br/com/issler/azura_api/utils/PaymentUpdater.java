package br.com.issler.azura_api.utils;

import br.com.issler.azura_api.clients.enums.GatewayErrorType;
import br.com.issler.azura_api.database.models.PaymentEntity;
import br.com.issler.azura_api.database.repositories.IPaymentRepository;
import br.com.issler.azura_api.dtos.payment.requests.CreatePaymentDTO;
import br.com.issler.azura_api.dtos.payment.responses.PaymentResponse;
import br.com.issler.azura_api.enums.EnrollmentStatusTypeEnum;
import br.com.issler.azura_api.enums.PaymentStatusType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class PaymentUpdater {
    private final IPaymentRepository paymentRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PaymentEntity savePending(CreatePaymentDTO dto){
        return paymentRepository.save(PaymentEntity.builder()
                .status(PaymentStatusType.PENDING)
                .amount(dto.amount())
                .paymentMethod(dto.paymentMethod())
                .enrollmentId(dto.enrollment())
                .build());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateByResponse(PaymentEntity payment, PaymentResponse response){
        payment.setGatewayPaymentId(response.gatewayPaymentId());
        payment.setStatus(response.status());

        if(response.status() == PaymentStatusType.ACCEPTED){
            payment.setPaidAt(response.paidAt());
            payment.getEnrollmentId().setStatus(EnrollmentStatusTypeEnum.ACTIVE);
        }

        paymentRepository.save(payment);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void setAsFailed(PaymentEntity payment, GatewayErrorType errorType){
        payment.setStatus(PaymentStatusType.FAILED);
        payment.setFailureReason(errorType.name());

        paymentRepository.save(payment);
    }

}
