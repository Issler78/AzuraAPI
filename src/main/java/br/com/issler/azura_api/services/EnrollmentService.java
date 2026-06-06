package br.com.issler.azura_api.services;

import br.com.issler.azura_api.database.models.EnrollmentEntity;
import br.com.issler.azura_api.database.models.StudentEntity;
import br.com.issler.azura_api.database.repositories.IEnrollmentRepository;
import br.com.issler.azura_api.database.repositories.IStudentRepository;
import br.com.issler.azura_api.dtos.CreateEnrollmentDTO;
import br.com.issler.azura_api.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    private final IStudentRepository studentRepository;
    private final IEnrollmentRepository enrollmentRepository;

    public void save(CreateEnrollmentDTO createDTO) throws Exception {
        // checks if student exists
        StudentEntity student = studentRepository.findById(createDTO.getStudentId())
                .orElseThrow(() -> new NotFoundException("Student not found"));

        try {
            enrollmentRepository.save(EnrollmentEntity.builder()
                    .student(student)
                    .enrollmentDate(createDTO.getEnrollmentDate())
                    .build());
        } catch (Exception e) {
            throw new Exception("Error occured while saving enrollment on database");
        }
    }
}
