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
    @NotNull(message = "studentId field cannot be null")
    private UUID studentId;

    @NotNull(message = "enrollmentDate field cannot be null")
    private LocalDate enrollmentDate;
}
