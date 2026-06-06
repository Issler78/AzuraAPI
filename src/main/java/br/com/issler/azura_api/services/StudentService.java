package br.com.issler.azura_api.services;

import br.com.issler.azura_api.database.models.StudentEntity;
import br.com.issler.azura_api.database.repositories.IStudentRepository;
import br.com.issler.azura_api.dtos.CreateStudentDTO;
import br.com.issler.azura_api.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class StudentService {
    private final IStudentRepository studentRepository;

    public void save(CreateStudentDTO createDTO) throws Exception {
        // checks if there is already a student with the same email or cpf on database
        StudentEntity student = studentRepository.findByEmailOrCpf(createDTO.getEmail(), createDTO.getCpf()).orElse(null);

        if (student != null){
            throw new BadRequestException("CPF or E-mail already exists");
        }

        try {
            studentRepository.save(StudentEntity.builder()
                    .name(createDTO.getName())
                    .email(createDTO.getEmail())
                    .password(createDTO.getPassword())
                    .cpf(createDTO.getCpf())
                    .build());
        } catch (Exception e) {
            throw new Exception("Error occurred while saving student on database");
        }
    }
}
