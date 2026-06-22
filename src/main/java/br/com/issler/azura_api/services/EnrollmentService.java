package br.com.issler.azura_api.services;

import br.com.issler.azura_api.database.models.CourseEntity;
import br.com.issler.azura_api.database.models.EnrollmentEntity;
import br.com.issler.azura_api.database.models.UserEntity;
import br.com.issler.azura_api.database.repositories.ICourseRepository;
import br.com.issler.azura_api.database.repositories.IEnrollmentRepository;
import br.com.issler.azura_api.database.repositories.IUserRepository;
import br.com.issler.azura_api.dtos.CreateEnrollmentDTO;
import br.com.issler.azura_api.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    private final IEnrollmentRepository enrollmentRepository;
    private  final ICourseRepository courseRepository;

    public void save(CreateEnrollmentDTO createDTO, UserEntity user) throws Exception {
        CourseEntity course = courseRepository.findById(createDTO.courseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));

        try {
            enrollmentRepository.save(EnrollmentEntity.builder()
                    .user(user)
                    .enrollmentDate(LocalDate.now())
                    .course(course)
                    .build());
        } catch (Exception e) {
            throw new Exception("Error occured while saving enrollment on database");
        }
    }
}
