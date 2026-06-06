package br.com.issler.azura_api.database.repositories;

import br.com.issler.azura_api.database.models.EnrollmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IEnrollmentRepository extends JpaRepository<EnrollmentEntity, UUID> {
}
