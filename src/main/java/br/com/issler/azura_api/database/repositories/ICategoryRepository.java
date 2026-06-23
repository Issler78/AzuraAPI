package br.com.issler.azura_api.database.repositories;

import br.com.issler.azura_api.database.models.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ICategoryRepository extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findByName(@Param("name") String name);

    // exists a category with the new name and not is the same category
    boolean existsByNameAndIdNot(String name, Long id);

    List<CategoryEntity> findAllByDeletedAtIsNull();

    boolean existsByIdAndDeletedAtIsNull(Long id);
}
