package br.com.issler.azura_api.controllers;

import br.com.issler.azura_api.database.models.EnrollmentEntity;
import br.com.issler.azura_api.dtos.CategoryResponse;
import br.com.issler.azura_api.dtos.CourseResponse;
import br.com.issler.azura_api.dtos.CreateUserDTO;
import br.com.issler.azura_api.dtos.EnrollmentResponse;
import br.com.issler.azura_api.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public Set<EnrollmentResponse> getEnrollments(@PathVariable UUID userId) throws Exception {
        Set<EnrollmentEntity> enrollments = userService.getEnrollments(userId);

        return enrollments.stream().map(
                enrollment -> EnrollmentResponse.builder()
                        .id(enrollment.getId())
                        .enrollmentDate(enrollment.getEnrollmentDate())
                        .course(CourseResponse.builder()
                                .id(enrollment.getCourse().getId())
                                .title(enrollment.getCourse().getTitle())
                                .description(enrollment.getCourse().getDescription())
                                .category(CategoryResponse.builder()
                                        .id(enrollment.getCourse().getCategory().getId())
                                        .name(enrollment.getCourse().getCategory().getName())
                                        .build())
                                .build())
                        .build()
        ).collect(Collectors.toSet());
    }
}
