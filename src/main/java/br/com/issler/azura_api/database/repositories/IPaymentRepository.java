package br.com.issler.azura_api.database.repositories;

import br.com.issler.azura_api.database.models.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IPaymentRepository extends JpaRepository<PaymentEntity, UUID> {
    public Optional<PaymentEntity> findByGatewayPaymentId(UUID gatewayPaymentId);
}
