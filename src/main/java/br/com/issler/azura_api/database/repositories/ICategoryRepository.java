package br.com.issler.azura_api.database.repositories;

import br.com.issler.azura_api.database.models.CategoryEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ICategoryRepository extends CrudRepository<CategoryEntity, Long> {
    @Query("SELECT c FROM CategoryEntity c WHERE c.name = :name")
    Optional<CategoryEntity> findByName(@Param("name") String name);
}
