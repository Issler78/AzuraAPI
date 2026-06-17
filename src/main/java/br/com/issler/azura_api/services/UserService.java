package br.com.issler.azura_api.services;

import br.com.issler.azura_api.database.models.UserEntity;
import br.com.issler.azura_api.database.repositories.IUserRepository;
import br.com.issler.azura_api.dtos.CreateUserDTO;
import br.com.issler.azura_api.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserRepository userRepository;

    public void save(CreateUserDTO createDTO) throws Exception {
        // checks if there is already a user with the same email or cpf on database
        UserEntity user = userRepository.findByEmailOrCpf(createDTO.getEmail(), createDTO.getCpf()).orElse(null);

        if (user != null){
            throw new BadRequestException("CPF or E-mail already exists");
        }

        try {
            userRepository.save(UserEntity.builder()
                    .name(createDTO.getName())
                    .email(createDTO.getEmail())
                    .password(createDTO.getPassword())
                    .cpf(createDTO.getCpf())
                    .build());
        } catch (Exception e) {
            throw new Exception("Error occurred while saving user on database");
        }
    }
}
