package br.com.issler.azura_api.controllers;

import br.com.issler.azura_api.database.models.EnrollmentEntity;
import br.com.issler.azura_api.dtos.CreateUserDTO;
import br.com.issler.azura_api.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody CreateUserDTO createDTO) throws Exception {
        userService.save(createDTO);
    }

    @GetMapping("/{userId}/enrollments")
    @ResponseStatus(HttpStatus.OK)
    public Set<EnrollmentEntity> getEnrollments(@PathVariable UUID userId) throws Exception {
        return userService.getEnrollments(userId);
    }
}
