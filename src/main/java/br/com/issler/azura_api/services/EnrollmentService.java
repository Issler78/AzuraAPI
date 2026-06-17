package br.com.issler.azura_api.services;

import br.com.issler.azura_api.database.models.EnrollmentEntity;
import br.com.issler.azura_api.database.models.UserEntity;
import br.com.issler.azura_api.database.repositories.IEnrollmentRepository;
import br.com.issler.azura_api.database.repositories.IUserRepository;
import br.com.issler.azura_api.dtos.CreateEnrollmentDTO;
import br.com.issler.azura_api.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    private final IUserRepository userRepository;
    private final IEnrollmentRepository enrollmentRepository;

    public void save(CreateEnrollmentDTO createDTO) throws Exception {
        UserEntity user = userRepository.findById(createDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        try {
            enrollmentRepository.save(EnrollmentEntity.builder()
                    .user(user)
                    .enrollmentDate(createDTO.getEnrollmentDate())
                    .build());
        } catch (Exception e) {
            throw new Exception("Error occured while saving enrollment on database");
        }
    }
}
