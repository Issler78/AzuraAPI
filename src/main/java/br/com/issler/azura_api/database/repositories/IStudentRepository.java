package br.com.issler.azura_api.database.repositories;

import br.com.issler.azura_api.database.models.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IStudentRepository extends JpaRepository<StudentEntity, UUID> {
}
