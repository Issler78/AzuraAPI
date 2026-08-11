package br.com.issler.azura_api.services;

import br.com.issler.azura_api.database.models.CategoryEntity;
import br.com.issler.azura_api.database.models.CourseEntity;
import br.com.issler.azura_api.database.models.PaymentEntity;
import br.com.issler.azura_api.database.models.UserEntity;
import br.com.issler.azura_api.database.repositories.*;
import br.com.issler.azura_api.dtos.CreateEnrollmentDTO;
import br.com.issler.azura_api.dtos.EnrollmentCreateResult;
import br.com.issler.azura_api.dtos.GatewayPaymentResponse;
import br.com.issler.azura_api.dtos.PaymentResponse;
import br.com.issler.azura_api.enums.PaymentMethodType;
import br.com.issler.azura_api.enums.PaymentStatusType;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles(profiles = "test")
@Transactional
class EnrollmentServiceIntegrationTest {

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort().containerThreads(10))
            .build();

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry){
        registry.add("spring.application.gateway-url", wireMock::baseUrl);
    }

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private IEnrollmentRepository enrollmentRepository;

    @Autowired
    private IPaymentRepository paymentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ICategoryRepository categoryRepository;
    @Autowired
    private ICourseRepository courseRepository;
    @Autowired
    private IUserRepository userRepository;

    @Test
    @DisplayName("Should save enrollment and payment successfully with credit card payment method, passing through the payment gateway (mocked)")
    void saveCaseSuccessWithCreditCardAsPaymentMethod() throws Exception {
        // configure bd with fake data
        CategoryEntity category = categoryRepository.save(CategoryEntity.builder()
                .name("Test Category")
                .build());

        CourseEntity course = courseRepository.save(CourseEntity.builder()
                .title("Test Course")
                .description("Test Description")
                .price(new BigDecimal("100.00"))
                .category(category)
                .build());

        UserEntity user = userRepository.save(UserEntity.builder()
                .name("Test User")
                .email("test@example.com")
                .password("testpassword")
                .cpf("12345678900")
                .build());



        // configure wire mock when call the gateway
        GatewayPaymentResponse response = GatewayPaymentResponse.builder()
                .gatewayPaymentId(new UUID(1L, 2L))
                .amount(new BigDecimal("100.00"))
                .status(PaymentStatusType.ACCEPTED)
                .paymentMethod(PaymentMethodType.CREDIT_CARD)
                .paidAt(LocalDateTime.now()).build();
        String jsonResponse = objectMapper.writeValueAsString(response);

        wireMock.stubFor(
            post(urlEqualTo("/v1/payments")).willReturn(okJson(jsonResponse))
        );



        CreateEnrollmentDTO dto = CreateEnrollmentDTO.builder()
                .courseId(course.getId())
                .paymentMethod(PaymentMethodType.CREDIT_CARD)
                .build();
        EnrollmentCreateResult result = enrollmentService.save(dto, user);
        PaymentResponse paymentResponse = result.paymentResponse();


        assertEquals(1, enrollmentRepository.count());
        wireMock.verify(postRequestedFor(urlEqualTo("/v1/payments")));
        assertEquals(1, paymentRepository.count());
        PaymentEntity payment = paymentRepository.findByGatewayPaymentId(response.gatewayPaymentId()).orElse(null);
        assertNotNull(payment);
        assertEquals(response.gatewayPaymentId(), payment.getGatewayPaymentId());
        assertNotNull(paymentResponse);
    }

    @Test
    @DisplayName("Should save enrollment and payment successfully with pix payment method, passing through the payment gateway (mocked)")
    void saveCaseSuccessWithPixAsPaymentMethod() throws Exception {
        // configure bd with fake data
        CategoryEntity category = categoryRepository.save(CategoryEntity.builder()
                .name("Test Category")
                .build());

        CourseEntity course = courseRepository.save(CourseEntity.builder()
                .title("Test Course")
                .description("Test Description")
                .price(new BigDecimal("100.00"))
                .category(category)
                .build());

        UserEntity user = userRepository.save(UserEntity.builder()
                .name("Test User")
                .email("test@example.com")
                .password("testpassword")
                .cpf("12345678900")
                .build());



        // configure wire mock when call the gateway
        GatewayPaymentResponse response = GatewayPaymentResponse.builder()
                .gatewayPaymentId(new UUID(1L, 2L))
                .amount(new BigDecimal("100.00"))
                .status(PaymentStatusType.PENDING)
                .paymentMethod(PaymentMethodType.PIX)
                .paidAt(LocalDateTime.now()).build();
        String jsonResponse = objectMapper.writeValueAsString(response);

        wireMock.stubFor(
                post(urlEqualTo("/v1/payments")).willReturn(okJson(jsonResponse))
        );



        CreateEnrollmentDTO dto = CreateEnrollmentDTO.builder()
                .courseId(course.getId())
                .paymentMethod(PaymentMethodType.PIX)
                .build();
        EnrollmentCreateResult result = enrollmentService.save(dto, user);
        PaymentResponse paymentResponse = result.paymentResponse();


        assertEquals(1, enrollmentRepository.count());
        wireMock.verify(postRequestedFor(urlEqualTo("/v1/payments")));
        assertEquals(1, paymentRepository.count());
        PaymentEntity payment = paymentRepository.findByGatewayPaymentId(response.gatewayPaymentId()).orElse(null);
        assertNotNull(payment);
        assertEquals(response.gatewayPaymentId(), payment.getGatewayPaymentId());
        assertNotNull(paymentResponse);
    }
}