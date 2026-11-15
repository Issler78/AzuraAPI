package br.com.issler.azura_api.services;

import br.com.issler.azura_api.clients.GatewayClient;
import br.com.issler.azura_api.clients.exceptions.GatewayServiceException;
import br.com.issler.azura_api.database.models.PaymentEntity;
import br.com.issler.azura_api.database.repositories.IPaymentRepository;
import br.com.issler.azura_api.dtos.payment.requests.CreatePaymentDTO;
import br.com.issler.azura_api.dtos.payment.responses.PaymentResponse;
import br.com.issler.azura_api.dtos.webhook.requests.WebhookRequest;
import br.com.issler.azura_api.enums.EnrollmentStatusTypeEnum;
import br.com.issler.azura_api.enums.PaymentStatusType;
import br.com.issler.azura_api.exceptions.NotFoundException;
import br.com.issler.azura_api.security.IdempotencyKeyProvider;
import br.com.issler.azura_api.utils.PaymentUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final IPaymentRepository paymentRepository;
    private final GatewayClient gatewayClient;
    private final PaymentUpdater paymentUpdater;
    private final IdempotencyKeyProvider idempotencyKeyProvider;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PaymentEntity process(CreatePaymentDTO createDTO) {
        // payment is saved and commited as PENDING

        UUID idempotencyKey = idempotencyKeyProvider.generate();
        return paymentUpdater.savePending(createDTO, idempotencyKey);
    }

    public PaymentResponse sendToGatewayClient(PaymentEntity payment, String payerCpf){
        // payment has been commited, can be sent to gateway client, if fails, the payment will be set as FAILED and the exception will be thrown
        try {
            PaymentResponse response = gatewayClient.send(payment, payerCpf, payment.getIdempotencyKey());
            paymentUpdater.updateByResponse(payment, response);

            return response;
        } catch (GatewayServiceException e){
            paymentUpdater.setAsFailed(payment, e.getType());
            throw e;
        }
    }

    @Transactional
    public void setAsPayed(WebhookRequest webhookRequest) throws NotFoundException {
        PaymentEntity payment = paymentRepository.findByGatewayPaymentId(webhookRequest.gatewayPaymentId())
                .orElseThrow(() -> new NotFoundException("Payment not found"));

        if (webhookRequest.status() == PaymentStatusType.ACCEPTED) {
            payment.setStatus(PaymentStatusType.ACCEPTED);
            payment.setPaidAt(webhookRequest.paidAt());

            payment.getEnrollmentId().setStatus(EnrollmentStatusTypeEnum.ACTIVE);
        }
    }
}
