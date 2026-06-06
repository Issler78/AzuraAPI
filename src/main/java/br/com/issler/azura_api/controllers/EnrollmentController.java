package br.com.issler.azura_api.controllers;

import br.com.issler.azura_api.dtos.CreateEnrollmentDTO;
import br.com.issler.azura_api.exceptions.NotFoundException;
import br.com.issler.azura_api.services.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/enrollments")
@RequiredArgsConstructor
@Validated
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody CreateEnrollmentDTO createDTO) throws Exception {
        enrollmentService.save(createDTO);
    }
}
