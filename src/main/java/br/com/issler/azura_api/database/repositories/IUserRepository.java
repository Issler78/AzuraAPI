package br.com.issler.azura_api.database.repositories;

import br.com.issler.azura_api.database.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IUserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmailOrCpf(String email, String cpf);
}
