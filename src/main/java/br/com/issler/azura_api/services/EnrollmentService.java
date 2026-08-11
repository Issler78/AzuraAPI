package br.com.issler.azura_api.services;

import br.com.issler.azura_api.database.models.CourseEntity;
import br.com.issler.azura_api.database.models.EnrollmentEntity;
import br.com.issler.azura_api.database.models.UserEntity;
import br.com.issler.azura_api.database.repositories.IEnrollmentRepository;
import br.com.issler.azura_api.dtos.CreateEnrollmentDTO;
import br.com.issler.azura_api.dtos.CreatePaymentDTO;
import br.com.issler.azura_api.dtos.EnrollmentCreateResult;
import br.com.issler.azura_api.dtos.PaymentResponse;
import br.com.issler.azura_api.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    private final IEnrollmentRepository enrollmentRepository;
    private final CourseService courseService;
    private final PaymentService paymentService;

    public EnrollmentCreateResult save(CreateEnrollmentDTO createDTO, UserEntity user) throws Exception {
        CourseEntity course = courseService.getById(createDTO.courseId());

        EnrollmentEntity enrollment;
        try {
            enrollment = enrollmentRepository.save(EnrollmentEntity.builder()
                    .user(user)
                    .enrollmentDate(LocalDate.now())
                    .course(course)
                    .price(course.getPrice())
                    .build());

        } catch (Exception e) {
            throw new Exception("Error occurred while saving enrollment on database: " + e);
        }

        CreatePaymentDTO createPaymentDTO = CreatePaymentDTO.builder()
                .amount(course.getPrice())
                .paymentMethod(createDTO.paymentMethod())
                .enrollment(enrollment)
                .build();

        PaymentResponse paymentResponse = paymentService.process(createPaymentDTO, user.getCpf());


        // return the enrollment and payment response together to be has possible create the response on controller
        return EnrollmentCreateResult.builder()
                .enrollment(enrollment)
                .paymentResponse(paymentResponse)
                .build();

    }
}
