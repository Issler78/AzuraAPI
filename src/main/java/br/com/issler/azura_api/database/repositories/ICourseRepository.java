package br.com.issler.azura_api.database.repositories;

import br.com.issler.azura_api.database.models.CourseEntity;
import br.com.issler.azura_api.projections.ICoursesProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ICourseRepository extends JpaRepository<CourseEntity, Long> {
    Optional<CourseEntity> findByTitle(String title);

    @NativeQuery(value = """
        SELECT
            c.id courseId,
            c.title courseTitle,
            cat.id categoryId,
            cat.name category
        FROM courses c
        INNER JOIN categories cat
        ON c.category_id = cat.id
        WHERE (:search IS NULL OR :search = '' OR LOWER(c.title) LIKE LOWER(CONCAT('%', :search, '%')))
        AND c.deleted_at IS NULL
        ORDER BY c.title
    """,
    countQuery = """
        SELECT
            COUNT(*)
        FROM courses c
    """)
    Page<ICoursesProjection> getAllPaginate(Pageable pageable, @Param("search") String search);
}
