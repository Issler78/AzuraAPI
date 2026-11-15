package br.com.issler.azura_api.services;

import br.com.issler.azura_api.clients.GatewayClient;
import br.com.issler.azura_api.database.models.*;
import br.com.issler.azura_api.database.repositories.IPaymentRepository;
import br.com.issler.azura_api.dtos.payment.requests.CreatePaymentDTO;
import br.com.issler.azura_api.dtos.payment.responses.PaymentResponse;
import br.com.issler.azura_api.enums.PaymentMethodType;
import br.com.issler.azura_api.enums.PaymentStatusType;
import br.com.issler.azura_api.security.IdempotencyKeyProvider;
import br.com.issler.azura_api.utils.PaymentUpdater;
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
    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private GatewayClient gatewayClient;

    @Mock
    private PaymentUpdater paymentUpdater;

    @Mock
    private IdempotencyKeyProvider idempotencyKeyProvider;

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
                .id(UUID.randomUUID())
                .name("Test User")
                .email("test@example.com")
                .password("password")
                .cpf("12345678900")
                .build();

        EnrollmentEntity enrollment = EnrollmentEntity.builder()
                .id(UUID.randomUUID())
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
                .id(UUID.randomUUID())
                .status(PaymentStatusType.PENDING)
                .amount(createPaymentDTO.amount())
                .paymentMethod(createPaymentDTO.paymentMethod())
                .enrollmentId(enrollment)
                .idempotencyKey(UUID.randomUUID())
                .build();



        PaymentResponse gatewayResponse = PaymentResponse.builder()
                .gatewayPaymentId(UUID.randomUUID())
                .status(PaymentStatusType.ACCEPTED)
                .paymentMethod(PaymentMethodType.CREDIT_CARD)
                .paidAt(LocalDateTime.now())
                .build();



        when(idempotencyKeyProvider.generate()).thenReturn(UUID.randomUUID());
        when(paymentUpdater.savePending(any(CreatePaymentDTO.class), any(UUID.class))).thenReturn(payment);
        when(gatewayClient.send(any(PaymentEntity.class), eq(user.getCpf()), any(UUID.class))).thenReturn(gatewayResponse);

        PaymentEntity paymentResult = paymentService.process(
                createPaymentDTO
        );
        PaymentResponse response = paymentService.sendToGatewayClient(paymentResult, user.getCpf());

        assertNotNull(response);
        assertEquals(PaymentStatusType.ACCEPTED, response.status());
        assertNotNull(response.paidAt());
        verify(gatewayClient, times(1)).send(any(PaymentEntity.class), eq(user.getCpf()), any(UUID.class));
        verify(paymentUpdater, times(1)).updateByResponse(any(PaymentEntity.class), any(PaymentResponse.class));

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
                .id(UUID.randomUUID())
                .name("Test User")
                .email("test@example.com")
                .password("password")
                .cpf("12345678900")
                .build();

        EnrollmentEntity enrollment = EnrollmentEntity.builder()
                .id(UUID.randomUUID())
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
                .id(UUID.randomUUID())
                .status(PaymentStatusType.PENDING)
                .amount(createPaymentDTO.amount())
                .paymentMethod(createPaymentDTO.paymentMethod())
                .enrollmentId(enrollment)
                .idempotencyKey(UUID.randomUUID())
                .build();



        PaymentResponse gatewayResponse = PaymentResponse.builder()
                .gatewayPaymentId(UUID.randomUUID())
                .status(PaymentStatusType.PENDING)
                .paymentMethod(PaymentMethodType.PIX)
                .paidAt(null)
                .build();



        when(idempotencyKeyProvider.generate()).thenReturn(UUID.randomUUID());
        when(paymentUpdater.savePending(any(CreatePaymentDTO.class), any(UUID.class))).thenReturn(payment);
        when(gatewayClient.send(any(PaymentEntity.class), eq(user.getCpf()), any(UUID.class))).thenReturn(gatewayResponse);

        PaymentEntity paymentResult = paymentService.process(
                createPaymentDTO
        );

        PaymentResponse response = paymentService.sendToGatewayClient(paymentResult, user.getCpf());

        assertNotNull(response);
        assertEquals(PaymentStatusType.PENDING, response.status());
        assertNull(response.paidAt());
        verify(gatewayClient, times(1)).send(any(PaymentEntity.class), eq(user.getCpf()), any(UUID.class));
        verify(paymentUpdater, times(1)).updateByResponse(any(PaymentEntity.class), any(PaymentResponse.class));

    }
}