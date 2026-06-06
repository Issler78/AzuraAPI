package br.com.issler.azura_api.database.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "students")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, columnDefinition = "CHAR(11)")
    private String cpf;

    @OneToMany(mappedBy = "student") // here not exists a column, but the "student" field in EnrollmentEntity is a reference to this entity
    private Set<EnrollmentEntity> enrollments = new HashSet<>();
}
