package br.com.issler.azura_api.controllers;

import br.com.issler.azura_api.database.models.EnrollmentEntity;
import br.com.issler.azura_api.database.models.UserEntity;
import br.com.issler.azura_api.dtos.CategoryResponse;
import br.com.issler.azura_api.dtos.CourseResponse;
import br.com.issler.azura_api.dtos.EnrollmentResponse;
import br.com.issler.azura_api.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/{userId}/enrollments")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Set<EnrollmentResponse> getEnrollmentsByUserId(@PathVariable UUID userId) throws Exception {
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

    @GetMapping("/me/enrollments")
    @PreAuthorize("hasRole('STUDENT')")
    @ResponseStatus(HttpStatus.OK)
    public Set<EnrollmentResponse> getMyEnrollments(@AuthenticationPrincipal UserEntity user) throws Exception {
        Set<EnrollmentEntity> enrollments = userService.getEnrollments(user.getId());

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
