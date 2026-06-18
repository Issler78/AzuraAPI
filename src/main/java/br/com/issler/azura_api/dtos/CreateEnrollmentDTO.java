package br.com.issler.azura_api.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateEnrollmentDTO {
    @NotNull()
    private UUID userId;

    @NotNull()
    private LocalDate enrollmentDate;

    @NotNull
    private Long courseId;
}
