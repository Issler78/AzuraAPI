package br.com.issler.azura_api.services;

import br.com.issler.azura_api.clients.GatewayClient;
import br.com.issler.azura_api.database.models.PaymentEntity;
import br.com.issler.azura_api.database.repositories.IPaymentRepository;
import br.com.issler.azura_api.dtos.CreatePaymentDTO;
import br.com.issler.azura_api.dtos.PaymentResponse;
import br.com.issler.azura_api.enums.EnrollmentStatusTypeEnum;
import br.com.issler.azura_api.enums.PaymentStatusType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final IPaymentRepository paymentRepository;
    private final GatewayClient gatewayClient;

    @Transactional
    public PaymentResponse process(CreatePaymentDTO createDTO, String payerCpf) {
        // all operations in a single transaction, if the gateway fails, the payment will not be saved in the database (the same goes for if saved/updated failed)

        PaymentEntity payment = save(createDTO);

        PaymentResponse response = gatewayClient.send(payment, payerCpf);
        update(payment, response);

        return response;
    }

    private PaymentEntity save(CreatePaymentDTO dto){
        try {
            return paymentRepository.save(PaymentEntity.builder()
                    .status(PaymentStatusType.PENDING)
                    .amount(dto.amount())
                    .paymentMethod(dto.paymentMethod())
                    .enrollmentId(dto.enrollment())
                    .build());
        } catch (RuntimeException e) {
            throw new RuntimeException("Error occurred while saving payment on database: " + e);
        }
    }

    private void update(PaymentEntity payment, PaymentResponse response){
        payment.setGatewayPaymentId(response.gatewayPaymentId());
        payment.setStatus(response.status());

        if(response.status() == PaymentStatusType.ACCEPTED){
            payment.setPaidAt(response.paidAt());
            payment.getEnrollmentId().setStatus(EnrollmentStatusTypeEnum.ACTIVE);
        }

        paymentRepository.save(payment);
    }

}
