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
import br.com.issler.azura_api.utils.PaymentUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final IPaymentRepository paymentRepository;
    private final GatewayClient gatewayClient;
    private final PaymentUpdater paymentUpdater;

    public PaymentResponse process(CreatePaymentDTO createDTO, String payerCpf) {
        // all transactions are created and commited (REQUIRES_NEW), so if the gateway fails, the payment is still saved in the database with status PENDING/FAILED, with the failure reason.

        PaymentEntity payment = paymentUpdater.savePending(createDTO);

        try {
            PaymentResponse response = gatewayClient.send(payment, payerCpf);
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
