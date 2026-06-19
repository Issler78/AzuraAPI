package br.com.issler.azura_api.services;

import br.com.issler.azura_api.database.models.EnrollmentEntity;
import br.com.issler.azura_api.database.models.UserEntity;
import br.com.issler.azura_api.database.repositories.IUserRepository;
import br.com.issler.azura_api.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserRepository userRepository;

    public Set<EnrollmentEntity> getEnrollments(UUID userId) throws Exception {
        UserEntity user = userRepository.findById(userId).
                orElseThrow(() -> new NotFoundException("User not found"));

        return user.getEnrollments();
    }
}
