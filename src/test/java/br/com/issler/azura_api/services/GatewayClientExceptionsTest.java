package br.com.issler.azura_api.services;

import br.com.issler.azura_api.clients.GatewayClient;
import br.com.issler.azura_api.clients.enums.GatewayErrorType;
import br.com.issler.azura_api.clients.exceptions.GatewayServiceException;
import br.com.issler.azura_api.database.models.PaymentEntity;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest
@ActiveProfiles(profiles = "test")
@Transactional
public class GatewayClientExceptionsTest {

    @Autowired
    private GatewayClient gatewayClient;

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort().containerThreads(10))
            .build();

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry){
        registry.add("spring.external.gateway-url", wireMock::baseUrl);
    }

    @Test
    @DisplayName("Should return read timeout exception")
    void returnReadTimeoutException(){
        wireMock.stubFor(
                post(urlEqualTo("/v1/payments")).willReturn(
                        aResponse().withStatus(200).withFixedDelay(2000)
                )
        );

        GatewayServiceException exception = assertThrows(GatewayServiceException.class, () -> {
            gatewayClient.send(new PaymentEntity(), "00000000000");
        });

        assertEquals(GatewayErrorType.READ_TIMEOUT, exception.getType());
    }

    @Test
    @DisplayName("Should return rate limit exception")
    void returnRateLimitException(){
        wireMock.stubFor(
                post(urlEqualTo("/v1/payments")).willReturn(
                        aResponse().withStatus(429)
                )
        );

        GatewayServiceException exception = assertThrows(GatewayServiceException.class, () -> {
            gatewayClient.send(new PaymentEntity(), "00000000000");
        });

        assertEquals(GatewayErrorType.RATE_LIMITED, exception.getType());
    }

    @Test
    @DisplayName("Should return client error exception")
    void returnClientErrorException(){
        wireMock.stubFor(
                post(urlEqualTo("/v1/payments")).willReturn(
                        aResponse().withStatus(400)
                )
        );

        GatewayServiceException exception = assertThrows(GatewayServiceException.class, () -> {
            gatewayClient.send(new PaymentEntity(), "00000000000");
        });

        assertEquals(GatewayErrorType.CLIENT_ERROR, exception.getType());
    }

    @Test
    @DisplayName("Should return service unavailable exception")
    void returnServiceUnavailableException(){
        wireMock.stubFor(
                post(urlEqualTo("/v1/payments")).willReturn(
                        aResponse().withStatus(500)
                )
        );

        GatewayServiceException exception = assertThrows(GatewayServiceException.class, () -> {
            gatewayClient.send(new PaymentEntity(), "00000000000");
        });

        assertEquals(GatewayErrorType.SERVICE_UNAVAILABLE, exception.getType());
    }
}
