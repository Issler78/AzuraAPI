package br.com.issler.azura_api.controllers;

import br.com.issler.azura_api.dtos.CreateStudentDTO;
import br.com.issler.azura_api.services.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/students")
@RequiredArgsConstructor
@Validated
public class StudentController {
    private final StudentService studentService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody CreateStudentDTO createDTO) throws BadRequestException {
        studentService.save(createDTO);
    }
}
