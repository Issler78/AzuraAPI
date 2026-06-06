package br.com.issler.azura_api.database.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "enrollments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnrollmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "enrollment_date", nullable = false)
    private Date enrollmentDate;

    @ManyToOne
    @JoinColumn(name = "student_id") // here exists a column referencing the student
    private StudentEntity student;
}
