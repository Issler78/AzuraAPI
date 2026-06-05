package br.com.issler.azura_api.services;

import br.com.issler.azura_api.database.models.StudentEntity;
import br.com.issler.azura_api.database.repositories.IStudentRepository;
import br.com.issler.azura_api.dtos.CreateStudentDTO;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class StudentService {
    private final IStudentRepository studentRepository;

    public void save(CreateStudentDTO createDTO) throws BadRequestException {
        StudentEntity student = studentRepository.findByEmailOrCpf(createDTO.getEmail(), createDTO.getCpf()).orElse(null);

        if (student != null){
            throw new BadRequestException("CPF or e-mail already exists");
        }

        studentRepository.save(StudentEntity.builder()
                        .name(createDTO.getName())
                        .email(createDTO.getEmail())
                        .password(createDTO.getPassword())
                        .cpf(createDTO.getCpf())
                        .build());
    }
}
