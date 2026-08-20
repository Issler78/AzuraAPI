package br.com.issler.azura_api.services;

import br.com.issler.azura_api.database.models.CategoryEntity;
import br.com.issler.azura_api.database.models.CourseEntity;
import br.com.issler.azura_api.database.models.EnrollmentEntity;
import br.com.issler.azura_api.database.models.UserEntity;
import br.com.issler.azura_api.database.repositories.IEnrollmentRepository;
import br.com.issler.azura_api.dtos.enrollment.requests.CreateEnrollmentDTO;
import br.com.issler.azura_api.dtos.payment.requests.CreatePaymentDTO;
import br.com.issler.azura_api.enums.PaymentMethodType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class EnrollmentServiceTest {

    @Mock
    private IEnrollmentRepository enrollmentRepository;

    @Mock
    private CourseService courseService;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private EnrollmentService enrollmentService;

    @BeforeEach
    void setup(){
        openMocks(this);
    }

    @Test
    @DisplayName("Should save enrollment and payment successfully")
    void saveCaseSuccess() throws Exception {
        CategoryEntity category = CategoryEntity.builder()
                .id(1L)
                .name("Test Category")
                .build();

        CourseEntity course = CourseEntity.builder()
                .id(1L)
                .title("Test Course")
                .description("Test Description")
                .price(new BigDecimal("100.00"))
                .category(category)
                .build();

        UserEntity user = UserEntity.builder()
                .id(new UUID(1L, 2L))
                .name("Test User")
                .email("test@example.com")
                .password("testpassword")
                .cpf("123.456.789-00")
                .build();

        CreateEnrollmentDTO dto = CreateEnrollmentDTO.builder()
                .courseId(1L)
                .paymentMethod(PaymentMethodType.CREDIT_CARD)
                .build();



        EnrollmentEntity enrollment = EnrollmentEntity.builder()
                .id(new UUID(1L, 2L))
                .enrollmentDate(LocalDate.now())
                .price(course.getPrice())
                .user(user)
                .course(course)
                .build();



        CreatePaymentDTO createPaymentDTO = CreatePaymentDTO.builder()
                .amount(enrollment.getPrice())
                .paymentMethod(PaymentMethodType.CREDIT_CARD)
                .enrollment(enrollment)
                .build();



        when(courseService.getById(1L)).thenReturn(course);
        when(enrollmentRepository.save(enrollment)).thenReturn(enrollment);
        when(paymentService.process(createPaymentDTO, user.getCpf())).thenReturn(null);

        enrollmentService.save(dto, user);

        verify(courseService, times(1)).getById(1L);
        verify(enrollmentRepository, times(1)).save(any(EnrollmentEntity.class));
        verify(paymentService, times(1)).process(any(CreatePaymentDTO.class), anyString());
    }
}