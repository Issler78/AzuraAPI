package br.com.issler.azura_api.clients;

import br.com.issler.azura_api.database.models.PaymentEntity;
import br.com.issler.azura_api.dtos.GatewayPaymentResponse;
import br.com.issler.azura_api.dtos.GatewayRequestBody;
import br.com.issler.azura_api.dtos.PaymentResponse;
import br.com.issler.azura_api.exceptions.GatewayBadRequest;
import br.com.issler.azura_api.exceptions.GatewayUnavailableException;
import br.com.issler.azura_api.exceptions.GatewayUnexpectedResponseException;
import br.com.issler.azura_api.mappers.IPaymentMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;

@Component
public class GatewayClient {
    private final RestClient restClient;
    private final String gatewayKey;
    private final IPaymentMapper paymentMapper;

    public GatewayClient(
            @Value("${spring.application.gateway-url}")
            String gatewayUrl,
            @Value("${spring.application.gateway-key}")
            String gatewayKey,
            IPaymentMapper paymentMapper
    ){
        this.gatewayKey = gatewayKey;
        this.paymentMapper = paymentMapper;

        HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(client);
        this.restClient = RestClient.builder()
                .baseUrl(gatewayUrl)
                .requestFactory(requestFactory)
                .build();
    }

    public PaymentResponse send(PaymentEntity payment, String payerCpf){
        // gatewayPaymentResponse and paymentResponse are the same, but we need to map them because the gateway response has a different name for the fields in the future
        GatewayRequestBody gatewayRequestBody = GatewayRequestBody.builder()
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .payerCpf(payerCpf)
                .build();

        try {
            GatewayPaymentResponse result = restClient.post()
                    .uri("/v1/payments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-API-KEY", gatewayKey)
                    .body(gatewayRequestBody)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (request, response) -> {
                        if(response.getStatusCode().is5xxServerError()){
                            throw new GatewayUnavailableException("Gateway is unavailable: " + response.getStatusCode().value());
                        }

                        if(response.getStatusCode().value() == 429){
                            throw new GatewayUnavailableException("Rate limit exceeded: " + response.getStatusCode().value());
                        }

                        if(response.getStatusCode().is4xxClientError()){
                            throw new GatewayBadRequest("Gateway rejected the request: " + response.getBody());
                        }

                        throw new RuntimeException("Error occurred while sending request to gateway: " + response.getBody());
                    })
                    .body(GatewayPaymentResponse.class);


            if(result == null || result.gatewayPaymentId() == null) {
                throw new GatewayUnexpectedResponseException("Gateway returned a null response");
            }

            return paymentMapper.toPaymentResponse(result);
        } catch (ResourceAccessException e){
            throw new GatewayUnavailableException("Failed to communicate with the gateway: " + e.getMessage());
        }
    }
}
