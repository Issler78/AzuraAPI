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

import java.io.IOException;
import java.net.ServerSocket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles(profiles = "test")
@Transactional
public class GatewayClientConnectionFailureExceptionTest {
    @Autowired
    private GatewayClient gatewayClient;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry){
        int port;
        try (ServerSocket server = new ServerSocket(0)) {
            port = server.getLocalPort();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        registry.add("spring.external.gateway-url", () -> "http://localhost:" + port);
    }

    @Test
    @DisplayName("Should return connection failure exception")
    void returnConnectionFailureException(){
        GatewayServiceException exception = assertThrows(GatewayServiceException.class, () -> {
            gatewayClient.send(new PaymentEntity(), "00000000000");
        });

        assertEquals(GatewayErrorType.CONNECTION_FAILURE, exception.getType());
    }
}
