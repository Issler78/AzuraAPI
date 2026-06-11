package br.com.issler.azura_api.database.repositories;

import br.com.issler.azura_api.database.models.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICourseRepository extends JpaRepository<CourseEntity, Long> {
    Optional<CourseEntity> findByTitle(String title);
}
