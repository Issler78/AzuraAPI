package br.com.issler.azura_api.services;

import br.com.issler.azura_api.clients.GatewayClient;
import br.com.issler.azura_api.database.models.*;
import br.com.issler.azura_api.database.repositories.IPaymentRepository;
import br.com.issler.azura_api.dtos.CreatePaymentDTO;
import br.com.issler.azura_api.dtos.PaymentResponse;
import br.com.issler.azura_api.enums.PaymentMethodType;
import br.com.issler.azura_api.enums.PaymentStatusType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PaymentServiceTest {
    @Mock
    private IPaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private GatewayClient gatewayClient;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should save payment successfully with credit card payment method")
    void saveCaseCreditCard() throws Exception {
        CategoryEntity category = CategoryEntity.builder()
                .id(1L)
                .name("Test Category")
                .build();
        CourseEntity course = CourseEntity.builder()
                .id(1L)
                .title("Test Course")
                .description("Test Course Description")
                .price(new java.math.BigDecimal("100.00"))
                .category(category)
                .build();
        UserEntity user = UserEntity.builder()
                .id(new UUID(0L, 1L))
                .name("Test User")
                .email("test@example.com")
                .password("password")
                .cpf("12345678900")
                .build();

        EnrollmentEntity enrollment = EnrollmentEntity.builder()
                .id(new UUID(0L, 1L))
                .enrollmentDate(LocalDate.now())
                .price(new BigDecimal("100.00"))
                .user(user)
                .course(course)
                .build();



        CreatePaymentDTO createPaymentDTO = CreatePaymentDTO.builder()
                .amount(new java.math.BigDecimal("100.00"))
                .paymentMethod(PaymentMethodType.CREDIT_CARD)
                .enrollment(enrollment)
                .build();

        PaymentEntity payment = PaymentEntity.builder()
                .id(new UUID(0L, 1L))
                .status(PaymentStatusType.PENDING)
                .amount(createPaymentDTO.amount())
                .paymentMethod(createPaymentDTO.paymentMethod())
                .enrollmentId(enrollment)
                .build();



        PaymentResponse gatewayResponse = PaymentResponse.builder()
                .gatewayPaymentId(new UUID(0L, 1L))
                .status(PaymentStatusType.ACCEPTED)
                .paymentMethod(PaymentMethodType.CREDIT_CARD)
                .paidAt(LocalDateTime.now())
                .build();



        when(paymentRepository.save(any(PaymentEntity.class))).thenReturn(payment);
        when(gatewayClient.send(any(PaymentEntity.class), eq(user.getCpf()))).thenReturn(gatewayResponse);

        PaymentResponse response = paymentService.process(
                createPaymentDTO,
                user.getCpf()
        );

        assertNotNull(response);
        assertEquals(PaymentStatusType.ACCEPTED, response.status());
        assertNotNull(response.paidAt());
        verify(gatewayClient, times(1)).send(any(PaymentEntity.class), eq(user.getCpf()));
        verify(paymentRepository, times(2)).save(any());

    }

    @Test
    @DisplayName("Should save payment successfully with pix payment method")
    void saveCasePix() throws Exception {
        CategoryEntity category = CategoryEntity.builder()
                .id(1L)
                .name("Test Category")
                .build();
        CourseEntity course = CourseEntity.builder()
                .id(1L)
                .title("Test Course")
                .description("Test Course Description")
                .price(new java.math.BigDecimal("100.00"))
                .category(category)
                .build();
        UserEntity user = UserEntity.builder()
                .id(new UUID(0L, 1L))
                .name("Test User")
                .email("test@example.com")
                .password("password")
                .cpf("12345678900")
                .build();

        EnrollmentEntity enrollment = EnrollmentEntity.builder()
                .id(new UUID(0L, 1L))
                .enrollmentDate(LocalDate.now())
                .price(new BigDecimal("100.00"))
                .user(user)
                .course(course)
                .build();



        CreatePaymentDTO createPaymentDTO = CreatePaymentDTO.builder()
                .amount(new BigDecimal("100.00"))
                .paymentMethod(PaymentMethodType.PIX)
                .enrollment(enrollment)
                .build();

        PaymentEntity payment = PaymentEntity.builder()
                .id(new UUID(0L, 1L))
                .status(PaymentStatusType.PENDING)
                .amount(createPaymentDTO.amount())
                .paymentMethod(createPaymentDTO.paymentMethod())
                .enrollmentId(enrollment)
                .build();



        PaymentResponse gatewayResponse = PaymentResponse.builder()
                .gatewayPaymentId(new UUID(0L, 1L))
                .status(PaymentStatusType.PENDING)
                .paymentMethod(PaymentMethodType.PIX)
                .build();



        when(paymentRepository.save(any(PaymentEntity.class))).thenReturn(payment);
        when(gatewayClient.send(any(PaymentEntity.class), eq(user.getCpf()))).thenReturn(gatewayResponse);

        PaymentResponse response = paymentService.process(
                createPaymentDTO,
                user.getCpf()
        );

        assertNotNull(response);
        assertEquals(PaymentStatusType.PENDING, response.status());
        assertNull(response.paidAt());
        verify(gatewayClient, times(1)).send(any(PaymentEntity.class), eq(user.getCpf()));
        verify(paymentRepository, times(2)).save(any());

    }
}