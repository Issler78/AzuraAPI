package br.com.issler.azura_api.controllers;

import br.com.issler.azura_api.database.models.UserEntity;
import br.com.issler.azura_api.dtos.category.responses.CategoryResponse;
import br.com.issler.azura_api.dtos.course.responses.CourseResponse;
import br.com.issler.azura_api.dtos.enrollment.requests.CreateEnrollmentDTO;
import br.com.issler.azura_api.dtos.enrollment.responses.EnrollmentCreateResult;
import br.com.issler.azura_api.dtos.enrollment.responses.EnrollmentResponse;
import br.com.issler.azura_api.dtos.payment.responses.PaymentResponse;
import br.com.issler.azura_api.services.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public EnrollmentResponse save(
            @Valid @RequestBody CreateEnrollmentDTO createDTO,
            @AuthenticationPrincipal UserEntity user
    ) throws Exception {
        EnrollmentCreateResult result = enrollmentService.save(createDTO, user);

        return EnrollmentResponse.builder()
                .enrollmentDate(result.enrollment().getEnrollmentDate())
                .enrollmentStatus(result.enrollment().getStatus())
                .course(CourseResponse.builder()
                        .title(result.enrollment().getCourse().getTitle())
                        .price(result.enrollment().getCourse().getPrice())
                        .category(CategoryResponse.builder()
                                .name(result.enrollment().getCourse().getCategory().getName())
                                .build())
                        .build())
                .payment(PaymentResponse.builder()
                        .amount(result.paymentResponse().amount())
                        .status(result.paymentResponse().status())
                        .paymentMethod(result.paymentResponse().paymentMethod())
                        .qrCode(result.paymentResponse().qrCode())
                        .build())
                .build();
    }
}
