package br.com.issler.azura_api.services;

import br.com.issler.azura_api.clients.GatewayClient;
import br.com.issler.azura_api.clients.enums.GatewayErrorType;
import br.com.issler.azura_api.clients.exceptions.GatewayServiceException;
import br.com.issler.azura_api.database.models.PaymentEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles(profiles = "test")
@Transactional
public class GatewayClientConnectTimeoutTest {

    @Autowired
    private GatewayClient gatewayClient;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry){
        registry.add("spring.external.gateway-url", () -> "http://192.0.2.1:80");
    }

    @Test
    @DisplayName("Should return connect timeout exception")
    void returnConnectTimeoutException(){
        GatewayServiceException exception = assertThrows(GatewayServiceException.class, () -> {
            gatewayClient.send(new PaymentEntity(), "00000000000");
        });

        assertEquals(GatewayErrorType.CONNECT_TIMEOUT, exception.getType());
    }
}
