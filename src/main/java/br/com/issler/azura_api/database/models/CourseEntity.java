package br.com.issler.azura_api.database.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CourseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @ManyToOne()
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity categoryId;

    @OneToMany(mappedBy = "course")
    @JsonBackReference // temp
    private Set<EnrollmentEntity> enrollments = new HashSet<>();
}
